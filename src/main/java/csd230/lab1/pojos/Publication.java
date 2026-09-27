package csd230.lab1.pojos;

import java.util.Objects;

public abstract class Publication extends Product {
    private String title = "";
    private int copies = 0;

    public Publication() {
    }

    public Publication(String title, double price, int copies) {
        this.title = title;
        setPrice(price);
        this.copies = copies;
    }

    @Override
    public void initialize() {
        System.out.println("Enter Title:");
        this.title = getInput("Available Title"); // "Available Title" is default if empty
    }

    // Helper used by subclasses during initialize
    protected void initPriceCopies() {
        System.out.println("Enter copies:");
        this.copies = getInput(0);

        System.out.println("Enter price:");
        setPrice(getInput(0.0));
    }

    @Override
    public void edit() {
        System.out.println("Edit Title [" + this.title + "]:");
        this.title = getInput(this.title);

        System.out.println("Edit Price [" + getPrice() + "]:");
        setPrice(getInput(getPrice()));

        System.out.println("Edit Copies [" + this.copies + "]:");
        this.copies = getInput(this.copies);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "Publication{title='" + title + "', price=" + getPrice() + ", copies=" + copies + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Publication)) return false;
        Publication that = (Publication) o;
        return Double.compare(that.getPrice(), getPrice()) == 0 &&
                copies == that.copies &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, getPrice(), copies);
    }
}
