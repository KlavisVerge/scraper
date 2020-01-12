package org.statsplash.dynamo;

import java.math.BigDecimal;
import java.util.Date;

public class StockObject {
	private BigDecimal price;
	private BigDecimal volume;
	private BigDecimal averageVolume;
	private BigDecimal targetPrice;
	private String rating;
	private String evaulation;
	private String symbol;
	private Date date;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getAverageVolume() {
		return averageVolume;
	}
	public void setAverageVolume(BigDecimal averageVolume) {
		this.averageVolume = averageVolume;
	}
	public BigDecimal getTargetPrice() {
		return targetPrice;
	}
	public void setTargetPrice(BigDecimal targetPrice) {
		this.targetPrice = targetPrice;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getEvaulation() {
		return evaulation;
	}
	public void setEvaulation(String evaulation) {
		this.evaulation = evaulation;
	}
	@Override
	public String toString() {
		return this.getSymbol() + " " + this.getPrice() + " " + this.getDate() + " " + this.getVolume() + " " + this.getAverageVolume() + " " + this.getTargetPrice() + " " + this.getRating() + " " + this.getEvaulation();
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
}
