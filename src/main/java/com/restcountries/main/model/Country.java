package com.restcountries.main.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

	private String name;
	private String capital;
	private int population;
	private double area;
	private ArrayList<Currency> currency;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Currency> getCurrency() {
		return currency;
	}

	public void setCurrency(ArrayList<Currency> currency) {
		this.currency = currency;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}
	
	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	@Override
    public String toString() {
		//return "Country [name=" + name + "; capital=" + capital + "; population=" + population + "; currency=" + currency.get(1).toString() + "]";
		return name + "   ";
    }
}