/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;


/**
 * RepositoryRelatedEntitiesIterator is an iterator class for iteratively retrieving relationships for a starting entity (possibly restricting
 * the type of relationships returned) and returning the entity at the other end.  It is used where the caller needs to filter the results coming
 * from the repository and may need to make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelatedEntitiesIterator extends RepositoryIteratorForEntities
{
    private final String             startingEntityGUID;
    private final String             startingEntityTypeName;
    private final String             relationshipTypeGUID;
    private final String             relationshipTypeName;
    private final int                selectionEnd;

    private static final Logger log = LoggerFactory.getLogger(RepositoryRelatedEntitiesIterator.class);


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException when page size or start from parameters do not meet criteria
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler       repositoryHandler,
                                             InvalidParameterHandler invalidParameterHandler,
                                             String                  userId,
                                             String                  startingEntityGUID,
                                             String                  startingEntityTypeName,
                                             String                  relationshipTypeGUID,
                                             String                  relationshipTypeName,
                                             String                  sequencingPropertyName,
                                             boolean                 forLineage,
                                             boolean                 forDuplicateProcessing,
                                             int                     startingFrom,
                                             int                     pageSize,
                                             Date                    effectiveTime,
                                             String                  methodName) throws InvalidParameterException
    {
        this(repositoryHandler,
             invalidParameterHandler,
             userId,
             startingEntityGUID,
             startingEntityTypeName,
             relationshipTypeGUID,
             relationshipTypeName,
             sequencingPropertyName,
             forLineage,
             forDuplicateProcessing,
             startingFrom,
             pageSize,
             0,
             effectiveTime,
             methodName);
    }


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param invalidParameterHandler invalid parameter handler
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param forLineage the query is to support lineage retrieval
     * @param forDuplicateProcessing the query is for duplicate processing and so must not deduplicate
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     * @throws InvalidParameterException the bean properties are invalid
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler       repositoryHandler,
                                             InvalidParameterHandler invalidParameterHandler,
                                             String                  userId,
                                             String                  startingEntityGUID,
                                             String                  startingEntityTypeName,
                                             String                  relationshipTypeGUID,
                                             String                  relationshipTypeName,
                                             String                  sequencingPropertyName,
                                             boolean                 forLineage,
                                             boolean                 forDuplicateProcessing,
                                             int                     startingFrom,
                                             int                     pageSize,
                                             int                     selectionEnd,
                                             Date                    effectiveTime,
                                             String                  methodName) throws InvalidParameterException
    {
        super(repositoryHandler,
              invalidParameterHandler,
              userId,
              null,
              null,
              sequencingPropertyName,
              forLineage,
              forDuplicateProcessing,
              startingFrom,
              pageSize,
              effectiveTime,
              methodName);

        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeName   = relationshipTypeName;
        this.selectionEnd           = selectionEnd;

        if (log.isDebugEnabled())
        {
            log.debug("RepositoryRelatedEntitiesIterator: startingFrom=" + startingFrom + ", startingEntityGUID=" + startingEntityGUID);
        }
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
                entitiesCache = repositoryHandler.getEntitiesForRelationshipType(userId,
                                                                                 startingEntityGUID,
                                                                                 startingEntityTypeName,
                                                                                 relationshipTypeGUID,
                                                                                 relationshipTypeName,
                                                                                 null,
                                                                                 selectionEnd,
                                                                                 forLineage,
                                                                                 forDuplicateProcessing,
                                                                                 startingFrom,
                                                                                 pageSize,
                                                                                 effectiveTime,
                                                                                 methodName);

                if (entitiesCache != null)
                {
                    if (log.isDebugEnabled())
                    {
                        log.debug("RepositoryRelatedEntitiesIterator : moreToReceive() entitiesCache not null");

                        for (EntityDetail entity : entitiesCache)
                        {
                            String displayName   = "";
                            String qualifiedName = "";

                            if (entity.getProperties() != null && entity.getProperties().getInstanceProperties() != null)
                            {
                                if (entity.getProperties().getInstanceProperties().get("displayName") != null)
                                {
                                    displayName = entity.getProperties().getInstanceProperties().get("displayName").toString();
                                }
                                else if (entity.getProperties().getInstanceProperties().get("name") != null)
                                {
                                    displayName = entity.getProperties().getInstanceProperties().get("name").toString();
                                }
                                if (entity.getProperties().getInstanceProperties().get("qualifiedName") != null)
                                {
                                    qualifiedName = entity.getProperties().getInstanceProperties().get("qualifiedName").toString();
                                }
                            }

                            log.debug("Cached entity " + entity.getGUID() + ", displayName=" + displayName + ", qualifiedName=" + qualifiedName);
                        }
                    }
                }

                startingFrom = startingFrom + pageSize;

                if (log.isDebugEnabled())
                {
                    log.debug("StartingFrom=" + startingFrom);
                }
            }
        }

        return (entitiesCache != null);
    }
}
