package productsOrderXml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RestaurantApp {

    private RestaurantApp() {
    }

    public static void showMenu (String menuPathnameXml) {
        List<Product> menu = updatedMenuFromXml(menuPathnameXml);
        Scanner in = new Scanner(System.in);
        System.out.println("Hi! This is our Menu:\n");
        for (Product product : menu) {
            System.out.println(String.format("%3s", product.getId()) + ". " + 
            String.format("%-8s", product.getName()) + " - " + product.getPrice());
        }
        Order order = new Order();
        order.getTotal().setCurrency(menu.get(0).getPrice().getCurrency());
        order.getTotal().setAmount(0);
        while (true) {
            System.out.println("\nChoose and enter the id of the item, after that input quantity:");
            int productId = in.nextInt();
            int quantity  = in.nextInt();
            Product inputedProductInMenu = menu.get(productId-1);
            do {
                if (quantity > inputedProductInMenu.getQuantity()) {
                System.out.println("Selected quantity is unavailable, please select less or " + 
                inputedProductInMenu.getQuantity());
                quantity = in.nextInt();
                }
            } while (quantity > inputedProductInMenu.getQuantity());

            order.getItems().add(inputedProductInMenu.cloneWithNewQuantity(quantity));
            inputedProductInMenu.setQuantity(inputedProductInMenu.getQuantity() - quantity);
            order.getTotal().setAmount(order.getTotal().getAmount() + (inputedProductInMenu.getPrice().getAmount() * quantity));
            changeQuantityInXml(menuPathnameXml, inputedProductInMenu);

            System.out.println("\nYou selected:");
            for (Product item : order.getItems()) {
                System.out.println(String.format("%-8s", item.getName()) + " " + item.getQuantity());
            }
            System.out.println("Total: " + order.getTotal());
            System.out.println("Would you like to select more? Say \"no\" if so:");
            String answer = in.next();
            if (answer.equals("No") || answer.equals("no")) break;
        }
        System.out.println("Enter your name: ");
        in.nextLine();
        order.getClient().setName(in.nextLine());
        System.out.println("Enter your phone number: ");
        order.getClient().setPhone(in.nextLine());
        saveOrderInXml(order);
    }

    public static List<Product> updatedMenuFromXml (String pathname) {
        List<Product> xmlMenu = new ArrayList<>();
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(pathname));
            NodeList products = document.getDocumentElement().getElementsByTagName("product");
            int n = 0;
            while(true) {
                Element product = (Element)products.item(n);
                if(product == null) break;
                Element productId =  (Element)product.getElementsByTagName("id").item(0);
                Element productName =  (Element)product.getElementsByTagName("name").item(0);
                Element productPrice =  (Element)product.getElementsByTagName("price").item(0);
                Element productPriceAmount = (Element)productPrice.getElementsByTagName("amount").item(0);
                Element productPriceCurrency = (Element)productPrice.getElementsByTagName("currency").item(0);
                Element productAvailableQuantity =  (Element)product.getElementsByTagName("available").item(0);

                xmlMenu.add(new Product(Integer.parseInt(productId.getTextContent().trim()), productName.getTextContent().trim(),
                Integer.parseInt(productAvailableQuantity.getTextContent().trim()),
                new Money(Integer.parseInt(productPriceAmount.getTextContent().trim()), productPriceCurrency.getTextContent().trim())));
                n++;
            }
        } catch (SAXException| IOException| ParserConfigurationException e) {
            System.out.println("ERROR! Parsing products.xml is not functioning well!");
        }
        return xmlMenu;
    }

    public static void saveOrderInXml(Order order) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element orderElement = document.createElement("order");
            document.appendChild(orderElement);
            Element client = document.createElement("client");
            orderElement.appendChild(client);
            Element name = document.createElement("name");
            name.setTextContent(order.getClient().getName());
            client.appendChild(name);
            Element phone = document.createElement("phone");
            phone.setTextContent(order.getClient().getPhone());
            client.appendChild(phone);
            Element items = document.createElement("items");
            orderElement.appendChild(items);
            for (int i = 0; i < order.getItems().size(); i++) {
                Element item = document.createElement("item");
                items.appendChild(item);
                Element productId = document.createElement("product_id");
                productId.setTextContent(Integer.toString(order.getItems().get(i).getId()));
                item.appendChild(productId);
                Element quantityElement = document.createElement("quantity");
                quantityElement.setTextContent(Integer.toString(order.getItems().get(i).getQuantity()));
                item.appendChild(quantityElement);
            }
            Element total = document.createElement("total");
            orderElement.appendChild(total);
            Element amount = document.createElement("amount");
            amount.setTextContent(Integer.toString(order.getTotal().getAmount()));
            total.appendChild(amount);
            Element currency = document.createElement("currency");
            currency.setTextContent(order.getTotal().getCurrency());
            total.appendChild(currency);

            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            Source src = new DOMSource(document);
            Result result = new StreamResult(new File("productsOrderXml/order"+order.getClient().getName().replaceAll(" ", "")+".xml"));
            t.transform(src, result);

        } catch (ParserConfigurationException| TransformerFactoryConfigurationError| TransformerException e) {
            System.out.println("ERROR! Saving order"+order.getClient().getName().replaceAll(" ", "")+".xml is not functioning well!");
        }
    }

    private static void changeQuantityInXml(String menuPathname, Product product) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(menuPathname));
            NodeList products = document.getDocumentElement().getElementsByTagName("product");
            for (int i = 0; i < products.getLength(); i++) { 
                Element productElement = (Element) products.item(i);
                if (productElement.getElementsByTagName("id").item(0).getTextContent().trim().equals(Integer.toString(product.getId()).trim())) {
                    productElement.getElementsByTagName("available").item(0).setTextContent(Integer.toString(product.getQuantity()));                  
                }
            }
            Transformer t = TransformerFactory.newInstance().newTransformer();
            Source src = new DOMSource(document);
            Result result = new StreamResult(new File(menuPathname));
            t.transform(src, result);

        } catch (SAXException| IOException| ParserConfigurationException| TransformerException e) {
            System.out.println("ERROR! The change of quantity in xml is unsuccessful");
        }
    }
}
