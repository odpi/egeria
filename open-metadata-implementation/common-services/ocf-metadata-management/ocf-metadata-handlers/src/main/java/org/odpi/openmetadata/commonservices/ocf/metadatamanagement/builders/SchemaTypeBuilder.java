/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DerivedSchemaTypeQueryTarget;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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

    private String              anchorGUID           = null;
    /*
     * Values for simple, primitive, literal types
     */
    private String              dataType             = null;
    private String              defaultValue         = null;
    private String              fixedValue           = null;


    /*
     * Values for a schema type that is a map
     */
    private SchemaTypeBuilder mapFrom = null;
    private SchemaTypeBuilder mapTo   = null;

    /*
     * Values for a schema type choice
     */
    private List<SchemaTypeBuilder>  schemaOptions = null;

    /*
     * Values for when the schemaType is derived from other values rather than stored
     */
    private String                             formula = null;
    private List<DerivedSchemaTypeQueryTarget> queries = null;


    private int                 maximumElements      = 0;

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
                             String               typeName,
                             String               typeId,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName,
                             String               serverName)
    {
        super(qualifiedName, typeName, typeId, repositoryHelper, serviceName, serverName);
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
     * @param anchorGUID unique identifier of the anchor object for this schema type
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

        this.displayName = displayName;
        this.description = description;
        this.versionNumber = versionNumber;
        this.isDeprecated = isDeprecated;
        this.author = author;
        this.usage = usage;
        this.encodingStandard = encodingStandard;
        this.namespace = namespace;
        this.anchorGUID = anchorGUID;
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
     * @param queries optional queries to supply values to insert into the formula
     */
    public void setDerivedProperties(String                             formula,
                                     List<DerivedSchemaTypeQueryTarget> queries)
    {
        this.formula = formula;
        this.queries = queries;
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
     * Set up the maximum number of elements allowed - zero for no limit.  This property is deprecated because
     * it is for BoundedSchemaType which is also deprecated.
     *
     * @param maximumElements int
     */
    @Deprecated
    public void setMaximumElements(int maximumElements)
    {
        this.maximumElements = maximumElements;
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

        if (versionNumber != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.SCHEMA_VERSION_NUMBER_PROPERTY_NAME,
                                                                      versionNumber,
                                                                      methodName);
        }

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.SCHEMA_IS_DEPRECATED_PROPERTY_NAME,
                                                                   isDeprecated,
                                                                   methodName);

        if (author != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.SCHEMA_AUTHOR_PROPERTY_NAME,
                                                                      author,
                                                                      methodName);
        }

        if (usage != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.SCHEMA_USAGE_PROPERTY_NAME,
                                                                      usage,
                                                                      methodName);
        }

        if (encodingStandard != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.SCHEMA_ENCODING_STANDARD_PROPERTY_NAME,
                                                                      encodingStandard,
                                                                      methodName);
        }

        if (namespace != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.SCHEMA_NAMESPACE_PROPERTY_NAME,
                                                                      namespace,
                                                                      methodName);
        }


        if (dataType != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DATA_TYPE_PROPERTY_NAME,
                                                                      dataType,
                                                                      methodName);
        }

        if (defaultValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.DEFAULT_VALUE_PROPERTY_NAME,
                                                                      defaultValue,
                                                                      methodName);
        }

        if (fixedValue != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.FIXED_VALUE_PROPERTY_NAME,
                                                                      fixedValue,
                                                                      methodName);
        }

        if (maximumElements != 0)
        {
            properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                                   properties,
                                                                   SchemaElementMapper.MAX_ELEMENTS_PROPERTY_NAME,
                                                                   maximumElements,
                                                                   methodName);
        }

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

        if (typeName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      SchemaElementMapper.TYPE_NAME_PROPERTY_NAME,
                                                                      typeName,
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
                                                                      SchemaElementMapper.SCHEMA_DISPLAY_NAME_PROPERTY_NAME,
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

}
