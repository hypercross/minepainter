package hx.minepainter.item;

import org.lwjgl.opengl.GL11;

import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.ExpirablePool;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureBlock;
import hx.minepainter.sculpture.SculptureRenderBlocks;
import hx.utils.Debug;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;

@SideOnly(Side.CLIENT)
public class DroppedSculptureRenderer implements IItemRenderer{
	SculptureRenderBlocks rb = new SculptureRenderBlocks();
	RenderItem renderItem = new RenderItem();
	ItemStack is;

	ExpirablePool<ItemStack, CompiledRender> renders = new ExpirablePool<ItemStack, CompiledRender>(12){

		@Override
		public void release(CompiledRender v) {
			v.clear();
		}

		@Override
		public CompiledRender get() {
			return new CompiledRender();
		}
		
	};
	
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
//		return false;
		return type == ItemRenderType.INVENTORY ||
				type == ItemRenderType.ENTITY ||
				type == ItemRenderType.EQUIPPED ||
				type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return type == ItemRenderType.ENTITY || helper == ItemRendererHelper.EQUIPPED_BLOCK;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		CompiledRender cr = renders.get(item);
		if(!cr.compiled(type))cr.compile(item.getTagCompound(),type,data);
		if(!cr.compiled(type))return;
		TextureManager tm = Minecraft.getMinecraft().renderEngine;
		tm.bindTexture(TextureMap.locationBlocksTexture);
		GL11.glCallList(cr.glDispList);
	}

	private class CompiledRender{
		
		int glDispList = -1;
		ItemRenderType type = null;
		Sculpture sculpture = new Sculpture();
		
		public boolean compiled(ItemRenderType type){
			return glDispList >= 0 && this.type == type;
		}
		
		public void clear(){
			if(glDispList >= 0)
				GLAllocation.deleteDisplayLists(glDispList);
		}
		
		public void compile(NBTTagCompound nbt, ItemRenderType type, Object... data){
			this.type = type;
			if(nbt == null)return;
			sculpture.read(nbt);
			
			if(glDispList < 0)glDispList = GLAllocation.generateDisplayLists(1);
			if(is == null)is = new ItemStack(ModMinePainter.sculpture.block);
			
			GL11.glNewList(glDispList, GL11.GL_COMPILE);
			TextureManager tm = Minecraft.getMinecraft().renderEngine;
			tm.bindTexture(TextureMap.locationBlocksTexture);
			SculptureBlock sb = ModMinePainter.sculpture.block;
			
			if(type == ItemRenderType.INVENTORY){
				RenderHelper.enableGUIStandardItemLighting();
			}else if(type == ItemRenderType.EQUIPPED ||
				type == ItemRenderType.EQUIPPED_FIRST_PERSON){
				GL11.glTranslatef(0.5f, 0.5f, 0.5f);
			}else if(type == ItemRenderType.ENTITY){
				GL11.glTranslatef(0, 0.5f, 0f);
			}
			
			for(int i = 0; i < 512; i ++){
				int x = (i >> 6) & 7;
				int y = (i >> 3) & 7;
				int z = (i >> 0) & 7;
				
				if(sculpture.getBlockAt(x, y, z, null) == Blocks.air)continue;
				
				sb.setCurrentBlock(sculpture.getBlockAt(x, y, z, null), sculpture.getMetaAt(x, y, z, null));
				sb.setBlockBounds(x/8f, y/8f, z/8f, (x+1)/8f, (y+1)/8f, (z+1)/8f);
				
				if(type == ItemRenderType.INVENTORY){
//					renderItem.renderItemIntoGUI(
//							Minecraft.getMinecraft().fontRenderer,
//							Minecraft.getMinecraft().renderEngine, is, 0, 0);
					GL11.glPushMatrix();
			        GL11.glEnable(GL11.GL_BLEND);
			        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		            GL11.glTranslatef(-2f, 3f, -3.0F + 50);
		            GL11.glScalef(10.0F, 10.0F, 10.0F);
		            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
		            GL11.glScalef(1.0F, 1.0F, -1.0F);
		            GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
		            GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		            GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
					rb.cull(sculpture, x, y, z);
		            rb.renderBlockAsItem(sb, 0, 1.0F);
		            GL11.glEnable(GL11.GL_CULL_FACE);
		            GL11.glPopMatrix();
				}else {
					GL11.glPushMatrix();
					rb.cull(sculpture, x, y, z);
					rb.renderBlockAsItem(sb, 0, 1f);
					GL11.glPopMatrix();
				}
			}
			
			GL11.glEndList();
			
			sb.setCurrentBlock(null, 0);
			sb.setBlockBounds(0,0,0,1,1,1);
		}
	}
}
