package graphics;

import game.Manager;

public class Animation extends CollidableObject{
	int fpu;
	int updates;
	int frame;
	int length;
	//for animations
	public Animation(int xLocation, int yLocation, int width, int height, int xInset, int yInset, String path){
		super(xLocation, yLocation, width, height, xInset, yInset, path);
	}
	
	public void animate(int length, int yInset, int fpu){
		this.fpu = fpu;
		this.length = length;
		frame = 0;
		updates = 0;
		this.yInset=yInset;
		xInset=0;
		render();
		Manager.runningAnimations.add(this);
	}
	
	public void update(){
		updates ++;
		if (updates==fpu){
			if (frame < length-1){
				updates = 0;
				frame++;
				xInset=frame*width;
				render();
			}else{
				xInset = 0;
				yInset = 0;
				render();
				Manager.runningAnimations.remove(this);
			}
		}
	}
	
}
