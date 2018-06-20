package dk.tuborgbrackets.weborder.app;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.camel.Expression;
import org.apache.camel.builder.ExpressionBuilder;
import org.apache.camel.jsonpath.JsonPath;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.tuborgbrackets.model.order.Item;

public class MyBean {
  
 
  
  public static String method(@JsonPath("orderperson") String cat
      ,@JsonPath("item") String itemArray)  { 
      System.out.println("catg : " + cat + " au: " + itemArray);
      ObjectMapper mapper = new ObjectMapper();    
      try {
        List<Item> listCar = mapper.readValue(itemArray, new TypeReference<List<Item>>(){});
        for(Item i : listCar) {
          System.out.println("title : " + i.getTitle());
        }
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    return cat + " " + itemArray;
  } 
}
