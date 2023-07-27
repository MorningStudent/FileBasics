package productsOrderXml;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<Product> items;
    private Client client;
    private Money total;

    public Order() {
        items  = new ArrayList<>();
        client = new Client();
        total  = new Money();
    }

    public List<Product> getItems() {
        return items;
    }

    public void setItems(List<Product> items) {
        this.items = items;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Money getTotal() {
        return total;
    }

    public void setTotal(Money total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Order [items: " + items + ", client: " + client + ", total: " + total + "]";
    }

    
    
}
