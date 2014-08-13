package hx.minepainter.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import hx.minepainter.ModMinePainter;
import hx.minepainter.painting.PaintingPlacement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class CanvasItem extends Item{

	public CanvasItem(){
		super();
		this.setCreativeTab(ModMinePainter.tabMinePainter);
		this.setTextureName("painting");
	}
	
	@SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2) {
        return 0xFFCCCC;
    }
	
	 @Override
    public boolean onItemUse(ItemStack is, EntityPlayer ep, World w, int x, int y, int z, int face, float xs, float ys, float zs) {

		if(!w.getBlock(x, y, z).isBlockNormalCube())return false;
		 
        ForgeDirection dir = ForgeDirection.getOrientation(face);
        int _x = x + dir.offsetX;
        int _y = y + dir.offsetY;
        int _z = z + dir.offsetZ;
		
        if(!w.isAirBlock(_x, _y, _z))return false;
        if(!ep.canPlayerEdit(x,y,z,face,is))return false;
		 
        PaintingPlacement pp = PaintingPlacement.of(ep.getLookVec(), face);
        w.setBlock(_x,_y,_z,ModMinePainter.painting.block, pp.ordinal(), 3);
        
        return true;
	}
}
