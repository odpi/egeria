/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ConnectionMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ConnectionBuilder is able to build the properties for a Connection entity from a Connection bean.
 */
public class ConnectionBuilder extends ReferenceableBuilder
{
    private String              displayName;
    private String              description;
    private Map<String, String> securedProperties       = null;
    private Map<String, Object> configurationProperties = null;
    private String              connectorUserId         = null;
    private String              clearPassword           = null;
    private String              encryptedPassword       = null;


    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of connection
     * @param description description of connection
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ConnectionBuilder(String               qualifiedName,
                             String               displayName,
                             String               description,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new description for the connection.
     * @param additionalProperties additional properties
     * @param securedProperties protected properties
     * @param configurationProperties  properties passed to configure underlying technologies
     * @param connectorUserId user identity that the connector should use
     * @param clearPassword password for the userId in clear text
     * @param encryptedPassword encrypted password that the connector needs to decrypt before use
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ConnectionBuilder(String                   qualifiedName,
                             String                   displayName,
                             String                   description,
                             Map<String, String>      additionalProperties,
                             Map<String, String>      securedProperties,
                             Map<String, Object>      configurationProperties,
                             String                   connectorUserId,
                             String                   clearPassword,
                             String                   encryptedPassword,
                             Map<String, Object>      extendedProperties,
                             OMRSRepositoryHelper     repositoryHelper,
                             String                   serviceName,
                             String                   serverName)
    {
        super(qualifiedName,
              additionalProperties,
              ConnectionMapper.CONNECTION_TYPE_NAME,
              ConnectionMapper.CONNECTION_TYPE_GUID,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.securedProperties = securedProperties;
        this.configurationProperties = configurationProperties;
        this.connectorUserId = connectorUserId;
        this.clearPassword = clearPassword;
        this.encryptedPassword = encryptedPassword;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (securedProperties != null)
        {
            properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                         properties,
                                                                         ConnectionMapper.SECURED_PROPERTIES_PROPERTY_NAME,
                                                                         securedProperties,
                                                                         methodName);
        }

        if (configurationProperties != null)
        {
            properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                                   properties,
                                                                   ConnectionMapper.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                   configurationProperties,
                                                                   methodName);
        }

        if (connectorUserId != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.USER_ID_PROPERTY_NAME,
                                                                      connectorUserId,
                                                                      methodName);
        }

        if (clearPassword != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.CLEAR_PASSWORD_PROPERTY_NAME,
                                                                      clearPassword,
                                                                      methodName);
        }

        if (encryptedPassword != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.ENCRYPTED_PASSWORD_PROPERTY_NAME,
                                                                      encryptedPassword,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return super.getNameInstanceProperties(methodName);
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      ConnectionMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        return properties;
    }
}
