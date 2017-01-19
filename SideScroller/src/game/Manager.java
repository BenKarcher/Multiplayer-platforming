package game;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import graphics.Animation;

import input.Keyboard;

public class Manager implements Runnable{
	
	public static JFrame frame;
	public static double ups;
	private static Game game;
	public static Keyboard keyboard;
	private static String title = "pvp";
	private boolean running;
	private Thread thread;
	public static List<Animation> runningAnimations = new ArrayList<Animation>();
	
	public static void main(String[] args){
		Manager manager = new Manager();
		
		game = new Game();
		game.addKeyListener(keyboard);
		game.setFocusable(true);
		frame.setResizable(false);
		frame.setTitle(title);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		manager.start();
	}
	
	public Manager(){
		keyboard = new Keyboard();
		
		frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		//this is a double
		ups = 60.0;
	}
	
	public void run() {
		while(keepPlaying());
		System.exit(0);
	}
	
	private boolean keepPlaying(){
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0/ups;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		
		while(running){
			long currentTime = System.nanoTime();
			delta += (currentTime-lastTime)/ns;
			lastTime = currentTime;
			
			//what to do every update
			while(delta>1){
				keyboard.update();
				game.update();
				game.render();
				updates++;
				delta--;
			}
			frames++;
			if(System.currentTimeMillis() - timer > 1000){
				timer+=1000;
				//System.out.println(updates + "ups," + frames + "fps");
				Manager.frame.setTitle(Manager.title + " | " + updates + "ups," + frames + "fps");
				frames = 0;
				updates = 0;
			}
		}
		//never reached
		return true;
	}
	

	public synchronized void start(){
		running = true;
		thread = new Thread(this, "Display");
		thread.start();
	}
	
	public synchronized void stop(){
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	} 
}
