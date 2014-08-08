package hx.minepainter.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SculptureBlock extends BlockContainer{

	protected SculptureBlock(Material m) {
		super(m);
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new SculptureEntity();
	}

}
