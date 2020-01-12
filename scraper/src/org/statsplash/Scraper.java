package org.statsplash;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Scraper {
	
	public void execute() throws IOException {
		System.out.println("Running");
		
		for (int i = 65; i <= 90; i++) {
			var url = new URL("http://eoddata.com/stocklist/NYSE/" + (char)i + ".htm");
	        try (var br = new BufferedReader(new InputStreamReader(url.openStream()))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	            	if(line.contains("/stockquote/NYSE")) {
	            		String symbol = line.substring(line.indexOf("/stockquote/NYSE/") + 17, line.indexOf(".htm"));
	            		System.out.println(symbol);
	            		checkYahoo(symbol);
	            	}
	            }
	        }
		}
		
	}

	private void checkYahoo(String symbol) throws IOException {
	    String inputLine;
	    URL link = new URL("https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol);
	    URLConnection con = link.openConnection();
	    con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.117 Safari/537.36");
	    try(BufferedReader in = new BufferedReader( new InputStreamReader(con.getInputStream(),"UTF-8"))){
	    	 while ((inputLine = in.readLine()) != null){
	 	        if(inputLine.contains("\"currentPrice\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"currentPrice\":{\"raw\":") + 22);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	System.out.println(value);
	 	        }
	 	        if(inputLine.contains("\"regularMarketVolume\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"regularMarketVolume\":{\"raw\":") + 29);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	System.out.println(value);
	 	        }
	 	        if(inputLine.contains("\"averageDailyVolume3Month\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"averageDailyVolume3Month\":{\"raw\":") + 34);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	System.out.println(value);
	 	        }
	 	        if(inputLine.contains("\"targetMeanPrice\":{\"raw\":")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"targetMeanPrice\":{\"raw\":") + 25);
	 	        	value = value.substring(0, value.indexOf(",\"fmt\""));
	 	        	System.out.println(value);
	 	        }
	 	        if(inputLine.contains(",\"rating\":\"")) {
	 	        	var value = inputLine.substring(inputLine.indexOf(",\"rating\":\"") + 11);
	 	        	value = value.substring(0, value.indexOf("\"}}"));
	 	        	System.out.println(value);
	 	        }
	 	        if(inputLine.contains("\"valuation\":{\"color\":0,\"description\":\"")) {
	 	        	var value = inputLine.substring(inputLine.indexOf("\"valuation\":{\"color\":0,\"description\":\"") + 38);
	 	        	value = value.substring(0, value.indexOf("\",\""));
	 	        	System.out.println(value);
	 	        }
	 	    }
	    }
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
