package hx.minepainter.painting;

import java.util.LinkedList;

public class PaintingCache {
	public static final int res = 256; 

	LinkedList<PaintingSheet> sheets = new LinkedList<PaintingSheet>();
	
	private static final PaintingCache instance = new PaintingCache();
	
	public static PaintingIcon get(){
		for(PaintingSheet sheet : instance.sheets){
			if(sheet.isEmpty())continue;
			return sheet.get();
		}
		PaintingSheet sheet = new PaintingSheet(res);
		instance.sheets.add(sheet);
		return sheet.get();
	}
}
