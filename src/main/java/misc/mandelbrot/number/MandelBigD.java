package misc.mandelbrot.number;

import hellblazer.math.DoubleDouble;

import java.math.BigDecimal;
import java.math.MathContext;

public class MandelBigD extends SupportedNumeric<MandelBigD> {
	private BigDecimal value;

	public MandelBigD() {
		this.value = BigDecimal.ZERO;
	}
	public MandelBigD(final int value) {
		this.value = new BigDecimal(value);
	}
	public MandelBigD(final long value) {
		this.value = new BigDecimal(value);
	}
	public MandelBigD(final float value) {
		this.value = new BigDecimal(value);
	}
	public MandelBigD(final double value) {
		this.value = new BigDecimal(value);
	}
	public MandelBigD(final DoubleDouble value) {
		this.value = new BigDecimal(value.toString());
	}
	public MandelBigD(final BigDecimal value) {
		this.value = value;
	}
	public MandelBigD(final String numStr) {
		this.value = new BigDecimal(numStr);
	}

	@Override
	public MandelBigD negate() {
		return new MandelBigD(
				this.value.negate()
		);
	}

	@Override
	public MandelBigD abs() {
		return new MandelBigD(
				this.value.abs()
		);
	}

	@Override
	public MandelBigD add(MandelBigD that) {
		return new MandelBigD(
				this.value.add(that.value)
		);
	}

	@Override
	public MandelBigD subtract(MandelBigD that) {
		return new MandelBigD(
				this.value.subtract(that.value)
		);
	}

	@Override
	public MandelBigD multiply(MandelBigD that) {
		return new MandelBigD(
				this.value.multiply(that.value)
		);
	}

	@Override
	public MandelBigD divide(MandelBigD that) {
		return new MandelBigD(
				this.value.divide(that.value)
		);
	}

	@Override
	public MandelBigD sqrt() {
		return new MandelBigD(
				this.value.sqrt(MathContext.DECIMAL128)
		);
	}

	@Override
	public int compareTo(MandelBigD that) {
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
		return this.value;
	}



	@Override
	public MandelBigD getFor(final int val) {
		return new MandelBigD(val);
	}

	@Override
	public MandelBigD getFor(final long val) {
		return new MandelBigD(val);
	}

	@Override
	public MandelBigD getFor(final float val) {
		return new MandelBigD(val);
	}

	@Override
	public MandelBigD getFor(final double val) {
		return new MandelBigD(val);
	}

	@Override
	public MandelBigD getFor(final DoubleDouble val) {
		return new MandelBigD(val);
	}

	@Override
	public MandelBigD getFor(final BigDecimal val) {
		return new MandelBigD(val);
	}

	@Override
	public MandelBigD getFor(final String numStr) {
		return new MandelBigD(numStr);
	}

	@Override
	public MandelBigD getFor(final SupportedNumeric num) {
		return new MandelBigD(num.bigDValue());
	}
}
