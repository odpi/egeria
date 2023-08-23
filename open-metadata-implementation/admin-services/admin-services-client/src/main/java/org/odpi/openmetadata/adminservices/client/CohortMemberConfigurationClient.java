/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.client;

import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortTopicStructure;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.properties.DedicatedTopicList;
import org.odpi.openmetadata.adminservices.rest.DedicatedTopicListResponse;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.StringResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

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
     * @param adminUserId           administrator's (end user's) userId to associate with calls.
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the admin services
     * @throws OMAGInvalidParameterException there is a problem creating the client-side components to issue any
     *                                       REST API calls.
     */
    public CohortMemberConfigurationClient(String adminUserId,
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
    public CohortMemberConfigurationClient(String adminUserId,
                                           String serverName,
                                           String serverPlatformRootURL,
                                           String connectionUserId,
                                           String connectionPassword) throws OMAGInvalidParameterException
    {
        super(adminUserId, serverName, serverPlatformRootURL, connectionUserId, connectionPassword);
    }


    /*
     * =============================================================
     * Configure server making maximum use of defaults
     */

    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (SINGLE_TOPIC).
     *
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param cohortName  name of the cohort.
     * @param additionalProperties additional properties for the event bus connection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addCohortRegistration(String              cohortName,
                                      Map<String, Object> additionalProperties) throws OMAGNotAuthorizedException,
                                                                                       OMAGInvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName    = "addCohortRegistration";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        Map<String, Object>  requestBody = additionalProperties;
        if (requestBody == null)
        {
            requestBody = new HashMap<>();
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        cohortName);
    }


    /**
     * Enable registration of server to an open metadata repository cohort using the topic pattern specified by cohortTopicStructure.
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param cohortName  name of the cohort
     * @param cohortTopicStructure the style of cohort topic set up to use
     * @param additionalProperties additional properties for the event bus connection
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command
     * @throws OMAGInvalidParameterException invalid parameter
     * @throws OMAGConfigurationErrorException unusual state in the admin server
     */
    public void addCohortRegistration(String               cohortName,
                                      CohortTopicStructure cohortTopicStructure,
                                      Map<String, Object>  additionalProperties) throws OMAGNotAuthorizedException,
                                                                                        OMAGInvalidParameterException,
                                                                                        OMAGConfigurationErrorException
    {
        final String methodName    = "addCohortRegistration";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/topic-structure/{3}";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        Map<String, Object>  requestBody = additionalProperties;
        if (requestBody == null)
        {
            requestBody = new HashMap<>();
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName,
                                        cohortName,
                                        cohortTopicStructure);
    }


    /**
     * Retrieve the current topic name for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @return string topic name
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public String getCohortTopicName(String cohortName) throws OMAGNotAuthorizedException,
                                                               OMAGInvalidParameterException,
                                                               OMAGConfigurationErrorException
    {
        final String methodName          = "getCohortTopicName";
        final String parameterName       = "cohortName";
        final String urlTemplate         = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/topic-name";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        StringResponse response = restClient.callStringGetRESTCall(methodName,
                                                                   serverPlatformRootURL + urlTemplate,
                                                                   adminUserId,
                                                                   serverName,
                                                                   cohortName);

        return response.getResultString();
    }


    /**
     * Retrieve the current topic names for the three dedicated topics of the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @return List of topic names - registration first, then types and then instances
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public DedicatedTopicList getDedicatedCohortTopicNames(String cohortName) throws OMAGNotAuthorizedException,
                                                                                     OMAGInvalidParameterException,
                                                                                     OMAGConfigurationErrorException
    {
        final String methodName    = "getDedicatedCohortTopicNames";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/dedicated-topic-names";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        DedicatedTopicListResponse response = restClient.callDedicatedTopicListGetRESTCall(methodName,
                                                                                           serverPlatformRootURL + urlTemplate,
                                                                                           adminUserId,
                                                                                           serverName,
                                                                                           cohortName);

        return response.getDedicatedTopicList();
    }


    /**
     * Override the current topic name for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideCohortTopicName(String cohortName,
                                        String topicName) throws OMAGNotAuthorizedException,
                                                                 OMAGInvalidParameterException,
                                                                 OMAGConfigurationErrorException
    {
        final String methodName          = "overrideCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/topic-name-override";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
            invalidParameterHandler.validateName(topicName, topicParameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        adminUserId,
                                        serverName,
                                        cohortName);
    }


    /**
     * Override the current topic name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideRegistrationCohortTopicName(String cohortName,
                                                    String topicName) throws OMAGNotAuthorizedException,
                                                                             OMAGInvalidParameterException,
                                                                             OMAGConfigurationErrorException
    {
        final String methodName          = "overrideRegistrationCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/topic-name-override/registration";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
            invalidParameterHandler.validateName(topicName, topicParameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        adminUserId,
                                        serverName,
                                        cohortName);
    }


    /**
     * Override the current topic name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideTypesCohortTopicName(String cohortName,
                                             String topicName) throws OMAGNotAuthorizedException,
                                                                      OMAGInvalidParameterException,
                                                                      OMAGConfigurationErrorException
    {
        final String methodName          = "overrideTypesCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/topic-name-override/types";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
            invalidParameterHandler.validateName(topicName, topicParameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        adminUserId,
                                        serverName,
                                        cohortName);
    }



    /**
     * Override the current topic name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with addCohortRegistration().
     *
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void overrideInstancesCohortTopicName(String cohortName,
                                                 String topicName) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName          = "overrideInstancesCohortTopicName";
        final String parameterName       = "cohortName";
        final String topicParameterName  = "topicName";
        final String urlTemplate         = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/topic-name-override/instances";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
            invalidParameterHandler.validateName(topicName, topicParameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        topicName,
                                        adminUserId,
                                        serverName,
                                        cohortName);
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
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setCohortConfig(String       cohortName,
                                CohortConfig cohortConfig) throws OMAGNotAuthorizedException,
                                                                  OMAGInvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName    = "setCohortConfig";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}/configuration";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        cohortConfig,
                                        adminUserId,
                                        serverName,
                                        cohortName);
    }


    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param cohortName  name of the cohort.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearCohortRegistration(String cohortName) throws OMAGNotAuthorizedException,
                                                                  OMAGInvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName    = "clearCohortRegistration";
        final String parameterName = "cohortName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/cohorts/{2}";

        try
        {
            invalidParameterHandler.validateName(cohortName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName,
                                          cohortName);
    }


    /*
     * ===================================================================================
     * Configure open metadata archives to load at server startup
     */


    /**
     * Add a new open metadata archive file to load at startup.
     *
     * @param fileName name of the open metadata archive file.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addStartUpOpenMetadataArchiveFile(String fileName) throws OMAGNotAuthorizedException,
                                                                          OMAGInvalidParameterException,
                                                                          OMAGConfigurationErrorException
    {
        final String methodName    = "addStartUpOpenMetadataArchiveFile";
        final String parameterName = "fileName";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/open-metadata-archives/file";

        try
        {
            invalidParameterHandler.validateName(fileName, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        fileName,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Add a list of connections to connectors that can access an open metadata archive file.
     *
     * @param connections list of connections to open metadata archive connectors
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void addStartUpOpenMetadataArchiveList(List<Connection> connections) throws OMAGNotAuthorizedException,
                                                                                       OMAGInvalidParameterException,
                                                                                       OMAGConfigurationErrorException
    {
        final String methodName    = "addStartUpOpenMetadataArchiveFile";
        final String parameterName = "connections";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/open-metadata-archives";

        try
        {
            invalidParameterHandler.validateObject(connections, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        connections,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Clear the list of open metadata archives for loading at server startup.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearOpenMetadataArchives() throws OMAGNotAuthorizedException,
                                                   OMAGInvalidParameterException,
                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "clearOpenMetadataArchives";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/open-metadata-archives";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /*
     * =======================================================================
     * Working with local repository config - used for advanced set up
     */


    /**
     * Set up the configuration for the local repository in one operation.  This overrides the current values.
     *
     * @param localRepositoryConfig - configuration properties for the local repository.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    protected void setLocalRepositoryConfig(LocalRepositoryConfig localRepositoryConfig) throws OMAGNotAuthorizedException,
                                                                                                OMAGInvalidParameterException,
                                                                                                OMAGConfigurationErrorException
    {
        final String methodName    = "setEventMapperConnection";
        final String parameterName = "localRepositoryConfig";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/configuration";

        try
        {
            invalidParameterHandler.validateObject(localRepositoryConfig, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        localRepositoryConfig,
                                        adminUserId,
                                        serverName);
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.  This call effectively sets the
     * type of a metadata server back to a metadata access point.
     *
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void clearLocalRepository() throws OMAGNotAuthorizedException,
                                              OMAGInvalidParameterException,
                                              OMAGConfigurationErrorException
    {
        final String methodName  = "clearLocalRepository";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository";

        restClient.callVoidDeleteRESTCall(methodName,
                                          serverPlatformRootURL + urlTemplate,
                                          adminUserId,
                                          serverName);
    }


    /**
     * Update the URL broadcast across the cohort to allow other members to issue queries to this repository.
     * This method is needed to reconfigure a server that has moved from one platform to another.  Once the
     * URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.
     *
     * @param serverURLRoot  String url.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void resetRemoteCohortURL(String serverURLRoot) throws OMAGNotAuthorizedException,
                                                                  OMAGInvalidParameterException,
                                                                  OMAGConfigurationErrorException
    {
        final String methodName    = "resetRemoteCohortURL";
        final String parameterName = "serverURLRoot";
        final String urlTemplate   = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/configuration/remote-repository-connector-url";

        try
        {
            invalidParameterHandler.validateName(serverURLRoot, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        URLRequestBody requestBody = new URLRequestBody();

        requestBody.setUrlRoot(serverURLRoot);
        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        requestBody,
                                        adminUserId,
                                        serverName);
    }


    /*
     * =======================================================================
     * Working with the metadata collections managed by cohort members
     */


    /**
     * Set up the local metadata collection name.  If a null is passed, the connection name is cleared to null.
     * If this is not set then the default value is the local server name.
     *
     * @param name  metadata collection name.
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setLocalMetadataCollectionName(String name) throws OMAGNotAuthorizedException,
                                                                   OMAGInvalidParameterException,
                                                                   OMAGConfigurationErrorException
    {
        final String methodName  = "setLocalMetadataCollectionName";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/metadata-collection-name/{2}";

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        nullRequestBody,
                                        adminUserId,
                                        serverName,
                                        name);
    }


    /**
     * Return the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @return string guid
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public String getLocalMetadataCollectionId() throws OMAGNotAuthorizedException,
                                                        OMAGInvalidParameterException,
                                                        OMAGConfigurationErrorException
    {
        final String methodName  = "getLocalMetadataCollectionId";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/metadata-collection-id";

        GUIDResponse restResult = restClient.callGUIDGetRESTCall(methodName,
                                                                 serverPlatformRootURL + urlTemplate,
                                                                 adminUserId,
                                                                 serverName);
        return restResult.getGUID();
    }


    /**
     * Set up the local metadata collection id.  If the local repository is not configured
     * then the invalid parameter exception is returned.
     *
     * @param metadataCollectionId unique identifier for the metadata collection
     * @throws OMAGNotAuthorizedException the supplied userId is not authorized to issue this command.
     * @throws OMAGInvalidParameterException invalid parameter.
     * @throws OMAGConfigurationErrorException unusual state in the admin server.
     */
    public void setLocalMetadataCollectionId(String metadataCollectionId) throws OMAGNotAuthorizedException,
                                                                                 OMAGInvalidParameterException,
                                                                                 OMAGConfigurationErrorException
    {
        final String methodName  = "setLocalMetadataCollectionId";
        final String parameterName = "metadataCollectionId";
        final String urlTemplate = "/open-metadata/admin-services/users/{0}/servers/{1}/local-repository/metadata-collection-id";

        try
        {
            invalidParameterHandler.validateGUID(metadataCollectionId, parameterName, methodName);
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGInvalidParameterException(error.getReportedErrorMessage(), error);
        }

        restClient.callVoidPostRESTCall(methodName,
                                        serverPlatformRootURL + urlTemplate,
                                        metadataCollectionId,
                                        adminUserId,
                                        serverName);
    }
}
