package org.odpi.openmetadata.viewservices.serverauthor.server.spring;/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.SupportedAuditLogSeveritiesResponse;
import org.odpi.openmetadata.viewservices.serverauthor.services.ServerAuthorViewRESTServices;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ConfigRepositoryServicesResource provides the configuration services for setting
 * up the repository services subsystems.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/view-services/server-author/users/{userId}/servers/{serverToBeConfiguredName}")

@Tag(name="View Server: Server Author OMVS", description="The Server Author OMVS is for user interfaces supporting the creating and editing of OMAG Server Configuration Documents.",
     externalDocs=@ExternalDocumentation(description="Further information",
                                         url="https://egeria-project.org/services/omvs/server-author/overview"))

class ConfigRepositoryServicesViewResource {
    private final ServerAuthorViewRESTServices serverAPI = new ServerAuthorViewRESTServices();

    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param requestBody              null request body
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @SuppressWarnings(value = "unused")
    @PostMapping(path = "/local-repository/mode/in-memory-repository")
    public FFDCResponseBase setInMemLocalRepository(@PathVariable String userId,
                                                    @PathVariable String serverName,
                                                    @PathVariable String serverToBeConfiguredName,
                                                    @RequestBody(required = false) NullRequestBody requestBody) {
        return serverAPI.setInMemLocalRepository(userId, serverName, serverToBeConfiguredName);
    }


    /**
     * Set up a graph store as the local repository.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param storageProperties        properties used to configure the back end storage for the graph
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/local-graph-repository")
    public FFDCResponseBase setGraphLocalRepository(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @PathVariable String serverToBeConfiguredName,
                                               // @RequestBody Map<String, Object> storageProperties)
    // TODO resolve Nullable
     @RequestBody @Nullable Map<String, Object> storageProperties)
    {
        return serverAPI.setGraphLocalRepository(userId, serverName, serverToBeConfiguredName, storageProperties);
    }


    /**
     * Set up a read only local repository.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/read-only-repository")
    public FFDCResponseBase setGraphLocalRepository(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @PathVariable String serverToBeConfiguredName) {
        return serverAPI.setReadOnlyLocalRepository(userId, serverName, serverToBeConfiguredName);
    }
    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    @PostMapping(path = "/local-repository/mode/plugin-repository/connection")
    public FFDCResponseBase setPluginRepositoryConnection(@PathVariable String     userId,
                                                      @PathVariable String     serverName,
                                                      @PathVariable String     serverToBeConfiguredName,
                                                      @RequestBody  Connection connection)
    {
        return serverAPI.setPluginRepositoryConnection(userId, serverName, serverToBeConfiguredName, connection);
    }

    /*
     * =============================================================
     * Configure basic options using defaults
     */

    /**
     * Set up the default audit log for the server.  This adds the console audit log destination.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/default")
    public ServerAuthorConfigurationResponse setDefaultAuditLog(@PathVariable String userId,
                                                                @PathVariable String serverName,
                                                                @PathVariable String serverToBeConfiguredName) {
        return serverAPI.setDefaultAuditLog(userId, serverName, serverToBeConfiguredName);
    }

    /**
     * Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/console")
    public ServerAuthorConfigurationResponse addConsoleAuditLogDestination(@PathVariable String userId,
                                                      @PathVariable String serverName,
                                                      @PathVariable String serverToBeConfiguredName,
                                                      @RequestBody List<String> supportedSeverities) {
        return serverAPI.addConsoleAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates slf4j records.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/slf4j")
    public ServerAuthorConfigurationResponse addSLF4JAuditLogDestination(@PathVariable String userId,
                                                    @PathVariable String serverName,
                                                    @PathVariable String serverToBeConfiguredName,
                                                    @RequestBody List<String> supportedSeverities) {
        return serverAPI.addSLF4JAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/files")
    public ServerAuthorConfigurationResponse addFileAuditLogDestination(@PathVariable String userId,
                                                   @PathVariable String serverName,
                                                   @PathVariable String serverToBeConfiguredName,
                                                   @RequestBody List<String> supportedSeverities) {
        return serverAPI.addFileAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param supportedSeverities      list of severities that should be logged to this destination (empty list means all)
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/event-topic")
    public ServerAuthorConfigurationResponse addEventTopicAuditLogDestination(@PathVariable String userId,
                                                         @PathVariable String serverName,
                                                         @PathVariable String serverToBeConfiguredName,
                                                         @RequestBody List<String> supportedSeverities) {
        return serverAPI.addEventTopicAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
    }


    /**
     * Add an audit log destination that is defined by the supplied connection object.
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param connection               connection object that defines the audit log destination
     * @return the current stored configuration or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/connection")
    public ServerAuthorConfigurationResponse addAuditLogDestination(@PathVariable String userId,
                                               @PathVariable String serverName,
                                               @PathVariable String serverToBeConfiguredName,
                                               @RequestBody Connection connection) {
        return serverAPI.addAuditLogDestination(userId, serverName, serverToBeConfiguredName, connection);
    }
    /**
     * Update an audit log destination that is identified with the supplied destination name with
     * the supplied connection object.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param auditLogDestinationName name of the audit log destination to be updated
     * @param auditLogDestination connection object that defines the audit log destination
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PutMapping(path = "/audit-log-destinations/connection/{auditLogDestinationName}")
    public ServerAuthorConfigurationResponse updateAuditLogDestination(@PathVariable String     userId,
                                                  @PathVariable String     serverName,
                                                  @PathVariable String     serverToBeConfiguredName,
                                                  @PathVariable String     auditLogDestinationName,
                                                  @RequestBody  Connection auditLogDestination)
    {
        return serverAPI.updateAuditLogDestination(userId, serverName, serverToBeConfiguredName, auditLogDestinationName, auditLogDestination);
    }
    /**
     * Delete an audit log destination that is identified with the supplied destination name
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @param auditLogDestinationName name of the audit log destination to be deleted
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @DeleteMapping(path = "/audit-log-destinations/connection/{auditLogDestinationName}")
    public ServerAuthorConfigurationResponse deleteAuditLogDestination(@PathVariable String     userId,
                                                  @PathVariable String     serverName,
                                                  @PathVariable String     serverToBeConfiguredName,
                                                  @PathVariable String     auditLogDestinationName)
    {
        return serverAPI.deleteAuditLogDestination(userId, serverName, serverToBeConfiguredName, auditLogDestinationName);
    }

    /**
     * Get the audit log supported severities for the server being configured
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return a list of supported audit log severities
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @GetMapping(path = "/audit-log-destinations")
    public SupportedAuditLogSeveritiesResponse getAuditLogDestinationSupportedSeverities(@PathVariable String userId,
                                                                                         @PathVariable String serverName,
                                                                                         @PathVariable String serverToBeConfiguredName) {
        return serverAPI.getAuditLogDestinationSupportedSeverities(userId, serverName, serverToBeConfiguredName);
    }

    /**
     * Clear  the audit log destinations associated with the server being configured
     *
     * @param userId                   user that is issuing the request.
     * @param serverName               local server name.
     * @param serverToBeConfiguredName name of the server to be configured.
     * @return a list of supported audit log severities
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @DeleteMapping(path = "/audit-log-destinations")
    public FFDCResponseBase deleteAuditLogDestinationSupportedSeverities(@PathVariable String userId,
                                                                                         @PathVariable String serverName,
                                                                                         @PathVariable String serverToBeConfiguredName) {
        return serverAPI.clearAuditLogDestinations(userId, serverName, serverToBeConfiguredName);
    }

    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS).
     * <br><br>
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/cohorts/{cohortName}")
    public VoidResponse addCohortRegistration(@PathVariable                   String               userId,
                                              @PathVariable                   String               serverName,
                                              @PathVariable                   String               serverToBeConfiguredName,
                                              @PathVariable                   String               cohortName)
    {
        return serverAPI.addCohortRegistration(userId, serverName, serverToBeConfiguredName,  cohortName);
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
    @DeleteMapping(path = "/cohorts/{cohortName}")
    public VoidResponse removeCohortRegistration(@PathVariable                String               userId,
                                              @PathVariable                   String               serverName,
                                              @PathVariable                   String               serverToBeConfiguredName,
                                              @PathVariable                   String               cohortName)
    {
        return serverAPI.removeCohortRegistration(userId, serverName, serverToBeConfiguredName,  cohortName);
    }


}
