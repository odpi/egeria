/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexpert.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.metadataexpert.rest.MatchCriteriaListResponse;
import org.odpi.openmetadata.viewservices.metadataexpert.rest.PropertyComparisonOperatorListResponse;
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     * Update the zone membership to increase its visibility.  The publishZones are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/publish")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse publishMetadataElement(@PathVariable String                    serverName,
                                               @PathVariable String                    urlMarker,
                                               @PathVariable String                    metadataElementGUID,
                                               @RequestBody(required = false)  MetadataSourceRequestBody requestBody)
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
     *  PropertyServerException a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/withdraw")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse withdrawMetadataElement(@PathVariable String                    serverName,
                                                @PathVariable String                    urlMarker,
                                                @PathVariable String                    metadataElementGUID,
                                                @RequestBody(required = false)  MetadataSourceRequestBody requestBody)
    {
        return restAPI.withdrawMetadataElement(serverName, urlMarker, metadataElementGUID, requestBody);
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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
     *  PropertyServerException a problem with the metadata store
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



    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  elementGUID,
                                                                @PathVariable String  urlMarker,
                                                                @RequestBody (required = false)
                                                                GetRequestBody requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, elementGUID, urlMarker, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                      @PathVariable String          urlMarker,
                                                                      @RequestBody  UniqueNameRequestBody requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/guid-by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @PathVariable String          urlMarker,
                                                           @RequestBody  UniqueNameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of object to retrieve
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementHistory",
            description="Retrieve all the versions of an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse getMetadataElementHistory(@PathVariable String                 serverName,
                                                                  @PathVariable String                 urlMarker,
                                                                  @PathVariable String                 elementGUID,
                                                                  @RequestBody(required = false)
                                                                  HistoryRequestBody     requestBody)
    {
        return restAPI.getMetadataElementHistory(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve all the versions of an element's classification.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of object to retrieve
     * @param classificationName name of classification to retrieve
     * @param requestBody the time window required
     * @return list of classifications or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/classifications/{classificationName}/history")

    @Operation(summary="getClassificationHistory",
            description="Retrieve all the versions of an element's classification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omf-metadata-management/"))

    public AttachedClassificationsResponse getClassificationHistory(@PathVariable String                 serverName,
                                                                    @PathVariable String             urlMarker,
                                                                    @PathVariable String                 elementGUID,
                                                                    @PathVariable String                 classificationName,
                                                                    @RequestBody(required = false)
                                                                    HistoryRequestBody     requestBody)
    {
        return restAPI.getClassificationHistory(serverName, urlMarker, elementGUID, classificationName, requestBody);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMetadataElementsWithString",
            description="Retrieve the metadata elements that contain the requested string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse findMetadataElementsWithString(@PathVariable String                  serverName,
                                                                       @PathVariable String                  urlMarker,
                                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findMetadataElementsWithString(serverName, urlMarker, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param anchorGUID unique identifier of anchor
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/metadata-elements/by-search-string/for-anchor/{anchorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsForAnchor",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied anchorGUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesResponse findElementsForAnchor(@PathVariable String                  serverName,
                                                             @PathVariable String                  urlMarker,
                                                             @PathVariable String                  anchorGUID,
                                                             @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsForAnchor(serverName, urlMarker, anchorGUID, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param anchorDomainName name of open metadata type for the domain
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/metadata-elements/by-search-string/in-anchor-domain/{anchorDomainName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsInAnchorDomain",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied domain name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesListResponse findElementsInAnchorDomain(@PathVariable String                  serverName,
                                                                      @PathVariable String                  urlMarker,
                                                                      @PathVariable String                  anchorDomainName,
                                                                      @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsInAnchorDomain(serverName, urlMarker, anchorDomainName, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */

    @PostMapping(path = "/metadata-elements/by-search-string/in-anchor-scope/{anchorScopeGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsInAnchorScope",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied scope guid.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesListResponse findElementsInAnchorScope(@PathVariable String                  serverName,
                                                                     @PathVariable String                  urlMarker,
                                                                     @PathVariable String                  anchorScopeGUID,
                                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsInAnchorScope(serverName, urlMarker, anchorScopeGUID, requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/any-type")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public RelatedMetadataElementListResponse getAllRelatedMetadataElements(@PathVariable String  serverName,
                                                                            @PathVariable String  elementGUID,
                                                                            @PathVariable String  urlMarker,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int     startingAtEnd,
                                                                            @RequestBody (required = false)
                                                                            ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  elementGUID,
                                                  null,
                                                  urlMarker,
                                                  startingAtEnd,
                                                  requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element via a specific relationship type.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/type/{relationshipTypeName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element via a specific relationship type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public RelatedMetadataElementListResponse getRelatedMetadataElements(@PathVariable String  serverName,
                                                                         @PathVariable String  elementGUID,
                                                                         @PathVariable String  relationshipTypeName,
                                                                         @PathVariable String  urlMarker,
                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                         int     startingAtEnd,
                                                                         @RequestBody (required = false)
                                                                         ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  elementGUID,
                                                  relationshipTypeName,
                                                  urlMarker,
                                                  startingAtEnd,
                                                  requestBody);
    }



    /**
     * Retrieve the relationships linking the supplied elements.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.

     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-any-type/to-elements/{metadataElementAtEnd2GUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getAllMetadataElementRelationships(@PathVariable String  serverName,
                                                                                   @PathVariable String  metadataElementAtEnd1GUID,
                                                                                   @PathVariable String  metadataElementAtEnd2GUID,
                                                                                   @PathVariable String  urlMarker,
                                                                                   @RequestBody(required = false)
                                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       metadataElementAtEnd1GUID,
                                                       null,
                                                       metadataElementAtEnd2GUID,
                                                       urlMarker,
                                                       requestBody);
    }


    /**
     * Return all the elements that are anchored to an element plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker      the identifier of the view service (for example runtime-manager for the Runtime Manager OMVS)
     * @param elementGUID  unique identifier for the element
     * @param requestBody effective time and asOfTime
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/with-anchored-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAnchoredElementsGraph",
            description="Return all the elements that are anchored to an element plus relationships between these elements and to other elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataGraphResponse getAnchoredElementsGraph(@PathVariable String          serverName,
                                                              @PathVariable String          urlMarker,
                                                              @PathVariable String          elementGUID,
                                                              @RequestBody (required = false)
                                                              ResultsRequestBody requestBody)
    {
        return restAPI.getAnchoredElementsGraph(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Retrieve the relationships linking the supplied elements via a specific type of relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-type/{relationshipTypeName}/to-elements/{metadataElementAtEnd2GUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements via a specific type of relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getMetadataElementRelationships(@PathVariable String  serverName,
                                                                                @PathVariable String  metadataElementAtEnd1GUID,
                                                                                @PathVariable String  relationshipTypeName,
                                                                                @PathVariable String  metadataElementAtEnd2GUID,
                                                                                @PathVariable String  urlMarker,
                                                                                @RequestBody(required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       metadataElementAtEnd1GUID,
                                                       relationshipTypeName,
                                                       metadataElementAtEnd2GUID,
                                                       urlMarker,
                                                       requestBody);
    }




    /* ===============================================================================
     * The find request uses a match criteria and a property comparison operator.
     */

    /**
     * Return the list of enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/metadata-search/property-comparison-operator-values")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getPropertyComparisonOperatorList",
            description="Return the list of valid property comparison operator values",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public PropertyComparisonOperatorListResponse getPropertyComparisonOperatorList(@PathVariable String serverName)
    {
        return restAPI.getPropertyComparisonOperatorList(serverName);
    }

    /**
     * Return the list of enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    @GetMapping(path = "/metadata-search/match-criteria-values")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMatchCriteriaList",
            description="Return the list of valid match criteria values",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public MatchCriteriaListResponse getMatchCriteriaList(@PathVariable String serverName)
    {
        return restAPI.getMatchCriteriaList(serverName);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-conditions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMetadataElements",
            description="Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse findMetadataElements(@PathVariable String          serverName,
                                                             @PathVariable String          urlMarker,
                                                             @RequestBody (required = false)
                                                             FindRequestBody requestBody)
    {
        return restAPI.findMetadataElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-search-conditions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRelationshipsBetweenMetadataElements",
            description="Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse findRelationshipsBetweenMetadataElements(@PathVariable String                      serverName,
                                                                                         @PathVariable String                      urlMarker,
                                                                                         @RequestBody  FindRelationshipRequestBody requestBody)
    {
        return restAPI.findRelationshipsBetweenMetadataElements(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-guid/{relationshipGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipByGUID",
            description="Retrieve the relationship using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipResponse getRelationshipByGUID(@PathVariable String  serverName,
                                                                  @PathVariable String  relationshipGUID,
                                                                  @PathVariable String  urlMarker,
                                                                  @RequestBody (required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getRelationshipByGUID(serverName, relationshipGUID, urlMarker, requestBody);
    }



    /**
     * Retrieve all the versions of a relationship.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipGUID unique identifier of object to retrieve
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/relationships/{relationshipGUID}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipHistory",
            description="Retrieve all the versions of a relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getRelationshipHistory(@PathVariable String                 serverName,
                                                                       @PathVariable String                 urlMarker,
                                                                       @PathVariable String                 relationshipGUID,
                                                                       @RequestBody(required = false)
                                                                       HistoryRequestBody     requestBody)
    {
        return restAPI.getRelationshipHistory(serverName,
                                              urlMarker,
                                              relationshipGUID,
                                              requestBody);
    }
}
