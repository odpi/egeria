/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.EgeriaOpenMetadataStoreHandler;

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

    private final Class<B>                handlerClass;
    private final List<ViewServiceConfig> activeViewServices;
    private final String                  localServerName;
    private final AuditLog                auditLog;
    private final String                  serviceName;
    private final int                     maxPageSize;

    private final String localServerUserId;
    private final String localServerUserPassword;



    /**
     * Create new clients that pass userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param handlerClass          name of the class to create
     * @param localServerName       name of this server (view server)
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param activeViewServices    which view services are running
     * @param serviceName           local service name
     * @param auditLog              logging destination
     */
    public ViewServiceClientMap(Class<B>                handlerClass,
                                String                  localServerName,
                                String                  userId,
                                String                  password,
                                AuditLog                auditLog,
                                List<ViewServiceConfig> activeViewServices,
                                String                  serviceName,
                                int                     maxPageSize)
    {
        this.handlerClass    = handlerClass;
        this.localServerName = localServerName;
        this.activeViewServices = activeViewServices;
        this.auditLog = auditLog;
        this.serviceName = serviceName;
        this.localServerUserId = userId;
        this.localServerUserPassword = password;
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
                            for (CommonServicesDescription commonServicesDescription : CommonServicesDescription.values())
                            {
                                if (viewServicePartnerService.equals(commonServicesDescription.getServiceName()))
                                {
                                    viewServiceClient = this.createViewServiceClient(viewServiceConfig,
                                                                                     commonServicesDescription.getServiceURLMarker());
                                    viewServiceClientMap.put(viewServiceURLMarker, viewServiceClient);
                                    return viewServiceClient;
                                }
                            }

                            for (AccessServiceDescription accessServiceDescription : AccessServiceDescription.values())
                            {
                                if (accessServiceDescription.getServiceName().equals(viewServicePartnerService))
                                {
                                    viewServiceClient = this.createViewServiceClient(viewServiceConfig,
                                                                                     accessServiceDescription.getServiceURLMarker());
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
     * Create a new client.
     *
     * @param viewServiceConfig configuration from the matching view service
     * @param partnerServiceURL url marker to call
     * @return client
     * @throws InvalidParameterException problem initializing the client
     * @throws PropertyServerException problem with the client class
     */
    private B createViewServiceClient(ViewServiceConfig viewServiceConfig,
                                      String            partnerServiceURL) throws InvalidParameterException,
                                                                                  PropertyServerException
    {
        final String methodName = "createViewServiceClient";

        B viewServiceClient = null;

        EgeriaOpenMetadataStoreHandler openMetadataClient;
        EgeriaOpenGovernanceClient     openGovernanceClient;
        if (localServerUserPassword == null)
        {
            openMetadataClient = new EgeriaOpenMetadataStoreHandler(viewServiceConfig.getOMAGServerName(),
                                                                    viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                    maxPageSize);

            openGovernanceClient = new EgeriaOpenGovernanceClient(viewServiceConfig.getOMAGServerName(),
                                                                  viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                  maxPageSize);

        }
        else
        {
            openMetadataClient = new EgeriaOpenMetadataStoreHandler(viewServiceConfig.getOMAGServerName(),
                                                                    viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                    localServerUserId,
                                                                    localServerUserPassword,
                                                                    maxPageSize);

            openGovernanceClient = new EgeriaOpenGovernanceClient(viewServiceConfig.getOMAGServerName(),
                                                                  viewServiceConfig.getOMAGServerPlatformRootURL(),
                                                                  localServerUserId,
                                                                  localServerUserPassword,
                                                                  maxPageSize);
        }

        try
        {
            if (handlerClass.isInstance(openMetadataClient))
            {
                viewServiceClient = handlerClass.cast(openMetadataClient);
            }
            else if (handlerClass.isInstance(openGovernanceClient))
            {
                viewServiceClient = handlerClass.cast(openGovernanceClient);
            }
            else
            {
                /*
                 * Add a specialized handler around the open metadata client.
                 */
                Constructor<B> constructor = handlerClass.getDeclaredConstructor(String.class,
                                                                                 AuditLog.class,
                                                                                 String.class,
                                                                                 OpenMetadataClient.class);
                viewServiceClient = constructor.newInstance(localServerName,
                                                            auditLog,
                                                            serviceName,
                                                            openMetadataClient);
            }
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException |
               InvocationTargetException error)
        {
            this.handleInvalidBeanClass(handlerClass.getName(), error, methodName);
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
