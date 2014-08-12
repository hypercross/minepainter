package hx.minepainter.painting;

import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.Operations;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public enum PaintingPlacement {

	UPXNEG(ForgeDirection.UP, ForgeDirection.WEST),
	UPXPOS(ForgeDirection.UP, ForgeDirection.EAST),
	UPZNEG(ForgeDirection.UP, ForgeDirection.NORTH),
	UPZPOS(ForgeDirection.UP, ForgeDirection.SOUTH),
	
	XNEG(ForgeDirection.WEST, ForgeDirection.DOWN),
	XPOS(ForgeDirection.EAST, ForgeDirection.DOWN),
	ZNEG(ForgeDirection.NORTH, ForgeDirection.DOWN),
	ZPOS(ForgeDirection.SOUTH, ForgeDirection.DOWN),
	
	DOWNXNEG(ForgeDirection.DOWN, ForgeDirection.WEST),
	DOWNXPOS(ForgeDirection.DOWN, ForgeDirection.EAST),
	DOWNZNEG(ForgeDirection.DOWN, ForgeDirection.NORTH),
	DOWNZPOS(ForgeDirection.DOWN, ForgeDirection.SOUTH);
	
	public static PaintingPlacement of(int id){
		return values()[id % values().length];
	}
	
	public static PaintingPlacement of(Vec3 vec){
		double x = Math.abs(vec.xCoord), y = Math.abs(vec.yCoord), z = Math.abs(vec.zCoord);
		
		if(x > y && x > z)
			return vec.xCoord > 0 ? XNEG : XPOS;
		if(z > x && z > y)
			return vec.zCoord > 0 ? ZNEG : ZPOS;
			
		if(vec.yCoord > 0){
			if(x > z)
				return vec.xCoord > 0 ? DOWNXNEG : DOWNXPOS;
			return vec.zCoord > 0 ? DOWNZNEG: DOWNZPOS;
		}
			 
		if(x > z)
			return vec.xCoord > 0 ? UPXNEG : UPXPOS;
		return vec.zCoord > 0 ? UPZNEG : UPZPOS;
			
	}
	
	ForgeDirection normal, ypos, xpos;

	PaintingPlacement(ForgeDirection normal, ForgeDirection ypos){
		this.normal = normal;
		this.ypos = ypos;
		this.xpos = normal.getRotation(ypos);
	}
	
	
	public float[] painting2block(float x,float y){
		float[] point = new float[3];
		point[0] = xpos.offsetX * x + ypos.offsetX * y + (1 - normal.offsetX)/2;
		point[1] = xpos.offsetY * x + ypos.offsetY * y + (1 - normal.offsetY)/2;
		point[2] = xpos.offsetZ * x + ypos.offsetZ * y + (1 - normal.offsetZ)/2;
		return point;
	}
	
	public float[] block2painting(float x,float y,float z){
		float [] point = new float[2];
		point[0] = xpos.offsetX * x + xpos.offsetY * y + xpos.offsetZ * z;
		point[1] = ypos.offsetX * x + ypos.offsetY * y + ypos.offsetZ * z;
		return point;
	}
}
