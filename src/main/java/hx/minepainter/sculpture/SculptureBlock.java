package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//TODO add hooks for block bounds
//TODO add hooks for collision raytracing
//TODO add hooks for transparent blocks
//TODO make sculpture piece look correct
//TODO make sculpture piece place-able
//TODO make more sculpture piece variants 
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
	}

	@Override
	public boolean shouldSideBeRendered(IBlockAccess iba, int x, int y, int z, int side){
		if(x>=0 && y>=0 && z>=0 && x<8 && y<8 && z<8)
			return iba.isAirBlock(x, y, z);
		return iba.isAirBlock(x, y, z) || !iba.getBlock(x, y, z).isOpaqueCube();
	}
	
	@Override
	public TileEntity createNewTileEntity(World var1, int var2) {
		return new SculptureEntity();
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
