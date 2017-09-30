package misc.mandelbrot;

class Viewport {
	double minX;
	double maxX;
	double minY;
	double maxY;

	Viewport() {
		this.minX = 0;
		this.maxX = 0;
		this.minY = 0;
		this.maxY = 0;
	}

	Viewport(final double minX, final double maxX, final double minY, final double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Viewport clone() {
		Viewport vp = new Viewport();
		vp.minX = this.minX;
		vp.maxX = this.maxX;
		vp.minY = this.minY;
		vp.maxY = this.maxY;

		return vp;
	}
}