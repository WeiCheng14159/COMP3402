
package helloservice;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the helloservice package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SayHelloResponse_QNAME = new QName("http://helloservice/", "sayHelloResponse");
    private final static QName _AddNumbers_QNAME = new QName("http://helloservice/", "addNumbers");
    private final static QName _AddNumbersResponse_QNAME = new QName("http://helloservice/", "addNumbersResponse");
    private final static QName _HelloResponse_QNAME = new QName("http://helloservice/", "HelloResponse");
    private final static QName _Hello_QNAME = new QName("http://helloservice/", "Hello");
    private final static QName _DivideNumbersResponse_QNAME = new QName("http://helloservice/", "divideNumbersResponse");
    private final static QName _DivideNumbersException_QNAME = new QName("http://helloservice/", "DivideNumbersException");
    private final static QName _DivideNumbers_QNAME = new QName("http://helloservice/", "divideNumbers");
    private final static QName _SayHello_QNAME = new QName("http://helloservice/", "sayHello");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: helloservice
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DivideNumbersException }
     * 
     */
    public DivideNumbersException createDivideNumbersException() {
        return new DivideNumbersException();
    }

    /**
     * Create an instance of {@link DivideNumbers }
     * 
     */
    public DivideNumbers createDivideNumbers() {
        return new DivideNumbers();
    }

    /**
     * Create an instance of {@link SayHello }
     * 
     */
    public SayHello createSayHello() {
        return new SayHello();
    }

    /**
     * Create an instance of {@link Hello_Type }
     * 
     */
    public Hello_Type createHello_Type() {
        return new Hello_Type();
    }

    /**
     * Create an instance of {@link DivideNumbersResponse }
     * 
     */
    public DivideNumbersResponse createDivideNumbersResponse() {
        return new DivideNumbersResponse();
    }

    /**
     * Create an instance of {@link AddNumbers }
     * 
     */
    public AddNumbers createAddNumbers() {
        return new AddNumbers();
    }

    /**
     * Create an instance of {@link AddNumbersResponse }
     * 
     */
    public AddNumbersResponse createAddNumbersResponse() {
        return new AddNumbersResponse();
    }

    /**
     * Create an instance of {@link HelloResponse }
     * 
     */
    public HelloResponse createHelloResponse() {
        return new HelloResponse();
    }

    /**
     * Create an instance of {@link SayHelloResponse }
     * 
     */
    public SayHelloResponse createSayHelloResponse() {
        return new SayHelloResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHelloResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "sayHelloResponse")
    public JAXBElement<SayHelloResponse> createSayHelloResponse(SayHelloResponse value) {
        return new JAXBElement<SayHelloResponse>(_SayHelloResponse_QNAME, SayHelloResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNumbers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "addNumbers")
    public JAXBElement<AddNumbers> createAddNumbers(AddNumbers value) {
        return new JAXBElement<AddNumbers>(_AddNumbers_QNAME, AddNumbers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddNumbersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "addNumbersResponse")
    public JAXBElement<AddNumbersResponse> createAddNumbersResponse(AddNumbersResponse value) {
        return new JAXBElement<AddNumbersResponse>(_AddNumbersResponse_QNAME, AddNumbersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HelloResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "HelloResponse")
    public JAXBElement<HelloResponse> createHelloResponse(HelloResponse value) {
        return new JAXBElement<HelloResponse>(_HelloResponse_QNAME, HelloResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Hello_Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "Hello")
    public JAXBElement<Hello_Type> createHello(Hello_Type value) {
        return new JAXBElement<Hello_Type>(_Hello_QNAME, Hello_Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DivideNumbersResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "divideNumbersResponse")
    public JAXBElement<DivideNumbersResponse> createDivideNumbersResponse(DivideNumbersResponse value) {
        return new JAXBElement<DivideNumbersResponse>(_DivideNumbersResponse_QNAME, DivideNumbersResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DivideNumbersException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "DivideNumbersException")
    public JAXBElement<DivideNumbersException> createDivideNumbersException(DivideNumbersException value) {
        return new JAXBElement<DivideNumbersException>(_DivideNumbersException_QNAME, DivideNumbersException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DivideNumbers }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "divideNumbers")
    public JAXBElement<DivideNumbers> createDivideNumbers(DivideNumbers value) {
        return new JAXBElement<DivideNumbers>(_DivideNumbers_QNAME, DivideNumbers.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SayHello }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://helloservice/", name = "sayHello")
    public JAXBElement<SayHello> createSayHello(SayHello value) {
        return new JAXBElement<SayHello>(_SayHello_QNAME, SayHello.class, null, value);
    }

}
