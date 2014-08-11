package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SculptureRender implements ISimpleBlockRenderingHandler{

	private static int chunk_x,chunk_y,chunk_z;
	public static void setCurrentChunkPos(int x,int y,int z){
		chunk_x = x; chunk_y = y; chunk_z = z;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		SculptureEntity se = (SculptureEntity) world.getTileEntity(x, y, z);
		se.getRender().updateLight(block.getMixedBrightnessForBlock(world, x, y, z));
		se.getRender().updateAO(world, x, y, z);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		GL11.glTranslated(-chunk_x, -chunk_y, -chunk_z);		
		if(se.getRender().ready())GL11.glCallList(se.getRender().glDisplayList);
		GL11.glPopMatrix();
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ModMinePainter.sculpture.renderID;
	}

}
