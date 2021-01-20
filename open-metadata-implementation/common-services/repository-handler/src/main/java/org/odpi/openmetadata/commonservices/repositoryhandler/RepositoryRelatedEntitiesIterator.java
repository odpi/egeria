/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * RepositoryRelatedEntitiesIterator is an iterator class for iteratively retrieving relationships for an starting entity (possibly restricting
 * the type of relationships returned) and returning the entity at the other end.  It is used where the caller needs to filter the results coming
 * from the repository and may need to make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelatedEntitiesIterator extends RepositoryIteratorForEntities
{
    private String             startingEntityGUID;
    private String             startingEntityTypeName;
    private String             relationshipTypeGUID;
    private String             relationshipTypeName;


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
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             String            userId,
                                             String            startingEntityGUID,
                                             String            startingEntityTypeName,
                                             String            relationshipTypeGUID,
                                             String            relationshipTypeName,
                                             int               startingFrom,
                                             int               pageSize,
                                             String            methodName)
    {
        super(repositoryHandler, userId, null, null, startingFrom, pageSize, methodName);

        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeName   = relationshipTypeName;
    }


    /**
     * Determine if there is more to receive.  It will populate the iterator's cache with more content.
     *
     * @return boolean flag
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    @Override
    public boolean  moreToReceive() throws UserNotAuthorizedException,
                                           PropertyServerException
    {
        if ((entitiesCache == null) || (entitiesCache.isEmpty()))
        {
            entitiesCache = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                             startingEntityGUID,
                                                                             startingEntityTypeName,
                                                                             relationshipTypeGUID,
                                                                             relationshipTypeName,
                                                                             startingFrom,
                                                                             pageSize,
                                                                             methodName);

            if (entitiesCache != null)
            {
                startingFrom = startingFrom + entitiesCache.size();
            }
        }

        return entitiesCache != null;
    }
}
