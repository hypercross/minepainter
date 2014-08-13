package hx.minepainter.painting;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import net.minecraft.item.ItemStack;

public class PaintingCache {
	public static final int res = 256; 

	private static LinkedList<PaintingSheet> sheets = new LinkedList<PaintingSheet>();
	private static ExpirableIconPool<ItemStack> item_pool = new ExpirableIconPool<ItemStack>(12);
	
	public static PaintingIcon get(){
		for(PaintingSheet sheet : sheets){
			if(sheet.isEmpty())continue;
			return sheet.get();
		}
		PaintingSheet sheet = new PaintingSheet(res);
		sheets.add(sheet);
		return sheet.get();
	}
	
	public static PaintingIcon get(ItemStack is){
		boolean upload = !item_pool.contains(is);
		PaintingIcon pi = item_pool.get(is);
		if(!item_pool.running)item_pool.start();
		if(upload){
			try{
				byte[] data = is.getTagCompound().getByteArray("image_data");
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				BufferedImage img = ImageIO.read(bais);
				pi.fill(img);
			}catch(IOException e){
			}
		}
		return pi;
	}
}
