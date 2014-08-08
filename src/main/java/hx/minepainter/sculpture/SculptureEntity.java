package hx.minepainter.sculpture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;

public class SculptureEntity extends TileEntity{

	Sculpture sculpture;
	
	@SideOnly(Side.CLIENT)
	SculptureRenderCompiler render = new SculptureRenderCompiler();
}
