/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.RatingBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.RatingConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.RatingMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Rating;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.StarRating;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * RatingHandler manages the Rating entity.  The Rating entity describes the star rating and review text
 * type of feedback
 */
public class RatingHandler extends FeedbackHandlerBase
{



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
    public RatingHandler(String                  serviceName,
                         String                  serverName,
                         InvalidParameterHandler invalidParameterHandler,
                         RepositoryHandler       repositoryHandler,
                         OMRSRepositoryHelper    repositoryHelper,
                         LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Count the number of Ratings attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the rating is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    int countRatings(String   userId,
                     String   anchorGUID,
                     String   methodName) throws InvalidParameterException,
                                                 PropertyServerException,
                                                 UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      anchorGUID,
                                      ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                      RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                      RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                      methodName);
    }



    /**
     * Return the Ratings attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the feedback is attached to
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     * @return list of objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Rating>  getRatings(String   userId,
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
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        List<Relationship>  relationships = this.getAttachmentLinks(userId,
                                                                    anchorGUID,
                                                                    ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                    RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                                    RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                                    startingFrom,
                                                                    queryPageSize,
                                                                    methodName);

        if ((relationships == null) || (relationships.isEmpty()))
        {
            return null;
        }

        List<Rating>  results = new ArrayList<>();

        for (Relationship relationship : relationships)
        {
            if (relationship != null)
            {
                Rating bean = this.getRating(userId, relationship, methodName);
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
     * Retrieve the requested rating object - assumption is that the relationship
     * is visible to the end user.
     *
     * @param userId       calling user
     * @param relationship relationship between referenceable and rating
     * @param methodName   calling method
     * @return new bean
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private Rating getRating(String       userId,
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
                                                                        RatingMapper.RATING_TYPE_NAME,
                                                                        methodName);

                RatingConverter converter = new RatingConverter(entity,
                                                                relationship,
                                                                repositoryHelper,
                                                                serviceName);

                return converter.getBean();
            }

        }

        return null;
    }


    /**
     * Add or replace and existing Rating for this user.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID   unique identifier for the anchor entity (Referenceable).
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName calling method
     * @return unique identifier of the rating
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String saveRating(String     userId,
                              String     anchorGUID,
                              StarRating starRating,
                              String     review,
                              boolean    isPublic,
                              String     methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        try
        {
            this.removeRating(userId, anchorGUID, methodName);
        }
        catch (Throwable error)
        {
            /*
             * Exception means this is the first rating from user.
             */
        }

        RatingBuilder builder = new RatingBuilder(starRating,
                                                  review,
                                                  isPublic,
                                                  anchorGUID,
                                                  repositoryHelper,
                                                  serviceName,
                                                  serverName);

        return repositoryHandler.addUniqueAttachedEntityToAnchor(userId,
                                                                 anchorGUID,
                                                                 ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                                 RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                                 RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                                 builder.getRelationshipInstanceProperties(methodName),
                                                                 RatingMapper.RATING_TYPE_GUID,
                                                                 RatingMapper.RATING_TYPE_NAME,
                                                                 builder.getEntityInstanceProperties(methodName),
                                                                 methodName);
    }


    /**
     * Remove the requested rating.
     *
     * @param userId       calling user
     * @param anchorGUID   anchor object where rating is attached
     * @param methodName   calling method
     *
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void removeRating(String userId,
                              String anchorGUID,
                              String methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        repositoryHandler.removeUniqueEntityTypeFromAnchor(userId,
                                                           anchorGUID,
                                                           ReferenceableMapper.REFERENCEABLE_TYPE_NAME,
                                                           RatingMapper.REFERENCEABLE_TO_RATING_TYPE_GUID,
                                                           RatingMapper.REFERENCEABLE_TO_RATING_TYPE_NAME,
                                                           RatingMapper.RATING_TYPE_GUID,
                                                           RatingMapper.RATING_TYPE_NAME,
                                                           methodName);
    }

    /**
     * Adds a star rating and optional review text to the asset.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID  unique identifier of referenceable
     * @param anchorType  type name of referenceable.
     * @param starRating  StarRating enumeration for not recommended, one to five stars.
     * @param review      user review of asset.  This can be null.
     * @param isPublic    indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToReferenceable(String     userId,
                                         String     anchorGUID,
                                         String     anchorType,
                                         StarRating starRating,
                                         String     review,
                                         boolean    isPublic,
                                         String     methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String anchorGUIDParameter = "anchorGUID";
        final String ratingParameter = "starRating";
        final String ratingDescription = "New rating from ";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameter, methodName);
        invalidParameterHandler.validateEnum(starRating, ratingParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, anchorGUID, anchorType, methodName, anchorGUIDParameter);

        String ratingGUID = this.saveRating(userId, anchorGUID, starRating, review, isPublic, methodName);

        lastAttachmentHandler.updateLastAttachment(anchorGUID,
                                                   anchorType,
                                                   ratingGUID,
                                                   RatingMapper.RATING_TYPE_NAME,
                                                   userId,
                                                   ratingDescription + userId,
                                                   methodName);
    }


    /**
     * Removes of a review that was added to the asset by this user.
     *
     * @param userId      userId of user making request.
     * @param anchorGUID   unique identifier for the asset where the rating is attached.
     * @param anchorType   type of anchor entity.
     * @param methodName  calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromReferencable(String     userId,
                                             String     anchorGUID,
                                             String     anchorType,
                                             String     methodName) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String guidParameter = "anchorGUID";
        final String ratingDescription = "Removed rating from ";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameter, methodName);

        repositoryHandler.validateEntityGUID(userId, anchorGUID, anchorType, methodName, guidParameter);

        removeRating(userId, anchorGUID, methodName);

        lastAttachmentHandler.updateLastAttachment(anchorGUID,
                                                   anchorType,
                                                   null,
                                                   RatingMapper.RATING_TYPE_NAME,
                                                   userId,
                                                   ratingDescription + userId,
                                                   methodName);
    }
}
