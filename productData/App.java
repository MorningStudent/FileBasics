package productData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {

        File file = new File("productData/products.csv");
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        String line = br.readLine();
        String[] cols = line.split(",");

        Product p1 = new Product(cols[0], Integer.parseInt(cols[1]));

        System.out.println(p1);

        /*HW1
         * -------------valueOf()-----------------|---------------parseInt()------------------|
         * -> It returns an Integer object holding|-> It returns primitive data type (int)    |
         * the value passed as a argument         |                                           |
         * ---------------------------------------|-------------------------------------------|
         * -> It accepts both String and int as a |-> It accepts only String as a parameter   |
         * parameter, also accepts char returning |                                           |
         * it's unicode vale                      |                                           |
         * _______________________________________|___________________________________________|
         */

    }
}

class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product [name=" + name + ", price=" + price + "]";
    }
    
}
