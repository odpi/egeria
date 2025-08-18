/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.RatingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;


/**
 * FeedbackHandler is the handler for managing comments, ratings, likes and tags.
 */
public class RatingHandler extends FeedbackHandler
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public RatingHandler(String             localServerName,
                         AuditLog           auditLog,
                         String             serviceName,
                         OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.RATING.typeName);
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties  properties of the rating
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addRatingToElement(String                            userId,
                                   String                            elementGUID,
                                   UpdateOptions                     updateOptions,
                                   RatingProperties                  properties) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "addRatingToElement";
        final String propertiesParameterName = "properties";
        final String starRatingParameterName = "properties.starRating";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateObject(properties.getStarRating(), starRatingParameterName, methodName);

        this.addFeedbackToElement(userId,
                                  elementGUID,
                                  OpenMetadataType.RATING.typeName,
                                  OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                  updateOptions,
                                  elementBuilder.getNewElementProperties(properties),
                                  methodName);
    }


    /**
     * Removes of a review that was added to the element by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID  unique identifier for the attached element.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeRatingFromElement(String                userId,
                                        String                elementGUID,
                                        MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "removeRatingFromElement";

        OpenMetadataRootElement existingRating = this.getFeedbackForUser(userId,
                                                                         elementGUID,
                                                                         OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                                                         new QueryOptions(metadataSourceOptions),
                                                                         methodName);

        if (existingRating != null)
        {
            DeleteOptions deleteOptions = new DeleteOptions(metadataSourceOptions);

            openMetadataClient.deleteMetadataElementInStore(userId,
                                                            existingRating.getElementHeader().getGUID(),
                                                            deleteOptions);
        }
    }



    /**
     * Return the ratings attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     * @return list of ratings
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement>  getAttachedRatings(String              userId,
                                                             String              elementGUID,
                                                             QueryOptions        queryOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getAttachedRatings";
        final String guidParameterName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }
}
