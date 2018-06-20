package dk.tuborgbrackets.addorderid.processor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringBufferInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AddOrderIdAttribute implements Processor {


	@Override
	public void process(Exchange exchange) throws Exception {
		Document doc = getDocument(exchange.getIn().getBody(String.class));
		System.out.println("xml in :" + exchange.getIn().getBody(String.class));
		updateElementValue(doc, exchange.getIn().getHeader("orderId").toString());
		String result = getString(doc);
		System.out.println("xml ud :" + result);
		exchange.getIn().setBody(result);
	}
	
	private Document getDocument(String xml) {
		System.out.println("xml ind :" + xml);
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new StringBufferInputStream(xml));
			doc.getDocumentElement().normalize();
			return doc;
		} catch (ParserConfigurationException e) {
		} catch (SAXException e) {
		} catch (IOException e) {
		}
		return null;
	}

	private String getString(Document doc) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(baos);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			return baos.toString();
		} catch (TransformerException e1) {
			e1.printStackTrace();
			return "";
		}

	}

	
	
	private static void updateElementValue(Document doc, String id) {
		NodeList employees = doc.getElementsByTagName("shiporder");
		Element emp = null;
		for (int i = 0; i < employees.getLength(); i++) {
			emp = (Element) employees.item(i);
			Attr genderAttribute = doc.createAttribute("orderId");
			genderAttribute.setValue(id);
			emp.setAttributeNode(genderAttribute);
		}
	}

}
