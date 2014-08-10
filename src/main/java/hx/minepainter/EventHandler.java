package hx.minepainter;

import hx.minepainter.sculpture.Operations;
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
		if(is == null || is.getItem() != ModMinePainter.chisel)return;
		
		int x = event.target.blockX, y = event.target.blockY,z = event.target.blockZ;		
		Block sculpture = event.player.worldObj.getBlock(x,y,z);
		if(sculpture != ModMinePainter.sculpture.block)return;
		
		SculptureEntity se = Utils.getTE(event.player.worldObj, x, y, z);		
		
		Vec3 from = event.player.getPosition(1.0f);
		from = from.addVector(-event.target.blockX, -event.target.blockY, -event.target.blockZ);
		Vec3 look = event.player.getLookVec();
		
		int[] pos = Operations.raytrace(se.sculpture(), from,from.addVector(look.xCoord * 5, look.yCoord * 5, look.zCoord * 5));
		if(pos[0] == -1)return;
		
		ForgeDirection dir = ForgeDirection.getOrientation(pos[3]);
		float[] box = new float[] { pos[0]/8f,pos[1]/8f,pos[2]/8f,(1+pos[0])/8f,(1+pos[1])/8f,(1+pos[2])/8f};
		
		if(dir == ForgeDirection.UP)box[1]+=1/8f;
		else if(dir == ForgeDirection.DOWN)box[4]-=1/8f;
		else if(dir == ForgeDirection.EAST)box[0]+=1/8f;
		else if(dir == ForgeDirection.WEST)box[3]-=1/8f;
		else if(dir == ForgeDirection.SOUTH)box[2]+=1/8f;
		else if(dir == ForgeDirection.NORTH)box[5]-=1/8f;
		
		sculpture.setBlockBounds(box[0],box[1],box[2],box[3],box[4],box[5]);
//		sculpture.setBlockBounds(pos[0]/8f,pos[1]/8f,pos[2]/8f,(1+pos[0])/8f,(1+pos[1])/8f,(1+pos[2])/8f);
		event.context.drawSelectionBox(event.player, event.target, 0, event.partialTicks);
		sculpture.setBlockBounds(0, 0, 0, 1, 1, 1);
		
		event.setCanceled(true);
	}
}
