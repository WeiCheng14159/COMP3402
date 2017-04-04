import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class DemoThreadGUI {

	public static void main(String [] args) {
		DemoThreadGUI app = new DemoThreadGUI();
		app.go();
	}

	public void go() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				generateGUI();
			}
		});		
	}
	
	public void generateGUI() {
		JFrame frame = new JFrame("Demo");
		frame.add(new MyPanel());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	/*MyPanel is the inner class of DemoTHreadGUI*/
	class MyPanel extends JPanel implements MouseListener {
		private int x,y;
		private int r;
		public MyPanel() {
			this.setPreferredSize(new Dimension(100,100));
			this.addMouseListener(this);
			x = 50;
			y = 50;
			r = 25;
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.fillOval(x-r,y-r,r*2,r*2);
		}
		
		public void mouseClicked(MouseEvent event) {
			new Animation(this, event).start();
		}
		
		public void mouseEntered(MouseEvent event) {}
		public void mouseExited(MouseEvent event) {}
		public void mousePressed(MouseEvent event) {}
		public void mouseReleased(MouseEvent event) {}
		
		/*Animation class is the inner class of MyPanel*/
		public class Animation extends Thread{

			private MouseEvent mouse_event; 
			private int step_size; 
			
			public Animation(JPanel panel, MouseEvent event) {
				// TODO Auto-generated constructor stub
				this.mouse_event = event;
				this.step_size = 5;
			}
			
			public void run(){
				int targetX = this.mouse_event.getX();
				int targetY = this.mouse_event.getY();
				
				for (int i = 0 ; i < this.step_size ; i++){
					x = ( x + targetX ) / 2;
					y = ( y + targetY ) / 2;
					repaint();
					try{
						Thread.sleep(100);
					}catch(InterruptedException e){
						e.printStackTrace();
					}
				}
			}
		}	
	}
}


