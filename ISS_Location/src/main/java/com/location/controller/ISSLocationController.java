package com.location.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.location.exceptions.LocationServiceException;
import com.location.response.ISSLocation;
import com.location.service.LocationService;

//Controller class to expose the ISS location API
@RestController
@RequestMapping(value = "/location")
public class ISSLocationController {
	
	private static final Logger logger = LogManager.getLogger(ISSLocationController.class);
	
	@Autowired
	LocationService locationService;
	
	/**
     * Retrieves the current location of the International Space Station.
     *
     * @return The current location of the International Space Station
     */
	@GetMapping(path = "/currentISSLocation")
	public ResponseEntity<ISSLocation> getCurrentLocation(){

        logger.debug("Getting current location of ISS");
        try {
            ISSLocation location = locationService.getCurrentLocation();
            return new ResponseEntity<>(location, HttpStatus.OK);
            
        } catch (LocationServiceException ex) {
            logger.error("Error getting current location of ISS: {}", ex.getMessage());
            return new ResponseEntity<>(new ISSLocation(), HttpStatus.OK);
        }
	}

}
