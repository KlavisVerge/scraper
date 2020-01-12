package org.statsplash.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.statsplash.dynamo.StockObject;

public class Scraper {
	
	public void execute() throws IOException {
		Date runDate = new Date();
		System.out.println("Running: " + runDate);
		
		for (int i = 65; i <= 90; i++) {
			var url = new URL("http://eoddata.com/stocklist/NYSE/" + (char)i + ".htm");
	        try (var br = new BufferedReader(new InputStreamReader(url.openStream()))) {
	            String line;
	            int stockCount = 0;
	            while ((line = br.readLine()) != null) {
	            	if(line.contains("/stockquote/NYSE")) {
	            		String symbol = line.substring(line.indexOf("/stockquote/NYSE/") + 17, line.indexOf(".htm"));
	            		System.out.println(symbol);
	            		StockObject stockObject = checkYahoo(symbol, runDate);
	            		System.out.println(stockObject);
	            		stockCount++;
	            		if(stockCount == 24) {
	            			stockCount = 0;
	            			// TODO: write to dynamo
	            		}
	            	}
	            }
	        }
		}
		
	}

	private StockObject checkYahoo(String symbol, Date runDate) throws IOException {
		StockObject stockObject = new StockObject();
		stockObject.setSymbol(symbol);
		stockObject.setDate(runDate);
	    String inputLine;
	    URL link = new URL("https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol);
	    URLConnection con = link.openConnection();
	    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
	    try(BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(),"UTF-8"))){
	    	 while ((inputLine = in.readLine()) != null){
	 	        if(inputLine.contains("\"currentPrice\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"currentPrice\":{\"raw\":") + 22);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	stockObject.setPrice(new BigDecimal(value));
	 	        }
	 	        if(inputLine.contains("\"regularMarketVolume\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"regularMarketVolume\":{\"raw\":") + 29);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	stockObject.setVolume(new BigDecimal(value));
	 	        }
	 	        if(inputLine.contains("\"averageDailyVolume3Month\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"averageDailyVolume3Month\":{\"raw\":") + 34);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	stockObject.setAverageVolume(new BigDecimal(value));
	 	        }
	 	        if(inputLine.contains("\"targetMeanPrice\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"targetMeanPrice\":{\"raw\":") + 25);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	stockObject.setTargetPrice(new BigDecimal(value));
	 	        }
	 	        if(inputLine.contains(",\"rating\":\"")) {
	 	        	var value = inputLine.substring(inputLine.indexOf(",\"rating\":\"") + 11);
	 	        	value = value.substring(0, value.indexOf("\"}}"));
	 	        	stockObject.setRating(value);
	 	        }
	 	        if(inputLine.contains("\"valuation\":{\"color\":0,\"description\":\"")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"valuation\":{\"color\":0,\"description\":\"") + 38);
	 	        	value = value.substring(0, value.indexOf("\",\""));
	 	        	stockObject.setEvaulation(value);
	 	        }
	 	    }
	    }
	    return stockObject;
	}

	public static void main(String[] args) {
		Scraper runner = new Scraper();
		try {
			runner.execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
