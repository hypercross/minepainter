package hx.minepainter.painting;

import hx.utils.Debug;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

public class PaintingRenderer extends TileEntitySpecialRenderer{

	@Override
	public void renderTileEntityAt(TileEntity entity, double x, double y,
			double z, float partial) {

		PaintingEntity pe = (PaintingEntity)entity;
		Tessellator tes = Tessellator.instance;
		PaintingIcon icon = pe.getIcon();
		PaintingPlacement placement = PaintingPlacement.of(pe.getBlockMetadata());

		GL11.glPushMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, icon.sheet.glTexId);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_BLEND);
		GL11.glTranslated(x+1/16f, y+1/16f, z+1/16f);
		GL11.glScalef(0.875f, 0.875f, 0.875f);
		tes.startDrawingQuads();
		float[] pos = placement.painting2block(0, 0);
		tes.addVertexWithUV(pos[0], pos[1], pos[2], icon.getMinU(), icon.getMinV());
		pos = placement.painting2block(0, 1);
		tes.addVertexWithUV(pos[0], pos[1], pos[2], icon.getMinU(), icon.getMaxV());
		pos = placement.painting2block(1, 1);
		tes.addVertexWithUV(pos[0], pos[1], pos[2], icon.getMaxU(), icon.getMaxV());
		pos = placement.painting2block(1, 0);
		tes.addVertexWithUV(pos[0], pos[1], pos[2], icon.getMaxU(), icon.getMinV());
		tes.draw();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

}
