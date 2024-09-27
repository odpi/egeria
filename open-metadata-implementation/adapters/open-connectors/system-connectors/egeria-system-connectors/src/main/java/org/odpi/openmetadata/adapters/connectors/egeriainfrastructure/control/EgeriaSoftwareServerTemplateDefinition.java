/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.control;


import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerProvider;
import org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerProvider;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.ReplacementAttributeType;
import org.odpi.openmetadata.frameworks.openmetadata.controls.TemplateDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationTypeDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A description of the default Egeria software server templates.
 * The template consists of a SoftwareServer asset linked to a software capability, plus a connection, linked
 * to the supplied connector type and an endpoint,
 */
public enum EgeriaSoftwareServerTemplateDefinition implements TemplateDefinition
{
    OMAG_SERVER_PLATFORM_TEMPLATE("9b06c4dc-ddc8-47ae-b56b-28775d3a96f0",
                                  EgeriaDeployedImplementationType.OMAG_SERVER_PLATFORM,
                                  DeployedImplementationType.USER_AUTHENTICATION_MANAGER,
                                  "User Token Manager",
                                  OMAGServerPlatformPlaceholderProperty.PLATFORM_NAME.getPlaceholder(),
                                  null,
                                  OMAGServerPlatformPlaceholderProperty.PLATFORM_DESCRIPTION.getPlaceholder(),
                                  OMAGServerPlatformPlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                  new OMAGServerPlatformProvider().getConnectorType().getGUID(),
                                  OMAGServerPlatformPlaceholderProperty.HOST_URL.getPlaceholder() + ":" +
                                          OMAGServerPlatformPlaceholderProperty.PORT_NUMBER.getPlaceholder(),
                                  getOMAGServerPlatformConfigProperties(),
                                  null,
                                  null,
                                  null,
                                  null,
                                  OMAGServerPlatformPlaceholderProperty.getPlaceholderPropertyTypes()),

    ENGINE_HOST_TEMPLATE("1764a891-4234-45f1-8cc3-536af40c790d",
                         EgeriaDeployedImplementationType.ENGINE_HOST,
                         DeployedImplementationType.REST_API_MANAGER,
                         "Governance Engine Status APIs",
                         PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                         PlaceholderProperty.SERVER_ID.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                         new EngineHostProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder(),
                         getOMAGServerConfigProperties(),
                         null,
                         null,
                         null,
                         null,
                         getOMAGServerPlaceholderPropertyTypes()),

    INTEGRATION_DAEMON_TEMPLATE("6b3516f0-dd13-4786-9601-07215f995197",
                                EgeriaDeployedImplementationType.INTEGRATION_DAEMON,
                                DeployedImplementationType.REST_API_MANAGER,
                                "Governance Engine Status APIs",
                                PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                PlaceholderProperty.SERVER_ID.getPlaceholder(),
                                PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                new IntegrationDaemonProvider().getConnectorType().getGUID(),
                                PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder(),
                                getOMAGServerConfigProperties(),
                                null,
                                null,
                                null,
                                null,
                                getOMAGServerPlaceholderPropertyTypes()),

    METADATA_ACCESS_SERVER_TEMPLATE("bd8de890-fa79-4c24-aab8-20b41b5893dd",
                                    EgeriaDeployedImplementationType.METADATA_ACCESS_SERVER,
                                    DeployedImplementationType.REST_API_MANAGER,
                                    "Open Metadata Repository Access APIs",
                                    PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                    PlaceholderProperty.SERVER_ID.getPlaceholder(),
                                    PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                                    PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                                    new MetadataAccessServerProvider().getConnectorType().getGUID(),
                                    PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder(),
                                    getOMAGServerConfigProperties(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    getOMAGServerPlaceholderPropertyTypes()),

    VIEW_SERVER_TEMPLATE("fd61ca01-390d-4aa2-a55d-426826aa4e1b",
                         EgeriaDeployedImplementationType.VIEW_SERVER,
                         DeployedImplementationType.REST_API_MANAGER,
                         "Open Metadata and Governance End User APIs",
                         PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                         PlaceholderProperty.SERVER_ID.getPlaceholder(),
                         PlaceholderProperty.DESCRIPTION.getPlaceholder(),
                         PlaceholderProperty.CONNECTION_USER_ID.getPlaceholder(),
                         new ViewServerProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholder(),
                         getOMAGServerConfigProperties(),
                         SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                         new YAMLSecretsStoreProvider().getConnectorType().getGUID(),
                         PlaceholderProperty.SECRETS_STORE.getPlaceholder(),
                         null,
                         getViewServerPlaceholderPropertyTypes()),

    ;


    /**
     * Set up the configuration properties for an OMAG Server
     *
     * @return configuration properties map
     */
    private static Map<String, Object> getOMAGServerConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PlaceholderProperty.SERVER_NAME.getName(), PlaceholderProperty.SERVER_NAME.getPlaceholder());

        return configurationProperties;
    }


    /**
     * Set up the configuration properties for an OMAG Server
     *
     * @return configuration properties map
     */
    private static Map<String, Object> getOMAGServerPlatformConfigProperties()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put(PlaceholderProperty.SECRETS_STORE.getName(), PlaceholderProperty.SECRETS_STORE.getPlaceholder());

        return configurationProperties;
    }


    public static List<PlaceholderPropertyType> getOMAGServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    public static List<PlaceholderPropertyType> getViewServerPlaceholderPropertyTypes()
    {
        List<PlaceholderPropertyType> placeholderPropertyTypes = new ArrayList<>();

        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NETWORK_ADDRESS.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_NAME.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SERVER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.DESCRIPTION.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.VERSION_IDENTIFIER.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.CONNECTION_USER_ID.getPlaceholderType());
        placeholderPropertyTypes.add(PlaceholderProperty.SECRETS_STORE.getPlaceholderType());

        return placeholderPropertyTypes;
    }


    private final String                               guid;
    private final DeployedImplementationTypeDefinition deployedImplementationType;
    private final DeployedImplementationTypeDefinition softwareCapabilityType;
    private final String                               softwareCapabilityName;
    private final String                               serverName;
    private final String                               serverId;
    private final String                               description;
    private final String                               userId;
    private final String                               connectorTypeGUID;
    private final String                               networkAddress;
    private final Map<String, Object>                  configurationProperties;
    private final String                               secretsStorePurpose;
    private final String                               secretsStoreConnectorTypeGUID;
    private final String                               secretsStoreFileName;
    private final List<ReplacementAttributeType>       replacementAttributeTypes;
    private final List<PlaceholderPropertyType>        placeholderPropertyTypes;


    /**
     * Construct the description of the template.
     *
     * @param guid                             fixed unique identifier
     * @param deployedImplementationType       deployed implementation type for the technology
     * @param softwareCapabilityType           type of the associated capability
     * @param softwareCapabilityName           name for the associated capability
     * @param serverName                       name for the server
     * @param serverId                         id for the server
     * @param description                      description for the server
     * @param userId                           userId for the connection
     * @param connectorTypeGUID                connector type to link to the connection
     * @param networkAddress                   network address for the endpoint
     * @param configurationProperties          additional properties for the connection
     * @param secretsStorePurpose              type of authentication information provided by the secrets store
     * @param secretsStoreConnectorTypeGUID    optional connector type for secrets store
     * @param secretsStoreFileName             location of the secrets store
     * @param replacementAttributeTypes        attributes that should have a replacement value to successfully use the template
     * @param placeholderPropertyTypes         placeholder variables used in the supplied parameters
     */
    EgeriaSoftwareServerTemplateDefinition(String                               guid,
                                           DeployedImplementationTypeDefinition deployedImplementationType,
                                           DeployedImplementationTypeDefinition softwareCapabilityType,
                                           String                               softwareCapabilityName,
                                           String                               serverName,
                                           String                               serverId,
                                           String                               description,
                                           String                               userId,
                                           String                               connectorTypeGUID,
                                           String                               networkAddress,
                                           Map<String, Object>                  configurationProperties,
                                           String                               secretsStorePurpose,
                                           String                               secretsStoreConnectorTypeGUID,
                                           String                               secretsStoreFileName,
                                           List<ReplacementAttributeType>       replacementAttributeTypes,
                                           List<PlaceholderPropertyType>        placeholderPropertyTypes)
    {
        this.guid                          = guid;
        this.deployedImplementationType    = deployedImplementationType;
        this.softwareCapabilityType        = softwareCapabilityType;
        this.softwareCapabilityName        = softwareCapabilityName;
        this.serverName                    = serverName;
        this.serverId                      = serverId;
        this.description                   = description;
        this.userId                        = userId;
        this.connectorTypeGUID             = connectorTypeGUID;
        this.networkAddress                = networkAddress;
        this.configurationProperties       = configurationProperties;
        this.secretsStorePurpose           = secretsStorePurpose;
        this.secretsStoreConnectorTypeGUID = secretsStoreConnectorTypeGUID;
        this.secretsStoreFileName          = secretsStoreFileName;
        this.replacementAttributeTypes     = replacementAttributeTypes;
        this.placeholderPropertyTypes      = placeholderPropertyTypes;
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
        if (serverId == null)
        {
            return deployedImplementationType.getDeployedImplementationType() + ":" + serverName;
        }
        else
        {
            return deployedImplementationType.getDeployedImplementationType() + ":" + serverName + "[" + serverId + "]";
        }
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
     * Return the id of the server.
     *
     * @return string
     */
    public String getServerId()
    {
        return serverId;
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
