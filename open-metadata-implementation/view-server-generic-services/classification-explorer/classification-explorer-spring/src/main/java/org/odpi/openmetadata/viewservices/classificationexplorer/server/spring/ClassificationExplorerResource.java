/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationexplorer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindAssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagQueryProperties;
import org.odpi.openmetadata.viewservices.classificationexplorer.server.ClassificationExplorerRESTServices;
import org.springframework.web.bind.annotation.*;



/**
 * The ClassificationExplorerResource provides the Spring API endpoints of the Classification Explorer Open Metadata View Service (OMVS).
= */

@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")

@Tag(name="API: Classification Explorer OMVS",
     description="Retrieve elements by type, or by classifications/relationships attached to them.",
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


    /**
     * Return information about the elements classified with the DataFieldValues classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getDataFieldClassifiedElements",
            description="Return information about the elements classified with the DataFieldValues classification.",
            externalDocs=@ExternalDocumentation(description="DataFieldValues classification", url="https://egeria-project.org/types/3/0340-Dictionary/"))


    public MetadataElementSummariesResponse getDataFieldClassifiedElements(@PathVariable String                      serverName,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestParam(required = false, defaultValue = "0")
                                                               int                         startFrom,
                                                                           @RequestParam(required = false, defaultValue = "0")
                                                               int                         pageSize,
                                                                           @RequestParam(required = false, defaultValue = "false")
                                                               boolean                     forLineage,
                                                                           @RequestParam(required = false, defaultValue = "false")
                                                               boolean                     forDuplicateProcessing,
                                                                           @RequestBody(required = false)
                                                                               DataFieldQueryProperties requestBody)
    {
        return restAPI.getDataFieldClassifiedElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-confidence")

    @Operation(summary="getConfidenceClassifiedElements",
            description="Return information about the elements classified with the confidence classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))


    public MetadataElementSummariesResponse getConfidenceClassifiedElements(@PathVariable String                      serverName,
                                                                            @PathVariable String                        urlMarker,
                                                                            @RequestParam(required = false, defaultValue = "0")
                                                                            int                         startFrom,
                                                                            @RequestParam(required = false, defaultValue = "0")
                                                                            int                         pageSize,
                                                                            @RequestParam(required = false, defaultValue = "false")
                                                                            boolean                     forLineage,
                                                                            @RequestParam(required = false, defaultValue = "false")
                                                                            boolean                     forDuplicateProcessing,
                                                                            @RequestBody(required = false)
                                                                                LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getConfidenceClassifiedElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-criticality")

    @Operation(summary="getCriticalityClassifiedElements",
            description="Return information about the elements classified with the criticality classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public MetadataElementSummariesResponse getCriticalityClassifiedElements(@PathVariable String                      serverName,
                                                                             @PathVariable String                        urlMarker,
                                                                             @RequestParam(required = false, defaultValue = "0")
                                                                             int                         startFrom,
                                                                             @RequestParam(required = false, defaultValue = "0")
                                                                             int                         pageSize,
                                                                             @RequestParam(required = false, defaultValue = "false")
                                                                             boolean                     forLineage,
                                                                             @RequestParam(required = false, defaultValue = "false")
                                                                             boolean                     forDuplicateProcessing,
                                                                             @RequestBody(required = false)
                                                                                 LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getCriticalityClassifiedElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-confidentiality")

    @Operation(summary="getConfidentialityClassifiedElements",
            description="Return information about the elements classified with the confidentiality classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public MetadataElementSummariesResponse getConfidentialityClassifiedElements(@PathVariable String                      serverName,
                                                                                 @PathVariable String                        urlMarker,
                                                                                 @RequestParam(required = false, defaultValue = "0")
                                                                                 int                         startFrom,
                                                                                 @RequestParam(required = false, defaultValue = "0")
                                                                                 int                         pageSize,
                                                                                 @RequestParam(required = false, defaultValue = "false")
                                                                                 boolean                     forLineage,
                                                                                 @RequestParam(required = false, defaultValue = "false")
                                                                                 boolean                     forDuplicateProcessing,
                                                                                 @RequestBody(required = false)
                                                                                     LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getConfidentialityClassifiedElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-retention")

    @Operation(summary="getRetentionClassifiedElements",
            description="Return information about the elements classified with the retention classification.",
            externalDocs=@ExternalDocumentation(description="Governance data classifications", url="https://egeria-project.org/types/4/0422-Governed-Data-Classifications/"))

    public MetadataElementSummariesResponse getRetentionClassifiedElements(@PathVariable String                      serverName,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestParam(required = false, defaultValue = "0")
                                                                           int                         startFrom,
                                                                           @RequestParam(required = false, defaultValue = "0")
                                                                           int                         pageSize,
                                                                           @RequestParam(required = false, defaultValue = "false")
                                                                           boolean                     forLineage,
                                                                           @RequestParam(required = false, defaultValue = "false")
                                                                           boolean                     forDuplicateProcessing,
                                                                           @RequestBody(required = false)
                                                                               LevelIdentifierQueryProperties requestBody)
    {
        return restAPI.getRetentionClassifiedElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-security-tags")

    @Operation(summary="getSecurityTaggedElements",
            description="Return information about the elements classified with the security tags classification.",
            externalDocs=@ExternalDocumentation(description="Security tags classification", url="https://egeria-project.org/types/4/0423-Security-Definitions/"))

    public MetadataElementSummariesResponse getSecurityTaggedElements(@PathVariable String                      serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @RequestParam(required = false, defaultValue = "0")
                                                                      int                         startFrom,
                                                                      @RequestParam(required = false, defaultValue = "0")
                                                                      int                         pageSize,
                                                                      @RequestParam(required = false, defaultValue = "false")
                                                                      boolean                     forLineage,
                                                                      @RequestParam(required = false, defaultValue = "false")
                                                                      boolean                     forDuplicateProcessing,
                                                                      @RequestBody(required = false)
                                                                          SecurityTagQueryProperties requestBody)
    {
        return restAPI.getSecurityTaggedElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the ownership classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-ownership")

    @Operation(summary="getOwnersElements",
            description="Return information about the elements classified with the ownership classification.",
            externalDocs=@ExternalDocumentation(description="Governance roles", url="https://egeria-project.org/types/4/0445-Governance-Roles/"))

    public MetadataElementSummariesResponse getOwnersElements(@PathVariable String                      serverName,
                                                              @PathVariable String                        urlMarker,
                                                              @RequestParam(required = false, defaultValue = "0")
                                                              int                         startFrom,
                                                              @RequestParam(required = false, defaultValue = "0")
                                                              int                         pageSize,
                                                              @RequestParam(required = false, defaultValue = "false")
                                                              boolean                     forLineage,
                                                              @RequestParam(required = false, defaultValue = "false")
                                                              boolean                     forDuplicateProcessing,
                                                              @RequestBody(required = false)
                                                                  FindNameProperties requestBody)
    {
        return restAPI.getOwnersElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the elements classified with the subject area classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getMembersOfSubjectArea",
            description="Return information about the elements classified with the subject area classification.",
            externalDocs=@ExternalDocumentation(description="Subject areas", url="https://egeria-project.org/types/4/0425-Subject-Areas/"))

    public MetadataElementSummariesResponse getMembersOfSubjectArea(@PathVariable String                      serverName,
                                                                    @PathVariable String                        urlMarker,
                                                                    @RequestParam(required = false, defaultValue = "0")
                                                                    int                         startFrom,
                                                                    @RequestParam(required = false, defaultValue = "0")
                                                                    int                         pageSize,
                                                                    @RequestParam(required = false, defaultValue = "false")
                                                                    boolean                     forLineage,
                                                                    @RequestParam(required = false, defaultValue = "false")
                                                                    boolean                     forDuplicateProcessing,
                                                                    @RequestBody(required = false)
                                                                        FindNameProperties requestBody)
    {
        return restAPI.getMembersOfSubjectArea(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/by-asset-origin")

    @Operation(summary="getAssetsByOrigin",
            description="Return information about the assets from a specific origin.",
            externalDocs=@ExternalDocumentation(description="Asset Origins", url="https://egeria-project.org/types/4/0440-Organizational-Controls/"))

    public MetadataElementSummariesResponse getAssetsByOrigin(@PathVariable String                      serverName,
                                                              @PathVariable String                        urlMarker,
                                                              @RequestParam(required = false, defaultValue = "0")
                                                              int                         startFrom,
                                                              @RequestParam(required = false, defaultValue = "0")
                                                              int                         pageSize,
                                                              @RequestParam(required = false, defaultValue = "false")
                                                              boolean                     forLineage,
                                                              @RequestParam(required = false, defaultValue = "false")
                                                              boolean                     forDuplicateProcessing,
                                                              @RequestBody(required = false)
                                                                  FindAssetOriginProperties requestBody)
    {
        return restAPI.getAssetsByOrigin(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getMeanings",
            description="Retrieve the glossary terms linked via a SemanticAssignment relationship to the requested element.",
            externalDocs=@ExternalDocumentation(description="Semantic assignment", url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public RelatedMetadataElementSummariesResponse getMeanings(@PathVariable String                        serverName,
                                                               @PathVariable String                        urlMarker,
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
                                                                   SemanticAssignmentQueryProperties requestBody)
    {
        return restAPI.getMeanings(serverName, urlMarker, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getSemanticAssignees",
            description="Retrieve the elements linked via a SemanticAssignment relationship to the requested glossary term",
            externalDocs=@ExternalDocumentation(description="Semantic assignment", url="https://egeria-project.org/types/3/0370-Semantic-Assignment/"))

    public RelatedMetadataElementSummariesResponse getSemanticAssignees(@PathVariable String                        serverName,
                                                                        @PathVariable String                        urlMarker,
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
                                                                            SemanticAssignmentQueryProperties requestBody)
    {
        return restAPI.getSemanticAssignees(serverName, urlMarker, glossaryTermGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getGovernedElements",
            description="Retrieve the governance definitions linked via a GovernedBy relationship to the requested element.",
            externalDocs=@ExternalDocumentation(description="Governance definitions", url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public RelatedMetadataElementSummariesResponse getGovernedElements(@PathVariable String                        serverName,
                                                                       @PathVariable String                        urlMarker,
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
                                                                           FindProperties requestBody)
    {
        return restAPI.getGovernedElements(serverName, urlMarker, governanceDefinitionGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getGovernedByDefinitions",
            description="Retrieve the elements linked via a GovernedBy relationship to the requested governance definition.",
            externalDocs=@ExternalDocumentation(description="Governance definitions", url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public RelatedMetadataElementSummariesResponse getGovernedByDefinitions(@PathVariable String                        serverName,
                                                                  @PathVariable String                        urlMarker,
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
                                                                      FindProperties requestBody)
    {
        return restAPI.getGovernedByDefinitions(serverName, urlMarker, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/elements/{elementGUID}/source")

    @Operation(summary="getSourceElements",
            description="Retrieve the elements linked via a SourceFrom relationship to the requested element.  The elements returned were used to create the requested element.  Typically only one element is returned.",
            externalDocs=@ExternalDocumentation(description="Templates", url="https://egeria-project.org/types/0/0011-Managing-Referenceables/"))

    public RelatedMetadataElementSummariesResponse getSourceElements(@PathVariable String                        serverName,
                                                                     @PathVariable String                        urlMarker,
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
                                                                         FindProperties requestBody)
    {
        return restAPI.getSourceElements(serverName, urlMarker, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the elements linked via a "SourcedFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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

    @Operation(summary="getElementsSourcedFrom",
            description="Retrieve the elements linked via a SourcedFrom relationship to the requested element. The elements returned were created using the requested element as a template.",
            externalDocs=@ExternalDocumentation(description="Templates", url="https://egeria-project.org/types/0/0011-Managing-Referenceables/"))

    public RelatedMetadataElementSummariesResponse getElementsSourcedFrom(@PathVariable String                        serverName,
                                                                          @PathVariable String                        urlMarker,
                                                                          @PathVariable String                        elementGUID,
                                                                          @RequestParam (required = false, defaultValue = "0")
                                                                         int                           startFrom,
                                                                          @RequestParam (required = false, defaultValue = "0")
                                                                         int                           pageSize,
                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                         boolean                       forLineage,
                                                                          @RequestParam (required = false, defaultValue = "false")
                                                                         boolean                       forDuplicateProcessing,
                                                                          @RequestBody  (required = false)
                                                                             FindProperties requestBody)
    {
        return restAPI.getElementsSourcedFrom(serverName, urlMarker, elementGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
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
                                                                   @PathVariable String                        urlMarker,
                                                                   @PathVariable String  elementGUID,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                   boolean forDuplicateProcessing,
                                                                   @RequestBody  (required = false)
                                                                       FindRequest requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, urlMarker, elementGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
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
                                                                         @PathVariable String                        urlMarker,
                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                         boolean         forLineage,
                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                         boolean         forDuplicateProcessing,
                                                                         @RequestBody (required = false) FindPropertyNameProperties requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, urlMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
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
                                                           @PathVariable String                        urlMarker,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean         forLineage,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean         forDuplicateProcessing,
                                                           @RequestBody  FindPropertyNameProperties requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, urlMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                        @PathVariable String                        urlMarker,
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
        return restAPI.getElements(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                                       @PathVariable String                        urlMarker,
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
        return restAPI.getElementsByPropertyValue(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must only be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                                        @PathVariable String                        urlMarker,
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
        return restAPI.findElementsByPropertyValue(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                                        @PathVariable String                        urlMarker,
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
        return restAPI.getElementsByClassification(serverName, urlMarker, classificationName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
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
                                                                                         @PathVariable String                        urlMarker,
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
        return restAPI.getElementsByClassificationWithPropertyValue(serverName, urlMarker, classificationName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
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
                                                                                          @PathVariable String                        urlMarker,
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
        return restAPI.findElementsByClassificationWithPropertyValue(serverName, urlMarker, classificationName, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Retrieve related elements of any type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                                      @PathVariable String                        urlMarker,
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
        return restAPI.getRelatedElements(serverName, urlMarker, elementGUID, null, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }




    /**
     * Retrieve related elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                                      @PathVariable String                        urlMarker,
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
        return restAPI.getRelatedElements(serverName, urlMarker, elementGUID, relationshipTypeName, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
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
                                                                                       @PathVariable String                        urlMarker,
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
        return restAPI.getRelatedElementsWithPropertyValue(serverName, urlMarker, elementGUID, relationshipTypeName, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
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
            description="Retrieve elements linked via the requested relationship type name and with the requested value found in one of the relationship's properties specified (or any property if no property names are specified).  The value must only be contained in the properties rather than needing to be an exact match.  An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public RelatedMetadataElementSummariesResponse findRelatedElementsWithPropertyValue(@PathVariable String                       serverName,
                                                                                        @PathVariable String                        urlMarker,
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
        return restAPI.findRelatedElementsWithPropertyValue(serverName, urlMarker, elementGUID, relationshipTypeName, startingAtEnd, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/relationships")

    @Operation(summary="getRelationships",
            description="Retrieve relationships of the requested relationship type name (passed in the request body as openMetadataType).",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationships(@PathVariable String                    serverName,
                                                                  @PathVariable String                        urlMarker,
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
        return restAPI.getRelationships(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/relationships/with-exact-property-value")

    @Operation(summary="getRelationshipsWithPropertyValue",
            description="Retrieve relationships of the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must match exactly.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                   @PathVariable String                        urlMarker,
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
        return restAPI.getRelationshipsWithPropertyValue(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
    @PostMapping(path = "/relationships/with-property-value-search")

    @Operation(summary="findRelationshipsWithPropertyValue",
            description="Retrieve relationships of the requested relationship type name and with the requested a value found in one of the relationship's properties specified.  The value must only be contained in the properties rather than needing to be an exact match.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(@PathVariable String                       serverName,
                                                                                    @PathVariable String                        urlMarker,
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
        return restAPI.findRelationshipsWithPropertyValue(serverName, urlMarker, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the header for the instance identified by the supplied unique identifier.  It may be an element (entity) or a relationship between elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
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
                                                         @PathVariable String                        urlMarker,
                                                         @PathVariable String       guid,
                                                         @RequestParam(required = false, defaultValue = "false")
                                                         boolean                       forLineage,
                                                         @RequestParam (required = false, defaultValue = "false")
                                                         boolean                       forDuplicateProcessing,
                                                         @RequestBody(required = false) ResultsRequestBody requestBody)
    {
        return restAPI.retrieveInstanceForGUID(serverName, urlMarker, guid, forLineage, forDuplicateProcessing, requestBody);
    }
}

