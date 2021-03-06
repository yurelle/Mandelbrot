package misc.mandelbrot;

import misc.mandelbrot.number.SupportedNumeric;

/**
 * A complex number is composed of 2 pieces: a real number & an imaginary number.
 * 
 * It is of the form: a + bi
 * ('i' is the symbol representing the sqrt(-1))
 * 
 */
public class ComplexNumber<T extends SupportedNumeric<T>> {

	//Single Precision
	T real;
	T imaginary;

	//Precision Handle
	private final T NUM;
	private final MathALU<T> ALU;
	private final CxNumProps CX_NUM_PROPS;

	ComplexNumber(T NUM, MathALU<T> ALU) {
		this.real = NUM.getFor(0);
		this.imaginary = NUM.getFor(0);

		this.NUM = NUM;
		this.ALU = ALU;
		this.CX_NUM_PROPS = new CxNumProps(NUM, ALU);
	}

	ComplexNumber(T NUM, MathALU<T> ALU, final T real, final T imaginary) {
		this(NUM, ALU);

		this.real = real;
		this.imaginary = imaginary;
	}

	public class CxNumProps<T extends SupportedNumeric<T>> {
		//Precision Handle
		private T NUM;
		private MathALU<T> ALU;

		CxNumProps(T NUM, MathALU<T> ALU) {
			this.NUM = NUM;
			this.ALU = ALU;
		}

		public ComplexNumber<T> create() {
			return new ComplexNumber<>(NUM, ALU, NUM.getFor(0), NUM.getFor(0));
		}

		public ComplexNumber<T> create(final T real, final T imaginary) {
			return new ComplexNumber<>(NUM, ALU, real, imaginary);
		}
	}
	
	/**
	 * Creates & returns a new ComplexNumber Object representing the value of this complex number object
	 * multiplied by itself (i.e. "squared"). The returned object is completely separate from this object;
	 * this Complex Number object remains unchanged, and retains its original value.
	 * 
	 *   (a + bi)^2
	 * = (a + bi) * (a + bi)
	 * = a^2 + abi + abi (b^2 * i^2)
	 * = a^2 + abi + abi (-1 * b^2) //i^2 => (-1)
	 * = a^2 + 2abi (-1 * b^2)
	 * = a^2 + 2abi -b^2
	 * = a^2 - b^2 + 2abi
	 *
	 * Real: [a^2 - b^2]
	 * Imaginary: [2abi] => [2ab]*i
	 */
	ComplexNumber<T> getSquaredVal() {
		ComplexNumber<T> z = CX_NUM_PROPS.create();
		
		//a^2 - b^2
		z.real = this.real.multiply(this.real).subtract(this.imaginary.multiply(this.imaginary));
		
		//2ab [i]
		z.imaginary = NUM.getFor(2).multiply(this.real).multiply(this.imaginary);
		
		return z;
	}
	
	/**
	 * Creates & returns a new Complex Number Object representing the value of this Complex Number object
	 * added to the given Complex Number Object. The returned object is completely separate from this object;
	 * both this Complex Number object & the passed in object remain unchanged, and retain their original values.
	 */
	ComplexNumber<T> add(final ComplexNumber<T> c_in) {
		ComplexNumber<T> c_out = CX_NUM_PROPS.create();
		
		c_out.real = this.real.add(c_in.real);
		c_out.imaginary = this.imaginary.add(c_in.imaginary);
		
		return c_out;
	}
	
	/**
	 * Creates & returns a new Complex Number Object representing the point 1/3rd (33.3%) of the distance, starting from this
	 * Complex Number towards the given Complex Number. The returned object is completely separate from this object;
	 * both this Complex Number object & the passed in object remain unchanged, and retain their original values.
	 */
	ComplexNumber<T> getOneThirdStepTowards(final ComplexNumber<T> dest) {
		ComplexNumber<T> result = CX_NUM_PROPS.create();
		result.real = calcOneThirdTo(this.real, dest.real);
		result.imaginary = calcOneThirdTo(this.imaginary, dest.imaginary);
		
		return result;
	}
	
	/**
	 * x1 is the starting point;
	 * x2 is the destination.
	 */
	private T calcOneThirdTo(final T x1, final T x2) {
		//By keeping the calculation in the form [dest] - [start], the positive/negative will automatically be correct for combining with simple addition (i.e. no condition logic necessary).
		final T distance = x2.subtract(x1).divide(NUM.getFor(3));
		return x1.add(distance);
	}
	
	/**
	 * The magnitude of a complex number is the length of the vector from the origin to that number. So, to calculate that, we use the pythagorian theorem, by
	 * making a right triangle with the real & imaginary coordinates as the length of the sides, and then solve for the hyponenus.
	 *
	 * A^2 + B^2 = C^2
	 * [Real]^2 + [Imaginary]^2 = [Magnitude]^2
	 *
	 * See: https://www.khanacademy.org/math/precalculus/imaginary-and-complex-numbers/absolute-value-and-angle-of-complex-numbers/v/absolute-value-of-a-complex-number
	 */
	T getMagnitude() {
		return (this.real.multiply(this.real).add(this.imaginary.multiply(this.imaginary))).sqrt();
	}
	T getSimpleMagnitude() {
		//Inaccurate mathematically speaking, but a slightly faster calculation & produces a cool spikey webbing pattern in the background data.
		return this.real.add(this.imaginary).abs();//Surprisingly, the core mandelbrot fractal remains utterly unchanged. Weird!
	}
}