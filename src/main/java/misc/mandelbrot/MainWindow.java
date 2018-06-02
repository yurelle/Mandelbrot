package misc.mandelbrot;

import java.awt.Color;

import javax.swing.JFrame;

public class MainWindow extends JFrame {
	
	private static ContentCanvas contentCanvas;
	
	public MainWindow() {
		contentCanvas = new ContentCanvas();

//		setSize(App.width+50, App.height+50);//Handled by canvas.setPreferredSize() & JFrame.pack();
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//setContentPane(contentCanvas);
		add(contentCanvas);
		pack();
		
		setVisible(true);
	}

	static void writePixelData(int pixelIndex, Color pixelColor) {
		contentCanvas.writePixelData(pixelIndex, pixelColor);
	}
	
	void clearContentImage() {
		contentCanvas.clearContentImage();
	}

}
