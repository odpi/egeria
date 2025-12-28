/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.AccessServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EnterpriseAccessConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.rest.AccessServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGServicesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringMapResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MetadataAccessServerConfigurationClient configures a MetadataAccessServer OMAG Server.  This server
 * can become a cohort member and, through the access services, offers a wide range of specialist APIs
 * and event streams to access and store metadata.
 */
public class MetadataAccessServerConfigurationClient extends CohortMemberConfigurationClient
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretStoreProvider class name of the secrets store
     * @param secretStoreLocation location (networkAddress) of the secrets store
     * @param secretStoreCollection name of the collection of secrets to use to connect to the remote server
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessServerConfigurationClient(String   serverName,
                                                   String   serverPlatformRootURL,
                                                   String   secretStoreProvider,
                                                   String   secretStoreLocation,
                                                   String   secretStoreCollection,
                                                   String   delegatingUserId,
                                                   AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformRootURL, secretStoreProvider, secretStoreLocation, secretStoreCollection, delegatingUserId, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public MetadataAccessServerConfigurationClient(String                             serverPlatformRootURL,
                                                   Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                                   String                             delegatingUserId,
                                                   AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverPlatformRootURL, secretsStoreConnectorMap, delegatingUserId, auditLog);
    }


    /*
     * =============================================================
     * Learn about all possible access services
     */

    /**
     * Return the list of access services for this server.
     *
     * @return list of access service descriptions
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getRegisteredAccessServices() throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "getRegisteredAccessServices";
        final String urlTemplate = "/open-metadata/platform-services/server-platform/registered-services/access-services?delegatingUserId={0}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     delegatingUserId);
        return restResult.getServices();
    }


    /*
     * =============================================================
     * Retrieve the current state of the access service configuration
     */


    /**
     * Return the list of access services that are configured for this server.
     *
     * @return list of access service descriptions
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<RegisteredOMAGService> getConfiguredAccessServices() throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "getConfiguredAccessServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/access-services?delegatingUserId={1}";

        RegisteredOMAGServicesResponse restResult = restClient.callRegisteredOMAGServicesGetRESTCall(methodName,
                                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                                     serverName,
                                                                                                     delegatingUserId);
        return restResult.getServices();
    }


    /**
     * Return the configuration for the access services in this server.
     *
     * @return list of access service configuration
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public List<AccessServiceConfig> getAccessServicesConfiguration() throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             OMAGConfigurationErrorException
    {
        final String methodName  = "getAccessServicesConfiguration";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/access-services/configuration?delegatingUserId={1}";

        AccessServicesResponse restResult = restClient.callAccessServicesGetRESTCall(methodName,
                                                                                     serverPlatformRootURL + urlTemplate,
                                                                                     serverName,
                                                                                     delegatingUserId);
        return restResult.getServices();
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */


    /**
     * Enable a single access service.
     *
     * @param serviceURLMarker string indicating which access service it is configuring
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAccessService(String  serviceURLMarker) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        this.configureAccessService(serviceURLMarker, new HashMap<>());
    }


    /**
     * Disable a single access service.
     *
     * @param serviceURLMarker string indicating which access service it is disabling
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void disableAccessService(String  serviceURLMarker) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "disableAccessService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/access-services/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          serviceURLMarker,
                                          delegatingUserId);
    }


    /**
     * Enable a single access service.
     *
     * @param serviceURLMarker string indicating which access service it is configuring
     * @param accessServiceOptions property name/value pairs used to configure the access service
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAccessService(String              serviceURLMarker,
                                       Map<String, Object> accessServiceOptions) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "configureAccessService";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/access-services/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        accessServiceOptions,
                                        serverName,
                                        serviceURLMarker,
                                        delegatingUserId);
    }


    /**
     * Enable a single access service without the In and Out topic.
     *
     * @param serviceURLMarker string indicating which access service it is configuring
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAccessServiceNoTopics(String  serviceURLMarker) throws UserNotAuthorizedException,
                                                                                InvalidParameterException,
                                                                                OMAGConfigurationErrorException
    {
        this.configureAccessServiceNoTopics(serviceURLMarker, new HashMap<>());
    }


    /**
     * Enable a single access service without the In and Out topic.
     *
     * @param serviceURLMarker string indicating which access service it is configuring
     * @param accessServiceOptions property name/value pairs used to configure the access service
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAccessServiceNoTopics(String              serviceURLMarker,
                                               Map<String, Object> accessServiceOptions) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "configureAccessServiceNoTopics";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/access-services/{1}/no-topics?delegatingUserId={2}";

        invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        accessServiceOptions,
                                        serverName,
                                        serviceURLMarker,
                                        delegatingUserId);
    }


    /**
     * Enable all access services that are registered into this server.   The configuration properties
     * for each access service can be changed from their default using setAccessServicesConfig operation.
     *
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAllAccessServices(Map<String, Object> accessServiceOptions) throws UserNotAuthorizedException,
                                                                                            InvalidParameterException,
                                                                                            OMAGConfigurationErrorException
    {
        final String methodName  = "configureAllAccessServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/access-services?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        accessServiceOptions,
                                        serverName,
                                        delegatingUserId);
    }



    /**
     * Enable all access services that are registered into this server without the in and out topics activated.
     * The configuration properties for each access service can be changed from their default using
     * setAccessServicesConfig operation.
     *
     * @param accessServiceOptions  property name/value pairs used to configure the access services
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void configureAllAccessServicesNoTopics(Map<String, Object> accessServiceOptions) throws UserNotAuthorizedException,
                                                                                                    InvalidParameterException,
                                                                                                    OMAGConfigurationErrorException
    {
        final String methodName  = "configureAllAccessServicesNoTopics";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/access-services/no-topics?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        accessServiceOptions,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Retrieve the topic names for this access service
     *
     * @param serviceURLMarker string indicating which access service it requested
     *
     * @return map of topic names to descriptions
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Map<String, String> getAccessServiceTopicNames(String  serviceURLMarker) throws UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           OMAGConfigurationErrorException
    {
        final String methodName    = "getAccessServiceTopicNames";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/access-services/{1}/topic-names?delegatingUserId={2}";

        invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);

        StringMapResponse response = restClient.callStringMapGetRESTCall(methodName,
                                                                         serverPlatformRootURL + urlTemplate,
                                                                         serverName,
                                                                         serviceURLMarker,
                                                                         delegatingUserId);

        return response.getStringMap();
    }


    /**
     * Retrieve the topic names for all configured access service
     *
     * @return map of topic names to descriptions
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public Map<String, String> getAllAccessServiceTopicNames() throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      OMAGConfigurationErrorException
    {
        final String methodName    = "getAllAccessServiceTopicNames";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/access-services/topic-names?delegatingUserId={1}";

        StringMapResponse response = restClient.callStringMapGetRESTCall(methodName,
                                                                         serverPlatformRootURL + urlTemplate,
                                                                         serverName,
                                                                         delegatingUserId);

        return response.getStringMap();
    }



    /**
     * Override the topic name for the in topic of a single access service.
     *
     * @param serviceURLMarker string indicating which access service it is configuring
     * @param topicName new topic name
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideAccessServiceOutTopic(String  serviceURLMarker,
                                              String  topicName) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "overrideAccessServiceOutTopic";
        final String parameterName = "serviceURLMarker";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/access-services/{1}/topic-names/out-topic?delegatingUserId={2}";

        invalidParameterHandler.validateName(serviceURLMarker, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        serverName,
                                        serviceURLMarker,
                                        delegatingUserId);
    }


    /**
     * Disable the access services.  This removes all configuration for the access services
     * and disables the enterprise repository services.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearAllAccessServices() throws UserNotAuthorizedException,
                                                InvalidParameterException,
                                                OMAGConfigurationErrorException
    {
        final String methodName  = "clearAllAccessServices";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/access-services?delegatingUserId={1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          delegatingUserId);
    }


    /*
     * ===================================================================================
     * Configure access and enterprise services with advanced options overriding defaults
     */

    /**
     * Set up the configuration for all the open metadata access services (OMASs).  This overrides
     * the current values.
     *
     * @param accessServicesConfig - list of configuration properties for each access service.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setAccessServicesConfig(List<AccessServiceConfig> accessServicesConfig) throws UserNotAuthorizedException,
                                                                                               InvalidParameterException,
                                                                                               OMAGConfigurationErrorException
    {
        final String methodName  = "setAccessServicesConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/access-services/configuration?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        accessServicesConfig,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Set up the default remote enterprise topic.  This allows a remote process to monitor enterprise topic events.
     *
     * @param configurationProperties additional properties for the cohort
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addRemoteEnterpriseTopic(Map<String, Object>  configurationProperties) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              OMAGConfigurationErrorException
    {
        final String methodName  = "setEnterpriseAccessConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/enterprise-access/remote-topic?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        configurationProperties,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Set up the configuration that controls the enterprise repository services.  These services are part
     * of the Open Metadata Repository Services (OMRS).  They provide federated queries and federated event
     * notifications that cover metadata from the local repository plus any repositories connected via
     * open metadata repository cohorts.  These services are configured automatically when the access
     * services are enabled and so this call is not normally needed unless the default settings for
     * the enterprise access services are not sufficient.
     *
     * @param enterpriseAccessConfig - enterprise repository services configuration properties.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setEnterpriseAccessConfig(EnterpriseAccessConfig enterpriseAccessConfig) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                OMAGConfigurationErrorException
    {
        final String methodName  = "setEnterpriseAccessConfig";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/enterprise-access/configuration?delegatingUserId={1}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        enterpriseAccessConfig,
                                        serverName,
                                        delegatingUserId);
    }
}
