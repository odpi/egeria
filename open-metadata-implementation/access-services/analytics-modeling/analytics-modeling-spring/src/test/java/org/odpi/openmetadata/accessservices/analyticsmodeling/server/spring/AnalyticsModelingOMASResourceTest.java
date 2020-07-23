/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server.spring;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.odpi.openmetadata.accessservices.analyticsmodeling.server.AnalyticsModelingRestServices;

/**
 * The test class verifies proper use of AnalyticsModelingRestServices to handle requests.
 */
@ExtendWith(MockitoExtension.class)
public class AnalyticsModelingOMASResourceTest {

	private static final String USER = "user";
	private static final String SERVER_NAME = "serverName";
	private static final Integer FROM = 0;
	private static final Integer PAGE_SIZE = 10;
	private static final String GUID = "b1c497ce.60641b50.0v9mgsb1m.9vbkmkr";
	private static final String SCHEMA = "schema_name";
	private static final String CATALOG = "catalog_name";

	@InjectMocks
	private AnalyticsModelingOMASResource resource = new AnalyticsModelingOMASResource();

	@Mock
	private AnalyticsModelingRestServices restAPI;
	
	@Test
	void getDatabases() {
		when(restAPI.getDatabases(SERVER_NAME, USER, FROM, PAGE_SIZE)).thenReturn(null);
		resource.getDatabases(SERVER_NAME, USER, FROM, PAGE_SIZE);
		verify(restAPI, times(1)).getDatabases(SERVER_NAME, USER, FROM, PAGE_SIZE);
	}

	@Test
	void getSchemas() {
		when(restAPI.getSchemas(SERVER_NAME, USER, GUID, FROM, PAGE_SIZE)).thenReturn(null);
		resource.getSchemas(SERVER_NAME, USER, GUID, FROM, PAGE_SIZE);
		verify(restAPI, times(1)).getSchemas(SERVER_NAME, USER, GUID, FROM, PAGE_SIZE);
	}

	@Test
	void getTables() {
		when(restAPI.getTables(SERVER_NAME, USER, GUID, SCHEMA)).thenReturn(null);
		resource.getTables(SERVER_NAME, USER, GUID, null, SCHEMA, null);
		verify(restAPI, times(1)).getTables(SERVER_NAME, USER, GUID, SCHEMA);
	}

	@Test
	void getModule() {
		when(restAPI.getModule(SERVER_NAME, USER, GUID, CATALOG, SCHEMA)).thenReturn(null);
		resource.getPhysicalModule(SERVER_NAME, USER, GUID, CATALOG, SCHEMA, null);
		verify(restAPI, times(1)).getModule(SERVER_NAME, USER, GUID, CATALOG, SCHEMA);
	}

}
