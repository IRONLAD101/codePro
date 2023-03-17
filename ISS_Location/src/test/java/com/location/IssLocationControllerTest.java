package com.location;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.location.controller.ISSLocationController;
import com.location.exceptions.LocationServiceException;
import com.location.response.ISSLocation;
import com.location.service.LocationService;

@ExtendWith(MockitoExtension.class)
class IssLocationControllerTest {

	@Mock
	LocationService locationService;

	@InjectMocks
	ISSLocationController issLocationController;

	/**
	 * Test method for successful location retrieval
	 * 
	 * @throws LocationServiceException
	 */
	@Test
	void getCurrentLocation() throws LocationServiceException {
		ISSLocation location = new ISSLocation(51.5072, -0.1276);
		when(locationService.getCurrentLocation()).thenReturn(location);

		ResponseEntity<ISSLocation> response = issLocationController.getCurrentLocation();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(location, response.getBody());
	}

	/**
	 * Test method for location retrieval error
	 * 
	 * @throws LocationServiceException
	 */
	@Test
	void getCurrentLocation_error() throws LocationServiceException {
		String errorMsg = "Something went wrong";
		ISSLocation location = new ISSLocation(0.0, 0.0);
		when(locationService.getCurrentLocation()).thenThrow(new LocationServiceException(errorMsg));

		ResponseEntity<ISSLocation> response = issLocationController.getCurrentLocation();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(location, response.getBody());
	}
}
