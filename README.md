RestCountries https://restcountries.eu
To expose
1.	Top N countries with the biggest population density. url call: http://localhost:9090/density?number={number of countries}, default=10 http://localhost:9090/density?number=5
2.	Countries with the given currency. url call: http://localhost:9090/currency?name={currency}, default=EUR
3.	Countries under the pattern { letters + * }, any combinations & case-insensitive. url call: http://localhost:9090/pattern?search={pattern}, default=all countries
