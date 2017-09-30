package misc.mandelbrot;

import java.awt.Color;
import java.util.Stack;

public class App {
	Viewport ORIG_VIEWPORT;
	Viewport CURRENT_VIEWPORT;
	
	Stack<Viewport> viewportHistory = new Stack<Viewport>();
	
	static int NUM_PIXELS;
	static Color[] pixelData;
	static long[] traversalData_R;
	static long[] traversalData_G;
	static long[] traversalData_B;
	static long MOST_PIXEL_TOUCHES_R;
	static long MOST_PIXEL_TOUCHES_G;
	static long MOST_PIXEL_TOUCHES_B;
	
	public App() {
		size(1024, 1024, P2D);
		pixelDensity(1);
		//frameRate(5);
		  
		//Testing--- Test Map Double
		//float f1 = map(10.235, 5.1, 350.678, 2.215, 9.267);
		//double d1 = mapDouble(10.235, 5.1, 350.678, 2.215, 9.267);
		//println("f1: "+f1);
		//println("d1: "+d1);
		//println("f1 == d1: "+(f1 == d1));//Evaluates to "false", but manual inspections shows that the double is simply more accurate on the last digit.
		//Testing---
		
		
		//Init Viewports
		ORIG_VIEWPORT = new Viewport();
		ORIG_VIEWPORT.minX = -2.25;
		ORIG_VIEWPORT.maxX = 0.75;
		ORIG_VIEWPORT.minY = -1.5;
		ORIG_VIEWPORT.maxY = 1.5;
		CURRENT_VIEWPORT = ORIG_VIEWPORT.clone();
		
		//Init Pixel Data
		NUM_PIXELS = width * height;
		pixelData = new Color[NUM_PIXELS];
		traversalData_R = new long[NUM_PIXELS];
		traversalData_G = new long[NUM_PIXELS];
		traversalData_B = new long[NUM_PIXELS];
		MOST_PIXEL_TOUCHES_R = 0;
		MOST_PIXEL_TOUCHES_G = 0;
		MOST_PIXEL_TOUCHES_B = 0;
		//println(pixelData.length);
		
		for (int x=0; x<NUM_PIXELS; x++) {
		  pixelData[x] = new Color(51, 51, 51, 255);
		  traversalData_R[x] = 0;
		  traversalData_G[x] = 0;
		  traversalData_B[x] = 0;
		}
		
		//Trigger first run of the data
		rebuildPixelData();
	}
	
	void draw() {
		background(51);
		// println(frameRate);

		// Render Pixel Data
		loadPixels();
		if (AppVars.RECORD_INTERMEDIATE_CALCULATIONS) {
			// Calculate Average Brightness of Data
			long total = 0;
			for (int x = 0; x < NUM_PIXELS; x++) {
				total += traversalData_G[x];
			}
			float avgBrightness = (float) (total / ((double) NUM_PIXELS));

			if (avgBrightness < 1) {
				avgBrightness = 1;// Only happens when debugging.
			}
			// println(avgBrightness);
			if (avgBrightness > 2) {
				int asdf = 0;
				asdf++;
			}

			for (int x = 0; x < NUM_PIXELS; x++) {
				final long pixelVal = traversalData_G[x];
				// final int brightness = (int) map(pixelVal, 0,
				// MOST_PIXEL_TOUCHES_G, 0, 255);
				// final int brightness = (int) map(pixelVal, 0,
				// avgBrightness*5, 0, 255);
				// final int brightness = min((int) map(pixelVal, 0,
				// MOST_PIXEL_TOUCHES, 0, 512), 255);//Increase brightness
				final int brightness = (int) min(pixelVal, 255);

				if (AppVars.IN_COLOR) {
					int R = (int) traversalData_R[x];
					int G = (int) traversalData_G[x];
					int B = (int) traversalData_B[x];
					pixels[x] = new Color(R, G, B, 255);
				} else {
					// int brightness = (int) traversalData_G[x];
					pixels[x] = new Color(brightness, brightness, brightness, 255);
				}
			}
		} else {
			arrayCopy(pixelData, pixels);
		}
		updatePixels();

		if (mouseDragging) {
			strokeWeight(1);
			stroke(new Color(0, 0, 0, 255));

			// fill(new Color(0, 128, 128, 255));
			// fill(new Color(128, 256, 0, 255));
			// fill(new Color(64, 128, 64, 255));
			fill(new Color(128, 64, 64, 255));

			// Orig (Allow Custom Aspect Ratio)
			// rect(_mouseDragStartX, _mouseDragStartY, mouseX-_mouseDragStartX,
			// mouseY-_mouseDragStartY);

			// Force Square (Maintain aspect Ratio)
			int boxSize = max(mouseX - _mouseDragStartX, mouseY - _mouseDragStartY);
			rect(_mouseDragStartX, _mouseDragStartY, boxSize, boxSize);
		}

		// loadPixels();
		// for (int x=0; x<windowWidth; x++) {
		// for (int y=0; y<windowHeight; y++) {
		// int pixelIndex = x + (y * windowWidth);
		// pixels[pixelIndex] = new Color(51, 51, 51, 255);
		// }
		// }
		// updatePixels();

		// Draw Progress Bar
		if (CALCULATING) {
			strokeWeight(3);
			stroke(new Color(255, 0, 0, 255));
			line(0, CALCULATING_PIXEL_ROW, width, CALCULATING_PIXEL_ROW);
		}

		if (AppVars.SHOW_MENU) {
			// Draw Menu Box
			strokeWeight(1);
			stroke(new Color(0, 0, 0, 255));
			fill(new Color(128, 128, 128, 255));
			rect(0, 0, 280, 245);

			// Paint Settings Status
			int padding = 5;
			textSize(12);
			textAlign(LEFT, TOP);

			final int lineHeight = 20;
			int lineLocation = padding;
			int len = menuText.length;
			for (int x = 0; x < len; x++) {
				// Get Pointers
				String text = menuText[x];
				Boolean val = menuVariables[x].isActive();

				// Paint isActive indicator
				if (val != null) {// Null means the record is stateless
					if (val) {
						fill(new Color(64, 192, 64, 255));
					} else {
						fill(new Color(192, 64, 64, 255));
					}
					ellipse(padding + 10, lineLocation + 8, lineHeight, lineHeight);
				}

				// Write Text
				fill(new Color(255, 255, 255, 255));
				text(text, padding + 25, lineLocation);

				// Advance to next line
				lineLocation += lineHeight;
			}
		}
	}
	
	boolean mouseDragging = false;
	int _mouseDragStartX = 0;
	int _mouseDragStartY = 0;

	void mousePressed() {
		mouseDragging = true;
		_mouseDragStartX = mouseX;
		_mouseDragStartY = mouseY;
	}

	void mouseReleased() {
		// Ensure that we were/are-still dragging
		if (mouseDragging) {
			// Save Previous Viewport
			final Viewport previousViewport = pushCurrentViewport();

			// Determine Drag Space
			final int boxSize = max(mouseX - _mouseDragStartX, mouseY - _mouseDragStartY);
			final int originX = min(mouseX, _mouseDragStartX);
			final int originY = min(mouseY, _mouseDragStartY);

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
				CURRENT_VIEWPORT.minX = map(originX, 0, width, previousViewport.minX, previousViewport.maxX);
				CURRENT_VIEWPORT.minY = map(originY, 0, height, previousViewport.minY, previousViewport.maxY);
				CURRENT_VIEWPORT.maxX = map(originX + boxSize, 0, width, previousViewport.minX, previousViewport.maxX);
				CURRENT_VIEWPORT.maxY = map(originY + boxSize, 0, height, previousViewport.minY, previousViewport.maxY);

				// Calculate New Viewport Data
				rebuildPixelData();
			}
		}

		// Clear State
		clearMouseDragState();
	}

	void clearMouseDragState() {
		mouseDragging = false;
		_mouseDragStartX = 0;
		_mouseDragStartY = 0;
	}

	void keyPressed() {
		if (key == 'h' || key == 'H') {
			// Toggle boolean
			AppVars.SHOW_MENU = !AppVars.SHOW_MENU;
		} else if (key == 'r' || key == 'R') {
			rebuildPixelData();
		} else if (key == 'i' || key == 'I') {
			// Toggle boolean
			AppVars.RECORD_INTERMEDIATE_CALCULATIONS = !AppVars.RECORD_INTERMEDIATE_CALCULATIONS;

			// Set Default Resolution
			AppVars.MAX_ITERATIONS_INDEX = 12;

			// Rebuild Data
			rebuildPixelData();
		} else if (key == 'c' || key == 'C') {
			// Toggle boolean
			AppVars.IN_COLOR = !AppVars.IN_COLOR;

			// Set Corresponding Default
			AppVars.DONT_FILL_BOUNDED = AppVars.IN_COLOR;

			// Rebuild Data
			rebuildPixelData();
		} else if (key == 'o' || key == 'O') {
			// Toggle boolean
			AppVars.DONT_FILL_BOUNDED = !AppVars.DONT_FILL_BOUNDED;

			// Rebuild Data
			rebuildPixelData();
		} else if (key == 's' || key == 'S') {
			// Toggle boolean
			AppVars.SMOOTH_POINTS = !AppVars.SMOOTH_POINTS;

			// Rebuild Data
			rebuildPixelData();
		} else if (key == 'm' || key == 'M') {
			// Toggle boolean
			AppVars.SIMPLE_MAGNITUDE = !AppVars.SIMPLE_MAGNITUDE;

			// Rebuild Data
			rebuildPixelData();
		} else if (keyCode == 2) {// Home Key
			// Save current viewport
			pushCurrentViewport();

			// Jump To Home Viewport
			CURRENT_VIEWPORT = ORIG_VIEWPORT.clone();

			// Calculate New Viewport Data
			rebuildPixelData();
		} else if (key == BACKSPACE) {
			// Revert To Previous Viewport
			popViewport();

			// Calculate New Viewport Data
			rebuildPixelData();
		} else if (key == ',') {// '<' w/out shift
			// Underflow Check
			if (AppVars.OVERFLOW_THRESHOLD_INDEX <= 0) {
				AppVars.OVERFLOW_THRESHOLD_INDEX = 0;
			} else {
				// Decrement
				AppVars.OVERFLOW_THRESHOLD_INDEX--;

				// Calculate New Viewport Data
				rebuildPixelData();
			}
		} else if (key == '.') {// '>' w/out shift
			// Overflow Check
			if (AppVars.OVERFLOW_THRESHOLD_INDEX >= AppVars.OVERFLOW_THRESHOLD_OPTIONS.length - 1) {
				AppVars.OVERFLOW_THRESHOLD_INDEX = AppVars.OVERFLOW_THRESHOLD_OPTIONS.length - 1;
			} else {
				// Increment
				AppVars.OVERFLOW_THRESHOLD_INDEX++;

				// Calculate New Viewport Data
				rebuildPixelData();
			}
		} else if (key == '-') {
			// Underflow Check
			if (AppVars.MAX_ITERATIONS_INDEX <= 0) {
				AppVars.MAX_ITERATIONS_INDEX = 0;
			} else {
				// Decrement
				AppVars.MAX_ITERATIONS_INDEX--;

				// Calculate New Viewport Data
				rebuildPixelData();
			}
		} else if (key == '+') {
			// Overflow Check
			if (AppVars.MAX_ITERATIONS_INDEX >= AppVars.ITERATION_OPTIONS.length - 1) {
				AppVars.MAX_ITERATIONS_INDEX = AppVars.ITERATION_OPTIONS.length - 1;
			} else {
				// Increment
				AppVars.MAX_ITERATIONS_INDEX++;

				// Calculate New Viewport Data
				rebuildPixelData();
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
			if (index == AppVars.MAX_ITERATIONS_INDEX) {
				// No change
				return;
			}

			// Copy Preset
			AppVars.MAX_ITERATIONS_INDEX = index;

			// Refresh Data With New Threshold (only if the setting changed)
			rebuildPixelData();
		} else if (key == 27) {// Escape key
			// Clear State
			clearMouseDragState();

			// Trap escape key event, so it doesn't exit the program.
			key = 0;
		}
	}

	double mapDouble(final double value, final double srcMin, final double srcMax, final double destMin, final double destMax) {
		// Calculate source range
		final double srcRange = absDouble(srcMax - srcMin);

		// Translate value to be based on source range minimum (rather than
		// zero)
		final double srcAdjValue = value - srcMin;

		// Calculate source percentage
		final double srcPercentage = srcAdjValue / srcRange;

		// Calculate destination range
		final double destRange = absDouble(destMax - destMin);

		// Calculate destination value in range
		final double destRangeVal = srcPercentage * destRange;

		// Translate dest range value to be based on zero (rather than dest
		// range minimimum)
		final double destVal = destRangeVal + destMin;

		// Return result
		return destVal;
	}

	double absDouble(final double value) {
		if (value < 0) {
			return value * -1;
		} else {
			return value;
		}
	}
	
	Viewport pushCurrentViewport() {
		Viewport vp = CURRENT_VIEWPORT.clone();
		viewportHistory.push(vp);
		return vp;
	}

	void popViewport() {
		// Empty Check
		if (viewportHistory.isEmpty()) {
			return;
		}

		// Do Viewport Change
		CURRENT_VIEWPORT = viewportHistory.pop();
	}
	
	Thread workerThread;
	void rebuildPixelData() {
		//thread("calcMandelFrame");
		workerThread = new Thread(this::calcMandelFrame);
	}

	Color avgColors(Color... colors) {
		// Init totals
		int R = 0;
		int G = 0;
		int B = 0;

		// Sum up channel values
		int len = colors.length;
		for (int x = 0; x < len; x++) {
			// Get pointer to current
			Color curColor = colors[x];

			// Sum up color channels
			R += curColor.getRed();
			G += curColor.getGreen();
			B += curColor.getBlue();
		}

		// Build final color
		return new Color(R / len, G / len, B / len, 255);
	}
	
	boolean CALCULATING = false;
	int CALCULATING_PIXEL_ROW = 0;

	void calcMandelFrame() {
		println("getMaxIterations(): " + AppVars.getMaxIterations());

		// Reset Variables - Must reset to zero, since the values are cumulative
		// not objective/direct.
		if (AppVars.RECORD_INTERMEDIATE_CALCULATIONS) {
			MOST_PIXEL_TOUCHES_R = 0;
			MOST_PIXEL_TOUCHES_G = 0;
			MOST_PIXEL_TOUCHES_B = 0;
			int len = NUM_PIXELS;
			for (int x = 0; x < len; x++) {
				traversalData_R[x] = 0;
				traversalData_G[x] = 0;
				traversalData_B[x] = 0;
			}
		}

		// Iterate through all pixels in the viewport
		for (int y = 0; y < height; y++) {
			// Set flag //until you fix the multi-thread issue, we set it inside
			// the loop to make sure that another thread doesn't turn this off
			// while this thread is still running.
			CALCULATING = true;
			CALCULATING_PIXEL_ROW = y + 1;
			for (int x = 0; x < width; x++) {
				// Calculate pixel's location in data array
				final int pixelIndex = getPixelIndexForXY(new PVector(x, y));

				// Derive the starting complex number from the xy coordinate of
				// the pixel.
				ComplexNumber centerPoint = getComplexNumberForXY(new PVector(x, y));

				// Calculate Pixel
				if (AppVars.SMOOTH_POINTS) {
					// Find Adjacent Pixels
					ComplexNumber topPixel = getComplexNumberForXY(new PVector(x, y - 1));
					ComplexNumber bottomPixel = getComplexNumberForXY(new PVector(x, y + 1));
					ComplexNumber leftPixel = getComplexNumberForXY(new PVector(x - 1, y));
					ComplexNumber rightPixel = getComplexNumberForXY(new PVector(x + 1, y));

					// Calculate partial steps towards adjacent pixels
					ComplexNumber upPoint = centerPoint.getOneThirdStepTowards(topPixel);
					ComplexNumber downPoint = centerPoint.getOneThirdStepTowards(bottomPixel);
					ComplexNumber leftPoint = centerPoint.getOneThirdStepTowards(leftPixel);
					ComplexNumber rightPoint = centerPoint.getOneThirdStepTowards(rightPixel);

					// Calculate Mandelbrot values for outward steps
					Color upColor = calcMandelPoint(ComplexNumber.ZERO, upPoint);
					Color downColor = calcMandelPoint(ComplexNumber.ZERO, downPoint);
					Color leftColor = calcMandelPoint(ComplexNumber.ZERO, leftPoint);
					Color rightColor = calcMandelPoint(ComplexNumber.ZERO, rightPoint);

					// Save average color value as pixel
					pixelData[pixelIndex] = avgColors(upColor, downColor, leftColor, rightColor);
				} else {
					pixelData[pixelIndex] = calcMandelPoint(ComplexNumber.ZERO, centerPoint);
				}
			}
		}

		// Reset flag
		CALCULATING = false;

		// println("MOST_PIXEL_TOUCHES_R: "+MOST_PIXEL_TOUCHES_R);
		// println("MOST_PIXEL_TOUCHES_G: "+MOST_PIXEL_TOUCHES_G);
		// println("MOST_PIXEL_TOUCHES_B: "+MOST_PIXEL_TOUCHES_B);
	}

	Color calcMandelPoint(ComplexNumber z, ComplexNumber ORIG_C) {
		// Run through the mandelbrot's recursive calculation for the given
		// complex number.
		int numIterations = 0;
		int iterationLimit = AppVars.getMaxIterations();
		// if (RECORD_INTERMEDIATE_CALCULATIONS && IN_COLOR) {
		// iterationLimit = ITERATION_OPTIONS[];
		// }
		while (numIterations < iterationLimit) {
			numIterations++;
			z = mandelFunc(z, ORIG_C);

			// Log touched pixel
			if (AppVars.RECORD_INTERMEDIATE_CALCULATIONS) {
				final PVector coordinates = getXYForComplexNumber(z);

				// Ensure point is still inside viewport
				if (coordinates != null) {
					final int pixelIndex = getPixelIndexForXY(coordinates);

					// Increment
					if (numIterations < 500) {
						traversalData_R[pixelIndex] += 1;
					}
					if (numIterations < 5000) {
						traversalData_B[pixelIndex] += 1;
					}
					traversalData_G[pixelIndex] += 1;

					// Update rolling maximum
					if (traversalData_R[pixelIndex] > MOST_PIXEL_TOUCHES_R) {
						MOST_PIXEL_TOUCHES_R = traversalData_R[pixelIndex];
					}
					if (traversalData_G[pixelIndex] > MOST_PIXEL_TOUCHES_G) {
						MOST_PIXEL_TOUCHES_G = traversalData_G[pixelIndex];
					}
					if (traversalData_B[pixelIndex] > MOST_PIXEL_TOUCHES_B) {
						MOST_PIXEL_TOUCHES_B = traversalData_B[pixelIndex];
					}
				}
			}

			// Check for overflow
			if (z.getMagnitude() > AppVars.getOverflowThreshold()) {
				break;
			}
		}

		// Calculate Grayscale Brightness
		final float brightness = (float) mapDouble(numIterations, 0, AppVars.getMaxIterations(), 0, 255);

		// Determine Pixel Color
		Color pixelColor;
		if (numIterations == AppVars.getMaxIterations() && AppVars.DONT_FILL_BOUNDED) {
			// Overflow Fill Color

			// pixelColor = new Color(45, 210, 255, 255);
			// pixelColor = new Color(31, 142, 173, 255);
			pixelColor = new Color(0, 0, 0, 255);
			// pixelColor = new Color(255, 255, 255, 255);
		} else {
			if (AppVars.IN_COLOR) {
				pixelColor = getHeatColor(numIterations);
			} else {// Grayscale
				pixelColor = new Color(brightness, brightness, brightness, 255);
			}
		}

		// Check if source number was stable
		// if (numIterations == MAX_NUM_ITERATIONS) {
		// pixelData[pixelIndex] = new Color(0, 0, 0, 255);
		// } else {
		// //pixelData[pixelIndex] = new Color(255, 255, 255, 255);
		// float colorVal = map(numIterations, 0, MAX_NUM_ITERATIONS, 0, 255);
		// pixelData[pixelIndex] = new Color(brightness, brightness, brightness,
		// 255);
		// }

		// Return Pixel Color
		return pixelColor;
	}

	// boolean LOOP_BACK_GRADIENT_RANGE_OVERFLOW = true;
	final int CHUNK_SIZE = 256;
	final int NUM_CHUNKS = 4; // Black-to-Red; Red-to-Yellow; Yellow-to-Green;
								// Green-to-Cyan; Cyan-to-Blue. (Could add blue
								// to purple)
	final int CHUNK_RANGE = NUM_CHUNKS * 256;
	// final int CYCLE_PERIOD = 32;
	final int CYCLE_PERIOD = 64;

	Color getHeatColor(final int val) {
		// Break up into multiple cycles of the gradient for higher contrast.
		final int cycleVal = val % CYCLE_PERIOD;

		// Scale cycle value back up to the full color space
		final int heatVal = (int) mapDouble(cycleVal, 0, CYCLE_PERIOD, 0, CHUNK_RANGE);

		// Scale value to full iteration range
		// int heatVal = (int) map(val, 0, getMaxIterations(), 0, CHUNK_RANGE);

		// Check overflow setting (whether to cycle back, or truncate)
		// if (LOOP_BACK_GRADIENT_RANGE_OVERFLOW) {
		// heatVal %= CHUNK_RANGE;
		// }

		// Calc color
		final int chunkVal = heatVal % CHUNK_SIZE;
		final float heatBand = ((float) heatVal) / CHUNK_SIZE;

		if (heatBand < 1) {// First Band [Cyan-To-White]
			return new Color(chunkVal, 255, 255, 255);
		} else if (heatBand < 2) {// Second Band [White-to-Yellow]
			return new Color(255, 255, 255 - chunkVal, 255);
		} else if (heatBand < 3) {// Third Band [Yellow-To-Black]
			return new Color(255 - chunkVal, 255 - chunkVal, 0, 255);
		} else if (heatBand < 4) {// Fourth Band [Black-To-Cyan]
			return new Color(0, chunkVal, chunkVal, 255);
		}

		/*
		 * if (heatBand < 1) {//First Band [Black-to-Red] return new Color(chunkVal,
		 * 0, 0, 255); } else if (heatBand < 2) {//Second Band [Red-to-Yellow]
		 * return new Color(255, chunkVal, 0, 255); } else if (heatBand < 3)
		 * {//Third Band [Yellow-to-Green] return new Color(255-chunkVal, 255, 0,
		 * 255); } else if (heatBand < 4) {//Fourth Band [Green-to-Cyan] return
		 * new Color(0, 255, chunkVal, 255); } else if (heatBand < 5) {//Fifth Band
		 * [Cyan-to-Blue] return new Color(0, 255-chunkVal, 255, 255); }
		 */
		return new Color(0, 0, 0, 255);
	}

	// TODO: go back and do a second kind of rendering where instead of
	// calculating each pixel in
	// order, it first calculates the odd pixels, then the even ones, to
	// basically give a half-res
	// preview of the whole canvas in half the time.
	int getPixelIndexForXY(final PVector point) {
		return (int) (point.x + (point.y * width));
	}

	ComplexNumber getComplexNumberForXY(final PVector point) {
		return new ComplexNumber(map(point.x, 0, width, CURRENT_VIEWPORT.minX, CURRENT_VIEWPORT.maxX),
				map(point.y, 0, height, CURRENT_VIEWPORT.minY, CURRENT_VIEWPORT.maxY));
	}

	PVector getXYForComplexNumber(final ComplexNumber point) {
		// Check if point is outside viewport
		if (point.real <= CURRENT_VIEWPORT.minX || point.real >= CURRENT_VIEWPORT.maxX
				|| point.imaginary <= CURRENT_VIEWPORT.minY || point.imaginary >= CURRENT_VIEWPORT.maxY) {

			return null;
		}
		return new PVector((int) map(point.real, CURRENT_VIEWPORT.minX, CURRENT_VIEWPORT.maxX, 0, width),
				(int) map(point.imaginary, CURRENT_VIEWPORT.minY, CURRENT_VIEWPORT.maxY, 0, height));
	}

	ComplexNumber mandelFunc(final ComplexNumber c, final ComplexNumber ORIG_C) {
		return c.getSquaredVal().add(ORIG_C);
	}
	
    public static void main( String[] args ) {
    }
}
