package hx.minepainter.item;

import hx.minepainter.sculpture.Operations;
import net.minecraft.entity.player.EntityPlayer;

public class SawItem extends ChiselItem{

	public SawItem(){
		super();
		this.setUnlocalizedName("saw");
		this.setTextureName("minepainter:diamond_chisel");
	}

	@Override
	public int getChiselFlags(EntityPlayer ep){
		int axis = Operations.getLookingAxis(ep);
		switch(axis){
		case 0: return Operations.ALLY | Operations.ALLZ;
		case 1: return Operations.ALLX | Operations.ALLZ;
		case 2: return Operations.ALLX | Operations.ALLY;
		}
		return 0;
	}
}
