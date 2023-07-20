package parsingXmlProducts;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class App {
    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

        List<Product> products2 = new ArrayList<>();

        File file = new File("parsingXmlProducts/products.xml");

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(file);
        
        Element root = document.getDocumentElement();
        NodeList products = root.getElementsByTagName("product");
        int n = 0;
        while(true) {
            Element product = (Element)products.item(n);
            if(product == null) break;
            Element productName =  (Element)product.getElementsByTagName("name").item(0);
            Element productPrice =  (Element)product.getElementsByTagName("price").item(0);
            Element productPriceAmount = (Element)productPrice.getElementsByTagName("amount").item(0);
            Element productPriceCurrency = (Element)productPrice.getElementsByTagName("currency").item(0);

            products2.add(new Product(productName.getTextContent().trim(),
            new Money(Integer.parseInt(productPriceAmount.getTextContent().trim()), productPriceCurrency.getTextContent().trim())));
            n++;
        }
        System.out.println(products2);
    }
}

class Product {
    private String name;
    private Money price;

    public Product(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", price=" + price + "]";
    }
    
}

class Money {
    private int amount;
    private String currency;

    public Money(int amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    @Override
    public String toString() {
        return  amount + " " + currency;
    }
    
}