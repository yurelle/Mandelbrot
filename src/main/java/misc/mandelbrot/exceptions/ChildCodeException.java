package misc.mandelbrot.exceptions;

public class ChildCodeException extends UnsupportedOperationException {
	public ChildCodeException() {
		super("This function should be implemented by child classes!");
	}
}
