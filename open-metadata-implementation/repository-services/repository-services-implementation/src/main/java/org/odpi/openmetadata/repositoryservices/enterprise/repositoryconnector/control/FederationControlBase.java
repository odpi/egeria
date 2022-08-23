/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.control;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.executors.RepositoryExecutor;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.List;

/**
 * FederationControlBase provides the base class for control logic that manages a federated query
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
public abstract class FederationControlBase implements FederationControl
{
    protected String                            userId;
    protected List<OMRSRepositoryConnector>     cohortConnectors;
    protected AuditLog                          auditLog;
    protected String                            methodName;


    /**
     * Constructor for a federated query
     *
     * @param userId calling user
     * @param cohortConnectors list of connectors to call
     * @param auditLog logging destination
     * @param methodName calling method
     */
    public FederationControlBase(String                        userId,
                                 List<OMRSRepositoryConnector> cohortConnectors,
                                 AuditLog                      auditLog,
                                 String                        methodName)
    {
        this.userId = userId;
        this.cohortConnectors = cohortConnectors;
        this.auditLog = auditLog;
        this.methodName = methodName;
    }


    /**
     * Issue the federated command
     *
     * @param executor command to execute
     * @throws RepositoryErrorException problem with the state of one of the repositories.
     * This is probably a logic error rather than an outage
     */
    public abstract void executeCommand(RepositoryExecutor   executor) throws RepositoryErrorException;


    /**
     * Verify that a cohort member's metadata collection is not null.
     *
     * @param cohortConnector connector to remote repository
     * @param cohortMetadataCollection metadata collection
     * @param methodName name of method
     * @return metadata collection id
     * @throws RepositoryErrorException null metadata collection or null metadata collection id
     */
    String validateMetadataCollection(OMRSRepositoryConnector cohortConnector,
                                      OMRSMetadataCollection  cohortMetadataCollection,
                                      String                  methodName) throws RepositoryErrorException
    {
        /*
         * The cohort metadata collection should not be null.  It is in a real mess if this fails.
         */
        if (cohortMetadataCollection == null)
        {
            /*
             * A problem in the setup of the metadata collection list.  Repository connectors implemented
             * with no metadata collection are tested for in the OMRSEnterpriseConnectorManager so something
             * else has gone wrong.
             */
            throw new RepositoryErrorException(OMRSErrorCode.NULL_ENTERPRISE_METADATA_COLLECTION.getMessageDefinition(),
                                               this.getClass().getName(),
                                               methodName);
        }

        try
        {
            return cohortMetadataCollection.getMetadataCollectionId(userId);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  OMRSAuditCode.SKIPPING_METADATA_COLLECTION.getMessageDefinition(cohortConnector.getRepositoryName(),
                                                                                                  error.getClass().getName(),
                                                                                                  error.getMessage()),
                                  error);
            return null;
        }
    }
}
