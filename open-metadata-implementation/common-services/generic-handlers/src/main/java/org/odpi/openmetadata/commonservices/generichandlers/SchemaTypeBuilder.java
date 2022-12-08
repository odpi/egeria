/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SchemaTypeBuilder manages the properties for a schema type.  These properties may be stored in a schema type entity or
 * a type embedded attribute classification (linked off of schema attribute).
 */
public class SchemaTypeBuilder extends ReferenceableBuilder
{
    private String              displayName          = null;
    private String              description          = null;
    private boolean             isDeprecated         = false;
    private String              versionNumber        = null;
    private String              author               = null;
    private String              usage                = null;
    private String              encodingStandard     = null;
    private String              namespace            = null;

    /*
     * Values for simple, primitive, literal types
     */
    private String              dataType             = null;
    private String              defaultValue         = null;
    private String              fixedValue           = null;

    /*
     * Values for an external schema type
     */
    private String              externalSchemaTypeGUID = null;


    /*
     * Values for an external schema type
     */
    private String              validValuesSetGUID = null;

    /*
     * Values for a schema type that is a map
     */
    private SchemaTypeBuilder   mapFrom     = null;
    private SchemaTypeBuilder   mapTo       = null;
    private String              mapFromGUID = null;
    private String              mapToGUID   = null;

    /*
     * Values for a schema type choice
     */
    private List<SchemaTypeBuilder>  schemaOptions = null;

    /*
     * Values for when the schemaType is derived from other values rather than stored
     */
    private String formula = null;


    /**
     * Minimal constructor
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaTypeBuilder(String               qualifiedName,
                             String               displayName,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
    }


    /**
     * Simple typed constructor
     *
     * @param qualifiedName unique name
     * @param typeName name of the type for this schema element
     * @param typeId unique identifier of the type for this schema element
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaTypeBuilder(String               qualifiedName,
                             String               typeId,
                             String               typeName,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(qualifiedName, typeId, typeName, repositoryHelper, serviceName, serverName);
    }


    /**
     * Constructor supporting all common properties.
     *
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param additionalProperties additional properties
     * @param typeName unique name of schema sub type
     * @param typeId unique identifier of the schema subtype
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaTypeBuilder(String               qualifiedName,
                             String               displayName,
                             String               description,
                             String               versionNumber,
                             boolean              isDeprecated,
                             String               author,
                             String               usage,
                             String               encodingStandard,
                             String               namespace,
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
        this.versionNumber = versionNumber;
        this.isDeprecated = isDeprecated;
        this.author = author;
        this.usage = usage;
        this.encodingStandard = encodingStandard;
        this.namespace = namespace;
    }
    /**
     * Constructor supporting all common properties and formula.
     *
     * @param qualifiedName unique name of schema type itself
     * @param displayName new value for the display name.
     * @param description description of the schema type.
     * @param versionNumber version of the schema type.
     * @param isDeprecated is the schema type deprecated
     * @param author name of the author
     * @param usage guidance on how the schema should be used.
     * @param encodingStandard format of the schema.
     * @param namespace namespace where the schema is defined.
     * @param formula formula for derived properties
     * @param additionalProperties additional properties
     * @param typeName unique name of schema sub type
     * @param typeId unique identifier of the schema subtype
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public SchemaTypeBuilder(String               qualifiedName,
                             String               displayName,
                             String               description,
                             String               versionNumber,
                             boolean              isDeprecated,
                             String               author,
                             String               usage,
                             String               encodingStandard,
                             String               namespace,
                             String               formula,
                             Map<String, String>  additionalProperties,
                             String               typeId,
                             String               typeName,
                             Map<String, Object>  extendedProperties,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        this(qualifiedName,
             displayName,
             description,
             versionNumber,
             isDeprecated,
             author,
             usage,
             encodingStandard,
             namespace,
             additionalProperties,
             typeId,
             typeName,
             extendedProperties,
             repositoryHelper,
             serviceName,
             serverName);
        setDerivedProperties(formula);
    }


    /**
     * Set up the type of data (for simple and literal types)
     *
     * @param dataType string name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Set up the default value of a field (for simple types).
     *
     * @param defaultValue string value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    /**
     * Set up the fixed data value (for literal types).
     *
     * @param fixedValue string value
     */
    public void setFixedValue(String fixedValue)
    {
        this.fixedValue = fixedValue;
    }


    /**
     * Set up the unique identifier for an external schema type.
     *
     * @param guid string guid
     */
    public void setExternalSchemaTypeGUID(String guid)
    {
        this.externalSchemaTypeGUID = guid;
    }


    /**
     * Return the unique identifier for an external schema type.
     *
     * @return string guid
     */
    public String getExternalSchemaTypeGUID()
    {
        return externalSchemaTypeGUID;
    }


    /**
     * Set up the unique identifier of the valid values set that lists the permitted values for the attached
     * schema attribute.
     *
     * @param validValuesSetGUID string guid
     */
    public void setValidValuesSetGUID(String validValuesSetGUID)
    {
        this.validValuesSetGUID = validValuesSetGUID;
    }


    /**
     * Return the unique identifier of the valid values set that lists the permitted values for the attached
     * schema attribute.
     *
     * @return string guid
     */
    public String getValidValuesSetGUID()
    {
        return validValuesSetGUID;
    }


    /**
     * Set up the builder to support a map type.  This needs to point to two other schema types.
     *
     * @param mapFrom the type of the value that is in the domain of the map
     * @param mapTo the type of the value that is in the rage of the map
     */
    public void setMapTypes(SchemaTypeBuilder   mapFrom,
                            SchemaTypeBuilder   mapTo)
    {
        this.mapFrom = mapFrom;
        this.mapTo   = mapTo;
    }


    /**
     * Set up the builder to support a map type.  This needs to point to two other schema types.
     *
     * @param mapFromGUID the GUID of the value that is in the domain of the map
     * @param mapToGUID the GUID of the value that is in the rage of the map
     */
    public void setMapGUIDs(String   mapFromGUID,
                            String   mapToGUID)
    {
        this.mapFromGUID = mapFromGUID;
        this.mapToGUID   = mapToGUID;
    }


    /**
     * Return the builder for the type of the value that is in the domain of the map.
     *
     * @return builder for requested type
     */
    public SchemaTypeBuilder  getMapFrom()
    {
        return this.mapFrom;
    }


    /**
     * Return the builder for the type of the value that is in the range of the map.
     *
     * @return builder for requested type
     */
    public SchemaTypeBuilder  getMapTo()
    {
        return this.mapTo;
    }


    /**
     * Return the GUID for the type of the value that is in the domain of the map.
     *
     * @return string
     */
    public String  getMapFromGUID()
    {
        return this.mapFromGUID;
    }


    /**
     * Return the GUID for the type of the value that is in the range of the map.
     *
     * @return string
     */
    public String  getMapToGUID()
    {
        return this.mapToGUID;
    }


    /**
     * Set up the list of types that are represented by a schema option type.
     *
     * @param schemaOptions list of builders
     */
    public void setSchemaOptions(List<SchemaTypeBuilder> schemaOptions)
    {
        this.schemaOptions = schemaOptions;
    }


    /**
     * Return the list of types that are represented by a schema option type.
     *
     * @return list of builders
     */
    public List<SchemaTypeBuilder> getSchemaOptions()
    {
        return schemaOptions;
    }


    /**
     * Set up the properties that indicate that the schema element's value is not stored, it is derived from other values.
     * The formula is stored in the CalculatedValue classification.  If it is null, the queries are ignored.  The queries
     * are each stored as a DerivedSchemaTypeQueryTarget.
     *
     * @param formula expression, possibly with place holders to insert the values returned from the queries
     */
    public void setDerivedProperties(String formula)
    {
        this.formula = formula;
    }


    /**
     * Return whether the schema has a derived value of not.  This is determined from the setting of formula
     *
     * @return boolean
     */
    public boolean isDerived()
    {
        return (! (formula == null));
    }
    /**
     * Return the formula expression.
     *
     * @return string
     */
    public String getFormula()
    {
        return formula;
    }


    /**
     * Return the list of builders that each hold details of a schema Type to update.
     *
     * @return list of builders including this one
     */
    List<SchemaTypeBuilder> getSchemaTypeBuilders()
    {
        List<SchemaTypeBuilder> builders = new ArrayList<>();

        builders.add(this);

        if (mapFrom != null)
        {
            builders.add(mapFrom);
        }
        if (mapTo != null)
        {
            builders.add(mapTo);
        }
        if ((schemaOptions != null) && (! schemaOptions.isEmpty()))
        {
            builders.addAll(schemaOptions);
        }

        return builders;
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
                                                                  OpenMetadataAPIMapper.SCHEMA_DISPLAY_NAME_PROPERTY_NAME,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SCHEMA_DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.VERSION_NUMBER_PROPERTY_NAME,
                                                                  versionNumber,
                                                                  methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.AUTHOR_PROPERTY_NAME,
                                                                  author,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SCHEMA_USAGE_PROPERTY_NAME,
                                                                  usage,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.ENCODING_STANDARD_PROPERTY_NAME,
                                                                  encodingStandard,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.NAMESPACE_PROPERTY_NAME,
                                                                  namespace,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DATA_TYPE_PROPERTY_NAME,
                                                                  dataType,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DEFAULT_VALUE_PROPERTY_NAME,
                                                                  defaultValue,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.FIXED_VALUE_PROPERTY_NAME,
                                                                  fixedValue,
                                                                  methodName);
        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object for a TypeEmbeddedAttribute
     * classification.  If the caller uses this when there are extended properties defined for the
     * builder then the call to create or add the entity will fail with invalid properties.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getTypeEmbeddedInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = this.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TYPE_NAME_PROPERTY_NAME,
                                                                  typeName,
                                                                  methodName);

        return properties;
    }
}
