package hx.minepainter.sculpture;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;

@SideOnly(Side.CLIENT)
public class SculptureRenderCompiler {
	public static RenderBlocks rb = new RenderBlocks();

	int glDisplayList = -1;
	int light;
	
	public void update(int light, BlockSlice slice){
		if(glDisplayList != -1 && light == this.light)return;
		
		glDisplayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(glDisplayList, GL11.GL_COMPILE);
		build(slice);
		GL11.glEndList();
		
		this.light = light;
	}
	
	
	public void build(BlockSlice slice){
		rb.blockAccess = slice;
		rb.setRenderBounds(0d, 0d, 0d, 1d, 1d, 1d);
		rb.renderAllFaces = false;
		
		TextureManager tm = Minecraft.getMinecraft().renderEngine;
		tm.bindTexture(TextureMap.locationBlocksTexture);
		
		Tessellator tes = Tessellator.instance;
		tes.startDrawingQuads();
		
		for(int i = 0; i < 512; i ++){
			int x = (i >> 8) & 7;
			int y = (i >> 4) & 7;
			int z = (i >> 0) & 7;

			rb.renderStandardBlock(slice.getBlock(x, y, z), x,y,z);
		}
		
		rb.blockAccess = null;
		tes.draw();
	}

	public void clear(){
		GL11.glDeleteLists(glDisplayList, 1);
	}
}
