/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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

import java.util.List;
import java.util.Map;

/**
 * ConfigRepositoryServicesResource provides the configuration services for setting
 * up the repository services subsystems.
 */
@RestController
@RequestMapping("/open-metadata/admin-services/servers/{serverName}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)

@Tag(name="Server Configuration", description="The server configuration administration services support the configuration" +
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
     * @param serverName  local server name.
     * @param requestBody null request body
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/default")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setDefaultAuditLog",
               description="Set up the default audit log for the server.  This adds the console audit log destination.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse setDefaultAuditLog(@PathVariable String                            serverName,
                                           @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return adminAPI.setDefaultAuditLog(serverName, requestBody);
    }


    /**
     * Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.
     *
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/console")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addConsoleAuditLogDestination",
               description="Set up the console audit log for the server.  This writes selected parts of the audit log record to stdout.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addConsoleAuditLogDestination(@PathVariable String       serverName,
                                                      @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addConsoleAuditLogDestination(serverName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates slf4j records.
     *
     * @param serverName  local server name.
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/slf4j")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addSLF4JAuditLogDestination",
               description="Add an audit log destination that creates slf4j records that are written to logback.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addSLF4JAuditLogDestination(@PathVariable String       serverName,
                                                    @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addSLF4JAuditLogDestination(serverName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates log records as JSON files in a shared directory.
     *
     * @param serverName  local server name.
     * @param directoryName name of directory
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/files")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addFileAuditLogDestination",
               description="Add an audit log destination that creates log records as JSON files in a shared directory.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addFileAuditLogDestination(@PathVariable String       serverName,
                                                   @RequestParam (required = false)
                                                                 String       directoryName,
                                                   @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addFileAuditLogDestination(serverName, directoryName, supportedSeverities);
    }


    /**
     * Add an audit log destination that creates log records as rows in a PostgreSQL Database Schema.
     *
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure access to the database
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @PostMapping(path = "/audit-log-destinations/postgres")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addPostgreSQLDatabaseAuditLogDestination",
            description="Add an audit log destination that creates log records as rows in a PostgreSQL Database Schema.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addPostgreSQLDatabaseAuditLogDestination(@PathVariable String              serverName,
                                                                 @RequestBody  Map<String, Object> storageProperties)
    {
        return adminAPI.addPostgreSQLAuditLogDestination(serverName, storageProperties);
    }


    /**
     * Add an audit log destination that sends each log record as an event on the supplied event topic.
     *
     * @param serverName  local server name.
     * @param topicName name of topic
     * @param supportedSeverities list of severities that should be logged to this destination (empty list means all)
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/event-topic")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addEventTopicAuditLogDestination",
               description="Add an audit log destination that sends each log record as an event on the supplied event topic.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addEventTopicAuditLogDestination(@PathVariable String       serverName,
                                                         @RequestParam (required = false)
                                                                       String       topicName,
                                                         @RequestBody  List<String> supportedSeverities)
    {
        return adminAPI.addEventTopicAuditLogDestination(serverName, topicName, supportedSeverities);
    }


    /**
     * Add an audit log destination that is defined by the supplied connection object.
     *
     * @param serverName  local server name.
     * @param connection connection object that defines the audit log destination
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/audit-log-destinations/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addAuditLogDestination",
               description="Add an audit log destination that is defined by the supplied connection object.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse addAuditLogDestination(@PathVariable String     serverName,
                                               @RequestBody  Connection connection)
    {
        return adminAPI.addAuditLogDestination(serverName, connection);
    }


    /**
     * Update an audit log destination that is identified with the supplied destination name with
     * the supplied connection object.
     *
     * @param serverName  local server name.
     * @param connectionName name of the audit log destination connection to be updated
     * @param connection connection object that defines the audit log destination
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName parameter.
     */
    @PostMapping(path = "/audit-log-destinations/connection/{connectionName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateAuditLogDestination",
               description="Update an audit log destination that is identified with the supplied destination name with the supplied connection object.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse updateAuditLogDestination(@PathVariable String     serverName,
                                                  @PathVariable String     connectionName,
                                                  @RequestBody  Connection connection)
    {
        return adminAPI.updateAuditLogDestination(serverName, connectionName, connection);
    }


    /**
     * Delete an audit log destination that is identified with the supplied connection's name.
     *
     * @param serverName  local server name.
     * @param connectionName qualified name of the audit log destination connection to be deleted.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @DeleteMapping(path = "/audit-log-destinations/connection/{connectionName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAuditLogDestination",
               description="Delete an audit log destination that is identified with the supplied connection's name.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse clearAuditLogDestination(@PathVariable String     serverName,
                                                 @PathVariable String     connectionName)
    {
        return adminAPI.clearAuditLogDestination(serverName, connectionName);
    }


    /**
     * Add a new open metadata archive to load at startup.  This open metadata archive is a JSON file.
     *
     * @param serverName  local server name.
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or fileName parameter.
     */
    @PostMapping(path = "/open-metadata-archives/file")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addStartUpOpenMetadataArchiveFile",
               description="Add a new open metadata archive to load at startup.  This open metadata archive is a JSON file.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public VoidResponse addStartUpOpenMetadataArchiveFile(@PathVariable String serverName,
                                                          @RequestBody  String fileName)
    {
        return adminAPI.addStartUpOpenMetadataArchiveFile(serverName, fileName);
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @DeleteMapping(path = "/local-repository")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setNoRepositoryMode",
               description="Remove all configuration for a local repository.  The default is no local repository.  This call" +
                                   " can be used to remove subsequent local repository configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setNoRepositoryMode(@PathVariable String serverName)
    {
        return adminAPI.setNoRepositoryMode(serverName);
    }


    /**
     * Set up an in memory local repository.  This repository uses hashmaps to store content.  It is useful
     * for demos, testing and POCs.
     *
     * @param serverName  local server name.
     * @param requestBody null request body
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/in-memory-repository")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setInMemLocalRepository",
               description="Set up an in memory local repository.  This native repository uses hashmaps to store content.  It is useful" +
                                   " for demos, testing and POCs.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/in-memory/overview/"))

    public VoidResponse setInMemLocalRepository(@PathVariable                   String          serverName,
                                                @RequestBody(required = false)  NullRequestBody requestBody)
    {
        return adminAPI.setInMemLocalRepository(serverName, requestBody);
    }


    /**
     * Set up a PostgreSQL database schema as the local repository.
     *
     * @param serverName  local server name.
     * @param storageProperties  properties used to configure access to postgres
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/postgres-repository")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPostgresLocalRepository",
            description="Set up a PostgreSQL Database schema as the local repository.  Each repository is stored in its own database schema.  The storage properties should include databaseURL, databaseSchema, secretsStore and secretsCollectionName.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/connectors/repository/postgres/overview/"))

    public VoidResponse setPostgresLocalRepository(@PathVariable                  String              serverName,
                                                   @RequestBody(required = false) Map<String, Object> storageProperties)
    {
        return adminAPI.setPostgresLocalRepository(serverName, storageProperties);
    }


    /**
     * Set up a read only local repository.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGConfigurationErrorException the event bus has not been configured or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @PostMapping(path = "/local-repository/mode/read-only-repository")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setReadOnlyLocalRepository",
               description="Set up a read-only local repository.  This native repository holds metadata from open metadata archives, " +
                                   "or reference metadata from other members of sny connected cohorts.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/repository/read-only/overview/"))

    public VoidResponse setReadOnlyLocalRepository(@PathVariable String serverName)
    {
        return adminAPI.setReadOnlyLocalRepository(serverName);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    @PostMapping(path = "/local-repository/mode/plugin-repository/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPluginRepositoryConnection",
               description="Provide the connection to the local repository.  Typically this is an adapter repository connector connecting a third party metadata repository into the open metadata ecosystem.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setPluginRepositoryConnection(@PathVariable String     serverName,
                                                      @RequestBody  Connection connection)
    {
        return adminAPI.setPluginRepositoryConnection(serverName, connection);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to plugin repository.
     *
     * @param serverName   local server name.
     * @param connectorProvider  connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * UserNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    @PostMapping(path = "/local-repository/mode/plugin-repository/details")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setPluginRepositoryConnection",
               description="Provide the connection to the local repository.  Typically this is an adapter repository connector connecting a third party metadata repository into the open metadata ecosystem.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setPluginRepositoryConnection(@PathVariable                   String               serverName,
                                                      @RequestParam                   String               connectorProvider,
                                                      @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setPluginRepositoryConnection(serverName, connectorProvider, additionalProperties);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository connector.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set
     */
    @PostMapping(path = "/local-repository/mode/repository-proxy/connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setRepositoryProxyConnection",
               description="Provide the connection to the local repository - used when the local repository mode is set to repository proxy.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setRepositoryProxyConnection(@PathVariable String     serverName,
                                                     @RequestBody  Connection connection)
    {
        return adminAPI.setRepositoryProxyConnection(serverName, connection);
    }


    /**
     * Provide the connection to the local repository - used when the local repository mode is set to repository proxy.
     *
     * @param serverName   local server name.
     * @param connectorProvider  connector provider class name to the OMRS repository connector.
     * @param additionalProperties      additional parameters to pass to the repository connector
     * @return void response or
     * UserNotAuthorizedException     the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or repositoryProxyConnection parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    @PostMapping(path = "/local-repository/mode/repository-proxy/details")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setRepositoryProxyConnection",
               description="Provide the connection to the local repository - used when the local repository mode is set to repository proxy.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setRepositoryProxyConnection(@PathVariable                   String               serverName,
                                                     @RequestParam                   String               connectorProvider,
                                                     @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setRepositoryProxyConnection(serverName, connectorProvider, additionalProperties);
    }


    /**
     * Provide the connection to a repository proxy's event mapper.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param serverName  local server name.
     * @param connection  connection to the OMRS repository event mapper.
     * @return void response
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the local repository mode, or the event mapper has not been set
     */
    @PostMapping(path = "/local-repository/event-mapper-connection")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setRepositoryProxyEventMapper",
               description="Provide the connection to a repository proxy's event mapper.  The default value is null which" +
                                   " means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change" +
                                   " the metadata in the repository without going through the open metadata and governance services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))


    public VoidResponse setRepositoryProxyEventMapper(@PathVariable String     serverName,
                                                      @RequestBody  Connection connection)
    {
        return adminAPI.setRepositoryProxyEventMapper(serverName, connection);
    }


    /**
     * Provide the connection to a repository proxy's event mapper.  The default value is null which
     * means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change
     * the metadata in the repository without going through the open metadata and governance services.
     *
     * @param serverName                  local server name.
     * @param connectorProvider           Java class name of the connector provider for the OMRS repository event mapper.
     * @param eventSource                 topic name or URL to the native event source.
     * @param additionalProperties        additional properties for the event mapper connection
     * @return void response or
     * UserNotAuthorizedException    the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryEventMapper parameter or
     * OMAGConfigurationErrorException the local repository mode has not been set.
     */
    @PostMapping(path = "/local-repository/event-mapper-details")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setRepositoryProxyEventMapper",
               description="Provide the connection to a repository proxy's event mapper.  The default value is null which" +
                                   " means no event mapper.  An event mapper is needed if the local repository has additional APIs that can change" +
                                   " the metadata in the repository without going through the open metadata and governance services.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse setRepositoryProxyEventMapper(@PathVariable                 String               serverName,
                                                      @RequestParam                 String               connectorProvider,
                                                      @RequestParam                 String               eventSource,
                                                      @RequestBody(required=false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.setRepositoryProxyEventMapper(serverName, connectorProvider, eventSource, additionalProperties);
    }


    /**
     * Return the local metadata collection name.  If the local repository is not configured then the invalid parameter exception is returned.
     *
     * @param serverName                  local server name.
     * @return guid response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @GetMapping(path = "/local-repository/metadata-collection-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getLocalMetadataCollectionName",
            description="Return the local metadata collection name.  If the local repository is not configured then the invalid parameter exception is returned.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public StringResponse getLocalMetadataCollectionName(@PathVariable  String serverName)
    {
        return adminAPI.getLocalMetadataCollectionName(serverName);
    }



    /**
     * Set up the local metadata collection name.  If this is not set then the default value is the local server name.
     *
     * @param serverName                  local server name.
     * @param name                        metadata collection name.
     * @return void response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/local-repository/metadata-collection-name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setLocalMetadataCollectionName",
               description="Provide the connection to the local repository - used when the local repository mode is set to repository proxy.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public VoidResponse setLocalMetadataCollectionName(@PathVariable  String               serverName,
                                                       @PathVariable  String               name)
    {
        return adminAPI.setLocalMetadataCollectionName(serverName, name);
    }


    /**
     * Return the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.
     *
     * @param serverName                  local server name.
     * @return guid response or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @GetMapping(path = "/local-repository/metadata-collection-id")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getLocalMetadataCollectionId",
               description="Return the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public GUIDResponse getLocalMetadataCollectionId(@PathVariable  String serverName)
    {
        return adminAPI.getLocalMetadataCollectionId(serverName);
    }


    /**
     * Set up the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.
     *
     * @param serverName                  local server name.
     * @param metadataCollectionId        new identifier for the local repository's metadata collection
     * @return void or
     * UserNotAuthorizedException  the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or name parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/local-repository/metadata-collection-id")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setLocalMetadataCollectionId",
               description="Set up the local metadata collection id.  If the local repository is not configured then the invalid parameter exception is returned.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/metadata-collection-id/"))

    public VoidResponse setLocalMetadataCollectionId(@PathVariable String serverName,
                                                     @RequestBody  String metadataCollectionId)
    {
        return adminAPI.setLocalMetadataCollectionId(serverName, metadataCollectionId);
    }


    /**
     * Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS).
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to the changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param additionalProperties additional properties for the event bus connection
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/cohorts/{cohortName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addCohortRegistration",
               description="Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS)." +
                                   " A cohort is a group of open metadata" +
                                   " repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts." +
                                   " Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration" +
                                   " information and events related to the changes in their supported metadata types and instances." +
                                   " They are also able to query each other's metadata directly through REST calls.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse addCohortRegistration(@PathVariable                   String               serverName,
                                              @PathVariable                   String               cohortName,
                                              @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.addCohortRegistration(serverName, cohortName, CohortTopicStructure.DEDICATED_TOPICS, additionalProperties);
    }


    /**
     * Enable registration of server to an open metadata repository cohort using the topic pattern specified by cohortTopicStructure.
     * A cohort is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to the changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param cohortTopicStructure the style of cohort topic set up to use
     * @param additionalProperties additional properties for the event bus connection
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-structure/{cohortTopicStructure}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="addCohortRegistration",
               description="Enable registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS)." +
                                   " A cohort is a group of open metadata" +
                                   " repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts." +
                                   " Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration" +
                                   " information and events related to the changes in their supported metadata types and instances." +
                                   " They are also able to query each other's metadata directly through REST calls.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse addCohortRegistration(@PathVariable                   String               serverName,
                                              @PathVariable                   String               cohortName,
                                              @PathVariable                   CohortTopicStructure cohortTopicStructure,
                                              @RequestBody(required = false)  Map<String, Object>  additionalProperties)
    {
        return adminAPI.addCohortRegistration(serverName, cohortName, cohortTopicStructure, additionalProperties);
    }


    /**
     * Retrieve the registration of server to an open metadata repository cohort.  This is a group of open metadata
     * repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts.
     * Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration
     * information and events related to the changes in their supported metadata types and instances.
     * They are also able to query each other's metadata directly through REST calls.
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return cohort config response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter or
     * OMAGConfigurationErrorException the event bus is not set.
     */
    @GetMapping(path = "/cohorts/{cohortName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCohortConfig",
               description="Retrieve the registration of server to an open metadata repository cohort using the default topic structure (DEDICATED_TOPICS)." +
                                   " A cohort is a group of open metadata" +
                                   " repositories that are sharing metadata.  An OMAG server can connect to zero, one or more cohorts." +
                                   " Each cohort needs a unique name.  The members of the cohort use a shared topic to exchange registration" +
                                   " information and events related to the changes in their supported metadata types and instances." +
                                   " They are also able to query each other's metadata directly through REST calls.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public CohortConfigResponse getCohortConfig(@PathVariable String serverName,
                                                @PathVariable String cohortName)
    {
        return adminAPI.getCohortConfig(serverName, cohortName);
    }


    /**
     * Retrieve the current topic name for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return string name or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @GetMapping(path = "/cohorts/{cohortName}/topic-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getCohortTopicName",
               description="Retrieve the current topic name for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public StringResponse getCohortTopicName(@PathVariable  String serverName,
                                             @PathVariable  String cohortName)
    {
        return adminAPI.getCohortTopicName(serverName, cohortName);
    }


    /**
     * Retrieve the current topic name for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return  List of topic names - registration first, then types and then instances or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @GetMapping(path = "/cohorts/{cohortName}/dedicated-topic-names")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDedicatedCohortTopicNames",
               description="Retrieve the current topic name for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public DedicatedTopicListResponse getDedicatedCohortTopicNames(@PathVariable  String serverName,
                                                                   @PathVariable  String cohortName)
    {
        return adminAPI.getDedicatedCohortTopicNames(serverName, cohortName);
    }


    /**
     * Override the current name for the single topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="overrideCohortTopicName",
               description="Override the current name for the single topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideCohortTopicName(@PathVariable  String serverName,
                                                @PathVariable  String cohortName,
                                                @RequestBody   String topicName)
    {
        return adminAPI.overrideCohortTopicName(serverName, cohortName, topicName);
    }


    /**
     * Override the current name for the registration topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override/registration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="overrideRegistrationCohortTopicName",
               description="Override the current name for the registration topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideRegistrationCohortTopicName(@PathVariable  String serverName,
                                                            @PathVariable  String cohortName,
                                                            @RequestBody   String topicName)
    {
        return adminAPI.overrideRegistrationCohortTopicName(serverName, cohortName, topicName);
    }


    /**
     * Override the current name for the "types" topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override/types")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="overrideTypesCohortTopicName",
               description="Override the current name for the \"types\" topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideTypesCohortTopicName(@PathVariable  String serverName,
                                                     @PathVariable  String cohortName,
                                                     @RequestBody   String topicName)
    {
        return adminAPI.overrideTypesCohortTopicName(serverName, cohortName, topicName);
    }


    /**
     * Override the current name for the "instances" topic for the cohort.  This call can only be made once the cohort
     * is set up with enableCohortRegistration().
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @param topicName new name for the topic.
     * @return void or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or cohortName parameter or
     * OMAGConfigurationErrorException the cohort is not set up.
     */
    @PostMapping(path = "/cohorts/{cohortName}/topic-name-override/instances")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="overrideInstancesCohortTopicName",
               description="Override the current name for the \"instances\" topic for the cohort.  This call can only be made once the cohort" +
                                   " is set up with enableCohortRegistration().",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse overrideInstancesCohortTopicName(@PathVariable  String serverName,
                                                         @PathVariable  String cohortName,
                                                         @RequestBody   String topicName)
    {
        return adminAPI.overrideInstancesCohortTopicName(serverName, cohortName, topicName);
    }


    /**
     * Unregister this server from an open metadata repository cohort.
     *
     * @param serverName  local server name.
     * @param cohortName  name of the cohort.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or serviceMode parameter.
     */
    @DeleteMapping(path = "/cohorts/{cohortName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearCohortConfig",
               description="Unregister this server from an open metadata repository cohort.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse clearCohortConfig(@PathVariable String          serverName,
                                          @PathVariable String          cohortName)
    {
        return adminAPI.clearCohortConfig(serverName, cohortName);
    }


    /*
     * =============================================================
     * Advanced options overriding defaults
     */

    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param serverName  local server name.
     * @param auditLogDestinations list of connection objects
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @PostMapping(path = "/audit-log-destinations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setAuditLogDestinations",
               description="Set up the list of audit log destinations.  These destinations are expressed as Connection objects" +
                                   " to the connectors that will handle the audit log records.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse setAuditLogDestinations(@PathVariable String                serverName,
                                                @RequestBody  List<Connection>      auditLogDestinations)
    {
        return adminAPI.setAuditLogDestinations(serverName, auditLogDestinations);
    }


    /**
     * Set up the list of audit log destinations.  These destinations are expressed as Connection objects
     * to the connectors that will handle the audit log records.
     *
     * @param serverName  local server name.
     * @return connection list response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @GetMapping(path = "/audit-log-destinations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAuditLogDestinations",
               description="Set up the list of audit log destinations.  These destinations are expressed as Connection objects" +
                                   " to the connectors that will handle the audit log records.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public ConnectionListResponse getAuditLogDestinations(@PathVariable String serverName)
    {
        return adminAPI.getAuditLogDestinations(serverName);
    }


    /**
     * Clears all audit log destinations for this server.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @DeleteMapping(path = "/audit-log-destinations")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearAuditLogDestinations",
               description="Clears all audit log destinations for this server.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/audit-log-destination-connector/"))

    public VoidResponse clearAuditLogDestinations(@PathVariable String serverName)
    {
        return adminAPI.clearAuditLogDestinations(serverName);
    }


    /**
     * Set up the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param serverName  local server name.
     * @param openMetadataArchives list of connection objects
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @PostMapping(path = "/open-metadata-archives")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setOpenMetadataArchives",
               description="Set up the list of open metadata archives.  " +
                                   "These are open metadata types and instances that are loaded at repository start up.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public VoidResponse setOpenMetadataArchives(@PathVariable String           serverName,
                                                @RequestBody  List<Connection> openMetadataArchives)
    {
        return adminAPI.setOpenMetadataArchives(serverName, openMetadataArchives);
    }


    /**
     * Return the list of open metadata archives.  These are open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param serverName  local server name.
     * @return connection list response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @GetMapping(path = "/open-metadata-archives")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getOpenMetadataArchives",
               description="Return the list of open metadata archives.  These are open metadata types and instances that are loaded at" +
                                   " repository start up.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public ConnectionListResponse getOpenMetadataArchives(@PathVariable String serverName)
    {
        return adminAPI.getOpenMetadataArchives(serverName);
    }


    /**
     * Clear the list of open metadata archives.  These are the open metadata types and instances that are loaded at
     * repository start up.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName.
     */
    @DeleteMapping(path = "/open-metadata-archives")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearOpenMetadataArchives",
               description="Clear the list of open metadata archives.  These are the open metadata types and instances that are loaded at" +
                                   " repository start up.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/open-metadata-archive/"))

    public VoidResponse clearOpenMetadataArchives(@PathVariable String           serverName)
    {
        return adminAPI.clearOpenMetadataArchives(serverName);
    }


    /**
     * Set up the configuration for the local repository.  This overrides the current values.
     *
     * @param serverName  local server name.
     * @param localRepositoryConfig  configuration properties for the local repository.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    @PostMapping(path = "/local-repository/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setLocalRepositoryConfig",
               description="Set up the configuration for the local repository.  This overrides the current values.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))


    public VoidResponse setLocalRepositoryConfig(@PathVariable String                serverName,
                                                 @RequestBody  LocalRepositoryConfig localRepositoryConfig)
    {
        return adminAPI.setLocalRepositoryConfig(serverName, localRepositoryConfig);
    }


    /**
     * Return the configuration for the local repository.
     *
     * @param serverName  local server name.
     * @return local repository response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryConfig parameter.
     */
    @GetMapping(path = "/local-repository/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getLocalRepositoryConfig",
               description="Return the configuration for the local repository.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public LocalRepositoryConfigResponse getLocalRepositoryConfig(@PathVariable String serverName)
    {
        return adminAPI.getLocalRepositoryConfig(serverName);
    }


    /**
     * Remove all configuration for a local repository.  The default is no local repository.  This call
     * can be used to remove subsequent local repository configuration.
     *
     * @param serverName  local server name.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or localRepositoryMode parameter.
     */
    @DeleteMapping(path = "/local-repository/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearLocalRepositoryConfig",
               description="Remove all configuration for a local repository.  The default is no local repository.  This call" +
                                   " can be used to remove subsequent local repository configuration.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/connectors/#repository-and-event-mapper-connectors"))

    public VoidResponse clearLocalRepositoryConfig(@PathVariable String serverName)
    {
        return adminAPI.clearLocalRepositoryConfig(serverName);
    }


    /**
     * Update the URL broadcast across the cohort to allow other members to issue queries to this repository.
     * This method is needed to reconfigure a server that has moved from one platform to another.  Once the
     * URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.
     *
     * @param serverName  local server name.
     * @param requestBody  String url.
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName or serverURLRoot parameter.
     */
    @PostMapping(path = "/local-repository/configuration/remote-repository-connector-url")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="resetRemoteCohortURL",
               description="Update the URL broadcast across the cohort to allow other members to issue queries to this repository." +
                                   " This method is needed to reconfigure a server that has moved from one platform to another.  Once the" +
                                   " URL is updated, and the server restarted, it will broadcast its new URL to the rest of the cohort.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse resetRemoteCohortURL(@PathVariable String         serverName,
                                             @RequestBody  URLRequestBody requestBody)
    {
        return adminAPI.resetRemoteCohortURL(serverName, requestBody);
    }


    /**
     * Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a
     * cohort.  Use setCohortMode to delete a cohort.
     *
     * @param serverName  local server name
     * @param cohortName  name of the cohort
     * @param cohortConfig  configuration for the cohort
     * @return void response or
     * UserNotAuthorizedException the supplied userId is not authorized to issue this command or
     * InvalidParameterException invalid serverName, cohortName or cohortConfig parameter.
     */
    @PostMapping(path = "/cohorts/{cohortName}/configuration")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setCohortConfig",
               description="Set up the configuration properties for a cohort.  This may reconfigure an existing cohort or create a" +
                                   " cohort.  Use setCohortMode to delete a cohort.",
               externalDocs=@ExternalDocumentation(description="Further Information",
                                                   url="https://egeria-project.org/concepts/cohort-member/"))

    public VoidResponse setCohortConfig(@PathVariable String       serverName,
                                        @PathVariable String       cohortName,
                                        @RequestBody  CohortConfig cohortConfig)
    {
        return adminAPI.setCohortConfig(serverName, cohortName, cohortConfig);
    }
}
