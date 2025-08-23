/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.GovernanceDefinitionHandler;

import java.util.List;

/**
 * GovernanceOfficerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GovernanceOfficerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.GOVERNANCE_OFFICER;

    private final ViewServiceClientMap<GovernanceDefinitionHandler>      governanceDefinitionHandlerMap;



    /**
     * Set up the Governance Officer OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public GovernanceOfficerInstance(String                  serverName,
                                     AuditLog                auditLog,
                                     String                  localServerUserId,
                                     String                  localServerUserPassword,
                                     int                     maxPageSize,
                                     String                  remoteServerName,
                                     String                  remoteServerURL,
                                     List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.governanceDefinitionHandlerMap = new ViewServiceClientMap<>(GovernanceDefinitionHandler.class,
                                                                         serverName,
                                                                         localServerUserId,
                                                                         localServerUserPassword,
                                                                         auditLog,
                                                                         activeViewServices,
                                                                         myDescription.getViewServiceFullName(),
                                                                         maxPageSize);
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public GovernanceDefinitionHandler getGovernanceDefinitionHandler(String urlMarker,
                                                                      String methodName) throws InvalidParameterException,
                                                                                                PropertyServerException
    {
        return governanceDefinitionHandlerMap.getClient(urlMarker, methodName);
    }
}
