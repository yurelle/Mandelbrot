package misc.mandelbrot;

import misc.mandelbrot.number.SupportedNumeric;

import java.awt.Color;

public class ColorUtils {
	private static AppContext appContext = App.APP_CONTEXT;
	
	static int grayscaleAsInt(int brightness) {
		return rgbAsInt(brightness, brightness, brightness);
	}
	
	/**
	 * Taken directly from the source code for the java.awt.Color(int r, int g, int b) constructor.
	 * 
	 * @See: http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/java/awt/Color.java#Color.%3Cinit%3E%28int%2Cint%2Cint%29
	 */
	static int rgbAsInt(int r, int g, int b) {
		return	rgbaAsInt(r, g, b, 255);
	}
	static int rgbaAsInt(int r, int g, int b, int a) {
		return	((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8)  |
				((b & 0xFF) << 0);
	}
	
	static int getLogReducedBrightness(final long pixelVal, final long maxVal) {
		final long log_pixelVal = log_nonNull(pixelVal);
		final long log_maxVal = log_nonNull(maxVal);
		
		//Scale brightness to 255
		MathALU<? extends SupportedNumeric<?>> ALU = appContext.getALU();
		return (int) Math.round(ALU.mapLong(log_pixelVal, 0, log_maxVal, 0, 255).doubleValue());
	}
	
	static long log_nonNull(final long rawVal) {
		//Log of Zero is undefined; kind of like dividing by zero
		final long logValue;
		if (rawVal > 0) {
			logValue = Math.round(Math.log(rawVal));
		} else {
			logValue = 0;
		}
		
		return logValue;
	}
	
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
		MathALU<? extends SupportedNumeric<?>> ALU = appContext.getALU();
		final int heatVal = (int) ALU.mapDouble(cycleVal, 0, appContext.CYCLE_PERIOD, 0, appContext.CHUNK_RANGE).doubleValue();

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
