package hx.minepainter;

import hx.minepainter.item.ChiselItem;
import hx.minepainter.sculpture.Operations;
import hx.minepainter.sculpture.Sculpture;
import hx.minepainter.sculpture.SculptureEntity;
import hx.minepainter.sculpture.SculptureRender;
import hx.utils.Debug;
import hx.utils.Utils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.util.ForgeDirection;

//TODO auto craft pieces together
public class EventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPreRenderWorld(RenderWorldEvent.Pre e){
		SculptureRender.setCurrentChunkPos(e.renderer.posX,e.renderer.posY, e.renderer.posZ);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawBlockhightlight(DrawBlockHighlightEvent event){
		ItemStack is = event.player.getCurrentEquippedItem();
		if(is == null || !(is.getItem() instanceof ChiselItem))return;
		
		int x = event.target.blockX, y = event.target.blockY,z = event.target.blockZ;		
		Block sculpture = event.player.worldObj.getBlock(x,y,z);
		
		int[] pos = Operations.raytrace(x,y,z, event.player);
		if(pos[0] == -1)return;
		
		ChiselItem ci = Utils.getItem(is);
		int flags = ci.getChiselFlags(event.player);
		if(!Operations.validOperation(event.player.worldObj, x,y,z, pos, flags))return;
		
		Operations.setBlockBoundsFromRaytrace(pos, sculpture, flags);
		event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
		sculpture.setBlockBounds(0, 0, 0, 1, 1, 1);
		
		event.setCanceled(true);
	}
}
