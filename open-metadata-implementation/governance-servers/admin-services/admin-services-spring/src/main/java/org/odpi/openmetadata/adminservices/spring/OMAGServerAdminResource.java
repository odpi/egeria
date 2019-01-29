/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import org.odpi.openmetadata.adminservices.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * OMAGServerAdminServices provides part of the server-side implementation of the administrative interface for
 * an Open Metadata and Governance (OMAG) Server.  In particular, this resource supports the configuration
 * of the server name, server type and organization name.  It also supports the setting up of the
 * Open Metadata Repository Services' local repository and cohort.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")
public class  OMAGServerAdminResource
{
    private OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();


    /*
     * =============================================================
     * Configure server - basic options using defaults
     */


    /**
     * Set up the descriptive type of the server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is "Open Metadata and Governance Server".
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param typeName  short description for the type of server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterE
     * xception invalid serverName or serverType parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-type")
    public VoidResponse setServerType(@PathVariable String userId,
                                      @PathVariable String serverName,
                                      @RequestParam String typeName)
    {
        return adminAPI.setServerType(userId, serverName, typeName);
    }


    /**
     * Set up the name of the organization that is running this server.  This value is added to distributed events to
     * make it easier to understand the source of events.  The default value is null.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param name  String name of the organization.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or organizationName parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/organization-name")
    public VoidResponse setOrganizationName(@PathVariable String userId,
                                            @PathVariable String serverName,
                                            @RequestParam String name)
    {
        return adminAPI.setOrganizationName(userId, serverName, name);
    }


    /**
     * Set up the user id to use when there is no external user driving the work (for example when processing events
     * from another server).
     *
     * @param userId - user that is issuing the request.
     * @param serverName - local server name.
     * @param id - String user is for the server.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-user-id")
    public VoidResponse setServerUserId(@PathVariable String userId,
                                        @PathVariable String serverName,
                                        @RequestParam String id)
    {
        return adminAPI.setServerUserId(userId, serverName, id);
    }


    /**
     * Set an upper limit in the page size that can be requested on a REST call to the server.  The default
     * value is 1000.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param limit  max number of elements that can be returned on a request.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or maxPageSize parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/max-page-size")
    public VoidResponse setMaxPageSize(@PathVariable String  userId,
                                       @PathVariable String  serverName,
                                       @RequestParam int     limit)
    {
        return adminAPI.setMaxPageSize(userId, serverName, limit);
    }


    /**
     * Set up the default event bus for embedding in event-driven connector.   The resulting connector will
     * be used in the OMRS Topic Connector for each cohort, the in and out topics for each Access Service and
     * the local repositories event mapper.
     *
     * When the event bus is configured, it is used only on future configuration.  It does not effect
     * existing configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName local server name.
     * @param connectorProvider  connector provider for the event bus (if it is null then Kafka is assumed).
     * @param topicURLRoot the common root of the topics used by the open metadata server.
     * @param additionalProperties  property name/value pairs used to configure the connection to the event bus connector
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException it is too late to configure the event bus - other configuration already exists or
     * OMAGInvalidParameterException invalid serverName or serviceMode parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/event-bus")
    public VoidResponse setEventBus(@PathVariable                   String              userId,
                                    @PathVariable                   String              serverName,
                                    @RequestParam(required = false) String              connectorProvider,
                                    @RequestParam(required = false) String              topicURLRoot,
                                    @RequestBody (required = false) Map<String, Object> additionalProperties)
    {
        return adminAPI.setEventBus(userId, serverName, connectorProvider, topicURLRoot, additionalProperties);
    }


    /*
     * =============================================================
     * Configure local repository
     */


    /**
     Set up the root URL for this server that is used to construct full URL paths to calls for
     * this server's REST interfaces that is used by other members of the cohorts that this server
     * connects to.
     *
     * The default value is "localhost:8080".
     *
     * ServerURLRoot is used during the configuration of the local repository.  If called
     * after the local repository is configured, it has no effect.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param url  String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/server-url-root")
    public VoidResponse setServerURLRoot(@PathVariable String userId,
                                         @PathVariable String serverName,
                                         @RequestParam String url)
    {
        return adminAPI.setServerURLRoot(userId, serverName, url);
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/mode/in-memory-repository")
    public VoidResponse setInMemLocalRepository(@PathVariable String userId,
                                                @PathVariable String serverName)
    {
        return adminAPI.setInMemLocalRepository(userId, serverName);
    }


    /**
     * Set up a graph store as the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param additionalProperties additional parameters to pass to the repository connector
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/mode/local-graph-repository")
    public VoidResponse setGraphLocalRepository(@PathVariable                  String userId,
                                                @PathVariable                  String serverName,
                                                @RequestBody(required = false) Map<String,Object>  additionalProperties)
    {
        return adminAPI.setGraphLocalRepository(userId, serverName, additionalProperties);
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/local-repository")
    public VoidResponse setNoRepositoryMode(@PathVariable String userId,
                                            @PathVariable String serverName)
    {
        return adminAPI.setNoRepositoryMode(userId, serverName);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/mode/repository-proxy/connection")
    public VoidResponse setRepositoryProxyConnection(@PathVariable String     userId,
                                                     @PathVariable String     serverName,
                                                     @RequestBody  Connection connection)
    {
        return adminAPI.setRepositoryProxyConnection(userId, serverName, connection);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param userId   user that is issuing the request.
     * @param serverName   local server name.
     * @param connectorProvider  connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/mode/repository-proxy/details")
    public VoidResponse setRepositoryProxyConnection(@PathVariable                   String               userId,
                                                     @PathVariable                   String               serverName,
                                                     @RequestParam                   String               connectorProvider,
                                                     @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setRepositoryProxyConnection(userId, serverName, connectorProvider, additionalProperties);
    }


    /**
     * Provide the connection to an IBM IGC repository - used when the local repository mode is set to IBM IGC.
     *
     * @param userId   user that is issuing the request.
     * @param serverName   IBM IGC server name.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * OMAGNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/mode/ibm-igc/details")
    public VoidResponse setIBMIGCConnection(@PathVariable                  String               userId,
                                            @PathVariable                  String               serverName,
                                            @RequestBody(required = true)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setIBMIGCConnection(userId, serverName, additionalProperties);
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository event mapper.
     * @return void response
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the local repository mode, or the event mapper has not been set
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/event-mapper-connection")
    public VoidResponse setLocalRepositoryEventMapper(@PathVariable String     userId,
                                                      @PathVariable String     serverName,
                                                      @RequestBody  Connection connection)
    {
        return adminAPI.setLocalRepositoryEventMapper(userId, serverName, connection);
    }


    /**
     * Provide the connection to the local repository's event mapper if needed.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param connectorProvider           Java class name of the connector provider for the OMRS repository event mapper.
     * @param eventSource                 topic name or URL to the native event source.
     * @param additionalProperties        additional properties for the event mapper connection
     * @return void response or
     * OMAGNotAuthorizedException    the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/event-mapper-details")
    public VoidResponse setLocalRepositoryEventMapper(@PathVariable                 String               userId,
                                                      @PathVariable                 String               serverName,
                                                      @RequestParam                 String               connectorProvider,
                                                      @RequestParam                 String               eventSource,
                                                      @RequestBody(required=false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setLocalRepositoryEventMapper(userId, serverName, connectorProvider, eventSource, additionalProperties);
    }


    /**
     * Set up the local metadata collection name.  If this is not set then the default value is the
     * local server name.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param name                        metadata collection name.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/metadata-collection-name/{name}")

    public VoidResponse setLocalMetadataCollectionName(@PathVariable  String               userId,
                                                       @PathVariable  String               serverName,
                                                       @PathVariable  String               name)
    {
        return adminAPI.setLocalMetadataCollectionName(userId, serverName, name);
    }


    /**
     * Enable registration of server to an open metadata repository cohort.  This is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param additionalProperties additional properties for the event bus connection
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/cohorts/{cohortName}")
    public VoidResponse enableCohortRegistration(@PathVariable                   String               userId,
                                                 @PathVariable                   String               serverName,
                                                 @PathVariable                   String               cohortName,
                                                 @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.enableCohortRegistration(userId, serverName, cohortName, additionalProperties);
    }


    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/cohorts/{cohortName}")
    public VoidResponse disableCohortRegistration(@PathVariable String          userId,
                                                  @PathVariable String          serverName,
                                                  @PathVariable String          cohortName)
    {
        return adminAPI.disableCohortRegistration(userId, serverName, cohortName);
    }


    /*
     * =============================================================
     * Configure server - advanced options overriding defaults
     */


    /**
     * Set up the configuration for the local repository.  This overrides the current values.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param localRepositoryConfig  configuration properties for the local repository.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/local-repository/configuration")
    public VoidResponse setLocalRepositoryConfig(@PathVariable String                userId,
                                                 @PathVariable String                serverName,
                                                 @RequestBody  LocalRepositoryConfig localRepositoryConfig)
    {
        return adminAPI.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
    }


    /**
     * Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a
     * cohort.  Use setCohortMode to delete a cohort.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @param cohortName  name of the cohort
     * @param cohortConfig  configuration for the cohort
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or cohortConfig parameter.
     */
    @RequestMapping(method = RequestMethod.POST, path = "/cohorts/{cohortName}/configuration")
    public VoidResponse setCohortConfig(@PathVariable String       userId,
                                        @PathVariable String       serverName,
                                        @PathVariable String       cohortName,
                                        @RequestBody  CohortConfig cohortConfig)
    {
        return adminAPI.setCohortConfig(userId, serverName, cohortName, cohortConfig);
    }


    /*
     * =============================================================
     * Query current configuration
     */


    /**
     * Return the stored configuration document for the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @RequestMapping(method = RequestMethod.GET, path = "/configuration")
    public OMAGServerConfigResponse getCurrentConfiguration(@PathVariable String userId,
                                                            @PathVariable String serverName)
    {
        return adminAPI.getStoredConfiguration(userId, serverName);
    }

}
