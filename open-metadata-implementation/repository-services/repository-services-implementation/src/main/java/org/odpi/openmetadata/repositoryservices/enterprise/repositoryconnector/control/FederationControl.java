/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control;


import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors.RepositoryExecutor;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;


/**
 * FederationControl defines the API for control logic that manages a federated query
 * across the repositories registered with the enterprise connector manager.
 *
 * An instance of the federation control class is created for each query.  It is passed an
 * executor object that is able to issue the appropriate query and handle the response.
 *
 * The federation control class calls the executor repeatedly, passing in the metadata collection
 * until the executor tells it to stop, or it runs out of registered repositories.
 *
 * The caller (enterprise connector) then requests the response from the executor.
 */
public interface FederationControl
{
    /**
     * Issue the federated command
     *
     * @param executor command to execute
     * @throws RepositoryErrorException problem with the state of one of the repositories.
     * This is probably a logic error rather than an outage
     */
    void executeCommand(RepositoryExecutor   executor) throws RepositoryErrorException;
}
