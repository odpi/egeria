/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countAttachedComments(String userId,
                                     String elementGUID,
                                     String methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        return super.countAttachments(userId,
                                      elementGUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                      methodName);
    }


    /**
     * Adds a comment and link it to the supplied parent entity.
     *
     * @param userId        String - userId of user making request.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID    head of the comment chain
     * @param parentGUID    String - unique id for a referenceable entity that the comment is to be attached to.
     * @param parentGUIDParameterName name of parameter that supplied the entity's unique identifier.
     * @param commentType   ordinal of comment enum.
     * @param commentText   String - the text of the comment.
     * @param isPublic      should this be visible to all or private to the caller
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
                                    String      methodName) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String textParameter = "commentText";
        final String commentGUIDParameter = "commentGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(parentGUID, parentGUIDParameterName, methodName);
        invalidParameterHandler.validateText(commentText, textParameter, methodName);

        /*
         * A comment is a referenceable.  It needs a unique qualified name.  There is no obvious value to use so
         * a UUID is used to create a unique string.
         */
        String qualifiedName = UUID.randomUUID().toString();
        CommentBuilder builder = new CommentBuilder(qualifiedName,
                                                    commentType,
                                                    commentText,
                                                    isPublic,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        if (anchorGUID != null)
        {
            builder.setAnchors(userId, anchorGUID, methodName);
        }

        String  commentGUID = this.createBeanInRepository(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          OpenMetadataAPIMapper.COMMENT_TYPE_GUID,
                                                          OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                                          qualifiedName,
                                                          OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                          builder,
                                                          methodName);

        if (commentGUID != null)
        {
            this.linkElementToElement(userId,
                                      externalSourceGUID,
                                      externalSourceName,
                                      parentGUID,
                                      parentGUIDParameterName,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      commentGUID,
                                      commentGUIDParameter,
                                      OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                      builder.getRelationshipInstanceProperties(methodName),
                                      methodName);
        }

        return commentGUID;
    }


    /**
     * Update an existing comment.
     *
     * @param userId        userId of user making request.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param commentGUID   unique identifier for the comment to change
     * @param commentGUIDParameterName name of parameter for commentGUID
     * @param commentType   type of comment enum.
     * @param commentText   the text of the comment.
     * @param isPublic      indicates whether the feedback should be shared or only be visible to the originating user
     * @param methodName    calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the asset properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateComment(String      userId,
                                String      externalSourceGUID,
                                String      externalSourceName,
                                String      commentGUID,
                                String      commentGUIDParameterName,
                                int         commentType,
                                String      commentText,
                                boolean     isPublic,
                                String      methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        final String textParameter             = "commentText";

        invalidParameterHandler.validateText(commentText, textParameter, methodName);

        CommentBuilder builder = new CommentBuilder(null,
                                                    commentType,
                                                    commentText,
                                                    isPublic,
                                                    repositoryHelper,
                                                    serviceName,
                                                    serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    commentGUID,
                                    commentGUIDParameterName,
                                    OpenMetadataAPIMapper.COMMENT_TYPE_GUID,
                                    OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                    builder.getInstanceProperties(methodName),
                                    true,
                                    methodName);
    }


    /**
     * Removes a comment added to the parent by this user.
     *
     * @param userId       userId of user making request.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param commentGUIDParameterName parameter supplying the
     * @param commentGUID  unique identifier for the comment object.
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
                                         String     methodName) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    commentGUID,
                                    commentGUIDParameterName,
                                    OpenMetadataAPIMapper.COMMENT_TYPE_GUID,
                                    OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                    null,
                                    null,
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
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                        OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        methodName);
    }


    /**
     * Return the comments attached to an entity. (No special security checking is required).
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the comment is attached to
     * @param elementGUIDParameterName name of the parameter providing the element GUID
     * @param elementTypeName name of the type of the anchor entity
     * @param serviceSupportedZones supported zones for the particular service
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param methodName calling method
     *
     * @return list of retrieved objects
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getComments(String       userId,
                                String       elementGUID,
                                String       elementGUIDParameterName,
                                String       elementTypeName,
                                List<String> serviceSupportedZones,
                                int          startingFrom,
                                int          pageSize,
                                String       methodName) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                        OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
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
                                String   methodName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return this.getAttachedElements(userId,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_COMMENT_TYPE_NAME,
                                        OpenMetadataAPIMapper.COMMENT_TYPE_NAME,
                                        supportedZones,
                                        startingFrom,
                                        pageSize,
                                        methodName);
    }
}
