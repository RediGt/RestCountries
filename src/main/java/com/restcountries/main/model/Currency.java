package com.restcountries.main.model;

public class Currency {
	public String code;
    public String name;
    public String symbol;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return "Currency{" + "code=" + code + ", name=" + name + ", symbol=" + symbol + '}';
    }
}
