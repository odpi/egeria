/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.server.AnalyticsModelingRestServices;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;

/**
 * The test class verifies proper use of AnalyticsModelingRestServices to handle requests.
 */
@ExtendWith(MockitoExtension.class)
public class AnalyticsModelingOMASResourceTest {

	private static final String REPORT_WRONG_ERROR_DETAILS = "Wrong error details.";
	private static final String REPORT_WRONG_ERROR_MESSAGE = "Wrong error message.";
	private static final String REPORT_WRONG_ERROR_CODE = "Wrong error code.";
	private static final String REPORT_WRONG_HTTP_STATUS = "'Bad request' http status 400 is expected";
	
	private static final String USER = "user";
	private static final String SERVER_NAME = "serverName";
	private static final Integer FROM = 0;
	private static final Integer PAGE_SIZE = 10;
	private static final String GUID = "b1c497ce.60641b50.0v9mgsb1m.9vbkmkr";
	private static final String SCHEMA = "schema_name";
	private static final String CATALOG = "catalog_name";
	private static final String DATABASE_GUID = "databaseGUID";


	@InjectMocks
	private AnalyticsModelingOMASResource resource = new AnalyticsModelingOMASResource();

	@Mock(answer = Answers.RETURNS_DEEP_STUBS)
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
		when(restAPI.getModule(SERVER_NAME, USER, GUID, CATALOG, SCHEMA, null)).thenReturn(null);
		resource.getPhysicalModule(SERVER_NAME, USER, GUID, CATALOG, SCHEMA, null);
		verify(restAPI, times(1)).getModule(SERVER_NAME, USER, GUID, CATALOG, SCHEMA, null);
	}
	
	@Test
	void getModuleInvalidGUIDParameter() throws AnalyticsModelingCheckedException, InvalidParameterException {
		InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
		when(restAPI.getModule(SERVER_NAME, USER, null, CATALOG, SCHEMA, null)).thenCallRealMethod();
		when(restAPI.getInvalidParameterHandler()).thenReturn(invalidParameterHandler);
		doCallRealMethod().when(restAPI).validateUrlParameters(SERVER_NAME, USER, null, DATABASE_GUID, null, null, "getModule");
		
		AnalyticsModelingOMASAPIResponse response =	resource.getPhysicalModule(SERVER_NAME, USER, null, CATALOG, SCHEMA, null);
		
		assertTrue(response instanceof ErrorResponse);
		ErrorResponse errResponse = (ErrorResponse)response;
		assertEquals( 400, errResponse.getRelatedHTTPCode(), REPORT_WRONG_HTTP_STATUS);
		assertEquals( AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition().getMessageId(),
				errResponse.getErrorCode(), REPORT_WRONG_ERROR_CODE);
		assertEquals( "OMAS-ANALYTICS-MODELING-015 The request parameter databaseGUID has invalid value.",
				errResponse.getMessage(), REPORT_WRONG_ERROR_MESSAGE);
		assertEquals( "OMAG-COMMON-400-005 The unique identifier (guid) passed on the databaseGUID parameter of the getModule operation is null",
				errResponse.getExceptionCause(), REPORT_WRONG_ERROR_DETAILS);
		
		verify(restAPI, times(1)).validateUrlParameters(SERVER_NAME, USER, null, DATABASE_GUID, null, null, "getModule");
	}
	
	@Test
	void getDatabasesInvalidPageParameter() throws AnalyticsModelingCheckedException, InvalidParameterException {
		InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
		when(restAPI.getDatabases(anyString(), anyString(), anyInt(), anyInt())).thenCallRealMethod();
		when(restAPI.getInvalidParameterHandler()).thenReturn(invalidParameterHandler);
		doCallRealMethod().when(restAPI).validateUrlParameters(anyString(), anyString(), eq(null), eq(null), anyInt(), anyInt(), eq("getDatabases"));
		when(restAPI.getInvalidParameterHandler()).thenReturn(invalidParameterHandler);
		
		AnalyticsModelingOMASAPIResponse response = resource.getDatabases(SERVER_NAME, USER, -1, PAGE_SIZE);
		
		assertTrue(response instanceof ErrorResponse);
		ErrorResponse errResponse = (ErrorResponse)response;
		assertEquals( 400, errResponse.getRelatedHTTPCode(), REPORT_WRONG_HTTP_STATUS);
		assertEquals( AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition().getMessageId(),
				errResponse.getErrorCode(), REPORT_WRONG_ERROR_CODE);
		assertEquals( "OMAS-ANALYTICS-MODELING-015 The request parameter startFrom has invalid value.",
				errResponse.getMessage(), REPORT_WRONG_ERROR_MESSAGE);
		assertEquals( "OMAG-COMMON-400-008 The starting point for the results -1, passed on the startFrom parameter of the getDatabases operation, is negative",
				errResponse.getExceptionCause(), REPORT_WRONG_ERROR_DETAILS);
		
		response = resource.getDatabases(SERVER_NAME, USER, FROM, -1);
		
		assertTrue(response instanceof ErrorResponse);
		errResponse = (ErrorResponse)response;
		assertEquals( 400, errResponse.getRelatedHTTPCode(), REPORT_WRONG_HTTP_STATUS);
		assertEquals( AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition().getMessageId(),
				errResponse.getErrorCode(), REPORT_WRONG_ERROR_CODE);
		assertEquals( "OMAS-ANALYTICS-MODELING-015 The request parameter pageSize has invalid value.",
				errResponse.getMessage(), REPORT_WRONG_ERROR_MESSAGE);
		assertEquals( "OMAG-COMMON-400-009 The page size -1 for the results, passed on the pageSize parameter of the getDatabases operation, is negative",
				errResponse.getExceptionCause(), REPORT_WRONG_ERROR_DETAILS);
		
		verify(restAPI, times(2)).validateUrlParameters(eq(SERVER_NAME), eq(USER), eq(null), eq(null), anyInt(), anyInt(), eq("getDatabases"));
	}

	@Test
	void getSchemasInvalidUserIdParameter() throws AnalyticsModelingCheckedException, InvalidParameterException {
		InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
		when(restAPI.getSchemas(SERVER_NAME, null, GUID, FROM, PAGE_SIZE)).thenCallRealMethod();
		when(restAPI.getInvalidParameterHandler()).thenReturn(invalidParameterHandler);
		doCallRealMethod().when(restAPI).validateUrlParameters(SERVER_NAME, null, GUID, DATABASE_GUID, FROM, PAGE_SIZE, "getSchemas");
		
		AnalyticsModelingOMASAPIResponse response =	resource.getSchemas(SERVER_NAME, null, GUID, FROM, PAGE_SIZE);
		
		assertTrue(response instanceof ErrorResponse);
		ErrorResponse errResponse = (ErrorResponse)response;
		assertEquals( 400, errResponse.getRelatedHTTPCode(), REPORT_WRONG_HTTP_STATUS);
		assertEquals( AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition().getMessageId(),
				errResponse.getErrorCode(), REPORT_WRONG_ERROR_CODE);
		assertEquals( "OMAS-ANALYTICS-MODELING-015 The request parameter userId has invalid value.",
				errResponse.getMessage(), REPORT_WRONG_ERROR_MESSAGE);
		assertEquals( "OMAG-COMMON-400-004 The user identifier (user id) passed on the getSchemas operation is null",
				errResponse.getExceptionCause(), REPORT_WRONG_ERROR_DETAILS);
		
		verify(restAPI, times(1)).validateUrlParameters(SERVER_NAME, null, GUID, DATABASE_GUID, FROM, PAGE_SIZE, "getSchemas");
	}
	
	@Test
	void getTablesInvalidServerParameter() throws AnalyticsModelingCheckedException, InvalidParameterException {
		InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
		when(restAPI.getTables(null, USER, GUID, SCHEMA)).thenCallRealMethod();
		when(restAPI.getInvalidParameterHandler()).thenReturn(invalidParameterHandler);
		doCallRealMethod().when(restAPI).validateUrlParameters(null, USER, GUID, DATABASE_GUID, null, null, "getTables");
		
		AnalyticsModelingOMASAPIResponse response =	resource.getTables(null, USER, GUID, CATALOG, SCHEMA, null);
		
		assertTrue(response instanceof ErrorResponse);
		ErrorResponse errResponse = (ErrorResponse)response;
		assertEquals( 400, errResponse.getRelatedHTTPCode(), REPORT_WRONG_HTTP_STATUS);
		assertEquals( AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition().getMessageId(),
				errResponse.getErrorCode(), REPORT_WRONG_ERROR_CODE);
		assertEquals( "OMAS-ANALYTICS-MODELING-015 The request parameter serverName has invalid value.",
				errResponse.getMessage(), REPORT_WRONG_ERROR_MESSAGE);
		assertEquals( "OMAG-COMMON-400-006 The name passed on the serverName parameter of the getTables operation is null",
				errResponse.getExceptionCause(), REPORT_WRONG_ERROR_DETAILS);
		
		verify(restAPI, times(1)).validateUrlParameters(null, USER, GUID, DATABASE_GUID, null, null, "getTables");
	}

}
