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
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public TemplateManagerInstance(String       serverName,
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
            openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
        }

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
