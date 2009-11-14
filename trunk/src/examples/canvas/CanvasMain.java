package examples.canvas;

import javax.swing.JFrame;

/**
 * This class creates a new window, containing a drawing panel.
 * @author Christopher
 */
public class CanvasMain extends JFrame{

	public CanvasMain(){
		DrawingPanel dPanel = new DrawingPanel();
		this.add(dPanel);
	}

	public static void main(String[] args){
		CanvasMain window = new CanvasMain();
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

}
