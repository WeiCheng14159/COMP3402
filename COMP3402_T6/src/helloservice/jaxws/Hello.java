
package helloservice.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Hello", namespace = "http://helloservice/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Hello", namespace = "http://helloservice/")
public class Hello {


}
