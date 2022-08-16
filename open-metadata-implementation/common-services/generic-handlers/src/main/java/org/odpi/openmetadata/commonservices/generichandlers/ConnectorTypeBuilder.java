/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * ConnectorTypeBuilder is able to build the properties for an ConnectorType entity from an ConnectorType bean.
 */
public class ConnectorTypeBuilder extends ReferenceableBuilder
{
    private final String       displayName;
    private final String       description;

    private String       supportedAssetTypeName            = null;
    private String       expectedDataFormat                = null;
    private String       connectorProviderClassName        = null;
    private String       connectorFrameworkName            = null;
    private String       connectorInterfaceLanguage        = null;
    private List<String> connectorInterfaces               = null;
    private String       targetTechnologySource            = null;
    private String       targetTechnologyName              = null;
    private List<String> targetTechnologyInterfaces        = null;
    private List<String> targetTechnologyVersions          = null;
    private List<String> recognizedAdditionalProperties    = null;
    private List<String> recognizedConfigurationProperties = null;
    private List<String> recognizedSecuredProperties       = null;


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name
     * @param description new description for the connector type
     * @param supportedAssetTypeName the type of asset that the connector implementation supports
     * @param expectedDataFormat the format of the data that the connector supports - null for "any"
     * @param connectorProviderClassName class name of the connector provider
     * @param connectorFrameworkName name of the connector framework that the connector implements - default Open Connector Framework (OCF)
     * @param connectorInterfaceLanguage the language that the connector is implemented in - default Java
     * @param connectorInterfaces list of interfaces that the connector supports
     * @param targetTechnologySource the organization that supplies the target technology that the connector implementation connects to
     * @param targetTechnologyName the name of the target technology that the connector implementation connects to
     * @param targetTechnologyInterfaces the names of the interfaces in the target technology that the connector calls
     * @param targetTechnologyVersions the versions of the target technology that the connector supports
     * @param recognizedAdditionalProperties property name for additionalProperties in a linked Connection object
     * @param recognizedSecuredProperties property name for securedProperties in a linked Connection object
     * @param recognizedConfigurationProperties property name for configurationProperties in a linked Connection object
     * @param additionalProperties additional properties
     * @param typeId unique identifier of the type for the connector type
     * @param typeName unique name of the type for the connector type
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ConnectorTypeBuilder(String               qualifiedName,
                                String               displayName,
                                String               description,
                                String               supportedAssetTypeName,
                                String               expectedDataFormat,
                                String               connectorProviderClassName,
                                String               connectorFrameworkName,
                                String               connectorInterfaceLanguage,
                                List<String>         connectorInterfaces,
                                String               targetTechnologySource,
                                String               targetTechnologyName,
                                List<String>         targetTechnologyInterfaces,
                                List<String>         targetTechnologyVersions,
                                List<String>         recognizedAdditionalProperties,
                                List<String>         recognizedSecuredProperties,
                                List<String>         recognizedConfigurationProperties,
                                Map<String, String>  additionalProperties,
                                String               typeId,
                                String               typeName,
                                Map<String, Object>  extendedProperties,
                                OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeId,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.supportedAssetTypeName = supportedAssetTypeName;
        this.expectedDataFormat = expectedDataFormat;
        this.connectorProviderClassName = connectorProviderClassName;
        this.connectorFrameworkName = connectorFrameworkName;
        this.connectorInterfaceLanguage = connectorInterfaceLanguage;
        this.connectorInterfaces = connectorInterfaces;
        this.targetTechnologySource = targetTechnologySource;
        this.targetTechnologyName = targetTechnologyName;
        this.targetTechnologyInterfaces = targetTechnologyInterfaces;
        this.targetTechnologyVersions = targetTechnologyVersions;
        this.recognizedAdditionalProperties = recognizedAdditionalProperties;
        this.recognizedSecuredProperties = recognizedSecuredProperties;
        this.recognizedConfigurationProperties = recognizedConfigurationProperties;
    }


    /**
     * Create constructor - when templating
     *
     * @param qualifiedName unique name for the connector type
     * @param displayName short display name for the connector type
     * @param description description of the connector type
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ConnectorTypeBuilder(String               qualifiedName,
                         String               displayName,
                         String               description,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_GUID,
              OpenMetadataAPIMapper.CONNECTOR_TYPE_TYPE_NAME,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SUPPORTED_ASSET_TYPE_NAME,
                                                                  supportedAssetTypeName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.EXPECTED_DATA_FORMAT,
                                                                  expectedDataFormat,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CONNECTOR_PROVIDER_PROPERTY_NAME,
                                                                  connectorProviderClassName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CONNECTOR_FRAMEWORK_NAME,
                                                                  connectorFrameworkName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CONNECTOR_INTERFACE_LANGUAGE,
                                                                  connectorInterfaceLanguage,
                                                                  methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.CONNECTOR_INTERFACES,
                                                                       connectorInterfaces,
                                                                       methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TARGET_TECHNOLOGY_SOURCE,
                                                                  targetTechnologySource,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TARGET_TECHNOLOGY_NAME,
                                                                  targetTechnologyName,
                                                                  methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.TARGET_TECHNOLOGY_INTERFACES,
                                                                       targetTechnologyInterfaces,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.TARGET_TECHNOLOGY_VERSIONS,
                                                                       targetTechnologyVersions,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.RECOGNIZED_ADD_PROPS_PROPERTY_NAME,
                                                                       recognizedAdditionalProperties,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.RECOGNIZED_SEC_PROPS_PROPERTY_NAME,
                                                                       recognizedSecuredProperties,
                                                                       methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                       properties,
                                                                       OpenMetadataAPIMapper.RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME,
                                                                       recognizedConfigurationProperties,
                                                                       methodName);

        return properties;
    }
}
