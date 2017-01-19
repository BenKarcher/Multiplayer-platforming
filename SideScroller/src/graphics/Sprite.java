package graphics;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	public int width;
	public int height;
	
	protected int xInset;
	protected int yInset;
	protected int originalXInset;
	protected int originalYInset;
	protected int xLocation;
	protected int yLocation;
	protected String path;
	protected int[] spritePixels;
	
	//constructor for general use
	public Sprite(int xLocation, int yLocation, int width, int height, int xInset, int yInset, String path){
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.width = width;
		this.height = height;
		this.xInset = xInset;
		this.yInset = yInset;
		this.path = path;
		spritePixels = new int[width*height];
		render();
	}
	
	//for animations
	public Sprite(int xLocation, int yLocation, int width, int height, String path){
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.width = width;
		this.height = height;
		this.path = path;
		xInset = 0;
		yInset = 0;
		spritePixels = new int[width*height];
		render();
	}
	
	protected void render() {
		try {
			BufferedImage spriteSheet = ImageIO.read(Sprite.class.getResource(path));
			spriteSheet.getRGB(xInset, yInset, width, height, spritePixels, 0, width);
		} catch (Exception e) {
			System.err.println("asdfasd");
			e.printStackTrace();
		}
	}
	
	protected void render(int[] destination, int xInset, int yInset) {
		try {
			BufferedImage spriteSheet = ImageIO.read(Sprite.class.getResource(path));
			spriteSheet.getRGB(xInset, yInset, width, height, destination, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getSpriteBufferedImage(){
		return createSpriteBufferedImage(spritePixels);
	}
	

	public BufferedImage createSpriteBufferedImage(int[] location){
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] bufferedImagePixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		for(int i=0;i<location.length;i++){
			bufferedImagePixels[i] = location[i];
		}
		return image;
	}

	public int getXLocation() {
		return xLocation;
	}

	public int getYLocation() {
		return yLocation;
	}
	
	public void setXLocation(int xLocation) {
		this.xLocation = xLocation;
	}

	public void setYLocation(int yLocation) {
		this.yLocation = yLocation;
	}
}
