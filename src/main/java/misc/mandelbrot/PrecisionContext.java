package misc.mandelbrot;

import misc.mandelbrot.number.SupportedNumeric;

public class PrecisionContext<T extends SupportedNumeric<T>> {
	public MathALU<T> ALU;
	public T NUM;

	public PrecisionContext(final T NUM) {
		this.NUM = NUM;
		this.ALU = new MathALU<>(NUM);
	}

	public Viewport<T> createViewport() {
		return new Viewport<T>(ALU);
	}
}
