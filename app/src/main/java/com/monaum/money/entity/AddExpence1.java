package com.monaum.money.entity;

public class AddExpence1 {

    private Long id;

    private Double amount;


    private String category;

    private String wallet;

    private String notes;


    private String date;

    private String time;

    public AddExpence1() {
    }


    public AddExpence1(Long id, Double amount, String category, String wallet, String notes, String date, String time) {
        this.id = id;
        this.amount = amount;
        this.category = category;
        this.wallet = wallet;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }

    public AddExpence1(Double amount, String category, String wallet, String notes, String date, String time) {
        this.amount = amount;
        this.category = category;
        this.wallet = wallet;
        this.notes = notes;
        this.date = date;
        this.time = time;
    }


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
