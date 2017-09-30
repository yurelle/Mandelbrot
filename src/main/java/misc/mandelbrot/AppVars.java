package misc.mandelbrot;

import java.awt.Color;
import java.util.Stack;

public class AppVars {
	//int MAX_NUM_ITERATIONS = 1250;
	//int MAX_NUM_ITERATIONS = 255;
	//int MAX_NUM_ITERATIONS = 150;
	static int MAX_ITERATIONS_INDEX;
	static int[] ITERATION_OPTIONS;
	static int OVERFLOW_THRESHOLD_INDEX;
	static int[] OVERFLOW_THRESHOLD_OPTIONS;
	static boolean SHOW_MENU;
	static boolean RECORD_INTERMEDIATE_CALCULATIONS;
	static boolean DONT_FILL_BOUNDED;
	static boolean IN_COLOR;
	static boolean SMOOTH_POINTS;
	static boolean SIMPLE_MAGNITUDE;
	
	static {
		//Init Constants
		ITERATION_OPTIONS = new int[]{5, 10, 25, 37, 50, 75, 100, 150, 200, 255, 512, 765, 1024, 2048, 4096, 8192, 16384, 32768};
		MAX_ITERATIONS_INDEX = 2;
		//OVERFLOW_THRESHOLD = 16;
		//OVERFLOW_THRESHOLD = 2;
		OVERFLOW_THRESHOLD_OPTIONS = new int[]{1,2,3,4,5,6,7,8,9,10, 16, 32, 64, 128, 256, 512, 765, 1024};
		OVERFLOW_THRESHOLD_INDEX = 11;
		SHOW_MENU = true;
		DONT_FILL_BOUNDED = false;
		IN_COLOR = false;
		SIMPLE_MAGNITUDE = false;
		SMOOTH_POINTS = false;
		RECORD_INTERMEDIATE_CALCULATIONS = false;
	}

	static String[] menuText = new String[]{
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

	static SettingVariable[] menuVariables = new SettingVariable[] {
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
	
	static int getMaxIterations() {
		return ITERATION_OPTIONS[MAX_ITERATIONS_INDEX];
	}

	static int getOverflowThreshold() {
		return OVERFLOW_THRESHOLD_OPTIONS[OVERFLOW_THRESHOLD_INDEX];
	}
	
}
