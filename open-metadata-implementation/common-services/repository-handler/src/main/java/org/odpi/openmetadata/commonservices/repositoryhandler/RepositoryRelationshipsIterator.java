/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * RepositoryRelationshipsIterator is an iterator class for iteratively retrieving relationships for a starting entity (possibly restricting
 * the type of relationships returned).  It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelationshipsIterator extends RepositoryIterator
{
    private static final Logger log = LoggerFactory.getLogger(RepositoryRelationshipsIterator.class);

    private final String startingEntityGUID;
    private final String startingEntityTypeName;
    private final String relationshipTypeGUID;
    private final String relationshipTypeName;
    private final int    selectionEnd;


    private List<Relationship> relationshipsCache = null;
    private EntityDetail  startingEntity;


    /**
     * Constructor if entity not already retrieved.  It takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param startingEntity  starting entity
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing is this retrieve part of duplicate processing?
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return by this iterator.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryRelationshipsIterator(RepositoryHandler       repositoryHandler,
                                           InvalidParameterHandler invalidParameterHandler,
                                           String                  userId,
                                           EntityDetail            startingEntity,
                                           String                  startingEntityTypeName,
                                           String                  relationshipTypeGUID,
                                           String                  relationshipTypeName,
                                           int                     selectionEnd,
                                           List<InstanceStatus>    limitResultsByStatus,
                                           Date                    asOfTime,
                                           SequencingOrder         sequencingOrder,
                                           String                  sequencingPropertyName,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           int                     startingFrom,
                                           int                     pageSize,
                                           Date                    effectiveTime,
                                           String                  methodName) throws InvalidParameterException
    {
        super(repositoryHandler,
              invalidParameterHandler,
              userId,
              startingFrom,
              pageSize,
              limitResultsByStatus,
              asOfTime,
              sequencingOrder,
              sequencingPropertyName,
              forLineage,
              forDuplicateProcessing,
              effectiveTime,
              methodName);

        this.startingEntity         = startingEntity;
        this.startingEntityGUID     = startingEntity.getGUID();
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeName   = relationshipTypeName;
        this.selectionEnd           = selectionEnd;

        log.debug("RepositoryRelationshipsIterator constructor startingEntityGUID=" + this.startingEntityGUID);
    }


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param limitResultsByStatus By default, relationships in all statuses (other than DELETE) are returned.  However, it is possible
     *                             to specify a list of statuses (for example ACTIVE) to restrict the results to.  Null means all status values.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingPropertyName String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing is this retrieve part of duplicate processing?
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return by this iterator.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryRelationshipsIterator(RepositoryHandler       repositoryHandler,
                                           InvalidParameterHandler invalidParameterHandler,
                                           String                  userId,
                                           String                  startingEntityGUID,
                                           String                  startingEntityTypeName,
                                           String                  relationshipTypeGUID,
                                           String                  relationshipTypeName,
                                           int                     selectionEnd,
                                           List<InstanceStatus>    limitResultsByStatus,
                                           Date                    asOfTime,
                                           SequencingOrder         sequencingOrder,
                                           String                  sequencingPropertyName,
                                           boolean                 forLineage,
                                           boolean                 forDuplicateProcessing,
                                           int                     startingFrom,
                                           int                     pageSize,
                                           Date                    effectiveTime,
                                           String                  methodName) throws InvalidParameterException
    {
        super(repositoryHandler,
              invalidParameterHandler,
              userId,
              startingFrom,
              pageSize,
              limitResultsByStatus,
              asOfTime,
              sequencingOrder,
              sequencingPropertyName,
              forLineage,
              forDuplicateProcessing,
              effectiveTime,
              methodName);

        this.startingEntity         = null;
        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeName   = relationshipTypeName;
        this.selectionEnd           = selectionEnd;

        log.debug("RepositoryRelationshipsIterator constructor startingEntityGUID=" + this.startingEntityGUID);
    }


    /**
     * Determine if there is more to receive.  It will populate the iterator's cache with more content.
     *
     * @return boolean flag
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException a problem in the repository
     */
    public boolean  moreToReceive() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        final String guidParameterName = "startingEntityGUID";

        if ((relationshipsCache == null) || (relationshipsCache.isEmpty()))
        {
            relationshipsCache = new ArrayList<>();

            if (this.startingEntity == null)
            {
                this.startingEntity = repositoryHandler.getEntityByGUID(userId,
                                                                        startingEntityGUID,
                                                                        guidParameterName,
                                                                        startingEntityTypeName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        effectiveTime,
                                                                        methodName);
            }

            /*
             * The loop is needed to ensure that another retrieve is attempted if the repository handler returns an empty list.
             * This occurs if all elements returned from the repositories do not match the effectiveTime requested.
             */
            while ((relationshipsCache != null) && (relationshipsCache.isEmpty()))
            {
                relationshipsCache = repositoryHandler.getRelationshipsByType(userId,
                                                                              startingEntity,
                                                                              startingEntityTypeName,
                                                                              relationshipTypeGUID,
                                                                              relationshipTypeName,
                                                                              selectionEnd,
                                                                              limitResultsByStatus,
                                                                              asOfTime,
                                                                              sequencingOrder,
                                                                              sequencingPropertyName,
                                                                              forLineage,
                                                                              forDuplicateProcessing,
                                                                              startingFrom,
                                                                              pageSize,
                                                                              effectiveTime,
                                                                              methodName);

                startingFrom = startingFrom + pageSize;
            }

            if (relationshipsCache != null)
            {
                if (log.isDebugEnabled())
                {
                    log.debug("relationshipsCache");
                    for (Relationship relationship : relationshipsCache)
                    {
                        log.debug("relationship guid" + relationship.getGUID() +
                                          " end1 " +
                                          relationship.getEntityOneProxy().getGUID() +
                                          " end2 " +
                                          relationship.getEntityTwoProxy().getGUID());
                    }
                }
            }
        }

        return (relationshipsCache != null);
    }


    /**
     * Return the next relationship.  It returns null if nothing left to retrieve.
     *
     * @return relationship or null
     * @throws InvalidParameterException bad parameter
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException a problem in the repository
     */
    public Relationship  getNext() throws InvalidParameterException,
                                          UserNotAuthorizedException,
                                          PropertyServerException
    {
        if (moreToReceive())
        {
            return relationshipsCache.remove(0);
        }
        else
        {
            return null;
        }
    }
}
