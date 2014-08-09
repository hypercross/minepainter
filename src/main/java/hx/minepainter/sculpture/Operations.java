package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Operations {
	
	public static int editSubBlock(World w, int[] minmax, int x,int y,int z, Block block, byte meta){
		int tx,ty,tz;
		int s = 0;
		
		for(int _x = minmax[0] ; _x < minmax[3]; _x ++)
			for(int _y = minmax[1] ; _y < minmax[4]; _y ++)
				for(int _z = minmax[2] ; _z < minmax[5]; _z ++){
					
					tx = x; ty = y; tz = z;
					
					while(_x > 7){ _x -= 8; tx++; }
					while(_y > 7){ _y -= 8; ty++; }
					while(_z > 7){ _z -= 8; tz++; }
					while(_x < 0){ _x += 8; tx--; }
					while(_y < 0){ _y += 8; ty--; }
					while(_z < 0){ _z += 8; tz--; }
					
					Block tgt_block = w.getBlock(tx, ty, tz);
					int tgt_meta = w.getBlockMetadata(tx, ty, tz);
					
					if(tgt_block == Blocks.air)
						w.setBlock(x, y, z, ModMinePainter.sculpture.block);
					else if(sculptable(tgt_block,tgt_meta))
						convertToFullSculpture(w,tx,ty,tz);
					
					if(w.getBlock(tx, ty, tz) != ModMinePainter.sculpture.block)
						continue;
					
					SculptureEntity se = (SculptureEntity) w.getTileEntity(tx, ty, tz);
					se.sculpture.setBlockAt(_x, _y, _z, block, meta);
					if(w.isRemote)se.render.changed = true;
					s++;
				}
		return s;
	}
	
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
	
	public static void convertToFullSculpture(World w, int x,int y, int z){
		Block was = w.getBlock(x, y, z);
		int meta = w.getBlockMetadata(x, y, z);
		w.setBlock(x, y, z, ModMinePainter.sculpture.block);
		SculptureEntity se = (SculptureEntity) w.getTileEntity(x, y, z);
		for(int i = 0 ; i < 512; i ++){
			se.sculpture.setBlockAt((i>>6)&7, (i>>3)&7, (i>>0)&7, was, (byte)meta);
		}
	}
	
	public static int[] raytrace(Sculpture sculpture, Vec3 start, Vec3 end){
		
		int[] xyz = new int[]{-1,-1,-1};
		
		for(int x = 0; x <= 8; x ++){

			Vec3 hit = start.getIntermediateWithXValue(end, x/8f);
			if(hit == null)continue;
			
			int y = (int)hit.yCoord;
			int z = (int)hit.zCoord;
			
			if(end.xCoord > start.xCoord)updateResult(sculpture, xyz,x,y,z,start);
			else updateResult(sculpture,xyz,x-1,y,z,start);
		}
		
		for(int y = 0; y <= 8; y ++){
			
			Vec3 hit = start.getIntermediateWithYValue(end, y/8f);
			
			int x = (int)hit.xCoord;
			int z = (int)hit.zCoord;
			
			if(end.yCoord > start.yCoord)updateResult(sculpture, xyz,x,y,z,start);
			else updateResult(sculpture, xyz,x,y-1,z,start);
		}
		
		for(int z = 0; z <= 8; z ++){
			
			Vec3 hit = start.getIntermediateWithZValue(end, z/8f);
			
			int x = (int)hit.xCoord;
			int y = (int)hit.yCoord;
			
			if(end.zCoord > start.zCoord)updateResult(sculpture, xyz,x,y,z,start);
			else updateResult(sculpture, xyz,x,y,z-1,start);
		}
		
		return xyz;
	}
	
	private static void updateResult(Sculpture sculpture, int[] xyz, int x,int y,int z, Vec3 start){
		if(!sculpture.contains(x, y, z))return;
		if(sculpture.getBlockAt(x, y, z, null) == Blocks.air)return;

		if(xyz[0] != -1){		
			if(x != xyz[0] && start.xCoord - x > 0 ==  xyz[0] - x > 0)return;
			if(y != xyz[1] && start.yCoord - y > 0 ==  xyz[1] - y > 0)return;
			if(z != xyz[2] && start.zCoord - z > 0 ==  xyz[2] - z > 0)return;
		}
		
		xyz[0] = x;xyz[1] = y; xyz[2] = z; 
	}
	
	
}
