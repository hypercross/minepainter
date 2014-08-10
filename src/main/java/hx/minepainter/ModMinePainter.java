package hx.minepainter;

import hx.minepainter.item.BarcutterItem;
import hx.minepainter.item.ChiselItem;
import hx.minepainter.item.SawItem;
import hx.minepainter.sculpture.SculptureBlock;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureOperationMessage;
import hx.minepainter.sculpture.SculptureRender;
import hx.utils.BlockLoader;
import hx.utils.Debug;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
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
	public static Item barcutter = new BarcutterItem();
	public static Item saw = new SawItem();
	
	public static SimpleNetworkWrapper network;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		sculpture.load();
		
		GameRegistry.registerItem(chisel, chisel.getClass().getSimpleName());
		GameRegistry.registerItem(barcutter, barcutter.getClass().getSimpleName());
		GameRegistry.registerItem(saw, saw.getClass().getSimpleName());
		
		MinecraftForge.EVENT_BUS.register(new hx.minepainter.EventHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel("minepainter");
		network.registerMessage(SculptureOperationMessage.SculptureOperationHandler.class, 
				SculptureOperationMessage.class, 0, Side.SERVER);
	}
	
	@SideOnly(Side.CLIENT)
	@EventHandler
	public void preInitClient(FMLPreInitializationEvent e){
		sculpture.registerRendering(new SculptureRender(), null);
	}
}
