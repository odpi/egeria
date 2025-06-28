/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * GlossaryBrowserInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GlossaryBrowserInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.GLOSSARY_BROWSER;

    private final GlossaryExchangeClient      glossaryExchangeClient;


    /**
     * Set up the Glossary Browser OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public GlossaryBrowserInstance(String       serverName,
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
            glossaryExchangeClient = new GlossaryExchangeClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        }
        else
        {
            glossaryExchangeClient = new GlossaryExchangeClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog, maxPageSize);
        }
    }


    /**
     * Return the glossary management client.  This client is from Asset Manager OMAS and is for maintaining glossaries and their content.
     *
     * @return client
     */
    public GlossaryExchangeClient getGlossaryExchangeClient()
    {
        return glossaryExchangeClient;
    }
}
