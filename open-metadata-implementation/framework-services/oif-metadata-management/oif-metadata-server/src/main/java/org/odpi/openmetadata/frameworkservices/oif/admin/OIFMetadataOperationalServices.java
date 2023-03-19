/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.oif.admin;

import org.odpi.openmetadata.frameworkservices.oif.ffdc.OpenIntegrationAuditCode;
import org.odpi.openmetadata.frameworkservices.oif.server.OpenIntegrationInstance;
import org.odpi.openmetadata.frameworkservices.oif.server.OpenIntegrationInstanceHandler;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;


/**
 * OIFMetadataOperationalServices initializes the REST Services that support the Open Integration Framework (OIF)
 * open integration calls.
 */
public class OIFMetadataOperationalServices
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
    public OIFMetadataOperationalServices(String                   serverName,
                                          OMRSRepositoryConnector  repositoryConnector,
                                          AuditLog                 auditLog,
                                          String                   localServerUserId,
                                          int                      maxPageSize) throws NewInstanceException
    {
        this.serverName = serverName;
        this.auditLog = auditLog;

        final String actionDescription = "initialize";

        auditLog.logMessage(actionDescription, OpenIntegrationAuditCode.SERVICE_INITIALIZING.getMessageDefinition());

        try
        {
            auditLog.logMessage(actionDescription, OpenIntegrationAuditCode.SERVICE_INITIALIZED.getMessageDefinition(serverName));

            new OpenIntegrationInstance(repositoryConnector, auditLog, localServerUserId, maxPageSize);
        }
        catch (NewInstanceException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  OpenIntegrationAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage()),
                                  error);
        }
    }


    /**
     * Shutdown the service.
     */
    public void shutdown()
    {
        final String actionDescription = "shutdown";

        this.auditLog.logMessage(actionDescription, OpenIntegrationAuditCode.SERVICE_SHUTDOWN.getMessageDefinition(serverName));

        new OpenIntegrationInstanceHandler().removeServerServiceInstance(serverName);
    }
}
