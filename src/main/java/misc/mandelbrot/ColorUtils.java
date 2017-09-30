package misc.mandelbrot;

import java.awt.Color;

public class ColorUtils {
	private static AppContext appContext = App.APP_CONTEXT;
	
	static Color avgColors(Color... colors) {
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
	
	static Color getHeatColor(final int val) {
		// Break up into multiple cycles of the gradient for higher contrast.
		final int cycleVal = val % appContext.CYCLE_PERIOD;

		// Scale cycle value back up to the full color space
		final int heatVal = (int) MathALU.mapDouble(cycleVal, 0, appContext.CYCLE_PERIOD, 0, appContext.CHUNK_RANGE);

		// Scale value to full iteration range
		// int heatVal = (int) map(val, 0, getMaxIterations(), 0, CHUNK_RANGE);

		// Check overflow setting (whether to cycle back, or truncate)
		// if (LOOP_BACK_GRADIENT_RANGE_OVERFLOW) {
		// heatVal %= CHUNK_RANGE;
		// }

		// Calc color
		final int chunkVal = heatVal % appContext.CHUNK_SIZE;
		final float heatBand = ((float) heatVal) / appContext.CHUNK_SIZE;

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
}
