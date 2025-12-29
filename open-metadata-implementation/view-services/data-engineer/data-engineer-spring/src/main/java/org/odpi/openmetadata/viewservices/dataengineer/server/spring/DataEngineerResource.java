/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dataengineer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.TabularDataSetReportResponse;
import org.odpi.openmetadata.viewservices.dataengineer.server.DataEngineerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataEngineerResource provides part of the server-side implementation of the Data Engineer OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-engineer")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="API: Data Engineer", description="Managing metadata about data pipelines and reference data.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-engineer/overview/"))

public class DataEngineerResource
{
    private final DataEngineerRESTServices restAPI = new DataEngineerRESTServices();

    /**
     * Default constructor
     */
    public DataEngineerResource()
    {
    }


    /**
     * Returns details of a tabular data set including the data rows that match the startFromRow and maxRowCount values.
     * If maxRowCount=0 then no data records are returned.
     *
     * @param serverName     name of called server
     * @param tabularDataSetGUID     unique identifier of tabular dat set asset
     * @param startFromRow    cursor position in the data set
     * @param maxRowCount maximum number of rows to return.  0 means no rows.
     *
     * @return a list of projects
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping("/tabular-data-sets/{tabularDataSetGUID}/report")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTabularDataSetReport",
            description="Returns details of a tabular data set including the data rows that match the startFromRow and maxRowCount values.  If maxRowCount=0 then no data records are returned.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/tabular-data-set"))

    public TabularDataSetReportResponse getTabularDataSetReport(@PathVariable String serverName,
                                                                @PathVariable String tabularDataSetGUID,
                                                                @RequestParam(required = false, defaultValue = "0") int startFromRow,
                                                                @RequestParam(required = false, defaultValue = "0") int maxRowCount)
    {
        return restAPI.getTabularDataSetReport(serverName, tabularDataSetGUID, startFromRow, maxRowCount);
    }
}
