/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.LikeBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.LikeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LikeMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Like;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * LikeHandler provides access and maintenance for Like objects and their attachment
 * to Referenceables.
 */
public class LikeHandler extends FeedbackHandlerBase
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private LastAttachmentHandler   lastAttachmentHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public LikeHandler(String                  serviceName,
                       String                  serverName,
                       InvalidParameterHandler invalidParameterHandler,
                       RepositoryHandler       repositoryHandler,
                       OMRSRepositoryHelper    repositoryHelper,
                       LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Count the number of Likes attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    int countLikes(String   userId,
                   String   anchorGUID,
                   String   methodName) throws InvalidParameterException,
                                               PropertyServerException,
                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      anchorGUID,
                                      ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                      LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                      LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                      methodName);
    }


    /**
     * Return the Likes attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the feedback is attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     * @return list of retrieved objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Like>  getLikes(String   userId,
                                String   anchorGUID,
                                int      startingFrom,
                                int      pageSize,
                                String   methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    anchorGUID,
                                                                    ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                    LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                                    LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<Like>  results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                Like bean = this.getLike(userId, relationship, methodName);

                if (bean != null)
                {
                    results.add(bean);
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the requested like object - assumption is that the relationship
     * is visible to the end user.
     *
     * @param userId       calling user
     * @param relationship relationship between referenceable and like
     * @param methodName   calling method
     * @return new bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Like getLike(String       userId,
                         Relationship relationship,
                         String       methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        final String guidParameterName = "referenceableRelationship.end2.guid";

        if (relationship != null)
        {
            EntityProxy entityProxy = relationship.getEntityTwoProxy();

            if (entityProxy != null)
            {
                EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                        entityProxy.getGUID(),
                                                                        guidParameterName,
                                                                        LikeMapper.LIKE_TYPE_NAME,
                                                                        methodName);

                LikeConverter converter = new LikeConverter(entity,
                                                            relationship,
                                                            repositoryHelper,
                                                            serviceName);
                return converter.getBean();
            }

        }

        return null;
    }


    /**
     * Add or replace and existing Like for this user.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID   unique identifier for the anchor entity (Referenceable).
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName calling method
     *
     * @return unique identifier of the new Like entity
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String saveLike(String     userId,
                           String     anchorGUID,
                           boolean    isPublic,
                           String     methodName) throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        this.removeLike(userId, anchorGUID);

        LikeBuilder builder = new LikeBuilder(isPublic,
                                              anchorGUID,
                                              repositoryHelper,
                                              serviceName,
                                              serverName);

        return repositoryHandler.addUniqueAttachedEntityToAnchor(userId,
                                                                 anchorGUID,
                                                                 ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                 LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                                 LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                                 builder.getRelationshipInstanceProperties(methodName),
                                                                 LikeMapper.LIKE_TYPE_GUID,
                                                                 LikeMapper.LIKE_TYPE_NAME,
                                                                 null,
                                                                 methodName);

    }


    /**
     * Remove the requested like.
     *
     * @param userId       calling user
     * @param anchorGUID   anchor object where rating is attached
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private  void removeLike(String userId,
                             String anchorGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String methodName        = "removeLike";

        repositoryHandler.removeUniqueEntityTypeFromAnchor(userId,
                                                           anchorGUID,
                                                           ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                           LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                           LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                           LikeMapper.LIKE_TYPE_GUID,
                                                           LikeMapper.LIKE_TYPE_NAME,
                                                           methodName);
    }




    /**
     * Adds a "Like" to the asset.  If the user has already attached a like then the original one
     * is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID   unique identifier for the asset where the like is to be attached.
     * @param anchorType   type of anchor entity.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addLikeToReferenceable(String     userId,
                                         String     anchorGUID,
                                         String     anchorType,
                                         boolean    isPublic,
                                         String     methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String guidParameter = "anchorGUID";
        final String likeDescription = "New like from ";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameter, methodName);

        String likeGUID = this.saveLike(userId, anchorGUID, isPublic, methodName);

        lastAttachmentHandler.updateLastAttachment(anchorGUID,
                                                   anchorType,
                                                   likeGUID,
                                                   LikeMapper.LIKE_TYPE_NAME,
                                                   userId,
                                                   likeDescription + userId,
                                                   methodName);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param anchorGUID unique identifier for the asset where the like is attached.
     * @param anchorType   type of anchor entity.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeLikeFromReferenceable(String     userId,
                                              String     anchorGUID,
                                              String     anchorType,
                                              String     methodName) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String guidParameter = "likeGUID";
        final String likeDescription = "Removed like from ";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, anchorGUID, anchorType, methodName, guidParameter);

        this.removeLike(userId, anchorGUID);

        lastAttachmentHandler.updateLastAttachment(anchorGUID,
                                                   anchorType,
                                                   null,
                                                   LikeMapper.LIKE_TYPE_NAME,
                                                   userId,
                                                   likeDescription + userId,
                                                   methodName);
    }
}
