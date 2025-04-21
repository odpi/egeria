/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetmanager.rest.AssetElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.server.StewardshipExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementStubsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelatedElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The StewardshipExchangeResource provides part of the server-side implementation of the Asset Manager Open Metadata
 * Assess Service (OMAS).  This interface provides the ability to add governance related classifications and relationship to elements.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Metadata Access Server: Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/asset-manager/overview/"))

public class StewardshipExchangeResource
{
    private final StewardshipExchangeRESTServices restAPI = new StewardshipExchangeRESTServices();

    /**
     * Default constructor
     */
    public StewardshipExchangeResource()
    {
    }


    /**
     * Return information about the elements classified with the data field classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-data-field")

    public ElementStubsResponse getDataFieldClassifiedElements(@PathVariable String                      serverName,
                                                               @PathVariable String                      userId,
                                                               @RequestParam(required = false, defaultValue = "0")
                                                                             int                         startFrom,
                                                               @RequestParam(required = false, defaultValue = "0")
                                                                             int                         pageSize,
                                                               @RequestParam(required = false, defaultValue = "false")
                                                                             boolean                     forLineage,
                                                               @RequestParam(required = false, defaultValue = "false")
                                                                             boolean                     forDuplicateProcessing,
                                                               @RequestBody(required = false)
                                                                             FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getDataFieldClassifiedElements(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidence")

    public VoidResponse setConfidenceClassification(@PathVariable String                    serverName,
                                                    @PathVariable String                    userId,
                                                    @PathVariable String                    elementGUID,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                                  boolean                   forLineage,
                                                    @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                   forDuplicateProcessing,
                                                    @RequestBody  (required = false)
                                                                  ClassificationRequestBody requestBody)
    {
        return restAPI.setConfidenceClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidence/remove")

    public VoidResponse clearConfidenceClassification(@PathVariable String                    serverName,
                                                      @PathVariable String                    userId,
                                                      @PathVariable String                    elementGUID,
                                                      @RequestParam(required = false, defaultValue = "false")
                                                      boolean                   forLineage,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                      boolean                   forDuplicateProcessing,
                                                      @RequestBody  (required = false)
                                                      ClassificationRequestBody requestBody)
    {
        return restAPI.clearConfidenceClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/criticality")

    public VoidResponse setCriticalityClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestParam(required = false, defaultValue = "false")
                                                     boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.setCriticalityClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/criticality/remove")

    public VoidResponse clearCriticalityClassification(@PathVariable String                    serverName,
                                                       @PathVariable String                    userId,
                                                       @PathVariable String                    elementGUID,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                       boolean                   forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                       boolean                   forDuplicateProcessing,
                                                       @RequestBody  (required = false)
                                                       ClassificationRequestBody requestBody)
    {
        return restAPI.clearCriticalityClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidentiality")

    public VoidResponse setConfidentialityClassification(@PathVariable String                    serverName,
                                                         @PathVariable String                    userId,
                                                         @PathVariable String                    elementGUID,
                                                         @RequestParam(required = false, defaultValue = "false")
                                                         boolean                   forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                         boolean                   forDuplicateProcessing,
                                                         @RequestBody  (required = false)
                                                         ClassificationRequestBody requestBody)
    {
        return restAPI.setConfidentialityClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidentiality/remove")

    public VoidResponse clearConfidentialityClassification(@PathVariable String                    serverName,
                                                           @PathVariable String                    userId,
                                                           @PathVariable String                    elementGUID,
                                                           @RequestParam(required = false, defaultValue = "false")
                                                           boolean                   forLineage,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean                   forDuplicateProcessing,
                                                           @RequestBody  (required = false)
                                                           ClassificationRequestBody requestBody)
    {
        return restAPI.clearConfidentialityClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/retention")

    public VoidResponse setRetentionClassification(@PathVariable String                    serverName,
                                                   @PathVariable String                    userId,
                                                   @PathVariable String                    elementGUID,
                                                   @RequestParam(required = false, defaultValue = "false")
                                                   boolean                   forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                   boolean                   forDuplicateProcessing,
                                                   @RequestBody  (required = false)
                                                   ClassificationRequestBody requestBody)
    {
        return restAPI.setRetentionClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/retention/remove")

    public VoidResponse clearRetentionClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestParam(required = false, defaultValue = "false")
                                                     boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.clearRetentionClassification(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Add or replace the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
     * @param elementGUID unique identifier of element to attach to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags")

    public VoidResponse addSecurityTags(@PathVariable String                    serverName,
                                        @PathVariable String                    userId,
                                        @PathVariable String                    elementGUID,
                                        @RequestParam(required = false, defaultValue = "false")
                                        boolean                   forLineage,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                   forDuplicateProcessing,
                                        @RequestBody  (required = false)
                                        ClassificationRequestBody requestBody)
    {
        return restAPI.addSecurityTags(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
     * @param elementGUID   unique identifier of element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags/remove")

    public VoidResponse removeSecurityTags(@PathVariable String          serverName,
                                           @PathVariable String          userId,
                                           @PathVariable String          elementGUID,
                                           @RequestParam (required = false, defaultValue = "false")
                                           boolean                   forLineage,
                                           @RequestParam (required = false, defaultValue = "false")
                                           boolean                   forDuplicateProcessing,
                                           @RequestBody(required = false)
                                           ClassificationRequestBody requestBody)
    {
        return restAPI.removeSecurityTags(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Add or replace the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/ownership")

    public VoidResponse addOwnership(@PathVariable String                    serverName,
                                     @PathVariable String                    userId,
                                     @PathVariable String                    elementGUID,
                                     @RequestParam(required = false, defaultValue = "false")
                                     boolean                   forLineage,
                                     @RequestParam (required = false, defaultValue = "false")
                                     boolean                   forDuplicateProcessing,
                                     @RequestBody  (required = false)
                                     ClassificationRequestBody requestBody)
    {
        return restAPI.addOwnership(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/ownership/remove")

    public VoidResponse clearOwnership(@PathVariable String                    serverName,
                                       @PathVariable String                    userId,
                                       @PathVariable String                    elementGUID,
                                       @RequestParam(required = false, defaultValue = "false")
                                       boolean                   forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                       boolean                   forDuplicateProcessing,
                                       @RequestBody  (required = false)
                                       ClassificationRequestBody requestBody)
    {
        return restAPI.clearOwnership(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Add or replace the origin classification for an asset.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID element to link it to - its type must inherit from Asset.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/origin")

    public VoidResponse addAssetOrigin(@PathVariable String                    serverName,
                                       @PathVariable String                    userId,
                                       @PathVariable String                    assetGUID,
                                       @RequestParam(required = false, defaultValue = "false")
                                       boolean                   forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                       boolean                   forDuplicateProcessing,
                                       @RequestBody  (required = false)
                                       ClassificationRequestBody requestBody)
    {
        return restAPI.addAssetOrigin(serverName, userId, assetGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the origin classification from an asset.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID element where the classification needs to be removed.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/assets/{assetGUID}/origin/remove")

    public VoidResponse clearAssetOrigin(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    assetGUID,
                                         @RequestParam(required = false, defaultValue = "false")
                                         boolean                   forLineage,
                                         @RequestParam (required = false, defaultValue = "false")
                                         boolean                   forDuplicateProcessing,
                                         @RequestBody  (required = false)
                                         ClassificationRequestBody requestBody)
    {
        return restAPI.clearAssetOrigin(serverName, userId, assetGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/asset/by-origin")

    public AssetElementsResponse getAssetsByOrigin(@PathVariable String                      serverName,
                                                   @PathVariable String                      userId,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   int                         startFrom,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   int                         pageSize,
                                                   @RequestParam(required = false, defaultValue = "false")
                                                   boolean                     forLineage,
                                                   @RequestParam(required = false, defaultValue = "false")
                                                   boolean                     forDuplicateProcessing,
                                                   @RequestBody(required = false)
                                                   FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getAssetsByOrigin(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/subject-area-member")

    public VoidResponse addElementToSubjectArea(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @PathVariable String                    elementGUID,
                                                @RequestParam(required = false, defaultValue = "false")
                                                boolean                   forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                boolean                   forDuplicateProcessing,
                                                @RequestBody  (required = false)
                                                ClassificationRequestBody requestBody)
    {
        return restAPI.addElementToSubjectArea(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/subject-area-member/remove")

    public VoidResponse removeElementFromSubjectArea(@PathVariable String                    serverName,
                                                     @PathVariable String                    userId,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestParam(required = false, defaultValue = "false")
                                                     boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.removeElementFromSubjectArea(serverName, userId, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-subject-area-membership")

    public ElementStubsResponse getMembersOfSubjectArea(@PathVariable String                      serverName,
                                                        @PathVariable String                      userId,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         startFrom,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         pageSize,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forLineage,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forDuplicateProcessing,
                                                        @RequestBody(required = false)
                                                        FindByPropertiesRequestBody requestBody)
    {
        return restAPI.getMembersOfSubjectArea(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */

    @PostMapping("/glossaries/terms/by-semantic-assignment/{elementGUID}")

    public GlossaryTermElementsResponse getMeanings(@PathVariable String                        serverName,
                                                    @PathVariable String                        userId,
                                                    @PathVariable String                        elementGUID,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int                         startFrom,
                                                    @RequestParam(required = false, defaultValue = "0")
                                                    int                         pageSize,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                    boolean                     forLineage,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                    boolean                     forDuplicateProcessing,
                                                    @RequestBody(required = false)
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getMeanings(serverName, userId, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping("/elements/by-semantic-assignment/{glossaryTermGUID}")

    public RelatedElementsResponse getSemanticAssignees(@PathVariable String                        serverName,
                                                        @PathVariable String                        userId,
                                                        @PathVariable String                        glossaryTermGUID,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         startFrom,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                         pageSize,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forLineage,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                     forDuplicateProcessing,
                                                        @RequestBody(required = false)
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getSemanticAssignees(serverName, userId, glossaryTermGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/governed-by/{governanceDefinitionGUID}")

    public RelatedElementsResponse getGovernedElements(@PathVariable String                        serverName,
                                                       @PathVariable String                        userId,
                                                       @PathVariable String                        governanceDefinitionGUID,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                                     int                         startFrom,
                                                       @RequestParam(required = false, defaultValue = "0")
                                                                     int                         pageSize,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                                     boolean                     forLineage,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                                     boolean                     forDuplicateProcessing,
                                                       @RequestBody(required = false)
                                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGovernedElements(serverName, userId, governanceDefinitionGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by")

    public GovernanceDefinitionsResponse getGovernedByDefinitions(@PathVariable String                        serverName,
                                                                  @PathVariable String                        userId,
                                                                  @PathVariable String                        elementGUID,
                                                                  @RequestParam(required = false, defaultValue = "0")
                                                                  int                         startFrom,
                                                                  @RequestParam(required = false, defaultValue = "0")
                                                                  int                         pageSize,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                  boolean                     forLineage,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                  boolean                     forDuplicateProcessing,
                                                                  @RequestBody(required = false)
                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getGovernedByDefinitions(serverName, userId, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/sourced-from/{elementGUID}")

    public RelatedElementsResponse getSourceElements(@PathVariable String                        serverName,
                                                     @PathVariable String                        userId,
                                                     @PathVariable String elementGUID,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                                   int                         startFrom,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                                   int                         pageSize,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                     forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                     forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getSourceElements(serverName, userId, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/sourced-from")

    public RelatedElementsResponse getElementsSourceFrom(@PathVariable String                        serverName,
                                                         @PathVariable String                        userId,
                                                         @PathVariable String                        elementGUID,
                                                         @RequestParam (required = false, defaultValue = "0")
                                                                       int                           startFrom,
                                                         @RequestParam (required = false, defaultValue = "0")
                                                                       int                           pageSize,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                       forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                       forDuplicateProcessing,
                                                         @RequestBody (required = false)
                                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getElementsSourceFrom(serverName, userId, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }
}
