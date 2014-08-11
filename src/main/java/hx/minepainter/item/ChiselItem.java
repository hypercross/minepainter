package hx.minepainter.item;

import hx.minepainter.ModMinePainter;
import hx.minepainter.sculpture.Operations;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureOperationMessage;
import hx.utils.Debug;
import hx.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ChiselItem extends Item{
	
	public ChiselItem(){
		super();
		this.setCreativeTab(ModMinePainter.tabMinePainter);
		this.setUnlocalizedName("chisel");
		this.setTextureName("minepainter:stone_chisel");
	}
	
	@Override
    public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {
		if(!w.isRemote)return false;
		int[] pos = Operations.raytrace(x,y,z,ep);
		
		int flags = this.getChiselFlags(ep);
		Block editBlock = this.getEditBlock(is);
		int editMeta = this.getEditMeta(is);
		if(!Operations.validOperation(w, x, y, z, pos, flags))
			return false;
		
		
		boolean done = Operations.applyOperation(w, x, y, z, pos, flags,editBlock, editMeta);
		if(!done)return false;
		
		ModMinePainter.network.sendToServer(new SculptureOperationMessage(pos,x,y,z,editBlock,editMeta,flags));		
		return true;
	}
	
	public Block getEditBlock(ItemStack is){
		return Blocks.air;
	}
	
	public int getEditMeta(ItemStack is){
		return 0;
	}
	
	public int getChiselFlags(EntityPlayer ep){
		return 0;
	}
}
