/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexplorer.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.*;
import org.odpi.openmetadata.viewservices.metadataexplorer.server.MetadataExplorerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The MetadataExplorerResource provides part of the server-side implementation of the Metadata Explorer OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/metadata-explorer")

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
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}")

    @Operation(summary="getMetadataElementByGUID",
            description="Retrieve the metadata element using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementResponse getMetadataElementByGUID(@PathVariable String  serverName,
                                                                @PathVariable String  elementGUID,
                                                                @RequestParam (required = false)
                                                                String                         viewServiceURLMarker,
                                                                @RequestParam (required = false, defaultValue = "asset-manager")
                                                                String                         accessServiceURLMarker,
                                                                @RequestParam (required = false, defaultValue = "false")
                                                                boolean forLineage,
                                                                @RequestParam (required = false, defaultValue = "false")
                                                                boolean forDuplicateProcessing,
                                                                @RequestBody (required = false)
                                                                EffectiveTimeRequestBody requestBody)
    {
        return restAPI.getMetadataElementByGUID(serverName, elementGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-unique-name")

    @Operation(summary="getMetadataElementByUniqueName",
            description="Retrieve the metadata element using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementResponse getMetadataElementByUniqueName(@PathVariable String          serverName,
                                                                      @RequestParam (required = false)
                                                                      String                         viewServiceURLMarker,
                                                                      @RequestParam (required = false, defaultValue = "asset-manager")
                                                                      String                         accessServiceURLMarker,
                                                                      @RequestParam (required = false, defaultValue = "false")
                                                                      boolean         forLineage,
                                                                      @RequestParam (required = false, defaultValue = "false")
                                                                      boolean         forDuplicateProcessing,
                                                                      @RequestBody NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementByUniqueName(serverName, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/guid-by-unique-name")

    @Operation(summary="getMetadataElementGUIDByUniqueName",
            description="Retrieve the metadata element GUID using its unique name (typically the qualified name, but it is possible to specify a different property name in the request body as long as it is unique).  If multiple matching instances are found, and exception is thrown.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public GUIDResponse getMetadataElementGUIDByUniqueName(@PathVariable String          serverName,
                                                           @RequestParam (required = false)
                                                           String                         viewServiceURLMarker,
                                                           @RequestParam (required = false, defaultValue = "asset-manager")
                                                           String                         accessServiceURLMarker,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean         forLineage,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                           boolean         forDuplicateProcessing,
                                                           @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getMetadataElementGUIDByUniqueName(serverName, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName name of the server to route the request to
     * @param elementGUID unique identifier of object to retrieve
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param startFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/metadata-elements/{elementGUID}/history")

    @Operation(summary="getMetadataElementHistory",
            description="Retrieve all the versions of an element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse getMetadataElementHistory(@PathVariable String                 serverName,
                                                                  @PathVariable String                 elementGUID,
                                                                  @RequestParam (required = false)
                                                                  String                         viewServiceURLMarker,
                                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                                  String                         accessServiceURLMarker,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                      int                    startFrom,
                                                                  @RequestParam (required = false, defaultValue = "0")
                                                                      int                    pageSize,
                                                                  @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                oldestFirst,
                                                                  @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                forLineage,
                                                                  @RequestParam (required = false, defaultValue = "false")
                                                                  boolean                forDuplicateProcessing,
                                                                  @RequestBody(required = false)
                                                                  HistoryRequestBody     requestBody)
    {
        return restAPI.getMetadataElementHistory(serverName, elementGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, startFrom, pageSize, oldestFirst, requestBody);
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-string")

    @Operation(summary="findMetadataElementsWithString",
            description="Retrieve the metadata elements that contain the requested string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse findMetadataElementsWithString(@PathVariable String                  serverName,
                                                                       @RequestParam (required = false)
                                                                       String                         viewServiceURLMarker,
                                                                       @RequestParam (required = false, defaultValue = "asset-manager")
                                                                       String                         accessServiceURLMarker,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 forLineage,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                 forDuplicateProcessing,
                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                       int                     startFrom,
                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                       int                     pageSize,
                                                                       @RequestBody SearchStringRequestBody requestBody)
    {
        return restAPI.findMetadataElementsWithString(serverName, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/any-type")

    @Operation(summary="getAllRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public RelatedMetadataElementListResponse getAllRelatedMetadataElements(@PathVariable String  serverName,
                                                                            @PathVariable String  elementGUID,
                                                                            @RequestParam (required = false)
                                                                            String                         viewServiceURLMarker,
                                                                            @RequestParam (required = false, defaultValue = "asset-manager")
                                                                            String                         accessServiceURLMarker,
                                                                            @RequestParam (required = false, defaultValue = "false")
                                                                            boolean forLineage,
                                                                            @RequestParam (required = false, defaultValue = "false")
                                                                            boolean forDuplicateProcessing,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int     startingAtEnd,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int     startFrom,
                                                                            @RequestParam (required = false, defaultValue = "0")
                                                                            int     pageSize,
                                                                            @RequestBody (required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  elementGUID,
                                                  null,
                                                  viewServiceURLMarker,
                                                  accessServiceURLMarker,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  startingAtEnd,
                                                  startFrom,
                                                  pageSize,
                                                  requestBody);
    }


    /**
     * Retrieve the metadata elements connected to the supplied element via a specific relationship type.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/related-elements/{elementGUID}/type/{relationshipTypeName}")

    @Operation(summary="getRelatedMetadataElements",
            description="Retrieve the metadata elements connected to the supplied element via a specific relationship type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public RelatedMetadataElementListResponse getRelatedMetadataElements(@PathVariable String  serverName,
                                                                         @PathVariable String  elementGUID,
                                                                         @PathVariable String  relationshipTypeName,
                                                                         @RequestParam (required = false)
                                                                         String                         viewServiceURLMarker,
                                                                         @RequestParam (required = false, defaultValue = "asset-manager")
                                                                         String                         accessServiceURLMarker,
                                                                         @RequestParam(required = false, defaultValue = "false")
                                                                         boolean forLineage,
                                                                         @RequestParam(required = false, defaultValue = "false")
                                                                         boolean forDuplicateProcessing,
                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                         int     startingAtEnd,
                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                         int     startFrom,
                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                         int     pageSize,
                                                                         @RequestBody (required = false)
                                                                         ResultsRequestBody requestBody)
    {
        return restAPI.getRelatedMetadataElements(serverName,
                                                  elementGUID,
                                                  relationshipTypeName,
                                                  viewServiceURLMarker,
                                                  accessServiceURLMarker,
                                                  forLineage,
                                                  forDuplicateProcessing,
                                                  startingAtEnd,
                                                  startFrom,
                                                  pageSize,
                                                  requestBody);
    }



    /**
     * Retrieve the relationships linking the supplied elements.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-any-type/to-elements/{metadataElementAtEnd2GUID}")

    @Operation(summary="getAllMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getAllMetadataElementRelationships(@PathVariable String  serverName,
                                                                                   @PathVariable String  metadataElementAtEnd1GUID,
                                                                                   @PathVariable String  metadataElementAtEnd2GUID,
                                                                                   @RequestParam (required = false)
                                                                                   String                         viewServiceURLMarker,
                                                                                   @RequestParam (required = false, defaultValue = "asset-manager")
                                                                                   String                         accessServiceURLMarker,
                                                                                   @RequestParam(required = false, defaultValue = "false")
                                                                                   boolean forLineage,
                                                                                   @RequestParam(required = false, defaultValue = "false")
                                                                                   boolean forDuplicateProcessing,
                                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                   int     startFrom,
                                                                                   @RequestParam (required = false, defaultValue = "0")
                                                                                   int     pageSize,
                                                                                   @RequestBody(required = false)
                                                                                   ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       metadataElementAtEnd1GUID,
                                                       null,
                                                       viewServiceURLMarker,
                                                       accessServiceURLMarker,
                                                       metadataElementAtEnd2GUID,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       startFrom,
                                                       pageSize,
                                                       requestBody);
    }


    /**
     * Retrieve the relationships linking the supplied elements via a specific type of relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/{metadataElementAtEnd1GUID}/linked-by-type/{relationshipTypeName}/to-elements/{metadataElementAtEnd2GUID}")

    @Operation(summary="getMetadataElementRelationships",
            description="Retrieve the relationships linking the supplied elements via a specific type of relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getMetadataElementRelationships(@PathVariable String  serverName,
                                                                                @PathVariable String  metadataElementAtEnd1GUID,
                                                                                @PathVariable String  relationshipTypeName,
                                                                                @PathVariable String  metadataElementAtEnd2GUID,
                                                                                @RequestParam (required = false)
                                                                                String                         viewServiceURLMarker,
                                                                                @RequestParam (required = false, defaultValue = "asset-manager")
                                                                                String                         accessServiceURLMarker,
                                                                                @RequestParam(required = false, defaultValue = "false")
                                                                                boolean forLineage,
                                                                                @RequestParam(required = false, defaultValue = "false")
                                                                                boolean forDuplicateProcessing,
                                                                                @RequestParam  (required = false, defaultValue = "0")
                                                                                int     startFrom,
                                                                                @RequestParam  (required = false, defaultValue = "0")
                                                                                int     pageSize,
                                                                                @RequestBody(required = false)
                                                                                ResultsRequestBody requestBody)
    {
        return restAPI.getMetadataElementRelationships(serverName,
                                                       metadataElementAtEnd1GUID,
                                                       relationshipTypeName,
                                                       metadataElementAtEnd2GUID,
                                                       viewServiceURLMarker,
                                                       accessServiceURLMarker,
                                                       forLineage,
                                                       forDuplicateProcessing,
                                                       startFrom,
                                                       pageSize,
                                                       requestBody);
    }


    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/metadata-elements/by-search-specification")

    @Operation(summary="findMetadataElements",
            description="Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataElementsResponse findMetadataElements(@PathVariable String          serverName,
                                                             @RequestParam (required = false)
                                                             String                         viewServiceURLMarker,
                                                             @RequestParam (required = false, defaultValue = "asset-manager")
                                                             String                         accessServiceURLMarker,
                                                             @RequestParam(required = false, defaultValue = "false")
                                                             boolean         forLineage,
                                                             @RequestParam(required = false, defaultValue = "false")
                                                             boolean         forDuplicateProcessing,
                                                             @RequestParam(required = false, defaultValue = "0")
                                                             int             startFrom,
                                                             @RequestParam(required = false, defaultValue = "0")
                                                             int             pageSize,
                                                             @RequestBody (required = false)
                                                             FindRequestBody requestBody)
    {
        return restAPI.findMetadataElements(serverName, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, startFrom, pageSize, requestBody);
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-search-specification")

    @Operation(summary="findRelationshipsBetweenMetadataElements",
            description="Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse findRelationshipsBetweenMetadataElements(@PathVariable String          serverName,
                                                                                         @RequestParam (required = false)
                                                                                         String                         viewServiceURLMarker,
                                                                                         @RequestParam (required = false, defaultValue = "asset-manager")
                                                                                         String                         accessServiceURLMarker,
                                                                                         @RequestParam(required = false, defaultValue = "false")
                                                                                         boolean         forLineage,
                                                                                         @RequestParam(required = false, defaultValue = "false")
                                                                                         boolean         forDuplicateProcessing,
                                                                                         @RequestParam (required = false, defaultValue = "0")
                                                                                         int             startFrom,
                                                                                         @RequestParam  (required = false, defaultValue = "0")
                                                                                         int             pageSize,
                                                                                         @RequestBody  FindRelationshipRequestBody requestBody)
    {
        return restAPI.findRelationshipsBetweenMetadataElements(serverName, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing,startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the metadata element
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException there is a problem accessing the metadata store
     */
    @PostMapping(path = "/relationships/by-guid/{relationshipGUID}")

    @Operation(summary="getRelationshipByGUID",
            description="Retrieve the relationship using its unique identifier.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipResponse getRelationshipByGUID(@PathVariable String  serverName,
                                                                  @PathVariable String  relationshipGUID,
                                                                  @RequestParam (required = false)
                                                                 String                         viewServiceURLMarker,
                                                                  @RequestParam (required = false, defaultValue = "asset-manager")
                                                                 String                         accessServiceURLMarker,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                 boolean forLineage,
                                                                  @RequestParam(required = false, defaultValue = "false")
                                                                 boolean forDuplicateProcessing,
                                                                  @RequestBody (required = false)
                                                                 EffectiveTimeRequestBody    requestBody)
    {
        return restAPI.getRelationshipByGUID(serverName, relationshipGUID, viewServiceURLMarker, accessServiceURLMarker, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Retrieve all the versions of a relationship.
     *
     * @param serverName name of the server to route the request to
     * @param relationshipGUID unique identifier of object to retrieve
     * @param viewServiceURLMarker optional view service URL marker (overrides accessServiceURLMarker)
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param startFrom the starting element number of the historical versions to return. This is used when retrieving
     *                         versions beyond the first page of results. Zero means start from the first element.
     * @param pageSize the maximum number of result versions that can be returned on this request. Zero means unrestricted
     *                 return results size.
     * @param oldestFirst  defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param requestBody the time window required
     * @return list of beans or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException there is a problem removing the properties from the repositories.
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/relationships/{relationshipGUID}/history")

    @Operation(summary="getRelationshipHistory",
            description="Retrieve all the versions of a relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/services/omvs/metadata-explorer/overview/"))

    public OpenMetadataRelationshipListResponse getRelationshipHistory(@PathVariable String                 serverName,
                                                                       @PathVariable String                 relationshipGUID,
                                                                       @RequestParam (required = false)
                                                                       String                         viewServiceURLMarker,
                                                                       @RequestParam (required = false, defaultValue = "asset-manager")
                                                                       String                         accessServiceURLMarker,
                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                           int                    startFrom,
                                                                       @RequestParam (required = false, defaultValue = "0")
                                                                           int                    pageSize,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                oldestFirst,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                forLineage,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                       boolean                forDuplicateProcessing,
                                                                       @RequestBody(required = false)
                                                                       HistoryRequestBody     requestBody)
    {
        return restAPI.getRelationshipHistory(serverName,
                                              relationshipGUID,
                                              viewServiceURLMarker,
                                              accessServiceURLMarker,
                                              forLineage,
                                              forDuplicateProcessing,
                                              startFrom,
                                              pageSize,
                                              oldestFirst,
                                              requestBody);
    }
}
