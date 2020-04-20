/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.List;

/**
 * RepositorySelectedEntitiesIterator is an iterator class for iteratively retrieving entities based on a search criteria.
 * It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositorySelectedEntitiesIterator
{
    private RepositoryHandler  repositoryHandler;
    private String             userId;
    private String             entityTypeGUID;
    private InstanceProperties properties;
    private MatchCriteria      matchCriteria;
    private int                startingFrom;
    private int                pageSize;
    private String             methodName;
    private List<EntityDetail> entitiesCache = null;


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
        this.repositoryHandler    = repositoryHandler;
        this.userId               = userId;
        this.entityTypeGUID       = entityTypeGUID;
        this.properties           = properties;
        this.matchCriteria        = matchCriteria;
        this.startingFrom         = startingFrom;
        this.pageSize             = pageSize;
        this.methodName           = methodName;
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
        if ((entitiesCache == null) || (entitiesCache.isEmpty()))
        {
            if (matchCriteria == MatchCriteria.ANY)
            {
                entitiesCache = repositoryHandler.getEntitiesByName(userId,
                                                                    properties,
                                                                    entityTypeGUID,
                                                                    startingFrom,
                                                                    pageSize,
                                                                    methodName);
            }
            else
            {
                entitiesCache = repositoryHandler.getEntitiesByAllProperties(userId,
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


    /**
     * Return the next entity.  It returns null if nothing left to retrieve.
     *
     * @return relationship or null
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    public EntityDetail  getNext() throws UserNotAuthorizedException,
                                          PropertyServerException
    {
        if (moreToReceive())
        {
            return entitiesCache.remove(0);
        }
        else
        {
            return null;
        }
    }
}
