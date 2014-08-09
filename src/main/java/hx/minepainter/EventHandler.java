package hx.minepainter;

import hx.minepainter.sculpture.SculptureRender;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.event.RenderWorldEvent;

public class EventHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPreRenderWorld(RenderWorldEvent.Pre e){
		SculptureRender.setCurrentChunkPos(e.renderer.posX,e.renderer.posY, e.renderer.posZ);
	}
	
}
