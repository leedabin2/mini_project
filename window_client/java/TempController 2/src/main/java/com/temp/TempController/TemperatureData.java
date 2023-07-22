package com.temp.TempController;

// TemperatureData.java
public class TemperatureData {
	private double celsius;
	private double fahrenheit;

	public TemperatureData(double celsius, double fahrenheit) {
		this.celsius = celsius;
		this.fahrenheit = fahrenheit;
	}

	public double getCelsius() {
		return celsius;
	}

	public void setCelsius(double celsius) {
		this.celsius = celsius;
	}

	public double getFahrenheit() {
		return fahrenheit;
	}

	public void setFahrenheit(double fahrenheit) {
		this.fahrenheit = fahrenheit;
	}
}




