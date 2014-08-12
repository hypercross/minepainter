package hx.minepainter.item;

import hx.minepainter.sculpture.Operations;
import hx.utils.Debug;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class PieceItem extends ChiselItem{

	public PieceItem(){
		super();
		this.setCreativeTab(null);
		this.setUnlocalizedName("sculpture_piece");
		this.setTextureName("");
		this.setHasSubtypes(true);
	}
	
	@Override
	public Block getEditBlock(ItemStack is){
		return Block.getBlockById( (is.getItemDamage() >> 4) & 0xfff);
	}
	
	@Override
	public int getEditMeta(ItemStack is){
		return is.getItemDamage() & 0xf;
	}
	
	@Override
	public int getChiselFlags(EntityPlayer ep){
		return Operations.PLACE;
	}
	
	public static class Bar extends PieceItem{
		@Override
		public int getChiselFlags(EntityPlayer ep){
			int axis = Operations.getLookingAxis(ep);
			switch(axis){
			case 0 : return Operations.PLACE | Operations.ALLX;
			case 1 : return Operations.PLACE | Operations.ALLY;
			case 2 : return Operations.PLACE | Operations.ALLZ;
			}
			return Operations.PLACE;
		}
	}
	
	public static class Cover extends PieceItem{
		@Override
		public int getChiselFlags(EntityPlayer ep){
			int axis = Operations.getLookingAxis(ep);
			switch(axis){
			case 0 : return Operations.PLACE | Operations.ALLY | Operations.ALLZ;
			case 1 : return Operations.PLACE | Operations.ALLX | Operations.ALLZ;
			case 2 : return Operations.PLACE | Operations.ALLX | Operations.ALLY;
			}
			return Operations.PLACE;
		}
	}
}
