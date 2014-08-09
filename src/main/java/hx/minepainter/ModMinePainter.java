package hx.minepainter;

import hx.minepainter.item.ChiselItem;
import hx.minepainter.sculpture.SculptureBlock;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureRender;
import hx.utils.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "minepainter", version = "0.2.0")
public class ModMinePainter {
	
	@SideOnly(Side.CLIENT)
	public static CreativeTabs tabMinePainter = new CreativeTabs("minepainter"){

		@Override public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.stone);
		}
		
	};
	
	public static BlockLoader<SculptureBlock> sculpture = 
			new BlockLoader<SculptureBlock>(new SculptureBlock(), SculptureEntity.class);
	
	public static Item chisel = new ChiselItem();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		sculpture.load();
		GameRegistry.registerItem(chisel, chisel.getClass().getSimpleName());
	}
	
	@SideOnly(Side.CLIENT)
	@EventHandler
	public void preInitClient(FMLPreInitializationEvent e){
		sculpture.registerRendering(new SculptureRender(), null);
	}
}
