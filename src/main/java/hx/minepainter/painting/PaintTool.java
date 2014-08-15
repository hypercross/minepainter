package hx.minepainter.painting;

import java.awt.image.BufferedImage;

import hx.minepainter.ModMinePainter;
import hx.minepainter.item.Palette;
import hx.utils.Debug;
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
		PaintingPlacement place = PaintingPlacement.of(w.getBlockMetadata(x, y, z));
		float[] point = place.block2painting(xs, ys, zs);
		
		boolean changed = false;
		for(int i = -1;i<=1;i++)
			for(int j = -1;j<=1;j++){
				int _x = x + place.xpos.offsetX * i + place.ypos.offsetX * j;
				int _y = y + place.xpos.offsetY * i + place.ypos.offsetY * j;
				int _z = z + place.xpos.offsetZ * i + place.ypos.offsetZ * j;
				
				if(w.getBlock(_x, _y, _z) != ModMinePainter.painting.block)continue;
				if(w.getBlockMetadata(_x, _y, _z) != place.ordinal())continue;
				
				PaintingEntity painting = Utils.getTE(w, _x, _y, _z);
				
				point[0] -= i;
				point[1] -= j;
				boolean _changed = apply(painting.image, point, getColor(ep,is));
				point[0] += i;
				point[1] += j;
				
				if(_changed){
					if(w.isRemote)pe.getIcon().fill(pe.image);
					else w.markBlockForUpdate(_x, _y, _z);
					changed = true;
				}
			}
		
		return changed;
	}
	
	public int getColor(EntityPlayer ep, ItemStack is){
		int size = ep.inventory.getSizeInventory();
		for(int i = 0; i < size; i ++){
			ItemStack slot = ep.inventory.getStackInSlot(i);
			if(slot == null)continue;
			if(!(slot.getItem() instanceof Palette))continue;
			
			return Palette.getColors(slot)[0];
		}
		return 0;
	}
	
	public boolean apply(BufferedImage img, float[] point, int color) {
		return false;
	}
	
	public boolean inBounds(int x,int y){
		return x>=0 && x<16 && y>=0 && y<16;
	}

	public static class Mini extends PaintTool{
		
		public Mini(){
			super();
			this.setUnlocalizedName("mini_brush");
			this.setTextureName("minepainter:brush_small");
		}
		
		@Override public boolean apply(BufferedImage img, float[] point, int color){
			
			int x = (int) (point[0] * 16 + 16) - 16;
			int y = (int) (point[1] * 16 + 16) - 16;
			
			if(!inBounds(x,y))return false;
			
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
			
			int x = (int) (point[0] * 16 + 16) - 16;
			int y = (int) (point[1] * 16 + 16) - 16;
			
			int a75 = (int)(((color >> 24) & 0xff) * 0.75f) << 24; 
			a75 += color & 0xffffff;
			int a50 = (int)(((color >> 24) & 0xff) * 0.5f) << 24;
			a50 += color & 0xffffff;
			
			boolean changed = false;
			for(int i = -1; i <=1; i++)
				for(int j = -1; j <= 1; j++){
					if(!inBounds(x+i,y+j))continue;
					changed = true;
					
					int to_blend = Math.abs(i) + Math.abs(j);
					if(to_blend == 0)to_blend = color;
					else if(to_blend == 1)to_blend = a75;
					else to_blend = a50;
					
					img.setRGB(x+i, y+j, mix(to_blend, img.getRGB(x+i, y+j)));
				}
			return changed;
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
		
	}
	
	
}
