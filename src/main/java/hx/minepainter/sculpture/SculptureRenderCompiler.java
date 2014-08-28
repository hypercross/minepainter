package hx.minepainter.sculpture;

import java.lang.reflect.Field;

import hx.minepainter.ModMinePainter;
import hx.utils.Debug;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class SculptureRenderCompiler {
	public static RenderBlocks rb = new SculptureRenderBlocks();

	int glDisplayList = -1;
	int light;
	public boolean changed = true;
	boolean context = false;
	float[][][] neighborAO = new float[3][3][3];
	
	public void updateAO(IBlockAccess w, int x,int y,int z){
		for(int i = 0 ; i < 27; i ++){
			int dx = i%3;
			int dy = (i/3)%3;
			int dz = (i/9)%3;
			
			float ao = w.getBlock(x+dx-1, y+dy-1, z+dz-1).getAmbientOcclusionLightValue();
			if(ao != neighborAO[dx][dy][dz]){
				changed = true;
				neighborAO[dx][dy][dz] = ao;
			}
		}
		context = true;
	}
	
	public void updateLight(int light){
		if(light != this.light)
			changed = true;
		this.light = light;
		context = true;
	}	
	
	public boolean hasContext(){
		return context;
	}
	
	public boolean update(BlockSlice slice){
		if(glDisplayList != -1 && !changed)return false;
		
		if(glDisplayList < 0)glDisplayList = GLAllocation.generateDisplayLists(1);
		GL11.glPushMatrix();
		GL11.glNewList(glDisplayList, GL11.GL_COMPILE);
		build(slice);
		GL11.glEndList();
		GL11.glPopMatrix();
		
		changed = false;
		return true;
	}
	
	public void build(BlockSlice slice){
		rb.blockAccess = slice;
		rb.renderAllFaces = false;
		SculptureBlock sculpture = ModMinePainter.sculpture.block;
		
		TextureManager tm = Minecraft.getMinecraft().renderEngine;
		tm.bindTexture(TextureMap.locationBlocksTexture);
		
		Tessellator tes = Tessellator.instance;
		double[] offs = getTesOffsets();
		tes.setTranslation(0, 0, 0);
		tes.startDrawingQuads();
		
		for(int i = 0; i < 512; i ++){
			int x = (i >> 6) & 7;
			int y = (i >> 3) & 7;
			int z = (i >> 0) & 7;

			Block b = slice.getBlock(x, y, z);
			if(b == Blocks.air)continue;
			int meta = slice.getBlockMetadata(x, y, z);
			sculpture.setCurrentBlock(b, meta);
			
			tes.setTranslation(-x, -y, -z);
			sculpture.setBlockBounds(x/8f, y/8f, z/8f, (x+1)/8f, (y+1)/8f, (z+1)/8f);
			rb.renderBlockByRenderType(sculpture, x,y,z);
		}
		
		Hinge hinge = Hinge.fromSculpture((SculptureEntity) slice.getTileEntity(0, 0, 0));
		if(hinge != null){
			hinge.setRenderBounds(sculpture);
			sculpture.setCurrentBlock(Blocks.iron_block, 0);
			tes.setTranslation(0, 0, 0);
			rb.setRenderBoundsFromBlock(sculpture);
			rb.renderAllFaces = true;
			rb.renderStandardBlockWithColorMultiplier(sculpture, 0,0,0, 1f,1f,1f);
		}
		
		sculpture.setCurrentBlock(null,0);
		sculpture.setBlockBounds(0,0,0,1,1,1);
		rb.blockAccess = null;
		tes.draw();
		tes.setTranslation(offs[0], offs[1], offs[2]);
	}

	public void clear(){
		if(glDisplayList>=0)
			GL11.glDeleteLists(glDisplayList, 1);
	}


	public boolean ready() {
		return glDisplayList>=0;
	}
	
	private double[] getTesOffsets(){
		double[] off = new double[3];
		
		int count = 0;
		int xoff = 0;
		Field[] fields = Tessellator.class.getDeclaredFields();
		for(int i = 0;i < fields.length; i++)
			if(fields[i].getType() == double.class){
				count++;
				if(count == 3)xoff = i-2;
			}else
				count = 0;
		
		off[0] = ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.instance, xoff  );
		off[1] = ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.instance, xoff+1);
		off[2] = ObfuscationReflectionHelper.getPrivateValue(Tessellator.class, Tessellator.instance, xoff+2);
		
		return off;
	}

	public void initFromSculptureAndLight(Sculpture sculpture,int light) {
		this.update(BlockSlice.of(sculpture, light));
	}
	
}
