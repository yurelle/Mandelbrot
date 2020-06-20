package misc.mandelbrot;

import hellblazer.math.DoubleDouble;

import java.awt.*;
import java.math.BigDecimal;

public class MathALU_DD {
	private static AppContext appContext = App.APP_CONTEXT;

	static DoubleDouble mapInt(final int value, final int srcMin, final int srcMax, final int destMin, final int destMax) {
		return mapDouble(new DoubleDouble(value), new DoubleDouble(srcMin), new DoubleDouble(srcMax), new DoubleDouble(destMin), new DoubleDouble(destMax));
	}

	static DoubleDouble mapDouble(final DoubleDouble value, final DoubleDouble srcMin, final DoubleDouble srcMax, final DoubleDouble destMin, final DoubleDouble destMax) {
		// Calculate source range
		final DoubleDouble srcRange = absDouble(srcMax.subtract(srcMin));

		// Translate value to be based on source range minimum (rather than
		// zero)
		final DoubleDouble srcAdjValue = value.subtract(srcMin);

		// Calculate source percentage
		final DoubleDouble srcPercentage = srcAdjValue.divide(srcRange);

		// Calculate destination range
		final DoubleDouble destRange = absDouble(destMax.subtract(destMin));

		// Calculate destination value in range
		final DoubleDouble destRangeVal = srcPercentage.multiply(destRange);

		// Translate dest range value to be based on zero (rather than dest
		// range minimimum)
		final DoubleDouble destVal = destRangeVal.add(destMin);

		// Return result
		return destVal;
	}

	static DoubleDouble absDouble(final DoubleDouble value) {
		if (value.compareTo(DoubleDouble.ZERO) < 0) {
			return value.negate();
		} else {
			return value;
		}
	}

	static void calcMandelFrame() {
		/*
		 * maxIterations is pulled out as a param due to multithreading issues. The menu event which changes maxIterations in the
		 * application context, is processed before the command is sent to cancel the existing worker thread. Therefore, if max
		 * iterations is set to a high value, and then lowered while a worker thread is processing a high iteration chunk of the data,
		 * the mapDouble() function which scales the value down to 0-255 for RBG color values will produce a value higher than 255.
		 * This will cause an illegalArgumentException to be thrown by the Color constructor. Freezing maxIterations as an imutable
		 * method param, prevents this from happening by decoupling the changes in the application context from the actual worker thread.
		 */
		final int maxIterations = appContext.getMaxIterations();
		System.out.println("getMaxIterations(): " + maxIterations);

		// Reset Variables - Must reset to zero, since the values are cumulative
		// not objective/direct.
		if (appContext.RECORD_INTERMEDIATE_CALCULATIONS) {
			//Clear old image
			appContext.clearContentImage();

			//Clear old pixel data
			appContext.MOST_PIXEL_TOUCHES_R = 0;
			appContext.MOST_PIXEL_TOUCHES_G = 0;
			appContext.MOST_PIXEL_TOUCHES_B = 0;
			appContext.MOST_PIXEL_TOUCHES_RAW = 0;
			int len = appContext.NUM_PIXELS;
			for (int x = 0; x < len; x++) {
				appContext.traversalData_R.get(x).set(0);
				appContext.traversalData_G.get(x).set(0);
				appContext.traversalData_B.get(x).set(0);
				appContext.traversalData_Raw.get(x).set(0);
			}
		}

		// Iterate through all pixels in the viewport
		for (int y = 0; y < appContext.height; y++) {
			//Check abort flag
			if (appContext.requestAbortCalc) {
				appContext.CALCULATING = false;
				return;
			}

			// Set flag //until you fix the multi-thread issue, we set it inside
			// the loop to make sure that another thread doesn't turn this off
			// while this thread is still running.
			appContext.CALCULATING = true;
			appContext.CALCULATING_PIXEL_ROW = y + 1;
			for (int x = 0; x < appContext.width; x++) {
				// Calculate pixel's location in data array
				final int pixelIndex = getPixelIndexForXY(new PVector(x, y));

				// Derive the starting complex number from the xy coordinate of
				// the pixel.
				ComplexNumber_DD centerPoint = getComplexNumberForXY(new PVector(x, y));

				// Calculate Pixel
				if (appContext.SMOOTH_POINTS) {
					// Find Adjacent Pixels
					ComplexNumber_DD topPixel		= getComplexNumberForXY(new PVector(x, y - 1));
					ComplexNumber_DD bottomPixel	= getComplexNumberForXY(new PVector(x, y + 1));
					ComplexNumber_DD leftPixel	= getComplexNumberForXY(new PVector(x - 1, y));
					ComplexNumber_DD rightPixel	= getComplexNumberForXY(new PVector(x + 1, y));

					// Calculate partial steps towards adjacent pixels
					ComplexNumber_DD upPoint		= centerPoint.getOneThirdStepTowards(topPixel);
					ComplexNumber_DD downPoint	= centerPoint.getOneThirdStepTowards(bottomPixel);
					ComplexNumber_DD leftPoint	= centerPoint.getOneThirdStepTowards(leftPixel);
					ComplexNumber_DD rightPoint	= centerPoint.getOneThirdStepTowards(rightPixel);

					// Calculate Mandelbrot values for outward steps
					Color upColor		= calcMandelPoint(ComplexNumber_DD.ZERO, upPoint, maxIterations);
					Color downColor		= calcMandelPoint(ComplexNumber_DD.ZERO, downPoint, maxIterations);
					Color leftColor		= calcMandelPoint(ComplexNumber_DD.ZERO, leftPoint, maxIterations);
					Color rightColor	= calcMandelPoint(ComplexNumber_DD.ZERO, rightPoint, maxIterations);

					//Save average color value as pixel //Move this outside the main calc method; as is, it calculates the normal color even when trying to calculate intermediates, doubling the work.
					//And smooth doesn't seem to take effect on intermediates in terms of actual results, it only makes the calculation slower, because it's calculating the smooth for the normal color,
					//and then throwing it away, and not calculating smooth for the intermediates.
					appContext.writePixelData(pixelIndex, ColorUtils.avgColors(upColor, downColor, leftColor, rightColor));
				} else {
					appContext.writePixelData(pixelIndex, calcMandelPoint(ComplexNumber_DD.ZERO, centerPoint, maxIterations));
				}
			}
		}

		// Reset flag
		appContext.CALCULATING = false;

		final long _max = appContext.traversalData_Raw.parallelStream().mapToLong((d) -> d.get()).max().getAsLong();
		final long _max_log = appContext.traversalData_Raw.parallelStream().mapToLong((d) -> d.get() == 0 ? 0 : (long)Math.log(d.get())).max().getAsLong();

		System.err.println("\n\n---\n_mad:\t"+_max +"\n_max_log:\t"+_max_log+"\n---\n\n");

		// println("MOST_PIXEL_TOUCHES_R: "+MOST_PIXEL_TOUCHES_R);
		// println("MOST_PIXEL_TOUCHES_G: "+MOST_PIXEL_TOUCHES_G);
		// println("MOST_PIXEL_TOUCHES_B: "+MOST_PIXEL_TOUCHES_B);
	}


	static Color calcMandelPoint(ComplexNumber_DD z, ComplexNumber_DD ORIG_C, final int iterationLimit) {
		// Run through the mandelbrot's recursive calculation for the given
		// complex number.
		int numIterations = 0;
		// if (RECORD_INTERMEDIATE_CALCULATIONS && IN_COLOR) {
		// iterationLimit = ITERATION_OPTIONS[];
		// }
		while (numIterations < iterationLimit) {
			numIterations++;
			z = mandelFunc(z, ORIG_C);

			// Log touched pixel
			if (appContext.RECORD_INTERMEDIATE_CALCULATIONS) {
				final PVector coordinates = getXYForComplexNumber(z);

				// Ensure point is still inside viewport
				if (coordinates != null) {
					final int pixelIndex = getPixelIndexForXY(coordinates);

					// Increment
					appContext.traversalData_Raw.get(pixelIndex).incrementAndGet();
					if (numIterations < 500) {
						appContext.traversalData_R.get(pixelIndex).incrementAndGet();
					}
					if (numIterations < 1500) {
						appContext.traversalData_B.get(pixelIndex).incrementAndGet();
					}
					appContext.traversalData_G.get(pixelIndex).incrementAndGet();

					// Update rolling maximum
					if (appContext.traversalData_R.get(pixelIndex).get() > appContext.MOST_PIXEL_TOUCHES_R) {
//						System.out.println("Increasing Max Pixel Touches [R]:\t"+appContext.MOST_PIXEL_TOUCHES_R+" -> "+appContext.traversalData_R.get(pixelIndex).get());
						appContext.MOST_PIXEL_TOUCHES_R = appContext.traversalData_R.get(pixelIndex).get();
					}
					if (appContext.traversalData_G.get(pixelIndex).get() > appContext.MOST_PIXEL_TOUCHES_G) {
//						System.out.println("Increasing Max Pixel Touches [G]:\t"+appContext.MOST_PIXEL_TOUCHES_G+" -> "+appContext.traversalData_G.get(pixelIndex).get());
						appContext.MOST_PIXEL_TOUCHES_G = appContext.traversalData_G.get(pixelIndex).get();
					}
					if (appContext.traversalData_B.get(pixelIndex).get() > appContext.MOST_PIXEL_TOUCHES_B) {
//						System.out.println("Increasing Max Pixel Touches [B]:\t"+appContext.MOST_PIXEL_TOUCHES_B+" -> "+appContext.traversalData_B.get(pixelIndex).get());
						appContext.MOST_PIXEL_TOUCHES_B = appContext.traversalData_B.get(pixelIndex).get();
					}
					if (appContext.traversalData_Raw.get(pixelIndex).get() > appContext.MOST_PIXEL_TOUCHES_RAW) {
//						System.out.println("Increasing Max Pixel Touches [Raw]:\t"+appContext.MOST_PIXEL_TOUCHES_RAW+" -> "+appContext.traversalData_Raw.get(pixelIndex).get());
						appContext.MOST_PIXEL_TOUCHES_RAW = appContext.traversalData_Raw.get(pixelIndex).get();
					}
				}
			}

			// Check for overflow
			DoubleDouble magnitude = appContext.SIMPLE_MAGNITUDE ? z.getSimpleMagnitude() : z.getMagnitude();
			if (magnitude.compareTo(getDD(appContext.getOverflowThreshold())) > 0) {
				break;
			}
		}

		// Calculate Grayscale Brightness
		final int brightness = mapInt(numIterations, 0, iterationLimit, 0, 255).intValue();//TODO .intValueExact()? threw error: "rounding necessary"; maybe "...exact()" is a sort of "doThisOrDie" type of thing.

		// Determine Pixel Color
		Color pixelColor;
		if (numIterations == iterationLimit && appContext.DONT_FILL_BOUNDED) {
			// Overflow Fill Color

			// pixelColor = new Color(45, 210, 255, 255);
			// pixelColor = new Color(31, 142, 173, 255);
			pixelColor = new Color(0, 0, 0, 255);
			// pixelColor = new Color(255, 255, 255, 255);
		} else {
			if (appContext.IN_COLOR) {
				pixelColor = ColorUtils.getHeatColor(numIterations);
			} else {// Grayscale
				try {
					pixelColor = new Color(brightness, brightness, brightness, 255);
				} catch (IllegalArgumentException e) {
					System.err.println("\n\n\n---\n\n\nThe bad Val: ["+brightness+"]\n\n\n---\n\n\n");
					throw e;
				}
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

	// TODO: go back and do a second kind of rendering where instead of
	// calculating each pixel in
	// order, it first calculates the odd pixels, then the even ones, to
	// basically give a half-res
	// preview of the whole canvas in half the time.
	static int getPixelIndexForXY(final PVector point) {
		return (int) (point.x + (point.y * appContext.width));
	}

	static ComplexNumber_DD getComplexNumberForXY(final PVector point) {
//		return new ComplexNumber_DD(   mapDouble(getDD((int) point.x), DoubleDouble.ZERO, getDD(appContext.width), appContext.CURRENT_VIEWPORT.minX, appContext.CURRENT_VIEWPORT.maxX),
//										 mapDouble(getDD((int) point.y), DoubleDouble.ZERO, getDD(appContext.height), appContext.CURRENT_VIEWPORT.minY, appContext.CURRENT_VIEWPORT.maxY));
		return null;//TODO
	}

	static PVector getXYForComplexNumber(final ComplexNumber_DD point) {
		// Check if point is outside viewport
//		if (point.real.compareTo(appContext.CURRENT_VIEWPORT.minX) <= 0 || point.real.compareTo(appContext.CURRENT_VIEWPORT.maxX) >= 0
//				|| point.imaginary.compareTo(appContext.CURRENT_VIEWPORT.minY) <= 0 || point.imaginary.compareTo(appContext.CURRENT_VIEWPORT.maxY) >= 0) {
//
//			return null;
//		}
//		return new PVector( mapDouble(point.real,      appContext.CURRENT_VIEWPORT.minX, appContext.CURRENT_VIEWPORT.maxX, DoubleDouble.ZERO, getDD(appContext.width)).intValue(),//TODO .intValueExact()?
//							mapDouble(point.imaginary, appContext.CURRENT_VIEWPORT.minY, appContext.CURRENT_VIEWPORT.maxY, DoubleDouble.ZERO, getDD(appContext.height)).intValue());
		return null;//TODO
	}

	static ComplexNumber_DD mandelFunc(final ComplexNumber_DD c, final ComplexNumber_DD ORIG_C) {
		return c.getSquaredVal().add(ORIG_C);
	}

	static DoubleDouble getDD(final int intNum) {
		return new DoubleDouble(String.valueOf(intNum));
	}
}
