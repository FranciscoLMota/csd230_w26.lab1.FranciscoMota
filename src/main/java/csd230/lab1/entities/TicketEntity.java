package csd230.lab1.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity @DiscriminatorValue("TICKET")
public class TicketEntity extends ProductEntity {
    private String description;
    public TicketEntity() {}
    public TicketEntity(String d, double p) { super(p); this.description = d; }

    @Override public void sellItem() { System.out.println("Selling Ticket: " + description + " for $" + getPrice()); }


    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }
    @Override public String toString() { return "Ticket{desc='" + description + "', price=" + getPrice() + "}"; }
}
