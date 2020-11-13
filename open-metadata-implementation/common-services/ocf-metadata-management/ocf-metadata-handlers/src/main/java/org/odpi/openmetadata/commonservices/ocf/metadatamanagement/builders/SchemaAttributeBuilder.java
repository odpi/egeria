/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * SchemaAttributeBuilder creates instance properties for a schema attribute.
 */
public class SchemaAttributeBuilder extends ReferenceableBuilder
{
    private String            displayName;
    private String            description           = null;
    private int               elementPosition       = 0;
    private int               minCardinality        = 0;
    private int               maxCardinality        = 0;
    private boolean           isDeprecated          = false;
    private String            cardinality           = null;
    private boolean           allowsDuplicateValues = false;
    private boolean           orderedValues         = false;
    private EnumPropertyValue sortOrder             = null;
    private int               minimumLength         = 0;
    private int               length                = 0;
    private int               significantDigits     = 0;
    private boolean           isNullable            = true;
    private String            defaultValueOverride  = null;
    private String            nativeJavaClass       = null;
    private List<String>      aliases               = null;
    private String            formula               = null;


    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               displayName,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
              SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
    }


    /**
     * Constructor supporting original properties. Deprecated because uses old form of cardinality.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param elementPosition position of the attribute in the parent schemaType.
     * @param cardinality does the attribute repeat?
     * @param defaultValueOverride override for the Type's default value.
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    @Deprecated
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               displayName,
                                  int                  elementPosition,
                                  String               cardinality,
                                  String               defaultValueOverride,
                                  Map<String, String>  additionalProperties,
                                  Map<String, Object>  extendedProperties,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
              SchemaElementMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName          = displayName;
        this.elementPosition      = elementPosition;
        this.cardinality          = cardinality;
        this.minCardinality       = 0;
        this.maxCardinality       = 0;
        this.defaultValueOverride = defaultValueOverride;
    }


    /**
     * Constructor supporting all properties for a schema attribute entity.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param elementPosition position of the attribute in the parent schemaType.
     * @param minCardinality is the attribute optional?
     * @param maxCardinality does the attribute repeat?
     * @param isDeprecated is this element deprecated?
     * @param defaultValueOverride override for the Type's default value.
     * @param allowsDuplicateValues unique values ?
     * @param orderedValues ordered values ?
     * @param sortOrder sort order if ordered
     * @param minimumLength minimum length of data in field
     * @param length size of data field
     * @param significantDigits number of digits on right of decimal point
     * @param isNullable can the value be null?
     * @param nativeJavaClass name of implementation class for Java
     * @param aliases aliases for the field
     * @param additionalProperties additional properties
     * @param anchorGUID unique identifier of any attached asset
     * @param typeName name of the type for this schema element
     * @param typeId unique identifier of the type for this schema element
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               displayName,
                                  String               description,
                                  int                  elementPosition,
                                  int                  minCardinality,
                                  int                  maxCardinality,
                                  boolean              isDeprecated,
                                  String               defaultValueOverride,
                                  boolean              allowsDuplicateValues,
                                  boolean              orderedValues,
                                  EnumPropertyValue    sortOrder,
                                  int                  minimumLength,
                                  int                  length,
                                  int                  significantDigits,
                                  boolean              isNullable,
                                  String               nativeJavaClass,
                                  List<String>         aliases,
                                  Map<String, String>  additionalProperties,
                                  String               anchorGUID,
                                  String               typeName,
                                  String               typeId,
                                  Map<String, Object>  extendedProperties,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeName,
              typeId,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName           = displayName;
        this.description           = description;
        this.elementPosition       = elementPosition;
        this.cardinality           = null;
        this.minCardinality        = minCardinality;
        this.maxCardinality        = maxCardinality;
        this.isDeprecated          = isDeprecated;
        this.defaultValueOverride  = defaultValueOverride;
        this.allowsDuplicateValues = allowsDuplicateValues;
        this.orderedValues         = orderedValues;
        this.sortOrder             = sortOrder;
        this.minimumLength         = minimumLength;
        this.length                = length;
        this.significantDigits     = significantDigits;
        this.isNullable            = isNullable;
        this.nativeJavaClass       = nativeJavaClass;
        this.aliases               = aliases;
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
                                                                      SchemaElementMapper.SCHEMA_DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.SCHEMA_DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.ELEMENT_POSITION_PROPERTY_NAME,
                                                               elementPosition,
                                                               methodName);

        if (cardinality != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.CARDINALITY_PROPERTY_NAME,
                                                                      cardinality,
                                                                      methodName);
        }


        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.MIN_CARDINALITY_PROPERTY_NAME,
                                                               minCardinality,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.MAX_CARDINALITY_PROPERTY_NAME,
                                                               maxCardinality,
                                                               methodName);


        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.SCHEMA_IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        if (defaultValueOverride != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
                                                                      defaultValueOverride,
                                                                      methodName);
        }


        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.ALLOWS_DUPLICATES_PROPERTY_NAME,
                                                                   allowsDuplicateValues,
                                                                   methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.ORDERED_VALUES_PROPERTY_NAME,
                                                                   orderedValues,
                                                                   methodName);

        if (sortOrder != null)
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    SchemaElementMapper.SORT_ORDER_PROPERTY_NAME,
                                                                    sortOrder.getOrdinal(),
                                                                    sortOrder.getSymbolicName(),
                                                                    sortOrder.getDescription(),
                                                                    methodName);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.MIN_LENGTH_PROPERTY_NAME,
                                                               minimumLength,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.LENGTH_PROPERTY_NAME,
                                                               length,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               SchemaElementMapper.SIGNIFICANT_DIGITS_PROPERTY_NAME,
                                                               significantDigits,
                                                               methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.IS_NULLABLE_PROPERTY_NAME,
                                                                   isNullable,
                                                                   methodName);

        if (nativeJavaClass != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.NATIVE_CLASS_PROPERTY_NAME,
                                                                      nativeJavaClass,
                                                                      methodName);
        }

        if ((aliases != null) && (!aliases.isEmpty()))
        {
            properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           SchemaElementMapper.ALIASES_PROPERTY_NAME,
                                                                           aliases,
                                                                           methodName);
        }

        if (formula != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.FORMULA_PROPERTY_NAME,
                                                                      formula,
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
    @Override
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.ATTRIBUTE_NAME_PROPERTY_NAME,
                                                                      literalName,
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
    @Override
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return super.getNameInstanceProperties(methodName);
    }


    /**
     * Set up a property value for the CommentType enum property.
     *
     * @param properties   current properties
     * @param sortOrder  enum value
     * @param methodName   calling method
     *
     * @return  InstanceProperties object with the enum value added
     */
    private InstanceProperties addSortOrderPropertyToInstance(InstanceProperties  properties,
                                                              DataItemSortOrder   sortOrder,
                                                              String              methodName)
    {
        int                ordinal = 99;
        String             symbolicName = null;
        String             description = null;

        final int    element1Ordinal         = 0;
        final String element1Value           = "Ascending";
        final String element1Description     = "Sort the data values so that they increase in value.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "Descending";
        final String element2Description     = "Sort the data values so that they decrease in value.";

        final int    element3Ordinal         = 99;
        final String element3Value           = "Ignore";
        final String element3Description     = "No specific sort order.";

        switch (sortOrder)
        {
            case ASCENDING:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case DESCENDING:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case UNSORTED:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          SchemaElementMapper.SORT_ORDER_PROPERTY_NAME,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }
}
