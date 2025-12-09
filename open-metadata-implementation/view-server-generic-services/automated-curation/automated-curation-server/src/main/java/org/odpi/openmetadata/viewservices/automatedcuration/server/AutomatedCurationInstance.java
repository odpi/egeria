/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.automatedcuration.server;


import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;
import org.odpi.openmetadata.viewservices.automatedcuration.handlers.TechnologyTypeHandler;

import java.util.List;

/**
 * AutomatedCurationInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class AutomatedCurationInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.AUTOMATED_CURATION;


    private final ViewServiceClientMap<EgeriaOpenMetadataStoreClient> openMetadataHandlerMap;
    private final ViewServiceClientMap<TechnologyTypeHandler>         technologyTypeHandlerMap;
    private final ViewServiceClientMap<EgeriaOpenGovernanceClient>    openGovernanceClientMap;


    /**
     * Set up the Automated Curation OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public AutomatedCurationInstance(String                  serverName,
                                     AuditLog                auditLog,
                                     String                  localServerUserId,
                                     int                     maxPageSize,
                                     String                  remoteServerName,
                                     String                  remoteServerURL,
                                     List<ViewServiceConfig> activeViewServices)
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        this.openMetadataHandlerMap = new ViewServiceClientMap<>(EgeriaOpenMetadataStoreClient.class,
                                                                 serverName,
                                                                 auditLog,
                                                                 activeViewServices,
                                                                 myDescription.getViewServiceFullName(),
                                                                 myDescription.getViewServiceURLMarker(),
                                                                 maxPageSize);

        this.technologyTypeHandlerMap = new ViewServiceClientMap<>(TechnologyTypeHandler.class,
                                                                   serverName,
                                                                   auditLog,
                                                                   activeViewServices,
                                                                   myDescription.getViewServiceFullName(),
                                                                   myDescription.getViewServiceURLMarker(),
                                                                   maxPageSize);

        this.openGovernanceClientMap = new ViewServiceClientMap<>(EgeriaOpenGovernanceClient.class,
                                                                  serverName,
                                                                  auditLog,
                                                                  activeViewServices,
                                                                  myDescription.getViewServiceFullName(),
                                                                  myDescription.getViewServiceURLMarker(),
                                                                  maxPageSize);
    }


    /**
     * Return the open metadata store client.  This client is from the Open Metadata Framework (OMF) and is for accessing and
     * maintaining all types of metadata.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     */
    public OpenMetadataClient getOpenMetadataStoreClient(String urlMarker,
                                                         String methodName) throws InvalidParameterException,
                                                                                   PropertyServerException
    {
        return openMetadataHandlerMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the technology type handler.  This client is from the Automated Curation OMVS and is for accessing
     * technology types.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public TechnologyTypeHandler getTechnologyTypeHandler(String urlMarker,
                                                          String methodName) throws InvalidParameterException,
                                                                                    PropertyServerException
    {
        return technologyTypeHandlerMap.getClient(urlMarker, methodName);
    }


    /**
     * Return the open governance client.  This client is from the Open Governance Framework (GAF) and is for
     * working with automation services.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     * @return client
     */
    public EgeriaOpenGovernanceClient getOpenGovernanceClient(String urlMarker,
                                                              String methodName) throws InvalidParameterException,
                                                                                        PropertyServerException
    {
        return openGovernanceClientMap.getClient(urlMarker, methodName);
    }
}
