package misc.mandelbrot.number;

import hellblazer.math.DoubleDouble;

import java.math.BigDecimal;

public class MandelDouble extends SupportedNumeric<MandelDouble> {
	private double value;

	public MandelDouble() {
		value = 0D;
	}
	public MandelDouble(final int value) {
		this.value = (double) value;
	}
	public MandelDouble(final long value) {
		this.value = (double) value;
	}
	public MandelDouble(final float value) {
		this.value = (double) value;
	}
	public MandelDouble(final double value) {
		this.value = (double) value;
	}
	public MandelDouble(final DoubleDouble value) {
		this.value = value.doubleValue();
	}
	public MandelDouble(final BigDecimal value) {
		this.value = value.doubleValue();
	}
	public MandelDouble(final String numStr) {
		this.value = Double.parseDouble(numStr);
	}

	@Override
	public MandelDouble negate() {
		return new MandelDouble(
				0 - this.value
		);
	}

	@Override
	public MandelDouble abs() {
		return new MandelDouble(
				Math.abs(this.value)
		);
	}

	@Override
	public MandelDouble add(MandelDouble that) {
		return new MandelDouble(
				this.value + that.value
		);
	}

	@Override
	public MandelDouble subtract(MandelDouble that) {
		return new MandelDouble(
				this.value - that.value
		);
	}

	@Override
	public MandelDouble multiply(MandelDouble that) {
		return new MandelDouble(
				this.value * that.value
		);
	}

	@Override
	public MandelDouble divide(MandelDouble that) {
		return new MandelDouble(
				this.value / that.value
		);
	}

	@Override
	public MandelDouble sqrt() {
		return new MandelDouble(
				Math.sqrt(this.value)
		);
	}

	@Override
	public int compareTo(MandelDouble that) {
		return Double.compare(this.value, that.value);
	}



	@Override
	public int intValue() {
		return (int) this.value;
	}

	@Override
	public long longValue() {
		return (long) this.value;
	}

	@Override
	public float floatValue() {
		return (float) this.value;
	}

	@Override
	public double doubleValue() {
		return (double) this.value;
	}

	@Override
	public DoubleDouble ddValue() {
		return new DoubleDouble(this.value);
	}

	@Override
	public BigDecimal bigDValue() {
		return new BigDecimal(this.value);
	}



	@Override
	public MandelDouble getFor(final int val) {
		return new MandelDouble(val);
	}

	@Override
	public MandelDouble getFor(final long val) {
		return new MandelDouble(val);
	}

	@Override
	public MandelDouble getFor(final float val) {
		return new MandelDouble(val);
	}

	@Override
	public MandelDouble getFor(final double val) {
		return new MandelDouble(val);
	}

	@Override
	public MandelDouble getFor(final DoubleDouble val) {
		return new MandelDouble(val);
	}

	@Override
	public MandelDouble getFor(final BigDecimal val) {
		return new MandelDouble(val);
	}

	@Override
	public MandelDouble getFor(final String numStr) {
		return new MandelDouble(numStr);
	}

	@Override
	public MandelDouble getFor(final SupportedNumeric num) {
		return new MandelDouble(num.bigDValue());
	}
}
