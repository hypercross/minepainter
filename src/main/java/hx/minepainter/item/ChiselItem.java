package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.Operations;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ChiselItem extends Item{
	public ChiselItem(){
		super();
		this.setCreativeTab(ModMinePainter.tabMinePainter);
		this.setUnlocalizedName("stone_chisel");
	}
	
	@Override
    public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
		int bs = Operations.editSubBlock(w, new int[]{0, 0,0,7,7,7}, x,y,z, Blocks.stone);
		return bs > 0;
	}
}
