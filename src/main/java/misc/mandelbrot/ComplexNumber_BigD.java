package misc.mandelbrot;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * A complex number is composed of 2 pieces: a real number & an imaginary number.
 * 
 * It is of the form: a + bi
 * ('i' is the symbol representing the sqrt(-1))
 * 
 */
public class ComplexNumber_BigD {
	static final ComplexNumber_BigD ZERO = new ComplexNumber_BigD(BigDecimal.ZERO, BigDecimal.ZERO);

	//Single Precision
	BigDecimal real;
	BigDecimal imaginary;

	ComplexNumber_BigD() {
		this.real = BigDecimal.ZERO;
		this.imaginary = BigDecimal.ZERO;
	}

	ComplexNumber_BigD(final BigDecimal real, final BigDecimal imaginary) {
		this.real = real;
		this.imaginary = imaginary;
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
	ComplexNumber_BigD getSquaredVal() {
		ComplexNumber_BigD z = new ComplexNumber_BigD();
		
		//a^2 - b^2
		z.real = this.real.multiply(this.real).subtract(this.imaginary.multiply(this.imaginary));
		
		//2ab [i]
		z.imaginary = MathALU_BigD.getBigD(2).multiply(this.real).multiply(this.imaginary);
		
		return z;
	}
	
	/**
	 * Creates & returns a new Complex Number Object representing the value of this Complex Number object
	 * added to the given Complex Number Object. The returned object is completely separate from this object;
	 * both this Complex Number object & the passed in object remain unchanged, and retain their original values.
	 */
	ComplexNumber_BigD add(final ComplexNumber_BigD c_in) {
		ComplexNumber_BigD c_out = new ComplexNumber_BigD();
		
		c_out.real = this.real.add(c_in.real);
		c_out.imaginary = this.imaginary.add(c_in.imaginary);
		
		return c_out;
	}
	
	/**
	 * Creates & returns a new Complex Number Object representing the point 1/3rd (33.3%) of the distance, starting from this
	 * Complex Number towards the given Complex Number. The returned object is completely separate from this object;
	 * both this Complex Number object & the passed in object remain unchanged, and retain their original values.
	 */
	ComplexNumber_BigD getOneThirdStepTowards(final ComplexNumber_BigD dest) {
		ComplexNumber_BigD result = new ComplexNumber_BigD();
		result.real = calcOneThirdTo(this.real, dest.real);
		result.imaginary = calcOneThirdTo(this.imaginary, dest.imaginary);
		
		return result;
	}
	
	/**
	 * x1 is the starting point;
	 * x2 is the destination.
	 */
	private BigDecimal calcOneThirdTo(final BigDecimal x1, final BigDecimal x2) {
		//By keeping the calculation in the form [dest] - [start], the positive/negative will automatically be correct for combining with simple addition (i.e. no condition logic necessary).
		final BigDecimal distance = x2.subtract(x1).divide(MathALU_BigD.getBigD(3));
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
	BigDecimal getMagnitude() {
		return (this.real.multiply(this.real).add(this.imaginary.multiply(this.imaginary))).sqrt(MathContext.DECIMAL128);//TODO
	}
	BigDecimal getSimpleMagnitude() {
		//Inaccurate mathematically speaking, but a slightly faster calculation & produces a cool spikey webbing pattern in the background data.
		return this.real.add(this.imaginary).abs();//Surprisingly, the core mandelbrot fractal remains utterly unchanged. Weird!
	}
}