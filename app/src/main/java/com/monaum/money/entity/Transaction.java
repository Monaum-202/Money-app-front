package com.monaum.money.entity;

public class Transaction {
    public long id;
    public double amount;
    public String category;
    public String wallet;
    public String date;
    public String time;
    public String type; // "income" or "expense"

    public Transaction(long id, double amount, String category, String wallet, String date, String time, String type) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.wallet = wallet;
        this.date = date;
        this.type = type;
        this.time = time;

    }
}
