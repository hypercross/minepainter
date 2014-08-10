package hx.minepainter.sculpture;

import hx.utils.Debug;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;

public class SculptureRenderBlocks extends RenderBlocks{

	private double[] overrideBounds = new double[6];
	
	@Override public void setRenderBoundsFromBlock(Block p_147775_1_){
		super.setRenderBoundsFromBlock(p_147775_1_);
		render2override();
		renderFull();
	}
	
	private void override2render(){
		this.renderMinX = overrideBounds[0];
		this.renderMinY = overrideBounds[1];
		this.renderMinZ = overrideBounds[2];
		this.renderMaxX = overrideBounds[3];
		this.renderMaxY = overrideBounds[4];
		this.renderMaxZ = overrideBounds[5];
	}
	
	private void render2override(){
		overrideBounds[0] = this.renderMinX;
		overrideBounds[1] = this.renderMinY;
		overrideBounds[2] = this.renderMinZ;
		overrideBounds[3] = this.renderMaxX;
		overrideBounds[4] = this.renderMaxY;
		overrideBounds[5] = this.renderMaxZ;
	}
	
	private void renderFull(){
		this.renderMinX = 0d;
		this.renderMinY = 0d;
		this.renderMinZ = 0d;
		this.renderMaxX = 1d;
		this.renderMaxY = 1d;
		this.renderMaxZ = 1d;
	}
	
	@Override public void renderFaceYNeg(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, IIcon p_147768_8_){
		override2render();
		super.renderFaceYNeg(p_147768_1_, p_147768_2_, p_147768_4_, p_147768_6_, p_147768_8_);
		renderFull();
	}
	
	@Override public void renderFaceYPos(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_){
		override2render();
		super.renderFaceYPos(p_147806_1_, p_147806_2_, p_147806_4_, p_147806_6_, p_147806_8_);
		renderFull();
	}
	
	@Override public void renderFaceZNeg(Block p_147761_1_, double p_147761_2_, double p_147761_4_, double p_147761_6_, IIcon p_147761_8_){
		override2render();
		super.renderFaceZNeg(p_147761_1_, p_147761_2_, p_147761_4_, p_147761_6_, p_147761_8_);
		renderFull();
	}
	
	@Override public void renderFaceZPos(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, IIcon p_147734_8_){
		override2render();
		super.renderFaceZPos(p_147734_1_, p_147734_2_, p_147734_4_, p_147734_6_, p_147734_8_);
		renderFull();
	}
	
	@Override public void renderFaceXNeg(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, IIcon p_147798_8_){
		override2render();
		super.renderFaceXNeg(p_147798_1_, p_147798_2_, p_147798_4_, p_147798_6_, p_147798_8_);
		renderFull();
	}
	
	@Override public void renderFaceXPos(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_){
		override2render();
		super.renderFaceXPos(p_147764_1_, p_147764_2_, p_147764_4_, p_147764_6_, p_147764_8_);
		renderFull();
	}
}
