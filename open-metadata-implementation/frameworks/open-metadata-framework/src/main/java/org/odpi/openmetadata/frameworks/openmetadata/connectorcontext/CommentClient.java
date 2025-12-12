/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CommentHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with comment elements.
 */
public class CommentClient extends ConnectorContextClientBase
{
    private final CommentHandler commentHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public CommentClient(ConnectorContextBase     parentContext,
                         String                   localServerName,
                         String                   localServiceName,
                         String                   connectorUserId,
                         String                   connectorGUID,
                         String                   externalSourceGUID,
                         String                   externalSourceName,
                         OpenMetadataClient       openMetadataClient,
                         AuditLog                 auditLog,
                         int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.commentHandler = new CommentHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Adds a comment to the element.
     *
     * @param elementGUID     unique identifier for the element
     * @param metadataSourceOptions options for the request
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToElement(String                                elementGUID,
                                      MetadataSourceOptions                 metadataSourceOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      CommentProperties                     properties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        String commentGUID = commentHandler.addCommentToElement(connectorUserId, elementGUID, metadataSourceOptions, initialClassifications, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(commentGUID);
        }

        return commentGUID;
    }


    /**
     * Update an existing comment.
     *
     * @param commentGUID   unique identifier for the comment to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the comment
     *
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateComment(String            commentGUID,
                                 UpdateOptions     updateOptions,
                                 CommentProperties properties) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        boolean updateOccurred = commentHandler.updateComment(connectorUserId, commentGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(commentGUID);
        }

        return updateOccurred;
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param makeAnchorOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String            questionCommentGUID,
                                    String            answerCommentGUID,
                                    MakeAnchorOptions makeAnchorOptions) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        commentHandler.setupAcceptedAnswer(connectorUserId,
                                           questionCommentGUID,
                                           answerCommentGUID,
                                           makeAnchorOptions);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String        questionCommentGUID,
                                    String        answerCommentGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        commentHandler.clearAcceptedAnswer(connectorUserId, questionCommentGUID, answerCommentGUID, deleteOptions);
    }


    /**
     * Removes a comment added to the element by this user.
     *
     * @param commentGUID  unique identifier for the comment object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deleteComment(String        commentGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        commentHandler.deleteComment(connectorUserId, commentGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(commentGUID);
        }
    }


    /**
     * Return the requested comment.
     *
     * @param commentGUID  unique identifier for the comment object.
     * @param getOptions multiple options to control the query
     * @return comment properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getCommentByGUID(String     commentGUID,
                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        return commentHandler.getCommentByGUID(connectorUserId, commentGUID, getOptions);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of comments
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement> getAttachedComments(String       elementGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        return commentHandler.getAttachedComments(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Retrieve the list of comment metadata elements that contain the search string.
     *
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findComments(String        searchString,
                                                      SearchOptions searchOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        return commentHandler.findComments(connectorUserId, searchString, searchOptions);
    }
}
