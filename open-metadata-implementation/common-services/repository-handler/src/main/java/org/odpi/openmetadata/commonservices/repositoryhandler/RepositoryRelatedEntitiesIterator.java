/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.repositoryhandler;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * RepositoryRelatedEntitiesIterator is an iterator class for iteratively retrieving relationships for an starting entity (possibly restricting
 * the type of relationships returned) and returning the entity at the other end.  It is used where the caller needs to filter the results coming
 * from the repository and may need to make more than one call to the repository in order to accumulate the number of requested results.
 *
 * Note this class is intended for a single request's use - it is not thread-safe.
 */
public class RepositoryRelatedEntitiesIterator extends RepositoryIteratorForEntities
{
    private String             startingEntityGUID;
    private String             startingEntityTypeName;
    private String             relationshipTypeGUID;   // only used when there is one relationship passed
    private List<String>       relationshipTypeNames = new ArrayList<>();
    private List<String>       relatedEntityTypeNames = new ArrayList<>();
    private List<InstanceStatus>  limitResultsByStatus = new ArrayList<>();
    private List<String>       limitResultsByClassification = new ArrayList<>();
    private int                selectionEnd = 0;
    OMRSRepositoryHelper omrsRepositoryHelper;

    private static final Logger log = LoggerFactory.getLogger(RepositoryRelatedEntitiesIterator.class);


    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             String            userId,
                                             String            startingEntityGUID,
                                             String            startingEntityTypeName,
                                             String            relationshipTypeGUID,
                                             String            relationshipTypeName,
                                             String            sequencingPropertyName,
                                             int               startingFrom,
                                             int               pageSize,
                                             Date              effectiveTime,
                                             String            methodName)
    {
        super(repositoryHandler,
              userId,
              null,
              null,
              sequencingPropertyName,
              startingFrom,
              pageSize,
              effectiveTime,
              methodName);

        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.relationshipTypeGUID   = relationshipTypeGUID;
        this.relationshipTypeNames.add(relationshipTypeName);

        if (log.isDebugEnabled())
        {
            log.debug("RepositoryRelatedEntitiesIterator :startingFrom=" + startingFrom + ",startingEntityGUID=" + startingEntityGUID);
        }
    }
    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeNames  type names for the relationship to follow
     * @param relatedEntityTypeNames entity type names to filter by
     * @param limitResultsByStatus   limit results by status
     * @param limitResultsByClassification limit results by  classification
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             OMRSRepositoryHelper omrsRepositoryHelper,
                                             String            userId,
                                             String            startingEntityGUID,
                                             String            startingEntityTypeName,
                                             List<String>      relationshipTypeNames,
                                             List<String>      relatedEntityTypeNames,
                                             List<InstanceStatus>  limitResultsByStatus,
                                             List<String>      limitResultsByClassification,
                                             String            sequencingPropertyName,
                                             int               startingFrom,
                                             int               pageSize,
                                             Date              effectiveTime,
                                             String            methodName) {
        super(repositoryHandler,
              userId,
              null,
              null,
              sequencingPropertyName,
              startingFrom,
              pageSize,
              effectiveTime,
              methodName);

        this.startingEntityGUID     = startingEntityGUID;
        this.startingEntityTypeName = startingEntityTypeName;
        this.omrsRepositoryHelper = omrsRepositoryHelper;

        this.relationshipTypeNames = relationshipTypeNames;
        this.relatedEntityTypeNames = relatedEntityTypeNames;
        this.limitResultsByStatus = limitResultsByStatus;
        this.limitResultsByClassification = limitResultsByClassification;

    }
    /**
     * Constructor takes the parameters used to call the repository handler.
     *
     * @param repositoryHandler interface to the open metadata repositories.
     * @param userId  user making the request
     * @param startingEntityGUID  starting entity's GUID
     * @param startingEntityTypeName  starting entity's type name
     * @param relationshipTypeGUID  identifier for the relationship to follow
     * @param relationshipTypeName  type name for the relationship to follow
     * @param sequencingPropertyName name of property used to sequence the results - null means no sequencing
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param selectionEnd 0 means either end, 1 means only take from end 1, 2 means only take from end 2
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName  name of calling method
     */
    public RepositoryRelatedEntitiesIterator(RepositoryHandler repositoryHandler,
                                             String            userId,
                                             String            startingEntityGUID,
                                             String            startingEntityTypeName,
                                             String            relationshipTypeGUID,
                                             String            relationshipTypeName,
                                             String            sequencingPropertyName,
                                             int               startingFrom,
                                             int               pageSize,
                                             int               selectionEnd,
                                             Date              effectiveTime,
                                             String            methodName)
    {
        this(repositoryHandler,
             userId,
             startingEntityGUID,
             startingEntityTypeName,
             relationshipTypeGUID,
             relationshipTypeName,
             sequencingPropertyName,
             startingFrom,
             pageSize,
             effectiveTime,
             methodName);

        this.selectionEnd =  selectionEnd;
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
            entitiesCache = new ArrayList<>();

            /*
             * The loop is needed to ensure that another retrieve is attempted if the repository handler returns an empty list.
             * This occurs if all elements returned from the repositories do not match the effectiveTime requested.
             */
            while ((entitiesCache != null) && (entitiesCache.isEmpty()))
            {
                if (selectionEnd == 0) {
                    // TODO check whether the relationships type contain duplicates?
                    for (String relationshipTypeName : relationshipTypeNames) {
                        String relationshipTypeGUID = omrsRepositoryHelper.getTypeDefByName(methodName, relationshipTypeName).getGUID();
                        List<Relationship> relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                                    startingEntityGUID,
                                                                                                    startingEntityTypeName,
                                                                                                    relationshipTypeGUID,
                                                                                                    relationshipTypeName,
                                                                                                    0,
                                                                                                    pageSize,
                                                                                                    effectiveTime,
                                                                                                    methodName);
                        for (Relationship relationship : relationships) {
                            EntityProxy entityProxy = relationship.getEntityOneProxy();
                            if (relationship.getEntityOneProxy().getGUID().equals(startingEntityGUID)) {
                                entityProxy = relationship.getEntityTwoProxy();
                            }
                            boolean statusOK = true;
                            if (limitResultsByStatus != null) {
                                statusOK = false;
                                for (InstanceStatus instanceStatus : limitResultsByStatus) {
                                    if (entityProxy.getStatus().equals(instanceStatus)) {
                                        statusOK = true;
                                    }
                                }
                            }
                            if (statusOK) {
                                boolean entityTypeOK = true;
                                // check entity type
                                if (relatedEntityTypeNames.size() > 0) {
                                    entityTypeOK = false;
                                    for (String relatedEntityTypeName : relatedEntityTypeNames) {
                                        if (omrsRepositoryHelper.isTypeOf(methodName, entityProxy.getType().getTypeDefName(), relatedEntityTypeName)) {
                                            entityTypeOK = true;
                                        }
                                    }
                                }
                                if (entityTypeOK) {
                                    // check classifications
                                    boolean classificationsOK = true;


                                    if (classificationsOK) {
                                        EntityDetail entityDetail = repositoryHandler.getEntityByGUID(userId, entityProxy.getGUID(), "guid", entityProxy.getType().getTypeDefName(), effectiveTime, methodName);
                                        entitiesCache.add(entityDetail);
                                    }
                                }
                            }
                        }
                    }
                }
                else
                {
                    // because of the constructor there can be only on relationship type
                    String  relationshipTypeName = relationshipTypeNames.get(0);

                    boolean startAtEnd1 = false;

                    if (selectionEnd == 2)
                    {
                        startAtEnd1 = true;
                    }

                    entitiesCache = repositoryHandler.getRelatedEntityProxies(userId,
                                                                                    startingEntityGUID,
                                                                                    startingEntityTypeName,
                                                                                    startAtEnd1,
                                                                                    relationshipTypeGUID,
                                                                                    relationshipTypeName,
                                                                                    startingFrom,
                                                                                    pageSize,
                                                                                    effectiveTime,
                                                                                    methodName);


                }

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

                            log.debug("Cached entity " + entity.getGUID() + ",displayName=" + displayName + ",qualifiedName=" + qualifiedName);
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
