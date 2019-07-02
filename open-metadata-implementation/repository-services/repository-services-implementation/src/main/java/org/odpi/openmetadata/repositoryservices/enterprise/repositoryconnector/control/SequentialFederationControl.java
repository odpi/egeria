/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors.RepositoryExecutor;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
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
     * @param methodName calling method
     */
    public SequentialFederationControl(String                        userId,
                                       List<OMRSRepositoryConnector> cohortConnectors,
                                       String                        methodName)
    {
        super(userId, cohortConnectors, methodName);
    }


    /**
     * Issue the federated command
     *
     * @param executor command to execute
     * @throws RepositoryErrorException problem with the state of one of the repositories.
     * This is probably a logic error rather than an outage
     */
    public void executeCommand(RepositoryExecutor executor) throws RepositoryErrorException
    {
        if (super.cohortConnectors != null)
        {
            for (OMRSRepositoryConnector cohortConnector : cohortConnectors)
            {
                if (cohortConnector != null)
                {
                    OMRSMetadataCollection metadataCollection = cohortConnector.getMetadataCollection();

                    String metadataCollectionId = this.validateMetadataCollection(metadataCollection, methodName);

                    if (executor.issueRequestToRepository(metadataCollectionId, metadataCollection))
                    {
                        /*
                         * The executor returns true if it has all of the results it needs.
                         * If it returns false it means it needs more info from another repository
                         */
                        return;
                    }
                }
            }
        }
    }


    /**
     * Verify that a cohort member's metadata collection is not null.
     *
     * @param cohortMetadataCollection metadata collection
     * @param methodName name of method
     * @return metadata collection id
     * @throws RepositoryErrorException null metadata collection or null metadata collection id
     */
    private String validateMetadataCollection(OMRSMetadataCollection  cohortMetadataCollection,
                                              String                  methodName) throws RepositoryErrorException
    {
        /*
         * The cohort metadata collection should not be null.  It is in a real mess if this fails.
         */
        if (cohortMetadataCollection == null)
        {
            /*
             * A problem in the set up of the metadata collection list.  Repository connectors implemented
             * with no metadata collection are tested for in the OMRSEnterpriseConnectorManager so something
             * else has gone wrong.
             */
            OMRSErrorCode errorCode    = OMRSErrorCode.NULL_ENTERPRISE_METADATA_COLLECTION;
            String        errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage();

            throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               errorMessage,
                                               errorCode.getSystemAction(),
                                               errorCode.getUserAction());
        }

        return cohortMetadataCollection.getMetadataCollectionId(super.userId);
    }
}
