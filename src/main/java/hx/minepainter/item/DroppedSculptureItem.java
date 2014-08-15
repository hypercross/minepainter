package hx.minepainter.item;

import hx.minepainter.sculpture.Sculpture;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DroppedSculptureItem extends Item{

	
	public void readTo(ItemStack is, Sculpture sculpture){
		if(is.hasTagCompound())
			sculpture.read(is.getTagCompound());
	}
	
}
