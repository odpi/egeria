/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.LikeBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.LikeConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
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
public class LikeHandler
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
    public LikeHandler(String                  serviceName,
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
     * Work out whether the requesting user is able to see the attached feedback.
     *
     * @param userId calling user
     * @param relationship relationship to the feedback content
     * @param methodName calling method
     * @return boolean - true if allowed
     */
    private boolean  visibleToUser(String        userId,
                                   Relationship relationship,
                                   String        methodName)
    {
        if (userId.equals(relationship.getCreatedBy()))
        {
            return true;
        }

        return repositoryHelper.getBooleanProperty(serviceName,
                                                   LikeMapper.IS_PUBLIC_PROPERTY_NAME,
                                                   relationship.getProperties(),
                                                   methodName);
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
    public int countLikes(String   userId,
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
                                                                  LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                                  LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                                  methodName);
    }


    /**
     * Return the Likes attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the feedback is attached to
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
        final String guidParameterName      = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);
        invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship>  relationships = repositoryHandler.getPagedRelationshipsByType(userId,
                                                                                          anchorGUID,
                                                                                          ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                                          LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                                                          LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                                                          startingFrom,
                                                                                          pageSize,
                                                                                          methodName);
        if (relationships != null)
        {
            List<Like>  results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (this.visibleToUser(userId, relationship, methodName))
                    {
                        EntityProxy entityProxy = relationship.getEntityTwoProxy();

                        if (entityProxy != null)
                        {
                            final String  entityParameterName = "entityProxyTwo.getGUID";
                            EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                                    entityProxy.getGUID(),
                                                                                    entityParameterName,
                                                                                    LikeMapper.LIKE_TYPE_NAME,
                                                                                    methodName);

                            LikeConverter converter = new LikeConverter(entity,
                                                                        relationship,
                                                                        repositoryHelper,
                                                                        serviceName);
                            results.add(converter.getBean());
                        }
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

        return null;
    }


    /**
     * Add or replace and existing Like for this user.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID   unique identifier for the anchor entity (Referenceable).
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void   saveLike(String     userId,
                           String     anchorGUID,
                           boolean    isPublic) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String methodName = "saveLike";

        this.removeLike(userId, anchorGUID);

        LikeBuilder builder = new LikeBuilder(isPublic,
                                              repositoryHelper,
                                              serviceName,
                                              serverName);

        repositoryHandler.addUniqueAttachedEntityToAnchor(userId,
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
    private   void removeLike(String userId,
                             String anchorGUID) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        final String methodName        = "removeLike";

        repositoryHandler.deleteUniqueEntityTypeFromAnchor(userId,
                                                           anchorGUID,
                                                           ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                           LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                           LikeMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                           LikeMapper.LIKE_TYPE_GUID,
                                                           LikeMapper.LIKE_TYPE_NAME,
                                                           methodName);
    }


    /**
     * Retrieve the requested rating object.
     *
     * @param userId       calling user
     * @param relationship relationship between referenceable and rating
     * @return new bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Like getLike(String       userId,
                        Relationship relationship) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        final String methodName        = "getLike";
        final String guidParameterName = "referenceableLikeRelationship.end2.guid";

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
     * Adds a "Like" to the asset.  If the user has already attached a like then the original one
     * is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param assetGUID   unique identifier for the asset where the like is to be attached.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   addLikeToAsset(String     userId,
                                 String     assetGUID,
                                 boolean    isPublic,
                                 String     methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String guidParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        this.saveLike(userId, assetGUID, isPublic);
    }


    /**
     * Removes a "Like" added to the asset by this user.
     *
     * @param userId   userId of user making request.
     * @param assetGUID unique identifier for the asset where the like is attached.
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   removeLikeFromAsset(String     userId,
                                      String     assetGUID,
                                      String     methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String guidParameter = "likeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, guidParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, assetGUID, AssetMapper.ASSET_TYPE_NAME, methodName, guidParameter);

        this.removeLike(userId, assetGUID);
    }
}
