package graphics;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollidableObject extends Sprite{
	
	protected double maskAngleError = .05;
	protected double[] maskRadii;
	protected static final double HALFPI = 1.5707;
		
	//constructor for general use
	public CollidableObject(int xLocation, int yLocation, int width, int height, int xInset, int yInset, String path) {
		super(xLocation, yLocation, width, height, xInset, yInset, path);
		//getEdgePoints();
	}

	public boolean collision(CollidableObject co){
		if(getBounds().intersects(co.getBounds())){
			double phi;
			int index;
			int xCenterDif = -(xLocation + width - co.getXLocation() - co.width);
			int yCenterDif = yLocation + height - co.getYLocation() - co.height;
			double distBetweenCenters = Math.sqrt(xCenterDif * xCenterDif + yCenterDif * yCenterDif);
			
			if(xCenterDif > 0){
				phi = Math.atan((double)yCenterDif / xCenterDif) + HALFPI;
			}else if(xCenterDif < 0){
				phi = Math.atan((double)yCenterDif / xCenterDif) + 3 * HALFPI;
			}else if(yCenterDif > 0){
				phi = HALFPI;
			}else{
				phi = 3*HALFPI;
			}
			
			index = (int)(phi / maskAngleError + maskAngleError / 2);
			if(getMaskRadii()[index] + co.getMaskRadii()[index] > distBetweenCenters)
				return true;
		}
		return false;
	}
		
	//hit and update should be overridden if a subclass needs to do something when hit
	public void hit(){
			
	}
	
	public void update(){
		
	}
		
	protected Rectangle getBounds(){
		return new Rectangle(xLocation, yLocation, width, height);
	}
	
	protected void getEdgePoints(){
		List<Double> maxPosXRadii = new ArrayList<Double>();
		List<Double> maxNegXRadii = new ArrayList<Double>();
		List<Double> allPosXRadii = new ArrayList<Double>();
		List<Double> allNegXRadii = new ArrayList<Double>();
		int[][] pixelCoordinates = new int[width*height][2];
		int xx = -width/2;
		int yy = -height/2;
		int j;
		int k;
		for(int x = 0; x < spritePixels.length; x++){
			if(xx == width/2){
				xx = -width/2;
			}
			pixelCoordinates[x][0] = xx;
			xx++;
		}
		for(int y = 0; y < spritePixels.length; y++){
			pixelCoordinates[y][1] = yy;
			if(y % width == 0 && y != 0){
				yy++;
			}
		}
		//find max radius at each value of theta
		for(double theta = -HALFPI; theta < HALFPI; theta += maskAngleError){
			//check the radius of each pixel given theta
			for(int i = 0; i < spritePixels.length; i++){
				int x = pixelCoordinates[i][0];
				int y = pixelCoordinates[i][1];
				if(spritePixels[x + width/2 + (y + height/2) * width] >> 24 != 0x00){
					double pixelAngle = Math.atan((double) y / x);
					if(pixelAngle < theta + maskAngleError && pixelAngle > theta - maskAngleError){
						double radius = Math.sqrt(x * x + y * y);
						if(x > 0 || (x == 0 && y > 0))
							allPosXRadii.add(radius);
						if(x < 0 || (x == 0 && y < 0))
							allNegXRadii.add(radius);
					}
				} 
			}
			maxPosXRadii.add(Collections.max(allPosXRadii));
			maxNegXRadii.add(Collections.max(allNegXRadii));
			allPosXRadii.removeAll(allPosXRadii);
			allNegXRadii.removeAll(allNegXRadii);
		}
		maskRadii = new double[maxPosXRadii.size() + maxNegXRadii.size()];
		j = 0;
		k = maxPosXRadii.size();
		for(double d : maxPosXRadii){
			maskRadii[j++] = d;
		}
		for(double d : maxNegXRadii){
			maskRadii[k++] = d;
		}
	}
	
	public double[] getMaskRadii(){
		return maskRadii;
	}
	
	protected void randomize(){
		
	}
}