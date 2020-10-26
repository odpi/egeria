/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;

/**
 * RepositoryIteratorForEntities is the shared interface of all repository helper iterators that retrieve entity details from
 * the repository.
 */
public abstract class RepositoryIteratorForEntities
{
    protected RepositoryHandler  repositoryHandler;
    protected String             userId;
    protected String             entityTypeGUID;
    protected String             entityTypeName;
    protected int                startingFrom;
    protected int                pageSize;
    protected String             methodName;
    protected List<EntityDetail> entitiesCache = null;


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the type of entity to retrieve
     * @param entityTypeName  name for the type of entity to retrieve
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositoryIteratorForEntities(RepositoryHandler  repositoryHandler,
                                         String             userId,
                                         String             entityTypeGUID,
                                         String             entityTypeName,
                                         int                startingFrom,
                                         int                pageSize,
                                         String             methodName)
    {
        this.repositoryHandler    = repositoryHandler;
        this.userId               = userId;
        this.entityTypeGUID       = entityTypeGUID;
        this.entityTypeName       = entityTypeName;
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
    public abstract boolean  moreToReceive() throws UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Return the next entity.  It returns null if nothing left to retrieve.
     *
     * @return relationship or null
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    public EntityDetail getNext() throws UserNotAuthorizedException,
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
