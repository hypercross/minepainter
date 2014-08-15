package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.sculpture.Sculpture;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DroppedSculptureItem extends Item{

	
	public void readTo(ItemStack is, Sculpture sculpture){
		if(is.hasTagCompound())
			sculpture.read(is.getTagCompound());
	}
	
	@SideOnly(Side.CLIENT)
	@Override public void registerIcons(IIconRegister r){}
	
	@Override public boolean getShareTag(){
        return true;
    }
}
