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
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindDigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagQueryProperties;
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
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public OpenMetadataRootElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                    @PathVariable String                        urlMarker,
                                                                    @PathVariable String  elementGUID,
                                                                    @RequestBody  (required = false)
                                                                    GetRequestBody requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, urlMarker, elementGUID, requestBody);
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
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public OpenMetadataRootElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestBody (required = false) FindPropertyNameProperties requestBody)
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
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/elements/guid-by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/gaf-metadata-management/"))

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @PathVariable String                        urlMarker,
                                                           @RequestBody  FindPropertyNameProperties requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-type")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElements",
            description="Retrieve elements of the requested type name.  If no type name is specified then any type of element may be returned.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getElements(@PathVariable String                    serverName,
                                                        @PathVariable String                        urlMarker,
                                                        @RequestBody  (required = false)
                                                        ResultsRequestBody requestBody)
    {
        return restAPI.getElements(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/elements/by-exact-property-value")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getElementsByPropertyValue",
            description="Retrieve elements by a value found in one of the properties specified.  The value must match exactly. An open metadata type name may be supplied to restrict the results.",
            externalDocs=@ExternalDocumentation(description="Open Metadata Types", url="https://egeria-project.org/types/"))

    public OpenMetadataRootElementsResponse getElementsByPropertyValue(@PathVariable String                       serverName,
                                                                       @PathVariable String                        urlMarker,
                                                                       @RequestBody  (required = false)
                                                                       FindPropertyNamesProperties requestBody)
    {
        return restAPI.getElementsByPropertyValue(serverName, urlMarker, requestBody);
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
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
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
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
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
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
     * PropertyServerException - there is a problem retrieving information from the property server(s) or
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
     *  PropertyServerException there is a problem updating the element properties in the property server.
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

