/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors.RepositoryExecutor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.List;

/**
 * SequentialFederationControl provides a simple sequential federation control loop to
 * manage a single federated query.
 */
public class SequentialFederationControl extends FederationControlBase
{
    /**
     * Constructor for a sequential federated query controller
     *
     * @param userId calling user
     * @param cohortConnectors list of connectors to call
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public SequentialFederationControl(String                        userId,
                                       List<OMRSRepositoryConnector> cohortConnectors,
                                       AuditLog                      auditLog,
                                       String                        methodName)
    {
        super(userId, cohortConnectors, auditLog, methodName);
    }


    /**
     * Issue the federated command.
     *
     * @param executor command to execute
     * @throws RepositoryErrorException problem with the state of one of the repositories.
     * This is probably a logic error rather than an outage
     */
    public void executeCommand(RepositoryExecutor executor) throws RepositoryErrorException
    {
        if (super.cohortConnectors != null)
        {
            /*
             * This is the first sweep of the repositories - used to gather the results.
             */
            for (OMRSRepositoryConnector cohortConnector : cohortConnectors)
            {
                if (cohortConnector != null)
                {
                    OMRSMetadataCollection metadataCollection = cohortConnector.getMetadataCollection();

                    String metadataCollectionId = this.validateMetadataCollection(cohortConnector, metadataCollection, methodName);

                    if (metadataCollectionId != null)
                    {
                        if (executor.issueRequestToRepository(metadataCollectionId, metadataCollection))
                        {
                            /*
                             * The executor returns true if it has all the results it needs.
                             * If it returns false it means it needs more info from another repository.
                             */
                            break;
                        }
                    }
                }
            }

            /*
             * All repositories have been called.
             * The executor may choose to augment each result element by making another sweep of the repositories.
             */
            List<String> resultGUIDs = executor.getResultsForAugmentation();

            if (resultGUIDs != null)
            {
                for (String resultGUID : resultGUIDs)
                {
                    for (OMRSRepositoryConnector cohortConnector : cohortConnectors)
                    {
                        if (cohortConnector != null)
                        {
                            OMRSMetadataCollection metadataCollection = cohortConnector.getMetadataCollection();

                            String metadataCollectionId = this.validateMetadataCollection(cohortConnector, metadataCollection, methodName);

                            if (metadataCollectionId != null)
                            {
                                executor.augmentResultFromRepository(resultGUID, metadataCollectionId, metadataCollection);
                            }
                        }
                    }
                }
            }
        }
    }
}
