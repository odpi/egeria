/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * UserIdentityBuilder creates the parts for an entity that represents a user identity.
 */
public class UserIdentityBuilder extends ReferenceableBuilder
{
    private final String userId;
    private String distinguishedName = null;

    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the user identity
     * @param userId user account identifier
     * @param distinguishedName LDAP distinguished name
     * @param additionalProperties additional properties for a user identity
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a user identity subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    UserIdentityBuilder(String               qualifiedName,
                        String               userId,
                        String               distinguishedName,
                        Map<String, String>  additionalProperties,
                        String               typeGUID,
                        String               typeName,
                        Map<String, Object>  extendedProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.userId = userId;
        this.distinguishedName = distinguishedName;
    }


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the user identity
     * @param userId user account identifier
     * @param additionalProperties additional properties for a user identity
     * @param extendedProperties  properties for a user identity subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    UserIdentityBuilder(String               qualifiedName,
                        String               userId,
                        Map<String, String>  additionalProperties,
                        Map<String, Object>  extendedProperties,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.USER_IDENTITY_TYPE_GUID,
              OpenMetadataAPIMapper.USER_IDENTITY_TYPE_NAME,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.userId = userId;
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
                                                                  OpenMetadataAPIMapper.USER_ID_PROPERTY_NAME,
                                                                  userId,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DISTINGUISHED_NAME_PROPERTY_NAME,
                                                                  distinguishedName,
                                                                  methodName);

        return properties;
    }
}
