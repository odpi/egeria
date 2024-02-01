/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.rest.ViewServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.List;
import java.util.Map;

/**
 * ViewServerConfigurationClient provides the configuration services for view servers.
 * This involves creating a list of view services config properties.
 */
public class ViewServerConfigurationClient extends OMAGServerConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ViewServerConfigurationClient(String adminUserId,
                                         String serverName,
                                         String serverPlatformRootURL) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL);
    }


    /**
     * Create a new client that passes a connection userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is passed as the admin userId.
     *
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param connectionUserId      caller's system userId embedded in all HTTP requests
     * @param connectionPassword    caller's system password embedded in all HTTP requests
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ViewServerConfigurationClient(String adminUserId,
                                         String serverName,
                                         String serverPlatformRootURL,
                                         String connectionUserId,
                                         String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }



    /*
     * =============================================================
     * Learn about all possible view services
     */

    /**
     * Return the list of view services for this server.
     *
     * @return list of view service descriptions
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getRegisteredViewServices() throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getRegisteredViewServices";
        final String urlTemplate = "/open-metadata/platform-services/users/{0}/server-platform/registered-services/view-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     adminUserId);
        return restResult.getServices();
    }


    /*
     * =============================================================
     * Retrieve the current state of the view service configuration
     */


    /**
     * Return the list of view services for this server.
     *
     * @return list of view service descriptions
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getConfiguredViewServices() throws OMAGNotAuthorizedException, 
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getConfiguredViewServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     adminUserId,
                                                                                                     serverName);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the view services in this server.
     *
     * @return list of view service configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<ViewServiceConfig> getViewServicesConfiguration() throws OMAGNotAuthorizedException, 
                                                                         OMAGInvalidParameterException,
                                                                         OMAGConfigurationErrorException
    {
        final String methodName  = "getViewServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services/configuration";

        ViewServicesResponse restResult = restClient.callViewServicesGetRESTCall(methodName, 
                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                 adminUserId,
                                                                                 serverName);
        return restResult.getServices();
    }



    /**
     * Return the configuration for the view services in this server.
     *
     * @param viewServices  list of view service configuration
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setViewServicesConfiguration(List<ViewServiceConfig> viewServices) throws OMAGNotAuthorizedException,
                                                                                          OMAGInvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "setViewServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services/configuration";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        viewServices,
                                        adminUserId,
                                        serverName);
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */

    /**
     * Enable a single view service.
     *
     * @param partnerOMASServerURLRoot URL root of the OMAG Server Platform where the access service used by this view service is running
     * @param partnerOMASServerName name of metadata access server where the access service used by this view service is running
     * @param serviceURLMarker string indicating which view service it is configuring
     * @param viewServiceOptions property name/value pairs used to configure the view service
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureViewService(String              partnerOMASServerURLRoot,
                                     String              partnerOMASServerName,
                                     String              serviceURLMarker,
                                     Map<String, Object> viewServiceOptions) throws OMAGNotAuthorizedException,
                                                                                    OMAGInvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        this.configureViewService(partnerOMASServerURLRoot, partnerOMASServerName, serviceURLMarker, viewServiceOptions, null);
    }

    /**
     * Enable a single view service.
     *
     * @param partnerOMASServerURLRoot URL root of the OMAG Server Platform where the access service used by this view service is running
     * @param partnerOMASServerName name of metadata access server where the access service used by this view service is running
     * @param serviceURLMarker string indicating which view service it is configuring
     * @param viewServiceOptions property name/value pairs used to configure the view service
     * @param resourceEndpoints list of resource endpoint configuration objects
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureViewService(String                       partnerOMASServerURLRoot,
                                     String                       partnerOMASServerName,
                                     String                       serviceURLMarker,
                                     Map<String, Object>          viewServiceOptions,
                                     List<ResourceEndpointConfig> resourceEndpoints) throws OMAGNotAuthorizedException,
                                                                                            OMAGInvalidParameterException,
                                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "configureViewService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services/{2}";

        try
        {
            invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        ViewServiceRequestBody requestBody = new ViewServiceRequestBody();

        requestBody.setOMAGServerPlatformRootURL(partnerOMASServerURLRoot);
        requestBody.setOMAGServerName(partnerOMASServerName);
        requestBody.setViewServiceOptions(viewServiceOptions);
        requestBody.setResourceEndpoints(resourceEndpoints);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        serviceURLMarker);
    }


    /**
     * Disable a single view service.
     *
     * @param serviceURLMarker string indicating which view service it is configuring
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableViewService(String serviceURLMarker) throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName    = "disableViewService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services/{2}";

        try
        {
            invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        adminUserId,
                                        serverName,
                                        serviceURLMarker);
    }



    /**
     * Enable all registered view services with the same partner server and options.
     *
     * @param partnerOMASServerURLRoot URL root of the OMAG Server Platform where the access service used by this view service is running
     * @param partnerOMASServerName name of metadata access server where the access service used by this view service is running
     * @param viewServiceOptions property name/value pairs used to configure the view service
     * @param resourceEndpoints list of resource endpoint configuration objects
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAllViewService(String                       partnerOMASServerURLRoot,
                                        String                       partnerOMASServerName,
                                        Map<String, Object>          viewServiceOptions,
                                        List<ResourceEndpointConfig> resourceEndpoints) throws OMAGNotAuthorizedException,
                                                                                               OMAGInvalidParameterException,
                                                                                               OMAGConfigurationErrorException
    {
        final String methodName    = "configureAllViewService";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services";

        ViewServiceRequestBody requestBody = new ViewServiceRequestBody();

        requestBody.setOMAGServerPlatformRootURL(partnerOMASServerURLRoot);
        requestBody.setOMAGServerName(partnerOMASServerName);
        requestBody.setViewServiceOptions(viewServiceOptions);
        requestBody.setResourceEndpoints(resourceEndpoints);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Disable the view services.  This removes all configuration for the view server.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllViewServices() throws OMAGNotAuthorizedException, 
                                              OMAGInvalidParameterException,
                                              OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllViewServices";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/view-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }
}
