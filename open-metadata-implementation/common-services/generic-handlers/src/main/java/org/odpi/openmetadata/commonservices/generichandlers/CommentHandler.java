/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * CommentHandler manages Comment objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Comment entities through the OMRSRepositoryConnector.
 */
public class CommentHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public CommentHandler(OpenMetadataAPIGenericConverter<B> converter,
                          Class<B>                           beanClass,
                          String                             serviceName,
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
        super(converter,
              beanClass,
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


    /**
     * Count the number of comments attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countAttachedComments(String  userId,
                                     String  elementGUID,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing,
                                     Date    effectiveTime,
                                     String  methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataType.REFERENCEABLE.typeName,
                                      OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                      OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Adds a comment and link it to the supplied parent entity.
     *
     * @param userId        String - userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID    head of the comment chain
     * @param parentGUID    String - unique id for a referenceable entity that the comment is to be attached to.
     * @param parentGUIDParameterName name of parameter that supplied the entity's unique identifier.
     * @param commentType   ordinal of comment enum.
     * @param commentText   String - the text of the comment.
     * @param isPublic      should this be visible to all or private to the caller
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @return guid of new comment.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public  String attachNewComment(String      userId,
                                    String      externalSourceGUID,
                                    String      externalSourceName,
                                    String      anchorGUID,
                                    String      parentGUID,
                                    String      parentGUIDParameterName,
                                    int         commentType,
                                    String      commentText,
                                    boolean     isPublic,
                                    Date        effectiveFrom,
                                    Date        effectiveTo,
                                    boolean     forLineage,
                                    boolean     forDuplicateProcessing,
                                    Date        effectiveTime,
                                    String      methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        return attachNewComment(userId,
                                externalSourceGUID,
                                externalSourceName,
                                anchorGUID,
                                parentGUID,
                                parentGUIDParameterName,
                                null,
                                commentType,
                                commentText,
                                isPublic,
                                effectiveFrom,
                                effectiveTo,
                                forLineage,
                                forDuplicateProcessing,
                                effectiveTime,
                                methodName);
    }


    /**
     * Adds a comment and link it to the supplied parent entity.
     *
     * @param userId        String - userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID    head of the comment chain
     * @param parentGUID    String - unique id for a referenceable entity that the comment is to be attached to.
     * @param parentGUIDParameterName name of parameter that supplied the entity's unique identifier.
     * @param suppliedQualifiedName unique identifier for the comment
     * @param commentType   ordinal of comment enum.
     * @param commentText   String - the text of the comment.
     * @param isPublic      should this be visible to all or private to the caller
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @return guid of new comment.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException There is a problem adding the asset properties to
     *                                   the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public  String attachNewComment(String      userId,
                                    String      externalSourceGUID,
                                    String      externalSourceName,
                                    String      anchorGUID,
                                    String      parentGUID,
                                    String      parentGUIDParameterName,
                                    String      suppliedQualifiedName,
                                    int         commentType,
                                    String      commentText,
                                    boolean     isPublic,
                                    Date        effectiveFrom,
                                    Date        effectiveTo,
                                    boolean     forLineage,
                                    boolean     forDuplicateProcessing,
                                    Date        effectiveTime,
                                    String      methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String textParameter = "commentText";
        final String commentGUIDParameter = "commentGUID";
        final String anchorGUIDParameter = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateText(commentText, textParameter, methodName);

        String qualifiedName = suppliedQualifiedName;
        if (qualifiedName == null)
        {
            qualifiedName = UUID.randomUUID().toString();
        }

        /*
         * A comment is a referenceable.  It needs a unique qualified name.  There is no obvious value to use so
         * a UUID is used to create a unique string.
         */
        CommentBuilder builder = new CommentBuilder(qualifiedName,
                                                    commentType,
                                                    commentText,
                                                    isPublic,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameter,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        String  commentGUID = this.createBeanInRepository(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          OpenMetadataType.COMMENT_TYPE_GUID,
                                                          OpenMetadataType.COMMENT_TYPE_NAME,
                                                          builder,
                                                          effectiveTime,
                                                          methodName);

        if (commentGUID != null)
        {
            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               parentGUID,
                                               parentGUIDParameterName,
                                               OpenMetadataType.REFERENCEABLE.typeName,
                                               commentGUID,
                                               commentGUIDParameter,
                                               OpenMetadataType.COMMENT_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                               OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                               builder.getRelationshipInstanceProperties(methodName),
                                               effectiveFrom,
                                               methodName);
        }

        return commentGUID;
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param commentGUID   unique identifier for the comment to change
     * @param commentGUIDParameterName name of parameter for commentGUID
     * @param qualifiedName unique name of the comment
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String              userId,
                                String              externalSourceGUID,
                                String              externalSourceName,
                                String              commentGUID,
                                String              commentGUIDParameterName,
                                String              qualifiedName,
                                int                 commentType,
                                String              commentText,
                                boolean             isPublic,
                                boolean             isMergeUpdate,
                                Date                effectiveFrom,
                                Date                effectiveTo,
                                boolean             forLineage,
                                boolean             forDuplicateProcessing,
                                Date                effectiveTime,
                                String              methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String textParameter = "commentText";

        invalidParameterHandler.validateText(commentText, textParameter, methodName);

        EntityDetail startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        commentGUID,
                                                                        commentGUIDParameterName,
                                                                        OpenMetadataType.COMMENT_TYPE_NAME,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);

        CommentBuilder builder = new CommentBuilder(qualifiedName,
                                                    commentType,
                                                    commentText,
                                                    isPublic,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    startingEntity,
                                    commentGUIDParameterName,
                                    OpenMetadataType.COMMENT_TYPE_GUID,
                                    OpenMetadataType.COMMENT_TYPE_NAME,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);

        List<Relationship> relationships = this.getAttachmentLinks(userId,
                                                                   startingEntity,
                                                                   commentGUIDParameterName,
                                                                   OpenMetadataType.COMMENT_TYPE_NAME,
                                                                   OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                                                   OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                                                   null,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   1,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   supportedZones,
                                                                   0,
                                                                   0,
                                                                   effectiveTime,
                                                                   methodName);


        if ((relationships == null) || (relationships.isEmpty()))
        {
            errorHandler.handleNoRelationship(commentGUID,
                                              OpenMetadataType.COMMENT_TYPE_NAME,
                                              OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                              methodName);
        }
        else if (relationships.size() == 1)
        {
            repositoryHandler.updateRelationshipProperties(userId,
                                                           externalSourceGUID,
                                                           externalSourceName,
                                                           relationships.get(0),
                                                           builder.getRelationshipInstanceProperties(methodName),
                                                           methodName);
        }
        else
        {
            errorHandler.handleAmbiguousRelationships(commentGUID,
                                                      OpenMetadataType.COMMENT_TYPE_NAME,
                                                      OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                                      relationships,
                                                      methodName);
        }

    }


    /**
     * Removes a comment added to the parent by this user.
     *
     * @param userId       userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param commentGUIDParameterName parameter supplying the
     * @param commentGUID  unique identifier for the comment object.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem updating the asset properties in the property server.
     * @throws UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public void removeCommentFromElement(String     userId,
                                         String     externalSourceGUID,
                                         String     externalSourceName,
                                         String     commentGUID,
                                         String     commentGUIDParameterName,
                                         boolean    forLineage,
                                         boolean    forDuplicateProcessing,
                                         Date       effectiveTime,
                                         String     methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    commentGUID,
                                    commentGUIDParameterName,
                                    OpenMetadataType.COMMENT_TYPE_GUID,
                                    OpenMetadataType.COMMENT_TYPE_NAME,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Link a comment that contains the best answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param questionCommentGUIDParameterName parameter supplying questionCommentGUID
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param answerCommentGUIDParameterName parameter supplying the answerCommentGUID
     * @param isPublic who can retrieve the relationship
     * @param effectiveFrom  the time that the relationship element must be effective from (null for any time, new Date() for now)
     * @param effectiveTo  the time that the relationship must be effective to (null for any time, new Date() for now)
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupAcceptedAnswer(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  questionCommentGUID,
                                    String  questionCommentGUIDParameterName,
                                    String  answerCommentGUID,
                                    String  answerCommentGUIDParameterName,
                                    boolean isPublic,
                                    Date    effectiveFrom,
                                    Date    effectiveTo,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        InstanceProperties instanceProperties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                              null,
                                                                                              OpenMetadataProperty.IS_PUBLIC.name,
                                                                                              isPublic,
                                                                                              methodName);
        this.linkElementToElement(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  questionCommentGUID,
                                  questionCommentGUIDParameterName,
                                  OpenMetadataType.COMMENT_TYPE_NAME,
                                  answerCommentGUID,
                                  answerCommentGUIDParameterName,
                                  OpenMetadataType.COMMENT_TYPE_NAME,
                                  forLineage,
                                  forDuplicateProcessing,
                                  supportedZones,
                                  OpenMetadataType.ANSWER_RELATIONSHIP_TYPE_GUID,
                                  OpenMetadataType.ANSWER_RELATIONSHIP_TYPE_NAME,
                                  instanceProperties,
                                  effectiveFrom,
                                  effectiveTo,
                                  effectiveTime,
                                  methodName);
    }


    /**
     * Unlink a comment that contains an answer to a question posed in another comment.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param questionCommentGUID unique identifier of the comment containing the question
     * @param questionCommentGUIDParameterName parameter supplying questionCommentGUID
     * @param answerCommentGUID unique identifier of the comment containing the accepted answer
     * @param answerCommentGUIDParameterName parameter supplying answerCommentGUID
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAcceptedAnswer(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  questionCommentGUID,
                                    String  questionCommentGUIDParameterName,
                                    String  answerCommentGUID,
                                    String  answerCommentGUIDParameterName,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    String  methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        this.unlinkElementFromElement(userId,
                                      false,
                                      externalSourceGUID,
                                      externalSourceName,
                                      questionCommentGUID,
                                      questionCommentGUIDParameterName,
                                      OpenMetadataType.COMMENT_TYPE_NAME,
                                      answerCommentGUID,
                                      answerCommentGUIDParameterName,
                                      OpenMetadataType.COMMENT_TYPE_GUID,
                                      OpenMetadataType.COMMENT_TYPE_NAME,
                                      forLineage,
                                      forDuplicateProcessing,
                                      OpenMetadataType.ANSWER_RELATIONSHIP_TYPE_GUID,
                                      OpenMetadataType.ANSWER_RELATIONSHIP_TYPE_NAME,
                                      effectiveTime,
                                      methodName);
    }

    /**
     * Return the comments attached to an entity.
     *
     * @param userId     calling user
     * @param anchorGUID expected anchorGUID for this element
     * @param anchorGUIDParameterName parameter supplying anchorGUID
     * @param elementGUID identifier for the entity that the comment is attached to
     * @param elementGUIDParameterName name of the parameter providing the element GUID
     * @param elementTypeName name of the type of the anchor entity
     * @param serviceSupportedZones supported zones for the particular service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getComments(String       userId,
                                String       anchorGUID,
                                String       anchorGUIDParameterName,
                                String       elementGUID,
                                String       elementGUIDParameterName,
                                String       elementTypeName,
                                List<String> serviceSupportedZones,
                                int          startingFrom,
                                int          pageSize,
                                boolean      forLineage,
                                boolean      forDuplicateProcessing,
                                Date         effectiveTime,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        anchorGUID,
                                        anchorGUIDParameterName,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                        OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                        OpenMetadataType.COMMENT_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Return the comments attached to an anchor entity. (No special security checking is required).
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the comment is attached to
     * @param elementGUIDParameterName name of the parameter providing the element GUID
     * @param elementTypeName name of the type of the anchor entity
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getComments(String   userId,
                                String   elementGUID,
                                String   elementGUIDParameterName,
                                String   elementTypeName,
                                int      startingFrom,
                                int      pageSize,
                                boolean  forLineage,
                                boolean  forDuplicateProcessing,
                                Date     effectiveTime,
                                String   methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                        OpenMetadataType.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                        OpenMetadataType.COMMENT_TYPE_NAME,
                                        null,
                                        null,
                                        2,
                                        forLineage,
                                        forDuplicateProcessing,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }
}
