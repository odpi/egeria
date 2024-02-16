/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * RepositoryEntitiesIterator is an iterator class for iteratively retrieving entities (possibly restricting
 * the type of entities returned).  It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryEntitiesIterator extends RepositoryIteratorForEntities
{
    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the relationship to follow
     * @param entityTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param limitResultsByStatus only return elements that have the requested status (null means all statuses
     * @param limitResultsByClassification only return elements that have the requested classification(s)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryEntitiesIterator(RepositoryHandler       repositoryHandler,
                                      InvalidParameterHandler invalidParameterHandler,
                                      String                  userId,
                                      String                  entityTypeGUID,
                                      String                  entityTypeName,
                                      String                  sequencingPropertyName,
                                      List<InstanceStatus>    limitResultsByStatus,
                                      List<String>            limitResultsByClassification,
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
              entityTypeGUID,
              entityTypeName,
              sequencingPropertyName,
              limitResultsByStatus,
              limitResultsByClassification,
              forLineage,
              forDuplicateProcessing,
              startingFrom,
              pageSize,
              effectiveTime,
              methodName);
    }


    /**
     * Determine if there is more to receive.  It will populate the iterator's cache with more content.
     *
     * @return boolean flag
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException the repository is not allowing the user to access the metadata
     * @throws PropertyServerException there is a problem in the repository
     */
    @Override
    public boolean  moreToReceive() throws InvalidParameterException,
                                           UserNotAuthorizedException,
                                           PropertyServerException
    {
        if ((entitiesCache == null) || (entitiesCache.isEmpty()))
        {
            entitiesCache = new ArrayList<>();

            /*
             * The loop is needed to ensure that another retrieve is attempted if the repository handler returns an empty list.
             * This occurs if all elements returned from the repositories do not match the effectiveTime requested.
             */
            while ((entitiesCache != null) && (entitiesCache.isEmpty()))
            {
                entitiesCache = repositoryHandler.getEntitiesForType(userId,
                                                                     entityTypeGUID,
                                                                     entityTypeName,
                                                                     limitResultsByStatus,
                                                                     limitResultsByClassification,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     startingFrom,
                                                                     pageSize,
                                                                     effectiveTime,
                                                                     methodName);

                startingFrom = startingFrom + pageSize;
            }
        }

        return (entitiesCache != null);
    }
}
