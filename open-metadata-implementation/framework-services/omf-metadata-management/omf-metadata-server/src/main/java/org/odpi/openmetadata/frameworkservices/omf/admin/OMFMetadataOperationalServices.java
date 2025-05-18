/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.admin;

import org.odpi.openmetadata.frameworkservices.omf.ffdc.OpenMetadataStoreAuditCode;
import org.odpi.openmetadata.frameworkservices.omf.server.OMFMetadataManagementInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworkservices.omf.server.OMFMetadataManagementInstanceHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * OMFMetadataOperationalServices initializes the REST Services that support the Governance Action Framework (omf)
 * open metadata store calls.
 */
public class OMFMetadataOperationalServices
{
    private final String   serverName;
    private final AuditLog auditLog;


    /**
     * Constructor
     *
     * @param serverName this server
     * @param repositoryConnector connector to repository
     * @param auditLog logging destination
     * @param localServerUserId userId for server initiated requests
     * @param maxPageSize max number of results to return on single request.
     * @throws NewInstanceException unable to initialize
     */
    public OMFMetadataOperationalServices(String                   serverName,
                                          OMRSRepositoryConnector  repositoryConnector,
                                          AuditLog                 auditLog,
                                          String                   localServerUserId,
                                          int                      maxPageSize) throws NewInstanceException
    {
        this.serverName = serverName;
        this.auditLog = auditLog;

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, OpenMetadataStoreAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            auditLog.logMessage(actionDescription, OpenMetadataStoreAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));

            new OMFMetadataManagementInstance(repositoryConnector, auditLog, localServerUserId, maxPageSize);
        }
        catch (NewInstanceException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OpenMetadataStoreAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        this.auditLog.logMessage(actionDescription, OpenMetadataStoreAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));

        new OMFMetadataManagementInstanceHandler().removeServerServiceInstance(serverName);
    }
}
