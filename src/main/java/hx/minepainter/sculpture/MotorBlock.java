package hx.minepainter.sculpture;

import net.minecraftforge.common.util.ForgeDirection;

public class MotorBlock {
	
	// direction of shift for counter clockwise rotation on axis front + corner id
	// corner id goes counter clockwise from the 'minmin' corner
	// x : y-z-, y-z+, y+z+, y+z-
	// y : z-x-, z-x+, z+x-, z+x+
	// z : x-y-, x-y+, x+y-, x+y+
	private static final int rotation_table[][] = new int[3][4];
	static{
		for(int i = 0; i < 3; i ++){
			int y = (i + 1)%3;
			int z = (i + 2)%3;
			
			rotation_table[i][0] = z*2;
			rotation_table[i][1] = y*2;
			rotation_table[i][2] = z*2+1;
			rotation_table[i][3] = y*2+1;
		}
	};
	
	private int getShiftAxis(int face, int meta){
		// if on pos axis or rotating clockwise, don't shift by 1
		int shift = (face % 2) ^ (meta >> 3) ^ 1;
		
		int y = (face/2 +1)%3;
		int z = (face/2 +2)%3;
		int corner = ((meta >> y) & 1) * 2 + ((meta >> z) & 1);
		
		// shift
		corner = (corner + shift) % 4;
		
		return rotation_table[face/2][corner];
	}
}
