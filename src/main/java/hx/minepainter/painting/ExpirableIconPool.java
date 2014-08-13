package hx.minepainter.painting;

import java.util.HashMap;
import java.util.Iterator;

public class ExpirableIconPool<T> {
	
	final int expire;
	public ExpirableIconPool(int expire){
		this.expire = expire;
	}

	HashMap<T,Integer> timeouts = new HashMap<T,Integer>();
	HashMap<T,PaintingIcon> icons = new HashMap<T,PaintingIcon>();
	
	boolean running = false;
	
	public void start(){
		running = true;
		new Thread(new Runnable(){
			@Override public void run(){
				while(running){
					for(Iterator<T> iter = timeouts.keySet().iterator(); iter.hasNext(); ){
						T t = iter.next();
						int count = timeouts.get(t); 
						if(count <= 0){
							iter.remove();
							icons.remove(t).release();
						}else timeouts.put(t, count - 1);
					}
					
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if(icons.isEmpty())running = false;
				}
					
			}
		}).start();
	}
	
	public void stop(){
		running = false;
	}
	
	public boolean contains(T t){
		return icons.containsKey(t);
	}
	
	public PaintingIcon get(T t){
		if(!icons.containsKey(t))
			icons.put(t, PaintingCache.get());
		timeouts.put(t, expire);
		return icons.get(t);
	}
}
