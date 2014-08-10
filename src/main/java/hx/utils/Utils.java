package hx.utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class Utils {

	
	public static <T extends TileEntity> T getTE(IBlockAccess iba, int x,int y,int z){
		return (T) iba.getTileEntity(x, y, z);
	}
	
	public static <T extends Item> T getItem(ItemStack is){
		return (T) is.getItem();
	}
}
