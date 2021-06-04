/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.validation.constraints.PositiveOrZero;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ModuleTableFilter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.responses.AnalyticsModelingOMASAPIResponse;
import org.odpi.openmetadata.accessservices.analyticsmodeling.server.AnalyticsModelingRestServices;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/analytics-modeling/users/{userId}/")

@Tag(name="Analytics Modeling OMAS",
	description="The Analytics Modeling OMAS queries RDBMS metadata from repository and creates module for database schema.",
	externalDocs=@ExternalDocumentation(description="Analytics Modeling Open Metadata Access Service (OMAS)",
	url="https://egeria.odpi.org/open-metadata-implementation/access-services/analytics-modeling/"))


public class AnalyticsModelingOMASResource {

    private AnalyticsModelingRestServices restAPI = new AnalyticsModelingRestServices();

	/**
	 * Get list of databases.
     * @param serverName	unique identifier for requested server.
     * @param userId		the unique identifier for the user
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return Analytics Modeling response contains list of databases.
	 */
    @Operation(summary = "Get list of databases")
	@GetMapping(path = "/databases")
	public AnalyticsModelingOMASAPIResponse getDatabases(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PositiveOrZero(message = "startFrom should be a positive number")
            @RequestParam Integer startFrom,
            @PositiveOrZero(message = "pageSize should be a positive number")
            @RequestParam Integer pageSize
            )
	{
		return restAPI.getDatabases(serverName, userId, startFrom, pageSize);
	}

	/**
	 * Get list of schemas of a given database.
     * @param serverName	unique identifier for requested server.
     * @param userId		the unique identifier for the user
	 * @param database		database GUID.
     * @param startFrom		starting element (used in paging through large result sets)
     * @param pageSize		maximum number of results to return
	 * @return Analytics Modeling response contains list of database schemas.
	 */
    @Operation(summary = "Get list of schemas of a given database")
	@GetMapping(path = "/{databaseGUID}/schemas")
	public AnalyticsModelingOMASAPIResponse getSchemas(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
            @PathVariable("databaseGUID") String database,
            @PositiveOrZero(message = "startFrom should be a positive number")
            @RequestParam Integer startFrom,
            @PositiveOrZero(message = "pageSize should be a positive number")
            @RequestParam Integer pageSize
			)
	{
		return restAPI.getSchemas(serverName, userId, database, startFrom, pageSize);
	}

	/**
	 * Get list of tables of a given database, catalog and schema.
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
	 * @param database	  data source id.
	 * @param catalog	  of the db.
	 * @param schema	  of the db.
	 * @param request body.
	 * @return Analytics Modeling response contains list of tables in the database schema.
	 */
    @Operation(summary = "Get list of tables of a given database, catalog and schema")
	@PostMapping(path = "/{databaseGUID}/tables")
	public AnalyticsModelingOMASAPIResponse getTables(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
			@PathVariable("databaseGUID") String database,
			@RequestParam(required = false) String catalog,
			@RequestParam(required = true) String schema,
			@RequestBody(required=false) Object request
			) 
	{
		return restAPI.getTables(serverName, userId, database, schema);
	}
	
	/**
	 * Get physical module of a given database, catalog and schema.
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
	 * @param database	  database guid.
	 * @param catalog	  of the db.
	 * @param schema	  of the db.
	 * @param request body.
	 * @return Analytics Modeling module for the database schema.
	 */
    @Operation(summary = "Get physical module of a given database, catalog and schema")
	@PostMapping(path = "/{databaseGUID}/physicalModule")
	public AnalyticsModelingOMASAPIResponse getPhysicalModule(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
			@PathVariable("databaseGUID") String database,
			@RequestParam(required=false) String catalog,
			@RequestParam(required=true) String schema,
			@RequestBody(required=false) ModuleTableFilter request
			) {

		return restAPI.getModule(serverName, userId, database, catalog, schema, request);
	}

    
	/**
	 * Create assets in repository defined by artifact (body of the request).
     * @param serverName  unique identifier for requested server.
     * @param userId      request user
	 * @param serverCapability where the artifact is stored.
	 * @param artifact definition json.
	 * @return errors or list of created assets.
	 */
    @Operation(summary = "Create assets representing analytics artifact.")
	@PostMapping(path = "/sync")
	public AnalyticsModelingOMASAPIResponse createArtifact(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
			@RequestParam(required=true) String serverCapability,
			@RequestBody(required=true) String artifact
			) {

		return restAPI.createArtifact(serverName, userId, serverCapability, artifact);
	}

    /**
	 * Update assets in repository defined by artifact (body of the request).
     * @param serverName  unique identifier for requested server.
     * @param userId      request user
	 * @param serverCapability where the artifact is stored.
	 * @param artifact from json definition.
	 * @return errors or list of created assets.
	 */
    @Operation(summary = "Update assets representing analytics artifact.")
	@PutMapping(path = "/sync")
	public AnalyticsModelingOMASAPIResponse updateArtifact(
			@PathVariable("serverName") String serverName,
            @PathVariable("userId") String userId,
			@RequestParam(required=true) String serverCapability,
			@RequestBody(required=true) AnalyticsAsset artifact
			) {

		return restAPI.updateArtifact(serverName, userId, serverCapability, artifact);
	}
}
