package hx.minepainter.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SculptureRender implements ISimpleBlockRenderingHandler{

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelId,
			RenderBlocks renderer) {
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {
		
		GL11.glPushMatrix();
		GL11.glTranslated(x,y,z);
		GL11.glScalef(0.125f, 0.125f, 0.125f);
		//TODO : call gl list
		SculptureEntity se = (SculptureEntity) world.getTileEntity(x, y, z);
		se.render.update(block.getMixedBrightnessForBlock(world, x, y, z), BlockSlice.at(world, x, y, z));
		GL11.glCallList(se.render.glDisplayList);
		BlockSlice.clear();
		GL11.glPopMatrix();
		
		return false;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return false;
	}

	@Override
	public int getRenderId() {
		return 0;
	}

}
