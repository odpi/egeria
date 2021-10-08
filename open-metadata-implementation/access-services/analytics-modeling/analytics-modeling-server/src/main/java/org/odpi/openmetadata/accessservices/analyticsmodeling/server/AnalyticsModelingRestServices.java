/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AssetsResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.DatabasesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.ModuleResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemaTablesResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.SchemasResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.AnalyticsArtifactHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;

/**
 * Server-side implementation of the Analytics Modeling OMAS interface for modeling.
 */
public class AnalyticsModelingRestServices {

	private static final String DATABASE_GUID = "databaseGUID";
	private AnalyticsModelingInstanceHandler instanceHandler = new AnalyticsModelingInstanceHandler();
	private InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
	
	private static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(AnalyticsModelingRestServices.class),
			AccessServiceDescription.ANALYTICS_MODELING_OMAS.getAccessServiceFullName());
	
	private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
	
	public RESTExceptionHandler getExceptionHandler() {
		return restExceptionHandler;
	}

	public AnalyticsModelingInstanceHandler getHandler() {
		return instanceHandler;
	}
	
	public InvalidParameterHandler getInvalidParameterHandler() {
		return invalidParameterHandler;
	}

	/**
	 * Get databases available on the server for the user.
	 * 
	 * @param serverName	of the server.
	 * @param userId		of the user.
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return list of databases for the requested server/user.
	 */
	public AnalyticsModelingOMASAPIResponse getDatabases(String serverName, String userId, Integer startFrom, Integer pageSize) {

		String methodName = "getDatabases";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {
			validateUrlParameters(serverName, userId, null, null, startFrom, pageSize, methodName);
			
			DatabasesResponse response = new DatabasesResponse();
			DatabaseContextHandler handler = getHandler().getDatabaseContextHandler(serverName, userId, methodName);
			List<ResponseContainerDatabase> databases = handler.getDatabases(userId, startFrom, pageSize);
			response.setDatabasesList(databases);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Get schema defined by database GUID.
	 * 
	 * @param serverName     of the request.
	 * @param userId         of the request.
	 * @param databaseGuid	 of the requested database.
     * @param startFrom		 starting element (used in paging through large result sets)
     * @param pageSize		 maximum number of results to return
	 * @return list of schemas for the requested database.
	 */
	public AnalyticsModelingOMASAPIResponse getSchemas(String serverName, String userId, String databaseGuid,
			Integer startFrom, Integer pageSize) {

		String methodName = "getSchemas";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {
			
			validateUrlParameters(serverName, userId, databaseGuid, DATABASE_GUID, startFrom, pageSize, methodName);

			SchemasResponse response = new SchemasResponse();
			DatabaseContextHandler handler = getHandler().getDatabaseContextHandler(serverName, userId, methodName);
			List<ResponseContainerDatabaseSchema> databasesSchemas = handler.getDatabaseSchemas(userId, databaseGuid, startFrom, pageSize);
			response.setSchemaList(databasesSchemas);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}

		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Get tables for the schema.
	 * 
	 * @param serverName   of the request.
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param schema       schema name on the database.
	 * @return list of tables for the requested schema.
	 */
	public AnalyticsModelingOMASAPIResponse getTables(String serverName, String userId, String databaseGuid, String schema) {

		String methodName = "getTables";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {
			validateUrlParameters(serverName, userId, databaseGuid, DATABASE_GUID, null, null, methodName);

			SchemaTablesResponse response = new SchemaTablesResponse();
			DatabaseContextHandler handler = getHandler().getDatabaseContextHandler(serverName, userId, methodName);
			ResponseContainerSchemaTables tables = handler.getSchemaTables(userId, databaseGuid, schema);
			response.setTableList(tables);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}
		
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Build module for the schema.
	 * 
	 * @param serverName   of the request.
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      catalog name of the database.
	 * @param schema       schema name of the database.
	 * @param request      table filter 
	 * @return module for the requested schema.
	 */
	public AnalyticsModelingOMASAPIResponse getModule(String serverName, String userId, String databaseGuid, String catalog,
			String schema, ModuleTableFilter request) {

		String methodName = "getModule";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {

			validateUrlParameters(serverName, userId, databaseGuid, DATABASE_GUID, null, null, methodName);

			ModuleResponse response = new ModuleResponse();
			DatabaseContextHandler handler = getHandler().getDatabaseContextHandler(serverName, userId, methodName);
			ResponseContainerModule module = handler.getModule(userId, databaseGuid, catalog, schema, request);
			response.setModule(module);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}
		
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}
	
	/**
	 * Create analytics artifact defined as json input.
	 * @param serverName where to create artifact.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param serverCapabilityGUID source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 */
	public AnalyticsModelingOMASAPIResponse createArtifact(String serverName, String userId, 
			String serverCapability, String serverCapabilityGUID, AnalyticsAsset artifact) 
	{

		String methodName = "createArtifact";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {

			validateUrlParameters(serverName, userId, null, null, null, null, methodName);

			AssetsResponse response = new AssetsResponse();
			AnalyticsArtifactHandler handler = getHandler().getAnalyticsArtifactHandler(serverName, userId, methodName);
			ResponseContainerAssets assets = handler.createAssets(userId, serverCapability, serverCapabilityGUID, artifact);
			response.setAssetList(assets);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}
		
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	/**
	 * Update analytics artifact defined as json input.
	 * @param serverName where to create artifact.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param serverCapabilityGUID source where artifact persist.
	 * @param artifact definition.
	 * @return response with artifact or error description.
	 */
	public AnalyticsModelingOMASAPIResponse updateArtifact(String serverName, String userId,
			String serverCapability, String serverCapabilityGUID, AnalyticsAsset artifact) 
	{
		String methodName = "updateArtifact";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {

			validateUrlParameters(serverName, userId, null, null, null, null, methodName);

			AssetsResponse response = new AssetsResponse();
			AnalyticsArtifactHandler handler = getHandler().getAnalyticsArtifactHandler(serverName, userId, methodName);
			ResponseContainerAssets assets = handler.updateAssets(userId, serverCapability, serverCapabilityGUID, artifact);
			response.setAssetList(assets);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}
		
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}
	
	/**
	 * Delete analytics artifact defined by unique identifier.
	 * @param serverName where to create artifact.
	 * @param userId requested the operation.
	 * @param serverCapability source where artifact persist.
	 * @param serverCapabilityGUID source where artifact persist.
	 * @param identifier of the artifact in the 3rd party system.
	 * @return response with status of the operation.
	 */
	public AnalyticsModelingOMASAPIResponse deleteArtifact(String serverName, String userId,
			String serverCapability, String serverCapabilityGUID, String identifier)
	{
		String methodName = "deleteArtifact";
		AnalyticsModelingOMASAPIResponse ret;
		RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

		try {

			validateUrlParameters(serverName, userId, null, null, null, null, methodName);

			AssetsResponse response = new AssetsResponse();
			AnalyticsArtifactHandler handler = getHandler().getAnalyticsArtifactHandler(serverName, userId, methodName);
			ResponseContainerAssets assets = handler.deleteAssets(userId, serverCapability, serverCapabilityGUID, identifier);
			response.setAssetList(assets);
			response.setMeta(handler.getMessages());
			ret = response;
		} catch (Exception e) {
			ret = handleErrorResponse(e, methodName);
		}
		
		restCallLogger.logRESTCallReturn(token, ret.toString());
		return ret;
	}

	
	/**
	 * Validate path and query parameters from URL.
	 * @param serverName mandatory path parameter of the base URL.
	 * @param userId mandatory path parameter of the base URL.
	 * @param guid optional path parameter.
	 * @param guidParamName name of GUID parameter in URL.
	 * @param startFrom optional query parameter.
	 * @param pageSize optional query parameter.
	 * @param methodName for message.
	 * @throws InvalidParameterException if validation failed.
	 * 
	 * To validate guid the guidParamName must not be null. 
	 */
	public void validateUrlParameters(String serverName, String userId, String guid, String guidParamName, 
			Integer startFrom, Integer pageSize, String methodName) throws InvalidParameterException {
		
		getInvalidParameterHandler().validateUserId(userId, methodName);
		getInvalidParameterHandler().validateName(serverName, "serverName", methodName);
		
		if (guidParamName != null) {
			getInvalidParameterHandler().validateGUID(guid, guidParamName, methodName);
		}
		
		if (startFrom != null && pageSize != null) {
			getInvalidParameterHandler().validatePaging(startFrom, pageSize, methodName);
		}
	}

	/**
	 * Handle error in processing: build error response, and log with standard handler.
	 * @param exception being thrown
	 * @param methodName context
	 * @return response with error definition.
	 */
	private AnalyticsModelingOMASAPIResponse handleErrorResponse(Exception exception, String methodName) {
		AnalyticsModelingCheckedException error = createAnalyticsException(exception, methodName);
		AnalyticsModelingOMASAPIResponse ret = new ErrorResponse(error);
		getExceptionHandler().captureExceptions(ret, exception, methodName);
		return ret;
	}
	
	private AnalyticsModelingCheckedException createAnalyticsException(Exception error, String methodName)	{
		
		if (error instanceof AnalyticsModelingCheckedException) {
			return (AnalyticsModelingCheckedException)error;
		}
        else if (error instanceof UserNotAuthorizedException)
        {
        	UserNotAuthorizedException e = (UserNotAuthorizedException)error;
        	
    		return new AnalyticsModelingCheckedException(
    				AnalyticsModelingErrorCode.UNAUTHORIZED_USER.getMessageDefinition(e.getUserId(), e.getReportingActionDescription()),
    				this.getClass().getSimpleName(),
    				methodName,
    				error);
        }
        else if (error instanceof InvalidParameterException)
        {
        	InvalidParameterException e = (InvalidParameterException)error;
        	
    		return new AnalyticsModelingCheckedException(
    				AnalyticsModelingErrorCode.INVALID_REQUEST_PARAMER.getMessageDefinition(e.getParameterName()),
    				this.getClass().getSimpleName(),
    				methodName,
    				error);
        }
        else
        {
            String message = error.getLocalizedMessage();

            if (message == null)
            {
                message = "null";
            }

            ExceptionMessageDefinition messageDefinition = 
            		AnalyticsModelingErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                            methodName,
                            message);

            return new AnalyticsModelingCheckedException(
            		messageDefinition,
    				this.getClass().getSimpleName(),
    				methodName,
    				error);
        }
	}

}
