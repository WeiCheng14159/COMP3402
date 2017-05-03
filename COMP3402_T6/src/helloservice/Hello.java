package helloservice;
import javax.jws.WebService;
import javax.jws.WebMethod;
/**
 * Web Service End-point implementation class
 */
   
@WebService
public class Hello {
   // Constructor
   public void Hello() {}
   
   @WebMethod
   public String sayHello(String name) {
      return "Hello, " + name + ".";
   }
   
   @WebMethod
   public int addNumbers(int number1, int number2) {
      return number1 + number2;
   }
   
   @WebMethod
   public double divideNumbers(int dividend, int divisor)
         throws DivideNumbersException {
      if (divisor == 0) {
         throw new DivideNumbersException("Divisor cannot be zero!");
      }
      return (double)dividend/divisor;
   }

   public class DivideNumbersException extends Exception {
	   public DivideNumbersException(String message) {
	      super(message);
	   }
	}

}
