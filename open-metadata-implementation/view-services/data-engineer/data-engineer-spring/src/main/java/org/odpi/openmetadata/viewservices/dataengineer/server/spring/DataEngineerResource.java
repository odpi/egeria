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
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.dataengineer.server.DataEngineerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataEngineerResource provides part of the server-side implementation of the Data Engineer OMVS.
 */
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
     * @return a list of rows
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
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


    /**
     * Create a data value.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the newly created element or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataValue",
            description="Create a data value.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public GUIDResponse createDataValue(@PathVariable String serverName,
                                        @RequestBody (required = false) NewElementRequestBody requestBody)
    {
        return restAPI.createDataValue(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a data value using an existing metadata element as a template.  The
     * template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataValueFromTemplate",
            description="Create a new metadata element to represent a data value using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public GUIDResponse createDataValueFromTemplate(@PathVariable String serverName,
                                                    @RequestBody (required = false) TemplateRequestBody requestBody)
    {
        return restAPI.createDataValueFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a data value.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   properties for the element
     *
     * @return boolean - true if an update occurred or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{dataValueGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDataValue",
            description="Update the properties of a data value.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public BooleanResponse updateDataValue(@PathVariable String serverName,
                                           @PathVariable String dataValueGUID,
                                           @RequestBody (required = false) UpdateElementRequestBody requestBody)
    {
        return restAPI.updateDataValue(serverName, dataValueGUID, requestBody);
    }


    /**
     * Delete a data value.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{dataValueGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteDataValue",
            description="Delete a data value.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse deleteDataValue(@PathVariable String serverName,
                                        @PathVariable String dataValueGUID,
                                        @RequestBody (required = false) DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteDataValue(serverName, dataValueGUID, requestBody);
    }


    /**
     * Returns the list of data values with a particular name.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody name to search for and the query options
     *
     * @return a list of elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataValuesByName",
            description="Returns the list of data values with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public OpenMetadataRootElementsResponse getDataValuesByName(@PathVariable String serverName,
                                                                @RequestBody (required = false) FilterRequestBody requestBody)
    {
        return restAPI.getDataValuesByName(serverName, requestBody);
    }


    /**
     * Return the properties of a specific data value.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   options for the retrieve
     *
     * @return retrieved properties or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{dataValueGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataValueByGUID",
            description="Return the properties of a specific data value.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public OpenMetadataRootElementResponse getDataValueByGUID(@PathVariable String serverName,
                                                              @PathVariable String dataValueGUID,
                                                              @RequestBody (required = false) GetRequestBody requestBody)
    {
        return restAPI.getDataValueByGUID(serverName, dataValueGUID, requestBody);
    }


    /**
     * Retrieve the list of data value metadata elements that contain the search string.
     *
     * @param serverName  name of the server to route the request to
     * @param requestBody search string and the query options
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDataValues",
            description="Retrieve the list of data value metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public OpenMetadataRootElementsResponse findDataValues(@PathVariable String serverName,
                                                           @RequestBody (required = false) SearchStringRequestBody requestBody)
    {
        return restAPI.findDataValues(serverName, requestBody);
    }


    /**
     * Attach a data value to the asset that describes the digital resource where the data is stored.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param assetGUID     unique identifier of the asset for the digital resource where the data is stored
     * @param requestBody   properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{dataValueGUID}/data-value-sources/{assetGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDataValueSource",
            description="Attach a data value to the asset that describes the digital resource where the data is stored.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse linkDataValueSource(@PathVariable String serverName,
                                            @PathVariable String dataValueGUID,
                                            @PathVariable String assetGUID,
                                            @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataValueSource(serverName, dataValueGUID, assetGUID, requestBody);
    }


    /**
     * Detach a data value from the asset that describes the digital resource where the data is stored.
     *
     * @param serverName    name of the server to route the request to
     * @param dataValueGUID unique identifier of the data value
     * @param assetGUID     unique identifier of the asset for the digital resource where the data is stored
     * @param requestBody   options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{dataValueGUID}/data-value-sources/{assetGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataValueSource",
            description="Detach a data value from the asset that describes the digital resource where the data is stored.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse detachDataValueSource(@PathVariable String serverName,
                                              @PathVariable String dataValueGUID,
                                              @PathVariable String assetGUID,
                                              @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataValueSource(serverName, dataValueGUID, assetGUID, requestBody);
    }


    /**
     * Attach an element (such as a process) to a data value that is used to configure its processing.
     *
     * @param serverName    name of the server to route the request to
     * @param elementGUID   unique identifier of the element that uses the data value
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/elements/{elementGUID}/processing-variables/{dataValueGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkProcessingVariable",
            description="Attach an element (such as a process) to a data value that is used to configure its processing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse linkProcessingVariable(@PathVariable String serverName,
                                               @PathVariable String elementGUID,
                                               @PathVariable String dataValueGUID,
                                               @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkProcessingVariable(serverName, elementGUID, dataValueGUID, requestBody);
    }


    /**
     * Detach an element (such as a process) from a data value that is used to configure its processing.
     *
     * @param serverName    name of the server to route the request to
     * @param elementGUID   unique identifier of the element that uses the data value
     * @param dataValueGUID unique identifier of the data value
     * @param requestBody   options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/elements/{elementGUID}/processing-variables/{dataValueGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachProcessingVariable",
            description="Detach an element (such as a process) from a data value that is used to configure its processing.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse detachProcessingVariable(@PathVariable String serverName,
                                                 @PathVariable String elementGUID,
                                                 @PathVariable String dataValueGUID,
                                                 @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachProcessingVariable(serverName, elementGUID, dataValueGUID, requestBody);
    }


    /**
     * Embed a data value in another data value.
     *
     * @param serverName            name of the server to route the request to
     * @param parentDataValueGUID   unique identifier of the parent data value
     * @param embeddedDataValueGUID unique identifier of the embedded data value
     * @param requestBody           properties for the relationship
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{parentDataValueGUID}/embedded-data-values/{embeddedDataValueGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkEmbeddedDataValue",
            description="Embed a data value in another data value.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse linkEmbeddedDataValue(@PathVariable String serverName,
                                              @PathVariable String parentDataValueGUID,
                                              @PathVariable String embeddedDataValueGUID,
                                              @RequestBody (required = false) NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkEmbeddedDataValue(serverName, parentDataValueGUID, embeddedDataValueGUID, requestBody);
    }


    /**
     * Remove an embedded data value from its parent data value.
     *
     * @param serverName            name of the server to route the request to
     * @param parentDataValueGUID   unique identifier of the parent data value
     * @param embeddedDataValueGUID unique identifier of the embedded data value
     * @param requestBody           options for the delete
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  PropertyServerException    a problem reported in the open metadata server(s)
     *  UserNotAuthorizedException the user is not authorized to issue this request
     */
    @PostMapping(path = "/data-values/{parentDataValueGUID}/embedded-data-values/{embeddedDataValueGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachEmbeddedDataValue",
            description="Remove an embedded data value from its parent data value.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/2/0208-Data-Values/"))

    public VoidResponse detachEmbeddedDataValue(@PathVariable String serverName,
                                                @PathVariable String parentDataValueGUID,
                                                @PathVariable String embeddedDataValueGUID,
                                                @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachEmbeddedDataValue(serverName, parentDataValueGUID, embeddedDataValueGUID, requestBody);
    }
}
