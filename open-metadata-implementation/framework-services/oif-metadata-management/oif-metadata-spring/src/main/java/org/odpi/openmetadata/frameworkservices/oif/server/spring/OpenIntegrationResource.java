/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.integration.properties.IntegrationReportProperties;
import org.odpi.openmetadata.frameworkservices.oif.rest.CatalogTargetsResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.IntegrationReportResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.IntegrationReportsResponse;
import org.odpi.openmetadata.frameworkservices.oif.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.frameworkservices.oif.server.OpenIntegrationRESTServices;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * OpenIntegrationResource supports the REST APIs for running the Open Integration Service.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/open-integration/users/{userId}")

@Tag(name="Framework Services: Open Integration Service",
     description="Provides support for the context used by integration connectors.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/oif-metadata-management/"))


public class OpenIntegrationResource
{
    private final OpenIntegrationRESTServices restAPI = new OpenIntegrationRESTServices();


    /**
     * Retrieve the unique identifier of the software server capability representing a metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "metadata-sources/by-name")

    public GUIDResponse getMetadataSourceGUID(@PathVariable String          serverName,
                                              @PathVariable String          userId,
                                              @RequestBody NameRequestBody requestBody)
    {
        return restAPI.getMetadataSourceGUID(serverName, userId, requestBody);
    }


    /**
     * Create a new metadata element to represent a software capability.  This describes the metadata source.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param requestBody properties of the software capability
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-sources/new")

    public GUIDResponse createMetadataSource(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.createMetadataSource(serverName, userId, requestBody);
    }


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the integration connector definition.
     */
    @GetMapping(path = "/integration-connectors/{integrationConnectorGUID}/catalog-targets")

    public CatalogTargetsResponse  getCatalogTargets(@PathVariable String  serverName,
                                                     @PathVariable String  userId,
                                                     @PathVariable String  integrationConnectorGUID,
                                                     @RequestParam int     startingFrom,
                                                     @RequestParam int     maximumResults)
    {
        return restAPI.getCatalogTargets(serverName, userId, integrationConnectorGUID, startingFrom, maximumResults);
    }


    /**
     * Create a new integration report for an element (identified by anchorGUID).
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param anchorGUID element to attach the integration report to
     * @param anchorTypeName typeName of the anchor for the integration report
     * @param properties properties of the report
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request,
     *  PropertyServerException problem with the metadata server.
     */
    @PostMapping(path = "/integration-reports/{anchorGUID}/{anchorTypeName}/new")

    public VoidResponse publishIntegrationReport(@PathVariable String                      serverName,
                                                 @PathVariable String                      userId,
                                                 @PathVariable String                      anchorGUID,
                                                 @PathVariable String                      anchorTypeName,
                                                 @RequestBody  IntegrationReportProperties properties)
    {
        return restAPI.publishIntegrationReport(serverName, userId, anchorGUID, anchorTypeName, properties);
    }


    /**
     * Retrieve a specific integration report by unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param reportGUID unique identifier of the integration report
     *
     * @return report or null or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request,
     *  PropertyServerException problem with the metadata server.
     */
    @GetMapping(path = "/integration-reports/{reportGUID}")

    public IntegrationReportResponse getIntegrationReport(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String reportGUID)
    {
        return restAPI.getIntegrationReport(serverName, userId, reportGUID);
    }


    /**
     * Retrieve the integration reports attached to an element.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException problem with the metadata server.
     */
    @GetMapping(path = "/integration-reports/for-element/{elementGUID}")

    public IntegrationReportsResponse getIntegrationReportsForElement(@PathVariable String  serverName,
                                                                      @PathVariable String  userId,
                                                                      @PathVariable String  elementGUID,
                                                                      @RequestParam (required = false)
                                                                                    Date    afterCompletionDate,
                                                                      @RequestParam (required = false)
                                                                                    Date    beforeStartDate,
                                                                      @RequestParam int     startingFrom,
                                                                      @RequestParam int     maximumResults)
    {
        return restAPI.getIntegrationReportsForElement(serverName,
                                                       userId,
                                                       elementGUID,
                                                       afterCompletionDate,
                                                       beforeStartDate,
                                                       startingFrom,
                                                       maximumResults);
    }


    /**
     * Retrieve the published integration reports.  The list can be filtered by start and completion date of the report along with the
     * paging options if there are many integration reports.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param afterCompletionDate restrict reports to those that completed after the requested time (null for any completion time).
     * @param beforeStartDate restrict reports to those that started before the requested time (null for any start time).
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of qualifying reports or
     *  InvalidParameterException one of the parameters is null or invalid,
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException problem with the metadata server.
     */
    @GetMapping(path = "/integration-reports")

    public IntegrationReportsResponse getIntegrationReports(@PathVariable String  serverName,
                                                            @PathVariable String  userId,
                                                            @RequestParam (required = false)
                                                            Date    afterCompletionDate,
                                                            @RequestParam (required = false)
                                                            Date    beforeStartDate,
                                                            @RequestParam int     startingFrom,
                                                            @RequestParam int     maximumResults)
    {
        return restAPI.getIntegrationReports(serverName, userId, afterCompletionDate, beforeStartDate, startingFrom, maximumResults);
    }
}
