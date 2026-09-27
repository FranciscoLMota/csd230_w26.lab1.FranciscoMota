package csd230.lab1.pojos;

import java.io.Serializable;

// Marked abstract because it implements SaleableItem but doesn't implement getPrice()
// (Price is defined in children: Ticket and Publication)
public abstract class Product extends Editable implements SaleableItem, Serializable {
    private String productId;
    private double price = 0.0;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
