package hx.minepainter.item;

import hx.minepainter.sculpture.Operations;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class PieceItem extends ChiselItem{

	public PieceItem(){
		this.setCreativeTab(null);
		this.setUnlocalizedName("sculpture_piece");
		this.setTextureName("");
		this.setHasSubtypes(true);
	}
	
	public Block getEditBlock(ItemStack is){
		return Block.getBlockById( (is.getItemDamage() >> 4) & 0xfff);
	}
	
	public int getEditMeta(ItemStack is){
		return is.getItemDamage() & 0xf;
	}
	
	public int getChiselFlags(EntityPlayer ep){
		return Operations.PLACE;
	}
}
