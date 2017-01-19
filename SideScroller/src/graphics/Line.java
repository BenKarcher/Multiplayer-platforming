package graphics;

public class Line {
	
	public enum Type{
		FLOOR, PLATFORM, CEILING, WALL, SLOPE
	}
	private Type type;
	private int x1, x2, y1, y2;
	private double slope;
	private boolean vertical = false;
	
	public Line(int x1, int y1, int x2, int y2, Type type){
		this.type = type;
		if(x1<x2){
			this.x1= x1;
			this.x2= x2;
			this.y1= y1;
			this.y2= y2;
		}else{
			this.x2= x1;
			this.x1= x2;
			this.y2= y1;
			this.y1= y2;
		}
		if(x1==x2){
			vertical = true;
			if(y1>y2){
				this.y1=y2;
				this.y2=y1;
			}
		}else{
			slope=(1.0*y2-y1)/(x2-x1);
		}
	}
	
	public boolean collide(int px1, int py1, int px2, int py2){
		if(vertical){
			return y1<py2 && y2>py2 && (px1>x1^px2>x1);
		}
		if(px2 > x1 && px2 < x2){
			return above(px1,py1)&&!above(px2,py2);
		}
		return false;
	}
	
	private boolean above(int x, int y){
		return y<=(y1+slope*(x-x1));
	}
	public int placeOn(int xPos){
		return (int) (y1+slope*(xPos-x1));
	}
}
