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


public class CognosRestServices {

    CognosInstanceHandler  instanceHandler = new CognosInstanceHandler();


    private static final Logger log = LoggerFactory.getLogger(CognosRestServices.class);

	public CognosOMASAPIResponse getDatabases(String serverName) {

		try {
			DatabasesResponse response = new DatabasesResponse();
			List<ResponseContainerDatabase> databases = instanceHandler.getAssetContextHandler(serverName)
					.getDatabases();
        	response.setDatabasesList(databases);
        	return response;
        }
        catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}
	
	public CognosOMASAPIResponse getSchemas(String serverName, String dataSource) {
        try {
        	SchemasResponse response = new SchemasResponse();
        	List<ResponseContainerDatabaseSchema> databasesSchemas = instanceHandler.getAssetContextHandler(serverName)
						.getDatabaseSchemas(dataSource);
        	response.setSchemaList(databasesSchemas);
        	return response;
        } catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}

	public CognosOMASAPIResponse getTables(String serverName, String databaseGuid, String schema) {
        try {
        	SchemaTablesResponse response = new SchemaTablesResponse();
        	ResponseContainerSchemaTables tables = instanceHandler.getAssetContextHandler(serverName)
        			.getSchemaTables(databaseGuid, schema);
        	response.setTableList(tables);
        	return response;
       } catch (CognosCheckedException e) {
            log.error(e.getMessage(), e);
            return handleErrorResponse(e);
        }
	}

	public CognosOMASAPIResponse getModule(String serverName, String databaseGuid, String catalog, String schema) {
        try {

        	ModuleResponse response = new ModuleResponse();
        	ResponseContainerModule module = instanceHandler.getAssetContextHandler(serverName)
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
