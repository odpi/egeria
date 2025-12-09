/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.OMAGServerClientConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.ViewServiceConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.ViewServiceRequestBody;
import org.odpi.openmetadata.adminservices.rest.ViewServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

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
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public ViewServerConfigurationClient(String   serverName,
                                         String   serverPlatformRootURL,
                                         String   secretStoreProvider,
                                         String   secretStoreLocation,
                                         String   secretStoreCollection,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, auditLog);
    }


    /*
     * =============================================================
     * Learn about all possible view services
     */

    /**
     * Return the list of view services for this server.
     *
     * @return list of view service descriptions
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getRegisteredViewServices() throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getRegisteredViewServices";
        final String urlTemplate = "/open-metadata/platform-services/users/{0}/server-platform/registered-services/view-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate);
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
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getConfiguredViewServices() throws UserNotAuthorizedException, 
                                                                          InvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getConfiguredViewServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/view-services";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     serverName);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the view services in this server.
     *
     * @return list of view service configuration
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<ViewServiceConfig> getViewServicesConfiguration() throws UserNotAuthorizedException, 
                                                                         InvalidParameterException,
                                                                         OMAGConfigurationErrorException
    {
        final String methodName  = "getViewServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/view-services/configuration";

        ViewServicesResponse restResult = restClient.callViewServicesGetRESTCall(methodName, 
                                                                                 serverPlatformRootURL + urlTemplate,
                                                                                 serverName);
        return restResult.getServices();
    }



    /**
     * Return the configuration for the view services in this server.
     *
     * @param viewServices  list of view service configuration
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setViewServicesConfiguration(List<ViewServiceConfig> viewServices) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "setViewServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/view-services/configuration";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        viewServices,
                                        serverName);
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */

    /**
     * Enable a single view service.
     *
     * @param serviceURLMarker string indicating which view service it is configuring
     * @param viewServiceConfig properties used to configure the view service
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureViewService(String            serviceURLMarker,
                                     ViewServiceConfig viewServiceConfig) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    OMAGConfigurationErrorException
    {
        final String methodName    = "configureViewService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/view-services/{1}";

        invalidParameterHandler.validateName(serviceURLMarker, serviceURLMarker, methodName);

        ViewServiceRequestBody requestBody = new ViewServiceRequestBody(viewServiceConfig);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        serviceURLMarker);
    }


    /**
     * Disable a single view service.
     *
     * @param serviceURLMarker string indicating which view service it is configuring
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableViewService(String serviceURLMarker) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName    = "disableViewService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/view-services/{1}";

        invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);

        restClient.callVoidDeleteRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        serverName,
                                        serviceURLMarker);
    }



    /**
     * Enable all registered view services with the same partner server and options.
     *
     * @param metadataServerDetails URL root of the OMAG Server Platform  and
     *  details of metadata access server where the OMF Services used by this view service is running
     * @param viewServiceOptions property name/value pairs used to configure the view service
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAllViewService(OMAGServerClientConfig metadataServerDetails,
                                        Map<String, Object>    viewServiceOptions) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "configureAllViewService";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/view-services";

        ViewServiceRequestBody requestBody = new ViewServiceRequestBody(metadataServerDetails);

        requestBody.setViewServiceOptions(viewServiceOptions);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName);
    }


    /**
     * Disable the view services.  This removes all configuration for the view server.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllViewServices() throws UserNotAuthorizedException, 
                                              InvalidParameterException,
                                              OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllViewServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/view-services";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName);
    }
}
