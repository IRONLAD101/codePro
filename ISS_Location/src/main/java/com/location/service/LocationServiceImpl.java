package com.location.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.location.exceptions.LocationServiceException;
import com.location.response.ISSLocation;

//Service class to retrieve the current location of the International Space Station
@Service
public class LocationServiceImpl implements LocationService {

	@Autowired
	public RestTemplate rest;

	@Value("${API_URL}")
	public String API_URL;

	private static final Logger logger = LogManager.getLogger(LocationServiceImpl.class);

	private static final int MAX_RETRIES = 2;

	private static final long RETRY_BACKOFF_MS = 1000;
	
	public ISSLocation lastKnownLocation;
		
	/**
     * Retrieves the current location of the International Space Station.
     * If the API is down, retries once. If the retry fails, returns the last known location
     * or (0,0) if no location is available.
     *
     * @return The current location of the International Space Station
     * @throws ISSLocationServiceException if there is an error retrieving the location
     */
	@Retryable(maxAttempts = MAX_RETRIES, backoff = @Backoff(delay = RETRY_BACKOFF_MS))
	public ISSLocation getCurrentLocation() throws LocationServiceException {
		logger.debug("Retrieving current location of ISS from API");

		ResponseEntity<ISSLocation> responseEntity;
		try {
			responseEntity = rest.getForEntity(API_URL, ISSLocation.class);
			
		} catch (Exception ex) {
			logger.error("Error retrieving current location of ISS from API: {}", ex.getMessage());
			throw new LocationServiceException("Error retrieving current location of ISS from API", ex);
		}

		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			logger.debug("Retrieved current location of ISS from API: {}", responseEntity.getBody());
			lastKnownLocation = responseEntity.getBody();
			return lastKnownLocation;
		} else {
			logger.error("Error retrieving current location of ISS from API: {} - {}", responseEntity.getStatusCode(), responseEntity.getBody());
			throw new LocationServiceException("Error retrieving current location of ISS from API");
		}
	}

	/**
     * This method is used as a fallback method in case an exception is thrown while attempting
     * to retrieve the current location of the International Space Station from the API
     *
     * @return The previous location or default location of the International Space Station
     * @throws ISSLocationServiceException if there is an error retrieving the location
     */
	@Recover
	public ISSLocation recover(Throwable ex) throws LocationServiceException {
		if (lastKnownLocation != null) {
			logger.debug("Returning last known location of ISS: {}", lastKnownLocation);
			return lastKnownLocation;
		} else {
			logger.debug("Returning default location of (0.0,0.0)");
			return new ISSLocation();
		}
	}

}
