package misc.mandelbrot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicLong;

public class AppContext {
	//int MAX_NUM_ITERATIONS = 1250;
	//int MAX_NUM_ITERATIONS = 255;
	//int MAX_NUM_ITERATIONS = 150;
	int MAX_ITERATIONS_INDEX = 2;
	int[] ITERATION_OPTIONS = new int[]{5, 10, 25, 37, 50, 75, 100, 150, 200, 255, 512, 765, 1024, 2048, 4096, 8192, 16384, 32768};
	
	//OVERFLOW_THRESHOLD = 16;
	//OVERFLOW_THRESHOLD = 2;
	int OVERFLOW_THRESHOLD_INDEX = 11;
	int[] OVERFLOW_THRESHOLD_OPTIONS = new int[]{1,2,3,4,5,6,7,8,9,10, 16, 32, 64, 128, 256, 512, 765, 1024};
	boolean SHOW_MENU = true;
	boolean RECORD_INTERMEDIATE_CALCULATIONS = false;
	boolean DONT_FILL_BOUNDED = false;
	boolean IN_COLOR = false;
	boolean SMOOTH_POINTS = false;
	boolean SIMPLE_MAGNITUDE = false;
	
	public int width;
	public int height;

	boolean CALCULATING = false;
	int CALCULATING_PIXEL_ROW = 0;

	// boolean LOOP_BACK_GRADIENT_RANGE_OVERFLOW = true;
	final int CHUNK_SIZE = 256;
	final int NUM_CHUNKS = 4;	// Black-to-Red; Red-to-Yellow; Yellow-to-Green;
								// Green-to-Cyan; Cyan-to-Blue. (Could add blue
								// to purple)
	final int CHUNK_RANGE = NUM_CHUNKS * 256;
	// final int CYCLE_PERIOD = 32;
	final int CYCLE_PERIOD = 64;

	String[] menuText = new String[]{
		"[H] Help - Show/Hide this menu",
		"[R] Rebuild Pixel Data",
		"[I] Record Intermediate Calculations",
		"[C] Render in Color",
		"[O] Fill overflow regions",
		"[S] Calculate 'Smooth' point data",
		"[M] Use 'simple' magnitude approximation",
		"[+] Increase Resolution",
		"[-] Decrease Resolution",
		"[1-9] Preset Resolutions",
		"[HOME] Jump back to the default viewport",
		"[BACKSPACE] Revert to previous viewport"
	};

	SettingVariable[] menuVariables = new SettingVariable[] {
		() -> SHOW_MENU,
		() -> null,
		() -> RECORD_INTERMEDIATE_CALCULATIONS,
		() -> IN_COLOR,
		() -> !DONT_FILL_BOUNDED,
		() -> SMOOTH_POINTS,
		() -> SIMPLE_MAGNITUDE,
		() -> null,
		() -> null,
		() -> null,
		() -> null,
		() -> null
	};
	
	int getMaxIterations() {
		return ITERATION_OPTIONS[MAX_ITERATIONS_INDEX];
	}

	int getOverflowThreshold() {
		return OVERFLOW_THRESHOLD_OPTIONS[OVERFLOW_THRESHOLD_INDEX];
	}
	
	Viewport ORIG_VIEWPORT;
	Viewport CURRENT_VIEWPORT;
	
	Stack<Viewport> viewportHistory = new Stack<Viewport>();
	
	int NUM_PIXELS;
	ArrayList<AtomicLong> traversalData_R;
	ArrayList<AtomicLong> traversalData_G;
	ArrayList<AtomicLong> traversalData_B;
	long MOST_PIXEL_TOUCHES_R;
	long MOST_PIXEL_TOUCHES_G;
	long MOST_PIXEL_TOUCHES_B;
	
	void writePixelData(int pixelIndex, Color pixelColor) {
		appWindow.writePixelData(pixelIndex, pixelColor);
	}
	
	public MainWindow appWindow;
	public AppContext() {
//		size(1024, 1024, P2D);
//		pixelDensity(1);
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
		width = 1024;
		height = 1024;
		NUM_PIXELS = width * height;
		traversalData_R = new ArrayList<>(NUM_PIXELS);
		traversalData_G = new ArrayList<>(NUM_PIXELS);
		traversalData_B = new ArrayList<>(NUM_PIXELS);
		MOST_PIXEL_TOUCHES_R = 0;
		MOST_PIXEL_TOUCHES_G = 0;
		MOST_PIXEL_TOUCHES_B = 0;
		//println(pixelData.length);
		
		//Init Pixel Arrays
		for (int x=0; x<NUM_PIXELS; x++) {
		  traversalData_R.add(new AtomicLong(0L));
		  traversalData_G.add(new AtomicLong(0L));
		  traversalData_B.add(new AtomicLong(0L));
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
	
	boolean requestAbortCalc = false;
	Thread workerThread;
	void rebuildPixelData() {
		//Stop existing thread if exists.
		if (workerThread != null) {
			requestAbortCalc = true;
			
			//Wait for successful abort
			while(CALCULATING) {
				try {
					workerThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		requestAbortCalc = false;
		workerThread = new Thread(MathALU::calcMandelFrame);
		workerThread.start();
	}

}
