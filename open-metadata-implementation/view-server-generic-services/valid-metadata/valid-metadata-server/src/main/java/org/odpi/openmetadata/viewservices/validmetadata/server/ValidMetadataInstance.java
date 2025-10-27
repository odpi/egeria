/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.validmetadata.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SpecificationPropertyHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.ValidMetadataValueHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EgeriaOpenMetadataStoreHandler;

import java.util.List;

/**
 * ValidMetadataInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ValidMetadataInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.VALID_METADATA;

    /*
     * These maps cache clients for specific view services.
     */
    private final ViewServiceClientMap<ValidMetadataValueHandler>      validMetadataValueHandlerMap;
    private final ViewServiceClientMap<SpecificationPropertyHandler>   specificationPropertyHandlerMap;
    private final ViewServiceClientMap<EgeriaOpenMetadataStoreHandler> openMetadataHandlerMap;

    /**
     * Set up theValid Metadata OMVS instance
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
    public ValidMetadataInstance(String                  serverName,
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

        this.validMetadataValueHandlerMap = new ViewServiceClientMap<>(ValidMetadataValueHandler.class,
                                                                       serverName,
                                                                       localServerUserId,
                                                                       localServerUserPassword,
                                                                       auditLog,
                                                                       activeViewServices,
                                                                       myDescription.getViewServiceFullName(),
                                                                       myDescription.getViewServiceURLMarker(),
                                                                       maxPageSize);

        this.specificationPropertyHandlerMap = new ViewServiceClientMap<>(SpecificationPropertyHandler.class,
                                                                          serverName,
                                                                          localServerUserId,
                                                                          localServerUserPassword,
                                                                          auditLog,
                                                                          activeViewServices,
                                                                          myDescription.getViewServiceFullName(),
                                                                          myDescription.getViewServiceURLMarker(),
                                                                          maxPageSize);

        this.openMetadataHandlerMap = new ViewServiceClientMap<>(EgeriaOpenMetadataStoreHandler.class,
                                                                 serverName,
                                                                 localServerUserId,
                                                                 localServerUserPassword,
                                                                 auditLog,
                                                                 activeViewServices,
                                                                 myDescription.getViewServiceFullName(),
                                                                 myDescription.getViewServiceURLMarker(),
                                                                 maxPageSize);
    }


    /**
     * Return the valid metadata value handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public ValidMetadataValueHandler getValidMetadataValueHandler(String urlMarker,
                                                                  String methodName) throws InvalidParameterException,
                                                                                            PropertyServerException
    {
        return validMetadataValueHandlerMap.getClient(urlMarker, methodName);
    }



    /**
     * Return the valid metadata value handler.
     *
     * @param urlMarker calling view service
     * @param methodName calling operation
     * @return client
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     */
    public SpecificationPropertyHandler getSpecificationPropertyHandler(String urlMarker,
                                                                        String methodName) throws InvalidParameterException,
                                                                                                  PropertyServerException
    {
        return specificationPropertyHandlerMap.getClient(urlMarker, methodName);
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
