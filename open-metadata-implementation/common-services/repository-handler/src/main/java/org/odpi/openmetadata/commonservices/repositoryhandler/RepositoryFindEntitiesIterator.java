/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * RepositoryFindEntitiesIterator is an iterator class for iteratively retrieving entities based on a search criteria.
 * It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryFindEntitiesIterator extends RepositoryIteratorForEntities
{
    private List<String>          entitySubtypeGUIDs;
    private SearchProperties      searchProperties;
    private List<InstanceStatus>  limitResultsByStatus;
    private SearchClassifications searchClassifications;
    private Date                  asOfTime;
    private String                sequencingProperty;
    private SequencingOrder       sequencingOrder;

    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param entityTypeGUID String unique identifier for the entity type of interest (null means any entity type).
     * @param entitySubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the entityTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of entity property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param searchClassifications Optional list of entity classifications to match.
     * @param asOfTime Requests a historical query of the entity.  Null means return the present values.
     * @param sequencingProperty String name of the entity property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param requesterPageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryFindEntitiesIterator(RepositoryHandler       repositoryHandler,
                                          InvalidParameterHandler invalidParameterHandler,
                                          String                  userId,
                                          String                  entityTypeGUID,
                                          List<String>            entitySubtypeGUIDs,
                                          SearchProperties        searchProperties,
                                          List<InstanceStatus>    limitResultsByStatus,
                                          SearchClassifications   searchClassifications,
                                          Date                    asOfTime,
                                          String                  sequencingProperty,
                                          SequencingOrder         sequencingOrder,
                                          boolean                 forLineage,
                                          boolean                 forDuplicateProcessing,
                                          int                     startingFrom,
                                          int                     requesterPageSize,
                                          Date                    effectiveTime,
                                          String                  methodName) throws InvalidParameterException
    {
        super(repositoryHandler,
              invalidParameterHandler,
              userId,
              entityTypeGUID,
              null,
              sequencingProperty,
              forLineage,
              forDuplicateProcessing,
              startingFrom,
              requesterPageSize,
              effectiveTime,
              methodName);

        this.entitySubtypeGUIDs    = entitySubtypeGUIDs;
        this.searchProperties      = searchProperties;
        this.limitResultsByStatus  = limitResultsByStatus;
        this.searchClassifications = searchClassifications;
        this.asOfTime              = asOfTime;
        this.sequencingProperty    = sequencingProperty;
        this.sequencingOrder       = sequencingOrder;
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
                entitiesCache = repositoryHandler.findEntities(userId,
                                                               entityTypeGUID,
                                                               entitySubtypeGUIDs,
                                                               searchProperties,
                                                               limitResultsByStatus,
                                                               searchClassifications,
                                                               asOfTime,
                                                               sequencingProperty,
                                                               sequencingOrder,
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
