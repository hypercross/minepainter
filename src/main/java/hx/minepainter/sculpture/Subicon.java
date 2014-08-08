package hx.minepainter.sculpture;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;

public class Subicon implements IIcon{

	public IIcon icon;
	private float umin,umax,vmin,vmax;
	
	public void setSubcoord(int x,int y,int z,int face){
		
		ForgeDirection dir = ForgeDirection.getOrientation(face);			
		if(dir.offsetX == 1)setIndex(7-z,7-y);
		else if(dir.offsetX == -1)setIndex(z,7-y);
		else if(dir.offsetZ == 1)setIndex(x,7-y);
		else if(dir.offsetZ == -1)setIndex(7-x,7-y);
		else if(dir.offsetY == 1)setIndex(x,z);
		else setIndex(x,z);
	}
	
	private void setIndex(int x,int y){
		
		umin = icon.getMinU() + (icon.getMaxU() - icon.getMinU())/8 * x;
		umax = umin + (icon.getMaxU() - icon.getMinU())/8;
		vmin = icon.getMinV() + (icon.getMaxV() - icon.getMinV())/8 * y;
		vmax = vmin + (icon.getMaxV() - icon.getMinV())/8;
	}
	
	@Override
	public int getIconWidth() {
		return icon.getIconHeight();
	}

	@Override
	public int getIconHeight() {
		return icon.getIconHeight();
	}

	@Override
	public float getMinU() {
		return umin;
	}

	@Override
	public float getMaxU() {
		return umax;
	}

	@Override
	public float getInterpolatedU(double d0) {
		return (float)(umin + (umax - umin) * d0 / 16d);
	}

	@Override
	public float getMinV() {
		return vmin;
	}

	@Override
	public float getMaxV() {
		return vmax;
	}

	@Override
	public float getInterpolatedV(double d0) {
		return (float)(vmin + (vmax - vmin) * d0 / 16d);
	}

	@Override
	public String getIconName() {
		return icon.getIconName();
	}
}
