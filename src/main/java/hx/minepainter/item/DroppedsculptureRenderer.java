package hx.minepainter.item;

import org.lwjgl.opengl.GL11;

import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.ExpirablePool;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureBlock;
import hx.utils.Debug;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
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
	RenderBlocks rb = new RenderBlocks();
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
		return type == ItemRenderType.ENTITY;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		CompiledRender cr = renders.get(item);
		if(!cr.compiled(type))cr.compile(item.getTagCompound(),type,data);
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
			sculpture.read(nbt);
			
			if(glDispList < 0)glDispList = GLAllocation.generateDisplayLists(1);
			if(is == null)is = new ItemStack(ModMinePainter.sculpture.block);
			
			GL11.glNewList(glDispList, GL11.GL_COMPILE);
			TextureManager tm = Minecraft.getMinecraft().renderEngine;
			tm.bindTexture(TextureMap.locationBlocksTexture);
			SculptureBlock sb = ModMinePainter.sculpture.block;
			
			if(type == ItemRenderType.INVENTORY){
				RenderHelper.enableGUIStandardItemLighting();
			}
			
			for(int i = 0; i < 512; i ++){
				int x = (i >> 6) & 7;
				int y = (i >> 3) & 7;
				int z = (i >> 0) & 7;
				
				if(sculpture.getBlockAt(x, y, z, null) == Blocks.air)continue;
				
				sb.setCurrentBlock(sculpture.getBlockAt(x, y, z, null), sculpture.getMetaAt(x, y, z, null));
				sb.setBlockBounds(x/8f, y/8f, z/8f, (x+1)/8f, (y+1)/8f, (z+1)/8f);
				
				if(type == ItemRenderType.INVENTORY){
					renderItem.renderItemIntoGUI(
							Minecraft.getMinecraft().fontRenderer,
							Minecraft.getMinecraft().renderEngine, is, 0, 0);
				}else if(type == ItemRenderType.ENTITY){
					GL11.glPushMatrix();
					rb.renderBlockAsItem(sb, 0, 1f);
					GL11.glPopMatrix();
//					renderBlockAsItem(sb, rb);
				}
				else 
					Minecraft.getMinecraft().entityRenderer.itemRenderer.renderItem((EntityLivingBase) data[1],
							is, 0, type);
			}
			
			GL11.glEndList();
			
			sb.setCurrentBlock(null, 0);
			sb.setBlockBounds(0,0,0,1,1,1);
		}
		
		private void renderBlockAsItem(Block par1Block,RenderBlocks renderer){
			Tessellator var4 = Tessellator.instance;
			int par2 = 0;
			
//			GL11.glPushMatrix();
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
	        var4.startDrawingQuads();
	        var4.setNormal(0.0F, -1.0F, 0.0F);
	        renderer.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getIcon(0, par2));
	        var4.draw();
	        var4.startDrawingQuads();
	        var4.setNormal(0.0F, 1.0F, 0.0F);
	        renderer.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getIcon(1, par2));
	        var4.draw();
	        var4.startDrawingQuads();
	        var4.setNormal(0.0F, 0.0F, -1.0F);
	        renderer.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getIcon(2, par2));
	        var4.draw();
	        var4.startDrawingQuads();
	        var4.setNormal(0.0F, 0.0F, 1.0F);
	        renderer.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getIcon(3, par2));
	        var4.draw();
	        var4.startDrawingQuads();
	        var4.setNormal(-1.0F, 0.0F, 0.0F);
	        renderer.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getIcon(4, par2));
	        var4.draw();
	        var4.startDrawingQuads();
	        var4.setNormal(1.0F, 0.0F, 0.0F);
	        renderer.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getIcon(5, par2));
	        var4.draw();
	        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
//	        GL11.glPopMatrix();
	        
		}
	}
}
