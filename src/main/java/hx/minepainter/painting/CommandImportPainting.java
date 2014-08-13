package hx.minepainter.painting;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandImportPainting extends CommandBase {
	private static LinkedBlockingQueue<Runnable> tasks = new LinkedBlockingQueue<Runnable>();
	private static Thread worker;
	
	@Override
	public String getCommandName() {
		return "mpimport";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "mpimport <image url> [--size <w> <h>]\n" +
				"to import image as w * h pieces of 16x16 paintings";
	}

	@Override
	public void processCommand(ICommandSender var1, String[] var2) {
		startWorking();
		int w = 1,h = 1;
		String url = var2[0];
		for(int i = 0 ; i < var2.length; i++){
			if(var2[i].equals("--size") && var2.length - i > 2){
				w = Integer.parseInt(var2[i+1]);
				h = Integer.parseInt(var2[i+2]);
			}
		}
		try {
			BufferedImage img = ImageIO.read(new URL(url));
			//TODO add that image
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void startWorking(){
		if(worker == null || !worker.isAlive()){
			worker = new Thread(){
				@Override public void run(){
					while(true)
						try {
							tasks.take().run();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			};
			worker.start();
		}
	}
}
