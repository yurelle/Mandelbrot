package misc.mandelbrot;

class Viewport_Double {
	double minX;
	double maxX;
	double minY;
	double maxY;

	Viewport_Double() {
		this.minX = 0;
		this.maxX = 0;
		this.minY = 0;
		this.maxY = 0;
	}

	Viewport_Double(final double minX, final double maxX, final double minY, final double maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Viewport_Double clone() {
		Viewport_Double vp = new Viewport_Double();
		vp.minX = this.minX;
		vp.maxX = this.maxX;
		vp.minY = this.minY;
		vp.maxY = this.maxY;

		return vp;
	}
}