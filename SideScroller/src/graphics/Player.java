package graphics;

import game.Game;
import game.Manager;
//REMEMBER TO SIGN OUT NEXT TIME :) ~2nd Period Student
public class Player extends Animation{
	
	private double playerSpeed = 10;
	private double  xPlayerVel = 0, yPlayerVel = 0;
	private boolean standing = true;
	private int jumping = 0;
	private int rolling = 0;
	private int rollingCD = 0;
	private boolean rollDir = false; //true is right false is left
	private boolean isRolling = false;
	private int state = 0;	//0=ground 1=jumping 2=gliding 3=used double jump 4=pounding 5=diving
	private double grav = .8;
	private int lastXInset = 0;
	private Line line1 = new Line(0,400,500,600,Line.Type.FLOOR);
	private int lastX, lastY, footX=0, footY=0;
	private int bounce = 0;	//time since leaving ground
	
	public Player(int xLocation, int yLocation, int width, int height, int xInset, int yInset, String path) {
		super(xLocation, yLocation, width, height, xInset, yInset, path);
	}
	
	@Override
	public void update(){
		bounce+=1;
		standing=false;
		yPlayerVel += grav;
		if(footY>=Game.HEIGHT/Game.SCALE){
			bounce=0;
			footY = (int) (Game.HEIGHT/Game.SCALE);
			yPlayerVel = 0;
			standing = true;
			state=0;
		}
		if(line1.collide(lastX, lastY, footX, footY)){
			bounce=0;
			footY=line1.placeOn(footX);
			yPlayerVel = 0;
			standing = true;
			state=0;
		}
		if (bounce<3){standing=true;}
		lastX=footX;
		lastY=footY;
			
		if(!Manager.keyboard.up) jumping = 0;
		if((standing && Manager.keyboard.up)||jumping!=0){
			//jumping
			jumping ++;
			rolling = 0;
			if (jumping==1) {
				yPlayerVel = -10;
				state = 1;
			}
			if (jumping>=4) yPlayerVel -= 1.25-jumping/12;
			if (yPlayerVel>=0) jumping = 0;
		}
		if(yPlayerVel>=0 && Manager.keyboard.up && (state==1 || state==2)){
			yPlayerVel=2;
			state=2;
		}
		
		if(Manager.keyboard.left){
			if(standing){
				xPlayerVel -= .5;
			}else{
				if(state==3){
					xPlayerVel -= .125;
				}else{
					xPlayerVel -= .25;
				}
			}
		}else{
			if(xPlayerVel<0 && standing){
				xPlayerVel += .25;
			}
		}
		if(Manager.keyboard.right){
			if(standing){
				xPlayerVel += .5;
			}else{
				if(state==3){
					xPlayerVel += .125;
				}else{
					xPlayerVel += .25;
				}
			}
		}else{
			if(xPlayerVel>0 && standing){
				xPlayerVel -= .25;
			}
		}
		isRolling=false;
		rollingCD--;
		if(Manager.keyboard.dodge){
			if(standing){
				if(rolling==0 && rollingCD<=0 && (Manager.keyboard.right || Manager.keyboard.left)){
					//start rolling
					rollingCD = 25;
					rolling = 10;
					if(Manager.keyboard.left) rollDir=false;
					if(Manager.keyboard.right) rollDir=true;
				}else if(rolling==0){
					//ducking
					xPlayerVel=0;
					isRolling=true;
				}
			}else{
				if(Manager.keyboard.up){
					//double jumping
					if (state==1 || state ==2){
						jumping=0;
						state=3;
						xPlayerVel=0;
						yPlayerVel=-15;
					}
				}else{
					//diving
					if(state!=5){
						if(Manager.keyboard.left){
							state = 5;
							xPlayerVel=-10;
							yPlayerVel=-5;
						}
						if(Manager.keyboard.right){
							state = 5;
							xPlayerVel=10;
							yPlayerVel=-5;
						}
					}
				}
			}
		}
		
		if(Manager.keyboard.down && !standing && state!=4){
			state=4;
			xPlayerVel=0;
			yPlayerVel=15;
		}
		if (yPlayerVel>playerSpeed*1.5) yPlayerVel = playerSpeed*1.5;
		if (xPlayerVel<-playerSpeed*1.2 && state!=5) xPlayerVel= -playerSpeed*1.2;
		if (xPlayerVel>playerSpeed*1.2 && state!=5) xPlayerVel = playerSpeed*1.2;
		
		if (rolling!=0){
			//actively rolling
			rolling--;
			isRolling=true;
			if(rollDir){
				xPlayerVel=20;
			}else{
				xPlayerVel=-20;
			}
			if(rolling==0 && !Manager.keyboard.right && !Manager.keyboard.left) xPlayerVel = 0;
		}
		
		if(state!=5){
			width=50; 
			height=75;
		}
		if(standing){
			xInset = 0;
			if(xPlayerVel>2) xInset = 50;
			if(xPlayerVel<-2) xInset = 2*50;
			if(isRolling){
				if(rolling%10<5){
					xInset = 6*50;
				}else{
					xInset = 7*50;
				}
			}
		}else{
			xInset = 3*50;
			if(xPlayerVel>5) xInset = 4*50;
			if(xPlayerVel<-5) xInset = 5*50;
			switch(state){
			case 1:
			break;
			case 2:
				if(xPlayerVel>0){
					xInset = 8*50;
				}else{
					xInset = 9*50;
				}
			break;
			case 3:
				xInset = 6*50;
			break;
			case 4:
				xInset = 10*50;
			break;
			case 5:
				width=75;
				height=50;
				xInset = 11*50;
				if(xPlayerVel>0) xInset = 11*50+75;
			break;
			}
		}
		if(lastXInset!=xInset){
			render();
			lastXInset=xInset;
		}
		footX += xPlayerVel;
		footY += yPlayerVel;
		xLocation=(int) (footX*Game.SCALE-.5*width);
		yLocation=(int) (footY*Game.SCALE-height);
	}
}