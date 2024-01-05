/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalReferenceListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalReferenceResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.assetowner.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.server.ExternalReferenceRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * AssetExternalReferenceResource sets up the external references that are part of an organization governance.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")

@Tag(name="Metadata Access Server: Asset Owner OMAS",
     description="The Asset Owner OMAS provides APIs and notifications for tools and applications supporting the work of " +
                         "Asset Owners in protecting and enhancing their assets.",
     externalDocs=@ExternalDocumentation(description="Further Information",
                                         url="https://egeria-project.org/services/omas/asset-owner/overview/"))

public class AssetExternalReferenceResource
{
    private final ExternalReferenceRESTServices restAPI = new ExternalReferenceRESTServices();

    /**
     * Default constructor
     */
    public AssetExternalReferenceResource()
    {
    }


    /**
     * Create a definition of an external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties for an external reference plus optional element to link the external reference to that will act as an anchor
     *                   - that is, this external reference will be deleted when the element is deleted (once the external reference is linked to the anchor).
     *
     * @return unique identifier of the external reference or
     *  InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references")

    public GUIDResponse createExternalReference(@PathVariable String                      serverName,
                                                @PathVariable String                      userId,
                                                @RequestBody  ReferenceableRequestBody    requestBody)
    {
        return restAPI.createExternalReference(serverName, userId, requestBody);
    }


    /**
     * Update the definition of an external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/update")

    public VoidResponse updateExternalReference(@PathVariable String                      serverName,
                                                @PathVariable String                      userId,
                                                @PathVariable String                      externalReferenceGUID,
                                                @RequestParam boolean                     isMergeUpdate,
                                                @RequestBody  ReferenceableRequestBody    requestBody)
    {
        return restAPI.updateExternalReference(serverName, userId, externalReferenceGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the definition of an external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/{externalReferenceGUID}/delete")

    public VoidResponse deleteExternalReference(@PathVariable String                    serverName,
                                                @PathVariable String                    userId,
                                                @PathVariable String                    externalReferenceGUID,
                                                @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.deleteExternalReference(serverName, userId, externalReferenceGUID, requestBody);
    }


    /**
     * Link an external reference to an object.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     * @param requestBody description for the reference from the perspective of the object that the reference is being attached to.
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{attachedToGUID}/external-references/{externalReferenceGUID}/link")

    public VoidResponse linkExternalReferenceToElement(@PathVariable String                  serverName,
                                                       @PathVariable String                  userId,
                                                       @PathVariable String                  attachedToGUID,
                                                       @PathVariable String                  externalReferenceGUID,
                                                       @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.linkExternalReferenceToElement(serverName, userId, attachedToGUID, externalReferenceGUID, requestBody);
    }



    /**
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/elements/{attachedToGUID}/external-references/{externalReferenceGUID}/unlink")

    public VoidResponse unlinkExternalReferenceFromElement(@PathVariable String                  serverName,
                                                           @PathVariable String                  userId,
                                                           @PathVariable String                  attachedToGUID,
                                                           @PathVariable String                  externalReferenceGUID,
                                                           @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.unlinkExternalReferenceFromElement(serverName, userId, attachedToGUID, externalReferenceGUID, requestBody);
    }


    /**
     * Return information about a specific external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the external reference
     *
     * @return properties of the external reference or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/external-references/{externalReferenceGUID}")

    public ExternalReferenceResponse getExternalReferenceByGUID(@PathVariable String serverName,
                                                                @PathVariable String userId,
                                                                @PathVariable String externalReferenceGUID)

    {
        return restAPI.getExternalReferenceByGUID(serverName, userId, externalReferenceGUID);
    }


    /**
     * Retrieve the list of external references for this resourceId.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param requestBody unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/by-resource-id")

    public ExternalReferenceListResponse findExternalReferencesById(@PathVariable String                  serverName,
                                                                    @PathVariable String                  userId,
                                                                    @RequestParam int                     startFrom,
                                                                    @RequestParam int                     pageSize,
                                                                    @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findExternalReferencesById(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of external references for this URL.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param requestBody URL of the external resource.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @PostMapping(path = "/external-references/by-url")

    public ExternalReferenceListResponse getExternalReferencesByURL(@PathVariable String          serverName,
                                                                    @PathVariable String          userId,
                                                                    @RequestParam int             startFrom,
                                                                    @RequestParam int             pageSize,
                                                                    @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getExternalReferencesByURL(serverName, userId, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/elements/{attachedToGUID}/external-references")

    public ExternalReferenceListResponse retrieveAttachedExternalReferences(@PathVariable String serverName,
                                                                            @PathVariable String userId,
                                                                            @PathVariable String attachedToGUID,
                                                                            @RequestParam int    startFrom,
                                                                            @RequestParam int    pageSize)
    {
        return restAPI.retrieveAttachedExternalReferences(serverName, userId, attachedToGUID, startFrom, pageSize);
    }


    /**
     * Return information about the elements linked to a externalReference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the externalReference
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the external reference
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/elements/external-references/{externalReferenceGUID}")

    public RelatedElementListResponse getElementsForExternalReference(@PathVariable String serverName,
                                                                      @PathVariable String userId,
                                                                      @PathVariable String externalReferenceGUID,
                                                                      @RequestParam int    startFrom,
                                                                      @RequestParam int    pageSize)
    {
        return restAPI.getElementsForExternalReference(serverName, userId, externalReferenceGUID, startFrom, pageSize);
    }
}
