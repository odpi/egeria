/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;

import java.util.ArrayList;
import java.util.Date;


/**
 * RepositorySelectedEntitiesIterator is an iterator class for iteratively retrieving entities based on a search criteria.
 * It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositorySelectedEntitiesIterator extends RepositoryIteratorForEntities
{
    private final InstanceProperties properties;
    private final MatchCriteria      matchCriteria;
    private final String             searchCriteria;

    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler error handler set up with max page size
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the relationship to follow
     * @param properties properties used in the search
     * @param matchCriteria all or any
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException bad parameter - probably page size
     */
    public RepositorySelectedEntitiesIterator(RepositoryHandler       repositoryHandler,
                                              InvalidParameterHandler invalidParameterHandler,
                                              String                  userId,
                                              String                  entityTypeGUID,
                                              InstanceProperties      properties,
                                              MatchCriteria           matchCriteria,
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
              entityTypeGUID,
              null,
              sequencingPropertyName,
              forLineage,
              forDuplicateProcessing,
              startingFrom,
              pageSize,
              effectiveTime,
              methodName);

        this.properties           = properties;
        this.matchCriteria        = matchCriteria;
        this.searchCriteria       = null;
    }


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler error handler set up with max page size
     * @param userId  user making the request
     * @param entityTypeGUID  identifier for the relationship to follow
     * @param searchCriteria value used in the search
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     * @throws InvalidParameterException bad parameter - probably page size
     */
    public RepositorySelectedEntitiesIterator(RepositoryHandler       repositoryHandler,
                                              InvalidParameterHandler invalidParameterHandler,
                                              String                   userId,
                                              String                   entityTypeGUID,
                                              String                   searchCriteria,
                                              String                   sequencingPropertyName,
                                              boolean                  forLineage,
                                              boolean                  forDuplicateProcessing,
                                              int                      startingFrom,
                                              int                      pageSize,
                                              Date                     effectiveTime,
                                              String                   methodName) throws InvalidParameterException
    {
        super(repositoryHandler,
              invalidParameterHandler,
              userId,
              entityTypeGUID,
              null,
              sequencingPropertyName,
              forLineage,
              forDuplicateProcessing,
              startingFrom,
              pageSize,
              effectiveTime,
              methodName);

        this.searchCriteria       = searchCriteria;
        this.properties           = null;
        this.matchCriteria        = null;
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
                if (searchCriteria != null)
                {
                    entitiesCache = repositoryHandler.getEntitiesByValue(userId,
                                                                         searchCriteria,
                                                                         entityTypeGUID,
                                                                         sequencingPropertyName,
                                                                         forLineage,
                                                                         forDuplicateProcessing,
                                                                         startingFrom,
                                                                         pageSize,
                                                                         effectiveTime,
                                                                         methodName);
                }
                else if (matchCriteria == MatchCriteria.ANY)
                {
                    entitiesCache = repositoryHandler.getEntitiesByName(userId,
                                                                        properties,
                                                                        entityTypeGUID,
                                                                        sequencingPropertyName,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        startingFrom,
                                                                        pageSize,
                                                                        effectiveTime,
                                                                        methodName);
                }
                else if (matchCriteria == MatchCriteria.ALL)
                {
                    entitiesCache = repositoryHandler.getEntitiesByAllProperties(userId,
                                                                                 properties,
                                                                                 entityTypeGUID,
                                                                                 sequencingPropertyName,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startingFrom,
                                                                                 pageSize,
                                                                                 effectiveTime,
                                                                                 methodName);
                }
                else /* (matchCriteria == MatchCriteria.NONE) */
                {
                    entitiesCache = repositoryHandler.getEntitiesWithoutPropertyValues(userId,
                                                                                       properties,
                                                                                       entityTypeGUID,
                                                                                       sequencingPropertyName,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       startingFrom,
                                                                                       pageSize,
                                                                                       effectiveTime,
                                                                                       methodName);
                }

                startingFrom = startingFrom + pageSize;
            }
        }

        return (entitiesCache != null);
    }
}
