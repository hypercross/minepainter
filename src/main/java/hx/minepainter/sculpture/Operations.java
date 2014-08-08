package hx.minepainter.sculpture;

import net.minecraft.init.Blocks;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class Operations {
	
	
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
