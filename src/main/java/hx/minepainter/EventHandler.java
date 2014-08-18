package hx.minepainter;

import hx.minepainter.item.ChiselItem;
import hx.minepainter.item.PieceItem;
import hx.minepainter.painting.PaintTool;
import hx.minepainter.painting.PaintingBlock;
import hx.minepainter.painting.PaintingPlacement;
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
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

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
		
//		event.setCanceled(true);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onDrawPaintingPixel(DrawBlockHighlightEvent event){
		ItemStack is = event.player.getCurrentEquippedItem();
		if(is == null || !(is.getItem() instanceof PaintTool))return;
		
		int x = event.target.blockX, y = event.target.blockY,z = event.target.blockZ;
		World w = event.player.worldObj;
		PaintingBlock painting = (PaintingBlock) w.getBlock(x,y,z);
		PaintingPlacement pp = PaintingPlacement.of(w.getBlockMetadata(x, y, z));
		
		Vec3 pos = event.player.getPosition(1.0f);
		Vec3 look = event.player.getLookVec();
		look = pos.addVector(look.xCoord * 5, look.yCoord * 5, look.zCoord * 5);
		
		MovingObjectPosition mop = painting.collisionRayTrace(w, x, y, z, pos, look);
		float[] point = pp.block2painting( (float)(mop.hitVec.xCoord - mop.blockX), 
				(float)(mop.hitVec.yCoord-mop.blockY),
				(float)(mop.hitVec.zCoord-mop.blockZ));
		
		point[0] = (int)(point[0] * 16)/16f;
		point[1] = (int)(point[0] * 16)/16f;
		
		float[] bound1 = pp.painting2block(point[0], point[1]);
		float[] bound2 = pp.painting2block(point[0]+1/16f, point[1]+1/16f);
		
		painting.setBlockBounds(Math.min(bound1[0], bound2[0]), 
								Math.min(bound1[1], bound2[1]),
								Math.min(bound1[2], bound2[2]),
								Math.max(bound1[3], bound2[3]),
								Math.max(bound1[4], bound2[4]),
								Math.max(bound1[5], bound2[5]));
		painting.ignore_bounds_on_state = true;
		event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
		painting.ignore_bounds_on_state = false;
		
	}
}
