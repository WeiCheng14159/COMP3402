import javax.swing.JFrame;

public class Page {
	protected JFrame frame;
	protected int Window_Width = 500;
	protected int Window_Height = 300;
	protected int window_x = 100;
	protected int window_y = 100;
	
	public Page() {
		
	}
	public JFrame get_frame(){
		return frame;
	}
	public void set_position( int x, int y){
		if ( x>0 && x < 1000 && y > 0 && y < 1000){
			window_x = x;
			window_y = y;
			frame.setLocation(window_x, window_y);
		}
	}
}
