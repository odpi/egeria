/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementHeadersResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworkservices.gaf.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.rest.MetadataCorrelationHeadersResponse;
import org.odpi.openmetadata.frameworkservices.gaf.rest.UpdateMetadataCorrelatorsRequestBody;
import org.odpi.openmetadata.frameworkservices.gaf.server.ExternalIdentifierRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * Server-side REST API support for asset manager independent REST endpoints
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/open-metadata-store/users/{userId}")

@Tag(name="Framework Services: Open Metadata Store Services",
        description="Provides generic open metadata retrieval and management services for Open Metadata Access Services (OMASs).",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/gaf-metadata-management/"))

public class ExternalIdentifierResource
{
    private final ExternalIdentifierRESTServices restAPI = new ExternalIdentifierRESTServices();


    /**
     * Instantiates a new Asset Manager OMAS resource.
     */
    public ExternalIdentifierResource()
    {
    }



    /**
     * Add the description of a specific external identifier.
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

    public VoidResponse addExternalIdentifier(@PathVariable String                        serverName,
                                              @PathVariable String                        serviceURLMarker,
                                              @PathVariable String                        userId,
                                              @PathVariable String                        openMetadataElementGUID,
                                              @PathVariable String                        openMetadataElementTypeName,
                                              @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.addExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
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

    public VoidResponse updateExternalIdentifier(@PathVariable String                        serverName,
                                                 @PathVariable String                        serviceURLMarker,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        openMetadataElementGUID,
                                                 @PathVariable String                        openMetadataElementTypeName,
                                                 @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.updateExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * That the external identifier matches the open metadata GUID.
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

    public BooleanResponse validateExternalIdentifier(@PathVariable String                        serverName,
                                                      @PathVariable String                        serviceURLMarker,
                                                      @PathVariable String                        userId,
                                                      @PathVariable String                        openMetadataElementGUID,
                                                      @PathVariable String                        openMetadataElementTypeName,
                                                      @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.validateExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, requestBody);
    }


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
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

    public VoidResponse removeExternalIdentifier(@PathVariable String                        serverName,
                                                 @PathVariable String                        serviceURLMarker,
                                                 @PathVariable String                        userId,
                                                 @PathVariable String                        openMetadataElementGUID,
                                                 @PathVariable String                        openMetadataElementTypeName,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forLineage,
                                                 @RequestParam (required = false, defaultValue = "false")
                                                         boolean                      forDuplicateProcessing,
                                                 @RequestBody UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.removeExternalIdentifier(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody details of the external identifier and its scope
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException user not authorized to issue this request
     * PropertyServerException    problem accessing the property server
     */
    @PostMapping(path = "/metadata-elements/{openMetadataElementTypeName}/{openMetadataElementGUID}/synchronized")

    public VoidResponse confirmSynchronization(@PathVariable String                        serverName,
                                               @PathVariable String                        serviceURLMarker,
                                               @PathVariable String                        userId,
                                               @PathVariable String                        openMetadataElementGUID,
                                               @PathVariable String                        openMetadataElementTypeName,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                       forLineage,
                                               @RequestParam (required = false, defaultValue = "false")
                                                             boolean                       forDuplicateProcessing,
                                               @RequestBody  MetadataCorrelationProperties requestBody)
    {
        return restAPI.confirmSynchronization(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically, the qualified name comes from the integration connector configuration.
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

    public ElementHeadersResponse getElementsForExternalIdentifier(@PathVariable String            serverName,
                                                                   @PathVariable String                        serviceURLMarker,
                                                                   @PathVariable String            userId,
                                                                   @RequestParam int               startFrom,
                                                                   @RequestParam int               pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean           forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean           forDuplicateProcessing,
                                                                   @RequestBody  UpdateMetadataCorrelatorsRequestBody requestBody)
    {
        return restAPI.getElementsForExternalIdentifier(serverName, serviceURLMarker, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Assemble the correlation headers attached to the supplied element guid.
     *
     * @param serverName name of the server to route the request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier of the requested metadata element
     * @param openMetadataElementTypeName type name for the open metadata element
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

    public MetadataCorrelationHeadersResponse getExternalIdentifiers(@PathVariable String                        serverName,
                                                                     @PathVariable String                        serviceURLMarker,
                                                                     @PathVariable String                        userId,
                                                                     @PathVariable String                        openMetadataElementGUID,
                                                                     @PathVariable String                        openMetadataElementTypeName,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                       forLineage,
                                                                     @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                       forDuplicateProcessing,
                                                                     @RequestBody  (required = false)
                                                                                          EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getMetadataCorrelationHeaders(serverName, serviceURLMarker, userId, openMetadataElementGUID, openMetadataElementTypeName, forLineage, forDuplicateProcessing, requestBody);
    }
}
