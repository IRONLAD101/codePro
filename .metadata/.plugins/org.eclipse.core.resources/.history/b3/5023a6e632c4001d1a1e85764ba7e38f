package com.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.location.exceptions.LocationServiceException;
import com.location.response.ISSLocation;
import com.location.service.LocationServiceImpl;

@ExtendWith(MockitoExtension.class)
@EnableRetry
class IssLocationApplicationTests {

	@Mock
	private RestTemplate restTemplateMock;

	@Mock
	private LocationServiceImpl locationService;

	private static final String API_URL = "http://api.open-notify.org/iss-now.json";

	/**
	 * This method is executed before each test and is used to set up the LocationServiceImpl instance.
	 * The mock RestTemplate and API URL are injected into the instance.
	 */
	@BeforeEach
	public void setup() {
		locationService = new LocationServiceImpl();
		locationService.rest = restTemplateMock;
		locationService.API_URL = API_URL;
	}

	/**
	 * This test case checks if the getCurrentLocation() method returns the expected location
	 * when a valid response is received from the API.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetCurrentLocation_Success() throws Exception {
		ISSLocation expectedLocation = new ISSLocation();
		expectedLocation.setLatitude(10.0);
		expectedLocation.setLongitude(20.0);
		ResponseEntity<ISSLocation> response = new ResponseEntity<>(expectedLocation, HttpStatus.OK);

		when(restTemplateMock.getForEntity(API_URL, ISSLocation.class)).thenReturn(response);

		ISSLocation actualLocation = locationService.getCurrentLocation();

		assertEquals(expectedLocation, actualLocation);
	}
	
	@Test
	void testGetCurrentLocation_Failure() throws Exception {
		ISSLocation expectedLocation = new ISSLocation();
		expectedLocation.setLatitude(10.0);
		expectedLocation.setLongitude(20.0);
		ResponseEntity<ISSLocation> response = new ResponseEntity<>(expectedLocation, HttpStatus.NOT_FOUND);

		when(restTemplateMock.getForEntity(API_URL, ISSLocation.class)).thenReturn(response);

		//ISSLocation actualLocation = locationService.getCurrentLocation();

		try {
			locationService.getCurrentLocation();
		} catch (LocationServiceException ex) {
			assertEquals("Error retrieving current location of ISS from API", ex.getMessage());
		}
	}

	/**
	 * This test case checks if the getCurrentLocation() method retries and returns the expected location
	 * when an internal server error is received from the API.
	 * 
	 * @throws Exception
	 */
	@Test
	void testGetCurrentLocation_Retry_Success() throws Exception {
		ISSLocation expectedLocation = new ISSLocation();
		expectedLocation.setLatitude(10.0);
		expectedLocation.setLongitude(20.0);
		ResponseEntity<ISSLocation> errorResponse = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		ResponseEntity<ISSLocation> validResponse = new ResponseEntity<>(expectedLocation, HttpStatus.OK);

		when(restTemplateMock.getForEntity(API_URL, ISSLocation.class))
		.thenThrow(new RestClientException("Exception while connecting ISS api"))
		.thenReturn(errorResponse).thenReturn(validResponse).thenReturn(validResponse);

		ISSLocation actualLocation = locationService.getCurrentLocation();

		assertEquals(expectedLocation, actualLocation);
	}

	/**
	 * Test case to verify the behavior of LocationServiceImpl.getCurrentLocation() method when the retry attempts
	 * exceed the maximum attempts limit and it still fails to retrieve the location.
	 * 
	 * @throws Exception
	*/
	@Test
	void testGetCurrentLocation_Retry_Failure() throws Exception {
		ResponseEntity<ISSLocation> response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

		when(restTemplateMock.getForEntity(API_URL, ISSLocation.class))
		.thenThrow(new RestClientException("First exception"))
		.thenReturn(response).thenReturn(response);

		try {
			locationService.getCurrentLocation();
		} catch (LocationServiceException ex) {
			assertEquals("Error retrieving current location of ISS from API", ex.getMessage());
		}
	}

	/**
	 * Test case to verify the behavior of LocationServiceImpl.getCurrentLocation() method when the retry attempts
	 * exceed the maximum attempts limit and it still fails to retrieve the location.
	 * 
	 * @throws Exception
	 */
	@Test
	void testRecover_LastKnownLocation() throws Exception {
		ISSLocation lastKnownLocation = new ISSLocation();
		lastKnownLocation.setLatitude(10.0);
		lastKnownLocation.setLongitude(20.0);
		locationService.lastKnownLocation = lastKnownLocation;

		ISSLocation actualLocation = locationService.recover(new LocationServiceException("Error"));

		assertEquals(lastKnownLocation, actualLocation);
	}

	/**
	 * Test case to verify the behavior of LocationServiceImpl.recover() method when lastKnownLocation is null.
	 * The method should return a default location of (0.0, 0.0).
	 * 
	 * @throws Exception
	*/
	@Test
	void testRecover_DefaultLocation() throws Exception {
		ISSLocation actualLocation = locationService.recover(new LocationServiceException("Error"));

		ISSLocation expectedLocation = new ISSLocation();
		expectedLocation.setLatitude(0.0);
		expectedLocation.setLongitude(0.0);
		assertEquals(expectedLocation.getLatitude(), actualLocation.getLatitude());
		assertEquals(expectedLocation.getLongitude(), actualLocation.getLongitude());
	}

}
