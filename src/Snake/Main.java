package Snake;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Main 
{
	static JFrame f = new JFrame();
	static final Snake s = new Snake();
	
	public static void main(String[] args) 
	{		
		f.add(s);
		f.setSize(1000, 500);
		f.setResizable(false);
		f.setVisible(true);
		KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		kfm.addKeyEventDispatcher(new Main().new KeyEventDisp());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.playGame();
	}
	
	/*  Needed because the default alternative is that each JLabel listens for
	    KeyEvents but not the outer JPanel.  We want the outer JPanel to get 
	    KeyEvents
	*/
	public class KeyEventDisp implements KeyEventDispatcher
	{
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) 
		{
			if(e.getID() == KeyEvent.KEY_PRESSED)
				s.keyTyped( e );
			return false;
		}
	}
}