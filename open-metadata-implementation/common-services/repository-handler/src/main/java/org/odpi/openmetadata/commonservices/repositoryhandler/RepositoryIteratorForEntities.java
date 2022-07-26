/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.Date;
import java.util.List;

/**
 * RepositoryIteratorForEntities is the shared interface of all repository helper iterators that retrieve entity details from
 * the repository.
 */
public abstract class RepositoryIteratorForEntities extends RepositoryIterator
{
    protected String             entityTypeGUID;
    protected String             entityTypeName;
    protected String             sequencingPropertyName;

    protected List<EntityDetail> entitiesCache = null;


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the type of entity to retrieve
     * @param entityTypeName  name for the type of entity to retrieve
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryIteratorForEntities(RepositoryHandler       repositoryHandler,
                                         InvalidParameterHandler invalidParameterHandler,
                                         String                  userId,
                                         String                  entityTypeGUID,
                                         String                  entityTypeName,
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
              forLineage,
              forDuplicateProcessing,
              effectiveTime,
              methodName);

        this.entityTypeGUID          = entityTypeGUID;
        this.entityTypeName          = entityTypeName;
        this.sequencingPropertyName  = sequencingPropertyName;
    }


    /**
     * Determine if there is more to receive.  It will populate the iterator's cache with more content.
     *
     * @return boolean flag
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    public abstract boolean  moreToReceive() throws InvalidParameterException,
                                                    UserNotAuthorizedException,
                                                    PropertyServerException;


    /**
     * Return the next entity.  It returns null if nothing left to retrieve.
     *
     * @return relationship or null
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    public EntityDetail getNext() throws InvalidParameterException,
                                         UserNotAuthorizedException,
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
