package org.odpi.openmetadata.viewservices.serverauthor.server.spring;/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.viewservices.serverauthor.api.rest.ServerAuthorConfigurationResponse;
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

@Tag(name = "Administration Services - Server Configuration", description = "The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
     externalDocs = @ExternalDocumentation(description = "Further information",
                                           url = "https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-an-omag-server.html"))
class ConfigRepositoryServicesViewResource {
    private ServerAuthorViewRESTServices adminAPI = new ServerAuthorViewRESTServices();

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
    @PostMapping(path = "/local-repository/mode/in-memory-repository")
    public FFDCResponseBase setInMemLocalRepository(@PathVariable String userId,
                                                    @PathVariable String serverName,
                                                    @PathVariable String serverToBeConfiguredName,
                                                    @RequestBody(required = false) NullRequestBody requestBody) {
        return adminAPI.setInMemLocalRepository(userId, serverName, serverToBeConfiguredName);
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
        return adminAPI.setGraphLocalRepository(userId, serverName, serverToBeConfiguredName, storageProperties);
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
        return adminAPI.setReadOnlyLocalRepository(userId, serverName, serverToBeConfiguredName);
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
        return adminAPI.setDefaultAuditLog(userId, serverName, serverToBeConfiguredName);
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
        return adminAPI.addConsoleAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
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
        return adminAPI.addSLF4JAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
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
        return adminAPI.addFileAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
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
        return adminAPI.addEventTopicAuditLogDestination(userId, serverName, serverToBeConfiguredName, supportedSeverities);
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
        return adminAPI.addAuditLogDestination(userId, serverName, serverToBeConfiguredName, connection);
    }

}
