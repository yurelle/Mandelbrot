package misc.mandelbrot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public class ContentCanvas extends JPanel implements KeyListener, MouseInputListener {
	private AppContext appContext;
	
	private Color BG_COLOR;
	private BufferedImage contentImage;
	private ScheduledExecutorService fpsTimer = new ScheduledThreadPoolExecutor(1);
	
	public ContentCanvas() {
		appContext = App.APP_CONTEXT;
		
		setSize(appContext.width, appContext.height);
		setPreferredSize(new Dimension(appContext.width, appContext.height));
		
		setFocusable(true); //Required for keyListener to work on JPanels
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		
		BG_COLOR = new Color (51,51,51);
		contentImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D imgG2 = (Graphics2D) contentImage.getGraphics();
		imgG2.setColor(BG_COLOR);
		imgG2.fillRect(0, 0, getWidth(), getHeight());
		
		//Set Goal FPS
		fpsTimer.scheduleAtFixedRate(this::repaint, 0L, 1000L/30, TimeUnit.MILLISECONDS);
	}
	
	void writePixelData(int pixelIndex, int r, int g, int b) {
		writePixelData(pixelIndex, new Color(r,g,b));
	}
	
	void writePixelData(int pixelIndex, Color pixelColor) {
		contentImage.setRGB(pixelIndex % getWidth(), pixelIndex / getWidth(), pixelColor.getRGB());
	}
	
	//void draw() {
	public void paintComponent(Graphics g_raw) {
        super.paintComponent(g_raw);
        this.getBounds();
		Graphics2D g = (Graphics2D) g_raw;
		
		// Render Pixel Data
		if (appContext.RECORD_INTERMEDIATE_CALCULATIONS) {
			// Calculate Average Brightness of Data
//			double avgBrightness = appContext.traversalData_G	.parallelStream()
//														.mapToDouble(d -> d)
//														.average()
//														.getAsDouble();
//
//			if (avgBrightness < 1) {
//				avgBrightness = 1;// Only happens when debugging.
//			}
//			// println(avgBrightness);
//			if (avgBrightness > 2) {
//				int asdf = 0;
//				asdf++;//For debugging
//			}

			for (int x = 0; x < appContext.NUM_PIXELS; x++) {
				final long pixelVal = appContext.traversalData_G.get(x).get();
				// final int brightness = (int) map(pixelVal, 0, MOST_PIXEL_TOUCHES_G, 0, 255);
				// final int brightness = (int) map(pixelVal, 0, avgBrightness*5, 0, 255);
				// final int brightness = min((int) map(pixelVal, 0, MOST_PIXEL_TOUCHES, 0, 512), 255);//Increase brightness
				final int brightness = (int) Math.min(pixelVal, 255);

				if (appContext.IN_COLOR) {
					int R = (int) appContext.traversalData_R.get(x).longValue();
					int G = (int) appContext.traversalData_G.get(x).longValue();
					int B = (int) appContext.traversalData_B.get(x).longValue();
					contentImage.setRGB(x % getWidth(), x / getWidth(), new Color(R, G, B).getRGB());
				} else {
					// int brightness = (int) traversalData_G[x];
					contentImage.setRGB(x % getWidth(), x / getWidth(), new Color(brightness, brightness, brightness).getRGB());
				}
			}
		} else {
			//arrayCopy(appContext.pixelData, pixels);
			g.drawImage(contentImage, 0, 0, this);
		}
//		updatePixels();

		if (mouseDragging) {
			g.setStroke(new BasicStroke(1));
			
			// Force Square (Maintain aspect Ratio)
			int boxSize = Math.max(mouseX - _mouseDragStartX, mouseY - _mouseDragStartY);
			
			//-- Fill box
			// fill(new Color(0, 128, 128, 255));
			// fill(new Color(128, 256, 0, 255));
			// fill(new Color(64, 128, 64, 255));
			g.setColor(new Color(128, 64, 64, 255));
			g.fillRect(_mouseDragStartX, _mouseDragStartY, boxSize, boxSize);
			
			//-- Draw border
			g.setColor(new Color(0, 0, 0, 255));
			g.drawRect(_mouseDragStartX, _mouseDragStartY, boxSize, boxSize);
		}

		// Draw Progress Bar
		if (appContext.CALCULATING) {
			g.setStroke(new BasicStroke(3));
			g.setColor(new Color(255, 0, 0, 255));
			g.drawLine(0, appContext.CALCULATING_PIXEL_ROW, getWidth(), appContext.CALCULATING_PIXEL_ROW);
		}

		if (appContext.SHOW_MENU) {
		//--Draw Menu Box
			g.setStroke(new BasicStroke(1));
			
			//Background fill
			g.setColor(new Color(128, 128, 128, 255));
			g.fillRect(0, 0, 280, 245);

			//Border
			g.setColor(new Color(0, 0, 0, 255));
			g.drawRect(0, 0, 280, 245);

		//--Paint Settings Status
			int padding = 5;
			g.setFont(new Font("Tahoma", Font.PLAIN, 12));
			//textAlign(LEFT, TOP);

			final int lineHeight = 20;
			final int textHeight = 14;
			final int ledDiameter = lineHeight;
			int lineLocation = 2;
			int len = appContext.menuText.length;
			for (int x = 0; x < len; x++) {
				// Get Pointers
				String text = appContext.menuText[x];
				Boolean val = appContext.menuVariables[x].isActive();

				// Paint isActive indicator
				if (val != null) {// Null means the record is stateless
					if (val) {
						g.setColor(new Color(64, 192, 64, 255));
					} else {
						g.setColor(new Color(192, 64, 64, 255));
					}
					g.fillOval(padding, lineLocation, ledDiameter, ledDiameter);

					//Draw Border
					g.setColor(new Color(0, 0, 0, 255));
					g.drawOval(padding, lineLocation, ledDiameter, ledDiameter);
				}

				// Write Text
				g.setColor(new Color(255, 255, 255, 255));
				g.drawString(text, padding + 25, lineLocation + textHeight);

				// Advance to next line
				lineLocation += lineHeight;
			}
		}
	}
	
	boolean mouseDragging = false;
	int _mouseDragStartX = 0;
	int _mouseDragStartY = 0;
	int mouseX = 0;
	int mouseY = 0;

	public void mousePressed(MouseEvent mouseEvent) {
		mouseDragging = true;
		_mouseDragStartX = mouseEvent.getX();
		_mouseDragStartY = mouseEvent.getY();
	}

	public void mouseReleased(MouseEvent mouseEvent) {
		mouseX = mouseEvent.getX();
		mouseY = mouseEvent.getY();
		
		// Ensure that we were/are-still dragging
		if (mouseDragging) {
			// Save Previous Viewport
			final Viewport previousViewport = appContext.pushCurrentViewport();

			// Determine Drag Space
			final int boxSize = Math.max(mouseX - _mouseDragStartX, mouseY - _mouseDragStartY);
			final int originX = Math.min(mouseX, _mouseDragStartX);
			final int originY = Math.min(mouseY, _mouseDragStartY);

			// Ensure Minimum Field (i.e. Prevent Click; produces useless zoom)
			if (boxSize > 0) {
				// Set New Viewport (Allow Custom Aspect Ratio)
				// CURRENT_VIEWPORT.minX = map(min(mouseX, _mouseDragStartX), 0,
				// width, previousViewport.minX, previousViewport.maxX);
				// CURRENT_VIEWPORT.maxX = map(max(mouseX, _mouseDragStartX), 0,
				// width, previousViewport.minX, previousViewport.maxX);
				// CURRENT_VIEWPORT.minY = map(min(mouseY, _mouseDragStartY), 0,
				// height, previousViewport.minY, previousViewport.maxY);
				// CURRENT_VIEWPORT.maxY = map(max(mouseY, _mouseDragStartY), 0,
				// height, previousViewport.minY, previousViewport.maxY);

				// Set New Viewport (Maintain aspect Ratio)
				appContext.CURRENT_VIEWPORT.minX = MathALU.mapDouble(originX, 0, appContext.width, previousViewport.minX, previousViewport.maxX);
				appContext.CURRENT_VIEWPORT.minY = MathALU.mapDouble(originY, 0, appContext.height, previousViewport.minY, previousViewport.maxY);
				appContext.CURRENT_VIEWPORT.maxX = MathALU.mapDouble(originX + boxSize, 0, appContext.width, previousViewport.minX, previousViewport.maxX);
				appContext.CURRENT_VIEWPORT.maxY = MathALU.mapDouble(originY + boxSize, 0, appContext.height, previousViewport.minY, previousViewport.maxY);

				// Calculate New Viewport Data
				appContext.rebuildPixelData();
			}
		}

		// Clear State
		clearMouseDragState();
	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent) {
		mouseX = mouseEvent.getX();
		mouseY = mouseEvent.getY();
	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent) {
		mouseX = mouseEvent.getX();
		mouseY = mouseEvent.getY();
	}

	void clearMouseDragState() {
		mouseDragging = false;
		_mouseDragStartX = 0;
		_mouseDragStartY = 0;
	}

	public void mouseClicked(MouseEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent keyEvent) {
		char key = keyEvent.getKeyChar();
		int keyCode = keyEvent.getKeyCode();
		
		if (key == 'h' || key == 'H') {
			// Toggle boolean
			appContext.SHOW_MENU = !appContext.SHOW_MENU;
		} else if (key == 'r' || key == 'R') {
			appContext.rebuildPixelData();
		} else if (key == 'i' || key == 'I') {
			// Toggle boolean
			appContext.RECORD_INTERMEDIATE_CALCULATIONS = !appContext.RECORD_INTERMEDIATE_CALCULATIONS;

			// Set Default Resolution
			appContext.MAX_ITERATIONS_INDEX = 12;

			// Rebuild Data
			appContext.rebuildPixelData();
		} else if (key == 'c' || key == 'C') {
			// Toggle boolean
			appContext.IN_COLOR = !appContext.IN_COLOR;

			// Set Corresponding Default
			appContext.DONT_FILL_BOUNDED = appContext.IN_COLOR;

			// Rebuild Data
			appContext.rebuildPixelData();
		} else if (key == 'o' || key == 'O') {
			// Toggle boolean
			appContext.DONT_FILL_BOUNDED = !appContext.DONT_FILL_BOUNDED;

			// Rebuild Data
			appContext.rebuildPixelData();
		} else if (key == 's' || key == 'S') {
			// Toggle boolean
			appContext.SMOOTH_POINTS = !appContext.SMOOTH_POINTS;

			// Rebuild Data
			appContext.rebuildPixelData();
		} else if (key == 'm' || key == 'M') {
			// Toggle boolean
			appContext.SIMPLE_MAGNITUDE = !appContext.SIMPLE_MAGNITUDE;

			// Rebuild Data
			appContext.rebuildPixelData();
		} else if (keyCode == 2) {// Home Key
			// Save current viewport
			appContext.pushCurrentViewport();

			// Jump To Home Viewport
			appContext.CURRENT_VIEWPORT = appContext.ORIG_VIEWPORT.clone();

			// Calculate New Viewport Data
			appContext.rebuildPixelData();
		} else if (key == KeyEvent.VK_BACK_SPACE) {
			// Revert To Previous Viewport
			appContext.popViewport();

			// Calculate New Viewport Data
			appContext.rebuildPixelData();
		} else if (key == ',') {// '<' w/out shift
			// Underflow Check
			if (appContext.OVERFLOW_THRESHOLD_INDEX <= 0) {
				appContext.OVERFLOW_THRESHOLD_INDEX = 0;
			} else {
				// Decrement
				appContext.OVERFLOW_THRESHOLD_INDEX--;

				// Calculate New Viewport Data
				appContext.rebuildPixelData();
			}
		} else if (key == '.') {// '>' w/out shift
			// Overflow Check
			if (appContext.OVERFLOW_THRESHOLD_INDEX >= appContext.OVERFLOW_THRESHOLD_OPTIONS.length - 1) {
				appContext.OVERFLOW_THRESHOLD_INDEX = appContext.OVERFLOW_THRESHOLD_OPTIONS.length - 1;
			} else {
				// Increment
				appContext.OVERFLOW_THRESHOLD_INDEX++;

				// Calculate New Viewport Data
				appContext.rebuildPixelData();
			}
		} else if (key == '-') {
			// Underflow Check
			if (appContext.MAX_ITERATIONS_INDEX <= 0) {
				appContext.MAX_ITERATIONS_INDEX = 0;
			} else {
				// Decrement
				appContext.MAX_ITERATIONS_INDEX--;

				// Calculate New Viewport Data
				appContext.rebuildPixelData();
			}
		} else if (key == '+') {
			// Overflow Check
			if (appContext.MAX_ITERATIONS_INDEX >= appContext.ITERATION_OPTIONS.length - 1) {
				appContext.MAX_ITERATIONS_INDEX = appContext.ITERATION_OPTIONS.length - 1;
			} else {
				// Increment
				appContext.MAX_ITERATIONS_INDEX++;

				// Calculate New Viewport Data
				appContext.rebuildPixelData();
			}
		} else if (key >= '0' && key <= '9') {
			// Convert to array index
			int index = key - '0';

			// Treat Zero As Ten
			if (index == 0) {
				index = 10;
			}

			// Slide left 1 digit to get zero-based again.
			index--;

			// Ensure change
			if (index == appContext.MAX_ITERATIONS_INDEX) {
				// No change
				return;
			}

			// Copy Preset
			appContext.MAX_ITERATIONS_INDEX = index;

			// Refresh Data With New Threshold (only if the setting changed)
			appContext.rebuildPixelData();
		} else if (key == 27) {// Escape key
			// Clear State
			clearMouseDragState();

			// Trap escape key event, so it doesn't exit the program.
			key = 0;
		}
	}
}
