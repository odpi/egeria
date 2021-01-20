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
    private String       displayName;
    private String       description;
    private String       connectorProviderClassName        = null;
    private List<String> recognizedAdditionalProperties    = null;
    private List<String> recognizedSecuredProperties       = null;
    private List<String> recognizedConfigurationProperties = null;


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name
     * @param description new description for the discovery engine
     * @param connectorProviderClassName class name of the connector provider
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
                                String               connectorProviderClassName,
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
        this.connectorProviderClassName = connectorProviderClassName;
        this.recognizedAdditionalProperties = recognizedAdditionalProperties;
        this.recognizedSecuredProperties = recognizedSecuredProperties;
        this.recognizedConfigurationProperties = recognizedConfigurationProperties;
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

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (connectorProviderClassName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.CONNECTOR_PROVIDER_PROPERTY_NAME,
                                                                      connectorProviderClassName,
                                                                      methodName);
        }

        if (recognizedAdditionalProperties != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.RECOGNIZED_ADD_PROPS_PROPERTY_NAME,
                                                                           recognizedAdditionalProperties,
                                                                           methodName);
        }

        if (recognizedSecuredProperties != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.RECOGNIZED_SEC_PROPS_PROPERTY_NAME,
                                                                           recognizedSecuredProperties,
                                                                           methodName);
        }

        if (recognizedConfigurationProperties != null)
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.RECOGNIZED_CONFIG_PROPS_PROPERTY_NAME,
                                                                           recognizedConfigurationProperties,
                                                                           methodName);
        }


        return properties;
    }
}
