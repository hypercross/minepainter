package hx.minepainter.sculpture;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;

public class Nail {

	byte flag = 0;
	
	public void readFrom(NBTTagCompound nbt){
		flag = nbt.getByte("nail_flag");
	}
	
	public void writeTo(NBTTagCompound nbt){
		nbt.setByte("nail_flag", flag);
	}
	
	public void setFace(int face){
		flag |= (1 << face);
	}
	
	public boolean isOnFace(int face){
		return (flag & (1 << face)) > 0;
	}
	
	private static Nail ALL = new Nail();
	static{
		ALL.flag = -1;
	}
	public static Nail fromSculpture(IBlockAccess iba, int x, int y, int z){
		//TODO implement nail save/load
		return ALL;
	}
}
