package hx.minepainter.sculpture;

import java.util.List;
import java.util.Random;

import hx.minepainter.ModMinePainter;
import hx.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

//TODO [DEFER] add hooks for transparent blocks
public class SculptureBlock extends BlockContainer{

	private int x,y,z,meta = 0;
	private Block current = Blocks.stone;
	private int renderID = -1;
	public void setCurrentBlock(Block that, int meta){
		if(that == null){
			meta = 0;
			renderID = -1;
			current = Blocks.stone;
			return;
		}
		current = that;
		this.meta = meta;
		renderID = that.getRenderType();
	}
	public void setSubCoordinate(int x,int y,int z){
		this.x = x; this.y = y; this.z = z;
	}
	
	public void dropScrap(World w, int x, int y, int z, ItemStack is ){
		this.dropBlockAsItem(w, x, y, z, is);
	}
	
	
	public SculptureBlock() {
		super(Material.rock);
		this.setHardness(1.0f);
		this.setBlockName("sculpture");
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World w, int x, int y, int z, Vec3 st, Vec3 ed)
	{
		SculptureEntity tile = Utils.getTE(w, x, y, z);
		Sculpture sculpture = tile.sculpture();

		int[] pos = Operations.raytrace(sculpture, st.addVector(-x, -y, -z), ed.addVector(-x,-y,-z));
		if(pos[0] == -1)return null;

		ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
		Vec3 hit = null;
		if(dir.offsetX != 0)hit = st.getIntermediateWithXValue(ed, x + pos[0]/8f + (dir.offsetX+1)/16f);
		else if(dir.offsetY != 0)hit = st.getIntermediateWithYValue(ed, y + pos[1]/8f + (dir.offsetY+1)/16f);
		else if(dir.offsetZ != 0)hit = st.getIntermediateWithZValue(ed, z + pos[2]/8f + (dir.offsetZ+1)/16f);
		if(hit == null){
			if(sculpture.isEmpty())return super.collisionRayTrace(w, x, y, z, st, ed);
			return null;
		}
		
		return new MovingObjectPosition(x,y,z,pos[3], hit);
	}
	
    @Override
	public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
	{
		SculptureEntity tile = Utils.getTE(par1World, par2, par3, par4);
		Sculpture sculpture = tile.sculpture();
		
		for(int x = 0 ; x < 8 ; x ++)
			for(int y = 0 ; y < 8 ; y ++)
				for(int z = 0 ; z < 8 ; z ++){
					if(sculpture.getBlockAt(x, y, z, null) == Blocks.air)continue;
					this.setBlockBounds(x/8f, y/8f,z/8f, (x+1)/8f, (y+1)/8f,(z+1)/8f);
					super.addCollisionBoxesToList(par1World,par2,par3,par4,par5AxisAlignedBB,par6List,par7Entity);
				}
		this.setBlockBounds(0, 0, 0, 1, 1, 1);
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side){
//		if(x>=0 && y>=0 && z>=0 && x<8 && y<8 && z<8)
//			return iba.isAirBlock(x, y, z);
		if(iba.getBlock(x, y, z) == this.current)return false;
		return iba.isAirBlock(x, y, z) || !iba.getBlock(x, y, z).isOpaqueCube();
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new SculptureEntity();
	}
	
	@Override @SideOnly(Side.CLIENT) public void registerBlockIcons(IIconRegister p_149651_1_){}
	
	@Override @SideOnly(Side.CLIENT) public IIcon getIcon(int side, int meta){
		return current.getIcon(side, this.meta);
	}
	
	@Override @SideOnly(Side.CLIENT) public int getRenderType()
	{
		if(renderID == -1)
			return ModMinePainter.sculpture.renderID;
		return renderID;
	}
	
    @Override public boolean isOpaqueCube()
	{
		return false;
	}

    @Override public boolean renderAsNormalBlock()
	{
		return false;
	}
    
    @Override public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z){
    	SculptureEntity se = Utils.getTE(world, x, y, z);
    	NBTTagCompound nbt = new NBTTagCompound();
    	ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);
    	
    	se.sculpture.write(nbt);
    	is.setTagCompound(nbt);
    	return is;
    }
  
    @Override public int getLightValue(IBlockAccess world,int x,int y,int z){
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te == null || !(te instanceof SculptureEntity))return super.getLightValue(world, x, y, z);
    	SculptureEntity se = (SculptureEntity) te;
    	return se.sculpture.getLight();
    }
    
    protected ItemStack createStackedBlock(int p_149644_1_){return null;}
    
    public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_)
    {
        return null;
    }
    
    @Override public void breakBlock(World w, int x,int y,int z,Block b, int meta){
    	SculptureEntity se = Utils.getTE(w, x, y, z);
    	if(se == null || se.sculpture().isEmpty()){
    		super.breakBlock(w, x, y, z, b, meta);
    		return;
    	}
    	NBTTagCompound nbt = new NBTTagCompound();
    	ItemStack is = new ItemStack(ModMinePainter.droppedSculpture.item);
    	
    	se.sculpture.write(nbt);
    	is.setTagCompound(nbt);
    	this.dropBlockAsItem(w, x, y, z, is);
    	super.breakBlock(w, x, y, z, b, meta);
    }
//
//    @Override public int getLightOpacity(){
//    	return current.getLightOpacity();
//    }
}
