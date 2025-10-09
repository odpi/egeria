/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.externalreferences.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ExternalReferenceHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EgeriaOpenMetadataStoreHandler;

import java.util.List;

/**
 * ExternalReferencesInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ExternalReferencesInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.EXTERNAL_REFERENCES;

    /*
     * These maps cache clients for specific view services.
     */
    private final ViewServiceClientMap<ExternalReferenceHandler>      externalReferenceHandlerMap;
    private final ViewServiceClientMap<EgeriaOpenMetadataStoreHandler> openMetadataHandlerMap;


    /**
     * Set up the External References OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerUserPassword  password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     */
    public ExternalReferencesInstance(String                  serverName,
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


        this.openMetadataHandlerMap = new ViewServiceClientMap<>(EgeriaOpenMetadataStoreHandler.class,
                                                                 serverName,
                                                                 localServerUserId,
                                                                 localServerUserPassword,
                                                                 auditLog,
                                                                 activeViewServices,
                                                                 myDescription.getViewServiceFullName(),
                                                                 maxPageSize);


        this.externalReferenceHandlerMap = new ViewServiceClientMap<>(ExternalReferenceHandler.class,
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
     * schema type artifacts.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public ExternalReferenceHandler getExternalReferenceHandler(String urlMarker,
                                                                String methodName) throws InvalidParameterException,
                                                                                          PropertyServerException
    {
        return externalReferenceHandlerMap.getClient(urlMarker, methodName);
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

}
