/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * RepositoryEntitiesIterator is an iterator class for iteratively retrieving entities (possibly restricting
 * the type of entities returned).  It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryEntitiesIterator extends RepositoryIteratorForEntities
{
    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the relationship to follow
     * @param entityTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositoryEntitiesIterator(RepositoryHandler repositoryHandler,
                                      String            userId,
                                      String            entityTypeGUID,
                                      String            entityTypeName,
                                      int               startingFrom,
                                      int               pageSize,
                                      String            methodName)
    {
        super(repositoryHandler, userId, entityTypeGUID, entityTypeName, startingFrom, pageSize, methodName);
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
            entitiesCache = repositoryHandler.getEntitiesForType(userId,
                                                                 entityTypeGUID,
                                                                 entityTypeName,
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
