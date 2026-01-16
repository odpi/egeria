/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.properties.DedicatedTopicList;
import org.odpi.openmetadata.adminservices.rest.DedicatedTopicListResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CohortMemberConfigurationClient provides the interfaces to add an OMAG Server to an Open Metadata Repository Cohort.
 */
public class CohortMemberConfigurationClient extends OMAGServerConfigurationClient
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
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public CohortMemberConfigurationClient(String   serverName,
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
     * @throws InvalidParameterException a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public CohortMemberConfigurationClient(String                             serverPlatformRootURL,
                                           Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                           String                             delegatingUserId,
                                           AuditLog                           auditLog) throws InvalidParameterException
    {
        super(serverPlatformRootURL, secretsStoreConnectorMap, delegatingUserId, auditLog);
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */

    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (SINGLE_TOPIC).
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param cohortName  name of the cohort.
     * @param additionalProperties additional properties for the event bus connection
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addCohortRegistration(String              cohortName,
                                      Map<String, Object> additionalProperties) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName    = "addCohortRegistration";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/cohorts/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(cohortName, parameterName, methodName);

        Map<String, Object>  requestBody = additionalProperties;
        if (requestBody == null)
        {
            requestBody = new HashMap<>();
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        cohortName,
                                        delegatingUserId);
    }



    /**
     * Retrieve the current topic names for the three dedicated topics of the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @return List of topic names - registration first, then types and then instances
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public DedicatedTopicList getCohortTopicNames(String cohortName) throws UserNotAuthorizedException,
                                                                            InvalidParameterException,
                                                                            OMAGConfigurationErrorException
    {
        final String methodName    = "getCohortTopicNames";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/cohorts/{1}/topic-names?delegatingUserId={2}";

        invalidParameterHandler.validateName(cohortName, parameterName, methodName);

        DedicatedTopicListResponse response = restClient.callDedicatedTopicListGetRESTCall(methodName,
                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                           serverName,
                                                                                           cohortName,
                                                                                           delegatingUserId);

        return response.getDedicatedTopicList();
    }


    /**
     * Override the current topic name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideRegistrationCohortTopicName(String cohortName,
                                                    String topicName) throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             OMAGConfigurationErrorException
    {
        final String methodName          = "overrideRegistrationCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/servers/{0}/cohorts/{1}/topic-name-override/registration?delegatingUserId={2}";


        invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        invalidParameterHandler.validateName(topicName, topicParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        serverName,
                                        cohortName,
                                        delegatingUserId);
    }


    /**
     * Override the current topic name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideTypesCohortTopicName(String cohortName,
                                             String topicName) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      OMAGConfigurationErrorException
    {
        final String methodName          = "overrideTypesCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/servers/{0}/cohorts/{1}/topic-name-override/types?delegatingUserId={2}";

        invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        invalidParameterHandler.validateName(topicName, topicParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        serverName,
                                        cohortName,
                                        delegatingUserId);
    }



    /**
     * Override the current topic name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideInstancesCohortTopicName(String cohortName,
                                                 String topicName) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName          = "overrideInstancesCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/servers/{0}/cohorts/{1}/topic-name-override/instances?delegatingUserId={2}";

        invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        invalidParameterHandler.validateName(topicName, topicParameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        serverName,
                                        cohortName,
                                        delegatingUserId);
    }


    /*
     * =============================================================
     * Configure cohort with advanced options overriding defaults
     */

    /**
     * Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a
     * cohort.  Use setCohortMode to delete a cohort.
     *
     * @param cohortName name of the cohort
     * @param cohortConfig configuration for the cohort
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setCohortConfig(String       cohortName,
                                CohortConfig cohortConfig) throws UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName    = "setCohortConfig";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/cohorts/{1}/configuration?delegatingUserId={2}";

        invalidParameterHandler.validateName(cohortName, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        cohortConfig,
                                        serverName,
                                        cohortName,
                                        delegatingUserId);
    }


    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param cohortName  name of the cohort.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearCohortRegistration(String cohortName) throws UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName    = "clearCohortRegistration";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/cohorts/{1}?delegatingUserId={2}";

        invalidParameterHandler.validateName(cohortName, parameterName, methodName);

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          cohortName,
                                          delegatingUserId);
    }


    /*
     * ===================================================================================
     * Configure open metadata archives to load at server startup
     */


    /**
     * Add a new open metadata archive file to load at startup.
     *
     * @param fileName name of the open metadata archive file.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addStartUpOpenMetadataArchiveFile(String fileName) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "addStartUpOpenMetadataArchiveFile";
        final String parameterName = "fileName";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/open-metadata-archives/file?delegatingUserId={1}";

        invalidParameterHandler.validateName(fileName, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        fileName,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Add a list of connections to connectors that can access an open metadata archive file.
     *
     * @param connections list of connections to open metadata archive connectors
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addStartUpOpenMetadataArchiveList(List<Connection> connections) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName    = "addStartUpOpenMetadataArchiveFile";
        final String parameterName = "connections";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/open-metadata-archives?delegatingUserId={1}";

        invalidParameterHandler.validateObject(connections, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connections,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Clear the list of open metadata archives for loading at server startup.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearOpenMetadataArchives() throws UserNotAuthorizedException,
                                                   InvalidParameterException,
                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "clearOpenMetadataArchives";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/open-metadata-archives?delegatingUserId={1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          delegatingUserId);
    }


    /*
     * =======================================================================
     * Working with local repository config - used for advanced set up
     */


    /**
     * Set up the configuration for the local repository in one operation.  This overrides the current values.
     *
     * @param localRepositoryConfig - configuration properties for the local repository.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    protected void setLocalRepositoryConfig(LocalRepositoryConfig localRepositoryConfig) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "setEventMapperConnection";
        final String parameterName = "localRepositoryConfig";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/local-repository/configuration?delegatingUserId={1}";

        invalidParameterHandler.validateObject(localRepositoryConfig, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        localRepositoryConfig,
                                        serverName,
                                        delegatingUserId);
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.  This call effectively sets the
     * type of a metadata server back to a metadata access point.
     *
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearLocalRepository() throws UserNotAuthorizedException,
                                              InvalidParameterException,
                                              OMAGConfigurationErrorException
    {
        final String methodName  = "clearLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository?delegatingUserId={1}";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          serverName,
                                          delegatingUserId);
    }


    /**
     * Update the URL broadcast across the cohort to allow other members to issue queries to this repository.
     * This method is needed to reconfigure a server that has moved from one platform to another.  Once the
     * URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.
     *
     * @param serverURLRoot  String url.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void resetRemoteCohortURL(String serverURLRoot) throws UserNotAuthorizedException,
                                                                  InvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName    = "resetRemoteCohortURL";
        final String parameterName = "serverURLRoot";
        final String urlTemplate   = "/open-metadata/admin-services/servers/{0}/local-repository/configuration/remote-repository-connector-url?delegatingUserId={1}";

        invalidParameterHandler.validateName(serverURLRoot, parameterName, methodName);

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(serverURLRoot);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        serverName,
                                        delegatingUserId);
    }


    /*
     * =======================================================================
     * Working with the metadata collections managed by cohort members
     */


    /**
     * Return the local metadata collection name.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @return string guid
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public String getLocalMetadataCollectionName() throws UserNotAuthorizedException,
                                                          InvalidParameterException,
                                                          OMAGConfigurationErrorException
    {
        final String methodName  = "getLocalMetadataCollectionName";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/metadata-collection-name?delegatingUserId={1}";

        StringResponse restResult = restClient.callStringGetRESTCall(methodName,
                                                                     serverPlatformRootURL + urlTemplate,
                                                                     serverName,
                                                                     delegatingUserId);
        return restResult.getResultString();
    }


    /**
     * Set up the local metadata collection name.  If a null is passed, the connection name is cleared to null.
     * If this is not set then the default value is the local server name.
     *
     * @param name  metadata collection name.
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setLocalMetadataCollectionName(String name) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "setLocalMetadataCollectionName";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/metadata-collection-name/{1}?delegatingUserId={2}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        name,
                                        delegatingUserId);
    }


    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @return string guid
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public String getLocalMetadataCollectionId() throws UserNotAuthorizedException,
                                                        InvalidParameterException,
                                                        OMAGConfigurationErrorException
    {
        final String methodName  = "getLocalMetadataCollectionId";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/metadata-collection-id?delegatingUserId={1}";

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 serverPlatformRootURL + urlTemplate,
                                                                 serverName,
                                                                 delegatingUserId);
        return restResult.getGUID();
    }


    /**
     * Set up the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param metadataCollectionId unique identifier for the metadata collection
     * @throws UserNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws InvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setLocalMetadataCollectionId(String metadataCollectionId) throws UserNotAuthorizedException,
                                                                                 InvalidParameterException,
                                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "setLocalMetadataCollectionId";
        final String parameterName = "metadataCollectionId";
        final String urlTemplate = "/open-metadata/admin-services/servers/{0}/local-repository/metadata-collection-id?delegatingUserId={1}";

        invalidParameterHandler.validateGUID(metadataCollectionId, parameterName, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        metadataCollectionId,
                                        serverName,
                                        delegatingUserId);
    }
}