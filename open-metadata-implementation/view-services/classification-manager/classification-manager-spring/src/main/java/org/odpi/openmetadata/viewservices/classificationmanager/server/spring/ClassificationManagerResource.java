/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.classificationmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindPropertyNamesProperties;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.EffectiveTimeQueryRequestBody;
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
                                                          ResultsRequestBody requestBody)
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
                                                           ResultsRequestBody requestBody)
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
     * @param elementGUID unique identifier of the metadata element to declassify
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
                                                               ResultsRequestBody requestBody)
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
                                                         ResultsRequestBody requestBody)
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


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/{elementGUID}")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public MetadataElementSummaryResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                   @PathVariable String  elementGUID,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean forDuplicateProcessing,
                                                                   @RequestBody  (required = false)
                                                                   EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/by-unique-name")

    @Operation(summary="getMetadataElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public MetadataElementSummaryResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                         boolean         forLineage,
                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                         boolean         forDuplicateProcessing,
                                                                         @RequestBody (required = false) NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/guid-by-unique-name")

    @Operation(summary="getMetadataElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean         forLineage,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean         forDuplicateProcessing,
                                                           @RequestBody (required = false) NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-type")

    @Operation(summary="getElements",
            description="Retrieve elements of the requested type name.  If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataElementSummariesResponse getElements(@PathVariable String                    serverName,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                       startFrom,
                                                        @RequestParam(required = false, defaultValue = "0")
                                                        int                       pageSize,
                                                        @RequestParam(required = false, defaultValue = "false")
                                                        boolean                       forLineage,
                                                        @RequestParam (required = false, defaultValue = "false")
                                                        boolean                       forDuplicateProcessing,
                                                        @RequestBody  (required = false)
                                                        FindProperties requestBody)
    {
        return restAPI.getElements(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-exact-property-value")

    @Operation(summary="getElementsByPropertyValue",
            description="Retrieve elements by a value found in one of the properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataElementSummariesResponse getElementsByPropertyValue(@PathVariable String                       serverName,
                                                                       @RequestParam(required = false, defaultValue = "0")
                                                                       int                       startFrom,
                                                                       @RequestParam(required = false, defaultValue = "0")
                                                                       int                       pageSize,
                                                                       @RequestParam(required = false, defaultValue = "false")
                                                                       boolean                       forLineage,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                       forDuplicateProcessing,
                                                                       @RequestBody  (required = false)
                                                                           FindPropertyNamesProperties requestBody)
    {
        return restAPI.getElementsByPropertyValue(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-property-value-search")

    @Operation(summary="findElementsByPropertyValue",
            description="Retrieve elements by a value found in one of the properties specified.  The value must only be contained in the" +
                    " properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataElementSummariesResponse findElementsByPropertyValue(@PathVariable String                       serverName,
                                                                        @RequestParam(required = false, defaultValue = "0")
                                                                        int                       startFrom,
                                                                        @RequestParam(required = false, defaultValue = "0")
                                                                        int                       pageSize,
                                                                        @RequestParam(required = false, defaultValue = "false")
                                                                        boolean                       forLineage,
                                                                        @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                       forDuplicateProcessing,
                                                                        @RequestBody  (required = false)
                                                                            FindPropertyNamesProperties requestBody)
    {
        return restAPI.findElementsByPropertyValue(serverName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param classificationName name of classification
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-classification/{classificationName}")

    @Operation(summary="getElementsByClassification",
            description="Retrieve elements with the requested classification name. It is also possible to limit the results by specifying a type name for the elements that should be returned. If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataElementSummariesResponse getElementsByClassification(@PathVariable String                    serverName,
                                                                        @PathVariable String                    classificationName,
                                                                        @RequestParam(required = false, defaultValue = "0")
                                                                        int                       startFrom,
                                                                        @RequestParam(required = false, defaultValue = "0")
                                                                        int                       pageSize,
                                                                        @RequestParam(required = false, defaultValue = "false")
                                                                        boolean                       forLineage,
                                                                        @RequestParam (required = false, defaultValue = "false")
                                                                        boolean                       forDuplicateProcessing,
                                                                        @RequestBody  (required = false)
                                                                        FindProperties requestBody)
    {
        return restAPI.getElementsByClassification(serverName, classificationName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-classification/{classificationName}/with-exact-property-value")

    @Operation(summary="getElementsByClassificationWithPropertyValue",
            description="Retrieve elements with the requested classification name and with the requested a value found in one of the classification's properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the types of elements returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataElementSummariesResponse getElementsByClassificationWithPropertyValue(@PathVariable String                       serverName,
                                                                                         @PathVariable String                    classificationName,
                                                                                         @RequestParam(required = false, defaultValue = "0")
                                                                                         int                       startFrom,
                                                                                         @RequestParam(required = false, defaultValue = "0")
                                                                                         int                       pageSize,
                                                                                         @RequestParam(required = false, defaultValue = "false")
                                                                                         boolean                       forLineage,
                                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                                         boolean                       forDuplicateProcessing,
                                                                                         @RequestBody  (required = false)
                                                                                         FindPropertyNamesProperties requestBody)
    {
        return restAPI.getElementsByClassificationWithPropertyValue(serverName, classificationName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value found in
     * one of the classification's properties specified.  The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-classification/{classificationName}/with-property-value-search")

    @Operation(summary="findElementsByClassificationWithPropertyValue",
            description="Retrieve elements with the requested classification name and with the requested a value found in one of the classification's" +
                    " properties specified.  The value must only be contained in the" +
                    " properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataElementSummariesResponse findElementsByClassificationWithPropertyValue(@PathVariable String                       serverName,
                                                                                          @PathVariable String                    classificationName,
                                                                                          @RequestParam(required = false, defaultValue = "0")
                                                                                          int                       startFrom,
                                                                                          @RequestParam(required = false, defaultValue = "0")
                                                                                          int                       pageSize,
                                                                                          @RequestParam(required = false, defaultValue = "false")
                                                                                          boolean                       forLineage,
                                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                       forDuplicateProcessing,
                                                                                          @RequestBody  (required = false)
                                                                                              FindPropertyNamesProperties requestBody)
    {
        return restAPI.findElementsByClassificationWithPropertyValue(serverName, classificationName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Retrieve related elements of any type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship")

    @Operation(summary="getRelatedElements",
            description="Retrieve elements linked via the requested relationship type name. It is also possible to limit the results by specifying a type name for the elements that should be returned. If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public RelatedMetadataElementSummariesResponse getRelatedElements(@PathVariable String                    serverName,
                                                                      @PathVariable String                  elementGUID,
                                                                      @RequestParam (required = false, defaultValue = "0")
                                                                          int     startingAtEnd,
                                                                      @RequestParam(required = false, defaultValue = "0")
                                                                      int                       startFrom,
                                                                      @RequestParam(required = false, defaultValue = "0")
                                                                      int                       pageSize,
                                                                      @RequestParam(required = false, defaultValue = "false")
                                                                      boolean                       forLineage,
                                                                      @RequestParam (required = false, defaultValue = "false")
                                                                      boolean                       forDuplicateProcessing,
                                                                      @RequestBody  (required = false)
                                                                      FindProperties requestBody)
    {
        return restAPI.getRelatedElements(serverName, elementGUID, null, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }




    /**
     * Retrieve related elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship/{relationshipTypeName}")

    @Operation(summary="getRelatedElements",
            description="Retrieve elements linked via the requested relationship type name. It is also possible to limit the results by specifying a type name for the elements that should be returned. If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public RelatedMetadataElementSummariesResponse getRelatedElements(@PathVariable String                    serverName,
                                                                      @PathVariable String                  elementGUID,
                                                                      @PathVariable String       relationshipTypeName,
                                                                      @RequestParam (required = false, defaultValue = "0")
                                                                      int     startingAtEnd,
                                                                      @RequestParam(required = false, defaultValue = "0")
                                                                      int                       startFrom,
                                                                      @RequestParam(required = false, defaultValue = "0")
                                                                      int                       pageSize,
                                                                      @RequestParam(required = false, defaultValue = "false")
                                                                      boolean                       forLineage,
                                                                      @RequestParam (required = false, defaultValue = "false")
                                                                      boolean                       forDuplicateProcessing,
                                                                      @RequestBody  (required = false)
                                                                          FindProperties requestBody)
    {
        return restAPI.getRelatedElements(serverName, elementGUID, relationshipTypeName, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Retrieve elements linked via the requested relationship type name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship/{relationshipTypeName}/with-exact-property-value")

    @Operation(summary="getRelatedElementsWithPropertyValue",
            description="Retrieve elements linked via the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the types of elements returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public RelatedMetadataElementSummariesResponse getRelatedElementsWithPropertyValue(@PathVariable String                       serverName,
                                                                                       @PathVariable String                  elementGUID,
                                                                                       @PathVariable String       relationshipTypeName,
                                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                                           int     startingAtEnd,
                                                                                       @RequestParam(required = false, defaultValue = "0")
                                                                                       int                       startFrom,
                                                                                       @RequestParam(required = false, defaultValue = "0")
                                                                                       int                       pageSize,
                                                                                       @RequestParam(required = false, defaultValue = "false")
                                                                                       boolean                       forLineage,
                                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                                       boolean                       forDuplicateProcessing,
                                                                                       @RequestBody  (required = false)
                                                                                           FindPropertyNamesProperties requestBody)
    {
        return restAPI.getRelatedElementsWithPropertyValue(serverName, elementGUID, relationshipTypeName, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the relationship's properties
     * specified.  The value must only be contained in the by a value found in one of the properties specified.
     * The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship/{relationshipTypeName}/with-property-value-search")

    @Operation(summary="findRelatedElementsWithPropertyValue",
            description="Retrieve elements linked via the requested relationship type name and with the requested value found in one of the relationship's properties specified.  The value must only be contained in the properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public RelatedMetadataElementSummariesResponse findRelatedElementsWithPropertyValue(@PathVariable String                       serverName,
                                                                                        @PathVariable String                  elementGUID,
                                                                                        @PathVariable String       relationshipTypeName,
                                                                                        @RequestParam (required = false, defaultValue = "0")
                                                                                            int     startingAtEnd,
                                                                                        @RequestParam(required = false, defaultValue = "0")
                                                                                        int                       startFrom,
                                                                                        @RequestParam(required = false, defaultValue = "0")
                                                                                        int                       pageSize,
                                                                                        @RequestParam(required = false, defaultValue = "false")
                                                                                        boolean                       forLineage,
                                                                                        @RequestParam (required = false, defaultValue = "false")
                                                                                        boolean                       forDuplicateProcessing,
                                                                                        @RequestBody  (required = false)
                                                                                            FindPropertyNamesProperties requestBody)
    {
        return restAPI.findRelatedElementsWithPropertyValue(serverName, elementGUID, relationshipTypeName, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/{relationshipTypeName}")

    @Operation(summary="getRelationships",
            description="Retrieve relationships of the requested relationship type name.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationships(@PathVariable String                    serverName,
                                                                  @PathVariable String       relationshipTypeName,
                                                                  @RequestParam(required = false, defaultValue = "0")
                                                          int                       startFrom,
                                                                  @RequestParam(required = false, defaultValue = "0")
                                                          int                       pageSize,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                          boolean                       forLineage,
                                                                  @RequestParam (required = false, defaultValue = "false")
                                                          boolean                       forDuplicateProcessing,
                                                                  @RequestBody  (required = false)
                                                          FindProperties requestBody)
    {
        return restAPI.getRelationships(serverName, relationshipTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/{relationshipTypeName}/with-exact-property-value")

    @Operation(summary="getRelationshipsWithPropertyValue",
            description="Retrieve relationships of the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must match exactly.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                   @PathVariable String       relationshipTypeName,
                                                                                   @RequestParam(required = false, defaultValue = "0")
                                                                                       int                       startFrom,
                                                                                   @RequestParam(required = false, defaultValue = "0")
                                                                                       int                       pageSize,
                                                                                   @RequestParam(required = false, defaultValue = "false")
                                                                                       boolean                       forLineage,
                                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                                       boolean                       forDuplicateProcessing,
                                                                                   @RequestBody  (required = false)
                                                                               FindPropertyNamesProperties requestBody)
    {
        return restAPI.getRelationshipsWithPropertyValue(serverName, relationshipTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/{relationshipTypeName}/with-property-value-search")

    @Operation(summary="findRelationshipsWithPropertyValue",
            description="Retrieve relationships of the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must only be contained in the properties rather than needing to be an exact match.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                    @PathVariable String       relationshipTypeName,
                                                                                    @RequestParam(required = false, defaultValue = "0")
                                                                                        int                       startFrom,
                                                                                    @RequestParam(required = false, defaultValue = "0")
                                                                                        int                       pageSize,
                                                                                    @RequestParam(required = false, defaultValue = "false")
                                                                                        boolean                       forLineage,
                                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                        boolean                       forDuplicateProcessing,
                                                                                    @RequestBody  (required = false)
                                                                                FindPropertyNamesProperties requestBody)
    {
        return restAPI.findRelationshipsWithPropertyValue(serverName, relationshipTypeName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the header for the instance identified by the supplied unique identifier.  It may be an element (entity) or a relationship between elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param guid identifier to use in the lookup
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/guids/{guid}")

    @Operation(summary="retrieveInstanceForGUID",
            description="Retrieve the header for the instance identified by the supplied unique identifier.  It may be an element (entity) or a relationship between elements.",
            externalDocs=@ExternalDocumentation(description="Unique Identifiers (GUID)", url="https://egeria-project.org/concepts/guid/"))

    public ElementHeaderResponse retrieveInstanceForGUID(@PathVariable String                       serverName,
                                                         @PathVariable String       guid,
                                                         @RequestParam(required = false, defaultValue = "false")
                                                         boolean                       forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                         boolean                       forDuplicateProcessing,
                                                         @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.retrieveInstanceForGUID(serverName, guid, forLineage, forDuplicateProcessing, requestBody);
    }
}
