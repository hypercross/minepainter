package hx.minepainter.sculpture;

import org.lwjgl.util.vector.Matrix;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

public class Sculpture {
	
	public static Block current;
	public static int current_meta;
	

	byte[][] layers;
	
	int[] block_ids;
	byte[] block_metas;
	int[] usage_count;
	
	int rotation;
	
	public Sculpture(){
		normalize();
	}
	
	public void write(NBTTagCompound nbt){
		nbt.setIntArray("block_ids", block_ids);
		nbt.setByteArray("block_metas", block_metas);
		for(int i = 0 ; i < layers.length; i ++)
			nbt.setByteArray("layer" + i, layers[i]);
		nbt.setInteger("rotation", rotation);
	}
	
	public void read(NBTTagCompound nbt){
		block_ids = nbt.getIntArray("block_ids");
		block_metas = nbt.getByteArray("block_metas");
		rotation = nbt.getInteger("rotation");
		layers = new byte[block_ids.length][];
		for(int i = 0; i < layers.length; i ++)
			layers[i] = nbt.getByteArray("layer" + i);
		
		normalize();
	}
	
	public Block getBlockAt(int x,int y,int z, BlockSlice slice){
		if(!contains(x,y,z))
			return slice.getBlock(x, y, z);
		
		int index = getIndex(x,y,z);
		
		return Block.getBlockById(block_ids[index]);
	}
	
	public int getMetaAt(int x,int y,int z, BlockSlice slice){
		if(!contains(x,y,z))
			return slice.getBlockMetadata(x, y, z);
		
		int index = getIndex(x,y,z);
		
		return block_metas[index];
	}
	
	public boolean setBlockAt(int x,int y,int z,Block block){
		if(!contains(x,y,z))return false;
		
		int index = findIndexForBlock(Block.getIdFromBlock(block));
		if(index < 0)return false;
		
		setIndex(x,y,z,index);		
		return true;
	}
	
	public boolean setMetaAt(int x,int y,int z,int meta){
		if(!contains(x,y,z))return false;
		
		int index = getIndex(x,y,z);
		block_metas[index] = (byte) meta;
		return true;
	}
	
	
	private int findIndexForBlock(int blockID){
		int index = -1;
		for(int i = 0; i < block_ids.length; i++){
			if(block_ids[i] == blockID){
				return index;
			}if(usage_count[i] == 0){
				block_ids[i] = blockID;
				return index;
			}
		}
		return index;
	}
	
	int getIndex(int... coord){
		for(Rotation r : Rotation.dirs[rotation])r.apply(coord);
		int index = 0;
		for(int l = 0; l < layers.length; l ++)
			if( (layers[l][coord[0]*8 + coord[1]] & (1<<coord[2])) > 0)index |= (1 << l);
		return index;
	}
	
	void setIndex(int... coord){
		
		int prev = getIndex(coord);
		int index = coord[3];
		usage_count[prev]--;
		usage_count[index]++;
		
		for(Rotation r : Rotation.dirs[rotation])r.apply(coord);
		for(int l = 0; l < layers.length; l ++)
			if( (index & (1 << l)) > 0)
				layers[l][coord[0]*8 + coord[1]] |= (1<<coord[2]);
			else layers[l][coord[0]*8 + coord[1]] &= ~(1<<coord[2]);
	}
	
	public boolean contains(int x,int y,int z){
		return x>=0 && y>=0 && z>=0 && x<8 && y<8 && z<8; 
	}
	
	private boolean check(){
		if(block_ids == null)return false;
		if(block_metas == null)return false;
		if(layers == null)return false;
		for(int i = 0; i <layers.length; i ++){
			if(layers[i] == null)return false;
			if(layers[i].length != 64)return false;
		}
		if(block_ids.length != (1 << layers.length) -1)return false;
		if(block_ids.length != block_metas.length)return false;
		
		if(usage_count.length != block_ids.length)usage_count = new int[block_ids.length];
		return true;
	}
	
	private void grow(){
		byte[][] nlayers = new byte[layers.length][];
		for(int i = 0; i < layers.length;i++)
			nlayers[i] = layers[i];
		layers = nlayers;
		
		int[] ids = new int[block_ids.length+1];
		for(int i = 0; i < block_ids.length;i++)ids[i]=block_ids[i];
		block_ids = ids;
		
		byte[] metas = new byte[block_metas.length+1];
		for(int i = 0; i < block_metas.length;i++)metas[i]=block_metas[i];
		block_metas = metas;
		
		int[] usage = new int[usage_count.length+1];
		for(int i = 0; i < usage_count.length;i++)usage[i]=usage_count[i];
		usage_count = usage;
	}
	
	private void normalize(){
		if(!check()){
			layers = new byte[0][64];
			block_ids = new int[0];
			block_metas = new byte[0];
			usage_count = new int[0];
		}
		for(int i = 0;i<usage_count.length;i++)usage_count[i] = 0;
		for(int i = 0; i < 512;i++){
			int index = getIndex(i >> 8, (i >> 4) & 7, i & 7);
			usage_count[index]++;
		}
		
	}
}
