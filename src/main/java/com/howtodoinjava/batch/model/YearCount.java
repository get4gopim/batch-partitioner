package com.howtodoinjava.batch.model;

public class YearCount {

    private int year;
    private int customerCount;

    public YearCount() {}

    public YearCount(int year, int customerCount) {
        this.year = year;
        this.customerCount = customerCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("YearCount{");
        sb.append("year=").append(year);
        sb.append(", customerCount=").append(customerCount);
        sb.append('}');
        return sb.toString();
    }
}
