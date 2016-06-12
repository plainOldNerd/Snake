package Snake;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.ImageIcon;

import java.util.Random;
import java.util.ArrayList;
import java.util.Date;

public class Snake extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	final int width = 60;
	final int height = 28;
	final int timerDelay = 100;
	final ImageIcon groundIcon = new ImageIcon("Images/ground.jpg");
	final ImageIcon wallIcon = new ImageIcon("Images/wall.jpg");
	final ImageIcon snakeIcon = new ImageIcon("Images/snake.jpg");
	final ImageIcon mouseIcon = new ImageIcon("Images/mouse.jpg");
	
	JLabel[][] labels = new JLabel[height][width];
	GridLayout gl = new GridLayout(height,width);
	Timer t = new Timer(timerDelay, new TimedMoves());
	
	//  start the snake moving upwards.  Then keep track of its head movement
	//  direction
	int direction = KeyEvent.VK_UP;
	//  to be used with keys to control speed
	int speed = 3*timerDelay;
	/*
	 * -1 will be used for a wall
	 *  0 will be used for empty
	 *  1 will be used for the snake
	 *  2 will be used for a mouse
	 */
	int[][] data = new int[height][width];
	/*
	 *  The 0th position in the ArrayList will be the snake's head.
	 *  The last position will be the tail.
	 */
	ArrayList<Coords> snake = new ArrayList<Coords>();
	boolean stillPlaying = true;
	int time = 0;
	boolean tailGrowing = false;
	Random r = new Random( new Date().getTime() );
	
	public Snake()
	{
		setLayout( gl );
		
		for(int i=0; i<height; ++i)
		{
			for(int j=0; j<width; ++j)
			{
				if( i==0 || j==0 || i==height-1 || j==width-1 )
				{
					data[i][j] = -1;
					labels[i][j] = new JLabel( wallIcon );
				}
				else
				{
					data[i][j] = 0;
					labels[i][j] = new JLabel( groundIcon );
				}
				add(labels[i][j]);
			}
		}
		
		labels[(int) height/2-1][(int) width/3].setIcon( snakeIcon );
		data[(int) height/2-1][(int) width/3] = 1;
		snake.add( 0, new Coords( (int) height/2-1, (int) width/3 ) );
		//  Remember that the 0th position is the head, and the snake starts upwards
		labels[(int) height/2][(int) width/3].setIcon( snakeIcon );
		data[(int) height/2][(int) width/3] = 1;
		snake.add( 1, new Coords( (int) height/2, (int) width/3 ) );
		labels[(int) height/2+1][(int) width/3].setIcon( snakeIcon );
		data[(int) height/2+1][(int) width/3] = 1;
		snake.add( 2, new Coords( (int) height/2+1, (int) width/3 ) );
		
		int mouseAtX, mouseAtY;
		do
		{
			mouseAtX = 1 + r.nextInt(height-2);
			mouseAtY = 1 + r.nextInt(width-2);
		} while( data[mouseAtX][mouseAtY] != 0 );
		data[mouseAtX][mouseAtY] = 2;
		labels[mouseAtX][mouseAtY].setIcon( mouseIcon );
		
		setVisible(true);
	}
	
	public void playGame()
	{	t.start();	}
	
	public void keyTyped(KeyEvent e) 
	{
		int key = e.getKeyCode();
		
		switch(key)
		{
			case KeyEvent.VK_P:
			{
				if( t.isRunning() )
				{
					t.stop();
					t.setInitialDelay(0);
				}
				else if( stillPlaying )
					t.restart();
				break;
			}
			case KeyEvent.VK_UP:
			{
				if(direction != KeyEvent.VK_DOWN)
					direction = KeyEvent.VK_UP;
				break;
			}
			case KeyEvent.VK_DOWN:
			{
				if(direction != KeyEvent.VK_UP)
					direction = KeyEvent.VK_DOWN;
				break;
			}
			case KeyEvent.VK_LEFT:
			{
				if(direction != KeyEvent.VK_RIGHT)
					direction = KeyEvent.VK_LEFT;
				break;
			}
			case KeyEvent.VK_RIGHT:
			{
				if(direction != KeyEvent.VK_LEFT)
					direction = KeyEvent.VK_RIGHT;
				break;
			}
			case KeyEvent.VK_S:
			{
				if( speed != 3*timerDelay )
					speed += timerDelay;
				break;
			}
			case KeyEvent.VK_F:
			{
				if( speed != timerDelay )
					speed -= timerDelay;
				break;
			}
			default:
		}
	}
	
	public class TimedMoves implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			time += timerDelay;
			
			if(time % speed == 0)
			{
				Coords nextSquare = null;
				int headAtX = snake.get(0).getRow(), headAtY = snake.get(0).getCol();
				
				if( direction == KeyEvent.VK_UP )
					nextSquare = new Coords( headAtX-1, headAtY );
				if( direction == KeyEvent.VK_DOWN )
					nextSquare = new Coords( headAtX+1, headAtY );
				if( direction == KeyEvent.VK_LEFT )
					nextSquare = new Coords( headAtX, headAtY-1 );
				if( direction == KeyEvent.VK_RIGHT )
					nextSquare = new Coords( headAtX, headAtY+1 );
				
				//  See if the snake has hit a wall or its tail
				if( data[nextSquare.getRow()][nextSquare.getCol()] == -1 
						|| data[nextSquare.getRow()][nextSquare.getCol()] == 1 )
				{
					t.stop();
					stillPlaying = false;
					return;
				}
				
				//  See if the snake has got the mouse
				if( data[nextSquare.getRow()][nextSquare.getCol()] == 2 )
					tailGrowing = true;
				
				//  Set the data, ArrayList and JLabels as appropriate for the 
				//  head of the snake
				snake.add( 0, new Coords( nextSquare.getRow(), nextSquare.getCol() ) );
				data[nextSquare.getRow()][nextSquare.getCol()] = 1;
				labels[nextSquare.getRow()][nextSquare.getCol()].setIcon(snakeIcon);
				
				if( !tailGrowing )
				{
					//  Make sure the tail moves on
					Coords tail = snake.get(snake.size()-1);
					int tailAtX = tail.getRow(), tailAtY = tail.getCol();
					
					labels[tailAtX][tailAtY].setIcon(groundIcon);
					data[tailAtX][tailAtY] = 0;
					snake.remove(snake.size()-1);
				}
				else
				{
					//  Reset the position of the mouse
					int mouseAtX, mouseAtY;
					
					do
					{
						mouseAtX = 1 + r.nextInt(height-2);
						mouseAtY = 1 + r.nextInt(width-2);
					} while( data[mouseAtX][mouseAtY] != 0 );
					
					data[mouseAtX][mouseAtY] = 2;
					labels[mouseAtX][mouseAtY].setIcon(mouseIcon);
					
					tailGrowing = false;
				}
			}
			
			if( time == 3*timerDelay )
				time = 0;
		}
	}
}