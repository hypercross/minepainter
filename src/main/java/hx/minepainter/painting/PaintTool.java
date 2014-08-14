package hx.minepainter.painting;

import java.awt.image.BufferedImage;

import hx.minepainter.ModMinePainter;
import hx.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PaintTool extends Item{
	public PaintTool(){
		this.setCreativeTab(ModMinePainter.tabMinePainter);
	}

	public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
		
		PaintingEntity pe = Utils.getTE(w, x, y, z);
		if(pe == null)return false;
		float[] point = PaintingPlacement.of(w.getBlockMetadata(x, y, z)).block2painting(xs, ys, zs);
		boolean changed = this.apply(pe.image, point, getColor(ep,is));
		if(!w.isRemote)return changed;
		if(changed)pe.getIcon().fill(pe.image);
		return changed;
	}
	
	public int getColor(EntityPlayer ep, ItemStack is){
		return is.getItemDamage();
	}
	
	public boolean apply(BufferedImage img, float[] point, int color) {
		return false;
	}

	public static class Mini extends PaintTool{
		
		public Mini(){
			super();
			this.setUnlocalizedName("mini_brush");
			this.setTextureName("minepainter:brush_small");
		}
		
		@Override public boolean apply(BufferedImage img, float[] point, int color){
			
			int x = (int) (point[0] * 16);
			int y = (int) (point[1] * 16);
			
			img.setRGB(x, y, color);
//			img.getRaster().setPixel(x, y, new int[]{ (color >> 8) & 0xff, (color >> 4) & 0xff, color & 0xff});
			return true;
		}
	}
	
	public static class Mixer extends PaintTool{
		
	}
}
