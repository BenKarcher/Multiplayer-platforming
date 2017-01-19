package input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
	
	//the highest key code is 222
	public boolean[] keyPress = new boolean[223];
	public boolean up, down, left, right, dodge, shoot;
	
	@Override
	public void keyPressed(KeyEvent e) {
		keyPress[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keyPress[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void update(){
		up = keyPress[KeyEvent.VK_UP] || keyPress[KeyEvent.VK_W];
		down = keyPress[KeyEvent.VK_DOWN] || keyPress[KeyEvent.VK_S];
		left = keyPress[KeyEvent.VK_LEFT] || keyPress[KeyEvent.VK_A];
		right = keyPress[KeyEvent.VK_RIGHT] || keyPress[KeyEvent.VK_D];
		dodge = keyPress[KeyEvent.VK_Z] || keyPress[KeyEvent.VK_K];
		shoot = keyPress[KeyEvent.VK_X] || keyPress[KeyEvent.VK_L];
	}
	
	public void reset(){
		for(int i=0; i<keyPress.length; i++){
			keyPress[i] = false;
		}
	}
}
