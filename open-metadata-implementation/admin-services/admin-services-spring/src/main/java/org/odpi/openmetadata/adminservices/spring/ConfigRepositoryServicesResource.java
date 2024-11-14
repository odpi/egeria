/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.adminservices.rest.URLRequestBody;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminServices;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.CohortTopicStructure;
import org.odpi.openmetadata.adminservices.configuration.properties.LocalRepositoryConfig;
import org.odpi.openmetadata.adminservices.rest.CohortConfigResponse;
import org.odpi.openmetadata.adminservices.rest.ConnectionListResponse;
import org.odpi.openmetadata.adminservices.rest.DedicatedTopicListResponse;
import org.odpi.openmetadata.adminservices.rest.LocalRepositoryConfigResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * ConfigRepositoryServicesResource provides the configuration services for setting
 * up the repository services subsystems.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/users/{userId}/servers/{serverName}")


@Tag(name="Administration Services - Server Configuration", description="The server configuration administration services support the configuration" +
        " of the open metadata and governance services within an OMAG Server. This configuration determines which of the Open Metadata and " +
        "Governance (OMAG) services are active.",
        externalDocs=@ExternalDocumentation(description="Further information",
                url="https://egeria-project.org/guides/admin/servers/"))

public class ConfigRepositoryServicesResource
{
    private final OMAGServerAdminServices adminAPI = new OMAGServerAdminServices();


    /*
     * =============================================================
     * Configure basic options using defaults
     */

    /**
     * Set up the default audit log for the server.  This adds the console audit log destination.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody null request body
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/default")

    @Operation(summary="setDefaultAuditLog",
               description="Set up the default audit log for the server.  This adds the console audit log destination.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse setDefaultAuditLog(@PathVariable String                            userId,
                                           @PathVariable String                            serverName,
                                           @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return adminAPI.setDefaultAuditLog(userId, serverName, requestBody);
    }


    /**
     * Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/console")

    @Operation(summary="addConsoleAuditLogDestination",
               description="Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addConsoleAuditLogDestination(@PathVariable String       userId,
                                                      @PathVariable String       serverName,
                                                      @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addConsoleAuditLogDestination(userId, serverName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates slf4j records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/slf4j")

    @Operation(summary="addSLF4JAuditLogDestination",
               description="Add an audit log destination that creates slf4j records that are written to logback.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addSLF4JAuditLogDestination(@PathVariable String       userId,
                                                    @PathVariable String       serverName,
                                                    @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addSLF4JAuditLogDestination(userId, serverName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param directoryName name of directory
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/files")

    @Operation(summary="addFileAuditLogDestination",
               description="Add an audit log destination that creates log records as JSON files in a shared directory.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addFileAuditLogDestination(@PathVariable String       userId,
                                                   @PathVariable String       serverName,
                                                   @RequestParam (required = false)
                                                                 String       directoryName,
                                                   @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addFileAuditLogDestination(userId, serverName, directoryName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates log records as rows in a PostgreSQL Database Schema.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure access to the database
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @PostMapping(path = "/audit-log-destinations/postgres")

    @Operation(summary="addPostgreSQLDatabaseAuditLogDestination",
            description="Add an audit log destination that creates log records as rows in a PostgreSQL Database Schema.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addPostgreSQLDatabaseAuditLogDestination(@PathVariable String              userId,
                                                                 @PathVariable String              serverName,
                                                                 @RequestBody  Map<String, Object> storageProperties)
    {
        return adminAPI.addPostgreSQLAuditLogDestination(userId, serverName, storageProperties);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param topicName name of topic
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/event-topic")

    @Operation(summary="addEventTopicAuditLogDestination",
               description="Add an audit log destination that sends each log record as an event on the supplied event topic.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addEventTopicAuditLogDestination(@PathVariable String       userId,
                                                         @PathVariable String       serverName,
                                                         @RequestParam (required = false)
                                                                       String       topicName,
                                                         @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addEventTopicAuditLogDestination(userId, serverName, topicName, supportedSeverities);
    }


    /**
     * Add an audit log destination that is defined by the supplied connection object.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection connection object that defines the audit log destination
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/connection")

    @Operation(summary="addAuditLogDestination",
               description="Add an audit log destination that is defined by the supplied connection object.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addAuditLogDestination(@PathVariable String     userId,
                                               @PathVariable String     serverName,
                                               @RequestBody  Connection connection)
    {
        return adminAPI.addAuditLogDestination(userId, serverName, connection);
    }


    /**
     * Update an audit log destination that is identified with the supplied destination name with
     * the supplied connection object.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connectionName name of the audit log destination connection to be updated
     * @param connection connection object that defines the audit log destination
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/audit-log-destinations/connection/{connectionName}")

    @Operation(summary="updateAuditLogDestination",
               description="Update an audit log destination that is identified with the supplied destination name with the supplied connection object.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse updateAuditLogDestination(@PathVariable String     userId,
                                                  @PathVariable String     serverName,
                                                  @PathVariable String     connectionName,
                                                  @RequestBody  Connection connection)
    {
        return adminAPI.updateAuditLogDestination(userId, serverName, connectionName, connection);
    }


    /**
     * Delete an audit log destination that is identified with the supplied connection's name.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connectionName qualified name of the audit log destination connection to be deleted.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @DeleteMapping(path = "/audit-log-destinations/connection/{connectionName}")

    @Operation(summary="clearAuditLogDestination",
               description="Delete an audit log destination that is identified with the supplied connection's name.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse clearAuditLogDestination(@PathVariable String     userId,
                                                 @PathVariable String     serverName,
                                                 @PathVariable String     connectionName)
    {
        return adminAPI.clearAuditLogDestination(userId, serverName, connectionName);
    }


    /**
     * Add a new open metadata archive to load at startup.  This open metadata archive is a JSON file.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    @PostMapping(path = "/open-metadata-archives/file")

    @Operation(summary="addStartUpOpenMetadataArchiveFile",
               description="Add a new open metadata archive to load at startup.  This open metadata archive is a JSON file.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public VoidResponse addStartUpOpenMetadataArchiveFile(@PathVariable String userId,
                                                          @PathVariable String serverName,
                                                          @RequestBody  String fileName)
    {
        return adminAPI.addStartUpOpenMetadataArchiveFile(userId, serverName, fileName);
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
    @DeleteMapping(path = "/local-repository")

    @Operation(summary="setNoRepositoryMode",
               description="Remove all configuration for a local repository.  The default is no local repository.  This call" +
                                   " can be used to remove subsequent local repository configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setNoRepositoryMode(@PathVariable String userId,
                                            @PathVariable String serverName)
    {
        return adminAPI.setNoRepositoryMode(userId, serverName);
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody null request body
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/in-memory-repository")

    @Operation(summary="setInMemLocalRepository",
               description="Set up an in memory local repository.  This native repository uses hashmaps to store content.  It is useful" +
                                   " for demos, testing and POCs.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/in-memory/overview/"))

    public VoidResponse setInMemLocalRepository(@PathVariable                   String          userId,
                                                @PathVariable                   String          serverName,
                                                @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return adminAPI.setInMemLocalRepository(userId, serverName, requestBody);
    }


    /**
     * Set up a graph store as the local repository.  This repository uses JanusGraph.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure the back end storage for the graph
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/local-graph-repository")

    @Operation(summary="setGraphLocalRepository",
               description="Set up a graph store as the local repository.  This native repository uses JanusGraph.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/janus-graph/overview/"))

    public VoidResponse setGraphLocalRepository(@PathVariable                  String              userId,
                                                @PathVariable                  String              serverName,
                                                @RequestBody @Nullable         Map<String, Object> storageProperties)
    {
        return adminAPI.setGraphLocalRepository(userId, serverName, storageProperties);
    }


    /**
     * Set up a PostgreSQL database schema as the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure access to postgres
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/postgres-repository")

    @Operation(summary="setPostgresLocalRepository",
            description="Set up a PostgreSQL Database schema as the local repository.  Each repository is stored in its own database schema.  The storage properties should include databaseURL, databaseSchema, secretsStore and secretsCollectionName",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/connectors/repository/postgres/overview/"))

    public VoidResponse setPostgresLocalRepository(@PathVariable                  String              userId,
                                                   @PathVariable                  String              serverName,
                                                   @RequestBody                   Map<String, Object> storageProperties)
    {
        return adminAPI.setPostgresLocalRepository(userId, serverName, storageProperties);
    }


    /**
     * Set up an XTDB store as the local repository.  This store uses XTDB to provide a historical metadata repository that operates in memory.
     * This version of the XTDB repository is designed for testing.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  null request body
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/xtdb-in-memory-repository")

    @Operation(summary="setXTDBInMemRepository",
               description="Set up an XTDB store as the local repository. " +
                                   " This store uses XTDB to provide a historical metadata repository that operates in memory." +
                                   " This version of the XTDB repository is designed for testing.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/xtdb/"))

    public VoidResponse setXTDBInMemRepository(@PathVariable String              userId,
                                               @PathVariable String              serverName,
                                               @RequestBody(required = false)
                                                            NullRequestBody requestBody)
    {
        return adminAPI.setXTDBInMemRepository(userId, serverName, requestBody);
    }


    /**
     * Set up an XTDB store as the local repository.  This store uses XTDB with RocksDB KV store to provide a high performance historical
     * metadata repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  null request body
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/xtdb-local-kv-repository")

    @Operation(summary="setXTDBLocalKVRepository",
               description="Set up an XTDB store as the local repository.  This store uses XTDB with RocksDB KV store to provide a high " +
                                   "performance historical metadata repository.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/xtdb/"))

    public VoidResponse setXTDBLocalKVRepository(@PathVariable String          userId,
                                                 @PathVariable String          serverName,
                                                 @RequestBody (required = false)
                                                               NullRequestBody requestBody)
    {
        return adminAPI.setXTDBLocalKVRepository(userId, serverName, requestBody);
    }


    /**
     * Set up an XTDB store as the local repository.  This store uses XTDB to provide a high performance historical
     * metadata repository.  It is scalable with different back ends and can be run in a HA context with multiple versions of the
     * same server deployed to the same repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure the back end storage for the graph
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/xtdb-local-repository")

    @Operation(summary="setXTDBLocalRepository",
               description="Set up an XTDB store as the local repository.  This store uses XTDB to provide a high performance historical" +
                                   " metadata repository.  It is scalable with different back ends and can be run in a HA context with multiple versions of the" +
                                   " same server deployed to the same repository.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/xtdb/"))

    public VoidResponse setXTDBLocalRepository(@PathVariable String              userId,
                                               @PathVariable String              serverName,
                                               @RequestBody  Map<String, Object> storageProperties)
    {
        return adminAPI.setXTDBLocalRepository(userId, serverName, storageProperties);
    }


    /**
     * Set up a read only local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/read-only-repository")

    @Operation(summary="setReadOnlyLocalRepository",
               description="Set up a read-only local repository.  This native repository holds metadata from open metadata archives, " +
                                   "or reference metadata from other members of sny connected cohorts.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/read-only/overview/"))

    public VoidResponse setReadOnlyLocalRepository(@PathVariable String userId,
                                                   @PathVariable String serverName)
    {
        return adminAPI.setReadOnlyLocalRepository(userId, serverName);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    @PostMapping(path = "/local-repository/mode/plugin-repository/connection")

    @Operation(summary="setPluginRepositoryConnection",
               description="Provide the connection to the local repository.  Typically this is an adapter repository connector connecting a third party metadata repository into the open metadata ecosystem.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setPluginRepositoryConnection(@PathVariable String     userId,
                                                      @PathVariable String     serverName,
                                                      @RequestBody  Connection connection)
    {
        return adminAPI.setPluginRepositoryConnection(userId, serverName, connection);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
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
    @PostMapping(path = "/local-repository/mode/plugin-repository/details")

    @Operation(summary="setPluginRepositoryConnection",
               description="Provide the connection to the local repository.  Typically this is an adapter repository connector connecting a third party metadata repository into the open metadata ecosystem.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setPluginRepositoryConnection(@PathVariable                   String               userId,
                                                      @PathVariable                   String               serverName,
                                                      @RequestParam                   String               connectorProvider,
                                                      @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setPluginRepositoryConnection(userId, serverName, connectorProvider, additionalProperties);
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
    @PostMapping(path = "/local-repository/mode/repository-proxy/connection")

    @Operation(summary="setRepositoryProxyConnection",
               description="Provide the connection to the local repository - used when the local repository mode is set to repository proxy.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

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
    @PostMapping(path = "/local-repository/mode/repository-proxy/details")

    @Operation(summary="setRepositoryProxyConnection",
               description="Provide the connection to the local repository - used when the local repository mode is set to repository proxy.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setRepositoryProxyConnection(@PathVariable                   String               userId,
                                                     @PathVariable                   String               serverName,
                                                     @RequestParam                   String               connectorProvider,
                                                     @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setRepositoryProxyConnection(userId, serverName, connectorProvider, additionalProperties);
    }


    /**
     * Provide the connection to a repository proxy's event mapper.  The default value is null which
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
    @PostMapping(path = "/local-repository/event-mapper-connection")

    @Operation(summary="setRepositoryProxyEventMapper",
               description="Provide the connection to a repository proxy's event mapper.  The default value is null which" +
                                   " means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change" +
                                   " the metadata in the repository without going through the open metadata and governance services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))


    public VoidResponse setRepositoryProxyEventMapper(@PathVariable String     userId,
                                                      @PathVariable String     serverName,
                                                      @RequestBody  Connection connection)
    {
        return adminAPI.setRepositoryProxyEventMapper(userId, serverName, connection);
    }


    /**
     * Provide the connection to a repository proxy's event mapper.  The default value is null which
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
    @PostMapping(path = "/local-repository/event-mapper-details")

    @Operation(summary="setRepositoryProxyEventMapper",
               description="Provide the connection to a repository proxy's event mapper.  The default value is null which" +
                                   " means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change" +
                                   " the metadata in the repository without going through the open metadata and governance services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setRepositoryProxyEventMapper(@PathVariable                 String               userId,
                                                      @PathVariable                 String               serverName,
                                                      @RequestParam                 String               connectorProvider,
                                                      @RequestParam                 String               eventSource,
                                                      @RequestBody(required=false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setRepositoryProxyEventMapper(userId, serverName, connectorProvider, eventSource, additionalProperties);
    }


    /**
     * Return the local metadata collection name.  If the local repository is not configured then the invalid parameter exception is returned.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @return guid response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @GetMapping(path = "/local-repository/metadata-collection-name")

    @Operation(summary="getLocalMetadataCollectionName",
            description="Return the local metadata collection name.  If the local repository is not configured then the invalid parameter exception is returned.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public StringResponse getLocalMetadataCollectionName(@PathVariable  String userId,
                                                         @PathVariable  String serverName)
    {
        return adminAPI.getLocalMetadataCollectionName(userId, serverName);
    }



    /**
     * Set up the local metadata collection name.  If this is not set then the default value is the local server name.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param name                        metadata collection name.
     * @return void response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/local-repository/metadata-collection-name/{name}")

    @Operation(summary="setLocalMetadataCollectionName",
               description="Provide the connection to the local repository - used when the local repository mode is set to repository proxy.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public VoidResponse setLocalMetadataCollectionName(@PathVariable  String               userId,
                                                       @PathVariable  String               serverName,
                                                       @PathVariable  String               name)
    {
        return adminAPI.setLocalMetadataCollectionName(userId, serverName, name);
    }


    /**
     * Return the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @return guid response or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @GetMapping(path = "/local-repository/metadata-collection-id")

    @Operation(summary="getLocalMetadataCollectionId",
               description="Return the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public GUIDResponse getLocalMetadataCollectionId(@PathVariable  String userId,
                                                     @PathVariable  String serverName)
    {
        return adminAPI.getLocalMetadataCollectionId(userId, serverName);
    }


    /**
     * Set up the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.
     *
     * @param userId                      user that is issuing the request.
     * @param serverName                  local server name.
     * @param metadataCollectionId        new identifier for the local repository's metadata collection
     * @return void or
     * OMAGNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/local-repository/metadata-collection-id")

    @Operation(summary="setLocalMetadataCollectionId",
               description="Set up the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public VoidResponse setLocalMetadataCollectionId(@PathVariable String userId,
                                                     @PathVariable String serverName,
                                                     @RequestBody  String metadataCollectionId)
    {
        return adminAPI.setLocalMetadataCollectionId(userId, serverName, metadataCollectionId);
    }


    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS).
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to the changes in their supported metadata types and instances.
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
    @PostMapping(path = "/cohorts/{cohortName}")

    @Operation(summary="addCohortRegistration",
               description="Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS)." +
                                   " A cohort is a group of open metadata" +
                                   " repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts." +
                                   " Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration" +
                                   " information and events related to the changes in their supported metadata types and instances." +
                                   " They are also able to query each other's metadata directly through REST calls.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse addCohortRegistration(@PathVariable                   String               userId,
                                              @PathVariable                   String               serverName,
                                              @PathVariable                   String               cohortName,
                                              @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.addCohortRegistration(userId, serverName, cohortName, CohortTopicStructure.DEDICATED_TOPICS, additionalProperties);
    }


    /**
     * Enable registration of server to an open metadata repository cohort using the topic pattern specified by cohortTopicStructure.
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to the changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param cohortTopicStructure the style of cohort topic set up to use
     * @param additionalProperties additional properties for the event bus connection
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-structure/{cohortTopicStructure}")

    @Operation(summary="addCohortRegistration",
               description="Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS)." +
                                   " A cohort is a group of open metadata" +
                                   " repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts." +
                                   " Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration" +
                                   " information and events related to the changes in their supported metadata types and instances." +
                                   " They are also able to query each other's metadata directly through REST calls.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse addCohortRegistration(@PathVariable                   String               userId,
                                              @PathVariable                   String               serverName,
                                              @PathVariable                   String               cohortName,
                                              @PathVariable                   CohortTopicStructure cohortTopicStructure,
                                              @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.addCohortRegistration(userId, serverName, cohortName, cohortTopicStructure, additionalProperties);
    }


    /**
     * Retrieve the registration of server to an open metadata repository cohort.  This is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to the changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return cohort config response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @GetMapping(path = "/cohorts/{cohortName}")

    @Operation(summary="getCohortConfig",
               description="Retrieve the registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS)." +
                                   " A cohort is a group of open metadata" +
                                   " repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts." +
                                   " Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration" +
                                   " information and events related to the changes in their supported metadata types and instances." +
                                   " They are also able to query each other's metadata directly through REST calls.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public CohortConfigResponse getCohortConfig(@PathVariable String userId,
                                                @PathVariable String serverName,
                                                @PathVariable String cohortName)
    {
        return adminAPI.getCohortConfig(userId, serverName, cohortName);
    }


    /**
     * Retrieve the current topic name for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return string name or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @GetMapping(path = "/cohorts/{cohortName}/topic-name")

    @Operation(summary="getCohortTopicName",
               description="Retrieve the current topic name for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public StringResponse getCohortTopicName(@PathVariable  String userId,
                                             @PathVariable  String serverName,
                                             @PathVariable  String cohortName)
    {
        return adminAPI.getCohortTopicName(userId, serverName, cohortName);
    }


    /**
     * Retrieve the current topic name for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return  List of topic names - registration first, then types and then instances or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @GetMapping(path = "/cohorts/{cohortName}/dedicated-topic-names")

    @Operation(summary="getDedicatedCohortTopicNames",
               description="Retrieve the current topic name for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public DedicatedTopicListResponse getDedicatedCohortTopicNames(@PathVariable  String userId,
                                                                   @PathVariable  String serverName,
                                                                   @PathVariable  String cohortName)
    {
        return adminAPI.getDedicatedCohortTopicNames(userId, serverName, cohortName);
    }


    /**
     * Override the current name for the single topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override")

    @Operation(summary="overrideCohortTopicName",
               description="Override the current name for the single topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideCohortTopicName(@PathVariable  String userId,
                                                @PathVariable  String serverName,
                                                @PathVariable  String cohortName,
                                                @RequestBody   String topicName)
    {
        return adminAPI.overrideCohortTopicName(userId, serverName, cohortName, topicName);
    }


    /**
     * Override the current name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override/registration")

    @Operation(summary="overrideRegistrationCohortTopicName",
               description="Override the current name for the registration topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideRegistrationCohortTopicName(@PathVariable  String userId,
                                                            @PathVariable  String serverName,
                                                            @PathVariable  String cohortName,
                                                            @RequestBody   String topicName)
    {
        return adminAPI.overrideRegistrationCohortTopicName(userId, serverName, cohortName, topicName);
    }


    /**
     * Override the current name for the "types" topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override/types")

    @Operation(summary="overrideTypesCohortTopicName",
               description="Override the current name for the \"types\" topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideTypesCohortTopicName(@PathVariable  String userId,
                                                     @PathVariable  String serverName,
                                                     @PathVariable  String cohortName,
                                                     @RequestBody   String topicName)
    {
        return adminAPI.overrideTypesCohortTopicName(userId, serverName, cohortName, topicName);
    }


    /**
     * Override the current name for the "instances" topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override/instances")

    @Operation(summary="overrideInstancesCohortTopicName",
               description="Override the current name for the \"instances\" topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideInstancesCohortTopicName(@PathVariable  String userId,
                                                         @PathVariable  String serverName,
                                                         @PathVariable  String cohortName,
                                                         @RequestBody   String topicName)
    {
        return adminAPI.overrideInstancesCohortTopicName(userId, serverName, cohortName, topicName);
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

    @Operation(summary="clearCohortConfig",
               description="Unregister this server from an open metadata repository cohort.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse clearCohortConfig(@PathVariable String          userId,
                                          @PathVariable String          serverName,
                                          @PathVariable String          cohortName)
    {
        return adminAPI.clearCohortConfig(userId, serverName, cohortName);
    }


    /*
     * =============================================================
     * Advanced options overriding defaults
     */


    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param auditLogDestinations list of connection objects
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @PostMapping(path = "/audit-log-destinations")

    @Operation(summary="setAuditLogDestinations",
               description="Set up the list of audit log destinations.  These destinations are expressed as Connection objects" +
                                   " to the connectors that will handle the audit log records.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse setAuditLogDestinations(@PathVariable String                userId,
                                                @PathVariable String                serverName,
                                                @RequestBody  List<Connection>      auditLogDestinations)
    {
        return adminAPI.setAuditLogDestinations(userId, serverName, auditLogDestinations);
    }


    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return connection list response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @GetMapping(path = "/audit-log-destinations")

    @Operation(summary="getAuditLogDestinations",
               description="Set up the list of audit log destinations.  These destinations are expressed as Connection objects" +
                                   " to the connectors that will handle the audit log records.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public ConnectionListResponse getAuditLogDestinations(@PathVariable String userId,
                                                          @PathVariable String serverName)
    {
        return adminAPI.getAuditLogDestinations(userId, serverName);
    }


    /**
     * Clears all audit log destinations for this server.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @DeleteMapping(path = "/audit-log-destinations")

    @Operation(summary="clearAuditLogDestinations",
               description="Clears all audit log destinations for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse clearAuditLogDestinations(@PathVariable String userId,
                                                  @PathVariable String serverName)
    {
        return adminAPI.clearAuditLogDestinations(userId, serverName);
    }


    /**
     * Set up the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param openMetadataArchives list of connection objects
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @PostMapping(path = "/open-metadata-archives")

    @Operation(summary="setOpenMetadataArchives",
               description="Set up the list of open metadata archives.  " +
                                   "These are open metadata types and instances that are loaded at repository start up.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public VoidResponse setOpenMetadataArchives(@PathVariable String           userId,
                                                @PathVariable String           serverName,
                                                @RequestBody  List<Connection> openMetadataArchives)
    {
        return adminAPI.setOpenMetadataArchives(userId, serverName, openMetadataArchives);
    }


    /**
     * Return the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return connection list response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @GetMapping(path = "/open-metadata-archives")

    @Operation(summary="getOpenMetadataArchives",
               description="Return the list of open metadata archives.  These are open metadata types and instances that are loaded at" +
                                   " repository start up.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public ConnectionListResponse getOpenMetadataArchives(@PathVariable String userId,
                                                          @PathVariable String serverName)
    {
        return adminAPI.getOpenMetadataArchives(userId, serverName);
    }


    /**
     * Clear the list of open metadata archives.  These are the open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName.
     */
    @DeleteMapping(path = "/open-metadata-archives")

    @Operation(summary="clearOpenMetadataArchives",
               description="Clear the list of open metadata archives.  These are the open metadata types and instances that are loaded at" +
                                   " repository start up.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public VoidResponse clearOpenMetadataArchives(@PathVariable String           userId,
                                                  @PathVariable String           serverName)
    {
        return adminAPI.clearOpenMetadataArchives(userId, serverName);
    }


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
    @PostMapping(path = "/local-repository/configuration")

    @Operation(summary="setLocalRepositoryConfig",
               description="Set up the configuration for the local repository.  This overrides the current values.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))


    public VoidResponse setLocalRepositoryConfig(@PathVariable String                userId,
                                                 @PathVariable String                serverName,
                                                 @RequestBody  LocalRepositoryConfig localRepositoryConfig)
    {
        return adminAPI.setLocalRepositoryConfig(userId, serverName, localRepositoryConfig);
    }


    /**
     * Return the configuration for the local repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @return local repository response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    @GetMapping(path = "/local-repository/configuration")

    @Operation(summary="getLocalRepositoryConfig",
               description="Return the configuration for the local repository.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public LocalRepositoryConfigResponse getLocalRepositoryConfig(@PathVariable String userId,
                                                                  @PathVariable String serverName)
    {
        return adminAPI.getLocalRepositoryConfig(userId, serverName);
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
    @DeleteMapping(path = "/local-repository/configuration")

    @Operation(summary="clearLocalRepositoryConfig",
               description="Remove all configuration for a local repository.  The default is no local repository.  This call" +
                                   " can be used to remove subsequent local repository configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse clearLocalRepositoryConfig(@PathVariable String userId,
                                                   @PathVariable String serverName)
    {
        return adminAPI.clearLocalRepositoryConfig(userId, serverName);
    }


    /**
     * Update the URL broadcast across the cohort to allow other members to issue queries to this repository.
     * This method is needed to reconfigure a server that has moved from one platform to another.  Once the
     * URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param requestBody  String url.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @PostMapping(path = "/local-repository/configuration/remote-repository-connector-url")

    @Operation(summary="resetRemoteCohortURL",
               description="Update the URL broadcast across the cohort to allow other members to issue queries to this repository." +
                                   " This method is needed to reconfigure a server that has moved from one platform to another.  Once the" +
                                   " URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse resetRemoteCohortURL(@PathVariable String         userId,
                                             @PathVariable String         serverName,
                                             @RequestBody  URLRequestBody requestBody)
    {
        return adminAPI.resetRemoteCohortURL(userId, serverName, requestBody);
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
    @PostMapping(path = "/cohorts/{cohortName}/configuration")

    @Operation(summary="setCohortConfig",
               description="Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a" +
                                   " cohort.  Use setCohortMode to delete a cohort.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse setCohortConfig(@PathVariable String       userId,
                                        @PathVariable String       serverName,
                                        @PathVariable String       cohortName,
                                        @RequestBody  CohortConfig cohortConfig)
    {
        return adminAPI.setCohortConfig(userId, serverName, cohortName, cohortConfig);
    }
}
