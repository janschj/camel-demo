package dk.tuborgbrackets.order.item.routes;

import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class XmlUtil {

  public static String toString(Object obj) {
    try {

      JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      StringWriter sw = new StringWriter();
      jaxbMarshaller.marshal(obj, sw);
      return sw.toString();
    } catch (JAXBException e) {
      e.printStackTrace();
      return "";
    }

  }
}

