/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceElementResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceLinkElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceLinkRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ExternalReferenceRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.NameRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.SearchStringRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.UpdateRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.server.ExternalReferenceExchangeRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * GlossaryExchangeResource is the server-side implementation of the Asset Manager OMAS's
 * support for glossaries.  It matches the GlossaryExchangeClient.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-manager/users/{userId}")

@Tag(name="Asset Manager OMAS",
     description="The Asset Manager OMAS provides APIs and events for managing metadata exchange with third party asset managers, such as data catalogs.",
        externalDocs=@ExternalDocumentation(description="Asset Manager Open Metadata Access Service (OMAS)",
                url="https://egeria-project.org/services/omas/asset-manager/overview"))

public class ExternalReferenceExchangeResource
{
    private final ExternalReferenceExchangeRESTServices restAPI = new ExternalReferenceExchangeRESTServices();

    /**
     * Default constructor
     */
    public ExternalReferenceExchangeResource()
    {
    }

    
    /**
     * Create a definition of an external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param requestBody properties for a external reference
     *
     * @return unique identifier of the external reference or
     *  InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references")

    public GUIDResponse createExternalReference(@PathVariable String                       serverName,
                                                @PathVariable String                       userId,
                                                @RequestParam boolean                      assetManagerIsHome,
                                                @RequestBody  ExternalReferenceRequestBody requestBody)
    {
        return restAPI.createExternalReference(serverName, userId, assetManagerIsHome, requestBody);
    }


    /**
     * Update the definition of an external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}")

    public VoidResponse updateExternalReference(@PathVariable String                       serverName,
                                                @PathVariable String                       userId,
                                                @PathVariable String                       externalReferenceGUID,
                                                @RequestParam boolean                      isMergeUpdate,
                                                @RequestParam (required = false, defaultValue = "false")
                                                        boolean                      forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                        boolean                      forDuplicateProcessing,
                                                @RequestBody  ExternalReferenceRequestBody requestBody)
    {
        return restAPI.updateExternalReference(serverName, userId, externalReferenceGUID, isMergeUpdate, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the definition of an external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifiers of the external reference in the external asset manager
     *
     * @return void or
     *  InvalidParameterException guid or userId is null; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/remove")

    public VoidResponse deleteExternalReference(@PathVariable String            serverName,
                                                @PathVariable String            userId,
                                                @PathVariable String            externalReferenceGUID,
                                                @RequestParam (required = false, defaultValue = "false")
                                                        boolean                      forLineage,
                                                @RequestParam (required = false, defaultValue = "false")
                                                        boolean                      forDuplicateProcessing,
                                                @RequestBody  UpdateRequestBody requestBody)
    {
        return restAPI.deleteExternalReference(serverName, userId, externalReferenceGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Link an external reference to an object.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param attachedToGUID object linked to external references.
     * @param requestBody description for the reference from the perspective of the object that the reference is being attached to.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     *
     * @return Unique identifier for new relationship or
     *  InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/links/{attachedToGUID}")

    public GUIDResponse linkExternalReferenceToElement(@PathVariable String                           serverName,
                                                       @PathVariable String                           userId,
                                                       @PathVariable String                           attachedToGUID,
                                                       @PathVariable String                           externalReferenceGUID,
                                                       @RequestParam boolean                          assetManagerIsHome,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forLineage,
                                                       @RequestParam (required = false, defaultValue = "false")
                                                               boolean                      forDuplicateProcessing,
                                                       @RequestBody  ExternalReferenceLinkRequestBody requestBody)
    {
        return restAPI.linkExternalReferenceToElement(serverName, userId, assetManagerIsHome, attachedToGUID, externalReferenceGUID, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Update the link between an external reference to an object.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param externalReferenceLinkGUID unique identifier (guid) of the external reference details.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody description for the reference from the perspective of the object that the reference is being attached to.
     *
     * @return void or
     *  InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/links/{externalReferenceLinkGUID}/update")

    public VoidResponse updateExternalReferenceToElementLink(@PathVariable String                           serverName,
                                                             @PathVariable String                           userId,
                                                             @PathVariable String                           externalReferenceLinkGUID,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                      forLineage,
                                                             @RequestParam (required = false, defaultValue = "false")
                                                                     boolean                      forDuplicateProcessing,
                                                             @RequestBody  ExternalReferenceLinkRequestBody requestBody)
    {
        return restAPI.updateExternalReferenceToElementLink(serverName, userId, externalReferenceLinkGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param externalReferenceLinkGUID identifier of the external reference relationship.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique identifier of software server capability representing the caller
     *
     * @return void or
     *  InvalidParameterException problem with the GUID or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/links/{externalReferenceLinkGUID}/remove")

    public VoidResponse unlinkExternalReferenceFromElement(@PathVariable String                             serverName,
                                                           @PathVariable String                             userId,
                                                           @PathVariable String                             externalReferenceLinkGUID,
                                                           @RequestBody  EffectiveTimeQueryRequestBody requestBody,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                      forLineage,
                                                           @RequestParam (required = false, defaultValue = "false")
                                                                   boolean                      forDuplicateProcessing)
    {
        return restAPI.unlinkExternalReferenceFromElement(serverName, userId, externalReferenceLinkGUID, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of external references sorted in open metadata.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/by-type")

    public ExternalReferenceElementsResponse getExternalReferences(@PathVariable String                        serverName,
                                                                   @PathVariable String                        userId,
                                                                   @RequestParam int                           startFrom,
                                                                   @RequestParam int                           pageSize,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                           boolean                      forLineage,
                                                                   @RequestParam (required = false, defaultValue = "false")
                                                                           boolean                      forDuplicateProcessing,
                                                                   @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getExternalReferences(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of external references for this requestBody.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/by-resource-id")

    public ExternalReferenceElementsResponse getExternalReferencesById(@PathVariable String          serverName,
                                                                       @PathVariable String          userId,
                                                                       @RequestParam int             startFrom,
                                                                       @RequestParam int             pageSize,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                      forLineage,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                      forDuplicateProcessing,
                                                                       @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getExternalReferencesById(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of external references for this URL.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody URL of the external resource.
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/by-url")

    public ExternalReferenceElementsResponse getExternalReferencesByURL(@PathVariable String          serverName,
                                                                        @PathVariable String          userId,
                                                                        @RequestParam int             startFrom,
                                                                        @RequestParam int             pageSize,
                                                                        @RequestParam (required = false, defaultValue = "false")
                                                                                boolean                      forLineage,
                                                                        @RequestParam (required = false, defaultValue = "false")
                                                                                boolean                      forDuplicateProcessing,
                                                                        @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getExternalReferencesByURL(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }



    /**
     * Retrieve the list of external references for this name.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody name of the external resource.
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/by-name")

    public ExternalReferenceElementsResponse getExternalReferencesByName(@PathVariable String          serverName,
                                                                         @PathVariable String          userId,
                                                                         @RequestParam int             startFrom,
                                                                         @RequestParam int             pageSize,
                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                      forLineage,
                                                                         @RequestParam (required = false, defaultValue = "false")
                                                                                 boolean                      forDuplicateProcessing,
                                                                         @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getExternalReferencesByName(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of external reference created on behalf of the named asset manager.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/external-references/by-asset-manager")

    public ExternalReferenceElementsResponse getExternalReferencesForAssetManager(@PathVariable String                        serverName,
                                                                                  @PathVariable String                        userId,
                                                                                  @RequestParam int                           startFrom,
                                                                                  @RequestParam int                           pageSize,
                                                                                  @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                      forLineage,
                                                                                  @RequestParam (required = false, defaultValue = "false")
                                                                                          boolean                      forDuplicateProcessing,
                                                                                  @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getExternalReferencesForAssetManager(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Find the external references that contain the search string - which may contain wildcards.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param requestBody regular expression (RegEx) to search for
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/by-search-string")

    public ExternalReferenceElementsResponse findExternalReferences(@PathVariable String                  serverName,
                                                                    @PathVariable String                  userId,
                                                                    @RequestParam int                     startFrom,
                                                                    @RequestParam int                     pageSize,
                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                            boolean                      forLineage,
                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                            boolean                      forDuplicateProcessing,
                                                                    @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findExternalReferences(serverName, userId, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param serverName name of the server to route the request to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return links to addition information or
     *  InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *  PropertyServerException the server is not available.
     *  UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    @PostMapping(path = "/external-references/attached-to/{attachedToGUID}")

    public ExternalReferenceLinkElementsResponse retrieveAttachedExternalReferences(@PathVariable String                        serverName,
                                                                                    @PathVariable String                        userId,
                                                                                    @PathVariable String                        attachedToGUID,
                                                                                    @RequestParam int                           startFrom,
                                                                                    @RequestParam int                           pageSize,
                                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                            boolean                      forLineage,
                                                                                    @RequestParam (required = false, defaultValue = "false")
                                                                                            boolean                      forDuplicateProcessing,
                                                                                    @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.retrieveAttachedExternalReferences(serverName, userId, attachedToGUID, startFrom, pageSize, forLineage, forDuplicateProcessing, requestBody);
    }


    /**
     * Return information about a specific external reference.
     *
     * @param serverName name of the server to route the request to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the external reference
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody the time that the retrieved elements must be effective for
     *
     * @return properties of the external reference or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/by-guid")

    public ExternalReferenceElementResponse getExternalReferenceByGUID(@PathVariable String                        serverName,
                                                                       @PathVariable String                        userId,
                                                                       @PathVariable String                        externalReferenceGUID,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                      forLineage,
                                                                       @RequestParam (required = false, defaultValue = "false")
                                                                               boolean                      forDuplicateProcessing,
                                                                       @RequestBody  EffectiveTimeQueryRequestBody requestBody)
    {
        return restAPI.getExternalReferenceByGUID(serverName, userId, externalReferenceGUID, forLineage, forDuplicateProcessing, requestBody);
    }
}
