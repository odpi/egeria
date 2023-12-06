/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.List;

/**
 * LikeHandler provides access and maintenance for Like objects and their attachment
 * to Referenceables.  There is no support for effectivity dates since this does not make sense for this element.
 * The Like is always anchored to its connected element
 */
public class LikeHandler<B> extends OpenMetadataAPIGenericHandler<B>
{
    /**
     * Construct the handler for likes.
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
    public LikeHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Count the number of Likes attached to an anchor entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     * @return unique identifier of the object or null
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countLikes(String  userId,
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
                                      OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                      OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                      2,
                                      forLineage,
                                      forDuplicateProcessing,
                                      effectiveTime,
                                      methodName);
    }


    /**
     * Return the Likes attached to an entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the feedback is attached to
     * @param elementGUIDParameterName name of parameter supplying the element guid
     * @param elementTypeName name of type for the element
     * @param serviceSupportedZones serviceSupportedZones
     * @param startingFrom where to start from in the list
     * @param pageSize maximum number of results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     * @return list of retrieved objects or null if none found
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<B>  getLikes(String       userId,
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
                                        null,
                                        null,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        elementTypeName,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                        OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                        null,
                                        null,
                                        0,
                                        forLineage,
                                        forDuplicateProcessing,
                                        serviceSupportedZones,
                                        startingFrom,
                                        pageSize,
                                        effectiveTime,
                                        methodName);
    }



    /**
     * Add or replace and existing Like for this user.
     *
     * @param userId      userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID   unique identifier for the liked entity (Referenceable).
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param isPublic   indicates whether the feedback should be shared or only be visible to the originating user
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void saveLike(String     userId,
                         String     externalSourceGUID,
                         String     externalSourceName,
                         String     elementGUID,
                         String     elementGUIDParameterName,
                         boolean    isPublic,
                         boolean    forLineage,
                         boolean    forDuplicateProcessing,
                         Date       effectiveTime,
                         String     methodName) throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        try
        {
            this.removeLike(userId, externalSourceGUID, externalSourceName, elementGUID, elementGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
        }
        catch (Exception error)
        {
            /*
             * Exception means this is the first rating from user.
             */
        }

        LikeBuilder builder = new LikeBuilder(isPublic,
                                              repositoryHelper,
                                              serviceName,
                                              serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    elementGUID,
                                    elementGUIDParameterName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        String likeGUID = this.createBeanInRepository(userId,
                                                      externalSourceGUID,
                                                      externalSourceName,
                                                      OpenMetadataAPIMapper.LIKE_TYPE_GUID,
                                                      OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                                      builder,
                                                      effectiveTime,
                                                      methodName);

        if (likeGUID != null)
        {
            final String likeGUIDParameterName = "likeGUID";

            this.uncheckedLinkElementToElement(userId,
                                               externalSourceGUID,
                                               externalSourceName,
                                               elementGUID,
                                               elementGUIDParameterName,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                               likeGUID,
                                               likeGUIDParameterName,
                                               OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                               forLineage,
                                               forDuplicateProcessing,
                                               supportedZones,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                               OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                               builder.getRelationshipInstanceProperties(methodName),
                                               effectiveTime,
                                               methodName);
        }
    }


    /**
     * Remove the requested like.
     *
     * @param userId       calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID    object where rating is attached
     * @param elementGUIDParameterName parameter supplying the elementGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     * @param methodName   calling method
     * @throws InvalidParameterException  the entity guid is not known
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public   void removeLike(String  userId,
                             String  externalSourceGUID,
                             String  externalSourceName,
                             String  elementGUID,
                             String  elementGUIDParameterName,
                             boolean forLineage,
                             boolean forDuplicateProcessing,
                             Date    effectiveTime,
                             String  methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        String likeGUID = this.unlinkConnectedElement(userId,
                                                        true,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        elementGUID,
                                                        elementGUIDParameterName,
                                                        OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        supportedZones,
                                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_GUID,
                                                        OpenMetadataAPIMapper.REFERENCEABLE_TO_LIKE_TYPE_NAME,
                                                        OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                                        effectiveTime,
                                                        methodName);

        if (likeGUID != null)
        {
            final String ratingGUIDParameterName = "likeGUID";

            this.deleteBeanInRepository(userId,
                                        externalSourceGUID,
                                        externalSourceName,
                                        likeGUID,
                                        ratingGUIDParameterName,
                                        OpenMetadataAPIMapper.LIKE_TYPE_GUID,
                                        OpenMetadataAPIMapper.LIKE_TYPE_NAME,
                                        null,
                                        null,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);
        }
    }
}
