package productsOrderJson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RestaurantApp {
    
    private RestaurantApp() {
    }

    public static void showMenu (String menuPathnameJson) {
        List<Product> menu = updatedMenuFromJson(menuPathnameJson);
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
            changeQuantityInJson(menuPathnameJson, inputedProductInMenu);

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
        saveOrderInJson(order);
    }

    public static List<Product> updatedMenuFromJson (String pathname) {
        List<Product> jsonMenu = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(pathname)));
            String stringData = "";
            String line = null;
            while (true){
                line = br.readLine();
                if(line == null) break;
                stringData += line;
            }
            JsonObject products = JsonParser.parseString(stringData).getAsJsonObject();
            JsonArray productsArray = products.get("products").getAsJsonArray();
            for (int i = 0; i < productsArray.size(); i++) {
                JsonObject product = productsArray.get(i).getAsJsonObject();
                int id = product.get("id").getAsInt();
                String name = product.get("name").getAsString();
                JsonObject price = product.get("price").getAsJsonObject();
                int priceAmount = price.get("amount").getAsInt();
                String priceCurrency = price.get("currency").getAsString();
                int available = product.get("available").getAsInt();
                jsonMenu.add(new Product(id, name, available, new Money(priceAmount, priceCurrency)));
            }   
        } catch (IOException e) {
            System.out.println("ERROR! Parsing " + pathname + "is not functioning well!");
        }
        return jsonMenu;
    }

    public static void saveOrderInJson(Order order) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            Writer fr = new FileWriter("productsOrderJson/order"+order.getClient().getName().replaceAll(" ", "")+".json");
            JsonObject orderObject = new JsonObject();
            JsonObject clientObject = new JsonObject();
            clientObject.addProperty("name", order.getClient().getName());
            clientObject.addProperty("phone", order.getClient().getPhone());
            JsonArray itemsArray = new JsonArray();
            for (int i = 0; i < order.getItems().size(); i++) {
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("id", order.getItems().get(i).getId());
                itemObject.addProperty("quantity", order.getItems().get(i).getQuantity());
                itemsArray.add(itemObject);
            }
            JsonObject totalObject = new JsonObject();
            totalObject.addProperty("amount", order.getTotal().getAmount());
            totalObject.addProperty("currency", order.getTotal().getCurrency());

            orderObject.add("client", clientObject);
            orderObject.add("items", itemsArray);
            orderObject.add("total", totalObject);
            gson.toJson(orderObject, fr);
            fr.close();

        } catch (IOException e) {
            System.out.println("ERROR! Saving order"+order.getClient().getName().replaceAll(" ", "")+".json is not functioning well!");
        }
    }

    private static void changeQuantityInJson(String menuPathname, Product product) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(menuPathname)));
            String stringData = "";
            String line = null;
            while (true){
                line = br.readLine();
                if(line == null) break;
                stringData += line;
            }
            JsonObject products = JsonParser.parseString(stringData).getAsJsonObject();
            JsonArray productsArray = products.get("products").getAsJsonArray();
            for (int i = 0; i < productsArray.size(); i++) {
                JsonObject productObject = productsArray.get(i).getAsJsonObject();
                int idProductObject = productObject.get("id").getAsInt();
                if(idProductObject == product.getId()) {
                    productObject.addProperty("available", product.getQuantity());
                }
            }
            Writer fr = new FileWriter(menuPathname);
            JsonObject object = new JsonObject();
            object.add("products", productsArray);
            gson.toJson(object, fr);
            fr.close();

        } catch (IOException e) {
            System.out.println("ERROR! The change of quantity in .json file is unsuccessful");
        }
    }
}
