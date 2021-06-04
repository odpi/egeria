/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.TabularColumnElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.SchemaTypeProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TabularColumnProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * TabularColumnConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a TabularColumnElement bean.
 */
public class TabularColumnConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public TabularColumnConverter(OMRSRepositoryHelper repositoryHelper,
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
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof TabularColumnElement)
            {
                TabularColumnElement bean = (TabularColumnElement) returnBean;
                TabularColumnProperties tabularColumnProperties = new TabularColumnProperties();

                if (schemaAttributeEntity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaAttributeEntity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                    tabularColumnProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    tabularColumnProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    tabularColumnProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    tabularColumnProperties.setDescription(this.removeDescription(instanceProperties));

                    tabularColumnProperties.setElementPosition(this.removePosition(instanceProperties));
                    tabularColumnProperties.setMinCardinality(this.removeMinCardinality(instanceProperties));
                    tabularColumnProperties.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                    tabularColumnProperties.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
                    tabularColumnProperties.setOrderedValues(this.removeOrderedValues(instanceProperties));
                    tabularColumnProperties.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
                    tabularColumnProperties.setSortOrder(this.removeSortOrder(instanceProperties));
                    tabularColumnProperties.setMinimumLength(this.removeMinimumLength(instanceProperties));
                    tabularColumnProperties.setLength(this.removeLength(instanceProperties));
                    tabularColumnProperties.setPrecision(this.removePrecision(instanceProperties));
                    tabularColumnProperties.setIsNullable(this.removeIsNullable(instanceProperties));
                    tabularColumnProperties.setNativeJavaClass(this.removeNativeClass(instanceProperties));
                    tabularColumnProperties.setAliases(this.removeAliases(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    tabularColumnProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    tabularColumnProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    if (schemaType instanceof SchemaTypeElement)
                    {
                        SchemaTypeProperties schemaTypeProperties = ((SchemaTypeElement) schemaType).getSchemaTypeProperties();

                        super.addSchemaTypeToColumn(schemaTypeProperties, tabularColumnProperties);
                    }

                    bean.setTabularColumnProperties(tabularColumnProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }
            }

            return returnBean;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }
}
