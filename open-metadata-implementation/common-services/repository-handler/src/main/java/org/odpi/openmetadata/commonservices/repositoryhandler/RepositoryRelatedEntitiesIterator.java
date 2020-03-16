/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;

/**
 * RepositoryRelationshipsIterator is an iterator class for iteratively retrieving relationships for an anchor entity (possibly restricting
 * the type of relationships returned).  It is used where the caller needs to filter the results coming from the repository and may need to
 * make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelatedEntitiesIterator
{
    private RepositoryHandler  repositoryHandler;
    private String             userId;
    private String             anchorEntityGUID;
    private String             anchorEntityTypeName;
    private String             relationshipTypeGUID;
    private String             relationshipTypeName;
    private int                startingFrom;
    private int                pageSize;
    private String             methodName;
    private List<EntityDetail> entitiesCache = null;


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param anchorEntityGUID  starting entity's GUID
     * @param anchorEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             String            userId,
                                             String            anchorEntityGUID,
                                             String            anchorEntityTypeName,
                                             String            relationshipTypeGUID,
                                             String            relationshipTypeName,
                                             int               startingFrom,
                                             int               pageSize,
                                             String            methodName)
    {
        this.repositoryHandler    = repositoryHandler;
        this.userId               = userId;
        this.anchorEntityGUID     = anchorEntityGUID;
        this.anchorEntityTypeName = anchorEntityTypeName;
        this.relationshipTypeGUID = relationshipTypeGUID;
        this.relationshipTypeName = relationshipTypeName;
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
            entitiesCache = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                             anchorEntityGUID,
                                                                             anchorEntityTypeName,
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


    /**
     * Return the next relationship.  It returns null if nothing left to retrieve.
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
