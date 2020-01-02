/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.CommentBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.CommentConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CommentMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Comment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * CommentHandler manages Comment objects.  It runs server-side in
 * the OMAG Server Platform and retrieves Comment entities through the OMRSRepositoryConnector.
 */
public abstract class FeedbackHandlerBase extends AttachmentHandlerBase
{

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param lastAttachmentHandler handler for recording last attachment
     */
    public FeedbackHandlerBase(String                  serviceName,
                               String                  serverName,
                               InvalidParameterHandler invalidParameterHandler,
                               RepositoryHandler       repositoryHandler,
                               OMRSRepositoryHelper    repositoryHelper,
                               LastAttachmentHandler   lastAttachmentHandler)
    {
        super(serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, lastAttachmentHandler);
    }


    /**
     * Work out whether the requesting user is able to see the attached feedback.
     *
     * @param userId calling user
     * @param relationship relationship to the feedback content
     * @param methodName calling method
     * @return boolean - true if allowed
     */
    protected boolean  visibleToUser(String        userId,
                                     Relationship  relationship,
                                     String        methodName)
    {
        if (userId.equals(relationship.getCreatedBy()))
        {
            return true;
        }

        return repositoryHelper.getBooleanProperty(serviceName,
                                                   CommentMapper.IS_PUBLIC_PROPERTY_NAME,
                                                   relationship.getProperties(),
                                                   methodName);
    }


    /**
     * Count up the number of elements of a certain type that are attached to an anchor entity.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier for the entity that the object is attached to (anchor entity)
     * @param anchorTypeName type of the anchor entity
     * @param attachmentTypeGUID unique identifier of the attachment's type
     * @param attachmentTypeName unique name of the attachment's type
     * @param methodName calling method
     *
     * @return count of attached objects
     *
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected int countAttachments(String   userId,
                                   String   anchorGUID,
                                   String   anchorTypeName,
                                   String   attachmentTypeGUID,
                                   String   attachmentTypeName,
                                   String   methodName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException
    {
        final String guidParameterName      = "anchorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, guidParameterName, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                    anchorGUID,
                                                                                    anchorTypeName,
                                                                                    attachmentTypeGUID,
                                                                                    attachmentTypeName,
                                                                                    methodName);

        int count = 0;

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (this.visibleToUser(userId, relationship, methodName))
                    {
                        count ++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Return the relationships to required elements attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the identifier is attached to
     * @param anchorTypeName type name for anchor
     * @param attachmentTypeGUID unique identifier of the attachment's type
     * @param attachmentTypeName unique name of the attachment's type
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved relationships or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<Relationship>  getAttachmentLinks(String   userId,
                                                  String   anchorGUID,
                                                  String   anchorTypeName,
                                                  String   attachmentTypeGUID,
                                                  String   attachmentTypeName,
                                                  int      startingFrom,
                                                  int      pageSize,
                                                  String   methodName) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        List<Relationship> relationships = super.getAttachmentLinks(userId,
                                                                    anchorGUID,
                                                                    anchorTypeName,
                                                                    attachmentTypeGUID,
                                                                    attachmentTypeName,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    methodName);

        if (relationships != null)
        {
            List<Relationship> visibleRelationships = new ArrayList<>();

            for (Relationship  relationship : relationships)
            {
                if (this.visibleToUser(userId,
                                       relationship,
                                       methodName))
                {
                    visibleRelationships.add(relationship);
                }
            }

            if (! visibleRelationships.isEmpty())
            {
                return visibleRelationships;
            }
        }

        return null;
    }


    /**
     * Return the entities for the required elements attached to an anchor entity.
     *
     * @param userId     calling user
     * @param anchorGUID identifier for the entity that the identifier is attached to
     * @param anchorTypeName type name for anchor
     * @param anchorAtEnd1  indicates which end to extract attachment from
     * @param attachmentTypeGUID unique identifier of the attachment's relationship type
     * @param attachmentTypeName unique name of the attachment's relationship type
     * @param entityTypeName unique name of the attached entity's type
     * @param startingFrom start position for results
     * @param pageSize     maximum number of results
     * @param methodName calling method
     *
     * @return list of retrieved objects or null if none found
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public List<EntityDetail>  getAttachments(String   userId,
                                              String   anchorGUID,
                                              String   anchorTypeName,
                                              boolean  anchorAtEnd1,
                                              String   attachmentTypeGUID,
                                              String   attachmentTypeName,
                                              String   entityTypeName,
                                              int      startingFrom,
                                              int      pageSize,
                                              String   methodName) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        List<Relationship> visibleRelationships = this.getAttachmentLinks(userId,
                                                                          anchorGUID,
                                                                          anchorTypeName,
                                                                          attachmentTypeGUID,
                                                                          attachmentTypeName,
                                                                          startingFrom,
                                                                          pageSize,
                                                                          methodName);

        if (visibleRelationships != null)
        {
            List<EntityDetail> visibleEntities = new ArrayList<>();

            for (Relationship  relationship : visibleRelationships)
            {
                if (this.visibleToUser(userId,
                                       relationship,
                                       methodName))
                {
                    EntityProxy entityProxy = relationship.getEntityTwoProxy();
                    if (! anchorAtEnd1)
                    {
                        entityProxy = relationship.getEntityOneProxy();
                    }

                    final String guidParameterName = "entityProxy.getGUID()";

                    visibleEntities.add(repositoryHandler.getEntityByGUID(userId,
                                                                          entityProxy.getGUID(),
                                                                          guidParameterName,
                                                                          entityTypeName,
                                                                          methodName));
                }
            }

            if (! visibleEntities.isEmpty())
            {
                return visibleEntities;
            }
        }

        return null;
    }
}
