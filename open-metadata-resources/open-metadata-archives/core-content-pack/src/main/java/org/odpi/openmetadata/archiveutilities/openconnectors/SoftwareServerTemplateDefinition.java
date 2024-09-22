/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;


import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.EgeriaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control.OMAGServerPlatformPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogTemplateType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.controls.UnityCatalogDeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A description of the default software server templates.
 * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
 * to the supplied connector type and an endpoint,
 */
public enum SoftwareServerTemplateDefinition implements TemplateDefinition
{
    POSTGRES_SERVER_TEMPLATE("542134e6-b9ce-4dce-8aef-22e8daf34fdb",
                             PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_MANAGER,
                             "Database Management System (DBMS)",
                             PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                             PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                             PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                             PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                             new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                             "jdbc:postgresql://" +
                                     PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                     PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/postgres",
                             null,
                             null,
                             PostgresPlaceholderProperty.getPostgresServerPlaceholderPropertyTypes(),
                             ContentPackDefinition.POSTGRES_CONTENT_PACK),

    APACHE_ATLAS_TEMPLATE("fe6dce45-a978-4417-ab55-17f05b8bcea7",
                          AtlasDeployedImplementationType.APACHE_ATLAS_SERVER,
                          DeployedImplementationType.ASSET_CATALOG,
                          "Metadata Catalog",
                          PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                          PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                          PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                          PlaceholderProperty.CONNECTION_PASSWORD.getPlaceholder(),
                          new ApacheAtlasRESTProvider().getConnectorType().getGUID(),
                          PlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                  PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                          null,
                          null,
                          PlaceholderProperty.getServerWithUserIdAndPasswordPlaceholderPropertyTypes(),
                          ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK),

    UNITY_CATALOG_SERVER_TEMPLATE(UnityCatalogTemplateType.UC_SERVER_TEMPLATE.getDefaultTemplateGUID(),
                                  UnityCatalogDeployedImplementationType.OSS_UNITY_CATALOG_SERVER,
                                  DeployedImplementationType.REST_API_MANAGER,
                                  "Unity Catalog REST API",
                                  PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                  PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                  null,
                                  null,
                                  new OSSUnityCatalogResourceProvider().getConnectorType().getGUID(),
                                  PlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                          PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                  null,
                                  null,
                                  UnityCatalogPlaceholderProperty.getServerPlaceholderPropertyTypes(),
                                  ContentPackDefinition.UNITY_CATALOG_CONTENT_PACK),

    OMAG_SERVER_PLATFORM_TEMPLATE("9b06c4dc-ddc8-47ae-b56b-28775d3a96f0",
                                  EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM,
                                  DeployedImplementationType.USER_AUTHENTICATION_MANAGER,
                                  "User Token Manager",
                                  OMAGServerPlatformPlaceholderProperty.PLATFORM_NAME.getPlaceholder(),
                                  OMAGServerPlatformPlaceholderProperty.PLATFORM_DESCRIPTION.getPlaceholder(),
                                  OMAGServerPlatformPlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                  null,
                                  new OMAGServerPlatformProvider().getConnectorType().getGUID(),
                                  OMAGServerPlatformPlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                          OMAGServerPlatformPlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                  null,
                                  null,
                                  OMAGServerPlatformPlaceholderProperty.getPlaceholderPropertyTypes(),
                                  ContentPackDefinition.CORE_CONTENT_PACK),

    ENGINE_HOST_TEMPLATE("1764a891-4234-45f1-8cc3-536af40c790d",
                         EgeriaDeployedImplementationType.ENGINE_HOST,
                         DeployedImplementationType.REST_API_MANAGER,
                         "Governance Engine Status APIs",
                         PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                         null,
                         new EngineHostProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                 PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                         getOMAGServerConfigProperties(),
                         null,
                         PlaceholderProperty.getServerWithUserIdOnlyPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    INTEGRATION_DAEMON_TEMPLATE("6b3516f0-dd13-4786-9601-07215f995197",
                                EgeriaDeployedImplementationType.INTEGRATION_DAEMON,
                                DeployedImplementationType.REST_API_MANAGER,
                                "Governance Engine Status APIs",
                                PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                null,
                                new IntegrationDaemonProvider().getConnectorType().getGUID(),
                                PlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                        PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                getOMAGServerConfigProperties(),
                                null,
                                PlaceholderProperty.getServerWithUserIdOnlyPlaceholderPropertyTypes(),
                                ContentPackDefinition.CORE_CONTENT_PACK),

    METADATA_ACCESS_SERVER_TEMPLATE("bd8de890-fa79-4c24-aab8-20b41b5893dd",
                                    EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER,
                                    DeployedImplementationType.REST_API_MANAGER,
                                    "Open Metadata Repository Access APIs",
                                    PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                    PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                    PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                    null,
                                    new MetadataAccessServerProvider().getConnectorType().getGUID(),
                                    PlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                            PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                    getOMAGServerConfigProperties(),
                                    null,
                                    PlaceholderProperty.getServerWithUserIdOnlyPlaceholderPropertyTypes(),
                                    ContentPackDefinition.CORE_CONTENT_PACK),

    VIEW_SERVER_TEMPLATE("fd61ca01-390d-4aa2-a55d-426826aa4e1b",
                         EgeriaDeployedImplementationType.VIEW_SERVER,
                         DeployedImplementationType.REST_API_MANAGER,
                         "Open Metadata and Governance End User APIs",
                         PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                         null,
                         new ViewServerProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                 PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                         getOMAGServerConfigProperties(),
                         null,
                         PlaceholderProperty.getServerWithUserIdOnlyPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    KAFKA_SERVER_TEMPLATE("5e1ff810-5418-43f7-b7c4-e6e062f9aff7",
                          KafkaDeployedImplementationType.APACHE_KAFKA_SERVER,
                          KafkaDeployedImplementationType.APACHE_KAFKA_EVENT_BROKER,
                          OpenMetadataType.EVENT_BROKER.typeName,
                          PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                          PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                          null,
                          null,
                          new ApacheKafkaAdminProvider().getConnectorType().getGUID(),
                          PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                  PlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                          null,
                          null,
                          KafkaPlaceholderProperty.getKafkaServerPlaceholderPropertyTypes(),
                          ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK),

    ;


    private static Map<String, Object> getOMAGServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PlaceholderProperty.SERVER_NAME.getName(), PlaceholderProperty.SERVER_NAME.getPlaceholder());

        return configurationProperties;
    }


    private final String                               guid;
    private final DeployedImplementationTypeDefinition deployedImplementationType;
    private final DeployedImplementationTypeDefinition softwareCapabilityType;
    private final String                               softwareCapabilityName;
    private final String                               serverName;
    private final String                               description;
    private final String                               userId;
    private final String                               password;
    private final String                               connectorTypeGUID;
    private final String                               networkAddress;
    private final Map<String, Object>                  configurationProperties;
    private final List<ReplacementAttributeType>       replacementAttributeTypes;
    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;
    private final ContentPackDefinition                contentPackDefinition;


    /**
     * Construct the description of the template.
     *
     * @param guid                             fixed unique identifier
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param softwareCapabilityType           type of the associated capability
     * @param softwareCapabilityName           name for the associated capability
     * @param serverName                       name for the server
     * @param description                      description for the server
     * @param userId                           userId for the connection
     * @param password                         password for the connection
     * @param connectorTypeGUID                connector type to link to the connection
     * @param networkAddress                   network address for the endpoint
     * @param configurationProperties          additional properties for the connection
     * @param replacementAttributeTypes        attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     * @param contentPackDefinition            which content pack does this server belong?
     */
    SoftwareServerTemplateDefinition(String                               guid,
                                     DeployedImplementationTypeDefinition deployedImplementationType,
                                     DeployedImplementationTypeDefinition softwareCapabilityType,
                                     String                               softwareCapabilityName,
                                     String                               serverName,
                                     String                               description,
                                     String                               userId,
                                     String                               password,
                                     String                               connectorTypeGUID,
                                     String                               networkAddress,
                                     Map<String, Object>                  configurationProperties,
                                     List<ReplacementAttributeType>       replacementAttributeTypes,
                                     List<PlaceholderPropertyType>        placeholderPropertyTypes,
                                     ContentPackDefinition                contentPackDefinition)
    {
        this.guid                       = guid;
        this.deployedImplementationType = deployedImplementationType;
        this.softwareCapabilityType     = softwareCapabilityType;
        this.softwareCapabilityName     = softwareCapabilityName;
        this.serverName                 = serverName;
        this.description                = description;
        this.userId                     = userId;
        this.password                   = password;
        this.connectorTypeGUID          = connectorTypeGUID;
        this.networkAddress             = networkAddress;
        this.configurationProperties    = configurationProperties;
        this.replacementAttributeTypes  = replacementAttributeTypes;
        this.placeholderPropertyTypes   = placeholderPropertyTypes;
        this.contentPackDefinition      = contentPackDefinition;
    }


    /**
     * Return the unique identifier of the template.
     *
     * @return name
     */
    @Override
    public String getTemplateGUID()
    {
        return guid;
    }


    /**
     * Return the name to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateName()
    {
        return deployedImplementationType.getDeployedImplementationType() + " template";
    }

    /**
     * Return the description to go in the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateDescription()
    {
        return "Create a " + deployedImplementationType.getDeployedImplementationType() + " SoftwareServer with an associated SoftwareCapability and Connection.";
    }


    /**
     * Return the version identifier for the template classification.
     *
     * @return string
     */
    @Override
    public String getTemplateVersionIdentifier()
    {
        return "V1.0";
    }


    /**
     * Return the supported deployed implementation for this template.
     *
     * @return enum
     */
    @Override
    public DeployedImplementationTypeDefinition getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Return the type of associated capability.
     *
     * @return enum
     */
    public DeployedImplementationTypeDefinition getSoftwareCapabilityType()
    {
        return softwareCapabilityType;
    }

    /**
     * Return the name for the associated capability.
     *
     * @return string
     */
    public String getSoftwareCapabilityName()
    {
        return softwareCapabilityName;
    }


    /**
     * Return the name of the server.
     *
     * @return string
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Return the server description.
     *
     * @return string
     */
    public String getServerDescription()
    {
        return description;
    }


    /**
     * Return the user id for tha connection.
     *
     * @return string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Return the password for the connection.
     *
     * @return string
     */
    public String getPassword()
    {
        return password;
    }


    /**
     * Return the connector type GUID for the connection.
     *
     * @return string
     */
    public String getConnectorTypeGUID()
    {
        return connectorTypeGUID;
    }


    /**
     * Return the endpoint address.
     *
     * @return string
     */
    public String getNetworkAddress()
    {
        return networkAddress;
    }


    /**
     * Return the configuration properties for the connection.
     *
     * @return map
     */
    public Map<String, Object> getConfigurationProperties()
    {
        return configurationProperties;
    }


    /**
     * Return the list of placeholders supported by this template.
     *
     * @return list of placeholder types
     */
    @Override
    public List<PlaceholderPropertyType> getPlaceholders()
    {
        return placeholderPropertyTypes;
    }


    /**
     * Return the list of attributes that should be supplied by the caller using this template.
     *
     * @return list of replacement attributes
     */
    @Override
    public List<ReplacementAttributeType> getReplacementAttributes()
    {
        return replacementAttributeTypes;
    }


    /**
     * Get identifier of content pack where this template should be located.
     *
     * @return content pack definition
     */
    public ContentPackDefinition getContentPackDefinition()
    {
        return contentPackDefinition;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TemplateDefinition{templateName='" + getTemplateName() + "'}";
    }
}
