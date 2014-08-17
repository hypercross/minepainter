package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import hx.utils.Debug;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class SculptureEntityRenderer  extends TileEntitySpecialRenderer{

	@Override
	public void renderTileEntityAt(TileEntity var1, double xd, double yd,
			double zd, float partial) {
		SculptureEntity se = (SculptureEntity) var1;
		Block block = ModMinePainter.sculpture.block;
		World world = se.getWorldObj();
		int x = (int) xd, y = (int) yd, z = (int) zd;
//		se.updateEntity();

		RenderHelper.disableStandardItemLighting();
		GL11.glShadeModel(GL11.GL_SMOOTH);
	    GL11.glEnable(GL11.GL_BLEND);
	    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	    
		GL11.glPushMatrix();
		GL11.glTranslated(xd,yd,zd);		
		if(se.getRender().ready())GL11.glCallList(se.getRender().glDisplayList);
		GL11.glPopMatrix();
		
		GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
	}

}
