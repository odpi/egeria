/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ValidValuesBuilder is used to manage the properties of a valid values set and definition.
 */
public class ValidValuesBuilder extends ReferenceableBuilder
{
    private final String  displayName;
    private final String  description;
    private final String  usage;
    private final String  scope;
    private final String  preferredValue;
    private final boolean isDeprecated;


    /**
     * Constructor supporting all common properties.
     *
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param usage guidance on how the schema should be used.
     * @param scope arena where this valid value is applicable.
     * @param preferredValue preferredValue where the schema is defined.
     * @param isDeprecated is the schema type deprecated
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ValidValuesBuilder(String               qualifiedName,
                       String               displayName,
                       String               description,
                       String               usage,
                       String               scope,
                       String               preferredValue,
                       boolean              isDeprecated,
                       Map<String, String>  additionalProperties,
                       Map<String, Object>  extendedProperties,
                       OMRSRepositoryHelper repositoryHelper,
                       String               serviceName,
                       String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_GUID,
              OpenMetadataAPIMapper.VALID_VALUE_DEFINITION_TYPE_NAME,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.usage = usage;
        this.scope = scope;
        this.preferredValue = preferredValue;
        this.isDeprecated = isDeprecated;
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
                                                                  OpenMetadataAPIMapper.VALID_VALUE_DISPLAY_NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.VALID_VALUE_DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
                                                                  usage,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.VALID_VALUE_SCOPE_PROPERTY_NAME,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.PREFERRED_VALUE_PROPERTY_NAME,
                                                                  preferredValue,
                                                                  methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        return properties;
    }
}
