package hx.minepainter;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import cpw.mods.fml.common.Mod;

@Mod(modid = "minepainter", version = "0.2.0")
public class ModMinePainter {

	public static boolean sculptable(Block b, int blockMeta)
	{
		if(b == null)return false;
		
		if(b == Blocks.grass)return false;
		if(b == Blocks.bedrock)return false;
		if(b == Blocks.cactus)return false;
		if(b == Blocks.glass)return true;
		if(b == Blocks.leaves)return false;

		if(b.hasTileEntity(blockMeta))return false;
		if(!b.renderAsNormalBlock())return false;
		
		if(b.getBlockBoundsMaxX()!=1.0f)return false;
		if(b.getBlockBoundsMaxY()!=1.0f)return false;
		if(b.getBlockBoundsMaxZ()!=1.0f)return false;
		if(b.getBlockBoundsMinX()!=0.0f)return false;
		if(b.getBlockBoundsMinY()!=0.0f)return false;
		if(b.getBlockBoundsMinZ()!=0.0f)return false;
		
		
		return true;
	}
	
}
