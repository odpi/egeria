/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.List;
import java.util.Map;

/**
 * SchemaAttributeBuilder creates instance properties for a schema attribute.
 */
public class SchemaAttributeBuilder extends ReferenceableBuilder
{
    private String            displayName           = null;
    private String            description           = null;
    private int               elementPosition       = 0;
    private int               minCardinality        = 0;
    private int               maxCardinality        = 0;
    private boolean           isDeprecated          = false;
    private String            cardinality           = null;
    private boolean           allowsDuplicateValues = false;
    private boolean           orderedValues         = false;
    private int               sortOrder             = 0;
    private int               minimumLength         = 0;
    private int               length                = 0;
    private int               significantDigits     = 0;
    private boolean           isNullable            = true;
    private String            defaultValueOverride  = null;
    private String            nativeJavaClass       = null;
    private List<String>      aliases               = null;

    private SchemaTypeBuilder schemaTypeBuilder     = null;


    /**
     * Template constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name
     * @param description description of the schema attribute
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(String               qualifiedName,
                                  String               displayName,
                                  String               description,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(qualifiedName,
              OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
              OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Template constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaAttributeBuilder(OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        super(OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_GUID,
              OpenMetadataAPIMapper.SCHEMA_ATTRIBUTE_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
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
                                  int                  sortOrder,
                                  int                  minimumLength,
                                  int                  length,
                                  int                  significantDigits,
                                  boolean              isNullable,
                                  String               nativeJavaClass,
                                  List<String>         aliases,
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
     * The schema type can be stored in the TypeEmbeddedAttribute classification for the schema attribute entity.
     * Depending on the type of schema, the schema type may require additional entities and relationships.  The
     * schemaTypeBuilder has this knowledge which is why the schemaTypeBuilder is saved.
     * This method overrides any previously defined TypeEmbeddedAttribute classification for this entity.
     *
     * @param userId calling user
     * @param schemaTypeBuilder builder containing the properties of the schema type
     * @param methodName calling method
     * @throws InvalidParameterException TypeEmbeddedAttribute is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    public void setSchemaType(String            userId,
                              SchemaTypeBuilder schemaTypeBuilder,
                              String            methodName) throws InvalidParameterException
    {
        this.schemaTypeBuilder = schemaTypeBuilder;

        try
        {
            Classification classification = repositoryHelper.getNewClassification(serviceName,
                                                                                  null,
                                                                                  null,
                                                                                  InstanceProvenanceType.LOCAL_COHORT,
                                                                                  userId,
                                                                                  OpenMetadataAPIMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                                                  typeName,
                                                                                  ClassificationOrigin.ASSIGNED,
                                                                                  null,
                                                                                  schemaTypeBuilder.getTypeEmbeddedInstanceProperties(methodName));
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Return the schema type builder to the handler to add any extra elements.
     *
     * @return schema type builder object
     */
    public SchemaTypeBuilder getSchemaTypeBuilder()
    {
        return schemaTypeBuilder;
    }


    /**
     * Set up the CalculatedValue classification for this entity.
     * This method overrides any previously defined CalculatedValue classification for this entity.
     *
     * @param userId calling user
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param formula                   details of how this value is calculated
     * @param methodName                calling method
     * @throws InvalidParameterException calculated value is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    void setCalculatedValue(String userId,
                            String externalSourceGUID,
                            String externalSourceName,
                            String formula,
                            String methodName) throws InvalidParameterException
    {
        this.setCalculatedValue(userId, externalSourceGUID,externalSourceName, getCalculatedValueProperties(formula, methodName), methodName);
    }
    /**
     * Set up the CalculatedValue classification for this entity.
     * This method overrides any previously defined CalculatedValue classification for this entity.
     *
     * @param userId calling user
     * @param externalSourceGUID        guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName        name of the software capability entity that represented the external source
     * @param instanceProperties        properties for the calculated vlaue classification
     * @param methodName calling method
     * @throws InvalidParameterException calculated value is not supported in the local repository, or any repository
     *                                   connected by an open metadata repository cohort
     */
    void setCalculatedValue(String userId,
                            String externalSourceGUID,
                            String externalSourceName,
                            InstanceProperties instanceProperties,
                            String methodName) throws InvalidParameterException
    {
        try
        {
            InstanceProvenanceType instanceProvenanceType = InstanceProvenanceType.LOCAL_COHORT;

            if (externalSourceGUID != null)
            {
                instanceProvenanceType = InstanceProvenanceType.EXTERNAL_SOURCE;
            }

            Classification classification = repositoryHelper.getNewClassification(serviceName,
                    externalSourceGUID,
                    externalSourceName,
                    instanceProvenanceType,
                    userId,
                    OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME,
                    typeName,
                    ClassificationOrigin.ASSIGNED,
                    null,
                    instanceProperties);
            newClassifications.put(classification.getName(), classification);
        }
        catch (TypeErrorException error)
        {
            errorHandler.handleUnsupportedType(error, methodName, OpenMetadataAPIMapper.CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME);
        }
    }


    /**
     * Return the schema type properties in an InstanceProperties object.
     *
     * @param formula details of how this value is calculated
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getCalculatedValueProperties(String formula,
                                                           String methodName)
    {
        InstanceProperties properties = null;

        if (formula != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                                      formula,
                                                                      methodName);
        }

        return properties;
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

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.ELEMENT_POSITION_PROPERTY_NAME,
                                                               elementPosition,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.CARDINALITY_PROPERTY_NAME,
                                                                  cardinality,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.MIN_CARDINALITY_PROPERTY_NAME,
                                                               minCardinality,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.MAX_CARDINALITY_PROPERTY_NAME,
                                                               maxCardinality,
                                                               methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME,
                                                                  defaultValueOverride,
                                                                  methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.ALLOWS_DUPLICATES_PROPERTY_NAME,
                                                                   allowsDuplicateValues,
                                                                   methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.ORDERED_VALUES_PROPERTY_NAME,
                                                                   orderedValues,
                                                                   methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.SORT_ORDER_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.DATA_ITEM_SORT_ORDER_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.DATA_ITEM_SORT_ORDER_TYPE_NAME,
                                                                    sortOrder,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.SORT_ORDER_PROPERTY_NAME);
        }

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.MIN_LENGTH_PROPERTY_NAME,
                                                               minimumLength,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.LENGTH_PROPERTY_NAME,
                                                               length,
                                                               methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataAPIMapper.SIGNIFICANT_DIGITS_PROPERTY_NAME,
                                                               significantDigits,
                                                               methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IS_NULLABLE_PROPERTY_NAME,
                                                                   isNullable,
                                                                   methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.NATIVE_CLASS_PROPERTY_NAME,
                                                                      nativeJavaClass,
                                                                      methodName);

        properties = repositoryHelper.addStringArrayPropertyToInstance(serviceName,
                                                                           properties,
                                                                           OpenMetadataAPIMapper.ALIASES_PROPERTY_NAME,
                                                                           aliases,
                                                                           methodName);

        return properties;
    }
}
