package csd230.lab1.entities;

import jakarta.persistence.Entity;

@Entity
public abstract class PublicationEntity extends ProductEntity {
    private String title;
    private int copies;
    public PublicationEntity() {}
    public PublicationEntity(String t, double p, int c) { super(p); this.title = t; this.copies = c; }

    @Override public void sellItem() {
        if (copies > 0) { copies--; System.out.println("Sold '" + title + "'. Remaining copies: " + copies); }
        else { System.out.println("Cannot sell '" + title + "'. Out of stock."); }
    }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
    public int getCopies() { return copies; }
    public void setCopies(int c) { this.copies = c; }
    @Override public String toString() { return "Pub{title='" + title + "', price=" + getPrice() + ", copies=" + copies + "}"; }
}
