package com.buygoods.orderservice.models;

public class Amount {
    private Double amount;

    public Amount(Double amount) {
        this.setAmount(amount);
    }

    public void addAmount(Double subtotal) {
        this.amount += subtotal;
    }

    public void performAdjustment(Double adjustment) {
        if(adjustment >= 0 && adjustment <= 10) {
            this.amount *= adjustment;
        }
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
