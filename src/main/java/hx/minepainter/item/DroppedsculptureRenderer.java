package hx.minepainter.item;

import org.lwjgl.opengl.GL11;

import hx.minepainter.painting.ExpirablePool;
import hx.minepainter.sculpture.Sculpture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public class DroppedsculptureRenderer implements IItemRenderer{
	RenderBlocks rb = new RenderBlocks();

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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		
	}

	private class CompiledRender{
		
		int glDispList = -1;
		Sculpture sculpture = new Sculpture();
		
		public boolean compiled(){
			return glDispList >= 0;
		}
		
		public void clear(){
			if(compiled())
				GL11.glDeleteLists(glDispList, 1);
		}
		
		public void compile(NBTTagCompound nbt){
			sculpture.read(nbt);
			
			if(glDispList < 0)glDispList = GLAllocation.generateDisplayLists(1);
			
			GL11.glNewList(glDispList, GL11.GL_COMPILE);
			
			
			
			GL11.glEndList();
		}
		
	}
}
