/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.CommentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;


/**
 * CommentHandler is the handler for managing comments.
 */
public class CommentHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public CommentHandler(String             localServerName,
                          AuditLog           auditLog,
                          String             serviceName,
                          OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.COMMENT.typeName);
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param elementGUID     unique identifier for the element.
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentToElement(String                                userId,
                                      String                                elementGUID,
                                      MetadataSourceOptions                 metadataSourceOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      CommentProperties                     properties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "addCommentToElement";

        NewElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(elementGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.COMMENT.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementProperties,
                                                               null);
    }


    /**
     * Validate comment properties and construct an element properties object.
     *
     * @param properties comment properties from caller
     * @param methodName calling method
     * @return element properties
     * @throws InvalidParameterException properties from caller are invalid
     */
    private NewElementProperties getElementPropertiesForComment(CommentProperties properties,
                                                                String            methodName) throws InvalidParameterException
    {
        final String propertiesParameterName   = "properties";
        final String commentQNameParameterName = "properties.qualifiedName";
        final String commentTextParameterName  = "properties.description";

        propertyHelper.validateObject(properties, propertiesParameterName, methodName);
        propertyHelper.validateMandatoryName(properties.getQualifiedName(), commentQNameParameterName, methodName);
        propertyHelper.validateText(properties.getDescription(), commentTextParameterName, methodName);

        return elementBuilder.getNewElementProperties(properties);
    }


    /**
     * Adds a comment to the element.
     *
     * @param userId        userId of user making request.
     * @param commentGUID     unique identifier for the comment to attach to.
     * @param elementGUID     unique identifier for the anchor element.
     * @param metadataSourceOptions  options to control access to open metadata
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties   properties of the comment
     *
     * @return guid of new comment.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String addCommentReply(String                                userId,
                                  String                                elementGUID,
                                  String                                commentGUID,
                                  MetadataSourceOptions                 metadataSourceOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  CommentProperties                     properties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "addCommentToElement";

        NewElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(elementGUID);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setParentGUID(commentGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        return openMetadataClient.createMetadataElementInStore(userId,
                                                               OpenMetadataType.COMMENT.typeName,
                                                               newElementOptions,
                                                               classificationBuilder.getInitialClassifications(initialClassifications),
                                                               elementProperties,
                                                               null);
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param commentGUID   unique identifier for the comment to change.
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties   properties of the comment
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the element properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String            userId,
                                String            commentGUID,
                                UpdateOptions     updateOptions,
                                CommentProperties properties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateComment";

        NewElementProperties elementProperties = this.getElementPropertiesForComment(properties, methodName);

        openMetadataClient.updateMetadataElementInStore(userId, commentGUID, updateOptions, elementProperties);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param metadataSourceOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String  userId,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID,
                                    MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName,
                                                        questionCommentGUID,
                                                        answerCommentGUID,
                                                        metadataSourceOptions,
                                                        null);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param deleteOptions  options to control access to open metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String        userId,
                                    String        questionCommentGUID,
                                    String        answerCommentGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        QueryOptions queryOptions = new QueryOptions(deleteOptions);

        OpenMetadataRelationshipList relationships = openMetadataClient.getMetadataElementRelationships(userId,
                                                                                                        questionCommentGUID,
                                                                                                        answerCommentGUID,
                                                                                                        OpenMetadataType.ACCEPTED_ANSWER_RELATIONSHIP.typeName,
                                                                                                        queryOptions);

        if ((relationships != null) && (relationships.getElementList() != null))
        {
            for (OpenMetadataRelationship relationship : relationships.getElementList())
            {
                if (relationship != null)
                {
                    openMetadataClient.deleteRelationshipInStore(userId,
                                                                 relationship.getRelationshipGUID(),
                                                                 deleteOptions);

                    break;
                }
            }
        }
    }


    /**
     * Removes a comment added to the element by this user.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param deleteOptions options for a delete request
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void deleteComment(String        userId,
                              String        commentGUID,
                              DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        openMetadataClient.deleteMetadataElementInStore(userId, commentGUID, deleteOptions);
    }


    /**
     * Return the requested comment.
     *
     * @param userId       userId of user making request.
     * @param commentGUID  unique identifier for the comment object.
     * @param getOptions multiple options to control the query
     * @return comment properties
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElement getCommentByGUID(String     userId,
                                                    String     commentGUID,
                                                    GetOptions getOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getCommentByGUID";

        return super.getRootElementByGUID(userId, commentGUID, getOptions, methodName);
    }


    /**
     * Return the comments attached to an element.
     *
     * @param userId       userId of user making request.
     * @param elementGUID    unique identifier for the element that the comments are connected to (maybe a comment too).
     * @param queryOptions multiple options to control the query
     *
     * @return list of comments
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the element properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public List<OpenMetadataRootElement> getAttachedComments(String       userId,
                                                             String       elementGUID,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName        = "getAttachedComments";
        final String guidParameterName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Retrieve the list of comment metadata elements that contain the search string.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findComments(String        userId,
                                                      String        searchString,
                                                      SearchOptions searchOptions) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String methodName = "findComments";

        return super.findRootElements(userId, searchString, searchOptions,methodName);
    }
}
