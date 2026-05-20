/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationexplorer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.FindDigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SecurityTagQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.FindRequestBody;
import org.odpi.openmetadata.viewservices.classificationexplorer.server.ClassificationExplorerRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The ClassificationExplorerResource provides the Spring API endpoints of the Classification Explorer Open Metadata View Service (OMVS).
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
@Tag(name="API: Classification Explorer",
        description="Retrieve open metadata elements by type, or by the classifications/relationships attached to them.",
        externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/omvs/classification-explorer/overview/"))

public class ClassificationExplorerResource
{
    private final ClassificationExplorerRESTServices restAPI = new ClassificationExplorerRESTServices();


    /**
     * Default constructor
     */
    public ClassificationExplorerResource()
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     * Update the governance expectations classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     *      InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-expectations/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateGovernanceExpectations",
            description="Update the governance expectations classification for an element.",
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
     *      InvalidParameterException guid or userId is null or
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
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addGovernanceMeasurements",
            description="Add the governance measurements classification to an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

    public VoidResponse addGovernanceMeasurements(@PathVariable String                    serverName,
                                                  @PathVariable String                    urlMarker,
                                                  @PathVariable String                    elementGUID,
                                                  @RequestBody  (required = false)
                                                  NewClassificationRequestBody requestBody)
    {
        return restAPI.addGovernanceMeasurements(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Update the governance measurements classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governance-measurements/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateGovernanceMeasurements",
            description="Update the governance measurements classification for an element.",
            externalDocs=@ExternalDocumentation(description="Governance Rollout",
                    url="https://egeria-project.org/types/4/0450-Governance-Rollout/"))

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
     *      InvalidParameterException guid or userId is null or
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
     * Add the data scope classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/data-scope")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addDataScope",
            description="Add the data scope classification to an element.",
            externalDocs=@ExternalDocumentation(description="Data Scope",
                    url="https://egeria-project.org/types/2/0210-Data-Stores/"))

    public VoidResponse addDataScope(@PathVariable String                    serverName,
                                     @PathVariable String                    urlMarker,
                                     @PathVariable String                    elementGUID,
                                     @RequestBody  (required = false)
                                     NewClassificationRequestBody requestBody)
    {
        return restAPI.addDataScope(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Update the data scope classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/data-scope/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDataScope",
            description="Update the data scope classification for an element.",
            externalDocs=@ExternalDocumentation(description="Data Scope",
                    url="https://egeria-project.org/types/2/0210-Data-Stores/"))

    public VoidResponse updateDataScope(@PathVariable String                    serverName,
                                        @PathVariable String                    urlMarker,
                                        @PathVariable String                    elementGUID,
                                        @RequestBody  (required = false)
                                        UpdateClassificationRequestBody requestBody)
    {
        return restAPI.updateDataScope(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Remove the data scope classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody null request body
     *
     * @return void or
     *      InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/data-scope/remove")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearDataScope",
            description="Clear the data scope classification from an element.",
            externalDocs=@ExternalDocumentation(description="Data Scope",
                    url="https://egeria-project.org/types/2/0210-Data-Stores/"))

    public VoidResponse clearDataScope(@PathVariable String          serverName,
                                       @PathVariable String                    urlMarker,
                                       @PathVariable String          elementGUID,
                                       @RequestBody(required = false)
                                       DeleteClassificationRequestBody requestBody)
    {
        return restAPI.clearDataScope(serverName, urlMarker, elementGUID, requestBody);
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     * InvalidParameterException guid or userId is null or
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
     * InvalidParameterException guid or userId is null or
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
     * InvalidParameterException guid or userId is null or
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
     * Link a glossary term to an element using the SupplementaryProperties relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param glossaryTermGUID identifier of the glossary term to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/supplementary-properties/{glossaryTermGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addSupplementaryPropertiesToElement",
            description="Link a glossary term to an element using the SupplementaryProperties relationship.",
            externalDocs=@ExternalDocumentation(description="Supplementary Properties",
                    url="https://egeria-project.org/types/0/0011-Managing-Reference-Values/"))

    public VoidResponse addSupplementaryPropertiesToElement(@PathVariable String                  serverName,
                                                            @PathVariable String                    urlMarker,
                                                            @PathVariable String                  elementGUID,
                                                            @PathVariable String glossaryTermGUID,
                                                            @RequestBody  (required = false)
                                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.addSupplementaryPropertiesToElement(serverName, urlMarker, elementGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Remove the SupplementaryProperties relationship between a glossary term and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param glossaryTermGUID identifier of the glossary term to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/supplementary-properties/{glossaryTermGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeSupplementaryPropertiesFromElement",
            description="Remove the SupplementaryProperties relationship between a glossary term and an element.",
            externalDocs=@ExternalDocumentation(description="Supplementary Properties",
                    url="https://egeria-project.org/types/0/0011-Managing-Reference-Values/"))

    public VoidResponse removeSupplementaryPropertiesFromElement(@PathVariable String                        serverName,
                                                                 @PathVariable String                    urlMarker,
                                                                 @PathVariable String                        elementGUID,
                                                                 @PathVariable String                        glossaryTermGUID,
                                                                 @RequestBody  (required = false)
                                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeSupplementaryPropertiesFromElement(serverName, urlMarker, elementGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Attach an actor to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID            unique identifier of the element (project, product, etc.)
     * @param actorGUID unique identifier of the actor
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/assigned-to-actor/{actorGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="assignActorToElement",
            description="Attach an actor to an element.",
            externalDocs=@ExternalDocumentation(description="Assignments",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse assignActorToElement(@PathVariable String                  serverName,
                                             @PathVariable String                    urlMarker,
                                             @PathVariable String                  elementGUID,
                                             @PathVariable String actorGUID,
                                             @RequestBody  (required = false)
                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.assignActorToElement(serverName, urlMarker, elementGUID, actorGUID, requestBody);
    }


    /**
     * Detach an actor from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID            unique identifier of the element (project, product, etc.)
     * @param actorGUID unique identifier of the actor
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException guid or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/assigned-to-actor/{actorGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="unassignActorFromElement",
            description="Detach an actor from an element.",
            externalDocs=@ExternalDocumentation(description="Assignments",
                    url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public VoidResponse unassignActorFromElement(@PathVariable String                        serverName,
                                                 @PathVariable String                    urlMarker,
                                                 @PathVariable String                        elementGUID,
                                                 @PathVariable String                        actorGUID,
                                                 @RequestBody  (required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unassignActorFromElement(serverName, urlMarker, elementGUID, actorGUID, requestBody);
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
     * InvalidParameterException guid or userId is null or
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
     * InvalidParameterException guid or userId is null or
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
     * InvalidParameterException guid or userId is null or
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
     * InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *       InvalidParameterException guid or userId is null or
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
     *      InvalidParameterException guid or userId is null or
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
     *       InvalidParameterException guid or userId is null or
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


    /**
     * Return information about the elements classified with the impact classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-impact")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getImpactClassifiedElements",
            description="Return information about the elements classified with the impact classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))


    public OpenMetadataRootElementsResponse getImpactClassifiedElements(@PathVariable String                      serverName,
                                                                        @PathVariable String                        urlMarker,
                                                                        @RequestBody(required = false)
                                                                        LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getImpactClassifiedElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-confidence")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfidenceClassifiedElements",
            description="Return information about the elements classified with the confidence classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))


    public OpenMetadataRootElementsResponse getConfidenceClassifiedElements(@PathVariable String                      serverName,
                                                                            @PathVariable String                        urlMarker,
                                                                            @RequestBody(required = false)
                                                                            LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getConfidenceClassifiedElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-criticality")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCriticalityClassifiedElements",
            description="Return information about the elements classified with the criticality classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public OpenMetadataRootElementsResponse getCriticalityClassifiedElements(@PathVariable String                      serverName,
                                                                             @PathVariable String                        urlMarker,
                                                                             @RequestBody(required = false)
                                                                             LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getCriticalityClassifiedElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-confidentiality")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConfidentialityClassifiedElements",
            description="Return information about the elements classified with the confidentiality classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public OpenMetadataRootElementsResponse getConfidentialityClassifiedElements(@PathVariable String                      serverName,
                                                                                 @PathVariable String                        urlMarker,
                                                                                 @RequestBody(required = false)
                                                                                 LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getConfidentialityClassifiedElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-retention")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRetentionClassifiedElements",
            description="Return information about the elements classified with the retention classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public OpenMetadataRootElementsResponse getRetentionClassifiedElements(@PathVariable String                      serverName,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestBody(required = false)
                                                                           LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getRetentionClassifiedElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-security-tags")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSecurityTaggedElements",
            description="Return information about the elements classified with the security tags classification.",
            externalDocs=@ExternalDocumentation(description="Security tags classification", url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public OpenMetadataRootElementsResponse getSecurityTaggedElements(@PathVariable String                      serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @RequestBody(required = false)
                                                                      SecurityTagQueryProperties requestBody)
    {
        return restAPI.getSecurityTaggedElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the elements classified with the ownership classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-ownership")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getOwnersElements",
            description="Return information about the elements classified with the ownership classification.",
            externalDocs=@ExternalDocumentation(description="Governance roles", url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public OpenMetadataRootElementsResponse getOwnersElements(@PathVariable String                      serverName,
                                                              @PathVariable String                        urlMarker,
                                                              @RequestBody(required = false)
                                                              FilterRequestBody requestBody)
    {
        return restAPI.getOwnersElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return information about the digital resources from a specific origin.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return classified elements or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/by-digital-resource-origin")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsByOrigin",
            description="Return information about the digital resources from a specific origin.",
            externalDocs=@ExternalDocumentation(description="Asset Origins", url="https://egeria-project.org/types/4/0440-Organizational-Controls/"))

    public OpenMetadataRootElementsResponse getElementsByOrigin(@PathVariable String                      serverName,
                                                                @PathVariable String                        urlMarker,
                                                                @RequestBody(required = false)
                                                                FindDigitalResourceOriginProperties requestBody)
    {
        return restAPI.getElementsByOrigin(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */

    @PostMapping("/glossaries/terms/by-semantic-assignment/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMeanings",
            description="Retrieve the glossary terms linked via a SemanticAssignment relationship to the requested element.",
            externalDocs=@ExternalDocumentation(description="Semantic assignment", url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public OpenMetadataRootElementsResponse getMeanings(@PathVariable String                        serverName,
                                                        @PathVariable String                        urlMarker,
                                                        @PathVariable String                        elementGUID,
                                                        SemanticAssignmentQueryProperties requestBody)
    {
        return restAPI.getMeanings(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping("/elements/by-semantic-assignment/{glossaryTermGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSemanticAssignees",
            description="Retrieve the elements linked via a SemanticAssignment relationship to the requested glossary term",
            externalDocs=@ExternalDocumentation(description="Semantic assignment", url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public OpenMetadataRootElementsResponse getSemanticAssignees(@PathVariable String                        serverName,
                                                                 @PathVariable String                        urlMarker,
                                                                 @PathVariable String                        glossaryTermGUID,
                                                                 @RequestBody(required = false)
                                                                        SemanticAssignmentQueryProperties requestBody)
    {
        return restAPI.getSemanticAssignees(serverName, urlMarker, glossaryTermGUID, requestBody);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/governed-by/{governanceDefinitionGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernedElements",
            description="Retrieve the governance definitions linked via a GovernedBy relationship to the requested element.",
            externalDocs=@ExternalDocumentation(description="Governance definitions", url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public OpenMetadataRootElementsResponse getGovernedElements(@PathVariable String                        serverName,
                                                                       @PathVariable String                        urlMarker,
                                                                       @PathVariable String                        governanceDefinitionGUID,
                                                                       @RequestBody(required = false)
                                                                       ResultsRequestBody requestBody)
    {
        return restAPI.getGovernedElements(serverName, urlMarker, governanceDefinitionGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernedByDefinitions",
            description="Retrieve the elements linked via a GovernedBy relationship to the requested governance definition.",
            externalDocs=@ExternalDocumentation(description="Governance definitions", url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public OpenMetadataRootElementsResponse getGovernedByDefinitions(@PathVariable String                        serverName,
                                                                            @PathVariable String                        urlMarker,
                                                                            @PathVariable String                        elementGUID,
                                                                            @RequestBody(required = false)
                                                                            ResultsRequestBody requestBody)
    {
        return restAPI.getGovernedByDefinitions(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/source")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSourceElements",
            description="Retrieve the elements linked via a SourceFrom relationship to the requested element.  The elements returned were used to create the requested element.  Typically only one element is returned.",
            externalDocs=@ExternalDocumentation(description="Templates", url="https://egeria-project.org/types/0/0011-Managing-Referenceables/"))

    public OpenMetadataRootElementsResponse getSourceElements(@PathVariable String                        serverName,
                                                                     @PathVariable String                        urlMarker,
                                                                     @PathVariable String elementGUID,
                                                                     @RequestBody  (required = false)
                                                                     ResultsRequestBody requestBody)
    {
        return restAPI.getSourceElements(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourcedFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/sourced-from")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsSourcedFrom",
            description="Retrieve the elements linked via a SourcedFrom relationship to the requested element. The elements returned were created using the requested element as a template.",
            externalDocs=@ExternalDocumentation(description="Templates", url="https://egeria-project.org/types/0/0011-Managing-Referenceables/"))

    public OpenMetadataRootElementsResponse getElementsSourcedFrom(@PathVariable String                        serverName,
                                                                          @PathVariable String                        urlMarker,
                                                                          @PathVariable String                        elementGUID,
                                                                          @RequestBody  (required = false)
                                                                          ResultsRequestBody requestBody)
    {
        return restAPI.getElementsSourcedFrom(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested scope.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/scoped-by")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getScopes",
            description="Retrieve the elements linked via a ScopedBy relationship to the requested element.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public OpenMetadataRootElementsResponse getScopes(@PathVariable String                        serverName,
                                                             @PathVariable String                        urlMarker,
                                                             @PathVariable String                        elementGUID,
                                                             @RequestBody  (required = false)
                                                             ResultsRequestBody requestBody)
    {
        return restAPI.getScopes(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested scope.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param scopeGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/scoped-by/{scopeGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getScopedElements",
            description="Retrieve the elements linked via a ScopedBy relationship to the requested scope.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/1/0120-Assignment-Scopes/"))

    public OpenMetadataRootElementsResponse getScopedElements(@PathVariable String                        serverName,
                                                                     @PathVariable String                        urlMarker,
                                                                     @PathVariable String scopeGUID,
                                                                     @RequestBody  (required = false)
                                                                     ResultsRequestBody requestBody)
    {
        return restAPI.getScopedElements(serverName, urlMarker, scopeGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested scope.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/resource-list")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getResourceList",
            description="Retrieve the elements linked via a ResourceList relationship to the requested element.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/0/0019-More-Information/"))

    public OpenMetadataRootElementsResponse getResourceList(@PathVariable String                        serverName,
                                                                   @PathVariable String                        urlMarker,
                                                                   @PathVariable String                        elementGUID,
                                                                   @RequestBody  (required = false)
                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getResourceList(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the elements linked via a "ResourceList" relationship to the requested resource.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param resourceGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return linked elements or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/resource-list/{resourceGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSupportedByResource",
            description="Retrieve the elements linked via a ResourceList relationship to the requested resource.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/0/0019-More-Information/"))

    public OpenMetadataRootElementsResponse getSupportedByResource(@PathVariable String                        serverName,
                                                                          @PathVariable String                        urlMarker,
                                                                          @PathVariable String resourceGUID,
                                                                          @RequestBody  (required = false)
                                                                          ResultsRequestBody requestBody)
    {
        return restAPI.getSupportedByResource(serverName, urlMarker, resourceGUID, requestBody);
    }


    /**
     * Return information about the elements linked to a license type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param licenseTypeGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/licenses/{licenseTypeGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getLicensedElements",
            description="Return information about the elements linked to a license type.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/4/0481-Licenses/"))

    public OpenMetadataRootElementsResponse  getLicensedElements(@PathVariable String serverName,
                                                                        @PathVariable String                        urlMarker,
                                                                        @PathVariable String licenseTypeGUID,
                                                                        @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getLicensedElements(serverName, urlMarker, licenseTypeGUID, requestBody);
    }


    /**
     * Return information about the licenses linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/{elementGUID}/licenses")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getLicenses",
            description="Return information about the licenses linked to an element.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/4/0481-Licenses/"))

    public OpenMetadataRootElementsResponse getLicenses(@PathVariable String serverName,
                                                               @PathVariable String urlMarker,
                                                               @PathVariable String elementGUID,
                                                               @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getLicenses(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Return information about the elements linked to a certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param certificationTypeGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/certifications/{certificationTypeGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="geCertifiedElements",
            description="Return information about the elements linked to a certification type.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/4/0482-Certifications/"))

    public OpenMetadataRootElementsResponse  geCertifiedElements(@PathVariable String serverName,
                                                                 @PathVariable String                        urlMarker,
                                                                 @PathVariable String certificationTypeGUID,
                                                                 @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getCertifiedElements(serverName, urlMarker, certificationTypeGUID, requestBody);
    }


    /**
     * Return information about the certifications linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/{elementGUID}/certifications")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCertifications",
            description="Return information about the certifications linked to an element.",
            externalDocs=@ExternalDocumentation(description="Scopes", url="https://egeria-project.org/types/4/0482-Certifications/"))

    public OpenMetadataRootElementsResponse getCertifications(@PathVariable String serverName,
                                                              @PathVariable String urlMarker,
                                                              @PathVariable String elementGUID,
                                                              @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.getCertifications(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier for the metadata element
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRootElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public OpenMetadataRootElementResponse getRootElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String                        urlMarker,
                                                                @PathVariable String  elementGUID,
                                                                @RequestBody  (required = false)
                                                                    GetRequestBody requestBody)
    {
        return restAPI.getRootElementByGUID(serverName, urlMarker, elementGUID, requestBody);
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
    @PostMapping(path = "/elements/by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRootElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public OpenMetadataRootElementResponse getRootElementByUniqueName(@PathVariable String          serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @RequestBody (required = false) FindPropertyNameProperties requestBody)
    {
        return restAPI.getRootElementByUniqueName(serverName, urlMarker, requestBody);
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
    @PostMapping(path = "/elements/guid-by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRootElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public GUIDResponse getRootElementGUIDByUniqueName(@PathVariable String          serverName,
                                                       @PathVariable String                        urlMarker,
                                                       @RequestBody  FindPropertyNameProperties requestBody)
    {
        return restAPI.getRootElementGUIDByUniqueName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-type")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRootElementsByType",
            description="Retrieve elements of the requested type name.  If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getRootElementsByType(@PathVariable String                    serverName,
                                                                  @PathVariable String                        urlMarker,
                                                                  @RequestBody  (required = false)
                                                                      ResultsRequestBody requestBody)
    {
        return restAPI.getRootElementsByType(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve elements that match the complex query.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-complex-query")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRootElements",
            description="Retrieve elements that match the complex query.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse findRootElements(@PathVariable String                             serverName,
                                                             @PathVariable String                             urlMarker,
                                                             @RequestBody  (required = false) FindRequestBody requestBody)
    {
        return restAPI.findRootElements(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-exact-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRootElementsByPropertyValue",
            description="Retrieve elements by a value found in one of the properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getRootElementsByPropertyValue(@PathVariable String                       serverName,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestBody  (required = false)
                                                                               FindPropertyNamesProperties requestBody)
    {
        return restAPI.getRootElementsByPropertyValue(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-property-value-search")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsByPropertyValue",
            description="Retrieve elements by a value found in one of the properties specified.  The value must only be contained in the" +
                    " properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse findElementsByPropertyValue(@PathVariable String                       serverName,
                                                                        @PathVariable String                        urlMarker,
                                                                        @RequestBody  (required = false)
                                                                        FindPropertyNamesProperties requestBody)
    {
        return restAPI.findElementsByPropertyValue(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of authored elements matching the search string and optional content status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of authored elements
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/authored-elements/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findAuthoredElements",
            description="Returns the list of authored elements matching the search string and optional content status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse findAuthoredElements(@PathVariable String            serverName,
                                                           @PathVariable String             urlMarker,
                                                           @RequestBody  (required = false)
                                                           ContentStatusSearchString requestBody)
    {
        return restAPI.findAuthoredElements(serverName, urlMarker, requestBody);
    }


    /**
     * Returns the list of authored elements matching the category and optional content status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of authored elements
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/authored-elements/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAuthoredElementsByCategory",
            description="Returns the list of authored elements matching the search string and optional content status.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/asset"))

    public OpenMetadataRootElementsResponse getAuthoredElementsByCategory(@PathVariable String            serverName,
                                                                          @PathVariable String             urlMarker,
                                                                          @RequestBody  (required = false)
                                                                              ContentStatusFilterRequestBody requestBody)
    {
        return restAPI.getAuthoredElementsByCategory(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-classification/{classificationName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsByClassification",
            description="Retrieve elements with the requested classification name. It is also possible to limit the results by specifying a type name for the elements that should be returned. If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getElementsByClassification(@PathVariable String                    serverName,
                                                                        @PathVariable String                        urlMarker,
                                                                        @PathVariable String                    classificationName,
                                                                        @RequestBody  (required = false)
                                                                        ResultsRequestBody requestBody)
    {
        return restAPI.getElementsByClassification(serverName, urlMarker, classificationName, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-classification/{classificationName}/with-exact-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsByClassificationWithPropertyValue",
            description="Retrieve elements with the requested classification name and with the requested a value found in one of the classification's properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the types of elements returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getElementsByClassificationWithPropertyValue(@PathVariable String                       serverName,
                                                                                         @PathVariable String                        urlMarker,
                                                                                         @PathVariable String                    classificationName,
                                                                                         @RequestBody  (required = false)
                                                                                         FindPropertyNamesProperties requestBody)
    {
        return restAPI.getElementsByClassificationWithPropertyValue(serverName, urlMarker, classificationName, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value found in
     * one of the classification's properties specified.  The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-classification/{classificationName}/with-property-value-search")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsByClassificationWithPropertyValue",
            description="Retrieve elements with the requested classification name and with the requested a value found in one of the classification's" +
                    " properties specified.  The value must only be contained in the" +
                    " properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse findElementsByClassificationWithPropertyValue(@PathVariable String                       serverName,
                                                                                          @PathVariable String                        urlMarker,
                                                                                          @PathVariable String                    classificationName,
                                                                                          @RequestBody  (required = false)
                                                                                          FindPropertyNamesProperties requestBody)
    {
        return restAPI.findElementsByClassificationWithPropertyValue(serverName, urlMarker, classificationName,  requestBody);
    }


    /**
     * Retrieve related elements of any type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelatedElements",
            description="Retrieve elements linked via the requested relationship type name. It is also possible to limit the results by specifying a type name for the elements that should be returned. If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getRelatedElements(@PathVariable String                    serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @PathVariable String                  elementGUID,
                                                                      @RequestParam (required = false, defaultValue = "0")
                                                                      int     startingAtEnd,
                                                                      @RequestBody  (required = false)
                                                                      ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedElements(serverName, urlMarker, elementGUID, null, startingAtEnd, requestBody);
    }


    /**
     * Retrieve related elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship/{relationshipTypeName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelatedElements",
            description="Retrieve elements linked via the requested relationship type name. It is also possible to limit the results by specifying a type name for the elements that should be returned. If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getRelatedElements(@PathVariable String                    serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @PathVariable String                  elementGUID,
                                                                      @PathVariable String       relationshipTypeName,
                                                                      @RequestParam (required = false, defaultValue = "0")
                                                                      int     startingAtEnd,
                                                                      @RequestBody  (required = false)
                                                                      ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedElements(serverName, urlMarker, elementGUID, relationshipTypeName, startingAtEnd, requestBody);
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the requested value
     * found in one of the relationship's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship/{relationshipTypeName}/with-exact-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelatedElementsWithPropertyValue",
            description="Retrieve elements linked via the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the types of elements returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getRelatedElementsWithPropertyValue(@PathVariable String                       serverName,
                                                                                       @PathVariable String                        urlMarker,
                                                                                       @PathVariable String                  elementGUID,
                                                                                       @PathVariable String       relationshipTypeName,
                                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                                       int     startingAtEnd,
                                                                                       @RequestBody  (required = false)
                                                                                       FindPropertyNamesProperties requestBody)
    {
        return restAPI.getRelatedElementsWithPropertyValue(serverName, urlMarker, elementGUID, relationshipTypeName, startingAtEnd, requestBody);
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the relationship's properties
     * specified.  The value must only be contained in the by a value found in one of the properties specified.
     * The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/{elementGUID}/by-relationship/{relationshipTypeName}/with-property-value-search")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRelatedElementsWithPropertyValue",
            description="Retrieve elements linked via the requested relationship type name and with the requested value found in one of the relationship's properties specified (or any property if no property names are specified).  The value must only be contained in the properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse findRelatedElementsWithPropertyValue(@PathVariable String                       serverName,
                                                                                        @PathVariable String                        urlMarker,
                                                                                        @PathVariable String                  elementGUID,
                                                                                        @PathVariable String       relationshipTypeName,
                                                                                        @RequestParam (required = false, defaultValue = "0")
                                                                                        int     startingAtEnd,
                                                                                        @RequestBody  (required = false)
                                                                                        FindPropertyNamesProperties requestBody)
    {
        return restAPI.findRelatedElementsWithPropertyValue(serverName, urlMarker, elementGUID, relationshipTypeName, startingAtEnd, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationships",
            description="Retrieve relationships of the requested relationship type name (passed in the request body as openMetadataType).",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationships(@PathVariable String                    serverName,
                                                                  @PathVariable String                        urlMarker,
                                                                  @RequestBody  (required = false)
                                                                  ResultsRequestBody requestBody)
    {
        return restAPI.getRelationships(serverName, urlMarker, null, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/{relationshipTypeName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationships",
            description="Retrieve relationships of the requested relationship type name.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationships(@PathVariable String                    serverName,
                                                                  @PathVariable String                        urlMarker,
                                                                  @PathVariable String       relationshipTypeName,
                                                                  @RequestBody  (required = false)
                                                                  ResultsRequestBody requestBody)
    {
        return restAPI.getRelationships(serverName, urlMarker, relationshipTypeName, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/with-exact-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipsWithPropertyValue",
            description="Retrieve relationships of any relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must match exactly.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                   @PathVariable String                        urlMarker,
                                                                                   @RequestBody  (required = false)
                                                                                   FindPropertyNamesProperties requestBody)
    {
        return restAPI.getRelationshipsWithPropertyValue(serverName, urlMarker, null, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/{relationshipTypeName}/with-exact-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipsWithPropertyValue",
            description="Retrieve relationships of the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must match exactly.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                   @PathVariable String                        urlMarker,
                                                                                   @PathVariable String                        relationshipTypeName,
                                                                                   @RequestBody  (required = false)
                                                                                   FindPropertyNamesProperties requestBody)
    {
        return restAPI.getRelationshipsWithPropertyValue(serverName, urlMarker, relationshipTypeName, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/with-property-value-search")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRelationshipsWithPropertyValue",
            description="Retrieve relationships of any relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must only be contained in the properties rather than needing to be an exact match.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                    @PathVariable String                        urlMarker,
                                                                                    @RequestBody  (required = false)
                                                                                    FindPropertyNamesProperties requestBody)
    {
        return restAPI.findRelationshipsWithPropertyValue(serverName, urlMarker, null, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/relationships/{relationshipTypeName}/with-property-value-search")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRelationshipsWithPropertyValue",
            description="Retrieve relationships of the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must only be contained in the properties rather than needing to be an exact match.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                    @PathVariable String                        urlMarker,
                                                                                    @PathVariable String                        relationshipTypeName,
                                                                                    @RequestBody  (required = false)
                                                                                    FindPropertyNamesProperties requestBody)
    {
        return restAPI.findRelationshipsWithPropertyValue(serverName, urlMarker, relationshipTypeName, requestBody);
    }


    /**
     * Retrieve the header for the instance identified by the supplied unique identifier.  It may be an element (entity) or a relationship between elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param guid identifier to use in the lookup
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/guids/{guid}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="retrieveInstanceForGUID",
            description="Retrieve the header for the instance identified by the supplied unique identifier.  It may be an element (entity) or a relationship between elements.",
            externalDocs=@ExternalDocumentation(description="Unique Identifiers (GUID)", url="https://egeria-project.org/concepts/guid/"))

    public ElementHeaderResponse retrieveInstanceForGUID(@PathVariable String                       serverName,
                                                         @PathVariable String                        urlMarker,
                                                         @PathVariable String       guid,
                                                         @RequestBody(required = false) GetRequestBody requestBody)
    {
        return restAPI.retrieveInstanceForGUID(serverName, urlMarker, guid, requestBody);
    }


    /**
     * Return the list of search keywords containing the supplied string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of search keyword objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/by-category")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsByCategory",
            description="Return the list of elements with the supplied category - also a composite mermaid graph is returned.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getElementsByCategory(@PathVariable String                  serverName,
                                                               @PathVariable String                        urlMarker,
                                                               @RequestBody  (required = false)
                                                               FilterRequestBody              requestBody)
    {
        return restAPI.getElementsByCategory(serverName, urlMarker, requestBody);
    }


    /**
     * Return the list of search keywords containing the supplied string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of search keyword objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/search-keywords/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findSearchKeyword",
            description="Return the list of search keywords containing the supplied string. The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of an effective time to restrict the search to element that are/were effective at a particular time.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/search-keyword/"))

    public OpenMetadataRootElementsResponse findSearchKeywords(@PathVariable String                  serverName,
                                                               @PathVariable String                        urlMarker,
                                                               @RequestBody  (required = false)
                                                               SearchStringRequestBody              requestBody)
    {
        return restAPI.findSearchKeywords(serverName, urlMarker, requestBody);
    }


    /**
     * Return the list of search keywords containing the supplied keyword.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of search keyword objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/search-keywords/by-keyword")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSearchKeywordByKeyword",
            description="Return the list of search keywords containing the supplied keyword. The keyword is located in the request body and is interpreted as a plain string.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/search-keyword/"))

    public OpenMetadataRootElementsResponse getSearchKeywordByKeyword(@PathVariable String                  serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @RequestBody  (required = false)
                                                                      FilterRequestBody              requestBody)
    {
        return restAPI.getSearchKeywordsByKeyword(serverName, urlMarker, requestBody);
    }


    /**
     * Return the requested searchKeyword.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param searchKeywordGUID  unique identifier for the search keyword object.
     * @param requestBody optional effective time
     * @return search keyword properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/search-keywords/{searchKeywordGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSearchKeywordByGUID",
            description="Return the requested searchKeyword.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/search-keyword/"))

    public OpenMetadataRootElementResponse getSearchKeywordByGUID(@PathVariable String                        serverName,
                                                                  @PathVariable String                        urlMarker,
                                                                  @PathVariable String                        searchKeywordGUID,
                                                                  @RequestBody(required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getSearchKeywordByGUID(serverName, urlMarker, searchKeywordGUID, requestBody);
    }

}

