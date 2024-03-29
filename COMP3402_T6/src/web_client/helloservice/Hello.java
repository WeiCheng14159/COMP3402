
package helloservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "Hello", targetNamespace = "http://helloservice/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface Hello {


    /**
     * 
     * @param arg0
     * @return
     *     returns java.lang.String
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "sayHello", targetNamespace = "http://helloservice/", className = "helloservice.SayHello")
    @ResponseWrapper(localName = "sayHelloResponse", targetNamespace = "http://helloservice/", className = "helloservice.SayHelloResponse")
    @Action(input = "http://helloservice/Hello/sayHelloRequest", output = "http://helloservice/Hello/sayHelloResponse")
    public String sayHello(
        @WebParam(name = "arg0", targetNamespace = "")
        String arg0);

    /**
     * 
     */
    @WebMethod(operationName = "Hello")
    @RequestWrapper(localName = "Hello", targetNamespace = "http://helloservice/", className = "helloservice.Hello_Type")
    @ResponseWrapper(localName = "HelloResponse", targetNamespace = "http://helloservice/", className = "helloservice.HelloResponse")
    @Action(input = "http://helloservice/Hello/HelloRequest", output = "http://helloservice/Hello/HelloResponse")
    public void hello();

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns int
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "addNumbers", targetNamespace = "http://helloservice/", className = "helloservice.AddNumbers")
    @ResponseWrapper(localName = "addNumbersResponse", targetNamespace = "http://helloservice/", className = "helloservice.AddNumbersResponse")
    @Action(input = "http://helloservice/Hello/addNumbersRequest", output = "http://helloservice/Hello/addNumbersResponse")
    public int addNumbers(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1);

    /**
     * 
     * @param arg1
     * @param arg0
     * @return
     *     returns double
     * @throws DivideNumbersException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "divideNumbers", targetNamespace = "http://helloservice/", className = "helloservice.DivideNumbers")
    @ResponseWrapper(localName = "divideNumbersResponse", targetNamespace = "http://helloservice/", className = "helloservice.DivideNumbersResponse")
    @Action(input = "http://helloservice/Hello/divideNumbersRequest", output = "http://helloservice/Hello/divideNumbersResponse", fault = {
        @FaultAction(className = DivideNumbersException_Exception.class, value = "http://helloservice/Hello/divideNumbers/Fault/DivideNumbersException")
    })
    public double divideNumbers(
        @WebParam(name = "arg0", targetNamespace = "")
        int arg0,
        @WebParam(name = "arg1", targetNamespace = "")
        int arg1)
        throws DivideNumbersException_Exception
    ;

}
