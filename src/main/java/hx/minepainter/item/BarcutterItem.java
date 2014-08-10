package hx.minepainter.item;

import hx.minepainter.sculpture.Operations;
import net.minecraft.entity.player.EntityPlayer;

public class BarcutterItem extends ChiselItem{
	
	public BarcutterItem(){
		super();
		this.setUnlocalizedName("barcutter");
		this.setTextureName("minepainter:iron_chisel");
	}

	@Override
	public int getChiselFlags(EntityPlayer ep){
		int axis = Operations.getLookingAxis(ep);
		switch(axis){
		case 0: return Operations.ALLX;
		case 1: return Operations.ALLY;
		case 2: return Operations.ALLZ;
		}
		return 0;
	}
}
