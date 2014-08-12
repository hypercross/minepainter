package hx.minepainter.painting;

import java.awt.image.BufferedImage;

import hx.minepainter.ModMinePainter;
import hx.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PaintTool extends Item implements IPainter{
	public PaintTool(){
		this.setCreativeTab(ModMinePainter.tabMinePainter);
	}

	public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
		
		PaintingEntity pe = Utils.getTE(w, x, y, z);
		return this.apply(pe.image, xs, ys, getColor(ep,is));
		
	}
	
	public int getColor(EntityPlayer ep, ItemStack is){
		return is.getItemDamage();
	}
	
	@Override
	public boolean apply(BufferedImage img, float xs, float ys, int color) {
		return false;
	}

}
