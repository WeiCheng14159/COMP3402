package helloservice;
import javax.jws.WebService;
import javax.jws.WebMethod;

@WebService
public class HelloService {

	private Hello h; 

	public HelloService() {
		// TODO Auto-generated constructor stub
		h = new Hello();
	}
	
	@WebMethod
	public String sayHello(String name) {
		return h.sayHello(name);
	}
   
	@WebMethod
	public Hello getHelloPort(){
		return this.h;
	}
	
   @WebMethod
   public int addNumbers(int number1, int number2) {
      return h.addNumbers(number1, number2);
   }
   
   @WebMethod
   public double divideNumbers(int dividend, int divisor)
         throws DivideNumbersException {
	   try{
		   return h.divideNumbers(dividend, divisor);
	   }
	   catch (Throwable t) { 
		     throw new DivideNumbersException("Error: " + t.getMessage()); 
	   } 
      
   }
   
   public class DivideNumbersException extends Exception {
	   public DivideNumbersException(String message) {
	      super(message);
	   }
	}
}
