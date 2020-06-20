package misc.mandelbrot.number;

import hellblazer.math.DoubleDouble;

import java.math.BigDecimal;

public class MandelDD extends SupportedNumeric<MandelDD> {
	private DoubleDouble value;

	public MandelDD() {
		value = DoubleDouble.ZERO;
	}
	public MandelDD(final int value) {
		this.value = new DoubleDouble(value);
	}
	public MandelDD(final long value) {
		this.value = new DoubleDouble(value);
	}
	public MandelDD(final float value) {
		this.value = new DoubleDouble(value);
	}
	public MandelDD(final double value) {
		this.value = new DoubleDouble(value);
	}
	public MandelDD(final DoubleDouble value) {
		this.value = value;
	}
	public MandelDD(final BigDecimal value) {
		this.value = new DoubleDouble(value.toString());
	}
	public MandelDD(final String numStr) {
		this.value = new DoubleDouble(numStr);
	}

	@Override
	public MandelDD negate() {
		return new MandelDD(
				this.value.negate()
		);
	}

	@Override
	public MandelDD abs() {
		return new MandelDD(
				this.value.abs()
		);
	}

	@Override
	public MandelDD add(MandelDD that) {
		return new MandelDD(
				this.value.add(that.value)
		);
	}

	@Override
	public MandelDD subtract(MandelDD that) {
		return new MandelDD(
				this.value.subtract(that.value)
		);
	}

	@Override
	public MandelDD multiply(MandelDD that) {
		return new MandelDD(
				this.value.multiply(that.value)
		);
	}

	@Override
	public MandelDD divide(MandelDD that) {
		return new MandelDD(
				this.value.divide(that.value)
		);
	}

	@Override
	public MandelDD sqrt() {
		return new MandelDD(
				this.value.sqrt()
		);
	}

	@Override
	public int compareTo(MandelDD that) {
		return this.value.compareTo(that.value);
	}



	@Override
	public int intValue() {
		return this.value.intValue();
	}

	@Override
	public long longValue() {
		return this.value.longValue();
	}

	@Override
	public float floatValue() {
		return this.value.floatValue();
	}

	@Override
	public double doubleValue() {
		return this.value.doubleValue();
	}

	@Override
	public DoubleDouble ddValue() {
		return new DoubleDouble(this.value.toString());
	}

	@Override
	public BigDecimal bigDValue() {
		return new BigDecimal(this.value.toString());
	}



	@Override
	public MandelDD getFor(final int val) {
		return new MandelDD(val);
	}

	@Override
	public MandelDD getFor(final long val) {
		return new MandelDD(val);
	}

	@Override
	public MandelDD getFor(final float val) {
		return new MandelDD(val);
	}

	@Override
	public MandelDD getFor(final double val) {
		return new MandelDD(val);
	}

	@Override
	public MandelDD getFor(final DoubleDouble val) {
		return new MandelDD(val);
	}

	@Override
	public MandelDD getFor(final BigDecimal val) {
		return new MandelDD(val);
	}

	@Override
	public MandelDD getFor(final String numStr) {
		return new MandelDD(numStr);
	}

	@Override
	public MandelDD getFor(final SupportedNumeric num) {
		return new MandelDD(num.bigDValue());
	}
}
