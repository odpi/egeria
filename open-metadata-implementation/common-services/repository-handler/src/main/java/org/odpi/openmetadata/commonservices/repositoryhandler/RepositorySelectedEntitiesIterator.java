/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * RepositorySelectedEntitiesIterator is an iterator class for iteratively retrieving entities based on a search criteria.
 * It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositorySelectedEntitiesIterator extends RepositoryIteratorForEntities
{
    private     InstanceProperties properties;
    private     MatchCriteria      matchCriteria;
    private     String             searchCriteria;

    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the relationship to follow
     * @param properties properties used in the search
     * @param matchCriteria all or any
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositorySelectedEntitiesIterator(RepositoryHandler  repositoryHandler,
                                              String             userId,
                                              String             entityTypeGUID,
                                              InstanceProperties properties,
                                              MatchCriteria      matchCriteria,
                                              int                startingFrom,
                                              int                pageSize,
                                              String             methodName)
    {
        super(repositoryHandler, userId, entityTypeGUID, null, startingFrom, pageSize, methodName);

        this.properties           = properties;
        this.matchCriteria        = matchCriteria;
        this.searchCriteria       = null;
    }


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the relationship to follow
     * @param searchCriteria value used in the search
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositorySelectedEntitiesIterator(RepositoryHandler  repositoryHandler,
                                              String             userId,
                                              String             entityTypeGUID,
                                              String             searchCriteria,
                                              int                startingFrom,
                                              int                pageSize,
                                              String             methodName)
    {
        super(repositoryHandler, userId, entityTypeGUID, null, startingFrom, pageSize, methodName);

        this.searchCriteria       = searchCriteria;
        this.properties           = null;
        this.matchCriteria        = null;
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
            if (searchCriteria != null)
            {
                entitiesCache = repositoryHandler.getEntitiesByValue(userId,
                                                                     searchCriteria,
                                                                     entityTypeGUID,
                                                                     startingFrom,
                                                                     pageSize,
                                                                     methodName);
            }
            else if (matchCriteria == MatchCriteria.ANY)
            {
                entitiesCache = repositoryHandler.getEntitiesByName(userId,
                                                                    properties,
                                                                    entityTypeGUID,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    methodName);
            }
            else if (matchCriteria == MatchCriteria.ALL)
            {
                entitiesCache = repositoryHandler.getEntitiesByAllProperties(userId,
                                                                             properties,
                                                                             entityTypeGUID,
                                                                             startingFrom,
                                                                             pageSize,
                                                                             methodName);
            }
            else /* (matchCriteria == MatchCriteria.NONE) */
            {
                entitiesCache = repositoryHandler.getEntitiesWithoutPropertyValues(userId,
                                                                                   properties,
                                                                                   entityTypeGUID,
                                                                                   startingFrom,
                                                                                   pageSize,
                                                                                   methodName);
            }

            if (entitiesCache != null)
            {
                startingFrom = startingFrom + entitiesCache.size();
            }
        }

        return entitiesCache != null;
    }
}
