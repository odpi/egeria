/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;


/**
 * LikeHandler is the handler for managing likes.
 */
public class LikeHandler extends FeedbackHandler
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public LikeHandler(String             localServerName,
                       AuditLog           auditLog,
                       String             serviceName,
                       OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.LIKE.typeName);
    }


    /**
     * Adds a "Like" to the element.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties properties
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addLikeToElement(String                            userId,
                                 String                            elementGUID,
                                 UpdateOptions                     updateOptions,
                                 LikeProperties                    properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "addLikeToElement";

        super.addFeedbackToElement(userId,
                                   elementGUID,
                                   OpenMetadataType.LIKE.typeName,
                                   OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                                   updateOptions,
                                   elementBuilder.getNewElementProperties(properties),
                                   methodName);
    }


    /**
     * Removes a "Like" added to the element by this user.
     *
     * @param userId   userId of user making request.
     * @param elementGUID unique identifier for the like object.
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeLikeFromElement(String                userId,
                                      String                elementGUID,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "removeLikeFromElement";

        OpenMetadataRootElement existingLike = this.getFeedbackForUser(userId,
                                                                       elementGUID,
                                                                       OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                                                                       new QueryOptions(metadataSourceOptions),
                                                                       methodName);
        if (existingLike != null)
        {
            DeleteOptions deleteOptions = new DeleteOptions(metadataSourceOptions);

            openMetadataClient.deleteMetadataElementInStore(userId,
                                                            existingLike.getElementHeader().getGUID(),
                                                            deleteOptions);
        }
    }


    /**
     * Return the likes attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of likes
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement>  getAttachedLikes(String       userId,
                                                           String       elementGUID,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getAttachedLikes";
        final String guidParameterName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }
}
