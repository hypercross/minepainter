package hx.minepainter.sculpture;

import hx.minepainter.ModMinePainter;
import hx.utils.Utils;
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
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

//TODO add hooks for block bounds
//TODO add hooks for collision boxes
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
	
	public void dropScrap(World w, int x, int y, int z, ItemStack is ){
		this.dropBlockAsItem(w, x, y, z, is);
	}
	
	
	public SculptureBlock() {
		super(Material.rock);
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
		if(hit == null)return null;
		
		return new MovingObjectPosition(x,y,z,pos[3], hit);
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
