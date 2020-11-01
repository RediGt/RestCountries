package com.restcountries.main.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.restcountries.main.model.Country;

@RestController
public class RestControllerCl {
	
    private String JSONSTRING = "";
    private final GsonBuilder BUILDER = new GsonBuilder();
    private final Gson gson = BUILDER.create();
	
    //URL request example http://localhost:9090/density?number={number of countries}
    @GetMapping("/density")
	public String getCountriesByNumber(@RequestParam(value="number", defaultValue="10") String number) {
    	String urlDensity = "https://restcountries.eu/rest/v2/regionalbloc/eu?fields=name;capital;currencies;population;area";
		ArrayList<Country> list = queryAPI(urlDensity);
		ArrayList<Country> sortedList = biggestCountriesByDensity(list, Integer.valueOf(number));
		return number + " Countries with biggest population density:  " + sortedList.toString();
	}

    //URL request example http://localhost:9090/currency?name={currency}
    @GetMapping("/currency")
	public String getCountriesByCurrency(@RequestParam(value="name", defaultValue="EUR") String name) {
    	String urlCurrency = "https://restcountries.eu/rest/v2/currency/" + 
    			name + "?fields=name;capital;currencies;population;area";
		ArrayList<Country> list = queryAPI(urlCurrency);
		ArrayList<Country> selectedList = countriesFromEuWithGivenCurrency(list);
		return "Countries with " + name + "  " + selectedList.toString();
	}
    
    //URL request example http://localhost:9090/pattern?search={pattern}
    @GetMapping("/pattern")
	public String getCountriesByPattern(@RequestParam(value="search", defaultValue="all") String pattern) {
    	String urlCurrency = "https://restcountries.eu/rest/v2/regionalbloc/eu?fields=name;capital;currencies;population;area";
		ArrayList<Country> list = queryAPI(urlCurrency);
		ArrayList<Country> selectedList = countriesMatchPattern(list, pattern);
		return "Countries matching pattern " + pattern + "  " + selectedList.toString();
	}

	public ArrayList<Country> queryAPI(String path) {
        try {
            JSONSTRING = "";
            URL url = new URL(path);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
            StringBuilder content = new StringBuilder();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
            JSONSTRING = content.toString();
            conn.disconnect();
        } catch (IOException ex) {
            System.out.println("Error");
        }
        return parseJSONtoObject(JSONSTRING);
    }

    public ArrayList<Country> parseJSONtoObject(String jsonString) {
        ArrayList<Country> list = new ArrayList<>();
        JsonArray jsonelement = JsonParser.parseString(jsonString).getAsJsonArray();
        if (jsonelement.isJsonArray()) {
            for (JsonElement el : jsonelement) {
                list.add(parser(el));
            }
        }
        return list;
    }
    
    public Country parser(JsonElement element) {
        JsonObject elem = element.getAsJsonObject();
        Country country = gson.fromJson(elem.toString(), Country.class);
        return country;
    }
    
    public ArrayList<Country> biggestCountriesByDensity(ArrayList<Country> list, int number) {
    	ArrayList<Country> sortedList = new ArrayList<Country>();
    	
    	if (number > list.size()) {
    		number = list.size();
    	}
    	for (int j = 0; j < number; j++) {
	    	double maxDensity = 0;
			int indexOfMax = 0;
	    	for (int i = 0; i < list.size(); i++) {
	    		if (list.get(i).getArea() > 0.01 && list.get(i).getPopulation() / list.get(i).getArea() > maxDensity) {
	    			maxDensity = list.get(i).getPopulation() / list.get(i).getArea();
	    			indexOfMax = i;
	    		} 		
	    	}
	    	sortedList.add(list.get(indexOfMax));
	    	list.remove(indexOfMax);
    	}
    	return sortedList;
    }
    
    public ArrayList<Country> countriesMatchPattern(ArrayList<Country> list, String pattern) {
    	ArrayList<Country> selectedList = new ArrayList<Country>();
       	
    	if (pattern.equals("all")) {
    		return list;
    	}
    	
    	String pat = pattern.replaceAll("\\*", ".*");
    	
    	for (int i = 0; i < list.size(); i++) {
    		if (Pattern.matches("(?i)" + pat, list.get(i).getName()) ) {
    			selectedList.add(list.get(i));
    		} 		
    	}
    	return selectedList;
    }
    
    public ArrayList<Country> countriesFromEuWithGivenCurrency(ArrayList<Country> list) {   	
    	ArrayList<Country> selectedList = new ArrayList<Country>();
    	String urlDensity = "https://restcountries.eu/rest/v2/regionalbloc/eu?fields=name;capital;currencies;population;area";
		ArrayList<Country> listOfEuCountries = queryAPI(urlDensity);
		
		for (int i = 0; i < list.size(); i++) {
			Country country = list.get(i);
			for (int j = 0; j < listOfEuCountries.size(); j++) {
				if (country.getName().equals(listOfEuCountries.get(j).getName())) {
					selectedList.add(country);
				}
			}
		}
    	
    	return selectedList;
    }
}