/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.server.spring;


import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
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
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return
     */
    @PostMapping(path = "/report")
    public InformationViewOMASAPIResponse submitReport(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @RequestBody ReportRequestBody requestBody) {
        return restAPI.submitReport(serverName, userId, requestBody);
    }


    /**
     * @param serverName  unique identifier for requested server.
     * @param userId      the unique identifier for the user
     * @param requestBody The json representing the structure of the report and basic report properties
     * @return
     */
    @PostMapping(path = "/dataview")
    public InformationViewOMASAPIResponse submitDataView(@PathVariable("serverName") String serverName,
                                                         @PathVariable("userId") String userId,
                                                         @RequestBody DataViewRequestBody requestBody) {
        return restAPI.submitDataView(serverName, userId, requestBody);
    }


    @GetMapping(path = "/databases")
    public InformationViewOMASAPIResponse retrieveDatabases(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @RequestParam int     startFrom,
                                                            @RequestParam int     pageSize) {
        return restAPI.getDatabases(serverName, userId, startFrom, pageSize);
    }
    @GetMapping(path = "database/{database}/tables")
    public InformationViewOMASAPIResponse retrieveTablesForDatabase(@PathVariable("serverName") String serverName,
                                                            @PathVariable("userId") String userId,
                                                            @PathVariable("database") String database,
                                                            @RequestParam int     startFrom,
                                                            @RequestParam int     pageSize) {
        return restAPI.getTablesForDatabase(serverName, userId, database, startFrom, pageSize);
    }

    @GetMapping(path = "table/{table}")
    public InformationViewOMASAPIResponse retrieveTableContext(@PathVariable("serverName") String serverName,
                                                                @PathVariable("userId") String userId,
                                                                @PathVariable("table") String table) {
        return restAPI.getTableContext(serverName, userId, table);
    }

    @GetMapping(path = "table/{table}/columns")
    public InformationViewOMASAPIResponse retrieveTableColumns(@PathVariable("serverName") String serverName,
                                                                @PathVariable("userId") String userId,
                                                                @PathVariable("table") String table,
                                                                @RequestParam int     startFrom,
                                                                @RequestParam int     pageSize) {
        return restAPI.getTableColumns(serverName, userId, table, startFrom, pageSize);
    }

}
