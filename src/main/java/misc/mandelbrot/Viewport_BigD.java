package misc.mandelbrot;

import java.math.BigDecimal;

class Viewport_BigD {
	BigDecimal minX;
	BigDecimal maxX;
	BigDecimal minY;
	BigDecimal maxY;

	Viewport_BigD() {
		this.minX = BigDecimal.ZERO;
		this.maxX = BigDecimal.ZERO;
		this.minY = BigDecimal.ZERO;
		this.maxY = BigDecimal.ZERO;
	}

	Viewport_BigD(final BigDecimal minX, final BigDecimal maxX, final BigDecimal minY, final BigDecimal maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Viewport_BigD clone() {
		Viewport_BigD vp = new Viewport_BigD();
		vp.minX = this.minX;
		vp.maxX = this.maxX;
		vp.minY = this.minY;
		vp.maxY = this.maxY;

		return vp;
	}
}