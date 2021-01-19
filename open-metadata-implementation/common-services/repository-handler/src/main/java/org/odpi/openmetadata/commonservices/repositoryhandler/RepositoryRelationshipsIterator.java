/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

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
    private RepositoryHandler  repositoryHandler;
    private String             userId;
    private String             startingEntityGUID;
    private String             startingEntityTypeName;
    private String             relationshipTypeGUID;
    private String             relationshipTypeName;
    private int                startingFrom;
    private int                requesterPageSize;
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
     * @param startingFrom initial position in the stored list.
     * @param requesterPageSize maximum number of definitions to return by this iterator.
     * @param methodName  name of calling method
     */
    public RepositoryRelationshipsIterator(RepositoryHandler repositoryHandler,
                                           String            userId,
                                           String            startingEntityGUID,
                                           String            startingEntityTypeName,
                                           String            relationshipTypeGUID,
                                           String            relationshipTypeName,
                                           int               startingFrom,
                                           int               requesterPageSize,
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
        this.methodName             = methodName;
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
            relationshipsCache = repositoryHandler.getRelationshipsByType(userId,
                                                                          startingEntityGUID,
                                                                          startingEntityTypeName,
                                                                          relationshipTypeGUID,
                                                                          relationshipTypeName,
                                                                          startingFrom,
                                                                          requesterPageSize,
                                                                          methodName);

            if (relationshipsCache != null)
            {
                startingFrom = startingFrom + relationshipsCache.size();
            }
        }

        return ! ((relationshipsCache == null) || (relationshipsCache.isEmpty()));
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
