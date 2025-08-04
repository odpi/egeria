/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementHeadersResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.ExternalIdEffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.rest.MetadataCorrelationHeadersResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.UpdateMetadataCorrelatorsRequestBody;
import org.odpi.openmetadata.frameworkservices.omf.server.ExternalIdentifierRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for managing external identifiers.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/{serviceURLMarker}/open-metadata-store/users/{userId}")

@Tag(name="Metadata Access Services: Open Metadata Store Services",
        description="Provides generic open metadata retrieval and management services for Open Metadata Access Services (OMASs).",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omf-metadata-management/"))

public class ExternalIdentifierResource
{
    private final ExternalIdentifierRESTServices restAPI = new ExternalIdentifierRESTServices();


    /**
     * Instantiates a new Open Metadata Store resource.
     */
    public ExternalIdentifierResource()
    {
    }


    /**
     * Add the description of a specific external identifier and link it to the associated metadata element.  Note, the external identifier is anchored to the scope.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/add")

    @Operation(summary = "addExternalIdentifier",
            description = "Add the description of a specific external identifier and link it to the associated metadata element.  Note, the external identifier is anchored to the scope (specified in the request body).",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse addExternalIdentifier(@PathVariable String                               serverName,
                                              @PathVariable String                               serviceURLMarker,
                                              @PathVariable String                               userId,
                                              @PathVariable String                               openMetadataElementGUID,
                                              @PathVariable String                               openMetadataElementTypeName,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                              forLineage,
                                              @RequestParam (required = false, defaultValue = "false")
                                                            boolean                              forDuplicateProcessing,
                                              @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.addExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/update")

    @Operation(summary = "updateExternalIdentifier",
            description = "Update the description of a specific external identifier.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse updateExternalIdentifier(@PathVariable String                               serverName,
                                                 @PathVariable String                               serviceURLMarker,
                                                 @PathVariable String                               userId,
                                                 @PathVariable String                               openMetadataElementGUID,
                                                 @PathVariable String                               openMetadataElementTypeName,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                              forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                              forDuplicateProcessing,
                                                 @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.updateExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Validate that the external identifier is linked to the open metadata GUID.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/validate")

    @Operation(summary = "validateExternalIdentifier",
            description = "Validate that the external identifier is linked to the open metadata GUID.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public BooleanResponse validateExternalIdentifier(@PathVariable String                               serverName,
                                                      @PathVariable String                               serviceURLMarker,
                                                      @PathVariable String                               userId,
                                                      @PathVariable String                               openMetadataElementGUID,
                                                      @PathVariable String                               openMetadataElementTypeName,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                                    boolean                              forLineage,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                                    boolean                              forDuplicateProcessing,
                                                      @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.validateExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers/remove")

    @Operation(summary = "removeExternalIdentifier",
            description = "Remove an external identifier from an existing open metadata element.  The open metadata element is not affected.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse removeExternalIdentifier(@PathVariable String                               serverName,
                                                 @PathVariable String                               serviceURLMarker,
                                                 @PathVariable String                               userId,
                                                 @PathVariable String                               openMetadataElementGUID,
                                                 @PathVariable String                               openMetadataElementTypeName,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                              forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                               boolean                              forDuplicateProcessing,
                                                 @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.removeExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param externalScopeGUID unique identifier (GUID) of the scope element in the open metadata ecosystem
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of this element in the external asset manager plus additional mapping properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-scope/{externalScopeGUID}/remove")

    @Operation(summary = "removeExternalScope",
            description = "Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.  The linked open metadata elements are not affected.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse removeExternalScope(@PathVariable String                   serverName,
                                            @PathVariable String                   serviceURLMarker,
                                            @PathVariable String                   userId,
                                            @PathVariable String                   externalScopeGUID,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                  forLineage,
                                            @RequestParam (required = false, defaultValue = "false")
                                                          boolean                  forDuplicateProcessing,
                                            @RequestBody  (required = false)
                                                          EffectiveTimeRequestBody requestBody)
    {
        return restAPI.removeExternalScope(serverName, serviceURLMarker, userId, externalScopeGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/synchronized")

    @Operation(summary = "confirmSynchronization",
            description = "Confirm that the values of a particular metadata element have been synchronized.  This is important from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public VoidResponse confirmSynchronization(@PathVariable String                        serverName,
                                               @PathVariable String                        serviceURLMarker,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        openMetadataElementGUID,
                                               @PathVariable String                        openMetadataElementTypeName,
                                               @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.confirmSynchronization(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Retrieve the metadata element associated with a particular external identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier
     *
     * @return list of linked elements, null if null or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/external-identifiers/open-metadata-elements")

    @Operation(summary = "getElementsForExternalIdentifier",
            description = "Retrieve the metadata element associated with a particular external identifier.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public ElementHeadersResponse getElementsForExternalIdentifier(@PathVariable String            serverName,
                                                                   @PathVariable String            serviceURLMarker,
                                                                   @PathVariable String            userId,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                 int               startFrom,
                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                 int               pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean           forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean           forDuplicateProcessing,
                                                                   @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.getElementsForExternalIdentifier(serverName, serviceURLMarker, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the external identifiers attached to the supplied element guid.
     *
     * @param serverName name of the server to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier of the requested metadata element
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody correlation properties
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/external-identifiers")

    @Operation(summary = "getExternalIdentifiers",
            description = "Retrieve the external identifiers attached to the supplied element guid.",
            externalDocs = @ExternalDocumentation(description = "External Identifiers",
                    url = "https://egeria-project.org/types/0/0017-External-Identifiers/"))

    public MetadataCorrelationHeadersResponse getExternalIdentifiers(@PathVariable String                        serverName,
                                                                     @PathVariable String                        serviceURLMarker,
                                                                     @PathVariable String                        userId,
                                                                     @PathVariable String                        openMetadataElementGUID,
                                                                     @PathVariable String                        openMetadataElementTypeName,
                                                                     @RequestParam (required = false, defaultValue = "0")
                                                                         int               startFrom,
                                                                     @RequestParam (required = false, defaultValue = "0")
                                                                         int               pageSize,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                       forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                       forDuplicateProcessing,
                                                                     @RequestBody  (required = false)
                                                                         ExternalIdEffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getExternalIdentifiers(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }
}
