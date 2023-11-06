/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.*;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.server.OCFMetadataRESTServices;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.*;
import org.springframework.web.bind.annotation.*;

/**
 * The ConnectedAssetResource is the server-side implementation of the REST services needed to
 * populate the Open Connector Framework (OCF) Connected Asset Properties.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/framework-services/{serviceURLName}/connected-asset/users/{userId}")

@Tag(name="Framework Services: Connected Asset Services",
     description="Provides common services for Open Metadata Access Services (OMASs) that managed connections, create connectors and retrieve information related to the asset connected to the connection.",
     externalDocs=@ExternalDocumentation(description="Further Information",url="https://egeria-project.org/services/ocf-metadata-management/"))

public class ConnectedAssetResource
{
    private final OCFMetadataRESTServices restAPI = new OCFMetadataRESTServices();

    /**
     * Default constructor
     */
    public ConnectedAssetResource()
    {
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request.
     * @param serviceURLName name of the service that created the connector that issued this request.
     * @param userId userId of user making request.
     * @param guid  the unique id for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/connections/{guid}")

    public ConnectionResponse getConnectionByGUID(@PathVariable String     serverName,
                                                  @PathVariable String     serviceURLName,
                                                  @PathVariable String     userId,
                                                  @PathVariable String     guid)
    {
        return restAPI.getConnectionByGUID(serverName, serviceURLName, userId, guid);
    }


    /**
     * Returns the connection object corresponding to the supplied connection GUID.
     *
     * @param serverName name of the server instances for this request.
     * @param serviceURLName name of the service that created the connector that issued this request.
     * @param userId userId of user making request.
     * @param name  the unique name for the connection within the property server.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the metadata repository or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/connections/by-name/{name}")

    public ConnectionResponse getConnectionByName(@PathVariable String     serverName,
                                                  @PathVariable String     serviceURLName,
                                                  @PathVariable String     userId,
                                                  @PathVariable String     name)
    {
        return restAPI.getConnectionByName(serverName, serviceURLName, userId, name);
    }



    /**
     * Returns the connection corresponding to the supplied asset GUID.
     *
     * @param serverName  name of the server instances for this request
     * @param serviceURLName name of the service that created the connector that issued this request.
     * @param userId      userId of user making request.
     * @param assetGUID   the unique id for the asset within the metadata repository.
     *
     * @return connection object or
     * InvalidParameterException one of the parameters is null or invalid or
     * UnrecognizedConnectionNameException there is no connection defined for this name or
     * PropertyServerException there is a problem retrieving information from the property (metadata) server or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/connection")

    public ConnectionResponse getConnectionForAsset(@PathVariable String   serverName,
                                                    @PathVariable String   serviceURLName,
                                                    @PathVariable String   userId,
                                                    @PathVariable String   assetGUID)
    {
        return restAPI.getConnectionForAsset(serverName, serviceURLName, userId, assetGUID);
    }


    /**
     * Returns the unique identifier for the asset connected to the connection identified by the supplied guid.
     *
     * @param serverName     String name of the server instances for this request.
     * @param serviceURLName String name of the service that created the connector that issued this request.
     * @param userId         String the userId of the requesting user.
     * @param connectionGUID  uniqueId for the connection.
     *
     * @return unique identifier of asset or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException there is a problem retrieving the connected asset properties from the property server or
     * UnrecognizedConnectionGUIDException the supplied GUID is not recognized by the property server or
     * NoConnectedAssetException there is no asset associated with this connection or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/by-connection/{connectionGUID}")

    public GUIDResponse getAssetForConnectionGUID(@PathVariable String   serverName,
                                                  @PathVariable String   serviceURLName,
                                                  @PathVariable String   userId,
                                                  @PathVariable String   connectionGUID)
    {
        return restAPI.getAssetForConnectionGUID(serverName, serviceURLName, userId, connectionGUID);
    }


    /**
     * Returns the basic information about the asset.  The connection guid allows the short description for the
     * asset to be filled out.
     *
     * @param serverName      String   name of the server.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId          String   userId of user making request.
     * @param assetGUID       String   unique id for asset.
     * @param connectionGUID  unique   id for connection used to access asset.
     *
     * @return a bean with the basic properties about the asset or
     * InvalidParameterException - the asset GUID is null or invalid or
     * UnrecognizedAssetGUIDException - the asset GUID is not recognized by the property server or
     * UnrecognizedConnectionGUIDException - the connection GUID is not recognized by the property server or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/via-connection/{connectionGUID}")

    public AssetResponse getConnectedAssetSummary(@PathVariable String   serverName,
                                                  @PathVariable String   serviceURLName,
                                                  @PathVariable String   userId,
                                                  @PathVariable String   assetGUID,
                                                  @PathVariable String   connectionGUID)
    {
        return restAPI.getConnectedAssetSummary(serverName, serviceURLName, userId, assetGUID, connectionGUID);
    }


    /**
     * Returns the anchor asset.
     *
     * @param serverName  String   name of server instance to call.
     * @param serviceURLName String   name of the service that created the connector that issued this request.
     * @param userId      String   userId of user making request.
     * @param guid   String   unique id for the metadata element.
     * @return a bean with the basic properties about the anchor asset or
     * InvalidParameterException - the userId is null or invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/from-anchor/{guid}")

    public AssetResponse getAnchorAssetFromGUID(@PathVariable String serverName,
                                                @PathVariable String serviceURLName,
                                                @PathVariable String userId,
                                                @PathVariable String guid)
    {
        return restAPI.getAnchorAssetFromGUID(serverName, serviceURLName, userId, guid);
    }


    /**
     * Returns the basic information about the asset.
     *
     * @param serverName  String   name of server instance to call.
     * @param serviceURLName String   name of the service that created the connector that issued this request.
     * @param userId      String   userId of user making request.
     * @param assetGUID   String   unique id for asset.
     * @return a bean with the basic properties about the asset or
     * InvalidParameterException - the userId is null or invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}")

    public AssetResponse getAssetSummary(@PathVariable String   serverName,
                                         @PathVariable String   serviceURLName,
                                         @PathVariable String   userId,
                                         @PathVariable String   assetGUID)
    {
        return restAPI.getAssetSummary(serverName, serviceURLName, userId, assetGUID);
    }


    /**
     * Returns the list of certifications for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of certifications or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/certifications")

    public CertificationsResponse getCertifications(@PathVariable String  serverName,
                                                    @PathVariable String  serviceURLName,
                                                    @PathVariable String  userId,
                                                    @PathVariable String  assetGUID,
                                                    @RequestParam int     elementStart,
                                                    @RequestParam int     maxElements)
    {
        return restAPI.getCertifications(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of comments for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/comments")

    public CommentsResponse getAssetComments(@PathVariable String  serverName,
                                             @PathVariable String  serviceURLName,
                                             @PathVariable String  userId,
                                             @PathVariable String  assetGUID,
                                             @RequestParam int     elementStart,
                                             @RequestParam int     maxElements)
    {
        return restAPI.getAssetComments(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of comments for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique identifier for the linked asset.
     * @param commentGUID  String   unique id for the root comment.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of comments or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/comments/{commentGUID}/replies")

    public CommentsResponse getAssetCommentReplies(@PathVariable String  serverName,
                                                   @PathVariable String  serviceURLName,
                                                   @PathVariable String  userId,
                                                   @PathVariable String  assetGUID,
                                                   @PathVariable String  commentGUID,
                                                   @RequestParam int     elementStart,
                                                   @RequestParam int     maxElements)
    {
        return restAPI.getAssetCommentReplies(serverName, serviceURLName, userId, assetGUID, commentGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of connections for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of connections or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/connections")

    public ConnectionsResponse getConnections(@PathVariable String  serverName,
                                              @PathVariable String  serviceURLName,
                                              @PathVariable String  userId,
                                              @PathVariable String  assetGUID,
                                              @RequestParam int     elementStart,
                                              @RequestParam int     maxElements)
    {
        return restAPI.getConnections(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of external identifiers for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of external identifiers or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/external-identifiers")

    public ExternalIdentifiersResponse getExternalIdentifiers(@PathVariable String  serverName,
                                                              @PathVariable String  serviceURLName,
                                                              @PathVariable String  userId,
                                                              @PathVariable String  assetGUID,
                                                              @RequestParam int     elementStart,
                                                              @RequestParam int     maxElements)
    {
        return restAPI.getExternalIdentifiers(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of external references for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of external references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/external-references")

    public ExternalReferencesResponse getExternalReferences(@PathVariable String  serverName,
                                                            @PathVariable String  serviceURLName,
                                                            @PathVariable String  userId,
                                                            @PathVariable String  assetGUID,
                                                            @RequestParam int     elementStart,
                                                            @RequestParam int     maxElements)
    {
        return restAPI.getExternalReferences(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of informal tags for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of informal tags or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/informal-tags")

    public InformalTagsResponse getInformalTags(@PathVariable String  serverName,
                                                @PathVariable String  serviceURLName,
                                                @PathVariable String  userId,
                                                @PathVariable String  assetGUID,
                                                @RequestParam int     elementStart,
                                                @RequestParam int     maxElements)
    {
        return restAPI.getInformalTags(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of licenses for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of informal tags or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/licenses")

    public LicensesResponse getLicenses(@PathVariable String  serverName,
                                        @PathVariable String  serviceURLName,
                                        @PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getLicenses(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of likes for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of likes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/likes")

    public LikesResponse getLikes(@PathVariable String  serverName,
                                  @PathVariable String  serviceURLName,
                                  @PathVariable String  userId,
                                  @PathVariable String  assetGUID,
                                  @RequestParam int     elementStart,
                                  @RequestParam int     maxElements)
    {
        return restAPI.getLikes(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of known locations for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of locations or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/known-locations")

    public LocationsResponse getKnownLocations(@PathVariable String  serverName,
                                               @PathVariable String  serviceURLName,
                                               @PathVariable String  userId,
                                               @PathVariable String  assetGUID,
                                               @RequestParam int     elementStart,
                                               @RequestParam int     maxElements)
    {
        return restAPI.getKnownLocations(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of note logs for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of note logs or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/note-logs")

    public NoteLogsResponse getNoteLogs(@PathVariable String  serverName,
                                        @PathVariable String  serviceURLName,
                                        @PathVariable String  userId,
                                        @PathVariable String  assetGUID,
                                        @RequestParam int     elementStart,
                                        @RequestParam int     maxElements)
    {
        return restAPI.getNoteLogs(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of notes for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param noteLogGUID  String   unique id for note log.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of notes or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/note-logs/{noteLogGUID}/notes")

    public NotesResponse getNotes(@PathVariable String  serverName,
                                  @PathVariable String  serviceURLName,
                                  @PathVariable String  userId,
                                  @PathVariable String  noteLogGUID,
                                  @RequestParam int     elementStart,
                                  @RequestParam int     maxElements)
    {
        return restAPI.getNotes(serverName, serviceURLName, userId, noteLogGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of ratings for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of ratings or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/ratings")

    public RatingsResponse getRatings(@PathVariable String  serverName,
                                      @PathVariable String  serviceURLName,
                                      @PathVariable String  userId,
                                      @PathVariable String  assetGUID,
                                      @RequestParam int     elementStart,
                                      @RequestParam int     maxElements)
    {
        return restAPI.getRatings(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of related assets for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/related-assets")

    public RelatedAssetsResponse getRelatedAssets(@PathVariable String  serverName,
                                                  @PathVariable String  serviceURLName,
                                                  @PathVariable String  userId,
                                                  @PathVariable String  assetGUID,
                                                  @RequestParam int     elementStart,
                                                  @RequestParam int     maxElements)
    {
        return restAPI.getRelatedAssets(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of related Referenceables that provide more information for this asset, schema, ...
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param elementGUID    String   unique id for the element.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related assets or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/referenceables/{elementGUID}/more-information")

    public MoreInformationResponse getMoreInformation(@PathVariable String  serverName,
                                                      @PathVariable String  serviceURLName,
                                                      @PathVariable String  userId,
                                                      @PathVariable String  elementGUID,
                                                      @RequestParam int     elementStart,
                                                      @RequestParam int     maxElements)
    {
        return restAPI.getMoreInformation(serverName, serviceURLName, userId, elementGUID, elementStart, maxElements);
    }


    /**
     * Returns the list of related media references for the asset.
     *
     * @param serverName   String   name of server instance to call.
     * @param serviceURLName  String   name of the service that created the connector that issued this request.
     * @param userId       String   userId of user making request.
     * @param assetGUID    String   unique id for asset.
     * @param elementStart int      starting position for fist returned element.
     * @param maxElements  int      maximum number of elements to return on the call.
     *
     * @return a list of related media references or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedAssetGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/{assetGUID}/related-media-references")

    public RelatedMediaReferencesResponse getRelatedMediaReferences(@PathVariable String  serverName,
                                                                    @PathVariable String  serviceURLName,
                                                                    @PathVariable String  userId,
                                                                    @PathVariable String  assetGUID,
                                                                    @RequestParam int     elementStart,
                                                                    @RequestParam int     maxElements)
    {
        return restAPI.getRelatedMediaReferences(serverName, serviceURLName, userId, assetGUID, elementStart, maxElements);
    }


    /**
     * Returns a list of schema attributes for a schema type.
     *
     * @param serverName     String   name of server instance to call.
     * @param serviceURLName    String   name of the service that created the connector that issued this request.
     * @param userId         String   userId of user making request.
     * @param parentSchemaGUID String   unique id for containing schema type.
     * @param elementStart   int      starting position for fist returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     *
     * @return a schema attributes response or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/schemas/{parentSchemaGUID}/schema-attributes")

    public SchemaAttributesResponse getSchemaAttributes(@PathVariable String serverName,
                                                        @PathVariable String serviceURLName,
                                                        @PathVariable String userId,
                                                        @PathVariable String parentSchemaGUID,
                                                        @RequestParam int    elementStart,
                                                        @RequestParam int    maxElements)
    {
        return restAPI.getSchemaAttributes(serverName, serviceURLName, userId, parentSchemaGUID, elementStart, maxElements);
    }


    /**
     * Returns a list of api operations for a schema type.
     *
     * @param serverName     String   name of server instance to call.
     * @param serviceURLName    String   name of the service that created the connector that issued this request.
     * @param userId         String   userId of user making request.
     * @param parentSchemaTypeGUID String   unique id for containing schema element.
     * @param elementStart   int      starting position for fist returned element.
     * @param maxElements    int      maximum number of elements to return on the call.
     *
     * @return a schema attributes response or
     * InvalidParameterException - the GUID is not recognized or the paging values are invalid or
     * UnrecognizedGUIDException - the GUID is null or invalid or
     * PropertyServerException - there is a problem retrieving the asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/assets/schemas/apis/{parentSchemaTypeGUID}/api-operations")

    public APIOperationsResponse getAPIOperations(@PathVariable String  serverName,
                                                  @PathVariable String  serviceURLName,
                                                  @PathVariable String  userId,
                                                  @PathVariable String  parentSchemaTypeGUID,
                                                  @RequestParam int     elementStart,
                                                  @RequestParam int     maxElements)
    {
        return restAPI.getAPIOperations(serverName, serviceURLName, userId, parentSchemaTypeGUID, elementStart, maxElements);
    }

}
