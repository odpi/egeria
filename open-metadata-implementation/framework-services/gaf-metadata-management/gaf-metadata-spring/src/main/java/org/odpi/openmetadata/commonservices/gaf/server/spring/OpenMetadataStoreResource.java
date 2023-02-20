/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.gaf.properties.TranslationDetail;
import org.odpi.openmetadata.commonservices.gaf.properties.ValidMetadataValue;
import org.odpi.openmetadata.commonservices.gaf.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.gaf.server.OpenMetadataStoreRESTServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * OpenMetadataStoreResource supports the REST APIs for running Open Metadata Store Service
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLMarker}/open-metadata-store/users/{userId}")

@Tag(name="Open Metadata Store Services",
     description="Provides generic open metadata retrieval and management services for Open Metadata Access Services (OMASs).",
     externalDocs=@ExternalDocumentation(description="Open Metadata Store Services",
                                         url="https://egeria-project.org/services/gaf-metadata-management/"))


public class OpenMetadataStoreResource
{
    private final OpenMetadataStoreRESTServices restAPI = new OpenMetadataStoreRESTServices();

    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @GetMapping(path = "/metadata-elements/{elementGUID}")

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  serviceURLMarker,
                                                                @PathVariable String  userId,
                                                                @PathVariable String  elementGUID,
                                                                @RequestParam boolean forLineage,
                                                                @RequestParam boolean forDuplicateProcessing,
                                                                @RequestParam long    effectiveTime)
    {
        return restAPI.getMetadataElementByGUID(serverName, serviceURLMarker, userId, elementGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-unique-name")

    public OpenMetadataElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                      @PathVariable String          serviceURLMarker,
                                                                      @PathVariable String          userId,
                                                                      @RequestParam boolean         forLineage,
                                                                      @RequestParam boolean         forDuplicateProcessing,
                                                                      @RequestParam long            effectiveTime,
                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, serviceURLMarker, userId, forLineage, forDuplicateProcessing, effectiveTime, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/guid-by-unique-name")

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @PathVariable String          serviceURLMarker,
                                                           @PathVariable String          userId,
                                                           @RequestParam boolean         forLineage,
                                                           @RequestParam boolean         forDuplicateProcessing,
                                                           @RequestParam long            effectiveTime,
                                                           @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, serviceURLMarker, userId, forLineage, forDuplicateProcessing, effectiveTime, requestBody);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-string")

    public OpenMetadataElementsResponse findMetadataElementsWithString(@PathVariable String                  serverName,
                                                                       @PathVariable String                  serviceURLMarker,
                                                                       @PathVariable String                  userId,
                                                                       @RequestParam boolean                 forLineage,
                                                                       @RequestParam boolean                 forDuplicateProcessing,
                                                                       @RequestParam long                    effectiveTime,
                                                                       @RequestParam int                     startFrom,
                                                                       @RequestParam int                     pageSize,
                                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findMetadataElementsWithString(serverName, serviceURLMarker, userId, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @GetMapping(path = "/related-elements/{elementGUID}")

    public RelatedMetadataElementListResponse getAllRelatedMetadataElements(@PathVariable String  serverName,
                                                                            @PathVariable String  serviceURLMarker,
                                                                            @PathVariable String  userId,
                                                                            @PathVariable String  elementGUID,
                                                                            @RequestParam boolean forLineage,
                                                                            @RequestParam boolean forDuplicateProcessing,
                                                                            @RequestParam long    effectiveTime,
                                                                            @RequestParam int     startingAtEnd,
                                                                            @RequestParam int     startFrom,
                                                                            @RequestParam int     pageSize)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  serviceURLMarker,
                                                  userId,
                                                  elementGUID,
                                                  null,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  startingAtEnd,
                                                  startFrom,
                                                  pageSize);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @GetMapping(path = "/related-elements/{elementGUID}/type/{relationshipTypeName}")

    public RelatedMetadataElementListResponse getRelatedMetadataElements(@PathVariable String  serverName,
                                                                         @PathVariable String  serviceURLMarker,
                                                                         @PathVariable String  userId,
                                                                         @PathVariable String  elementGUID,
                                                                         @PathVariable String  relationshipTypeName,
                                                                         @RequestParam boolean forLineage,
                                                                         @RequestParam boolean forDuplicateProcessing,
                                                                         @RequestParam long    effectiveTime,
                                                                         @RequestParam int     startingAtEnd,
                                                                         @RequestParam int     startFrom,
                                                                         @RequestParam int     pageSize)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  serviceURLMarker,
                                                  userId,
                                                  elementGUID,
                                                  relationshipTypeName,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  effectiveTime,
                                                  startingAtEnd,
                                                  startFrom,
                                                  pageSize);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-specification")

    public OpenMetadataElementsResponse findMetadataElements(@PathVariable String          serverName,
                                                             @PathVariable String          serviceURLMarker,
                                                             @PathVariable String          userId,
                                                             @RequestParam boolean         forLineage,
                                                             @RequestParam boolean         forDuplicateProcessing,
                                                             @RequestParam long            effectiveTime,
                                                             @RequestParam int             startFrom,
                                                             @RequestParam int             pageSize,
                                                             @RequestBody  FindRequestBody requestBody)
    {
        return restAPI.findMetadataElements(serverName, serviceURLMarker, userId, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize, requestBody);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/by-search-specification")

    public RelatedMetadataElementsListResponse findRelationshipsBetweenMetadataElements(@PathVariable String          serverName,
                                                                                        @PathVariable String          serviceURLMarker,
                                                                                        @PathVariable String          userId,
                                                                                        @RequestParam boolean         forLineage,
                                                                                        @RequestParam boolean         forDuplicateProcessing,
                                                                                        @RequestParam long            effectiveTime,
                                                                                        @RequestParam int             startFrom,
                                                                                        @RequestParam int             pageSize,
                                                                                        @RequestBody  FindRequestBody requestBody)
    {
        return restAPI.findRelationshipsBetweenMetadataElements(serverName, serviceURLMarker, userId, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize, requestBody);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/new")

    public GUIDResponse createMetadataElementInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        serviceURLMarker,
                                                     @PathVariable String                        userId,
                                                     @RequestBody  NewMetadataElementRequestBody requestBody)
    {
        return restAPI.createMetadataElementInStore(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new properties
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the properties are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-properties")

    public VoidResponse updateMetadataElementInStore(@PathVariable String                      serverName,
                                                     @PathVariable String                      serviceURLMarker,
                                                     @PathVariable String                      userId,
                                                     @PathVariable String                      metadataElementGUID,
                                                     @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementInStore(serverName, serviceURLMarker, userId, metadataElementGUID, requestBody);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-status")

    public VoidResponse updateMetadataElementStatusInStore(@PathVariable String                  serverName,
                                                           @PathVariable String                  serviceURLMarker,
                                                           @PathVariable String                  userId,
                                                           @PathVariable String                  metadataElementGUID,
                                                           @RequestBody  UpdateStatusRequestBody requestBody)
    {
        return restAPI.updateMetadataElementStatusInStore(serverName, serviceURLMarker, userId, metadataElementGUID, requestBody);
    }



    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/update-effectivity")

    public VoidResponse updateMetadataElementEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            serviceURLMarker,
                                                                @PathVariable String                            userId,
                                                                @PathVariable String                            metadataElementGUID,
                                                                @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementEffectivityInStore(serverName, serviceURLMarker, userId, metadataElementGUID, requestBody);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/delete")

    public  VoidResponse deleteMetadataElementInStore(@PathVariable String            serverName,
                                                      @PathVariable String            serviceURLMarker,
                                                      @PathVariable String            userId,
                                                      @PathVariable String            metadataElementGUID,
                                                      @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.deleteMetadataElementInStore(serverName, serviceURLMarker, userId, metadataElementGUID, requestBody);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param requestBody properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/new")

    public VoidResponse classifyMetadataElementInStore(@PathVariable String                       serverName,
                                                       @PathVariable String                       serviceURLMarker,
                                                       @PathVariable String                       userId,
                                                       @PathVariable String                       metadataElementGUID,
                                                       @PathVariable String                       classificationName,
                                                       @RequestBody  NewClassificationRequestBody requestBody)
    {
        return restAPI.classifyMetadataElementInStore(serverName, serviceURLMarker, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody new properties for the classification
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-properties")

    public VoidResponse reclassifyMetadataElementInStore(@PathVariable String                      serverName,
                                                         @PathVariable String                      serviceURLMarker,
                                                         @PathVariable String                      userId,
                                                         @PathVariable String                      metadataElementGUID,
                                                         @PathVariable String                      classificationName,
                                                         @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.reclassifyMetadataElementInStore(serverName, serviceURLMarker, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-effectivity")

    public VoidResponse updateClassificationEffectivityInStore(@PathVariable String                            serverName,
                                                               @PathVariable String                            serviceURLMarker,
                                                               @PathVariable String                            userId,
                                                               @PathVariable String                            metadataElementGUID,
                                                               @PathVariable String                            classificationName,
                                                               @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateClassificationEffectivityInStore(serverName, serviceURLMarker, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to remove this classification
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/delete")

    public VoidResponse unclassifyMetadataElementInStore(@PathVariable String            serverName,
                                                         @PathVariable String            serviceURLMarker,
                                                         @PathVariable String            userId,
                                                         @PathVariable String            metadataElementGUID,
                                                         @PathVariable String            classificationName,
                                                         @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.unclassifyMetadataElementInStore(serverName, serviceURLMarker, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody the properties of the relationship
     *
     * @return unique identifier of the new relationship or
     *
     *  InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/new")

    public GUIDResponse createRelatedElementsInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        serviceURLMarker,
                                                     @PathVariable String                        userId,
                                                     @RequestBody  NewRelatedElementsRequestBody requestBody)
    {
        return restAPI.createRelatedElementsInStore(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody new properties for the relationship
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to update this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/update-properties")

    public VoidResponse updateRelatedElementsInStore(@PathVariable String                      serverName,
                                                     @PathVariable String                      serviceURLMarker,
                                                     @PathVariable String                      userId,
                                                     @PathVariable String                      relationshipGUID,
                                                     @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsInStore(serverName, serviceURLMarker, userId, relationshipGUID, requestBody);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/update-effectivity")

    public VoidResponse updateRelatedElementsEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            serviceURLMarker,
                                                                @PathVariable String                            userId,
                                                                @PathVariable String                            relationshipGUID,
                                                                @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsEffectivityInStore(serverName, serviceURLMarker, userId, relationshipGUID, requestBody);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/related-elements/{relationshipGUID}/delete")

    public VoidResponse deleteRelatedElementsInStore(@PathVariable String            serverName,
                                                     @PathVariable String            serviceURLMarker,
                                                     @PathVariable String            userId,
                                                     @PathVariable String            relationshipGUID,
                                                     @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.deleteRelatedElementsInStore(serverName, serviceURLMarker, userId, relationshipGUID, requestBody);
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody properties for the new incident report
     *
     * @return unique identifier of the resulting incident report or
     *
     *  InvalidParameterException null or non-unique qualified name for the incident report
     *  UserNotAuthorizedException this governance action service is not authorized to create an incident report
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/incident-reports")

    public GUIDResponse createIncidentReport(@PathVariable String                    serverName,
                                             @PathVariable String                    serviceURLMarker,
                                             @PathVariable String                    userId,
                                             @RequestBody  IncidentReportRequestBody requestBody)
    {
        return restAPI.createIncidentReport(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Create a To-Do request for someone to work on.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param requestBody unique name for the to do and other characteristics
     *
     * @return unique identifier of new to do element or
     * InvalidParameterException either todoQualifiedName or assignedTo are null or not recognized
     * UserNotAuthorizedException the governance action service is not authorized to create a to-do
     * PropertyServerException there is a problem connecting to (or inside) the metadata store
     */
    @PostMapping(path = "/to-dos")

    public GUIDResponse openToDo(@PathVariable String          serverName,
                                 @PathVariable String          serviceURLMarker,
                                 @PathVariable String          userId,
                                 @RequestBody  ToDoRequestBody requestBody)
    {
        return restAPI.openToDo(serverName, serviceURLMarker, userId, requestBody);
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @return void or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/multi-language/set-translation/{elementGUID}")

    public VoidResponse setTranslation(@PathVariable String            serverName,
                                       @PathVariable String            serviceURLMarker,
                                       @PathVariable String            userId,
                                       @PathVariable String            elementGUID,
                                       @RequestBody  TranslationDetail translationDetail)
    {
        return restAPI.setTranslation(serverName, serviceURLMarker, userId, elementGUID, translationDetail);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the language is null or not known or not unique (add locale)
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/multi-language/clear-translation/{elementGUID}")

    public VoidResponse clearTranslation(@PathVariable String          serverName,
                                         @PathVariable String          serviceURLMarker,
                                         @PathVariable String          userId,
                                         @PathVariable String          elementGUID,
                                         @RequestParam(required = false)
                                                       String language,
                                         @RequestParam(required = false)
                                                       String locale,
                                         @RequestBody (required=false)
                                                       NullRequestBody requestBody)
    {
        return restAPI.clearTranslation(serverName, serviceURLMarker, userId, elementGUID, language, locale, requestBody);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/multi-language/get-translation/{elementGUID}")

    public TranslationDetailResponse getTranslation(@PathVariable String serverName,
                                                    @PathVariable String serviceURLMarker,
                                                    @PathVariable String userId,
                                                    @PathVariable String elementGUID,
                                                    @RequestParam(required = false)
                                                                  String language,
                                                    @RequestParam(required = false)
                                                                  String locale)
    {
        return restAPI.getTranslation(serverName, serviceURLMarker, userId, elementGUID, language, locale);
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none or
     * InvalidParameterException  the unique identifier is null or not known.
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/multi-language/get-translations/{elementGUID}")

    public TranslationListResponse getTranslations(@PathVariable String serverName,
                                                   @PathVariable String serviceURLMarker,
                                                   @PathVariable String userId,
                                                   @PathVariable String elementGUID,
                                                   @RequestParam int    startFrom,
                                                   @RequestParam int    pageSize)
    {
        return restAPI.getTranslations(serverName, serviceURLMarker, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/valid-metadata-values/setup-value/{propertyName}")

    public VoidResponse setUpValidMetadataValue(@PathVariable String             serverName,
                                                @PathVariable String             serviceURLMarker,
                                                @PathVariable String             userId,
                                                @RequestParam(required = false)
                                                              String             typeName,
                                                @PathVariable String             propertyName,
                                                @RequestBody  ValidMetadataValue validMetadataValue)
    {
        return restAPI.setUpValidMetadataValue(serverName, serviceURLMarker, userId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     *
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/valid-metadata-values/setup-map-name/{propertyName}")

    public VoidResponse setUpValidMetadataMapName(@PathVariable String             serverName,
                                                  @PathVariable String             serviceURLMarker,
                                                  @PathVariable String             userId,
                                                  @RequestParam(required = false)
                                                                String             typeName,
                                                  @PathVariable String             propertyName,
                                                  @RequestBody  ValidMetadataValue validMetadataValue)
    {
        return restAPI.setUpValidMetadataMapName(serverName, serviceURLMarker, userId, typeName, propertyName, validMetadataValue);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     *
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/valid-metadata-values/setup-map-value/{propertyName}/{mapName}")

    public VoidResponse setUpValidMetadataMapValue(@PathVariable String             serverName,
                                                   @PathVariable String             serviceURLMarker,
                                                   @PathVariable String             userId,
                                                   @RequestParam(required = false)
                                                                 String             typeName,
                                                   @PathVariable String             propertyName,
                                                   @PathVariable String             mapName,
                                                   @RequestBody  ValidMetadataValue validMetadataValue)
    {
        return restAPI.setUpValidMetadataMapValue(serverName, serviceURLMarker, userId, typeName, propertyName, mapName, validMetadataValue);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/valid-metadata-values/clear-value/{propertyName}")

    public VoidResponse clearValidMetadataValue(@PathVariable String          serverName,
                                                @PathVariable String          serviceURLMarker,
                                                @PathVariable String          userId,
                                                @RequestParam(required = false)
                                                              String          typeName,
                                                @PathVariable String          propertyName,
                                                @RequestParam(required = false)
                                                              String          preferredValue,
                                                @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.clearValidMetadataValue(serverName, serviceURLMarker, userId, typeName, propertyName, preferredValue, requestBody);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/valid-metadata-values/clear-map-name/{propertyName}")

    public VoidResponse clearValidMetadataMapName(@PathVariable String          serverName,
                                                  @PathVariable String          serviceURLMarker,
                                                  @PathVariable String          userId,
                                                  @RequestParam(required = false)
                                                                String          typeName,
                                                  @PathVariable String          propertyName,
                                                  @RequestParam(required = false)
                                                                String          preferredValue,
                                                  @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.clearValidMetadataMapName(serverName, serviceURLMarker, userId, typeName, propertyName, preferredValue, requestBody);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/valid-metadata-values/clear-map-value/{propertyName}/{mapName}")

    public VoidResponse clearValidMetadataMapValue(@PathVariable String          serverName,
                                                   @PathVariable String          serviceURLMarker,
                                                   @PathVariable String          userId,
                                                   @RequestParam(required = false)
                                                           String          typeName,
                                                   @PathVariable String          propertyName,
                                                   @PathVariable String          mapName,
                                                   @RequestParam(required = false)
                                                           String          preferredValue,
                                                   NullRequestBody requestBody)
    {
        return restAPI.clearValidMetadataMapValue(serverName, serviceURLMarker, userId, typeName, propertyName, mapName, preferredValue, requestBody);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/validate-value/{propertyName}")

    public BooleanResponse validateMetadataValue(@PathVariable String serverName,
                                                 @PathVariable String serviceURLMarker,
                                                 @PathVariable String userId,
                                                 @RequestParam(required = false)
                                                               String typeName,
                                                 @PathVariable String propertyName,
                                                 @RequestParam String actualValue)
    {
        return restAPI.validateMetadataValue(serverName, serviceURLMarker, userId, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/validate-map-name/{propertyName}")

    public BooleanResponse validateMetadataMapName(@PathVariable String serverName,
                                                   @PathVariable String serviceURLMarker,
                                                   @PathVariable String userId,
                                                   @RequestParam(required = false)
                                                                 String typeName,
                                                   @PathVariable String propertyName,
                                                   @RequestParam String actualValue)
    {
        return restAPI.validateMetadataMapName(serverName, serviceURLMarker, userId, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/validate-map-value/{propertyName}/{mapName}")

    public BooleanResponse validateMetadataMapValue(@PathVariable String serverName,
                                                    @PathVariable String serviceURLMarker,
                                                    @PathVariable String userId,
                                                    @RequestParam(required = false)
                                                                  String typeName,
                                                    @PathVariable String propertyName,
                                                    @PathVariable String mapName,
                                                    @RequestParam String actualValue)
    {
        return restAPI.validateMetadataMapValue(serverName, serviceURLMarker, userId, typeName, propertyName, mapName, actualValue);
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/get-value/{propertyName}")

    public ValidMetadataValueResponse getValidMetadataValue(@PathVariable String serverName,
                                                            @PathVariable String serviceURLMarker,
                                                            @PathVariable String userId,
                                                            @RequestParam(required = false)
                                                                          String typeName,
                                                            @PathVariable String propertyName,
                                                            @RequestParam String preferredValue)
    {
        return restAPI.getValidMetadataValue(serverName, serviceURLMarker, userId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/get-map-name/{propertyName}")

    public ValidMetadataValueResponse getValidMetadataMapName(@PathVariable String serverName,
                                                              @PathVariable String serviceURLMarker,
                                                              @PathVariable String userId,
                                                              @RequestParam(required = false)
                                                                            String typeName,
                                                              @PathVariable String propertyName,
                                                              @RequestParam String preferredValue)
    {
        return  restAPI.getValidMetadataMapName(serverName, serviceURLMarker, userId, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/get-map-value/{propertyName}/{mapName}")

    public ValidMetadataValueResponse getValidMetadataMapValue(@PathVariable String serverName,
                                                               @PathVariable String serviceURLMarker,
                                                               @PathVariable String userId,
                                                               @RequestParam(required = false)
                                                                             String typeName,
                                                               @PathVariable String propertyName,
                                                               @PathVariable String mapName,
                                                               @RequestParam String preferredValue)
    {
        return restAPI.getValidMetadataMapValue(serverName, serviceURLMarker, userId, typeName, propertyName, mapName, preferredValue);
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param serviceURLMarker the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId caller's userId
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/valid-metadata-values/get-valid-metadata-values/{propertyName}")

    public ValidMetadataValueListResponse getValidMetadataValues(@PathVariable String serverName,
                                                                 @PathVariable String serviceURLMarker,
                                                                 @PathVariable String userId,
                                                                 @RequestParam(required = false)
                                                                               String typeName,
                                                                 @PathVariable String propertyName,
                                                                 @RequestParam int    startFrom,
                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getValidMetadataValues(serverName, serviceURLMarker, userId, typeName, propertyName, startFrom, pageSize);
    }


    /**
     * Log an audit message about an asset.
     *
     * @param serverName            name of server instance to route request to
     * @param serviceURLMarker      the identifier of the access service (for example asset-owner for the Asset Owner OMAS)
     * @param userId                userId of user making request.
     * @param assetGUID             unique identifier for asset.
     * @param callingService     unique name for governance service.
     * @param message               message to log
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records/{callingService}")

    public VoidResponse logAssetAuditMessage(@PathVariable String serverName,
                                             @PathVariable String serviceURLMarker,
                                             @PathVariable String userId,
                                             @PathVariable String assetGUID,
                                             @PathVariable String callingService,
                                             @RequestBody  String message)
    {
        return restAPI.logAssetAuditMessage(serverName, serviceURLMarker, userId, assetGUID, callingService, message);
    }
}
