/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.governanceofficer.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.viewservices.governanceofficer.ffdc.GovernanceOfficerErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GovernanceOfficerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GovernanceOfficerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.GOVERNANCE_OFFICER;

    /*
     * This map caches clients for specific view services/access services.
     */
    private final Map<String, GovernanceDefinitionHandler> governanceDefinitionHandlerMap = new HashMap<>();

    private final List<ViewServiceConfig> activeViewServices;



    /**
     * Set up the Governance Officer OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public GovernanceOfficerInstance(String                  serverName,
                                     AuditLog                auditLog,
                                     String                  localServerUserId,
                                     int                     maxPageSize,
                                     String                  remoteServerName,
                                     String                  remoteServerURL,
                                     List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.activeViewServices = activeViewServices;
    }


    /**
     * Return the client.  This client is from the Open Metadata Store services and is for maintaining
     * governance definition artifacts.
     *
     * @return client
     */
    public GovernanceDefinitionHandler getGovernanceDefinitionHandler(String viewServiceURLMarker,
                                                                      String methodName) throws InvalidParameterException
    {

        GovernanceDefinitionHandler governanceDefinitionHandler = null;

        if (viewServiceURLMarker != null)
        {
            governanceDefinitionHandler = governanceDefinitionHandlerMap.get(viewServiceURLMarker);

            if (governanceDefinitionHandler == null)
            {
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceURLMarker().equals(viewServiceURLMarker))
                    {
                        String viewServicePartnerService = viewServiceConfig.getViewServicePartnerService();

                        if (viewServicePartnerService != null)
                        {
                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getAccessServiceFullName().equals(viewServicePartnerService))
                                {
                                    governanceDefinitionHandler = new GovernanceDefinitionHandler(serverName,
                                                                                                  viewServiceConfig.getOMAGServerName(),
                                                                                                  viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                                  auditLog,
                                                                                                  accessServiceDescription.getAccessServiceURLMarker(),
                                                                                                  ViewServiceDescription.GOVERNANCE_OFFICER.getViewServiceFullName(),
                                                                                                  maxPageSize);

                                    governanceDefinitionHandlerMap.put(viewServiceURLMarker,
                                                                       governanceDefinitionHandler);
                                }
                            }
                        }
                    }
                }
            }
        }


        if (governanceDefinitionHandler == null)
        {
            throw new InvalidParameterException(GovernanceOfficerErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return governanceDefinitionHandler;
    }
}
