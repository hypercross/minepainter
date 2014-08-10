package hx.minepainter.sculpture;

import java.util.LinkedList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.utils.Debug;
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
	
	static double length;
	static int[] xyzf = new int[]{-1,-1,-1,-1};
	
	public static int[] raytrace(Sculpture sculpture, Vec3 start, Vec3 end){
		Debug.log(start,end);
		xyzf[0] = xyzf[1] = xyzf[2] = xyzf[3] = -1; 
		length = Double.MAX_VALUE;
		
		for(int x = 0; x <= 8; x ++){

			Vec3 hit = start.getIntermediateWithXValue(end, x/8f);
			if(hit == null)continue;
			
			if(hit.yCoord < 0)continue;
			if(hit.zCoord < 0)continue;
			int y = (int)(hit.yCoord * 8);
			int z = (int)(hit.zCoord * 8);

			if(end.xCoord > start.xCoord)updateRaytraceResult(sculpture, x,y,z,ForgeDirection.WEST.ordinal(), 
					hit.subtract(start).lengthVector());
			else updateRaytraceResult(sculpture, x-1,y,z, ForgeDirection.EAST.ordinal(), 
					hit.subtract(start).lengthVector());
		}
		
		for(int y = 0; y <= 8; y ++){
			
			Vec3 hit = start.getIntermediateWithYValue(end, y/8f);
			if(hit == null)continue;
			
			if(hit.xCoord < 0)continue;
			if(hit.zCoord < 0)continue;
			int x = (int)(hit.xCoord * 8);
			int z = (int)(hit.zCoord * 8);
			
			if(end.yCoord > start.yCoord)updateRaytraceResult(sculpture,x,y,z, ForgeDirection.DOWN.ordinal(),
					hit.subtract(start).lengthVector());
			else updateRaytraceResult(sculpture,x,y-1,z,ForgeDirection.UP.ordinal(),
					hit.subtract(start).lengthVector());
		}
		
		for(int z = 0; z <= 8; z ++){
			
			Vec3 hit = start.getIntermediateWithZValue(end, z/8f);
			if(hit == null)continue;
			
			if(hit.xCoord < 0)continue;
			if(hit.yCoord < 0)continue;
			int x = (int)(hit.xCoord * 8);
			int y = (int)(hit.yCoord * 8);
			
			if(end.zCoord > start.zCoord)updateRaytraceResult(sculpture,x,y,z,ForgeDirection.NORTH.ordinal(),
					hit.subtract(start).lengthVector());
			else updateRaytraceResult(sculpture,x,y,z-1,ForgeDirection.SOUTH.ordinal(),
					hit.subtract(start).lengthVector());
		}
		
		return xyzf;
	}
	
	private static void updateRaytraceResult(Sculpture sculpture, int x,int y,int z,int f, double len){
		if(!sculpture.contains(x, y, z))return;
		if(sculpture.getBlockAt(x, y, z, null) == Blocks.air)return;
		if(len >= length)return;
		
		length = len;
		xyzf[0] = x; xyzf[1] = y; xyzf[2] = z;  xyzf[3] = f;
	}
}
