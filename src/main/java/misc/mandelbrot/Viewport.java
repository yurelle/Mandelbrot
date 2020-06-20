package misc.mandelbrot;

import misc.mandelbrot.number.SupportedNumeric;

class Viewport<T extends SupportedNumeric<T>> {
	T minX;
	T maxX;
	T minY;
	T maxY;

	//Precision Handle
	private MathALU<T> ALU;

	Viewport(MathALU<T> ALU) {
		this.ALU = ALU;

		this.minX = ALU.getFor(0);
		this.maxX = ALU.getFor(0);
		this.minY = ALU.getFor(0);
		this.maxY = ALU.getFor(0);
	}

	Viewport(final T minX, final T maxX, final T minY, final T maxY) {
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Viewport clone() {
		Viewport<T> vp = new Viewport<>(ALU);
		vp.minX = this.minX;
		vp.maxX = this.maxX;
		vp.minY = this.minY;
		vp.maxY = this.maxY;

		return vp;
	}

	public void setMinX(String minX) {
		this.minX = ALU.getFor(minX);
	}
	public void setMaxX(String maxX) {
		this.maxX = ALU.getFor(maxX);
	}
	public void setMinY(String minY) {
		this.minY = ALU.getFor(minY);
	}
	public void setMaxY(String maxY) {
		this.maxY = ALU.getFor(maxY);
	}

	public void mapMinX(final int value, final int srcMin, final int srcMax, final Viewport<? extends SupportedNumeric<?>> previousViewport) {//When precision changes, T will change, and previousViewport will have a different precision.
		this.minX = ALU.map(ALU.getFor(value), ALU.getFor(srcMin), ALU.getFor(srcMax), ALU.getFor(previousViewport.minX), ALU.getFor(previousViewport.maxX));
	}
	public void mapMinY(final int value, final int srcMin, final int srcMax, final Viewport<? extends SupportedNumeric<?>> previousViewport) {//When precision changes, T will change, and previousViewport will have a different precision.
		this.minY = ALU.map(ALU.getFor(value), ALU.getFor(srcMin), ALU.getFor(srcMax), ALU.getFor(previousViewport.minY), ALU.getFor(previousViewport.maxY));
	}
	public void mapMaxX(final int value, final int srcMin, final int srcMax, final Viewport<? extends SupportedNumeric<?>> previousViewport) {//When precision changes, T will change, and previousViewport will have a different precision.
		this.maxX = ALU.map(ALU.getFor(value), ALU.getFor(srcMin), ALU.getFor(srcMax), ALU.getFor(previousViewport.minX), ALU.getFor(previousViewport.maxX));
	}
	public void mapMaxY(final int value, final int srcMin, final int srcMax, final Viewport<? extends SupportedNumeric<?>> previousViewport) {//When precision changes, T will change, and previousViewport will have a different precision.
		this.maxY = ALU.map(ALU.getFor(value), ALU.getFor(srcMin), ALU.getFor(srcMax), ALU.getFor(previousViewport.minY), ALU.getFor(previousViewport.maxY));
	}
}