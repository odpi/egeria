/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.governanceengine.rest.*;
import org.odpi.openmetadata.accessservices.governanceengine.rest.PeerDuplicatesRequestBody;
import org.odpi.openmetadata.accessservices.governanceengine.server.GovernanceEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.ConnectionResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * GovernanceEngineOMASResource supports the REST APIs for running Governance Action Service
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/governance-engine/users/{userId}")

@Tag(name="Governance Engine OMAS",
     description="The Governance Engine Open Metadata Access Service (OMAS) provides support for governance engines, services and actions.",
     externalDocs=@ExternalDocumentation(description="Governance Engine Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/governance-engine/overview/"))

public class GovernanceEngineOMASResource
{

    private final GovernanceEngineRESTServices restAPI = new GovernanceEngineRESTServices();


    /**
     * Return the connection object for the Governance Engine OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the topic connection.
     */
    @GetMapping(path = "/topics/out-topic-connection/{callerId}")

    public ConnectionResponse getOutTopicConnection(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @PathVariable String callerId)
    {
        return restAPI.getOutTopicConnection(serverName, userId, callerId);
    }


    /**
     * Log an audit message about an asset.
     *
     * @param serverName            name of server instance to route request to
     * @param userId                userId of user making request.
     * @param assetGUID             unique identifier for asset.
     * @param governanceService     unique name for governance service.
     * @param message               message to log
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem that occurred within the property server.
     */
    @PostMapping(path = "/assets/{assetGUID}/log-records/{governanceService}")

    public VoidResponse logAssetAuditMessage(@PathVariable String serverName,
                                             @PathVariable String userId,
                                             @PathVariable String assetGUID,
                                             @PathVariable String governanceService,
                                             @RequestBody  String message)
    {
        return restAPI.logAssetAuditMessage(serverName, userId, assetGUID, governanceService, message);
    }



    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
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
    @GetMapping(path = "/open-metadata-store/metadata-elements/{elementGUID}")

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  userId,
                                                                @PathVariable String  elementGUID,
                                                                @RequestParam boolean forLineage,
                                                                @RequestParam boolean forDuplicateProcessing,
                                                                @RequestParam long    effectiveTime)
    {
        return restAPI.getMetadataElementByGUID(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, effectiveTime);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/by-unique-name")

    public OpenMetadataElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                      @PathVariable String          userId,
                                                                      @RequestParam boolean         forLineage,
                                                                      @RequestParam boolean         forDuplicateProcessing,
                                                                      @RequestParam long            effectiveTime,
                                                                      @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, userId, forLineage, forDuplicateProcessing, effectiveTime, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/guid-by-unique-name")

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @PathVariable String          userId,
                                                           @RequestParam boolean         forLineage,
                                                           @RequestParam boolean         forDuplicateProcessing,
                                                           @RequestParam long            effectiveTime,
                                                           @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, userId, forLineage, forDuplicateProcessing, effectiveTime, requestBody);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/by-search-string")

    public OpenMetadataElementsResponse findMetadataElementsWithString(@PathVariable String                  serverName,
                                                                       @PathVariable String                  userId,
                                                                       @RequestParam boolean                 forLineage,
                                                                       @RequestParam boolean                 forDuplicateProcessing,
                                                                       @RequestParam long                    effectiveTime,
                                                                       @RequestParam int                     startFrom,
                                                                       @RequestParam int                     pageSize,
                                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findMetadataElementsWithString(serverName, userId, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
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
    @GetMapping(path = "/open-metadata-store/related-elements/{elementGUID}/type/{relationshipTypeName}")

    public RelatedMetadataElementListResponse getRelatedMetadataElements(@PathVariable String  serverName,
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/by-search-specification")

    public OpenMetadataElementsResponse findMetadataElements(@PathVariable String          serverName,
                                                             @PathVariable String          userId,
                                                             @RequestParam boolean         forLineage,
                                                             @RequestParam boolean         forDuplicateProcessing,
                                                             @RequestParam long            effectiveTime,
                                                             @RequestParam int             startFrom,
                                                             @RequestParam int             pageSize,
                                                             @RequestBody  FindRequestBody requestBody)
    {
        return restAPI.findMetadataElements(serverName, userId, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize, requestBody);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/related-elements/by-search-specification")

    public RelatedMetadataElementsListResponse findRelationshipsBetweenMetadataElements(@PathVariable String          serverName,
                                                                                        @PathVariable String          userId,
                                                                                        @RequestParam boolean         forLineage,
                                                                                        @RequestParam boolean         forDuplicateProcessing,
                                                                                        @RequestParam long            effectiveTime,
                                                                                        @RequestParam int             startFrom,
                                                                                        @RequestParam int             pageSize,
                                                                                        @RequestBody  FindRequestBody requestBody)
    {
        return restAPI.findRelationshipsBetweenMetadataElements(serverName, userId, forLineage, forDuplicateProcessing, effectiveTime, startFrom, pageSize, requestBody);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/open-metadata-store/metadata-elements/new")

    public GUIDResponse createMetadataElementInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @RequestBody  NewMetadataElementRequestBody requestBody)
    {
        return restAPI.createMetadataElementInStore(serverName, userId, requestBody);
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/update-properties")

    public VoidResponse updateMetadataElementInStore(@PathVariable String                      serverName,
                                                     @PathVariable String                      userId,
                                                     @PathVariable String                      metadataElementGUID,
                                                     @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Update the status of specific metadata element. The new status must match a status value that is defined for the element's type
     * assigned when it was created.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/update-status")

    public VoidResponse updateMetadataElementStatusInStore(@PathVariable String                  serverName,
                                                           @PathVariable String                  userId,
                                                           @PathVariable String                  metadataElementGUID,
                                                           @RequestBody  UpdateStatusRequestBody requestBody)
    {
        return restAPI.updateMetadataElementStatusInStore(serverName, userId, metadataElementGUID, requestBody);
    }



    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/update-effectivity")

    public VoidResponse updateMetadataElementEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            userId,
                                                                @PathVariable String                            metadataElementGUID,
                                                                @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateMetadataElementEffectivityInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Delete a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/delete")

    public  VoidResponse deleteMetadataElementInStore(@PathVariable String            serverName,
                                                      @PathVariable String            userId,
                                                      @PathVariable String            metadataElementGUID,
                                                      @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.deleteMetadataElementInStore(serverName, userId, metadataElementGUID, requestBody);
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/new")

    public VoidResponse classifyMetadataElementInStore(@PathVariable String                       serverName,
                                                       @PathVariable String                       userId,
                                                       @PathVariable String                       metadataElementGUID,
                                                       @PathVariable String                       classificationName,
                                                       @RequestBody  NewClassificationRequestBody requestBody)
    {
        return restAPI.classifyMetadataElementInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-properties")

    public VoidResponse reclassifyMetadataElementInStore(@PathVariable String                      serverName,
                                                         @PathVariable String                      userId,
                                                         @PathVariable String                      metadataElementGUID,
                                                         @PathVariable String                      classificationName,
                                                         @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.reclassifyMetadataElementInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/update-effectivity")

    public VoidResponse updateClassificationEffectivityInStore(@PathVariable String                            serverName,
                                                               @PathVariable String                            userId,
                                                               @PathVariable String                            metadataElementGUID,
                                                               @PathVariable String                            classificationName,
                                                               @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateClassificationEffectivityInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/metadata-elements/{metadataElementGUID}/classifications/{classificationName}/delete")

    public VoidResponse unclassifyMetadataElementInStore(@PathVariable String            serverName,
                                                         @PathVariable String            userId,
                                                         @PathVariable String            metadataElementGUID,
                                                         @PathVariable String            classificationName,
                                                         @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.unclassifyMetadataElementInStore(serverName, userId, metadataElementGUID, classificationName, requestBody);
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/related-elements/new")

    public GUIDResponse createRelatedElementsInStore(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @RequestBody  NewRelatedElementsRequestBody requestBody)
    {
        return restAPI.createRelatedElementsInStore(serverName, userId, requestBody);
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/related-elements/{relationshipGUID}/update-properties")

    public VoidResponse updateRelatedElementsInStore(@PathVariable String                      serverName,
                                                     @PathVariable String                      userId,
                                                     @PathVariable String                      relationshipGUID,
                                                     @RequestBody  UpdatePropertiesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsInStore(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/related-elements/{relationshipGUID}/update-effectivity")

    public VoidResponse updateRelatedElementsEffectivityInStore(@PathVariable String                            serverName,
                                                                @PathVariable String                            userId,
                                                                @PathVariable String                            relationshipGUID,
                                                                @RequestBody  UpdateEffectivityDatesRequestBody requestBody)
    {
        return restAPI.updateRelatedElementsEffectivityInStore(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
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
    @PostMapping(path = "/open-metadata-store/related-elements/{relationshipGUID}/delete")

    public VoidResponse deleteRelatedElementsInStore(@PathVariable String            serverName,
                                                     @PathVariable String            userId,
                                                     @PathVariable String            relationshipGUID,
                                                     @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.deleteRelatedElementsInStore(serverName, userId, relationshipGUID, requestBody);
    }


    /**
     * Update the status of a specific action target. By default, these values are derived from
     * the values for the governance action service.  However, if the governance action service has to process name
     * target elements, then setting the status on each individual target will show the progress of the
     * governance action service.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody relationship properties
     *
     * @return void or
     *
     *  InvalidParameterException the action target GUID is not recognized
     *  UserNotAuthorizedException the governance action service is not authorized to update the action target properties
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    @PostMapping(path = "/governance-actions/action-targets/update")

    public VoidResponse updateActionTargetStatus(@PathVariable String                        serverName,
                                                 @PathVariable String                        userId,
                                                 @RequestBody  ActionTargetStatusRequestBody requestBody)
    {
        return restAPI.updateActionTargetStatus(serverName, userId, requestBody);
    }


    /**
     * Update the status of the governance action - providing the caller is permitted.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param requestBody new status ordinal
     *
     * @return void or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/status/update")

    public VoidResponse updateGovernanceActionStatus(@PathVariable String            serverName,
                                                     @PathVariable String            userId,
                                                     @PathVariable String            governanceActionGUID,
                                                     @RequestBody  StatusRequestBody requestBody)
    {
        return restAPI.updateGovernanceActionStatus(serverName, userId, governanceActionGUID, requestBody);
    }


    /**
     * Declare that all the processing for the governance action service is finished and the status of the work.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceActionGUID unique identifier of the governance action to update
     * @param requestBody completion status enum value, optional guard strings for triggering subsequent action(s) plus
     *                    a list of additional elements to add to the action targets for the next phase
     *
     * @return void or
     *
     *  InvalidParameterException the completion status is null
     *  UserNotAuthorizedException the governance action service is not authorized to update the governance action service status
     *  PropertyServerException there is a problem connecting to the metadata store
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/completion-status")

    public VoidResponse recordCompletionStatus(@PathVariable String                      serverName,
                                               @PathVariable String                      userId,
                                               @PathVariable String                      governanceActionGUID,
                                               @RequestBody  CompletionStatusRequestBody requestBody)
    {
        return restAPI.recordCompletionStatus(serverName, userId, governanceActionGUID, requestBody);
    }


    /**
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestBody properties for the governance action and to pass to the governance action service
     *
     * @return unique identifier of the governance action or
     *
     *  InvalidParameterException null qualified name
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-engines/{governanceEngineName}/governance-actions/initiate")

    public GUIDResponse initiateGovernanceAction(@PathVariable String                      serverName,
                                                 @PathVariable String                      userId,
                                                 @PathVariable String                      governanceEngineName,
                                                 @RequestBody  GovernanceActionRequestBody requestBody)
    {
        return restAPI.initiateGovernanceAction(serverName, userId, governanceEngineName, requestBody);
    }


    /**
     * Using the named governance action process as a template, initiate a chain of governance actions.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties to initiate the new instance of the process
     *
     * @return unique identifier of the first governance action of the process or
     *
     *  InvalidParameterException null or unrecognized qualified name of the process
     *  UserNotAuthorizedException this governance action service is not authorized to create a governance action process
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/governance-action-processes/initiate")

    public GUIDResponse initiateGovernanceActionProcess(@PathVariable String                             serverName,
                                                        @PathVariable String                             userId,
                                                        @RequestBody  GovernanceActionProcessRequestBody requestBody)
    {
        return restAPI.initiateGovernanceActionProcess(serverName, userId, requestBody);
    }


    /**
     * Create an incident report to capture the situation detected by this governance action service.
     * This incident report will be processed by other governance activities.
     *
     * @param serverName     name of server instance to route request to
     * @param userId caller's userId
     * @param requestBody properties for the new incident report
     *
     * @return unique identifier of the resulting incident report or
     *
     *  InvalidParameterException null or non-unique qualified name for the incident report
     *  UserNotAuthorizedException this governance action service is not authorized to create a incident report
     *  PropertyServerException there is a problem with the metadata store
     */
    @PostMapping(path = "/incident-reports")

    public GUIDResponse createIncidentReport(@PathVariable String                    serverName,
                                             @PathVariable String                    userId,
                                             @RequestBody  IncidentReportRequestBody requestBody)
    {
        return restAPI.createIncidentReport(serverName, userId, requestBody);
    }



    /**
     * Create a simple relationship between two elements. If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/open-metadata-store/related-elements/link-as-peer-duplicate")

    public VoidResponse linkElementsAsPeerDuplicates(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @RequestBody  PeerDuplicatesRequestBody requestBody)
    {
        return restAPI.linkElementsAsDuplicates(serverName, userId, requestBody);
    }


    /**
     * Create a simple relationship between two elements. If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/open-metadata-store/related-elements/link-as-consolidated-duplicate")

    public VoidResponse linkConsolidatedDuplicate(@PathVariable String                            serverName,
                                                  @PathVariable String                            userId,
                                                  @RequestBody  ConsolidatedDuplicatesRequestBody requestBody)
    {
        return restAPI.linkConsolidatedDuplicate(serverName, userId, requestBody);
    }


    /**
     * Request the status and properties of an executing governance action request.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @return governance action properties and status or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-actions/{governanceActionGUID}")

    public GovernanceActionElementResponse getGovernanceAction(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String governanceActionGUID)
    {
        return restAPI.getGovernanceAction(serverName, userId, governanceActionGUID);
    }


    /**
     * Request that execution of a governance action is allocated to the caller.
     *
     * @param serverName     name of server instance to route request to
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     * @param requestBody null request body
     *
     * @return void or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @PostMapping(path = "/governance-actions/{governanceActionGUID}/claim")

    public VoidResponse claimGovernanceAction(@PathVariable                  String          serverName,
                                              @PathVariable                  String          userId,
                                              @PathVariable                  String          governanceActionGUID,
                                              @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.claimGovernanceAction(serverName, userId, governanceActionGUID, requestBody);
    }


    /**
     * Retrieve the governance actions that are known to the server.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-actions")

    public GovernanceActionElementsResponse getGovernanceActions(@PathVariable String serverName,
                                                                 @PathVariable String userId,
                                                                 @RequestParam int    startFrom,
                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getGovernanceActions(serverName, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the governance actions that are still in process.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-actions/active")

    public GovernanceActionElementsResponse getActiveGovernanceActions(@PathVariable String serverName,
                                                                       @PathVariable String userId,
                                                                       @RequestParam int    startFrom,
                                                                       @RequestParam int    pageSize)
    {
        return restAPI.getActiveGovernanceActions(serverName, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the governance actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param serverName     name of server instance to route request to
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     *
     * @return list of governance action elements or
     *
     *  InvalidParameterException one of the parameters is null or invalid.
     *  UserNotAuthorizedException user not authorized to issue this request.
     *  PropertyServerException there was a problem detected by the metadata store.
     */
    @GetMapping(path = "/governance-engines/{governanceEngineGUID}/active-governance-actions")

    public GovernanceActionElementsResponse getActiveClaimedGovernanceActions(@PathVariable String serverName,
                                                                              @PathVariable String userId,
                                                                              @PathVariable String governanceEngineGUID,
                                                                              @RequestParam int    startFrom,
                                                                              @RequestParam int    pageSize)
    {
        return restAPI.getActiveClaimedGovernanceActions(serverName, userId, governanceEngineGUID, startFrom, pageSize);
    }
}
