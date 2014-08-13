package hx.minepainter.item;

import hx.minepainter.painting.PaintingCache;
import hx.minepainter.painting.PaintingIcon;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class CanvasRenderer implements IItemRenderer{

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.INVENTORY ||
        		type == ItemRenderType.EQUIPPED ||
        		type == ItemRenderType.EQUIPPED_FIRST_PERSON ||
        		type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
    	if(type == ItemRenderType.ENTITY){
    		return helper == ItemRendererHelper.ENTITY_ROTATION ||
    			   helper == ItemRendererHelper.ENTITY_BOBBING;
    	}
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        
        PaintingIcon pi = PaintingCache.get(item);
        
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, pi.glTexId());
        if(type == ItemRenderType.INVENTORY)
        	renderInventory(pi);
        else if(type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON)
        	renderEquipped(pi);
        else{
        	GL11.glTranslatef(-0.5f, 0, 0);
        	renderEquipped(pi);
        }
    }
    
    private void renderInventory(PaintingIcon icon)
    {
        Tessellator tes = Tessellator.instance;
        tes.startDrawingQuads();
        tes.addVertexWithUV(1,  1,  0, icon.getMinU(), icon.getMinV());
        tes.addVertexWithUV(1,  15, 0, icon.getMinU(), icon.getMaxV());
        tes.addVertexWithUV(15, 15, 0, icon.getMaxU(), icon.getMaxV());
        tes.addVertexWithUV(15, 1,  0, icon.getMaxU(), icon.getMinV());
        tes.draw();
    }
    
    private void renderEquipped(PaintingIcon icon)
    {
        Tessellator var5 = Tessellator.instance;
        float var7 = icon.getMinU();
        float var8 = icon.getMaxU();
        float var9 = icon.getMinV();
        float var10 = icon.getMaxV();
        ItemRenderer.renderItemIn2D(var5, var8, var9, var7, var10, 256, 256, 0.0625F);
    }
}