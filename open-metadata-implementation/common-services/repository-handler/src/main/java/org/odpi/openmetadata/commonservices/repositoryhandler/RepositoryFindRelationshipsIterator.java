/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * RepositoryFindEntitiesIterator is an iterator class for iteratively retrieving relationships based on a search criteria.
 * It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryFindRelationshipsIterator extends RepositoryIterator
{
    private final String               relationshipTypeGUID;
    private final List<String>         relationshipSubtypeGUIDs;
    private final SearchProperties     searchProperties;
    private final List<InstanceStatus> limitResultsByStatus;
    private final Date                 asOfTime;
    private final String               sequencingProperty;
    private final SequencingOrder      sequencingOrder;

    private List<Relationship>   relationshipsCache = null;


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param relationshipTypeGUID String unique identifier for the relationship type of interest (null means any relationship type).
     * @param relationshipSubtypeGUIDs optional list of the unique identifiers (guids) for subtypes of the relationshipTypeGUID to
     *                           include in the search results. Null means all subtypes.
     * @param searchProperties Optional list of relationship property conditions to match.
     * @param limitResultsByStatus By default, entities in all statuses are returned.  However, it is possible
     *                             to specify a list of statuses (eg ACTIVE) to restrict the results to.  Null means all
     *                             status values.
     * @param asOfTime Requests a historical query of the relationship.  Null means return the present values.
     * @param sequencingProperty String name of the relationship property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing       the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime          the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryFindRelationshipsIterator(RepositoryHandler       repositoryHandler,
                                               InvalidParameterHandler invalidParameterHandler,
                                               String                  userId,
                                               String                  relationshipTypeGUID,
                                               List<String>            relationshipSubtypeGUIDs,
                                               SearchProperties        searchProperties,
                                               List<InstanceStatus>    limitResultsByStatus,
                                               Date                    asOfTime,
                                               String                  sequencingProperty,
                                               SequencingOrder         sequencingOrder,
                                               int                     startingFrom,
                                               int                     pageSize,
                                               boolean                 forLineage,
                                               boolean                 forDuplicateProcessing,
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

        this.relationshipTypeGUID     = relationshipTypeGUID;
        this.relationshipSubtypeGUIDs = relationshipSubtypeGUIDs;
        this.searchProperties         = searchProperties;
        this.limitResultsByStatus     = limitResultsByStatus;
        this.asOfTime                 = asOfTime;
        this.sequencingProperty       = sequencingProperty;
        this.sequencingOrder          = sequencingOrder;
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
            relationshipsCache = new ArrayList<>();

            /*
             * The loop is needed to ensure that another retrieve is attempted if the repository handler returns an empty list.
             * This occurs if all elements returned from the repositories do not match the effectiveTime requested.
             */
            while ((relationshipsCache != null) && (relationshipsCache.isEmpty()))
            {
                relationshipsCache = repositoryHandler.findRelationships(userId,
                                                                         relationshipTypeGUID,
                                                                         relationshipSubtypeGUIDs,
                                                                         searchProperties,
                                                                         limitResultsByStatus,
                                                                         asOfTime,
                                                                         sequencingProperty,
                                                                         sequencingOrder,
                                                                         forDuplicateProcessing,
                                                                         startingFrom,
                                                                         pageSize,
                                                                         effectiveTime,
                                                                         methodName);

                startingFrom = startingFrom + pageSize;
            }
        }

        return (relationshipsCache != null);
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
