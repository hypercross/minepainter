package hx.minepainter;

import hx.minepainter.item.BarcutterItem;
import hx.minepainter.item.ChiselItem;
import hx.minepainter.item.PieceItem;
import hx.minepainter.item.PieceRenderer;
import hx.minepainter.item.SawItem;
import hx.minepainter.painting.PaintTool;
import hx.minepainter.painting.PaintingBlock;
import hx.minepainter.painting.PaintingEntity;
import hx.minepainter.painting.PaintingRenderer;
import hx.minepainter.sculpture.SculptureBlock;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureOperationMessage;
import hx.minepainter.sculpture.SculptureRender;
import hx.utils.BlockLoader;
import hx.utils.Debug;
import hx.utils.ItemLoader;
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
	
	public static CreativeTabs tabMinePainter = new CreativeTabs("minepainter"){

		@Override public Item getTabIconItem() {
			return Item.getItemFromBlock(Blocks.stone);
		}
		
	};
	
	public static BlockLoader<SculptureBlock> sculpture = 
			new BlockLoader(new SculptureBlock(), SculptureEntity.class);
	public static BlockLoader<PaintingBlock> painting = 
			new BlockLoader(new PaintingBlock(), PaintingEntity.class);
	
	public static ItemLoader<ChiselItem> chisel = new ItemLoader(new ChiselItem());
	public static ItemLoader<BarcutterItem> barcutter = new ItemLoader(new BarcutterItem());
	public static ItemLoader<SawItem> saw = new ItemLoader(new SawItem());
	public static ItemLoader<PieceItem> piece = new ItemLoader(new PieceItem());
	public static ItemLoader<PieceItem> bar = new ItemLoader(new PieceItem.Bar().setUnlocalizedName("sculpture_bar"));
	public static ItemLoader<PieceItem> cover = new ItemLoader(new PieceItem.Cover().setUnlocalizedName("sculpture_cover"));
	public static ItemLoader<PaintTool> minibrush = new ItemLoader(new PaintTool.Mini());
	
	public static SimpleNetworkWrapper network;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e){
		sculpture.load();
		painting.load();
		
		chisel.load();
		barcutter.load();
		saw.load();
		piece.load();
		bar.load();
		cover.load();
		minibrush.load();
		
		MinecraftForge.EVENT_BUS.register(new hx.minepainter.EventHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel("minepainter");
		network.registerMessage(SculptureOperationMessage.SculptureOperationHandler.class, 
				SculptureOperationMessage.class, 0, Side.SERVER);
	}
	
	@SideOnly(Side.CLIENT)
	@EventHandler
	public void preInitClient(FMLPreInitializationEvent e){
		sculpture.registerRendering(new SculptureRender(), null);
		painting.registerRendering(null, new PaintingRenderer());
		
		piece.registerRendering(new PieceRenderer());
		bar.registerRendering(new PieceRenderer.Bar());
		cover.registerRendering(new PieceRenderer.Cover());
	}
}
