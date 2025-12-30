/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.viewservices.classificationmanager.server.ClassificationManagerRESTServices;
import org.springframework.web.bind.annotation.*;

/**
 * The ClassificationManagerResource provides the Spring API endpoints of the Classification Manager Open Metadata View Service (OMVS).
 * This interface provides a service for Egeria UIs.
 */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Classification Manager",
        description="Attach classifications and governance relationships to open metadata elements to enhance the description of your digital resources and identify which resources need specific types of governance actions performed.",
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
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/impact")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setImpactClassification",
            description="Classify the element (typically a context event, to do or incident report) to indicate the level of impact that it is having/likely to have on the organization.  The level of impact is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setImpactClassification(@PathVariable String                    serverName,
                                                @PathVariable String                    urlMarker,
                                                @PathVariable String                    elementGUID,
                                                @RequestBody  (required = false) NewClassificationRequestBody requestBody)
    {
        return restAPI.setImpactClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the impact classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/impact/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearImpactClassification",
            description="Remove the impact classification from the element.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearImpactClassification(@PathVariable String                    serverName,
                                                  @PathVariable String                    urlMarker,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                  DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearImpactClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidence")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setConfidenceClassification",
            description="Classify the element (typically an asset) to indicate the level of confidence that the organization has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setConfidenceClassification(@PathVariable String                    serverName,
                                                    @PathVariable String                    urlMarker,
                                                    @PathVariable String                    elementGUID,
                                                    @RequestBody  (required = false)
                                                    NewClassificationRequestBody requestBody)
    {
        return restAPI.setConfidenceClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidence/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearConfidenceClassification",
            description="Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of confidence to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearConfidenceClassification(@PathVariable String                    serverName,
                                                      @PathVariable String                    urlMarker,
                                                      @PathVariable String                    elementGUID,
                                                      @RequestBody  (required = false)
                                                      DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearConfidenceClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/criticality")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setCriticalityClassification",
            description="Classify the element (typically an asset) to indicate how critical the element (or associated resource) is to the organization.  The level of criticality is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setCriticalityClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    urlMarker,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                     NewClassificationRequestBody requestBody)
    {
        return restAPI.setCriticalityClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/criticality/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearCriticalityClassification",
            description="Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of criticality to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearCriticalityClassification(@PathVariable String                    serverName,
                                                       @PathVariable String                    urlMarker,
                                                       @PathVariable String                    elementGUID,
                                                       @RequestBody  (required = false)
                                                       DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearCriticalityClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidentiality")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setConfidentialityClassification",
            description="Classify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality that any data associated with the element should be given.  If the classification is attached to a glossary term, the level of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification. The level of confidence is expressed by the levelIdentifier property.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setConfidentialityClassification(@PathVariable String                    serverName,
                                                         @PathVariable String                    urlMarker,
                                                         @PathVariable String                    elementGUID,
                                                         @RequestBody  (required = false)
                                                         NewClassificationRequestBody requestBody)
    {
        return restAPI.setConfidentialityClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/confidentiality/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearConfidentialityClassification",
            description="Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of confidentiality to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearConfidentialityClassification(@PathVariable String                    serverName,
                                                           @PathVariable String                    urlMarker,
                                                           @PathVariable String                    elementGUID,
                                                           @RequestBody  (required = false)
                                                           DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearConfidentialityClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Classify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/retention")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setRetentionClassification",
            description="Classify the element (typically an asset) to indicate how long the element (or associated resource) is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter properties respectively.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse setRetentionClassification(@PathVariable String                    serverName,
                                                   @PathVariable String                    urlMarker,
                                                   @PathVariable String                    elementGUID,
                                                   @RequestBody  (required = false)
                                                   NewClassificationRequestBody requestBody)
    {
        return restAPI.setRetentionClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/retention/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearRetentionClassification",
            description="Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to track the retention period to assign to the element.",
            externalDocs=@ExternalDocumentation(description="Governed Data Classifications",
                    url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public VoidResponse clearRetentionClassification(@PathVariable String                    serverName,
                                                     @PathVariable String                    urlMarker,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                     DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearRetentionClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Add the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-expectations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addGovernanceExpectations",
            description="Add the governance expectations classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse addGovernanceExpectations(@PathVariable String                    serverName,
                                                  @PathVariable String                    urlMarker,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                  NewClassificationRequestBody requestBody)
    {
        return restAPI.addGovernanceExpectations(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Update the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-expectations/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateGovernanceExpectations",
            description="Update the governance expectations classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse updateGovernanceExpectations(@PathVariable String                    serverName,
                                                     @PathVariable String                    urlMarker,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                     UpdateClassificationRequestBody requestBody)
    {
        return restAPI.updateGovernanceExpectations(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the governance expectations classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-expectations/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearGovernanceExpectations",
            description="Clear the governance expectations classification from an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

    public VoidResponse clearGovernanceExpectations(@PathVariable String          serverName,
                                                    @PathVariable String                    urlMarker,
                                                    @PathVariable String          elementGUID,
                                                    @RequestBody(required = false)
                                                    DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearGovernanceExpectations(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Add the governance measurements classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addGovernanceMeasurements",
            description="Add the governance measurements classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse addGovernanceMeasurements(@PathVariable String                    serverName,
                                                  @PathVariable String                    urlMarker,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                  NewClassificationRequestBody requestBody)
    {
        return restAPI.addGovernanceMeasurements(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Update the governance measurements classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateGovernanceMeasurements",
            description="Update the governance measurements classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout//"))

    public VoidResponse updateGovernanceMeasurements(@PathVariable String                    serverName,
                                                     @PathVariable String                    urlMarker,
                                                     @PathVariable String                    elementGUID,
                                                     @RequestBody  (required = false)
                                                     UpdateClassificationRequestBody requestBody)
    {
        return restAPI.updateGovernanceMeasurements(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Remove the governance measurements  classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearGovernanceMeasurements",
            description="Clear the governance measurements classification from an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

    public VoidResponse clearGovernanceMeasurements(@PathVariable String          serverName,
                                                    @PathVariable String                    urlMarker,
                                                    @PathVariable String          elementGUID,
                                                    @RequestBody(required = false)
                                                    DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearGovernanceMeasurements(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Add the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addSecurityTags",
            description="Add the security tags for an element.",
            externalDocs=@ExternalDocumentation(description="Security Tags",
                    url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public VoidResponse addSecurityTags(@PathVariable String                    serverName,
                                        @PathVariable String                    urlMarker,
                                        @PathVariable String                    elementGUID,
                                        @RequestBody  (required = false)
                                        NewClassificationRequestBody requestBody)
    {
        return restAPI.addSecurityTags(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/security-tags/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearSecurityTags",
            description="Clear the security tags for an element.",
            externalDocs=@ExternalDocumentation(description="Security Tags",
                    url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public VoidResponse clearSecurityTags(@PathVariable String          serverName,
                                          @PathVariable String                    urlMarker,
                                          @PathVariable String          elementGUID,
                                          @RequestBody(required = false)
                                          DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearSecurityTags(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Add the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/ownership")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addOwnership",
            description="Add the ownership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Ownership",
                    url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public VoidResponse addOwnership(@PathVariable String                    serverName,
                                     @PathVariable String                    urlMarker,
                                     @PathVariable String                    elementGUID,
                                     @RequestBody  (required = false)
                                     NewClassificationRequestBody requestBody)
    {
        return restAPI.addOwnership(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element where the classification needs to be removed.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/ownership/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearOwnership",
            description="Remove the ownership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Ownership",
                    url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public VoidResponse clearOwnership(@PathVariable String                    serverName,
                                       @PathVariable String                    urlMarker,
                                       @PathVariable String                    elementGUID,
                                       @RequestBody  (required = false)
                                       DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearOwnership(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Add the digital resource origin classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/digital-resource-origin")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addDigitalResourceOrigin",
            description="Add the digital resource origin classification for an element.",
            externalDocs=@ExternalDocumentation(description="Origin",
                    url="https://egeria-project.org/types/4/0440-Organizational-Controls/"))

    public VoidResponse addDigitalResourceOrigin(@PathVariable String                    serverName,
                                                 @PathVariable String                    urlMarker,
                                                 @PathVariable String                    elementGUID,
                                                 @RequestBody  (required = false)
                                                 NewClassificationRequestBody requestBody)
    {
        return restAPI.addOrigin(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the digital resource origin classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element where the classification needs to be removed.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/digital-resource-origin/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearDigitalResourceOrigin",
            description="Remove the digital resource origin classification for an element.",
            externalDocs=@ExternalDocumentation(description="Origin",
                    url="https://egeria-project.org/types/4/0440-Organizational-Controls/"))

    public VoidResponse clearDigitalResourceOrigin(@PathVariable String                    serverName,
                                                   @PathVariable String                    urlMarker,
                                                   @PathVariable String                    elementGUID,
                                                   @RequestBody  (required = false)
                                                   DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearOrigin(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Add the zone membership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/zone-membership")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addZoneMembership",
            description="Add the zone membership classification for an element.",
            externalDocs=@ExternalDocumentation(description="Governance Zones",
                    url="https://egeria-project.org/types/4/0424-Governance-Zones/"))

    public VoidResponse addZoneMembership(@PathVariable String                    serverName,
                                          @PathVariable String                    urlMarker,
                                          @PathVariable String                    elementGUID,
                                          @RequestBody  (required = false)
                                          NewClassificationRequestBody requestBody)
    {
        return restAPI.addZoneMembership(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element where the classification needs to be removed.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/zone-membership/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearZoneMembership",
            description="Remove the zone membership classification from an element to make it visible to all.",
            externalDocs=@ExternalDocumentation(description="Governance Zones",
                    url="https://egeria-project.org/types/4/0424-Governance-Zones/"))

    public VoidResponse clearZoneMembership(@PathVariable String                    serverName,
                                            @PathVariable String                    urlMarker,
                                            @PathVariable String                    elementGUID,
                                            @RequestBody  (required = false)
                                            DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearZoneMembership(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/semantic-assignment/terms/{glossaryTermGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setupSemanticAssignment",
            description="Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset). This relationship indicates that the data associated with the element meaning matches the description in the glossary term.",
            externalDocs=@ExternalDocumentation(description="Semantic Assignments",
                    url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public VoidResponse setupSemanticAssignment(@PathVariable String                  serverName,
                                                @PathVariable String                    urlMarker,
                                                @PathVariable String                  elementGUID,
                                                @PathVariable String                  glossaryTermGUID,
                                                @RequestBody  (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.setupSemanticAssignment(serverName, urlMarker, elementGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/semantic-assignment/terms/{glossaryTermGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearSemanticAssignment",
            description="Remove a semantic assignment relationship between an element and its glossary term.",
            externalDocs=@ExternalDocumentation(description="Semantic Assignments",
                    url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public VoidResponse clearSemanticAssignment(@PathVariable String                        serverName,
                                                @PathVariable String                    urlMarker,
                                                @PathVariable String                        elementGUID,
                                                @PathVariable String                        glossaryTermGUID,
                                                @RequestBody  (required = false)
                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.clearSemanticAssignment(serverName, urlMarker, elementGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/scoped-by/{scopeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addScopeToElement",
            description="Link a scope to an element using the ScopedBy relationship.",
            externalDocs=@ExternalDocumentation(description="Scopes",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse addScopeToElement(@PathVariable String                  serverName,
                                          @PathVariable String                    urlMarker,
                                          @PathVariable String                  elementGUID,
                                          @PathVariable String scopeGUID,
                                          @RequestBody  (required = false)
                                          NewRelationshipRequestBody requestBody)
    {
        return restAPI.addScopeToElement(serverName, urlMarker, elementGUID, scopeGUID, requestBody);
    }


    /**
     * Remove the ScopedBy relationship between a scope and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/scoped-by/{scopeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeScopeFromElement",
            description="Remove the ScopedBy relationship between a scope and an element.",
            externalDocs=@ExternalDocumentation(description="Scopes",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse removeScopeFromElement(@PathVariable String                        serverName,
                                               @PathVariable String                    urlMarker,
                                               @PathVariable String                        elementGUID,
                                               @PathVariable String                        scopeGUID,
                                               @RequestBody  (required = false)
                                               DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeScopeFromElement(serverName, urlMarker, elementGUID, scopeGUID, requestBody);
    }


    /**
     * Assign an action to an actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/assign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="assignAction",
            description="Assign a action to an actor.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse assignAction(@PathVariable String         serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable String         actionGUID,
                                       @PathVariable String         actorGUID,
                                       @RequestBody (required = false)
                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.assignAction(serverName, urlMarker, actionGUID, actorGUID, requestBody);
    }


    /**
     * Assign an action to a new actor.  This will unassign all other actors previously assigned to the action.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/reassign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="reassignAction",
            description="Assign a action to a new actor.  This will unassign all other actors previously assigned to the action.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse reassignAction(@PathVariable String         serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable String         actionGUID,
                                       @PathVariable String         actorGUID,
                                       @RequestBody (required = false)
                                       UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.reassignAction(serverName, urlMarker, actionGUID, actorGUID, requestBody);
    }


    /**
     * Remove an action from an actor.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param actionGUID unique identifier of the action
     * @param actorGUID  actor to assign the action to
     * @param requestBody  request body
     *
     * @return void or
     * InvalidParameterException a parameter is invalid
     * PropertyServerException the server is not available
     * UserNotAuthorizedException the calling user is not authorized to issue the call
     */
    @PostMapping(path = "/actions/{actionGUID}/unassign/{actorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="unassignAction",
            description="Remove an action from an actor.",
            externalDocs=@ExternalDocumentation(description="Actions",
                    url="https://egeria-project.org/concepts/action"))

    public VoidResponse unassignAction(@PathVariable String         serverName,
                                       @PathVariable String             urlMarker,
                                       @PathVariable String         actionGUID,
                                       @PathVariable String         actorGUID,
                                       @RequestBody (required = false)
                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unassignAction(serverName, urlMarker, actionGUID, actorGUID, requestBody);
    }



    /**
     * Link a resource to an element using the ResourceList relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/resource-list/{resourceGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addResourceListToElement",
            description="Link a resource to an element using the ResourceList relationship.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse addResourceListToElement(@PathVariable String                  serverName,
                                                 @PathVariable String                    urlMarker,
                                                 @PathVariable String                  elementGUID,
                                                 @PathVariable String resourceGUID,
                                                 @RequestBody  (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.addResourceListToElement(serverName, urlMarker, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Remove the ResourceList relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/resource-list/{resourceGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeResourceListFromElement",
            description="Remove the ResourceList relationship between a resource and an element.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse removeResourceListFromElement(@PathVariable String                        serverName,
                                                      @PathVariable String                    urlMarker,
                                                      @PathVariable String                        elementGUID,
                                                      @PathVariable String resourceGUID,
                                                      @RequestBody  (required = false)
                                                      DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeResourceListFromElement(serverName, urlMarker, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Link a resource to an element using the MoreInformation relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/more-information/{resourceGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addMoreInformationToElement",
            description="Link a resource to an element using the MoreInformation relationship.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse addMoreInformationToElement(@PathVariable String                  serverName,
                                                    @PathVariable String                    urlMarker,
                                                    @PathVariable String                  elementGUID,
                                                    @PathVariable String resourceGUID,
                                                    @RequestBody  (required = false)
                                                    NewRelationshipRequestBody requestBody)
    {
        return restAPI.addMoreInformationToElement(serverName, urlMarker, elementGUID, resourceGUID, requestBody);
    }


    /**
     * Remove the MoreInformation relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/more-information/{resourceGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeMoreInformationFromElement",
            description="Remove the MoreInformation relationship between a resource and an element.",
            externalDocs=@ExternalDocumentation(description="Resource Lists",
                    url="https://egeria-project.org/types/0/0019-More-Information/"))

    public VoidResponse removeMoreInformationFromElement(@PathVariable String                        serverName,
                                                         @PathVariable String                    urlMarker,
                                                         @PathVariable String                        elementGUID,
                                                         @PathVariable String resourceGUID,
                                                         @RequestBody  (required = false)
                                                         DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeMoreInformationFromElement(serverName, urlMarker, elementGUID, resourceGUID, requestBody);
    }



    /**
     * Creates a search keyword and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID        String - unique id for the element.
     * @param requestBody containing type of search keyword enum and the text of the searchKeyword.
     *
     * @return elementGUID for new search keyword object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/search-keywords")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addSearchKeywordToElement",
            description="Creates a search keyword and attaches it to an element.",
            externalDocs=@ExternalDocumentation(description="Search Keyword",
                    url="https://egeria-project.org/concepts/search-keywords/"))

    public GUIDResponse addSearchKeywordToElement(@PathVariable String                 serverName,
                                                  @PathVariable String                 urlMarker,
                                                  @PathVariable String                 elementGUID,
                                                  @RequestBody(required = false)  NewAttachmentRequestBody requestBody)
    {
        return restAPI.addSearchKeywordToElement(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Update an existing search keyword.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param searchKeywordGUID  unique identifier for the search keyword to change.
     * @param requestBody  containing type of search keyword enum and the text of the searchKeyword.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/search-keywords/{searchKeywordGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateSearchKeyword",
            description="Update an existing search keyword.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/search-keyword/"))

    public BooleanResponse updateSearchKeyword(@PathVariable String                         serverName,
                                               @PathVariable String                        urlMarker,
                                               @PathVariable String                         searchKeywordGUID,
                                               @RequestBody(required = false)  UpdateElementRequestBody requestBody)
    {
        return restAPI.updateSearchKeyword(serverName, urlMarker, searchKeywordGUID, requestBody);
    }



    /**
     * Removes a search keyword added to the element by this user.  This deletes the link to the search keyword and the search keyword itself.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param searchKeywordGUID  String - unique id for the search keyword object
     * @param requestBody  containing type of search keyword enum and the text of the searchKeyword.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/search-keywords/{searchKeywordGUID}/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeSearchKeywordFromElement",
            description="Removes a search keyword added to the element.  This deletes the link to the search keyword and the search keyword itself.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/search-keyword/"))

    public VoidResponse removeSearchKeywordFromElement(@PathVariable String                         serverName,
                                                       @PathVariable String                        urlMarker,
                                                       @PathVariable String                         searchKeywordGUID,
                                                       @RequestBody(required = false)
                                                       DeleteElementRequestBody requestBody)
    {
        return restAPI.removeSearchKeywordFromElement(serverName, urlMarker,  searchKeywordGUID, requestBody);
    }


    /**
     * Create a relationship between two elements that show they represent the same "thing". If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param peerDuplicateGUID identifier of the duplicate to link
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/{elementGUID}/peer-duplicate/{peerDuplicateGUID}/attach")
    @Operation(summary="linkElementsAsPeerDuplicates",
            description="Create a relationship between two elements that show they represent the same 'thing'. If the relationship already exists, the properties are updated.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse linkElementsAsPeerDuplicates(@PathVariable String                     serverName,
                                                     @PathVariable String                     urlMarker,
                                                     @PathVariable String                     elementGUID,
                                                     @PathVariable String                     peerDuplicateGUID,
                                                     @RequestBody  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkElementsAsPeerDuplicates(serverName, urlMarker, elementGUID, peerDuplicateGUID, requestBody);
    }


    /**
     * Remove the PeerDuplicateLink a relationship between two elements that showed they represent the same "thing".
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param peerDuplicateGUID identifier of the duplicate to link
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/{elementGUID}/peer-duplicate/{peerDuplicateGUID}/detach")
    @Operation(summary="unlinkElementsAsPeerDuplicates",
            description="Remove the PeerDuplicateLink a relationship between two elements that showed they represent the same 'thing'.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse unlinkElementsAsPeerDuplicates(@PathVariable String                        serverName,
                                                       @PathVariable String                        urlMarker,
                                                       @PathVariable String                        elementGUID,
                                                       @PathVariable String                        peerDuplicateGUID,
                                                       @RequestBody  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unlinkElementsAsPeerDuplicates(serverName, urlMarker, elementGUID, peerDuplicateGUID, requestBody);
    }


    /**
     * Classify the element to indicate that is has one or more duplicate in the open metadata ecosystem.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/known-duplicate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setKnownDuplicateClassification",
            description="Classify the element to indicate that is has one or more duplicate in the open metadata ecosystem.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse setKnownDuplicateClassification(@PathVariable String                    serverName,
                                                        @PathVariable String                    urlMarker,
                                                        @PathVariable String                    elementGUID,
                                                        @RequestBody  (required = false) NewClassificationRequestBody requestBody)
    {
        return restAPI.setKnownDuplicateClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the KnownDuplicate classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/known-duplicate/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearKnownDuplicateClassification",
            description="Remove the KnownDuplicate classification from the element.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse clearKnownDuplicateClassification(@PathVariable String                    serverName,
                                                          @PathVariable String                    urlMarker,
                                                          @PathVariable String                    elementGUID,
                                                          @RequestBody  (required = false)
                                                          DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearKnownDuplicateClassification(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Create a relationship between two elements that shows that one is a combination of a number of duplicates, and it should
     * be used instead.
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param sourceElementGUID identifier of the source to link
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/{elementGUID}/consolidated-duplicate-source/{sourceElementGUID}/attach")
    @Operation(summary="linkConsolidatedDuplicateToSource",
            description="Create a relationship between two elements that shows that one is a combination of a number of duplicates, and it should be used instead.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse linkConsolidatedDuplicateToSource(@PathVariable String                            serverName,
                                                          @PathVariable String                            urlMarker,
                                                          @PathVariable String                        elementGUID,
                                                          @PathVariable String                        sourceElementGUID,
                                                          @RequestBody  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkConsolidatedDuplicateToSourceElement(serverName, urlMarker, elementGUID, sourceElementGUID, requestBody);
    }


    /**
     * Remove the ConsolidatedDuplicateLink relationship between two elements that showed they represent the same "thing".
     *
     * @param serverName name of the service to route the request to.
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param sourceElementGUID identifier of the source to link
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/related-elements/{elementGUID}/consolidated-duplicate-source/{sourceElementGUID}/detach")
    @Operation(summary="unlinkConsolidatedDuplicateFromSourceElement",
            description="Remove the ConsolidatedDuplicateLink relationship between two elements that showed they represent the same 'thing'.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse unlinkConsolidatedDuplicateFromSourceElement(@PathVariable String                        serverName,
                                                                     @PathVariable String                        urlMarker,
                                                                     @PathVariable String                        elementGUID,
                                                                     @PathVariable String sourceElementGUID,
                                                                     @RequestBody  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unlinkConsolidatedDuplicateFromSourceElement(serverName, urlMarker, elementGUID, sourceElementGUID, requestBody);
    }


    /**
     * Classify the element to indicate that it is derived from one or more duplicates in the open metadata ecosystem.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/consolidated-duplicate")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setConsolidatedDuplicateClassification",
            description="Classify the element to indicate that it is derived from one or more duplicates in the open metadata ecosystem.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse setConsolidatedDuplicateClassification(@PathVariable String                    serverName,
                                                               @PathVariable String                    urlMarker,
                                                               @PathVariable String                    elementGUID,
                                                               @RequestBody  (required = false) NewClassificationRequestBody requestBody)
    {
        return restAPI.setConsolidatedDuplicateClassification(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the ConsolidatedDuplicate classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/consolidated-duplicate/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearConsolidatedDuplicateClassification",
            description="Remove the ConsolidatedDuplicate classification from the element.",
            externalDocs=@ExternalDocumentation(description="Duplicate Management",
                    url="https://egeria-project.org/features/duplicate-management/overview"))

    public VoidResponse clearConsolidatedDuplicateClassification(@PathVariable String                    serverName,
                                                                 @PathVariable String                    urlMarker,
                                                                 @PathVariable String                    elementGUID,
                                                                 @RequestBody  (required = false)
                                                                 DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearConsolidatedDuplicateClassification(serverName, urlMarker, elementGUID, requestBody);
    }
}
