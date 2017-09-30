package misc.mandelbrot;
/**
 * A complex number is composed of 2 pieces: a real number & an imaginary number.
 * 
 * It is of the form: a + bi
 * ('i' is the symbol representing the sqrt(-1))
 * 
 */
public class ComplexNumber {
  static final ComplexNumber ZERO = new ComplexNumber(0,0);
  
  //Single Precision
  float real;
  float imaginary;
  
  ComplexNumber() {
    this.real = 0;
    this.imaginary = 0;
  }
  
  ComplexNumber(final float real, final float imaginary) {
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
  ComplexNumber getSquaredVal() {
    ComplexNumber z = new ComplexNumber();
    
    //a^2 - b^2
    z.real = (this.real * this.real) - (this.imaginary * this.imaginary);
    
    //2ab [i]
    z.imaginary = 2 * this.real * this.imaginary;
    
    return z;
  }
  
  /**
   * Creates & returns a new Complex Number Object representing the value of this Complex Number object
   * added to the given Complex Number Object. The returned object is completely separate from this object;
   * both this Complex Number object & the passed in object remain unchanged, and retain their original values.
   */
  ComplexNumber add(final ComplexNumber c_in) {
    ComplexNumber c_out = new ComplexNumber();
    
    c_out.real = this.real + c_in.real;
    c_out.imaginary = this.imaginary + c_in.imaginary;
    
    return c_out;
  }

  /**
   * Creates & returns a new Complex Number Object representing the point 1/3rd (33.3%) of the distance, starting from this
   * Complex Number towards the given Complex Number. The returned object is completely separate from this object;
   * both this Complex Number object & the passed in object remain unchanged, and retain their original values.
   */
  ComplexNumber getOneThirdStepTowards(final ComplexNumber dest) {
    ComplexNumber result = new ComplexNumber();
    result.real = calcOneThirdTo(this.real, dest.real);
    result.imaginary = calcOneThirdTo(this.imaginary, dest.imaginary);
    
    return result;
  }

  /**
   * x1 is the starting point;
   * x2 is the destination.
   */
  private float calcOneThirdTo(final float x1, final float x2) {
    //By keeping the calculation in the form [dest] - [start], the positive/negative will automatically be correct for combining with simple addition (i.e. no condition logic necessary).
    final float distance = (x2 - x1) / 3;
    return x1 + distance;
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
  float getMagnitude() {
    if (AppVars.SIMPLE_MAGNITUDE) {//Inaccurate mathematically speaking, but a slightly faster calculation & produces a cool spikey webbing pattern in the background data.
      return Math.abs(this.real + this.imaginary);//Surprizingly, the core mandelbrot fractal remains utterly unchanged. Weird!
    } else {
      return (float) Math.sqrt((this.real * this.real) + (this.imaginary * this.imaginary));
    }
  }
}