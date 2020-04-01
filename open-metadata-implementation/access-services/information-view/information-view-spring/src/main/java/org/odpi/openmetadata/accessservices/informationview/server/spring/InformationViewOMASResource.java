/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.RegistrationRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.responses.InformationViewOMASAPIResponse;
import org.odpi.openmetadata.accessservices.informationview.server.InformationViewRestServices;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/")

@Tag(name="Information View OMAS", description="The Information View OMAS configures and manages metadata for data tools that create virtual views over data - such as business intelligence tools and data virtualization platforms." +
        "\n", externalDocs=@ExternalDocumentation(description="Information View Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/information-view/"))

@Validated
public class InformationViewOMASResource {

    private final InformationViewRestServices restAPI = new InformationViewRestServices();

    public InformationViewOMASResource() {

    }

    /**
     * Save the report metadata representation
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return Success or Failure response describing the error based on status of the operation
     */
    @PostMapping(path = "/report")
    public InformationViewOMASAPIResponse submitReport(@PathVariable("serverName") String serverName,
                                                       @PathVariable("userId") String userId,
                                                       @Valid @RequestBody ReportRequestBody requestBody) {
        return restAPI.submitReport(serverName, userId, requestBody);
    }


    /**
     * Create the metadata for the data view
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return Success or Failure response describing the error based on status of the operation
     */
    @PostMapping(path = "/dataview")
    public InformationViewOMASAPIResponse submitDataView(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @RequestBody @Valid DataViewRequestBody requestBody) {
        return restAPI.submitDataView(serverName, userId, requestBody);
    }


    /**
     * Returns the list of databases
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @return list of databases
     */
    @GetMapping(path = "/databases")
    public InformationViewOMASAPIResponse retrieveDatabases(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @PositiveOrZero(message = "startFrom should be a positive number")
                                                            @RequestParam Integer startFrom,
                                                            @PositiveOrZero(message = "pageSize should be a positive number")
                                                            @RequestParam Integer pageSize) {
        return restAPI.getDatabases(serverName, userId, startFrom, pageSize);
    }

    /**
     *
     * Retrieve list of tables for a database
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param databaseGuid guid of database for which we want to retrieve the list of tables
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @return list of tables belonging to the database
     */
    @GetMapping(path = "databases/{database}/tables")
    public InformationViewOMASAPIResponse retrieveTablesForDatabase(@PathVariable("serverName") String serverName,
                                                                    @PathVariable("userId") String userId,
                                                                    @NotBlank(message = "guid of the database should not be blank")
                                                                    @PathVariable("database") String databaseGuid,
                                                                    @PositiveOrZero(message = "startFrom should be a positive number")
                                                                    @RequestParam int startFrom,
                                                                    @PositiveOrZero(message = "pageSize should be a positive number")
                                                                    @RequestParam int pageSize) {
        return restAPI.getTablesForDatabase(serverName, userId, databaseGuid, startFrom, pageSize);
    }

    /**
     *
     * Retrieve the full context of a table
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param tableGuid   guid of the table entity
     * @return the full context of the table, including host, database, schema, table name and list of columns with business terms assigned
     */
    @GetMapping(path = "tables/{table}")
    public InformationViewOMASAPIResponse retrieveTableContext(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @NotBlank(message = "guid of the table should not be blank")
                                                               @PathVariable("table") String tableGuid) {
        return restAPI.getTableContext(serverName, userId, tableGuid);
    }

    /**
     * Retrieve list of columns for a table using pagination
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param tableGuid guid of the table
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @return list of columns belonging to the table
     */
    @GetMapping(path = "tables/{table}/columns")
    public InformationViewOMASAPIResponse retrieveTableColumns(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @NotBlank(message = "guid of the table should not be blank")
                                                               @PathVariable("table") String tableGuid,
                                                               @PositiveOrZero(message = "startFrom should be a positive number")
                                                               @RequestParam Integer startFrom,
                                                               @PositiveOrZero(message = "pageSize should be a positive number")
                                                               @RequestParam Integer pageSize) {
        return restAPI.getTableColumns(serverName, userId, tableGuid, startFrom, pageSize);
    }

    /**
     * Create a SoftwareServerCapability entity containing the properties submitted in the request and describing the external tool
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody payload containing the registration details to create
     * @return response containing guid of SoftwareServerCapability entity assigned to the external tool
     */
    @PostMapping(path = "register")
    public InformationViewOMASAPIResponse registerExternalTool(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @Valid @RequestBody RegistrationRequestBody requestBody) {
        return restAPI.registerExternalTool(serverName, userId, requestBody);
    }

    /**
     * Return the SoftwareServerCapability entity for the external tool described in the payload
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody payload containing the registration details to lookup
     * @return response containing properties of the external tool used at registration step
     */
    @PostMapping(path = "registration/lookup")
    public InformationViewOMASAPIResponse lookupExternalTool(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @Valid @RequestBody RegistrationRequestBody requestBody) {
        return restAPI.lookupRegistration(serverName, userId, requestBody);
    }

    /**
     * Retrieve the report metadata representation for the report with the specified report id
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param reportId - id of the report to retrieve
     * @return response containing the report metadata representation or the error in case of failure to retrieve it
     */
    @GetMapping(path = "/report")
    public InformationViewOMASAPIResponse retrieveReport(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @NotBlank(message = "report id should not be blank")
                                                         @RequestParam String reportId) {
        return restAPI.retrieveReport(serverName, userId, reportId);
    }

    /**
     * Retrieve the metadata representation of the data view with the specified data view id
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param dataViewId id of the data view
     * @return response containing the data view metadata representation or the error in case of failure to retrieve it
     */
    @GetMapping(path = "/dataview")
    public InformationViewOMASAPIResponse retrieveDataView(@PathVariable("serverName") String serverName,
                                                           @PathVariable("userId") String userId,
                                                           @NotBlank(message = "data view id should not be blank")
                                                           @RequestParam String dataViewId) {
        return restAPI.retrieveDataView(serverName, userId, dataViewId);
    }

}
