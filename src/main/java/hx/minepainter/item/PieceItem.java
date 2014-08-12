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
}
