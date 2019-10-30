/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.InformalTagMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.InformalTag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * InformalTagHandler manages InformalTag objects.  It runs server-side in
 * OMAS and retrieves InformalTag entities through the OMRSRepositoryConnector.
 */
public class InformalTagHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     */
    public InformalTagHandler(String                  serviceName,
                              String                  serverName,
                              InvalidParameterHandler invalidParameterHandler,
                              RepositoryHandler       repositoryHandler,
                              OMRSRepositoryHelper    repositoryHelper)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Count the number of informal tags attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countTags(String   userId,
                         String   anchorGUID,
                         String   methodName) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException
    {
        final String guidParameterName      = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);

        return repositoryHandler.countAttachedRelationshipsByType(userId,
                                                                  anchorGUID,
                                                                  ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                  InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                                                  InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_NAME,
                                                                  methodName);
    }


    /**
     * Return the informal tags attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the identifier is attached to
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<InformalTag>  getAttachedTags(String   userId,
                                              String   anchorGUID,
                                              int      startingFrom,
                                              int      pageSize,
                                              String   methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        // todo
        return null;
    }


    /**
     * Creates a new informal tag and returns the unique identifier for it.
     *
     * @param userId           userId of user making request.
     * @param tagName          name of the tag.
     * @param tagDescription  (optional) description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param methodName calling method
     *
     * @return GUID for new tag.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createTag(String  userId,
                            String  tagName,
                            String  tagDescription,
                            boolean isPublic,
                            String  methodName) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        // todo
        return null;
    }


    /**
     * Updates the description of an existing tag (either private of public).
     *
     * @param userId          userId of user making request.
     * @param tagGUID         unique identifier for the tag.
     * @param tagDescription  description of the tag.  Setting a description, particularly in a public tag
     *                        makes the tag more valuable to other users and can act as an embryonic glossary term.
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateTagDescription(String  userId,
                                       String  tagGUID,
                                       String  tagDescription,
                                       String  methodName) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        // todo
    }


    /**
     * Removes a tag from the repository.  All of the relationships to assets are lost.
     *
     * @param userId    userId of user making request.
     * @param tagGUID   unique id for the tag.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   deleteTag(String userId,
                            String tagGUID,
                            String methodName) throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        // todo
    }


    /**
     * Return the tag for the supplied unique identifier (guid).
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the tag.
     * @param methodName calling method
     *
     * @return tag
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public InformalTag getTag(String userId,
                              String guid,
                              String methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        // todo
        return null;
    }


    /**
     * Return the list of tags exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> getTagsByName(String userId,
                                           String tag,
                                           int    startFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        // todo
        return null;
    }


    /**
     * Return the list of tags matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param tag name of tag.  This may include wild card characters.
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param methodName calling method
     *
     * @return tag list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<InformalTag> findTags(String userId,
                                      String tag,
                                      int    startFrom,
                                      int    pageSize,
                                      String methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        // todo
        return null;
    }


    /**
     * Adds a tag (either private of public) to an asset.
     *
     * @param userId           userId of user making request.
     * @param assetGUID        unique id for the asset.
     * @param tagGUID          unique id of the tag.
     * @param isPublic         flag indicating whether the attachment of the tag is public or not
     * @param methodName       calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addTagToAsset(String  userId,
                                String  assetGUID,
                                String  tagGUID,
                                boolean isPublic,
                                String  methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        final String tagGUIDParameterName = "tagGUID";
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(tagGUID, tagGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        InstanceProperties properties = null;

        repositoryHandler.createRelationship(userId,
                                             InformalTagMapper.REFERENCEABLE_TO_TAG_TYPE_GUID,
                                             assetGUID,
                                             tagGUID,
                                             properties,
                                             methodName);

        // todo
    }


    /**
     * Removes a tag from the asset that was added by this user.
     *
     * @param userId    userId of user making request.
     * @param assetGUID unique id for the asset.
     * @param tagGUID   unique id for the tag.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeTagFromAsset(String userId,
                                     String assetGUID,
                                     String tagGUID,
                                     String methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        // todo
    }


}
