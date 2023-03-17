package com.location.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ISSLocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JsonAlias(value = "latitude")
	private double latitude;
	
	@JsonAlias(value = "longitude")
	private double longitude;

	public ISSLocation(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public ISSLocation() {
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return "[latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
	
	
}
