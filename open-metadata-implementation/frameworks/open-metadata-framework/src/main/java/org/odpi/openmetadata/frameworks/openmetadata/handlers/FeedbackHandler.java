/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;


/**
 * FeedbackHandler provides common methods for ratings and likes.
 */
public abstract class FeedbackHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public FeedbackHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             serviceName,
                           OpenMetadataClient openMetadataClient,
                           String             metadataElementTypeName)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, metadataElementTypeName);
    }


    /**
     * Adds a star rating and optional review text to the element.  If the user has already attached
     * a rating then the original one is over-ridden.
     *
     * @param userId      userId of user making request
     * @param elementGUID   unique identifier for the element
     * @param feedbackTypeName type of feedback element
     * @param relationshipTypeName type of attaching relationship
     * @param metadataSourceOptions  options to control access to open metadata
     * @param feedbackProperties  properties of the feedback
     * @return guid of new feedback element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected String addFeedbackToElement(String                userId,
                                        String                elementGUID,
                                        String                feedbackTypeName,
                                        String                relationshipTypeName,
                                        MetadataSourceOptions metadataSourceOptions,
                                        NewElementProperties  feedbackProperties,
                                        String                methodName) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        OpenMetadataRootElement existingFeedback = this.getFeedbackForUser(userId,
                                                                           elementGUID,
                                                                           relationshipTypeName,
                                                                           feedbackTypeName,
                                                                           new QueryOptions(metadataSourceOptions),
                                                                           methodName);

        if (existingFeedback == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setParentGUID(elementGUID);
            newElementOptions.setParentRelationshipTypeName(relationshipTypeName);

            return openMetadataClient.createMetadataElementInStore(userId,
                                                                   feedbackTypeName,
                                                                   newElementOptions,
                                                                   null,
                                                                   feedbackProperties,
                                                                   null);
        }
        else
        {
            openMetadataClient.updateMetadataElementInStore(userId,
                                                            existingFeedback.getElementHeader().getGUID(),
                                                            new UpdateOptions(metadataSourceOptions),
                                                            feedbackProperties);

            return existingFeedback.getElementHeader().getGUID();
        }
    }


    /**
     * Retrieve any feedback previously added by this user.
     *
     * @param userId      userId of user making request.
     * @param elementGUID   unique identifier for the element.
     * @param relationshipTypeName name of the relationship type
     * @param attachmentEntityTypeName requested type name for retrieved entities
     * @param queryOptions  options to control access to open metadata
     * @param methodName calling method
     * @return relationship and entity
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected OpenMetadataRootElement getFeedbackForUser(String       userId,
                                                         String       elementGUID,
                                                         String       relationshipTypeName,
                                                         String       attachmentEntityTypeName,
                                                         QueryOptions queryOptions,
                                                         String       methodName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String guidParameterName = "elementGUID";

        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);

        workingQueryOptions.setStartFrom(0);
        workingQueryOptions.setPageSize(openMetadataClient.getMaxPagingSize());

        List<OpenMetadataRootElement> attachedFeedbacks = super.getRelatedRootElements(userId,
                                                                                       elementGUID,
                                                                                       guidParameterName,
                                                                                       1,
                                                                                       relationshipTypeName,
                                                                                       attachmentEntityTypeName,
                                                                                       workingQueryOptions,
                                                                                       methodName);

        OpenMetadataRootElement existingFeedback = null;

        while ((existingFeedback == null) && (attachedFeedbacks != null))
        {
            for (OpenMetadataRootElement attachedFeedback : attachedFeedbacks)
            {
                if (attachedFeedback != null)
                {
                    if (userId.equals(attachedFeedback.getElementHeader().getVersions().getCreatedBy()))
                    {
                        existingFeedback = attachedFeedback;
                        break;
                    }
                }
            }

            workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataClient.getMaxPagingSize());
            attachedFeedbacks = super.getRelatedRootElements(userId,
                                                             elementGUID,
                                                             guidParameterName,
                                                             1,
                                                             relationshipTypeName,
                                                             attachmentEntityTypeName,
                                                             workingQueryOptions,
                                                             methodName);;
        }

        return existingFeedback;
    }
}
