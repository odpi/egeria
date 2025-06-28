/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a map from view service URL marker to client.  This is used by the generic view
 * services to ensure the request is mapped to the correct access service in the metadata access server.
 */
public class ViewServiceClientMap<B>
{
    private final Map<String, B> viewServiceClientMap = new HashMap<>();

    private final Class<B> beanClass;

    private final List<ViewServiceConfig> activeViewServices;
    String   localServerName;
    String   remoteServerName;
    String   remotePlatformURLRoot;

    AuditLog auditLog;
    String   serviceName;
    int      maxPageSize;

    String   localServerUserId = null;
    String   localServerUserPassword = null;

    /**
     * Create new clients with no authentication embedded in the HTTP request.
     *
     * @param beanClass name of the class to create
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param serviceName           local service name
     * @param maxPageSize           maximum number of results supported by this server
     */
    public ViewServiceClientMap(Class<B> beanClass,
                                String   localServerName,
                                String   serverName,
                                String   serverPlatformURLRoot,
                                AuditLog auditLog,
                                List<ViewServiceConfig> activeViewServices,
                                String   serviceName,
                                int      maxPageSize)
    {
        this.beanClass = beanClass;
        this.localServerName = localServerName;
        this.activeViewServices = activeViewServices;
        this.auditLog = auditLog;
        this.serviceName = serviceName;
        this.remotePlatformURLRoot = serverPlatformURLRoot;
        this.remoteServerName = serverName;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Create new clients that pass userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param beanClass name of the class to create
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param activeViewServices    which view services are running
     * @param serviceName           local service name
     * @param auditLog              logging destination
     */
    public ViewServiceClientMap(Class<B>                beanClass,
                                String                  localServerName,
                                String                  serverName,
                                String                  serverPlatformURLRoot,
                                String                  userId,
                                String                  password,
                                AuditLog                auditLog,
                                List<ViewServiceConfig> activeViewServices,
                                String                  serviceName,
                                int                     maxPageSize)
    {
        this.beanClass = beanClass;
        this.localServerName = localServerName;
        this.activeViewServices = activeViewServices;
        this.auditLog = auditLog;
        this.serviceName = serviceName;
        this.localServerUserId = userId;
        this.localServerUserPassword = password;
        this.remotePlatformURLRoot = serverPlatformURLRoot;
        this.remoteServerName = serverName;
        this.maxPageSize = maxPageSize;
    }


    /**
     * Retrieve/create the client for the requested view service.
     *
     * @param viewServiceURLMarker url marker from the incoming request
     * @param methodName calling method
     * @return requested client
     * @throws InvalidParameterException problem initializing the client
     * @throws PropertyServerException problem with the client class
     */
    public B getClient(String viewServiceURLMarker,
                       String methodName) throws InvalidParameterException, PropertyServerException
    {
        B viewServiceClient = null;

        if (viewServiceURLMarker != null)
        {
            viewServiceClient = viewServiceClientMap.get(viewServiceURLMarker);

            if (viewServiceClient == null)
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
                                    if (localServerUserPassword == null)
                                    {
                                        try
                                        {
                                            Constructor<B> constructor = beanClass.getDeclaredConstructor(String.class,
                                                                                                          String.class,
                                                                                                          String.class,
                                                                                                          AuditLog.class,
                                                                                                          String.class,
                                                                                                          String.class,
                                                                                                          int.class);
                                            viewServiceClient = constructor.newInstance(localServerName,
                                                                                        viewServiceConfig.getOMAGServerName(),
                                                                                        viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                        auditLog,
                                                                                        accessServiceDescription.getAccessServiceURLMarker(),
                                                                                        serviceName,
                                                                                        maxPageSize);
                                        }
                                        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException |
                                               InvocationTargetException error)
                                        {
                                            this.handleInvalidBeanClass(beanClass.getName(), error, methodName);
                                        }
                                    }
                                    else
                                    {
                                        try
                                        {
                                            Constructor<B> constructor = beanClass.getDeclaredConstructor(String.class,
                                                                                                          String.class,
                                                                                                          String.class,
                                                                                                          String.class,
                                                                                                          String.class,
                                                                                                          AuditLog.class,
                                                                                                          String.class,
                                                                                                          String.class,
                                                                                                          int.class);
                                            viewServiceClient = constructor.newInstance(localServerName,
                                                                                        viewServiceConfig.getOMAGServerName(),
                                                                                        viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                                        localServerUserId,
                                                                                        localServerUserPassword,
                                                                                        auditLog,
                                                                                        accessServiceDescription.getAccessServiceURLMarker(),
                                                                                        serviceName,
                                                                                        maxPageSize);
                                        }
                                        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException |
                                               InvocationTargetException error)
                                        {
                                            this.handleInvalidBeanClass(beanClass.getName(), error, methodName);
                                        }
                                    }

                                    viewServiceClientMap.put(viewServiceURLMarker, viewServiceClient);
                                    return viewServiceClient;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (viewServiceClient == null)
        {
            throw new InvalidParameterException(OMAGServerInstanceErrorCode.INVALID_URL_MARKER.getMessageDefinition(viewServiceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                "viewServiceURLMarker");
        }

        return viewServiceClient;
    }


    /**
     * Throw an exception to indicate that one of the update methods has not been implemented by an OMAS.
     *
     * @param beanClassName class name of bean
     * @param error exception generated when the new bean is created
     * @param methodName method that is missing
     * @throws PropertyServerException there is a problem in the use of the generic handlers because
     * the converter has been configured with a type of bean that is not a known class
     */
    protected void handleInvalidBeanClass(String    beanClassName,
                                          Exception error,
                                          String    methodName) throws PropertyServerException
    {
        throw new PropertyServerException(OMAGServerInstanceErrorCode.INVALID_BEAN_CLASS.getMessageDefinition(beanClassName,
                                                                                                              methodName,
                                                                                                              serviceName,
                                                                                                              localServerName,
                                                                                                              error.getMessage()),
                                          this.getClass().getName(),
                                          methodName,
                                          error);
    }

}
