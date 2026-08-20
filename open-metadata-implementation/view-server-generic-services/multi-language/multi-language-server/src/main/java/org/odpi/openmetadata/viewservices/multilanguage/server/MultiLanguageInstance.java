/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.multilanguage.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ViewServiceClientMap;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

import java.util.List;

/**
 * MultiLanguageInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class MultiLanguageInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.MULTI_LANGUAGE;

    private final ViewServiceClientMap<EgeriaOpenMetadataStoreClient> openMetadataHandlerMap;


    /**
     * Set up the Multi Language OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @param activeViewServices list of view services active in this server
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public MultiLanguageInstance(String                  serverName,
                                 AuditLog                auditLog,
                                 String                  localServerUserId,
                                 int                     maxPageSize,
                                 String                  remoteServerName,
                                 String                  remoteServerURL,
                                 List<ViewServiceConfig> activeViewServices) throws InvalidParameterException
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
    }


    /**
     * Return the open metadata handler.  This client implements MultiLanguageInterface which provides the
     * translation operations - it maintains the TranslationLink relationship and TranslationDetail elements
     * under the covers.
     *
     * @param urlMarker  view service URL marker
     * @param methodName calling method
     * @throws InvalidParameterException bad client initialization
     * @throws PropertyServerException bad client handler class
     * @return client
     */
    public OpenMetadataClient getOpenMetadataHandler(String urlMarker,
                                                     String methodName) throws InvalidParameterException,
                                                                               PropertyServerException
    {
        return openMetadataHandlerMap.getClient(urlMarker, methodName);
    }
}
