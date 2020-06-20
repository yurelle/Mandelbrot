package misc.mandelbrot.number;

import hellblazer.math.DoubleDouble;

import java.math.BigDecimal;

public class MandelFloat extends SupportedNumeric<MandelFloat> {
	private float value;

	public MandelFloat() {
		value = 0F;
	}
	public MandelFloat(final int value) {
		this.value = (float) value;
	}
	public MandelFloat(final long value) {
		this.value = (float) value;
	}
	public MandelFloat(final float value) {
		this.value = (float) value;
	}
	public MandelFloat(final double value) {
		this.value = (float) value;
	}
	public MandelFloat(final DoubleDouble value) {
		this.value = value.floatValue();
	}
	public MandelFloat(final BigDecimal value) {
		this.value = value.floatValue();
	}
	public MandelFloat(final String numStr) {
		this.value = Float.parseFloat(numStr);
	}

	@Override
	public MandelFloat negate() {
		return new MandelFloat(
				0 - this.value
		);
	}

	@Override
	public MandelFloat abs() {
		return new MandelFloat(
				Math.abs(this.value)
		);
	}

	@Override
	public MandelFloat add(MandelFloat that) {
		return new MandelFloat(
				this.value + that.value
		);
	}

	@Override
	public MandelFloat subtract(MandelFloat that) {
		return new MandelFloat(
				this.value - that.value
		);
	}

	@Override
	public MandelFloat multiply(MandelFloat that) {
		return new MandelFloat(
				this.value * that.value
		);
	}

	@Override
	public MandelFloat divide(MandelFloat that) {
		return new MandelFloat(
				this.value / that.value
		);
	}

	@Override
	public MandelFloat sqrt() {
		return new MandelFloat(
				(float) Math.sqrt(this.value)
		);
	}

	@Override
	public int compareTo(MandelFloat that) {
		return Float.compare(this.value, that.value);
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
	public MandelFloat getFor(final int val) {
		return new MandelFloat(val);
	}

	@Override
	public MandelFloat getFor(final long val) {
		return new MandelFloat(val);
	}

	@Override
	public MandelFloat getFor(final float val) {
		return new MandelFloat(val);
	}

	@Override
	public MandelFloat getFor(final double val) {
		return new MandelFloat(val);
	}

	@Override
	public MandelFloat getFor(final DoubleDouble val) {
		return new MandelFloat(val);
	}

	@Override
	public MandelFloat getFor(final BigDecimal val) {
		return new MandelFloat(val);
	}

	@Override
	public MandelFloat getFor(final String numStr) {
		return new MandelFloat(numStr);
	}

	@Override
	public MandelFloat getFor(final SupportedNumeric num) {
		return new MandelFloat(num.bigDValue());
	}
}
