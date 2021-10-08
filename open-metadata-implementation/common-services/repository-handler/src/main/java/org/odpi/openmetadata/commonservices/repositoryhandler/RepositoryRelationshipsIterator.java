/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
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
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelationshipsIterator
{
    private static final Logger log = LoggerFactory.getLogger(RepositoryRelatedEntitiesIterator.class);

    private RepositoryHandler  repositoryHandler;
    private String             userId;
    private String             startingEntityGUID;
    private String             startingEntityTypeName;
    private String             relationshipTypeGUID;
    private String             relationshipTypeName;
    private int                startingFrom;
    private int                requesterPageSize;
    private boolean            forDuplicateProcessing;
    private Date               effectiveTime;
    private String             methodName;
    private List<Relationship> relationshipsCache = null;


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param forDuplicateProcessing is this retrieve part of duplicate processing?
     * @param startingFrom initial position in the stored list.
     * @param requesterPageSize maximum number of definitions to return by this iterator.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     */
    public RepositoryRelationshipsIterator(RepositoryHandler repositoryHandler,
                                           String            userId,
                                           String            startingEntityGUID,
                                           String            startingEntityTypeName,
                                           String            relationshipTypeGUID,
                                           String            relationshipTypeName,
                                           boolean           forDuplicateProcessing,
                                           int               startingFrom,
                                           int               requesterPageSize,
                                           Date              effectiveTime,
                                           String            methodName)
    {
        this.repositoryHandler      = repositoryHandler;
        this.userId                 = userId;
        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeName   = relationshipTypeName;
        this.startingFrom           = startingFrom;
        this.requesterPageSize      = requesterPageSize;
        this.forDuplicateProcessing = forDuplicateProcessing;
        this.effectiveTime          = effectiveTime;
        this.methodName             = methodName;

        if (log.isDebugEnabled())
        {
            log.debug("RepositoryRelationshipsIterator constructor startingEntityGUID=" + this.startingEntityGUID);
        }
    }


    /**
     * Determine if there is more to receive.  It will populate the iterator's cache with more content.
     *
     * @return boolean flag
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    public boolean  moreToReceive() throws UserNotAuthorizedException,
                                           PropertyServerException
    {
        if ((relationshipsCache == null) || (relationshipsCache.isEmpty()))
        {
            relationshipsCache = new ArrayList<>();

            /*
             * The loop is needed to ensure that another retrieve is attempted if the repository handler returns an empty list.
             * This occurs if all elements returned from the repositories do not match the effectiveTime requested.
             */
            while ((relationshipsCache != null) && (relationshipsCache.isEmpty()))
            {
                relationshipsCache = repositoryHandler.getRelationshipsByType(userId,
                                                                              startingEntityGUID,
                                                                              startingEntityTypeName,
                                                                              relationshipTypeGUID,
                                                                              relationshipTypeName,
                                                                              forDuplicateProcessing,
                                                                              startingFrom,
                                                                              requesterPageSize,
                                                                              effectiveTime,
                                                                              methodName);

                startingFrom = startingFrom + requesterPageSize;
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
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    public Relationship  getNext() throws UserNotAuthorizedException,
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
