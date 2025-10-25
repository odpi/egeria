/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.governanceofficer.server.GovernanceOfficerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The GovernanceOfficerResource provides part of the server-side implementation of the Governance Officer OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Governance Officer OMVS", description="The Governance Officer OMVS provides APIs for supporting the creation and editing of a new governance domain.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/governance-officer/overview/"))

public class GovernanceOfficerResource
{
    private final GovernanceOfficerRESTServices restAPI = new GovernanceOfficerRESTServices();

    /**
     * Default constructor
     */
    public GovernanceOfficerResource()
    {
    }


    /**
     * Create a governance definition.
     *
     * @param serverName                 name of called server.
     * @param urlMarker  view service URL marker
     * @param requestBody             properties for the governance definition.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createGovernanceDefinition",
            description="Create a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GUIDResponse createGovernanceDefinition(@PathVariable String                               serverName,
                                                   @PathVariable String             urlMarker,
                                                   @RequestBody (required = false)
                                                   NewElementRequestBody requestBody)
    {
        return restAPI.createGovernanceDefinition(serverName, urlMarker, requestBody);
    }


    /**
     * Create a new metadata element to represent a governance definition using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param urlMarker  view service URL marker
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createGovernanceDefinitionFromTemplate",
            description="Create a new metadata element to represent a governance definition using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public GUIDResponse createGovernanceDefinitionFromTemplate(@PathVariable
                                                               String              serverName,
                                                               @PathVariable String             urlMarker,
                                                               @RequestBody (required = false)
                                                               TemplateRequestBody requestBody)
    {
        return restAPI.createGovernanceDefinitionFromTemplate(serverName, urlMarker, requestBody);
    }


    /**
     * Update the properties of a governance definition.
     *
     * @param serverName         name of called server.
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateGovernanceDefinition",
            description="Update the properties of a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse updateGovernanceDefinition(@PathVariable
                                                   String                                  serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                                  governanceDefinitionGUID,
                                                   @RequestBody (required = false)
                                                   UpdateElementRequestBody requestBody)
    {
        return restAPI.updateGovernanceDefinition(serverName, urlMarker, governanceDefinitionGUID, requestBody);
    }


    /**
     * Attach two peer governance definitions.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/peer-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkPeerDefinitions",
            description="Attach two peer governance definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse linkPeerDefinitions(@PathVariable
                                            String                     serverName,
                                            @PathVariable String             urlMarker,
                                            @PathVariable
                                            String                     governanceDefinitionOneGUID,
                                            @PathVariable
                                            String                     relationshipTypeName,
                                            @PathVariable
                                            String                     governanceDefinitionTwoGUID,
                                            @RequestBody (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkPeerDefinitions(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Detach a governance definition from one of its peers.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/peer-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachPeerDefinitions",
            description="Detach a governance definition from one of its peers.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse detachPeerDefinitions(@PathVariable
                                              String                    serverName,
                                              @PathVariable String             urlMarker,
                                              @PathVariable
                                              String                     governanceDefinitionOneGUID,
                                              @PathVariable
                                              String                     relationshipTypeName,
                                              @PathVariable
                                              String                     governanceDefinitionTwoGUID,
                                              @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachPeerDefinitions(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Attach a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/supporting-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="attachSupportingDefinition",
            description="Attach a supporting governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse attachSupportingDefinition(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                     governanceDefinitionOneGUID,
                                                   @PathVariable
                                                   String                     relationshipTypeName,
                                                   @PathVariable
                                                   String                     governanceDefinitionTwoGUID,
                                                   @RequestBody (required = false)
                                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.attachSupportingDefinition(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Detach a governance definition from a supporting governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionOneGUID unique identifier of the first governance definition
     * @param governanceDefinitionTwoGUID unique identifier of the second governance definition
     * @param relationshipTypeName name of the relationship to use
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionOneGUID}/supporting-definitions/{relationshipTypeName}/{governanceDefinitionTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSupportingDefinition",
            description="Detach a governance definition from a supporting governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse detachSupportingDefinition(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                     governanceDefinitionOneGUID,
                                                   @PathVariable
                                                   String                     relationshipTypeName,
                                                   @PathVariable
                                                   String                     governanceDefinitionTwoGUID,
                                                   @RequestBody (required = false)
                                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSupportingDefinition(serverName, urlMarker, governanceDefinitionOneGUID, governanceDefinitionTwoGUID, relationshipTypeName, requestBody);
    }


    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param definitionGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by/definition/{definitionGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addGovernanceDefinitionToElement",
            description="Link a governance definition to an element using the GovernedBy relationship.",
            externalDocs=@ExternalDocumentation(description="Governance Definitions",
                    url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public VoidResponse addGovernanceDefinitionToElement(@PathVariable String       serverName,
                                                         @PathVariable String       urlMarker,
                                                         @PathVariable String       elementGUID,
                                                         @PathVariable String       definitionGUID,
                                                         @RequestBody  (required = false)
                                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.addGovernanceDefinitionToElement(serverName, urlMarker, elementGUID, definitionGUID, requestBody);
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param definitionGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{elementGUID}/governed-by/definition/{definitionGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="removeGovernanceDefinitionFromElement",
            description="Remove the GovernedBy relationship between a governance definition and an element.",
            externalDocs=@ExternalDocumentation(description="Governance Definitions",
                    url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public VoidResponse removeGovernanceDefinitionFromElement(@PathVariable String  serverName,
                                                              @PathVariable String  urlMarker,
                                                              @PathVariable String  elementGUID,
                                                              @PathVariable String  definitionGUID,
                                                              @RequestBody  (required = false)
                                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.removeGovernanceDefinitionFromElement(serverName, urlMarker, elementGUID, definitionGUID, requestBody);
    }


    /**
     * Attach a governance metric to an asset that represents the data store where the measurements are located.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param governanceMetricGUID            unique identifier of the metric
     * @param dataSourceGUID                   unique identifier of the asset
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-metrics/{governanceMetricGUID}/measurements/{dataSourceGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkGovernanceResults",
            description="Attach a governance metric to an asset that represents the data store where the measurements are located.",
            externalDocs=@ExternalDocumentation(description="Governance Definitions",
                    url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public VoidResponse linkGovernanceResults(@PathVariable String       serverName,
                                              @PathVariable String       urlMarker,
                                              @PathVariable String       governanceMetricGUID,
                                              @PathVariable String       dataSourceGUID,
                                              @RequestBody  (required = false)
                                                  NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkGovernanceResults(serverName, urlMarker, governanceMetricGUID, dataSourceGUID, requestBody);
    }


    /**
     * Detach a governance metric from an asset that represents the data store where the measurements are located.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param governanceMetricGUID            unique identifier of the metric
     * @param dataSourceGUID                   unique identifier of the asset
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/governance-metrics/{governanceMetricGUID}/measurements/{dataSourceGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachGovernanceResults",
            description="Detach a governance metric from an asset that represents the data store where the measurements are located.",
            externalDocs=@ExternalDocumentation(description="Governance Definitions",
                    url="https://egeria-project.org/types/4/0401-Governance-Definitions/"))

    public VoidResponse detachGovernanceResults(@PathVariable String  serverName,
                                                              @PathVariable String  urlMarker,
                                                              @PathVariable String  governanceMetricGUID,
                                                              @PathVariable String  dataSourceGUID,
                                                              @RequestBody  (required = false)
                                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachGovernanceResults(serverName, urlMarker, governanceMetricGUID, dataSourceGUID, requestBody);
    }


    /* =======================================
     * Licenses
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element being licensed
     * @param licenseTypeGUID unique identifier for the license type
     * @param requestBody the properties of the license
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/{elementGUID}/license-types/{licenseTypeGUID}/license")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse licenseElement(@PathVariable String                    serverName,
                                       @PathVariable String                    urlMarker,
                                       @PathVariable String                    elementGUID,
                                       @PathVariable String                    licenseTypeGUID,
                                       @RequestBody NewRelationshipRequestBody requestBody)
    {
        return restAPI.licenseElement(serverName, urlMarker, elementGUID, licenseTypeGUID, requestBody);
    }


    /**
     * Update the properties of a license.  Remember to include the licenseId in the properties if the element has multiple
     * licenses for the same license type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param licenseGUID unique identifier for the license relationship
     * @param requestBody the properties of the license
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/licenses/{licenseGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateLicense(@PathVariable String                        serverName,
                                      @PathVariable String                        urlMarker,
                                      @PathVariable String                        licenseGUID,
                                      @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateLicense(serverName, urlMarker, licenseGUID, requestBody);
    }


    /**
     * Remove the license for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param licenseGUID unique identifier for the license relationship
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/licenses/{licenseGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse unlicenseElement(@PathVariable String           serverName,
                                         @PathVariable String           urlMarker,
                                         @PathVariable String           licenseGUID,
                                         @RequestBody DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.unlicenseElement(serverName, urlMarker, licenseGUID, requestBody);
    }



    /* =======================================
     * Certifications
     */

    /**
     * Link an element to a certification type and include details of the certification in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param requestBody the properties of the certification
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/elements/{elementGUID}/certification-types/{certificationTypeGUID}/certify")
    @SecurityRequirement(name = "BearerAuthorization")

    public GUIDResponse certifyElement(@PathVariable String                    serverName,
                                       @PathVariable String                    urlMarker,
                                       @PathVariable String                    elementGUID,
                                       @PathVariable String                    certificationTypeGUID,
                                       @RequestBody NewRelationshipRequestBody requestBody)
    {
        return restAPI.certifyElement(serverName, urlMarker, elementGUID, certificationTypeGUID, requestBody);
    }


    /**
     * Update the properties of a certification.  Remember to include the certificationId in the properties if the element has multiple
     * certifications for the same certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param certificationGUID unique identifier for the certification relationship
     * @param requestBody the properties of the certification
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certifications/{certificationGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse updateCertification(@PathVariable String                        serverName,
                                            @PathVariable String                        urlMarker,
                                            @PathVariable String                        certificationGUID,
                                            @RequestBody  UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateCertification(serverName, urlMarker, certificationGUID, requestBody);
    }


    /**
     * Remove the certification for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param certificationGUID unique identifier for the certification relationship
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping (path = "/certifications/{certificationGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    public VoidResponse decertifyElement(@PathVariable String           serverName,
                                         @PathVariable String           urlMarker,
                                         @PathVariable String           certificationGUID,
                                         @RequestBody DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.decertifyElement(serverName, urlMarker, certificationGUID, requestBody);
    }


    /**
     * Delete a governance definition.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteGovernanceDefinition",
            description="Delete a governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public VoidResponse deleteGovernanceDefinition(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String                    governanceDefinitionGUID,
                                                   @RequestBody (required = false)
                                                       DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteGovernanceDefinition(serverName, urlMarker, governanceDefinitionGUID, requestBody);
    }


    /**
     * Returns the list of governance definitions with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceDefinitionsByName",
            description="Returns the list of governance definitions with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public OpenMetadataRootElementsResponse getGovernanceDefinitionsByName(@PathVariable
                                                                        String            serverName,
                                                                        @PathVariable String             urlMarker,
                                                                        @RequestBody (required = false)
                                                                        FilterRequestBody requestBody)
    {
        return restAPI.getGovernanceDefinitionsByName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the list of governance definition metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findGovernanceDefinitions",
            description="Retrieve the list of governance definition metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public  OpenMetadataRootElementsResponse findGovernanceDefinitions(@PathVariable
                                                                   String                  serverName,
                                                                   @PathVariable String             urlMarker,
                                                                   @RequestBody (required = false)
                                                                   SearchStringRequestBody requestBody)
    {
        return restAPI.findGovernanceDefinitions(serverName, urlMarker, requestBody);
    }


    /**
     * Return the properties of a specific governance definition.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-definitions/{governanceDefinitionGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceDefinitionByGUID",
            description="Return the properties of a specific governance definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-definition"))

    public OpenMetadataRootElementResponse getGovernanceDefinitionByGUID(@PathVariable
                                                                      String             serverName,
                                                                      @PathVariable String             urlMarker,
                                                                      @PathVariable
                                                                      String             governanceDefinitionGUID,
                                                                      @RequestBody (required = false)
                                                                             GetRequestBody requestBody)
    {
        return restAPI.getGovernanceDefinitionByGUID(serverName, urlMarker, governanceDefinitionGUID, requestBody);
    }



    /* =====================================================================================================================
     * A governance action process describes a well-defined series of steps that gets something done.
     * The steps are defined using GovernanceActionProcessSteps.
     */

    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier
     * along with the flow definition describing its implementation.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param processGUID unique identifier of the requested metadata element
     * @param requestBody effective time
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/governance-action-processes/{processGUID}/graph")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getGovernanceActionProcessGraph",
            description="Retrieve the governance action process metadata element with the supplied " +
                    "unique identifier along with the flow definition describing its implementation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/governance-action-process"))

    public GovernanceActionProcessGraphResponse getGovernanceActionProcessGraph(@PathVariable String                   serverName,
                                                                                @PathVariable String             urlMarker,
                                                                                @PathVariable String                   processGUID,
                                                                                @RequestBody(required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getGovernanceActionProcessGraph(serverName, urlMarker, processGUID, requestBody);
    }


    /* =====================================================================================================================
     * Linking designs to implementation
     */

    /**
     * Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementations/{implementationGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDesignToImplementation",
            description="Attach a design object such as a solution component or governance definition to its implementation via the ImplementedBy relationship. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse linkDesignToImplementation(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String designGUID,
                                                   @PathVariable
                                                   String implementationGUID,
                                                   @RequestBody (required = false)
                                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDesignToImplementation(serverName, urlMarker, designGUID, implementationGUID, requestBody);
    }


    /**
     * Detach a design object such as a solution component or governance definition from its implementation. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID             unique identifier of the  governance definition, solution component etc
     * @param implementationGUID     unique identifier of the implementation
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementations/{implementationGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDesignFromImplementation",
            description="Detach a design object such as a solution component or governance definition from its implementation. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse detachDesignFromImplementation(@PathVariable
                                                       String                    serverName,
                                                       @PathVariable String             urlMarker,
                                                       @PathVariable
                                                       String                     designGUID,
                                                       @PathVariable
                                                       String                     implementationGUID,
                                                       @RequestBody (required = false)
                                                           DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDesignFromImplementation(serverName, urlMarker, designGUID, implementationGUID, requestBody);
    }


    /**
     * Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID unique identifier of the design object
     * @param implementationResourceGUID unique identifier of the implementation resource
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementation-resources/{implementationResourceGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkImplementationResource",
            description="Attach a design object such as a solution component or governance definition to one of its implementation resources via the ImplementationResource relationship. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse linkImplementationResource(@PathVariable
                                                   String                     serverName,
                                                   @PathVariable String             urlMarker,
                                                   @PathVariable
                                                   String designGUID,
                                                   @PathVariable
                                                   String implementationResourceGUID,
                                                   @RequestBody (required = false)
                                                       NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkImplementationResource(serverName, urlMarker, designGUID, implementationResourceGUID, requestBody);
    }


    /**
     * Detach a design object such as a solution component or governance definition from one of its implementation resources. Request body is optional.
     *
     * @param serverName         name of called server
     * @param urlMarker  view service URL marker
     * @param designGUID unique identifier of the design object
     * @param implementationResourceGUID unique identifier of the implementation resource
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/designs/{designGUID}/implementation-resources/{implementationResourceGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachImplementationResource",
            description="Detach a design object such as a solution component or governance definition from one of its implementation resources. Request body is optional.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/7/0737-Solution-Implementation/"))

    public VoidResponse detachImplementationResource(@PathVariable String                    serverName,
                                                     @PathVariable String             urlMarker,
                                                     @PathVariable String                     designGUID,
                                                     @PathVariable String implementationResourceGUID,
                                                     @RequestBody (required = false) DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachImplementationResource(serverName, urlMarker, designGUID, implementationResourceGUID, requestBody);
    }
}
