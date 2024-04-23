/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DataItemSortOrder;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;


/**
 * SchemaAttributeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a SchemaAttributeElement bean.
 */
public class SchemaAttributeConverter<B> extends AssetOwnerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public SchemaAttributeConverter(OMRSRepositoryHelper repositoryHelper,
                                    String               serviceName,
                                    String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Extract the properties from the schema attribute entity.  Each API creates a specialization of this method for its beans.
     *
     * @param beanClass name of the class to create
     * @param schemaAttributeEntity entity containing the properties for the main schema attribute
     * @param typeClass name of type used to describe the schema type
     * @param schemaType bean containing the properties of the schema type - this is filled out by the schema type converter
     * @param schemaAttributeRelationships relationships containing the links to other schema attributes
     * @param methodName calling method
     * @param <T> bean type used to create the schema type
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public <T> B getNewSchemaAttributeBean(Class<B>           beanClass,
                                           EntityDetail       schemaAttributeEntity,
                                           Class<T>           typeClass,
                                           T                  schemaType,
                                           List<Relationship> schemaAttributeRelationships,
                                           String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof SchemaAttributeElement bean)
            {
                SchemaAttributeProperties schemaAttributeProperties = new SchemaAttributeProperties();

                if (schemaAttributeEntity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaAttributeEntity, methodName));

                    /*
                     * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                     * properties, leaving any subclass properties to be stored in extended properties.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                    schemaAttributeProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    schemaAttributeProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    schemaAttributeProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    schemaAttributeProperties.setDescription(this.removeDescription(instanceProperties));
                    schemaAttributeProperties.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
                    schemaAttributeProperties.setElementPosition(this.removePosition(instanceProperties));
                    schemaAttributeProperties.setMinCardinality(this.removeMinCardinality(instanceProperties));
                    schemaAttributeProperties.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                    schemaAttributeProperties.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
                    schemaAttributeProperties.setOrderedValues(this.removeOrderedValues(instanceProperties));
                    schemaAttributeProperties.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
                    schemaAttributeProperties.setMinimumLength(this.removeMinimumLength(instanceProperties));
                    schemaAttributeProperties.setLength(this.removeLength(instanceProperties));
                    schemaAttributeProperties.setPrecision(this.removePrecision(instanceProperties));
                    schemaAttributeProperties.setSignificantDigits(this.removeSignificantDigits(instanceProperties));
                    schemaAttributeProperties.setIsNullable(this.removeIsNullable(instanceProperties));
                    schemaAttributeProperties.setNativeJavaClass(this.removeNativeClass(instanceProperties));
                    schemaAttributeProperties.setAliases(this.removeAliases(instanceProperties));
                    schemaAttributeProperties.setSortOrder(this.removeSortOrder(instanceProperties));

                    if (schemaType instanceof SchemaTypeElement)
                    {
                        schemaAttributeProperties.setAttributeType(((SchemaTypeElement) schemaType).getSchemaTypeProperties());
                    }

                    bean.setSchemaAttributeProperties(schemaAttributeProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }



    /**
     * Extract and delete the sortOrder property from the supplied instance properties.
     *
     * @param instanceProperties properties from entity
     * @return DataItemSortOrder enum
     */
    private DataItemSortOrder removeSortOrder(InstanceProperties  instanceProperties)
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

        return DataItemSortOrder.UNSORTED;
    }

}
