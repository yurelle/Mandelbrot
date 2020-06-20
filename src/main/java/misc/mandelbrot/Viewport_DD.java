package misc.mandelbrot;

import hellblazer.math.DoubleDouble;

class Viewport_DD {
	DoubleDouble minX;
	DoubleDouble maxX;
	DoubleDouble minY;
	DoubleDouble maxY;

	Viewport_DD() {
		this.minX = DoubleDouble.ZERO;
		this.maxX = DoubleDouble.ZERO;
		this.minY = DoubleDouble.ZERO;
		this.maxY = DoubleDouble.ZERO;
	}

	Viewport_DD(final DoubleDouble minX, final DoubleDouble maxX, final DoubleDouble minY, final DoubleDouble maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Viewport_DD clone() {
		Viewport_DD vp = new Viewport_DD();
		vp.minX = this.minX;
		vp.maxX = this.maxX;
		vp.minY = this.minY;
		vp.maxY = this.maxY;

		return vp;
	}
}