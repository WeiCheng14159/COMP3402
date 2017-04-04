import javax.xml.ws.WebServiceRef;
import helloservice.HelloService;
import helloservice.Hello;
   
public class HelloClient {
   
   @WebServiceRef(wsdlLocation="http://localhost:8080/hellows/hello?wsdl")
   static HelloService service = new HelloService();
   //static HelloService service;
   
   public static void main(String[] args) {
      try {
         System.out.println("Retrieving the port from the following service: "
            + service);
         Hello port = service.getHelloPort();
   
         System.out.println("Invoking the sayHello operation on the port.");
         String response = port.sayHello("World");
         System.out.println(response);
   
         System.out.println("Invoking the addNumbers operation on the port.");
         int sum = port.addNumbers(55, 66);
         System.out.println(sum);
   
         System.out.println("Invoking the divideNumbers operation on the port.");
         double quotient = port.divideNumbers(1, 0);
         System.out.println(quotient);
   
      } catch(Exception e) {
         e.printStackTrace();
      }
   }
}
