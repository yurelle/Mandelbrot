package misc.mandelbrot.number;

import hellblazer.math.DoubleDouble;
import misc.mandelbrot.exceptions.ChildCodeException;

import java.math.BigDecimal;

public class SupportedNumeric<T> {
	public T negate() {
		throw new ChildCodeException();
	}

	public T abs() {
		throw new ChildCodeException();
	}

	public T add(T t) {
		throw new ChildCodeException();
	}

	public T subtract(T t) {
		throw new ChildCodeException();
	}

	public T multiply(T t) {
		throw new ChildCodeException();
	}

	public T divide(T t) {
		throw new ChildCodeException();
	}

	public T sqrt() {
		throw new ChildCodeException();
	}

	public int compareTo(T t) {
		throw new ChildCodeException();
	}




	public int intValue() {
		throw new ChildCodeException();
	}
	public long longValue() {
		throw new ChildCodeException();
	}
	public float floatValue() {
		throw new ChildCodeException();
	}
	public double doubleValue() {
		throw new ChildCodeException();
	}
	public DoubleDouble ddValue() {
		throw new ChildCodeException();
	}
	public BigDecimal bigDValue() {
		throw new ChildCodeException();
	}



	public T getFor(final int val) {
		throw new ChildCodeException();
	}
	public T getFor(final long val) {
		throw new ChildCodeException();
	}
	public T getFor(final float val) {
		throw new ChildCodeException();
	}
	public T getFor(final double val) {
		throw new ChildCodeException();
	}
	public T getFor(final DoubleDouble val) {
		throw new ChildCodeException();
	}
	public T getFor(final BigDecimal val) {
		throw new ChildCodeException();
	}
	public T getFor(final String numStr) {
		throw new ChildCodeException();
	}
	public T getFor(final SupportedNumeric num) {
		throw new ChildCodeException();
	}
}
