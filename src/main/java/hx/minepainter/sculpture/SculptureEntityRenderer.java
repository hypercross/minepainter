package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import hx.utils.Debug;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class SculptureEntityRenderer  extends TileEntitySpecialRenderer{

	@Override
	public void renderTileEntityAt(TileEntity var1, double xd, double yd,
			double zd, float partial) {
		SculptureEntity se = (SculptureEntity) var1;
	    
	    if(!se.getRender().ready() && !se.getRender().hasContext()){
	    	
	    	int lightX = (int) (OpenGlHelper.lastBrightnessX);
	    	int lightY = (int) (OpenGlHelper.lastBrightnessY);
	    	int light = lightY * 65536 + lightX; 
	    	
	    	se.getRender().initFromSculptureAndLight(se.sculpture(), light);
	    }
	    else
	    	se.updateRender();

		RenderHelper.disableStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslated(xd,yd,zd);
		
		int displayList = se.getRender().glDisplayList[0];
		if(displayList > 0)
			GL11.glCallList(displayList);
		
		displayList = se.getRender().glDisplayList[1];
		if(displayList > 0){
			GL11.glEnable(GL11.GL_BLEND);
		    OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		    GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
			GL11.glCallList(displayList);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		GL11.glPopMatrix();
		
//        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.enableStandardItemLighting();
	}

}
