/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.openconnectors;


import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A description of the default software server templates.
 * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
 * to the supplied connector type and an endpoint,
 */
public enum DataAssetTemplateDefinition implements TemplateDefinition
{
    POSTGRES_DATABASE_TEMPLATE(PostgreSQLTemplateType.POSTGRES_DATABASE_TEMPLATE.getTemplateGUID(),
                               PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                               PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                               PostgresPlaceholderProperty.DATABASE_DESCRIPTION.getPlaceholder(),
                               PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                               new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                               "jdbc:postgresql://" +
                                       PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                       PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                               null,
                               SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName(),
                               new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                               PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                               null,
                               PostgresPlaceholderProperty.getPostgresDatabasePlaceholderPropertyTypes(),
                               ContentPackDefinition.POSTGRES_CONTENT_PACK),

    POSTGRES_SCHEMA_TEMPLATE(PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE.getTemplateGUID(),
                             PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                             PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                             PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholder(),
                             PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                             new JDBCResourceConnectorProvider().getConnectorType().getGUID(),
                             "jdbc:postgresql://" +
                                     PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                     PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
                                     PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "?currentSchema=" +
                                     PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                             getPostgresSchemaConfigurationProperties(),
                             SecretsStorePurpose.REST_BASIC_AUTHENTICATION.getName(),
                             new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                             PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                             null,
                             PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes(),
                             ContentPackDefinition.POSTGRES_CONTENT_PACK),

    KAFKA_TOPIC_TEMPLATE(KafkaTemplateType.KAFKA_TOPIC_TEMPLATE.getTemplateGUID(),
                         DeployedImplementationType.APACHE_KAFKA_TOPIC,
                         KafkaPlaceholderProperty.SHORT_TOPIC_NAME.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         PlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + KafkaPlaceholderProperty.FULL_TOPIC_NAME.getPlaceholder() + ":inOut",
                         new KafkaOpenMetadataTopicProvider().getConnectorType().getGUID(),
                         KafkaPlaceholderProperty.FULL_TOPIC_NAME.getPlaceholder(),
                         getKafkaConfigurationProperties(),
                         null,
                         null,
                         null,
                         null,
                         KafkaPlaceholderProperty.getKafkaTopicPlaceholderPropertyTypes(),
                         ContentPackDefinition.CORE_CONTENT_PACK),

    ;


    /**
     * Build the configuration properties for a kafka topic.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getKafkaConfigurationProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();
        Map<String, String> bootstrapServersProperties = new HashMap<>();

        bootstrapServersProperties.put("bootstrap.servers",
                                       PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                               PlaceholderProperty.PORT_NUMBER.getPlaceholder());

        configurationProperties.put(KafkaPlaceholderProperty.EVENT_DIRECTION.getName(), "inOut");
        configurationProperties.put("producer", bootstrapServersProperties);
        configurationProperties.put("consumer", bootstrapServersProperties);

        return configurationProperties;
    }


    /**
     * Build the configuration properties for a kafka topic.
     *
     * @return configuration properties
     */
    private static Map<String, Object> getPostgresSchemaConfigurationProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(JDBCConfigurationProperty.DATABASE_NAME.getName(), PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder());
        configurationProperties.put(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(), PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder());

        return configurationProperties;
    }


    private final String                               guid;
    private final DeployedImplementationTypeDefinition deployedImplementationType;
    private final String                               assetName;
    private final String                               assetDescription;
    private final String                               serverName;
    private final String                               connectorTypeGUID;
    private final String                               networkAddress;
    private final Map<String, Object>                  configurationProperties;
    private final String                               secretsStorePurpose;
    private final String                               secretsStoreConnectorTypeGUID;
    private final String                               secretsStoreFileName;
    private final List<ReplacementAttributeType>       replacementAttributeTypes;
    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;
    private final ContentPackDefinition                contentPackDefinition;


    /**
     * Construct the description of the template.
     *
     * @param guid fixed unique identifier
     * @param deployedImplementationType deployed implementation type for the technology
     * @param assetName name for the asset
     * @param assetDescription description
     * @param serverName optional server name
     * @param connectorTypeGUID connector type to link to the connection
     * @param networkAddress network address for the endpoint
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param configurationProperties  additional properties for the connection
     * @param replacementAttributeTypes attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes placeholder variables used in the supplied parameters
     * @param contentPackDefinition            which content pack does this server belong?
     */
    DataAssetTemplateDefinition(String                               guid,
                                DeployedImplementationTypeDefinition deployedImplementationType,
                                String                               assetName,
                                String                               assetDescription,
                                String                               serverName,
                                String                               connectorTypeGUID,
                                String                               networkAddress,
                                Map<String, Object>                  configurationProperties,
                                String                               secretsStorePurpose,
                                String                               secretsStoreConnectorTypeGUID,
                                String                               secretsStoreFileName,
                                List<ReplacementAttributeType>       replacementAttributeTypes,
                                List<PlaceholderPropertyType>        placeholderPropertyTypes,
                                ContentPackDefinition                contentPackDefinition)
    {
        this.guid                          = guid;
        this.deployedImplementationType    = deployedImplementationType;
        this.assetName                     = assetName;
        this.assetDescription              = assetDescription;
        this.serverName                    = serverName;
        this.connectorTypeGUID             = connectorTypeGUID;
        this.networkAddress                = networkAddress;
        this.configurationProperties       = configurationProperties;
        this.secretsStorePurpose           = secretsStorePurpose;
        this.secretsStoreConnectorTypeGUID = secretsStoreConnectorTypeGUID;
        this.secretsStoreFileName          = secretsStoreFileName;
        this.replacementAttributeTypes     = replacementAttributeTypes;
        this.placeholderPropertyTypes      = placeholderPropertyTypes;
        this.contentPackDefinition         = contentPackDefinition;
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
     * Return the unique name to use in the template.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
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
        return "Create a " + deployedImplementationType.getDeployedImplementationType() + " asset with an associated Connection.";
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
     * Return the value to use in the element that describes its version.
     *
     * @return version identifier placeholder
     */
    @Override
    public String getElementVersionIdentifier()
    {
        return PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholder();
    }


    /**
     * Return the name of the asset.
     *
     * @return enum
     */
    public String getAssetName()
    {
        return assetName;
    }

    /**
     * Return the name for the associated capability.
     *
     * @return string
     */
    public String getAssetDescription()
    {
        return assetDescription;
    }


    /**
     * Return the name of the server where this asset resides.
     *
     * @return string
     */
    public String getServerName()
    {
        return serverName;
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
     * Return the purpose of the secrets store.
     *
     * @return name
     */
    public String getSecretsStorePurpose()
    {
        return secretsStorePurpose;
    }

    /**
     * Return the optional secrets store connector provider for the server.
     *
     * @return connector provider
     */
    public String getSecretsStoreConnectorTypeGUID()
    {
        return secretsStoreConnectorTypeGUID;
    }


    /**
     * Return the location of the secrets store.
     *
     * @return path name of file
     */
    public String getSecretsStoreFileName()
    {
        return secretsStoreFileName;
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
