/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
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
     * Classify the element  to indicate the level of impact it is having or likely to have on the organization.
     * The level of impact is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/impact")

    @Operation(summary="setImpactClassification",
            description="Classify the element (typically a context event, to do or incident report) to indicate the level of impact that it is having/likely to have on the organization.  The level of impact is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setImpactClassification(@PathVariable String                    serverName,
                                                @PathVariable String                    elementGUID,
                                                @RequestBody  (required = false) NewClassificationRequestBody requestBody)
    {
        return restAPI.setImpactClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the impact classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/impact/remove")

    @Operation(summary="clearImpactClassification",
            description="Remove the impact classification from the element.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearImpactClassification(@PathVariable String                    serverName,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                  MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearImpactClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidence")

    @Operation(summary="setConfidenceClassification",
            description="Classify the element (typically an asset) to indicate the level of confidence that the organization has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setConfidenceClassification(@PathVariable String                    serverName,
                                                    @PathVariable String                    elementGUID,
                                                    @RequestBody  (required = false)
                                                        NewClassificationRequestBody requestBody)
    {
        return restAPI.setConfidenceClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
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
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearConfidenceClassification(@PathVariable String                    serverName,
                                                      @PathVariable String                    elementGUID,
                                                      @RequestBody  (required = false)
                                                      MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearConfidenceClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/criticality")

    @Operation(summary="setCriticalityClassification",
            description="Classify the element (typically an asset) to indicate how critical the element (or associated resource) is to the organization.  The level of criticality is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setCriticalityClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                         NewClassificationRequestBody requestBody)
    {
        return restAPI.setCriticalityClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
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
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearCriticalityClassification(@PathVariable String                    serverName,
                                                       @PathVariable String                    elementGUID,
                                                       @RequestBody  (required = false)
                                                       MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearCriticalityClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidentiality")

    @Operation(summary="setConfidentialityClassification",
            description="Classify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality that any data associated with the element should be given.  If the classification is attached to a glossary term, the level of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification. The level of confidence is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setConfidentialityClassification(@PathVariable String                    serverName,
                                                         @PathVariable String                    elementGUID,
                                                         @RequestBody  (required = false)
                                                             NewClassificationRequestBody requestBody)
    {
        return restAPI.setConfidentialityClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
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
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearConfidentialityClassification(@PathVariable String                    serverName,
                                                           @PathVariable String                    elementGUID,
                                                           @RequestBody  (required = false)
                                                           MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearConfidentialityClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/retention")

    @Operation(summary="setRetentionClassification",
            description="Classify the element (typically an asset) to indicate how long the element (or associated resource) is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter properties respectively.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setRetentionClassification(@PathVariable String                    serverName,
                                                   @PathVariable String                    elementGUID,
                                                   @RequestBody  (required = false)
                                                       NewClassificationRequestBody requestBody)
    {
        return restAPI.setRetentionClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
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
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearRetentionClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                     MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearRetentionClassification(serverName, elementGUID, requestBody);
    }


    /**
     * Add the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-expectations")

    @Operation(summary="addGovernanceExpectations",
            description="Add the governance expectations classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse addGovernanceExpectations(@PathVariable String                    serverName,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                      NewClassificationRequestBody requestBody)
    {
        return restAPI.addGovernanceExpectations(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the governance expectations classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-expectations/remove")

    @Operation(summary="clearGovernanceExpectations",
            description="Clear the governance expectations classification from an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

    public VoidResponse clearGovernanceExpectations(@PathVariable String          serverName,
                                                    @PathVariable String          elementGUID,
                                                    @RequestBody(required = false)
                                                    MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGovernanceExpectations(serverName, elementGUID, requestBody);
    }




    /**
     * Add the governance measurements classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements")

    @Operation(summary="addGovernanceMeasurements",
            description="Add the governance measurements classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse addGovernanceMeasurements(@PathVariable String                    serverName,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                      NewClassificationRequestBody requestBody)
    {
        return restAPI.addGovernanceMeasurements(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the governance measurements  classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements/remove")

    @Operation(summary="clearGovernanceMeasurements",
            description="Clear the governance measurements classification from an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

    public VoidResponse clearGovernanceMeasurements(@PathVariable String          serverName,
                                                    @PathVariable String          elementGUID,
                                                    @RequestBody(required = false)
                                                    MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGovernanceMeasurements(serverName, elementGUID, requestBody);
    }



    /**
     * Add the governance measurements results data set classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements-results-data-set")

    @Operation(summary="addGovernanceMeasurementsResultsDataSet",
            description="Add the governance measurements results data set classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse addGovernanceMeasurementsResultsDataSet(@PathVariable String                    serverName,
                                                                @PathVariable String                    elementGUID,
                                                                @RequestBody  (required = false)
                                                                    NewClassificationRequestBody requestBody)
    {
        return restAPI.addGovernanceMeasurementsResultsDataSet(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the governance measurements results data set classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements-results-data-set/remove")

    @Operation(summary="clearGovernanceMeasurementsResultsDataSet",
            description="Clear the governance measurements results data set classification from an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

    public VoidResponse clearGovernanceMeasurementsResultsDataSet(@PathVariable String          serverName,
                                                                  @PathVariable String          elementGUID,
                                                                  @RequestBody(required = false)
                                                                  MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearGovernanceMeasurementsResultsDataSet(serverName, elementGUID, requestBody);
    }


    /**
     * Add the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags")

    @Operation(summary="addSecurityTags",
            description="Add the security tags for an element.",
            externalDocs=@ExternalDocumentation(description="Security Tags",
                    url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public VoidResponse addSecurityTags(@PathVariable String                    serverName,
                                        @PathVariable String                    elementGUID,
                                        @RequestBody  (required = false)
                                            NewClassificationRequestBody requestBody)
    {
        return restAPI.addSecurityTags(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
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
                                          @RequestBody(required = false)
                                          MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearSecurityTags(serverName, elementGUID, requestBody);
    }


    /**
     * Add the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/ownership")

    @Operation(summary="addOwnership",
            description="Add the ownership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Ownership",
                    url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public VoidResponse addOwnership(@PathVariable String                    serverName,
                                     @PathVariable String                    elementGUID,
                                     @RequestBody  (required = false)
                                         NewClassificationRequestBody requestBody)
    {
        return restAPI.addOwnership(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be removed.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/ownership/remove")

    @Operation(summary="clearOwnership",
            description="Remove the ownership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Ownership",
                    url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public VoidResponse clearOwnership(@PathVariable String                    serverName,
                                       @PathVariable String                    elementGUID,
                                       @RequestBody  (required = false)
                                       MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearOwnership(serverName, elementGUID, requestBody);
    }



    /**
     * Add the digital resource origin classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/digital-resource-origin")

    @Operation(summary="addDigitalResourceOrigin",
            description="Add the digital resource origin classification for an element.",
            externalDocs=@ExternalDocumentation(description="Origin",
                    url="https://egeria-project.org/types/4/0440-Organizational-Controls/"))

    public VoidResponse addDigitalResourceOrigin(@PathVariable String                    serverName,
                                                 @PathVariable String                    elementGUID,
                                                 @RequestBody  (required = false)
                                                     NewClassificationRequestBody requestBody)
    {
        return restAPI.addOrigin(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the digital resource origin classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be removed.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/digital-resource-origin/remove")

    @Operation(summary="clearDigitalResourceOrigin",
            description="Remove the digital resource origin classification for an element.",
            externalDocs=@ExternalDocumentation(description="Origin",
                    url="https://egeria-project.org/types/4/0440-Organizational-Controls/"))

    public VoidResponse clearDigitalResourceOrigin(@PathVariable String                    serverName,
                                                   @PathVariable String                    elementGUID,
                                                   @RequestBody  (required = false)
                                                   MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearOrigin(serverName, elementGUID, requestBody);
    }



    /**
     * Add the zone membership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/zone-membership")

    @Operation(summary="addZoneMembership",
            description="Add the zone membership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Governance Zones",
                    url="https://egeria-project.org/types/4/0424-Governance-Zones/"))

    public VoidResponse addZoneMembership(@PathVariable String                    serverName,
                                          @PathVariable String                    elementGUID,
                                          @RequestBody  (required = false)
                                              NewClassificationRequestBody requestBody)
    {
        return restAPI.addZoneMembership(serverName, elementGUID, requestBody);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be removed.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/zone-membership/remove")

    @Operation(summary="clearZoneMembership",
            description="Remove the zone membership classification from an element to make it visible to all.",
            externalDocs=@ExternalDocumentation(description="Governance Zones",
                    url="https://egeria-project.org/types/4/0424-Governance-Zones/"))

    public VoidResponse clearZoneMembership(@PathVariable String                    serverName,
                                            @PathVariable String                    elementGUID,
                                            @RequestBody  (required = false)
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.clearZoneMembership(serverName, elementGUID, requestBody);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
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
                                                @RequestBody  (required = false)
                                                    NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupSemanticAssignment(serverName, elementGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
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
                                                @RequestBody  (required = false)
                                                    DeleteRequestBody requestBody)
    {
        return restAPI.clearSemanticAssignment(serverName, elementGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param stakeholderGUID identifier of the stakeholder to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/stakeholders/{stakeholderGUID}")

    @Operation(summary="addStakeholderToElement",
            description="Link a stakeholder to an element using the Stakeholder relationship.",
            externalDocs=@ExternalDocumentation(description="Stakeholders",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse addStakeholderToElement(@PathVariable String                  serverName,
                                                @PathVariable String                  elementGUID,
                                                @PathVariable String                  stakeholderGUID,
                                                @RequestBody  (required = false)
                                                    NewRelationshipRequestBody requestBody)
    {
        return restAPI.addStakeholderToElement(serverName, elementGUID, stakeholderGUID, requestBody);
    }


    /**
     * Remove the Stakeholder relationship between a stakeholder (typically Actor) and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param stakeholderGUID identifier of the stakeholder to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/stakeholders/{stakeholderGUID}/remove")

    @Operation(summary="removeStakeholderFromElement",
            description="Remove the Stakeholder relationship between a stakeholder (typically Actor) and an element.",
            externalDocs=@ExternalDocumentation(description="Stakeholders",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse removeStakeholderFromElement(@PathVariable String                        serverName,
                                                     @PathVariable String                        elementGUID,
                                                     @PathVariable String                        stakeholderGUID,
                                                     @RequestBody  (required = false)
                                                         DeleteRequestBody requestBody)
    {
        return restAPI.removeStakeholderFromElement(serverName, elementGUID, stakeholderGUID, requestBody);
    }


    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/scoped-by/{scopeGUID}")

    @Operation(summary="addScopeToElement",
            description="Link a scope to an element using the ScopedBy relationship.",
            externalDocs=@ExternalDocumentation(description="Scopes",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse addScopeToElement(@PathVariable String                  serverName,
                                          @PathVariable String                  elementGUID,
                                          @PathVariable String scopeGUID,
                                          @RequestBody  (required = false)
                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.addScopeToElement(serverName, elementGUID, scopeGUID, requestBody);
    }


    /**
     * Remove the ScopedBy relationship between a scope and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/scoped-by/{scopeGUID}/remove")

    @Operation(summary="removeScopeFromElement",
            description="Remove the ScopedBy relationship between a scope and an element.",
            externalDocs=@ExternalDocumentation(description="Scopes",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse removeScopeFromElement(@PathVariable String                        serverName,
                                               @PathVariable String                        elementGUID,
                                               @PathVariable String                        scopeGUID,
                                               @RequestBody  (required = false)
                                                   DeleteRequestBody requestBody)
    {
        return restAPI.removeScopeFromElement(serverName, elementGUID, scopeGUID, requestBody);
    }



    /**
     * Link a resource to an element using the ResourceList relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/resource-list/{resourceGUID}")

    @Operation(summary="addResourceListToElement",
            description="Link a resource to an element using the ResourceList relationship.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse addResourceListToElement(@PathVariable String                  serverName,
                                                 @PathVariable String                  elementGUID,
                                                 @PathVariable String resourceGUID,
                                                 @RequestBody  (required = false)
                                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.addResourceListToElement(serverName, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Remove the ResourceList relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/resource-list/{resourceGUID}/remove")

    @Operation(summary="removeResourceListFromElement",
            description="Remove the ResourceList relationship between a resource and an element.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse removeResourceListFromElement(@PathVariable String                        serverName,
                                                      @PathVariable String                        elementGUID,
                                                      @PathVariable String resourceGUID,
                                                      @RequestBody  (required = false)
                                                          DeleteRequestBody requestBody)
    {
        return restAPI.removeResourceListFromElement(serverName, elementGUID, resourceGUID, requestBody);
    }





    /**
     * Link a resource to an element using the MoreInformation relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/more-information/{resourceGUID}")

    @Operation(summary="addMoreInformationToElement",
            description="Link a resource to an element using the MoreInformation relationship.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse addMoreInformationToElement(@PathVariable String                  serverName,
                                                    @PathVariable String                  elementGUID,
                                                    @PathVariable String resourceGUID,
                                                    @RequestBody  (required = false)
                                                        NewRelationshipRequestBody requestBody)
    {
        return restAPI.addMoreInformationToElement(serverName, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Remove the MoreInformation relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/more-information/{resourceGUID}/remove")

    @Operation(summary="removeMoreInformationFromElement",
            description="Remove the MoreInformation relationship between a resource and an element.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse removeMoreInformationFromElement(@PathVariable String                        serverName,
                                                         @PathVariable String                        elementGUID,
                                                         @PathVariable String resourceGUID,
                                                         @RequestBody  (required = false)
                                                             DeleteRequestBody requestBody)
    {
        return restAPI.removeMoreInformationFromElement(serverName, elementGUID, resourceGUID, requestBody);
    }
}
