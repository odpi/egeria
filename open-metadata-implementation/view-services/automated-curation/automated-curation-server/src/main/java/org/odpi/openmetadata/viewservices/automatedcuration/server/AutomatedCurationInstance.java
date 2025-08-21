/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.oif.client.OpenIntegrationServiceClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.viewservices.automatedcuration.handlers.TechnologyTypeHandler;

/**
 * AutomatedCurationInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AutomatedCurationInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.AUTOMATED_CURATION;


    private final EgeriaOpenMetadataStoreClient openMetadataStoreClient;
    private final EgeriaOpenGovernanceClient    openGovernanceClient;
    private final OpenIntegrationServiceClient openIntegrationServiceClient;
    private final GovernanceConfigurationClient governanceConfigurationClient;
    private final TechnologyTypeHandler         technologyTypeHandler;

    /**
     * Set up the Automated Curation OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public AutomatedCurationInstance(String       serverName,
                                     AuditLog     auditLog,
                                     String       localServerUserId,
                                     String       localServerUserPassword,
                                     int          maxPageSize,
                                     String       remoteServerName,
                                     String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            openMetadataStoreClient       = new EgeriaOpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
            openGovernanceClient          = new EgeriaOpenGovernanceClient(remoteServerName, remoteServerURL, maxPageSize);
            openIntegrationServiceClient  = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, maxPageSize);
            governanceConfigurationClient = new GovernanceConfigurationClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            openMetadataStoreClient       = new EgeriaOpenMetadataStoreClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            openGovernanceClient          = new EgeriaOpenGovernanceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            openIntegrationServiceClient  = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            governanceConfigurationClient = new GovernanceConfigurationClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
        }

        technologyTypeHandler = new TechnologyTypeHandler(openMetadataStoreClient, serviceName, serverName);
    }


    /**
     * Return the open metadata store client.  This client is from the Open Metadata Framework (OMF) and is for accessing and
     * maintaining all types of metadata.
     *
     * @return client
     */
    public EgeriaOpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }


    /**
     * Return the technology type handler.  This client is from the Automated Curation OMVS and is for accessing
     * technology types.
     *
     * @return client
     */
    public TechnologyTypeHandler getTechnologyTypeHandler()
    {
        return technologyTypeHandler;
    }


    /**
     * Return the open governance client.  This client is from the Open Governance Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public EgeriaOpenGovernanceClient getOpenGovernanceClient()
    {
        return openGovernanceClient;
    }


    /**
     * Return the open integration client.  This client is from the Open Integration Framework (OIF) and is for
     * working with integration connectors.
     *
     * @return client
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient()
    {
        return openIntegrationServiceClient;
    }


    /**
     * Return the  governance configuration client.  This client is from the Open Governance Framework (OGF) and is for
     * working with automation services.
     *
     * @return client
     */
    public GovernanceConfigurationClient getGovernanceConfigurationClient()
    {
        return governanceConfigurationClient;
    }
}
