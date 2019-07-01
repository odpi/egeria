/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors.RepositoryExecutor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.List;

/**
 * ParallelFederationControl uses multiple worker threads to perform the calls to different systems in parallel.
 */
public class ParallelFederationControl extends FederationControlBase
{
    // todo at this point the parallel federation control uses the sequential federation control since the
    // todo worker threads are not implemented.

    private SequentialFederationControl sequentialFederationControl;

    /**
     * Constructor for a federated query
     *
     * @param userId calling user
     * @param cohortConnectors list of connectors to call
     * @param methodName calling method
     */
    public ParallelFederationControl(String                        userId,
                                     List<OMRSRepositoryConnector> cohortConnectors,
                                     String                        methodName)
    {
        super(userId, cohortConnectors, methodName);

        sequentialFederationControl = new SequentialFederationControl(userId, cohortConnectors, methodName);
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
        sequentialFederationControl.executeCommand(executor);
    }
}
