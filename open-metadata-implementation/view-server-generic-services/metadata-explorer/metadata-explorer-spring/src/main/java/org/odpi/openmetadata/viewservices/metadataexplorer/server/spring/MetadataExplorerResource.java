/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.metadataexplorer.server.MetadataExplorerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MetadataExplorerResource provides part of the server-side implementation of the Metadata Explorer OMVS.
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
@Tag(name="API: Metadata Explorer OMVS", description="The Metadata Explorer OMVS provides APIs for supporting the search, query and retrieval of open metadata.  It is an advanced API for users that understand the [Open Metadata Types](https://egeria-project.org/types/).",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

public class MetadataExplorerResource
{
    private final MetadataExplorerRESTServices restAPI = new MetadataExplorerRESTServices();

    /**
     * Default constructor
     */
    public MetadataExplorerResource()
    {
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  elementGUID,
                                                                @PathVariable String  urlMarker,
                                                                @RequestBody (required = false)
                                                                    GetRequestBody requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, elementGUID, urlMarker, requestBody);
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
    @PostMapping(path = "/metadata-elements/by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                      @PathVariable String          urlMarker,
                                                                      @RequestBody  UniqueNameRequestBody requestBody)
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
    @PostMapping(path = "/metadata-elements/guid-by-unique-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @PathVariable String          urlMarker,
                                                           @RequestBody  UniqueNameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of object to retrieve
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementHistory",
            description="Retrieve all the versions of an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse getMetadataElementHistory(@PathVariable String                 serverName,
                                                                  @PathVariable String                 elementGUID,
                                                                  @PathVariable String                 urlMarker,
                                                                  @RequestBody(required = false)
                                                                  HistoryRequestBody     requestBody)
    {
        return restAPI.getMetadataElementHistory(serverName, elementGUID, urlMarker, requestBody);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMetadataElementsWithString",
            description="Retrieve the metadata elements that contain the requested string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse findMetadataElementsWithString(@PathVariable String                  serverName,
                                                                       @PathVariable String                  urlMarker,
                                                                       @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findMetadataElementsWithString(serverName, urlMarker, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param anchorGUID unique identifier of anchor
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/metadata-elements/by-search-string/for-anchor/{anchorGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsForAnchor",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied anchorGUID.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesResponse findElementsForAnchor(@PathVariable String                  serverName,
                                                             @PathVariable String                  urlMarker,
                                                             @PathVariable String                  anchorGUID,
                                                             @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsForAnchor(serverName, urlMarker, anchorGUID, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param anchorDomainName name of open metadata type for the domain
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */
    @PostMapping(path = "/metadata-elements/by-search-string/in-anchor-domain/{anchorDomainName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsInAnchorDomain",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied domain name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesListResponse findElementsInAnchorDomain(@PathVariable String                  serverName,
                                                                      @PathVariable String                  urlMarker,
                                                                      @PathVariable String                  anchorDomainName,
                                                                      @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsInAnchorDomain(serverName, urlMarker, anchorDomainName, requestBody);
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param requestBody string to search for in text
     *
     * @return list of results for assets that match the search string or
     * InvalidParameterException the searchString is invalid or
     * PropertyServerException there is a problem access in the property server or
     * UserNotAuthorizedException the user does not have access to the properties
     */

    @PostMapping(path = "/metadata-elements/by-search-string/in-anchor-scope/{anchorScopeGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findElementsInAnchorScope",
            description="Return a list of elements with the requested search string in their (display, resource)name, qualified name, title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).  The breadth of the search is determined by the supplied scope guid.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/features/anchor-management/overview/"))

    public AnchorSearchMatchesListResponse findElementsInAnchorScope(@PathVariable String                  serverName,
                                                                     @PathVariable String                  urlMarker,
                                                                     @PathVariable String                  anchorScopeGUID,
                                                                     @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findElementsInAnchorScope(serverName, urlMarker, anchorScopeGUID, requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/any-type")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public RelatedMetadataElementListResponse getAllRelatedMetadataElements(@PathVariable String  serverName,
                                                                            @PathVariable String  elementGUID,
                                                                            @PathVariable String  urlMarker,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int     startingAtEnd,
                                                                            @RequestBody (required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  elementGUID,
                                                  null,
                                                  urlMarker,
                                                  startingAtEnd,
                                                  requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element via a specific relationship type.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/type/{relationshipTypeName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element via a specific relationship type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public RelatedMetadataElementListResponse getRelatedMetadataElements(@PathVariable String  serverName,
                                                                         @PathVariable String  elementGUID,
                                                                         @PathVariable String  relationshipTypeName,
                                                                         @PathVariable String  urlMarker,
                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                         int     startingAtEnd,
                                                                         @RequestBody (required = false)
                                                                         ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  elementGUID,
                                                  relationshipTypeName,
                                                  urlMarker,
                                                  startingAtEnd,
                                                  requestBody);
    }



    /**
     * Retrieve the relationships linking the supplied elements.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.

     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-any-type/to-elements/{metadataElementAtEnd2GUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getAllMetadataElementRelationships(@PathVariable String  serverName,
                                                                                   @PathVariable String  metadataElementAtEnd1GUID,
                                                                                   @PathVariable String  metadataElementAtEnd2GUID,
                                                                                   @PathVariable String  urlMarker,
                                                                                   @RequestBody(required = false)
                                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       metadataElementAtEnd1GUID,
                                                       null,
                                                       metadataElementAtEnd2GUID,
                                                       urlMarker,
                                                       requestBody);
    }


    /**
     * Return all the elements that are anchored to an element plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker      the identifier of the view service (for example runtime-manager for the Runtime Manager OMVS)
     * @param elementGUID  unique identifier for the element
     * @param requestBody effective time and asOfTime
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - there is a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/with-anchored-elements")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAnchoredElementsGraph",
            description="Return all the elements that are anchored to an element plus relationships between these elements and to other elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataGraphResponse getAnchoredElementsGraph(@PathVariable String          serverName,
                                                              @PathVariable String          urlMarker,
                                                              @PathVariable String          elementGUID,
                                                              @RequestBody (required = false)
                                                              ResultsRequestBody requestBody)
    {
        return restAPI.getAnchoredElementsGraph(serverName, urlMarker, elementGUID, requestBody);
    }



    /**
     * Retrieve the relationships linking the supplied elements via a specific type of relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-type/{relationshipTypeName}/to-elements/{metadataElementAtEnd2GUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements via a specific type of relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getMetadataElementRelationships(@PathVariable String  serverName,
                                                                                @PathVariable String  metadataElementAtEnd1GUID,
                                                                                @PathVariable String  relationshipTypeName,
                                                                                @PathVariable String  metadataElementAtEnd2GUID,
                                                                                @PathVariable String  urlMarker,
                                                                                @RequestBody(required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       metadataElementAtEnd1GUID,
                                                       relationshipTypeName,
                                                       metadataElementAtEnd2GUID,
                                                       urlMarker,
                                                       requestBody);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-conditions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findMetadataElements",
            description="Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse findMetadataElements(@PathVariable String          serverName,
                                                             @PathVariable String          urlMarker,
                                                             @RequestBody (required = false)
                                                             FindRequestBody requestBody)
    {
        return restAPI.findMetadataElements(serverName, urlMarker, requestBody);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-search-conditions")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findRelationshipsBetweenMetadataElements",
            description="Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse findRelationshipsBetweenMetadataElements(@PathVariable String          serverName,
                                                                                         @PathVariable String          urlMarker,
                                                                                         @RequestBody  FindRelationshipRequestBody requestBody)
    {
        return restAPI.findRelationshipsBetweenMetadataElements(serverName, urlMarker, requestBody);
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-guid/{relationshipGUID}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipByGUID",
            description="Retrieve the relationship using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipResponse getRelationshipByGUID(@PathVariable String  serverName,
                                                                  @PathVariable String  relationshipGUID,
                                                                  @PathVariable String  urlMarker,
                                                                  @RequestBody (required = false)
                                                                      GetRequestBody requestBody)
    {
        return restAPI.getRelationshipByGUID(serverName, relationshipGUID, urlMarker, requestBody);
    }



    /**
     * Retrieve all the versions of a relationship.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipGUID unique identifier of object to retrieve
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/relationships/{relationshipGUID}/history")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipHistory",
            description="Retrieve all the versions of a relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getRelationshipHistory(@PathVariable String                 serverName,
                                                                       @PathVariable String                 relationshipGUID,
                                                                       @PathVariable String                 urlMarker,
                                                                       @RequestBody(required = false)
                                                                       HistoryRequestBody     requestBody)
    {
        return restAPI.getRelationshipHistory(serverName,
                                              relationshipGUID,
                                              urlMarker,
                                              requestBody);
    }
}
