package productsOrderJson;

public class Product implements Cloneable{
    private int id;
    private String name;
    private int quantity;
    private Money price;

    public Product(int id, String name, int quantity, Money price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Product [id: " + id + ", name: " + name + ", quantiy: " + quantity + ", price: " + price + "]";
    }
    
    public final Product cloneWithNewQuantity(int quantity) {
        Product clonneProduct = null;
        try {
            clonneProduct = (Product)this.clone();
            clonneProduct.setQuantity(quantity);
        } catch (CloneNotSupportedException e) {
           System.out.println("CLONE ERROR!");
        }
        return clonneProduct;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
