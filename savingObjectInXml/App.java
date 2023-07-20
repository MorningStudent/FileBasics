package savingObjectInXml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class App {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException {

        List<Product> products2 = new ArrayList<>();
        products2.add(new Product("Pizza", new Money(100, "MDL")));
        products2.add(new Product("Salad", new Money(50, "MDL")));
        products2.add(new Product("Soup", new Money(45, "MDL")));
        products2.add(new Product("Dessert", new Money(35, "MDL")));
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element products = document.createElement("products");
        document.appendChild(products);

        for (int i = 0; i < products2.size(); i++) {
            Element product = document.createElement("product");
            products.appendChild(product);
            Element name = document.createElement("name");
            name.setTextContent(products2.get(i).getName());
            product.appendChild(name);
            Element price = document.createElement("price");
            product.appendChild(price);
            Element amount = document.createElement("amount");
            amount.setTextContent(Integer.toString(products2.get(i).getPrice().getAmount()));
            Element currency = document.createElement("currency");
            currency.setTextContent(products2.get(i).getPrice().getCurrency());
            price.appendChild(amount);
            price.appendChild(currency);
        }

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.INDENT, "yes");

        Source src = new DOMSource(document);
        Result result = new StreamResult(new File("savingObjectInXml/products.xml"));
        t.transform(src, result);
    }
    
}

class Product {
    private String name;
    private Money price;

    public Product(String name, Money price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
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

    public int getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return  amount + " " + currency;
    }
    
}