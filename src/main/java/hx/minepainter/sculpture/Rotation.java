package hx.minepainter.sculpture;

public enum Rotation {
	
	X{
		@Override public void apply(int[] coord) {
			int i = coord[1];
			coord[1] = 7-coord[2];
			coord[2] = i;	
		}
		
	},Y{
		@Override public void apply(int[] coord) {
			int i = coord[0];
			coord[0] = 7-coord[2];
			coord[2] = i;
		}
	},Z{
		@Override public void apply(int[] coord) {
			int i = coord[0];
			coord[0] = 7-coord[1];
			coord[1] = i;
		}
	};
	
	public static Rotation[][] dirs = new Rotation[][]{
		new Rotation[]{},
		new Rotation[]{X},
		new Rotation[]{X,X},
		new Rotation[]{X,X,X},
	};
	
	
	public abstract void apply(int[] coord);
	
	
}
