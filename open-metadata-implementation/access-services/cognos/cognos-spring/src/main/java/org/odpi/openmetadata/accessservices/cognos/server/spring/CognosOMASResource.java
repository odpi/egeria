/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.cognos.responses.CognosOMASAPIResponse;
import org.odpi.openmetadata.accessservices.cognos.server.CognosRestServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/cognos/users/{userId}/")

@Tag(name="Cognos OMAS",
	description="The Cognos OMAS queries RDBMS metadata and creates modules for IBM Cognos Analytics.",
	externalDocs=@ExternalDocumentation(description="Cognos Open Metadata Access Service (OMAS)",
	url="https://egeria.odpi.org/open-metadata-implementation/access-services/cognos/"))


public class CognosOMASResource {

    private final CognosRestServices restAPI = new CognosRestServices();

	/**
	 * Get list of databases.
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
	 * @return Cognos response contains list of databases.
	 */
	@GetMapping(path = "/databases")
	public CognosOMASAPIResponse getDatabases(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId
			)
	{
		return restAPI.getDatabases(serverName, userId);
	}

	/**
	 * Get list of schemas of a given dataSource.
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
	 * @param dataSource data source GUID.
	 * @return Cognos response contains list of database schemas.
	 */
	@GetMapping(path = "/{dataSourceGUID}/schemas")
	public CognosOMASAPIResponse getSchemas(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("dataSourceGUID") String dataSource
			)
	{
		return restAPI.getSchemas(serverName, userId, dataSource);
	}

	/**
	 * Get list of tables of a given dataSource, catalog and schema.
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
	 * @param dataSource data source id.
	 * @param catalog of the db.
	 * @param schema of the db.
	 * @param request body.
	 * @return Cognos response contains list of tables in the database schema.
	 */
	@PostMapping(path = "/{dataSourceGUID}/tables")
	public CognosOMASAPIResponse getTables(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
			@PathVariable("dataSourceGUID") String dataSource,
			@RequestParam(required = false) String catalog,
			@RequestParam(required = true) String schema,
			@RequestBody(required=false) Object request
			) 
	{
		return restAPI.getTables(serverName, userId, dataSource, schema);
	}
	
	/**
	 * Get physical module of a given dataSource, catalog and schema.
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
	 * @param dataSource data source id.
	 * @param catalog of the db.
	 * @param schema of the db.
	 * @param request body.
	 * @return physical module for the database schema.
	 */
	@PostMapping(path = "/{dataSourceGUID}/physicalModule")
	public CognosOMASAPIResponse getPhysicalModule(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
			@PathVariable("dataSourceGUID") String dataSource,
			@RequestParam(required=false) String catalog,
			@RequestParam(required=true) String schema,
			@RequestBody(required=false) Object request
			) {

		return restAPI.getModule(serverName, userId, dataSource, catalog, schema);
	}
	
}
