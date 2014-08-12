package hx.minepainter.painting;

import java.util.HashSet;

import net.minecraft.client.renderer.texture.TextureUtil;

/**
 * okay , the design of this.
 * each TE has full img data.
 * we also have a PaintingSheet that handles textures on the client.
 * then to render a TE we cache img data to texture and draw.
 * 
 * we also need the two types of brushes 
 * and a dropper item to pick colors directly onto the brush
 *  
 * finally we only export and import pngs on-demand
 * to export we just generate 16x16 png files
 * 
 * to import an image we just parse as 16x16
 * if it's not 16x16 we either break it or compress it
 * 
 * @author hypercross
 *
 */
public class PaintingSheet {
	final int resolution;
	int glTexId = -1;
	
	HashSet<PaintingIcon> icons = new HashSet<PaintingIcon>();
	
	public PaintingSheet(int res){
		glTexId = TextureUtil.glGenTextures();
		this.resolution = res;
		
		int total = resolution * resolution / 256;
		for(int i = 0 ;i < total; i ++)icons.add(new PaintingIcon(this,i));
		
		TextureUtil.allocateTexture(glTexId, resolution, resolution);
	}
	
	public boolean isEmpty(){
		return icons.isEmpty();
	}
	
	public PaintingIcon get(){
		for(PaintingIcon pi : icons){
			icons.remove(pi);
			return pi;
		}
		throw new IllegalStateException("painting slots depleted!");
	}
}
