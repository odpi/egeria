/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.datamanager.properties.*;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * DataManagerOMASConverter provides the generic methods for the Data Manager beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Data Manager bean.
 */
public class DataManagerOMASConverter<B> extends OpenMetadataAPIGenericConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    public DataManagerOMASConverter(OMRSRepositoryHelper   repositoryHelper,
                                    String                 serviceName,
                                    String                 serverName)
    {
        super (repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Extract and delete the sortOrder property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return DataItemSortOrder enum
     */
    DataItemSortOrder removeSortOrder(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeSortOrder";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.SORT_ORDER.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (DataItemSortOrder dataItemSortOrder : DataItemSortOrder.values())
            {
                if (dataItemSortOrder.getOrdinal() == ordinal)
                {
                    return dataItemSortOrder;
                }
            }
        }

        return null;
    }


    /**
     * Extract and delete the KeyPattern property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return KeyPattern enum
     */
    KeyPattern removeKeyPattern(InstanceProperties  instanceProperties)
    {
        final String methodName = "removeKeyPattern";

        if (instanceProperties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataProperty.KEY_PATTERN.name,
                                                                     instanceProperties,
                                                                     methodName);

            for (KeyPattern keyPattern : KeyPattern.values())
            {
                if (keyPattern.getOrdinal() == ordinal)
                {
                    return keyPattern;
                }
            }
        }

        return null;
    }


    /**
     * Set up the properties that can be extracted form the schema type.
     *
     * @param schemaAttributeEntity entity to unpack
     * @param schemaTypeElement schema type properties
     * @param properties output column properties
     */
    void setUpSchemaAttribute(EntityDetail              schemaAttributeEntity,
                              SchemaTypeElement         schemaTypeElement,
                              SchemaAttributeProperties properties)
    {
        /*
         * The initial set of values come from the entity.
         */
        InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

        properties.setQualifiedName(this.removeQualifiedName(instanceProperties));
        properties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
        properties.setDisplayName(this.removeDisplayName(instanceProperties));
        properties.setDescription(this.removeDescription(instanceProperties));

        properties.setElementPosition(this.removePosition(instanceProperties));
        properties.setMinCardinality(this.removeMinCardinality(instanceProperties));
        properties.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
        properties.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
        properties.setOrderedValues(this.removeOrderedValues(instanceProperties));
        properties.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
        properties.setSortOrder(this.removeSortOrder(instanceProperties));
        properties.setMinimumLength(this.removeMinimumLength(instanceProperties));
        properties.setLength(this.removeLength(instanceProperties));
        properties.setPrecision(this.removePrecision(instanceProperties));
        properties.setSignificantDigits(this.removeSignificantDigits(instanceProperties));
        properties.setIsNullable(this.removeIsNullable(instanceProperties));
        properties.setNativeJavaClass(this.removeNativeClass(instanceProperties));
        properties.setAliases(this.removeAliases(instanceProperties));

        /*
         * Any remaining properties are returned in the extended properties.  They are assumed to be defined in a subtype.
         */
        properties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

        if (schemaTypeElement != null)
        {
            this.addSchemaTypeToAttribute(schemaTypeElement, properties);
        }
    }


    /**
     * Set up the properties that can be extracted form the schema type.
     *
     * @param schemaTypeElement schema type properties
     * @param attributeProperties output column properties
     */
    void addSchemaTypeToAttribute(SchemaTypeElement         schemaTypeElement,
                                  SchemaAttributeProperties attributeProperties)
    {
        SchemaTypeProperties    schemaTypeProperties = schemaTypeElement.getSchemaTypeProperties();

        if (schemaTypeProperties instanceof PrimitiveSchemaTypeProperties)
        {
            attributeProperties.setDataType(((PrimitiveSchemaTypeProperties) schemaTypeProperties).getDataType());
            attributeProperties.setDefaultValue(((PrimitiveSchemaTypeProperties) schemaTypeProperties).getDefaultValue());
        }
        else if (schemaTypeProperties instanceof LiteralSchemaTypeProperties)
        {
            attributeProperties.setDataType(((LiteralSchemaTypeProperties) schemaTypeProperties).getDataType());
            attributeProperties.setFixedValue(((LiteralSchemaTypeProperties) schemaTypeProperties).getFixedValue());
        }
        else if (schemaTypeProperties instanceof EnumSchemaTypeProperties)
        {
            attributeProperties.setDataType(((EnumSchemaTypeProperties) schemaTypeProperties).getDataType());
            attributeProperties.setDefaultValue(((EnumSchemaTypeProperties) schemaTypeProperties).getDefaultValue());
            attributeProperties.setValidValuesSetGUID(((EnumSchemaTypeProperties) schemaTypeProperties).getValidValueSetGUID());
        }
        else if (schemaTypeProperties instanceof ExternalSchemaTypeProperties)
        {
            attributeProperties.setDataType(((ExternalSchemaTypeProperties) schemaTypeProperties).getDataType());

            SchemaTypeElement externalSchemaType = schemaTypeElement.getExternalSchemaType();
            attributeProperties.setExternalTypeGUID(externalSchemaType.getElementHeader().getGUID());
        }
    }


    /**
     * Using the supplied instances, return a new instance of a relatedElement bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public RelatedElement getRelatedElement(Class<B>     beanClass,
                                            EntityDetail entity,
                                            Relationship relationship,
                                            String       methodName) throws PropertyServerException
    {
        RelatedElement  relatedElement = new RelatedElement();

        relatedElement.setRelationshipHeader(this.getMetadataElementHeader(beanClass, relationship, null, methodName));

        if (relationship != null)
        {
            InstanceProperties instanceProperties = new InstanceProperties(relationship.getProperties());

            RelationshipProperties relationshipProperties = new RelationshipProperties();

            relationshipProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
            relationshipProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());
            relationshipProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            relatedElement.setRelationshipProperties(relationshipProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.RELATIONSHIP_DEF, methodName);
        }


        if (entity != null)
        {
            ElementStub elementStub = this.getElementStub(beanClass, entity, methodName);

            relatedElement.setRelatedElement(elementStub);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }

        return relatedElement;
    }

}
