/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.oif.client.contextmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;
import org.odpi.openmetadata.frameworkservices.oif.client.OpenIntegrationServiceClient;
import org.odpi.openmetadata.frameworkservices.oif.ffdc.OpenIntegrationAuditCode;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataEventClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;


/**
 * OIFContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration context
 */
public class OIFContextManager extends IntegrationContextManager
{
    /**
     * Default constructor
     */
    public OIFContextManager()
    {
        super();
    }


    /**
     * Initialize server properties for the context manager.
     *
     * @param localServerName name of this integration daemon
     * @param localServiceName name of calling service
     * @param partnerOMASServerName name of the server to connect to
     * @param partnerOMASPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum number of results that can be returned on a single REST call
     * @param auditLog logging destination
     */
    public void initializeContextManager(String              localServerName,
                                         String              localServiceName,
                                         String              partnerOMASServerName,
                                         String              partnerOMASPlatformRootURL,
                                         String              userId,
                                         String              password,
                                         int                 maxPageSize,
                                         AuditLog            auditLog)
    {
        super.initializeContextManager(localServerName, localServiceName, partnerOMASServerName, partnerOMASPlatformRootURL, userId, password, maxPageSize, auditLog);

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                            OpenIntegrationAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public void createClients() throws InvalidParameterException
    {
        super.openIntegrationClient   = new OpenIntegrationServiceClient(partnerOMASServerName, partnerOMASPlatformRootURL, maxPageSize);
        super.openMetadataClient      = new EgeriaOpenMetadataStoreClient(partnerOMASServerName, partnerOMASPlatformRootURL, maxPageSize);
        super.openGovernanceClient    = new EgeriaOpenGovernanceClient(partnerOMASServerName, partnerOMASPlatformRootURL, maxPageSize);
        super.governanceConfiguration = new GovernanceConfigurationClient(partnerOMASServerName, partnerOMASPlatformRootURL, maxPageSize);

        if (localServerPassword == null)
        {
            connectedAssetClient = new EgeriaConnectedAssetClient(partnerOMASServerName,
                                                                  partnerOMASPlatformRootURL,
                                                                  maxPageSize,
                                                                  auditLog);

            openMetadataClient = new EgeriaOpenMetadataStoreClient(partnerOMASServerName,
                                                                   partnerOMASPlatformRootURL,
                                                                   maxPageSize);
        }
        else
        {
            connectedAssetClient = new EgeriaConnectedAssetClient(partnerOMASServerName,
                                                            partnerOMASPlatformRootURL,
                                                            localServerUserId,
                                                            localServerPassword,
                                                            maxPageSize,
                                                            auditLog);

            openMetadataClient = new EgeriaOpenMetadataStoreClient(partnerOMASServerName,
                                                                   partnerOMASPlatformRootURL,
                                                                   localServerUserId,
                                                                   localServerPassword,
                                                                   maxPageSize);
        }
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @param connectorId used as the caller Id
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public OpenMetadataEventClient createEventClient(String connectorId) throws InvalidParameterException
    {
        return new EgeriaOpenMetadataEventClient(partnerOMASServerName,
                                                 partnerOMASPlatformRootURL,
                                                 localServerUserId,
                                                 localServerPassword,
                                                 maxPageSize,
                                                 auditLog,
                                                 connectorId);
    }
}
