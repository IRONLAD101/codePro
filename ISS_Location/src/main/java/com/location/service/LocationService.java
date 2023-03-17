package com.location.service;

import com.location.exceptions.LocationServiceException;
import com.location.response.ISSLocation;

public interface LocationService {

	ISSLocation getCurrentLocation() throws LocationServiceException;

}
