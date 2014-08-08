package hx.minepainter.sculpture;

public enum SculptureRotation {
	
	NORMAL {
		@Override boolean get(byte[] data, int x, int y, int z) {
			return ( data[x*8 + y] & (1 << z) ) > 0; 
		}

		@Override void set(byte[] data, int x, int y, int z, boolean on) {
			if(on)data[x*8 + y] |= (1 << z);
			else data[x*8 + y] &= ~(1 << z);
		}
	},
	L1{
		@Override boolean get(byte[] data, int x, int y, int z) {
			return ( data[(7-y)*8 + x] & (1 << z) ) > 0; 
		}

		@Override
		void set(byte[] data, int x, int y, int z, boolean on) {
			if(on)data[(7-y)*8 + x] |= (1 << z);
			else data[(7-y)*8 + x] &= ~(1 << z);
		}
	};
	
	abstract boolean get(byte[] data, int x,int y,int z);
	abstract void set(byte[] data, int x,int y,int z, boolean on);
	
}
