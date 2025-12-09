/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.client;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AssetHandler;
import org.odpi.openmetadata.frameworkservices.gaf.ffdc.OpenGovernanceAuditCode;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.EgeriaConnectedAssetClient;
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
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum number of results that can be returned on a single REST call
     * @param auditLog logging destination
     */
    public void initializeContextManager(String              localServerName,
                                         String              localServiceName,
                                         String              partnerOMASServerName,
                                         String              partnerOMASPlatformRootURL,
                                         String              userId,
                                         String              secretsStoreProvider,
                                         String              secretsStoreLocation,
                                         String              secretsStoreCollection,
                                         int                 maxPageSize,
                                         AuditLog            auditLog)
    {
        super.initializeContextManager(localServerName, localServiceName, partnerOMASServerName, partnerOMASPlatformRootURL, userId, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, maxPageSize, auditLog);

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                            OpenGovernanceAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public void createClients() throws InvalidParameterException
    {
        super.openMetadataClient      = new EgeriaOpenMetadataStoreClient(partnerOMASServerName,
                                                                          partnerOMASPlatformRootURL,
                                                                          secretsStoreProvider,
                                                                          secretsStoreLocation,
                                                                          secretsStoreCollection,
                                                                          maxPageSize,
                                                                          auditLog);

        super.openGovernanceClient    = new EgeriaOpenGovernanceClient(partnerOMASServerName,
                                                                       partnerOMASPlatformRootURL,
                                                                       secretsStoreProvider,
                                                                       secretsStoreLocation,
                                                                       secretsStoreCollection,
                                                                       maxPageSize,
                                                                       auditLog);

        super.governanceConfiguration = new GovernanceConfigurationClient(partnerOMASServerName,
                                                                          partnerOMASPlatformRootURL,
                                                                          secretsStoreProvider,
                                                                          secretsStoreLocation,
                                                                          secretsStoreCollection,
                                                                          maxPageSize,
                                                                          auditLog);

        super.connectedAssetClient = new EgeriaConnectedAssetClient(partnerOMASServerName,
                                                                    partnerOMASPlatformRootURL,
                                                                    secretsStoreProvider,
                                                                    secretsStoreLocation,
                                                                    secretsStoreCollection,
                                                                    maxPageSize,
                                                                    auditLog);

        super.openMetadataClient = new EgeriaOpenMetadataStoreClient(partnerOMASServerName,
                                                                     partnerOMASPlatformRootURL,
                                                                     secretsStoreProvider,
                                                                     secretsStoreLocation,
                                                                     secretsStoreCollection,
                                                                     maxPageSize,
                                                                     auditLog);

        super.assetHandler = new AssetHandler(localServerName, auditLog, localServiceName, openMetadataClient);
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
                                                 secretsStoreProvider,
                                                 secretsStoreLocation,
                                                 secretsStoreCollection,
                                                 maxPageSize,
                                                 auditLog,
                                                 connectorId);
    }
}
