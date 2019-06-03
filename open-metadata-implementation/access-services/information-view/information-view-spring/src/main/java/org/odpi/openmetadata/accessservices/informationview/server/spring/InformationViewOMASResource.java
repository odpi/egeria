/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server.spring;


import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.RegistrationRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.responses.InformationViewOMASAPIResponse;
import org.odpi.openmetadata.accessservices.informationview.server.InformationViewRestServices;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/information-view/users/{userId}/")
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
                                                       @RequestBody ReportRequestBody requestBody) {
        return restAPI.submitReport(serverName, userId, requestBody);
    }


    /**
     *
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return Success or Failure response describing the error based on status of the operation
     */
    @PostMapping(path = "/dataview")
    public InformationViewOMASAPIResponse submitDataView(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @RequestBody DataViewRequestBody requestBody) {
        return restAPI.submitDataView(serverName, userId, requestBody);
    }


    /**
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @return response containing the list of databases or the error in case of failure
     */
    @GetMapping(path = "/databases")
    public InformationViewOMASAPIResponse retrieveDatabases(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @RequestParam int startFrom,
                                                            @RequestParam int pageSize) {
        return restAPI.getDatabases(serverName, userId, startFrom, pageSize);
    }

    /**
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param database guid of the database for which to retrieve the list of tables
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @return response describing the list of tables belonging to the database or the error in case of failure
     */
    @GetMapping(path = "database/{database}/tables")
    public InformationViewOMASAPIResponse retrieveTablesForDatabase(@PathVariable("serverName") String serverName,
                                                                    @PathVariable("userId") String userId,
                                                                    @PathVariable("database") String database,
                                                                    @RequestParam int startFrom,
                                                                    @RequestParam int pageSize) {
        return restAPI.getTablesForDatabase(serverName, userId, database, startFrom, pageSize);
    }

    /**
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param table guid of the table for which we want to retrieve the full context
     * @return the full context of the table, including: host details, connection details, database, schema and table details and list of columns with business terms associated
     */
    @GetMapping(path = "table/{table}")
    public InformationViewOMASAPIResponse retrieveTableContext(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @PathVariable("table") String table) {
        return restAPI.getTableContext(serverName, userId, table);
    }

    /**
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param table guid of the table for which we want to retrieve list of columns
     * @param startFrom starting element (used in paging through large result sets)
     * @param pageSize maximum number of results to return
     * @return the list of columns of the table
     */
    @GetMapping(path = "table/{table}/columns")
    public InformationViewOMASAPIResponse retrieveTableColumns(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @PathVariable("table") String table,
                                                               @RequestParam int startFrom,
                                                               @RequestParam int pageSize) {
        return restAPI.getTableColumns(serverName, userId, table, startFrom, pageSize);
    }

    /**
     * Create a SoftwareServerCapability entity for the external tool described in the body
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody - payload containing the external tool properties used to uniquely identify it
     * @return the response containing the success status if operation was successful or error in case of failure
     */
    @PostMapping(path = "register")
    public InformationViewOMASAPIResponse registerExternalTool(@PathVariable("serverName") String serverName,
                                                               @PathVariable("userId") String userId,
                                                               @RequestBody RegistrationRequestBody requestBody) {
        return restAPI.registerExternalTool(serverName, userId, requestBody);
    }


    /**
     * Return the SoftwareServerCapability entity for the external tool described in the payload
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody payload describing the external tool
     * @return response containing the SoftwareServerCapability entity associated to the tool or the error in case of failure to retrieve it
     */
    @PostMapping(path = "registration/lookup")
    public InformationViewOMASAPIResponse lookupExternalTool(@PathVariable("serverName") String serverName,
                                                             @PathVariable("userId") String userId,
                                                             @RequestBody RegistrationRequestBody requestBody) {
        return restAPI.lookupRegistration(serverName, userId, requestBody);
    }

    /**
     * Retrieve the report metadata representation for the report with the specified report id
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param registrationGuid guid of SoftwareServerCapability entity associated to the external tool
     * @param reportId - id of the report to retrieve
     * @return response containing the report metadata representation or the error in case of failure to retrieve it
     */
    @GetMapping(path = "/report")
    public InformationViewOMASAPIResponse retrieveReport(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @RequestParam String registrationGuid,
                                                         @RequestParam String reportId) {
        return restAPI.retrieveReport(serverName, userId, reportId);
    }

    /**
     * Retrieve the metadata representation of the data view with the specified data view id
     *
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param registrationGuid guid of SoftwareServerCapability entity associated to the external tool
     * @param dataViewId id of the data view
     * @return response containing the data view metadata representation or the error in case of failure to retrieve it
     */
    @GetMapping(path = "/dataview")
    public InformationViewOMASAPIResponse retrieveDataView(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @RequestParam String registrationGuid,
                                                         @RequestParam String dataViewId) {
        return restAPI.retrieveDataView(serverName, userId, dataViewId);
    }

}
