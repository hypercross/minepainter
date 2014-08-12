package hx.minepainter.painting;

import java.awt.image.BufferedImage;

public interface IPainter {

	public boolean apply(BufferedImage img, float xs, float ys, int color);
	
}
