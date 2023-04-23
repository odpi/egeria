/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.CommentConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.CommentElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CommentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.CommentType;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.CommentHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * CommentExchangeHandler is the server side handler for managing comments.
 */
public class CommentExchangeHandler extends ExchangeHandlerBase
{
    private final CommentHandler<CommentElement> commentHandler;

    private final static String commentGUIDParameterName = "commentGUID";

    /**
     * Construct the comment exchange handler with information needed to work with comment related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public CommentExchangeHandler(String                             serviceName,
                                  String                             serverName,
                                  InvalidParameterHandler            invalidParameterHandler,
                                  RepositoryHandler                  repositoryHandler,
                                  OMRSRepositoryHelper               repositoryHelper,
                                  String                             localServerUserId,
                                  OpenMetadataServerSecurityVerifier securityVerifier,
                                  List<String>                       supportedZones,
                                  List<String>                       defaultZones,
                                  List<String>                       publishZones,
                                  AuditLog                           auditLog)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        commentHandler = new CommentHandler<>(new CommentConverter<>(repositoryHelper, serviceName, serverName),
                                              CommentElement.class,
                                              serviceName,
                                              serverName,
                                              invalidParameterHandler,
                                              repositoryHandler,
                                              repositoryHelper,
                                              localServerUserId,
                                              securityVerifier,
                                              supportedZones,
                                              defaultZones,
                                              publishZones,
                                              auditLog);
    }



    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */


    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToComments(String                userId,
                                                    String                assetManagerGUID,
                                                    String                assetManagerName,
                                                    List<CommentElement>  results,
                                                    String                methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement comment : results)
            {
                if ((comment != null) && (comment.getElementHeader() != null) && (comment.getElementHeader().getGUID() != null))
                {
                    comment.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                 comment.getElementHeader().getGUID(),
                                                                                 commentGUIDParameterName,
                                                                                 OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                                                 assetManagerGUID,
                                                                                 assetManagerName,
                                                                                 false,
                                                                                 false,
                                                                                 null,
                                                                                 methodName));
                }
            }
        }
    }


    /**
     * Create a new comment.
     *
     * @param userId calling user
     * @param guid unique identifier of the element to attach the comment to
     * @param guidParameterName parameter for guid
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param isPublic is this visible to other people
     * @param commentProperties properties to store
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createComment(String                        userId,
                                String                        guid,
                                String                        guidParameterName,
                                MetadataCorrelationProperties correlationProperties,
                                boolean                       isPublic,
                                CommentProperties             commentProperties,
                                boolean                       forLineage,
                                boolean                       forDuplicateProcessing,
                                String                        methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String propertiesParameterName    = "commentProperties";
        final String commentText = "commentProperties.getText";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(commentProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(commentProperties.getCommentText(), commentText, methodName);

        int commentType = CommentType.STANDARD_COMMENT.getOpenTypeOrdinal();

        if (commentProperties.getCommentType() != null)
        {
            commentType = commentProperties.getCommentType().getOpenTypeOrdinal();
        }

        String commentGUID = commentHandler.attachNewComment(userId,
                                                             correlationProperties.getAssetManagerGUID(),
                                                             getExternalSourceName(correlationProperties),
                                                             guid,
                                                             guid,
                                                             guidParameterName,
                                                             commentType,
                                                             commentProperties.getCommentText(),
                                                             isPublic,
                                                             commentProperties.getEffectiveFrom(),
                                                             commentProperties.getEffectiveTo(),
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             methodName);

        if (commentGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          commentGUID,
                                          commentGUIDParameterName,
                                          OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          null,
                                          methodName);
        }

        return commentGUID;
    }


    /**
     * Update the metadata element representing a comment.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param commentGUID unique identifier of the metadata element to update
     * @param commentProperties new properties for this element
     * @param isMergeUpdate should the properties be merged with the existing properties or completely over-write them
     * @param isPublic is this visible to other people
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateComment(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        commentGUID,
                              CommentProperties             commentProperties,
                              boolean                       isMergeUpdate,
                              boolean                       isPublic,
                              boolean                       forLineage,
                              boolean                       forDuplicateProcessing,
                              Date                          effectiveTime,
                              String                        methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String propertiesParameterName    = "commentProperties";
        final String qualifiedNameParameterName = "commentProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(commentProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(commentProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        int commentType = CommentType.STANDARD_COMMENT.getOpenTypeOrdinal();

        if (commentProperties.getCommentType() != null)
        {
            commentType = commentProperties.getCommentType().getOpenTypeOrdinal();
        }

        this.validateExternalIdentifier(userId,
                                        commentGUID,
                                        commentGUIDParameterName,
                                        OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        commentHandler.updateComment(userId,
                                     getExternalSourceGUID(correlationProperties),
                                     getExternalSourceName(correlationProperties),
                                     commentGUID,
                                     commentGUIDParameterName,
                                     commentProperties.getQualifiedName(),
                                     commentType,
                                     commentProperties.getCommentText(),
                                     isPublic,
                                     isMergeUpdate,
                                     commentProperties.getEffectiveFrom(),
                                     commentProperties.getEffectiveTo(),
                                     forLineage,
                                     forDuplicateProcessing,
                                     effectiveTime,
                                     methodName);
    }


    /**
     * Remove the metadata element representing a comment.  This will delete the comment and all comment replies.
     *
     * @param userId calling user
     * @param correlationProperties properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param commentGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeComment(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              String                        commentGUID,
                              boolean                       forLineage,
                              boolean                       forDuplicateProcessing,
                              Date                          effectiveTime,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(commentGUID, commentGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        commentGUID,
                                        commentGUIDParameterName,
                                        OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        if (correlationProperties != null)
        {
            commentHandler.removeCommentFromElement(userId,
                                                    correlationProperties.getAssetManagerGUID(),
                                                    correlationProperties.getAssetManagerName(),
                                                    commentGUID,
                                                    commentGUIDParameterName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
        else
        {
            commentHandler.removeCommentFromElement(userId,
                                                    null,
                                                    null,
                                                    commentGUID,
                                                    commentGUIDParameterName,
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
        }
    }



    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param isPublic who can retrieve the relationship
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID,
                                    boolean isPublic,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String questionCommentGUIDParameterName = "questionCommentGUID";
        final String answerCommentGUIDParameterName = "answerCommentGUID";

        commentHandler.setupAcceptedAnswer(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           questionCommentGUID,
                                           questionCommentGUIDParameterName,
                                           answerCommentGUID,
                                           answerCommentGUIDParameterName,
                                           isPublic,
                                           effectiveFrom,
                                           effectiveTo,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);

        externalIdentifierHandler.logRelationshipCreation(assetManagerGUID,
                                                          assetManagerName,
                                                          OpenMetadataAPIMapper.ANSWER_RELATIONSHIP_TYPE_GUID,
                                                          questionCommentGUID,
                                                          OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                          answerCommentGUID,
                                                          OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                          methodName);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  questionCommentGUID,
                                    String  answerCommentGUID,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String questionCommentGUIDParameterName = "questionCommentGUID";
        final String answerCommentGUIDParameterName = "answerCommentGUID";

        commentHandler.clearAcceptedAnswer(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           questionCommentGUID,
                                           questionCommentGUIDParameterName,
                                           answerCommentGUID,
                                           answerCommentGUIDParameterName,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);

        externalIdentifierHandler.logRelationshipRemoval(assetManagerGUID,
                                                         assetManagerName,
                                                         OpenMetadataAPIMapper.ANSWER_RELATIONSHIP_TYPE_NAME,
                                                         questionCommentGUID,
                                                         OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                         answerCommentGUID,
                                                         OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                         methodName);
    }


    /**
     * Retrieve the list of comment metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter for search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<CommentElement> findComments(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  searchString,
                                             String  searchStringParameterName,
                                             int     startFrom,
                                             int     pageSize,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing,
                                             Date    effectiveTime,
                                             String  methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        List<CommentElement> results = commentHandler.findBeans(userId,
                                                                searchString,
                                                                searchStringParameterName,
                                                                OpenMetadataAPIMapper.COMMENT_TYPE_GUID,
                                                                OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                                "qualifiedName",
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

        addCorrelationPropertiesToComments(userId, assetManagerGUID, assetManagerName, results , methodName);

        return results;
    }


    /**
     * Return the comments attached to an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID    unique identifier for the element where the like is attached.
     * @param elementGUIDParameterName name of parameter for elementGUID
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<CommentElement> getAttachedComments(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  elementGUID,
                                                    String  elementGUIDParameterName,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        List<CommentElement> results = commentHandler.getComments(userId,
                                                                  elementGUID,
                                                                  elementGUIDParameterName,
                                                                  OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                                  startFrom,
                                                                  pageSize,
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  effectiveTime,
                                                                  methodName);

        addCorrelationPropertiesToComments(userId, assetManagerGUID, assetManagerName, results , methodName);

        return results;
    }


    /**
     * Retrieve the comment metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param commentGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommentElement getCommentByGUID(String  userId,
                                           String  assetManagerGUID,
                                           String  assetManagerName,
                                           String  commentGUID,
                                           boolean forLineage,
                                           boolean forDuplicateProcessing,
                                           Date    effectiveTime,
                                           String  methodName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String guidParameterName  = "commentGUID";

        CommentElement comment = commentHandler.getBeanFromRepository(userId,
                                                                      commentGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName);

        if (comment != null)
        {
            comment.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                        commentGUID,
                                                                        guidParameterName,
                                                                        OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName));
        }

        return comment;
    }
}
