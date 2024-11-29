/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.EnterpriseOMRSRepositoryConnector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RelationshipHistoryAccumulator accumulates and validates historical versions of relationships received from a collection of open metadata
 * repositories.  It removes duplicates from the list but preserves one instance of each version.
 * This class may be called simultaneously from different threads, so it must be thread-safe.
 */
public class RelationshipHistoryAccumulator extends RelationshipsAccumulator
{
    /*
     * Map of version to relationships retrieved from the repositories
     */
    private final Map<Long, Relationship>   accumulatedRelationships       = new HashMap<>();
    private final Map<String, List<String>> accumulatedRelationshipSources = new HashMap<>();

    private String relationshipGUID = null;


    /**
     * Construct a relationship accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    public RelationshipHistoryAccumulator(String                  localMetadataCollectionId,
                                          AuditLog                auditLog,
                                          OMRSRepositoryValidator repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }


    /**
     * Provide a relationship received from an open metadata repository.
     *
     * @param incomingRelationship relationship returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    private  void addRelationship(Relationship incomingRelationship,
                                  String       metadataCollectionId)
    {
        if ((incomingRelationship != null) && (incomingRelationship.getGUID() != null) && (metadataCollectionId != null))
        {
            relationshipGUID = incomingRelationship.getGUID();

            /*
             * Remember that this metadata collection has already returned this entity.
             */
            List<String> contributingMetadataCollections = accumulatedRelationshipSources.get(relationshipGUID);

            if (contributingMetadataCollections == null)
            {
                contributingMetadataCollections = new ArrayList<>();
            }

            contributingMetadataCollections.add(metadataCollectionId);

            accumulatedRelationshipSources.put(relationshipGUID, contributingMetadataCollections);

            accumulatedRelationships.put(incomingRelationship.getVersion(), incomingRelationship);

            if (metadataCollectionId.equals(localMetadataCollectionId))
            {
                super.captureLocalInstance(relationshipGUID);
            }
        }
    }


    /**
     * Add a list of relationships to the accumulator. This method is included to save the executors from coding this
     * loop to process each entity.
     *
     * @param relationships list of retrieved relationships
     * @param metadataCollectionId source metadata collection
     */
    @Override
    public synchronized void addRelationships(List<Relationship>   relationships,
                                              String               metadataCollectionId)
    {
        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                relationshipGUID = relationship.getGUID();

                this.addRelationship(relationship, metadataCollectionId);
            }

            /*
             * Record that this repository has returned results from the request.
             */
            super.setResultsReturned(metadataCollectionId, relationships.size());
        }
        else
        {
            /*
             * Even though results were not found it was still a successful request.
             */
            super.setResultsReturned(metadataCollectionId, 0);
        }
    }


    /**
     * Extract the results - this will be a unique list of entities selected from the instances
     * supplied to this accumulator.  It should be called once all the executors have completed processing
     * their request(s).
     *
     * @param repositoryConnector enterprise connector
     * @return list of entities
     */
    @Override
    public synchronized List<Relationship> getResults(EnterpriseOMRSRepositoryConnector repositoryConnector)
    {
        if ((accumulatedRelationships == null) || (accumulatedRelationships.isEmpty()))
        {
            return null;
        }
        else
        {
            this.makeRefreshRecommendations(repositoryConnector);

            List<Relationship>  results = new ArrayList<>();

            for (Relationship accumulatedRelationship : accumulatedRelationships.values())
            {
                if (accumulatedRelationship != null)
                {
                    Relationship resultRelationship = new Relationship(accumulatedRelationship);

                    results.add(resultRelationship);
                }
            }

            if (results.isEmpty())
            {
                return null;
            }
            else
            {
                return results;
            }
        }
    }


    /**
     * Return the list of entities that where retrieved from other repositories and not stored in the local repository.
     * The local repository may use this list to send out refresh requests on the OMRS Topic.  If the rules allow, and
     * the remote repository response with a refresh response, this entity could be replicated into the local
     * repository.
     * The value of this processing is that the entity is of interest to the local users so having a local copy could
     * reduce the access time for the entity.
     * This call should be made once all processing has stopped.
     *
     * @param repositoryConnector enterprise connector
     */
    private void makeRefreshRecommendations(EnterpriseOMRSRepositoryConnector repositoryConnector)
    {
        /*
         * Either no local repository or nothing accumulated so nothing to return
         */
        if ((localMetadataCollectionId == null) || (accumulatedRelationships.isEmpty()))
        {
            return;
        }

        for (Relationship accumulatedRelationship : accumulatedRelationships.values())
        {
            if (accumulatedRelationship != null)
            {
                String  relationshipGUID = accumulatedRelationship.getGUID();

                if (relationshipGUID != null)
                {
                    if (super.notLocal(relationshipGUID))
                    {
                        repositoryConnector.requestRefreshOfRelationship(accumulatedRelationship);
                    }
                }
            }
        }
    }
}
