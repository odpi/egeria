/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.TemplateHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

/**
 * TemplateManagerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class TemplateManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.TEMPLATE_MANAGER;

    private final TemplateHandler    templateHandler;
    private final OpenMetadataClient openMetadataClient;


    /**
     * Set up theTemplate Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public TemplateManagerInstance(String   serverName,
                                   AuditLog auditLog,
                                   String   localServerUserId,
                                   String   localServerSecretsStoreProvider,
                                   String   localServerSecretsStoreLocation,
                                   String   localServerSecretsStoreCollection,
                                   int      maxPageSize,
                                   String   remoteServerName,
                                   String   remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                               remoteServerURL,
                                                               localServerSecretsStoreProvider,
                                                               localServerSecretsStoreLocation,
                                                               localServerSecretsStoreCollection,
                                                               maxPageSize,
                                                               auditLog);

        templateHandler = new TemplateHandler(serverName, auditLog, myDescription.getViewServiceFullName(), openMetadataClient);
    }


    /**
     * Return the template manager client.  This client is from the Digital Architecture OMAS and is for maintaining
     * template classifications and associated specifications.
     *
     * @return client
     */
    public TemplateHandler getTemplateHandler()
    {
        return templateHandler;
    }


    /**
     * Return the open metadata store client.  This client is from the Digital Architecture OMAS and
     * provides a generic interface to open metadata.
     *
     * @return client
     */
    public OpenMetadataClient getOpenMetadataClient()
    {
        return openMetadataClient;
    }
}
