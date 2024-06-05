/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.classificationmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.EffectiveTimeRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.ReferenceableUpdateRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.server.ClassificationManagerRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * The ClassificationManagerResource provides the Spring API endpoints of the Classification Manager Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/classification-manager")

@Tag(name="API: Classification Manager OMVS",
     description="The Classification Manager OMVS enables the caller to maintain classifications and governance relationships attached to open metadata elements.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omvs/classification-manager/overview/"))

public class ClassificationManagerResource
{

    private final ClassificationManagerRESTServices restAPI = new ClassificationManagerRESTServices();


    /**
     * Default constructor
     */
    public ClassificationManagerResource()
    {
    }


    /* =====================================================================================================================
     * These methods are for assigning relationships and classifications that help govern both metadata
     * and its associated resources.
     */

    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="setConfidenceClassification",
            description="Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse setConfidenceClassification(@PathVariable String                    serverName,
                                                    @PathVariable String                    elementGUID,
                                                    @RequestParam(required = false, defaultValue = "false")
                                                                  boolean                   forLineage,
                                                    @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                   forDuplicateProcessing,
                                                    @RequestBody  (required = false)
                                                                  ClassificationRequestBody requestBody)
    {
        return restAPI.setConfidenceClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="clearConfidenceClassification",
            description="Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of confidence to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse clearConfidenceClassification(@PathVariable String                    serverName,
                                                      @PathVariable String                    elementGUID,
                                                      @RequestParam(required = false, defaultValue = "false")
                                                      boolean                   forLineage,
                                                      @RequestParam (required = false, defaultValue = "false")
                                                      boolean                   forDuplicateProcessing,
                                                      @RequestBody  (required = false)
                                                          EffectiveTimeRequestBody requestBody)
    {
        return restAPI.clearConfidenceClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="setCriticalityClassification",
            description="Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource) is to the organization.  The level of criticality is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse setCriticalityClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestParam(required = false, defaultValue = "false")
                                                                   boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                   forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                                   ClassificationRequestBody requestBody)
    {
        return restAPI.setCriticalityClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="clearCriticalityClassification",
            description="Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of criticality to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse clearCriticalityClassification(@PathVariable String                    serverName,
                                                       @PathVariable String                    elementGUID,
                                                       @RequestParam(required = false, defaultValue = "false")
                                                                     boolean                   forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                   forDuplicateProcessing,
                                                       @RequestBody  (required = false)
                                                                     EffectiveTimeRequestBody requestBody)
    {
        return restAPI.clearCriticalityClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="setConfidentialityClassification",
            description="Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality that any data associated with the element should be given.  If the classification is attached to a glossary term, the level of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification. The level of confidence is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse setConfidentialityClassification(@PathVariable String                    serverName,
                                                         @PathVariable String                    elementGUID,
                                                         @RequestParam(required = false, defaultValue = "false")
                                                                       boolean                   forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                   forDuplicateProcessing,
                                                         @RequestBody  (required = false)
                                                                       ClassificationRequestBody requestBody)
    {
        return restAPI.setConfidentialityClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="clearConfidentialityClassification",
            description="Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of confidentiality to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse clearConfidentialityClassification(@PathVariable String                    serverName,
                                                           @PathVariable String                    elementGUID,
                                                           @RequestParam(required = false, defaultValue = "false")
                                                                         boolean                   forLineage,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                                         boolean                   forDuplicateProcessing,
                                                           @RequestBody  (required = false)
                                                                         EffectiveTimeRequestBody requestBody)
    {
        return restAPI.clearConfidentialityClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="setRetentionClassification",
            description="Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource) is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter properties respectively.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse setRetentionClassification(@PathVariable String                    serverName,
                                                   @PathVariable String                    elementGUID,
                                                   @RequestParam(required = false, defaultValue = "false")
                                                                 boolean                   forLineage,
                                                   @RequestParam (required = false, defaultValue = "false")
                                                                 boolean                   forDuplicateProcessing,
                                                   @RequestBody  (required = false)
                                                                 ClassificationRequestBody requestBody)
    {
        return restAPI.setRetentionClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
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
    @PostMapping(path = "/elements/{elementGUID}/retention/remove")

    @Operation(summary="clearRetentionClassification",
            description="Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to track the retention period to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governance Action Classifications",
                    url="https://egeria-project.org/types/4/0422-Governance-Action-Classifications/"))

    public VoidResponse clearRetentionClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestParam(required = false, defaultValue = "false")
                                                     boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                     EffectiveTimeRequestBody requestBody)
    {
        return restAPI.clearRetentionClassification(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Add or replace the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="addSecurityTags",
            description="Add or replace the security tags for an element.",
            externalDocs=@ExternalDocumentation(description="Security Tags",
                    url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public VoidResponse addSecurityTags(@PathVariable String                    serverName,
                                        @PathVariable String                    elementGUID,
                                        @RequestParam(required = false, defaultValue = "false")
                                        boolean                   forLineage,
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                   forDuplicateProcessing,
                                        @RequestBody  (required = false)
                                        ClassificationRequestBody requestBody)
    {
        return restAPI.addSecurityTags(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="clearSecurityTags",
            description="Clear the security tags for an element.",
            externalDocs=@ExternalDocumentation(description="Security Tags",
                    url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public VoidResponse clearSecurityTags(@PathVariable String          serverName,
                                          @PathVariable String          elementGUID,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                   forLineage,
                                          @RequestParam (required = false, defaultValue = "false")
                                                        boolean                   forDuplicateProcessing,
                                          @RequestBody(required = false)
                                                        ClassificationRequestBody requestBody)
    {
        return restAPI.clearSecurityTags(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Add or replace the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="addOwnership",
            description="Add or replace the ownership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Ownership",
                    url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public VoidResponse addOwnership(@PathVariable String                    serverName,
                                     @PathVariable String                    elementGUID,
                                     @RequestParam(required = false, defaultValue = "false")
                                     boolean                   forLineage,
                                     @RequestParam (required = false, defaultValue = "false")
                                     boolean                   forDuplicateProcessing,
                                     @RequestBody  (required = false)
                                     ClassificationRequestBody requestBody)
    {
        return restAPI.addOwnership(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="clearOwnership",
            description="Add or replace the ownership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Ownership",
                    url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public VoidResponse clearOwnership(@PathVariable String                    serverName,
                                       @PathVariable String                    elementGUID,
                                       @RequestParam(required = false, defaultValue = "false")
                                       boolean                   forLineage,
                                       @RequestParam (required = false, defaultValue = "false")
                                       boolean                   forDuplicateProcessing,
                                       @RequestBody  (required = false)
                                       ClassificationRequestBody requestBody)
    {
        return restAPI.clearOwnership(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="addElementToSubjectArea",
            description="Classify the element to assert that the definitions it represents are part of a subject area definition.",
            externalDocs=@ExternalDocumentation(description="Subject Areas",
                    url="https://egeria-project.org/types/4/0425-Subject-Areas/"))

    public VoidResponse addElementToSubjectArea(@PathVariable String                    serverName,
                                                @PathVariable String                    elementGUID,
                                                @RequestParam(required = false, defaultValue = "false")
                                                boolean                   forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                boolean                   forDuplicateProcessing,
                                                @RequestBody  (required = false)
                                                ClassificationRequestBody requestBody)
    {
        return restAPI.addElementToSubjectArea(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param serverName  name of the server instance to connect to
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

    @Operation(summary="removeElementFromSubjectArea",
            description="Remove the subject area designation from the identified element.",
            externalDocs=@ExternalDocumentation(description="Subject Areas",
                    url="https://egeria-project.org/types/4/0425-Subject-Areas/"))

    public VoidResponse removeElementFromSubjectArea(@PathVariable String                    serverName,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestParam(required = false, defaultValue = "false")
                                                     boolean                   forLineage,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                   forDuplicateProcessing,
                                                     @RequestBody  (required = false)
                                                     ClassificationRequestBody requestBody)
    {
        return restAPI.removeElementFromSubjectArea(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/semantic-assignment/terms/{glossaryTermGUID}")

    @Operation(summary="setupSemanticAssignment",
            description="Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset). This relationship indicates that the data associated with the element meaning matches the description in the glossary term.",
            externalDocs=@ExternalDocumentation(description="Semantic Assignments",
                    url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public VoidResponse setupSemanticAssignment(@PathVariable String                  serverName,
                                                @PathVariable String                  elementGUID,
                                                @PathVariable String                  glossaryTermGUID,
                                                @RequestParam(required = false, defaultValue = "false")
                                                boolean                 forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                boolean                 forDuplicateProcessing,
                                                @RequestBody  (required = false)
                                                RelationshipRequestBody requestBody)
    {
        return restAPI.setupSemanticAssignment(serverName, elementGUID, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/semantic-assignment/terms/{glossaryTermGUID}/remove")

    @Operation(summary="clearSemanticAssignment",
            description="Remove a semantic assignment relationship between an element and its glossary term.",
            externalDocs=@ExternalDocumentation(description="Semantic Assignments",
                    url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public VoidResponse clearSemanticAssignment(@PathVariable String                        serverName,
                                                @PathVariable String                        elementGUID,
                                                @PathVariable String                        glossaryTermGUID,
                                                @RequestParam(required = false, defaultValue = "false")
                                                boolean                       forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                boolean                       forDuplicateProcessing,
                                                @RequestBody  (required = false)
                                                EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.clearSemanticAssignment(serverName, elementGUID, glossaryTermGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by/definition/{definitionGUID}")

    @Operation(summary="addGovernanceDefinitionToElement",
            description="Link a governance definition to an element using the GovernedBy relationship.",
            externalDocs=@ExternalDocumentation(description="Governance Definitions",
                    url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public VoidResponse addGovernanceDefinitionToElement(@PathVariable String                  serverName,
                                                         @PathVariable String                  definitionGUID,
                                                         @PathVariable String                  elementGUID,
                                                         @RequestParam(required = false, defaultValue = "false")
                                                         boolean                 forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                         boolean                 forDuplicateProcessing,
                                                         @RequestBody  (required = false)
                                                         RelationshipRequestBody requestBody)
    {
        return restAPI.addGovernanceDefinitionToElement(serverName, definitionGUID, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by/definition/{definitionGUID}/remove")

    @Operation(summary="removeGovernanceDefinitionFromElement",
            description="Remove the GovernedBy relationship between a governance definition and an element.",
            externalDocs=@ExternalDocumentation(description="Governance Definitions",
                    url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public VoidResponse removeGovernanceDefinitionFromElement(@PathVariable String                        serverName,
                                                              @PathVariable String                        definitionGUID,
                                                              @PathVariable String                        elementGUID,
                                                              @RequestParam(required = false, defaultValue = "false")
                                                              boolean                       forLineage,
                                                              @RequestParam (required = false, defaultValue = "false")
                                                              boolean                       forDuplicateProcessing,
                                                              @RequestBody  (required = false)
                                                              EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.removeGovernanceDefinitionFromElement(serverName, definitionGUID, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }
}
