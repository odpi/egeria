/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ConnectionBuilder is able to build the properties for both a Connection entity and a VirtualConnection entity.
 */
public class ConnectionBuilder extends ReferenceableBuilder
{
    private final String displayName;
    private final String description;

    private Map<String, String> securedProperties;
    private Map<String, Object> configurationProperties;
    private String              connectorUserId;
    private String              clearPassword;
    private String              encryptedPassword;


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
     * @param typeId identifier of the type for this connection
     * @param typeName name of the type for this connection
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ConnectionBuilder(String                   qualifiedName,
                      String                   displayName,
                      String                   description,
                      Map<String, String>      additionalProperties,
                      Map<String, String>      securedProperties,
                      Map<String, Object>      configurationProperties,
                      String                   connectorUserId,
                      String                   clearPassword,
                      String                   encryptedPassword,
                      String                   typeId,
                      String                   typeName,
                      Map<String, Object>      extendedProperties,
                      OMRSRepositoryHelper     repositoryHelper,
                      String                   serviceName,
                      String                   serverName)
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
        this.securedProperties = securedProperties;
        this.configurationProperties = configurationProperties;
        this.connectorUserId = connectorUserId;
        this.clearPassword = clearPassword;
        this.encryptedPassword = encryptedPassword;
    }


    /**
     * Create constructor - when templating
     *
     * @param qualifiedName unique name for the connection
     * @param displayName short display name for the connection
     * @param description description of the connection
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ConnectionBuilder(String               qualifiedName,
                      String               displayName,
                      String               description,
                      OMRSRepositoryHelper repositoryHelper,
                      String               serviceName,
                      String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataType.CONNECTION_TYPE_GUID,
              OpenMetadataType.CONNECTION_TYPE_NAME,
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
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                     properties,
                                                                     OpenMetadataType.SECURED_PROPERTIES_PROPERTY_NAME,
                                                                     securedProperties,
                                                                     methodName);

        properties = repositoryHelper.addMapPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataType.CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                               configurationProperties,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.USER_ID_PROPERTY_NAME,
                                                                  connectorUserId,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.CLEAR_PASSWORD_PROPERTY_NAME,
                                                                  clearPassword,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.ENCRYPTED_PASSWORD_PROPERTY_NAME,
                                                                  encryptedPassword,
                                                                  methodName);

        return properties;
    }
}
