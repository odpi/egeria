/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexpert.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.metadataexpert.server.MetadataExpertRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MetadataExpertResource provides part of the server-side implementation of the Metadata Expert OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Metadata Expert", description="Supports advanced operations for the maintenance of open metadata.  It is an advanced API for users that understand the [Open Metadata Types](https://egeria-project.org/types/).",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/metadata-expert/overview/"))

public class MetadataExpertResource
{
    private final MetadataExpertRESTServices restAPI = new MetadataExpertRESTServices();

    /**
     * Default constructor
     */
    public MetadataExpertResource()
    {
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createMetadataElementInStore(@PathVariable String                            serverName,
                                                     @PathVariable String                            urlMarker,
                                                     @RequestBody  NewOpenMetadataElementRequestBody requestBody)
    {
        return restAPI.createMetadataElementInStore(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element in the metadata store using a template.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createMetadataElementFromTemplate(@PathVariable String                         serverName,
                                                          @PathVariable String                         urlMarker,
                                                          @RequestBody OpenMetadataTemplateRequestBody requestBody)
    {
        return restAPI.createMetadataElementFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new properties
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the properties are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public BooleanResponse updateMetadataElementInStore(@PathVariable String                      serverName,
                                                        @PathVariable String                      urlMarker,
                                                        @PathVariable String                      metadataElementGUID,
                                                        @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementInStore(serverName, urlMarker, metadataElementGUID, requestBody);
    }

    /**
     * Update the zone membership to increase its visibility.  The publishZones  are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/publish")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse publishMetadataElement(@PathVariable String                    serverName,
                                               @PathVariable String                    urlMarker,
                                               @PathVariable String                    metadataElementGUID,
                                               @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.publishMetadataElement(serverName, urlMarker, metadataElementGUID, requestBody);
    }


    /**
     * Update the zone membership to decrease its visibility.  The defaultZones are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/withdraw")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse withdrawMetadataElement(@PathVariable String                    serverName,
                                                @PathVariable String                    urlMarker,
                                                @PathVariable String                    metadataElementGUID,
                                                @RequestBody  MetadataSourceRequestBody requestBody)
    {
        return restAPI.withdrawMetadataElement(serverName, urlMarker, metadataElementGUID, requestBody);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-status")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateMetadataElementStatusInStore(@PathVariable String                  serverName,
                                                           @PathVariable String                  urlMarker,
                                                           @PathVariable String                  metadataElementGUID,
                                                           @RequestBody  UpdateStatusRequestBody requestBody)
    {
        return restAPI.updateMetadataElementStatusInStore(serverName, urlMarker, metadataElementGUID, requestBody);
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-effectivity")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateMetadataElementEffectivityInStore(@PathVariable String                           serverName,
                                                                @PathVariable String                           urlMarker,
                                                                @PathVariable String                           metadataElementGUID,
                                                                @RequestBody UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementEffectivityInStore(serverName, urlMarker, metadataElementGUID, requestBody);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody delete options request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public  VoidResponse deleteMetadataElementInStore(@PathVariable String            serverName,
                                                      @PathVariable String            urlMarker,
                                                      @PathVariable String            metadataElementGUID,
                                                      @RequestBody(required = false) OpenMetadataDeleteRequestBody requestBody)
    {
        return restAPI.deleteMetadataElementInStore(serverName, urlMarker, metadataElementGUID, requestBody);
    }


    /**
     * Archive a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to archive this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/archive")
    @SecurityRequirement(name = "BearerAuthorization")

    public  VoidResponse archiveMetadataElementInStore(@PathVariable String           serverName,
                                                       @PathVariable String           urlMarker,
                                                       @PathVariable String           metadataElementGUID,
                                                       @RequestBody(required = false) DeleteElementRequestBody requestBody)
    {
        return restAPI.archiveMetadataElementInStore(serverName, urlMarker, metadataElementGUID, requestBody);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param requestBody properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse classifyMetadataElementInStore(@PathVariable String                                  serverName,
                                                       @PathVariable String                                  urlMarker,
                                                       @PathVariable String                                  metadataElementGUID,
                                                       @PathVariable String                                  classificationName,
                                                       @RequestBody NewOpenMetadataClassificationRequestBody requestBody)
    {
        return restAPI.classifyMetadataElementInStore(serverName, urlMarker, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody new properties for the classification
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse reclassifyMetadataElementInStore(@PathVariable String                      serverName,
                                                         @PathVariable String                      urlMarker,
                                                         @PathVariable String                      metadataElementGUID,
                                                         @PathVariable String                      classificationName,
                                                         @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.reclassifyMetadataElementInStore(serverName, urlMarker, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-effectivity")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateClassificationEffectivityInStore(@PathVariable String                            serverName,
                                                               @PathVariable String                            urlMarker,
                                                               @PathVariable String                            metadataElementGUID,
                                                               @PathVariable String                            classificationName,
                                                               @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateClassificationEffectivityInStore(serverName, urlMarker, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to remove this classification
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse declassifyMetadataElementInStore(@PathVariable String            serverName,
                                                         @PathVariable String            urlMarker,
                                                         @PathVariable String            metadataElementGUID,
                                                         @PathVariable String            classificationName,
                                                         @RequestBody(required = false)  MetadataSourceRequestBody requestBody)
    {
        return restAPI.declassifyMetadataElementInStore(serverName, urlMarker, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody the properties of the relationship
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse createRelatedElementsInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        urlMarker,
                                                     @RequestBody  NewRelatedElementsRequestBody requestBody)
    {
        return restAPI.createRelatedElementsInStore(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody new properties for the relationship
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to update this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/update-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateRelatedElementsInStore(@PathVariable String                      serverName,
                                                     @PathVariable String                      urlMarker,
                                                     @PathVariable String                      relationshipGUID,
                                                     @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsInStore(serverName, urlMarker, relationshipGUID, requestBody);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/update-effectivity")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateRelatedElementsEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            urlMarker,
                                                                @PathVariable String                            relationshipGUID,
                                                                @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsEffectivityInStore(serverName, urlMarker, relationshipGUID, requestBody);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse deleteRelatedElementsInStore(@PathVariable String            serverName,
                                                     @PathVariable String            urlMarker,
                                                     @PathVariable String            relationshipGUID,
                                                     @RequestBody(required = false)  OpenMetadataDeleteRequestBody requestBody)
    {
        return restAPI.deleteRelatedElementsInStore(serverName, urlMarker, relationshipGUID, requestBody);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param requestBody  options to control access to open metadata
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{metadataElement1GUID}/{relationshipTypeName}/{metadataElement2GUID}/detach-all")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse detachRelatedElementsInStore(@PathVariable String            serverName,
                                                     @PathVariable String            urlMarker,
                                                     @PathVariable String            metadataElement1GUID,
                                                     @PathVariable String            relationshipTypeName,
                                                     @PathVariable String            metadataElement2GUID,
                                                     @RequestBody(required = false)  OpenMetadataDeleteRequestBody requestBody)
    {
        return restAPI.detachRelatedElementsInStore(serverName, urlMarker, relationshipTypeName,metadataElement1GUID, metadataElement2GUID, requestBody);
    }
}
