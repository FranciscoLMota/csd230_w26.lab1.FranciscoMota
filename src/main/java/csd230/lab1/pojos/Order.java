package csd230.lab1.pojos;

import csd230.lab1.entities.OrderEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DTO for {@link OrderEntity}
 */
public class Order {
    private double totalAmount;
    private LocalDateTime orderDate;
    private List<Product> products = new ArrayList<>();

    public Order() {
    }

    public Order(double totalAmount, LocalDateTime orderDate, List<Product> products) {
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.products = products;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Double.compare(totalAmount, order.totalAmount) == 0
                && Objects.equals(orderDate, order.orderDate)
                && Objects.equals(products, order.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalAmount, orderDate, products);
    }

    @Override
    public String toString() {
        return "Order{" +
                "totalAmount=" + totalAmount +
                ", orderDate=" + orderDate +
                ", products=" + products +
                '}';
    }
}
