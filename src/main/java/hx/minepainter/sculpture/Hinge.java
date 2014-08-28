package hx.minepainter.sculpture;

import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

public enum Hinge {
	
	Y0Z0(ForgeDirection.DOWN, 	ForgeDirection.NORTH),
	Y0Z1(ForgeDirection.DOWN, 	ForgeDirection.SOUTH),
	Y1Z0(ForgeDirection.UP, 	ForgeDirection.NORTH),
	Y1Z1(ForgeDirection.UP, 	ForgeDirection.SOUTH),

	Z0X0(ForgeDirection.NORTH, 	ForgeDirection.WEST),
	Z0X1(ForgeDirection.NORTH, 	ForgeDirection.EAST),
	Z1X0(ForgeDirection.SOUTH, 	ForgeDirection.WEST),
	Z1X1(ForgeDirection.SOUTH, 	ForgeDirection.EAST),

	X0Y0(ForgeDirection.WEST, 	ForgeDirection.DOWN),
	X0Y1(ForgeDirection.WEST, 	ForgeDirection.UP),
	X1Y0(ForgeDirection.EAST, 	ForgeDirection.DOWN),
	X1Y1(ForgeDirection.EAST, 	ForgeDirection.UP);
	
	private ForgeDirection dir1, dir2; 
	private static float mm = -0.1f, mM = 0.1f, Mm = 0.9f, MM = 1.1f;
	
	Hinge(ForgeDirection dir1, ForgeDirection dir2){
		this.dir1 = dir1;
		this.dir2 = dir2;
	}
	
	public int getRotationFace(ForgeDirection push){
		if(push == dir1 || push == dir2.getOpposite())return dir1.getRotation(dir2).ordinal();
		if(push == dir2 || push == dir1.getOpposite())return dir2.getRotation(dir1).ordinal();
		return -1;
	}
	
	public ForgeDirection getShift(ForgeDirection push){
		if(push == dir1 || push == dir2.getOpposite())return dir1;
		if(push == dir2 || push == dir1.getOpposite())return dir2;
		return null;
	}
	
	public Hinge getPushed(ForgeDirection push){
		if(push == dir1 || push == dir2.getOpposite())return rotate(2);
		if(push == dir2 || push == dir1.getOpposite())return rotate(1);
		return this;
	}
	
	private Hinge rotate(int count){
		int base = this.ordinal() & ~3;
		int partial = this.ordinal() & 3;
		return Hinge.values()[base + (partial+1) % 4];
	}
	
	/** get a hinge from sub-block x,y,z coords.
	 */
	public static Hinge placedAt(float x,float y,float z){
		float dx = Math.abs(x - 0.5f);
		float dy = Math.abs(y - 0.5f);
		float dz = Math.abs(z - 0.5f);
		
		int ordinal = 0;
		if(dx <= dy && dx <= dz){
			ordinal = 0;
			if(y > 0.5f)ordinal += 2;
			if(z > 0.5f)ordinal += 1;
		}
		else if(dy <= dx && dy <= dz){
			ordinal = 4;
			if(z > 0.5f)ordinal += 2;
			if(x > 0.5f)ordinal += 1;
		}
		else if(dz <= dy && dz <= dx){
			ordinal = 8;
			if(x > 0.5f)ordinal += 2;
			if(y > 0.5f)ordinal += 1;
		}
		
		return Hinge.values()[ordinal];
	}
	
	public static Hinge fromSculpture(IBlockAccess iba, int x,int y,int z){
		//TODO implement hinge save/load
		return Hinge.Z0X0;
	}
}
