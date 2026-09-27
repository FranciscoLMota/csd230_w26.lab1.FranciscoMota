package csd230.lab1.pojos;

public class Ticket extends Product {
    public String description = "";

    @Override
    public void sellItem() {
        System.out.println("Selling Ticket: " + description + " for " + getPrice());
    }

    @Override
    public void initialize() {
        System.out.println("Enter Description:");
        this.description = getInput("Ticket");

        System.out.println("Enter Price:");
        setPrice(getInput(0.0));
    }

    @Override
    public void edit() {
        System.out.println("Edit Description [" + this.description + "]:");
        this.description = getInput(this.description);

        System.out.println("Edit Price [" + getPrice() + "]:");
        setPrice(getInput(getPrice()));
    }

    @Override
    public String toString() {
        return "Ticket{desc='" + description + "', price=" + getPrice() + "}";
    }
}
