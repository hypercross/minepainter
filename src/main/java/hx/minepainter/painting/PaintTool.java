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
	private static final int MASK_ALPHA = 0xff000000;
	private static final int MASK_RED   = 0x00ff0000;
	private static final int MASK_GREEN = 0x0000ff00;
	private static final int MASK_BLUE  = 0x000000ff;
	
	public PaintTool(){
		this.setCreativeTab(ModMinePainter.tabMinePainter);
	}

	public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
		
		PaintingEntity pe = Utils.getTE(w, x, y, z);
		if(pe == null)return false;
		float[] point = PaintingPlacement.of(w.getBlockMetadata(x, y, z)).block2painting(xs, ys, zs);
		boolean changed = this.apply(pe.image, point, getColor(ep,is));
		if(!changed)return changed;
		if(w.isRemote){
			pe.getIcon().fill(pe.image);
			return changed;
		}
		w.markBlockForUpdate(x, y, z);
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
			return true;
		}
	}
	
	public static class Mixer extends PaintTool{
		
		public Mixer(){
			super();
			this.setUnlocalizedName("mixer_brush").setTextureName("minepainter:brush");
		}
		
		@Override public boolean apply(BufferedImage img, float[] point, int color){
			
			int x = (int) (point[0] * 16);
			int y = (int) (point[1] * 16);
			
			int a75 = multiplyMasked(color , MASK_ALPHA, 0.75f); 
			int a50 = multiplyMasked(color , MASK_ALPHA, 0.5f);
			
			img.setRGB(x,y, mix(color, img.getRGB(x, y)));
			return true;
		}
		
		private int mix(int color,int original){
			float a_alpha = (color >> 24 & 0xFF) / 255.0F;
		    float b_alpha = (original >> 24 & 0xFF) / 255.0F;
		    float c_alpha = a_alpha + b_alpha * (1.0F - a_alpha);
		    int result = 0;

		    for (int b = 0; b < 24; b += 8)
		    {
		      int ca = color >> b & 0xFF;
		      ca = (int)(ca * a_alpha);
		      int cb = original >> b & 0xFF;
		      cb = (int)(cb * b_alpha * (1.0F - a_alpha));
		      result += ((int)((ca + cb) / c_alpha) << b);
		    }

		    result += ((int)(255.0F * c_alpha) << 24);
		    return result;
		}
		
		private int multiplyMasked(int val, int mask, float scale){
			int result = (int)((val & mask) * scale) & mask;
			return result + (val & ~mask);
		}
	}
}
