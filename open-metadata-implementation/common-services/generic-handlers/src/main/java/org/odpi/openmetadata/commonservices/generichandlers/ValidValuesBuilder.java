/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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
    private final String  category;
    private final String  usage;
    private final String  scope;
    private final String  preferredValue;
    private final String  dataType;
    private final boolean isDeprecated;
    private final boolean isCaseSensitive;


    /**
     * Constructor supporting all common properties.
     *
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param category   what is the category of reference data does this fall into?
     * @param usage guidance on how the schema should be used.
     * @param scope arena where this valid value is applicable.
     * @param preferredValue preferredValue where the schema is defined.
     * @param dataType the data type of the preferred value.
     * @param isDeprecated is the valid value deprecated
     * @param isCaseSensitive is the valid value case-sensitive
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ValidValuesBuilder(String               qualifiedName,
                       String               displayName,
                       String               description,
                       String               category,
                       String               usage,
                       String               scope,
                       String               preferredValue,
                       String               dataType,
                       boolean              isDeprecated,
                       boolean              isCaseSensitive,
                       Map<String, String>  additionalProperties,
                       Map<String, Object>  extendedProperties,
                       OMRSRepositoryHelper repositoryHelper,
                       String               serviceName,
                       String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.VALID_VALUE_DEFINITION.typeGUID,
              OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.category = category;
        this.usage = usage;
        this.scope = scope;
        this.preferredValue = preferredValue;
        this.dataType = dataType;
        this.isDeprecated = isDeprecated;
        this.isCaseSensitive = isCaseSensitive;
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
                                                                  OpenMetadataProperty.NAME.name,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.CATEGORY_PROPERTY_NAME,
                                                                  category,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.USAGE_PROPERTY_NAME,
                                                                  usage,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.SCOPE_PROPERTY_NAME,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.PREFERRED_VALUE_PROPERTY_NAME,
                                                                  preferredValue,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataType.DATA_TYPE_PROPERTY_NAME,
                                                                  dataType,
                                                                  methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataType.IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataType.IS_CASE_SENSITIVE_PROPERTY_NAME,
                                                                   isCaseSensitive,
                                                                   methodName);

        return properties;
    }
}
