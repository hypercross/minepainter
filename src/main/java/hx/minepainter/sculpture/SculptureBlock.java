package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//TODO add hooks for block bounds
//TODO add hooks for collision raytracing
//TODO add hooks for transparent blocks
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
	
	
	
	public SculptureBlock() {
		super(Material.rock);
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side){
		return iba.isAirBlock(x, y, z);
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new SculptureEntity();
	}
	
	//TODO render update not triggered by block change
    @Override
    @SideOnly(Side.CLIENT)
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
    	Operations.markChanged(world,x,y,z);
    }
	
	@Override @SideOnly(Side.CLIENT) public void registerBlockIcons(IIconRegister p_149651_1_){}
	
	@Override @SideOnly(Side.CLIENT) public IIcon getIcon(int side, int meta){
		return current.getIcon(side, meta);
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
  
    @Override public int getLightValue(IBlockAccess world,int x,int y,int z){
    	TileEntity te = world.getTileEntity(x, y, z);
    	if(te == null || !(te instanceof SculptureEntity))return super.getLightValue(world, x, y, z);
    	SculptureEntity se = (SculptureEntity) te;
    	return se.sculpture.getLight();
    }
//
//    @Override public int getLightOpacity(){
//    	return current.getLightOpacity();
//    }
}
