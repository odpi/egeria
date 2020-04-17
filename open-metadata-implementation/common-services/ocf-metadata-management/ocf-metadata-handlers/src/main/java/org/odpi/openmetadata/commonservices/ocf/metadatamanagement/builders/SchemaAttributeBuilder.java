/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CommentMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * SchemaAttributeBuilder creates instance properties for a schema attribute.
 */
public class SchemaAttributeBuilder extends ReferenceableBuilder
{
    private String            attributeName;

    private String            description           = null;
    private int               elementPosition       = 0;
    private int               minCardinality        = 0;
    private int               maxCardinality        = 0;
    private boolean           isDeprecated          = false;
    private String            cardinality           = null;
    private boolean           allowsDuplicateValues = false;
    private boolean           orderedValues         = false;
    private DataItemSortOrder sortOrder             = null;
    private String            anchorGUID            = null;
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
     * @param attributeName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               attributeName,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.attributeName = attributeName;
    }


    /**
     * Constructor supporting original properties. Deprecated because uses old form of cardinality.
     *
     * @param qualifiedName unique name
     * @param attributeName new value for the display name.
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
                                  String               attributeName,
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
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.attributeName = attributeName;
        this.elementPosition = elementPosition;
        this.cardinality = cardinality;
        this.minCardinality = 0;
        this.maxCardinality = 0;
        this.defaultValueOverride = defaultValueOverride;
}


    /**
     * Constructor supporting all properties for a schema attribute entity.
     *
     * @param qualifiedName unique name
     * @param attributeName new value for the display name.
     * @param description new value for the description.
     * @param elementPosition position of the attribute in the parent schemaType.
     * @param minCardinality is the attribute optional?
     * @param maxCardinality does the attribute repeat?
     * @param isDeprecated is this element deprecated?
     * @param defaultValueOverride override for the Type's default value.
     * @param allowsDuplicateValues unique values ?
     * @param orderedValues ordered values ?
     * @param sortOrder sort order if ordered
     * @param anchorGUID unique identifier of the anchor entity
     * @param minimumLength minimum length of data in field
     * @param length size of data field
     * @param significantDigits number of digits on right of decimal point
     * @param isNullable can the value be null?
     * @param nativeJavaClass name of implementation class for Java
     * @param aliases aliases for the field
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               attributeName,
                                  String               description,
                                  int                  elementPosition,
                                  int                  minCardinality,
                                  int                  maxCardinality,
                                  boolean              isDeprecated,
                                  String               defaultValueOverride,
                                  boolean              allowsDuplicateValues,
                                  boolean              orderedValues,
                                  DataItemSortOrder    sortOrder,
                                  String               anchorGUID,
                                  int                  minimumLength,
                                  int                  length,
                                  int                  significantDigits,
                                  boolean              isNullable,
                                  String               nativeJavaClass,
                                  List<String>         aliases,
                                  Map<String, String>  additionalProperties,
                                  Map<String, Object>  extendedProperties,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.attributeName = attributeName;
        this.description = description;
        this.elementPosition = elementPosition;
        this.cardinality = null;
        this.minCardinality = minCardinality;
        this.maxCardinality = maxCardinality;
        this.isDeprecated = isDeprecated;
        this.defaultValueOverride = defaultValueOverride;
        this.allowsDuplicateValues = allowsDuplicateValues;
        this.orderedValues = orderedValues;
        this.sortOrder = sortOrder;
        this.anchorGUID = anchorGUID;
        this.minimumLength = minimumLength;
        this.length = length;
        this.significantDigits = significantDigits;
        this.isNullable = isNullable;
        this.nativeJavaClass = nativeJavaClass;
        this.aliases = aliases;
    }


    /**
     * Set up the formula for a derived schema attribute.
     *
     * @param formula formula used to derive the attribute value.
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
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

        if (attributeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.ATTRIBUTE_NAME_PROPERTY_NAME,
                                                                      attributeName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DESCRIPTION_PROPERTY_NAME,
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
                                                                   SchemaElementMapper.IS_DEPRECATED_PROPERTY_NAME,
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

        if (anchorGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                      anchorGUID,
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

        if ((sortOrder != null) && (sortOrder != DataItemSortOrder.UNKNOWN))
        {
            properties = this.addSortOrderPropertyToInstance(properties,
                                                             sortOrder,
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

        if (attributeName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(attributeName);

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
