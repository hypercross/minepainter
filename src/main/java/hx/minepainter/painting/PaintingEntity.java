package hx.minepainter.painting;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class PaintingEntity extends TileEntity{
	
	BufferedImage image = new BufferedImage(16,16, BufferedImage.TYPE_INT_ARGB);
	
	@SideOnly(Side.CLIENT)
	private PaintingIcon icon;
	
	@SideOnly(Side.CLIENT)
	public PaintingIcon getIcon(){
		if(icon == null)icon = PaintingCache.get();
		return icon;
	}
	
	@Override
	public void invalidate()
	{
        super.invalidate();
        if(this.worldObj.isRemote){
        	getIcon().release();
        	icon = null;
        }
	}
	
        @Override
	public void onChunkUnload()
	{
        super.onChunkUnload();
		if(this.worldObj.isRemote){
			getIcon().release();
			icon = null;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		nbt.setByteArray("image_data", baos.toByteArray());
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		byte[] data = nbt.getByteArray("image_data");
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		try{
			BufferedImage img = ImageIO.read(bais);
			this.image = img;
			if(worldObj.isRemote)
				this.getIcon().fill(img);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		super.readFromNBT(nbt);
	}
	
	public Packet getDescriptionPacket()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 17, nbttagcompound);
    }

    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
    {
        readFromNBT(pkt.func_148857_g());
    }
}
