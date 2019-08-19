/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.LastAttachmentBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.LastAttachmentConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LastAttachmentMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.LastAttachment;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * LastAttachmentHandler supports the storing and retrieval of LastAttachment entities.
 * These entities are linked to a Referenceable and describe the last attachment to be
 * made to the referenceable.
 */
public class LastAttachmentHandler
{
    private String                  localServerUserId;
    private String                  serviceName;
    private OMRSRepositoryHelper    repositoryHelper;
    private String                  serverName;
    private RepositoryHandler       repositoryHandler;


    /**
     * Construct the last attachment handler with information needed to work with LastAttachment objects.
     *
     * @param serviceName name of this service
     * @param serverName name of the local server
     * @param localServerUserId name of the userId for system initiated updates to assets.
     * @param repositoryHandler handler for interfacing with the repository services
     * @param repositoryHelper    helper utilities for managing repository services objects
     */
    public LastAttachmentHandler(String                    serviceName,
                                 String                    serverName,
                                 String                    localServerUserId,
                                 RepositoryHandler         repositoryHandler,
                                 OMRSRepositoryHelper      repositoryHelper)
    {
        this.serviceName               = serviceName;
        this.repositoryHelper          = repositoryHelper;
        this.serverName                = serverName;
        this.localServerUserId         = localServerUserId;
        this.repositoryHandler         = repositoryHandler;
    }


    /**
     * Add a LastAttachment entity to a Referenceable.
     *
     * @param anchorGUID unique identifier for the referenceable
     * @param anchorType specific type name of the referenceable
     * @param attachmentGUID unique identifier of the attachment
     * @param attachmentType specific type of the attachment
     * @param attachmentOwner userId of the caller making the attachment
     * @param description description of the attachment
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException the repository is not available (or not operating correctly)
     * @throws UserNotAuthorizedException the local server is not authorized to attach to this referencable.
     */
    private void addLastAttachment(String   anchorGUID,
                                   String   anchorType,
                                   String   attachmentGUID,
                                   String   attachmentType,
                                   String   attachmentOwner,
                                   String   description,
                                   String   methodName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        LastAttachmentBuilder builder = new LastAttachmentBuilder(anchorGUID,
                                                                  anchorType,
                                                                  attachmentGUID,
                                                                  attachmentType,
                                                                  attachmentOwner,
                                                                  description,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  serverName);

        String entityGUID = repositoryHandler.createEntity(localServerUserId,
                                                           LastAttachmentMapper.LAST_ATTACHMENT_TYPE_GUID,
                                                           LastAttachmentMapper.LAST_ATTACHMENT_TYPE_NAME,
                                                           builder.getInstanceProperties(methodName),
                                                           methodName);

        repositoryHandler.createRelationship(localServerUserId,
                                             LastAttachmentMapper.REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_GUID,
                                             anchorGUID,
                                             entityGUID,
                                             null,
                                             methodName);
    }


    /**
     * Update the LastAttachment entity attached to a Referenceable.
     *
     * @param anchorGUID unique identifier for the referenceable
     * @param anchorType specific type name of the referenceable
     * @param attachmentGUID unique identifier of the attachment
     * @param attachmentType specific type of the attachment
     * @param attachmentOwner userId of the caller making the attachment
     * @param description description of the attachment
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException the repository is not available (or not operating correctly)
     * @throws UserNotAuthorizedException the local server is not authorized to update the LastAttachment for this referencable.
     */
    void updateLastAttachment(String   anchorGUID,
                              String   anchorType,
                              String   attachmentGUID,
                              String   attachmentType,
                              String   attachmentOwner,
                              String   description,
                              String   methodName) throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(localServerUserId,
                                                                             anchorGUID,
                                                                             anchorType,
                                                                             LastAttachmentMapper.REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_GUID,
                                                                             LastAttachmentMapper.REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_NAME,
                                                                             methodName);
        if (entity == null)
        {
            this.addLastAttachment(anchorGUID, anchorType, attachmentGUID, attachmentType, attachmentOwner, description, methodName);
        }
        else
        {
            LastAttachmentBuilder builder = new LastAttachmentBuilder(anchorGUID,
                                                                      anchorType,
                                                                      attachmentGUID,
                                                                      attachmentType,
                                                                      attachmentOwner,
                                                                      description,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);

            repositoryHandler.updateEntity(localServerUserId,
                                           entity.getGUID(),
                                           LastAttachmentMapper.LAST_ATTACHMENT_TYPE_GUID,
                                           LastAttachmentMapper.LAST_ATTACHMENT_TYPE_NAME,
                                           builder.getInstanceProperties(methodName),
                                           methodName);
        }
    }


    /**
     * Retrieve the last attachment entity - null is returned if it is not created.
     *
     * @param anchorGUID unique identifier of referenceable
     * @param anchorTypeName type name for the referenceable
     * @param methodName calling method
     *
     * @return LastAttachment bean or null
     * @throws PropertyServerException the repository is not available (or not operating correctly)
     * @throws UserNotAuthorizedException the local server is not authorized to update the LastAttachment for this referencable.
     */
    LastAttachment  getLastAttachment(String  anchorGUID,
                                      String  anchorTypeName,
                                      String  methodName) throws PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        LastAttachment lastAttachment = null;

        EntityDetail entity = repositoryHandler.getEntityForRelationshipType(localServerUserId,
                                                                             anchorGUID,
                                                                             anchorTypeName,
                                                                             LastAttachmentMapper.REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_GUID,
                                                                             LastAttachmentMapper.REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_NAME,
                                                                             methodName);

        if (entity != null)
        {
            LastAttachmentConverter converter = new LastAttachmentConverter(entity,
                                                                            repositoryHelper,
                                                                            serviceName);

            lastAttachment = converter.getBean();
        }

        return lastAttachment;
    }
}
