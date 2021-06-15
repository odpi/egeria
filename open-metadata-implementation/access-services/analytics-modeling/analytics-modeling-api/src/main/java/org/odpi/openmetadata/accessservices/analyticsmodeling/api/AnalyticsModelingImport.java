/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.api;


import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;

public interface AnalyticsModelingImport {

	/**
	 * Get databases available on the server for the user.
	 * 
	 * @param userId		of the user.
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return list of databases for the requested server/user.
	 */
	public AnalyticsModelingOMASAPIResponse getDatabases(String userId, Integer startFrom, Integer pageSize);

	/**
	 * Get schema defined by database GUID.
	 * 
	 * @param userId         of the request.
	 * @param databaseGuid	 of the requested database.
     * @param startFrom		 starting element (used in paging through large result sets)
     * @param pageSize		 maximum number of results to return
	 * @return list of schemas for the requested database.
	 */
	public AnalyticsModelingOMASAPIResponse getSchemas(String userId, String databaseGuid,
			Integer startFrom, Integer pageSize);

	/**
	 * Get tables for the schema.
	 * 
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param schema       schema name on the database.
	 * @return list of tables for the requested schema.
	 */
	public AnalyticsModelingOMASAPIResponse getTables(String userId, String databaseGuid, String catalog, String schema) ;

	/**
	 * Build module for the schema.
	 * 
	 * @param userId       of the request.
	 * @param databaseGuid of the requested database.
	 * @param catalog      catalog name of the database.
	 * @param schema       schema name of the database.
	 * @param request      table filter 
	 * @return module for the requested schema.
	 */
	public AnalyticsModelingOMASAPIResponse getModule(String userId, String databaseGuid, String catalog,
			String schema, ModuleTableFilter request);

}
