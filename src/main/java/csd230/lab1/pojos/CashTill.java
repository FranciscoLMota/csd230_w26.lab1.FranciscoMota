package csd230.lab1.pojos;

public class CashTill {
    private Double runningTotal;

    public CashTill() {
        this.runningTotal = 0.0;
    }

    public void showTotal() {
        System.out.println("Current Cash Till Total: $" + runningTotal);
    }

    public void sellItem(SaleableItem item) {
        item.sellItem();
        runningTotal += item.getPrice();
        System.out.println("Added $" + item.getPrice() + " to till.");
    }

    public Double getRunningTotal() {
        return runningTotal;
    }
}
