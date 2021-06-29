/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.api;


import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

public interface AnalyticsModelingImport {

	/**
	 * Get databases available on the server for the user.
	 * 
	 * @param userId		of the user.
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return list of databases for the requested server/user.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public List<ResponseContainerDatabase> getDatabases(String userId, int startFrom, int pageSize)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

	/**
	 * Get schema defined by database GUID.
	 * 
	 * @param userId         of the request.
	 * @param databaseGuid	 of the requested database.
     * @param startFrom		 starting element (used in paging through large result sets)
     * @param pageSize		 maximum number of results to return
	 * @return list of schemas for the requested database.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public List<ResponseContainerDatabaseSchema> getSchemas(String userId, String databaseGuid, int startFrom, int pageSize)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

	/**
	 * Get tables for the schema.
	 * 
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      catalog name of the database.
	 * @param schema       schema name on the database.
	 * @return list of tables for the requested schema.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerSchemaTables getTables(String userId, String databaseGuid, String catalog, String schema)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

	/**
	 * Build module for the schema.
	 * 
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      catalog name of the database.
	 * @param schema       schema name of the database.
	 * @param request      table filter 
	 * @return module for the requested schema.
	 * @throws AnalyticsModelingCheckedException error executing request.
	 * @throws PropertyServerException in case REST call failed.
	 * @throws UserNotAuthorizedException in case user unauthorized to perform operation. 
	 * @throws InvalidParameterException in case any passed parameter is invalid.
	 */
	public ResponseContainerModule getModule(String userId, String databaseGuid, String catalog, String schema, ModuleTableFilter request)
			throws AnalyticsModelingCheckedException, PropertyServerException, InvalidParameterException, UserNotAuthorizedException;

}
