/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server;

import java.util.List;

import org.odpi.openmetadata.accessservices.cognos.ffdc.exceptions.CognosCheckedException;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabase;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerDatabaseSchema;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerModule;
import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerSchemaTables;
import org.odpi.openmetadata.accessservices.cognos.responses.CognosOMASAPIResponse;
import org.odpi.openmetadata.accessservices.cognos.responses.DatabasesResponse;
import org.odpi.openmetadata.accessservices.cognos.responses.ErrorResponse;
import org.odpi.openmetadata.accessservices.cognos.responses.ModuleResponse;
import org.odpi.openmetadata.accessservices.cognos.responses.SchemaTablesResponse;
import org.odpi.openmetadata.accessservices.cognos.responses.SchemasResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Server-side implementation of the Cognos OMAS interface for modeling.
 */
public class CognosRestServices {

    CognosInstanceHandler  instanceHandler = new CognosInstanceHandler();


    private static final Logger log = LoggerFactory.getLogger(CognosRestServices.class);

	/**
	 * Get databases available on the server for the user.
	 * @param serverName		of the server.
	 * @param userId			of the user.
	 * @return list of databases for the requested server/user.
	 */
	public CognosOMASAPIResponse getDatabases(String serverName, String userId) {

		try {
			DatabasesResponse response = new DatabasesResponse();
			List<ResponseContainerDatabase> databases = instanceHandler.getDatabaseContextHandler(serverName, userId, "getDatabases")
					.getDatabases();
        	response.setDatabasesList(databases);
        	return response;
        }
        catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}
	
	/**
	 * Get schema defined by data source GUID.
	 * @param serverName		of the request.
	 * @param userId			of the request.
	 * @param dataSourceGuid 	of the requested database.
	 * @return list of schemas for the requested database.
	 */
	public CognosOMASAPIResponse getSchemas(String serverName, String userId, String dataSourceGuid) {
        try {
        	SchemasResponse response = new SchemasResponse();
        	List<ResponseContainerDatabaseSchema> databasesSchemas = instanceHandler.getDatabaseContextHandler(serverName, userId, "getSchemas")
						.getDatabaseSchemas(dataSourceGuid);
        	response.setSchemaList(databasesSchemas);
        	return response;
        } catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}

	/**
	 * Get tables for the schema.
	 * @param serverName	of the request.
	 * @param userId		of the request.
	 * @param databaseGuid 	of the requested database.
	 * @param schema	 	schema name on the database.
	 * @return list of tables for the requested schema.
	 */
	public CognosOMASAPIResponse getTables(String serverName, String userId, String databaseGuid, String schema) {
        try {
        	SchemaTablesResponse response = new SchemaTablesResponse();
        	ResponseContainerSchemaTables tables = instanceHandler.getDatabaseContextHandler(serverName, userId, "getTables")
        			.getSchemaTables(databaseGuid, schema);
        	response.setTableList(tables);
        	return response;
       } catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}

	/**
	 * Build module for the schema.
	 * @param serverName	of the request.
	 * @param userId		of the request.
	 * @param databaseGuid 	of the requested database.
	 * @param catalog		catalog name of the database.
	 * @param schema		schema name of the database.
	 * @return module for the requested schema.
	 */
	public CognosOMASAPIResponse getModule(String serverName, String userId, String databaseGuid, String catalog, String schema) {
        try {

        	ModuleResponse response = new ModuleResponse();
        	ResponseContainerModule module = instanceHandler.getDatabaseContextHandler(serverName, userId, "getModule")
        			.getModule(databaseGuid, catalog, schema);
        	response.setModule(module);
        	return response;
        }
        catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}

    private CognosOMASAPIResponse handleErrorResponse(CognosCheckedException e) {
    	return new ErrorResponse(e);
    }
}
