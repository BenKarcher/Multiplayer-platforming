package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import graphics.Player;

public class Game extends Canvas{
	private static final long serialVersionUID = 8224942629660518049L;
	public static int WIDTH = 1600;
	public static int HEIGHT = 900;
	public static double SCALE = 1;
	public static Player player;
	
	public Game(){
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth()-100;
		double height = screenSize.getHeight()-100;
		SCALE = width/WIDTH;
		if (SCALE>height/HEIGHT){SCALE=height/HEIGHT;}	
		WIDTH*=SCALE;
		HEIGHT*=SCALE;
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		player = new Player((int)(Game.WIDTH*SCALE-100)/2,(int)(Game.HEIGHT*SCALE-100), 50, 75, 0, 0, "/textures/smaller.png");
	}
	
	void update() {
		player.update();
	}
	
	void render() {
		BufferStrategy bs = getBufferStrategy();
		if(bs==null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		
		//add graphics
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(player.getSpriteBufferedImage(), player.getXLocation(), player.getYLocation(), null);
        g.setColor(Color.BLACK);
        g.drawLine(0, 400, 500, 600);
		g.dispose();
		bs.show();
	}
	
	void reset(){
	}
}
