package hx.minepainter.sculpture;

public class Rotation {
	
	byte[] r = new byte[9];
	
	int x,y,z;
	public void apply(int x,int y,int z){
		this.x = x*r[0] + y*r[3] + z*r[6];
		this.y = x*r[1] + y*r[4] + z*r[7];
		this.z = x*r[2] + y*r[5] + z*r[8];
	}
	
	public void multiply(Rotation r){
		byte[] result = new byte[9];
		for(int i = 0 ; i < 9; i ++){
			int x = i / 3;
			int y = i % 3;
			for(int j = 0; j < 3; j ++)
				result[i] += this.r[x*3 + j] * r.r[j*3 + y];  
		}
		this.r = result;
	}
	
	public Rotation(){
		r[0] = 1;
		r[4] = 1;
		r[8] = 1;
	}
	
	public static Rotation I = new Rotation();
	public static Rotation X = new Rotation();
	public static Rotation Y = new Rotation();
	public static Rotation Z = new Rotation();
	static{
	}
}
