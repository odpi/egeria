/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.TabularColumnElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
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

                /*
                 * Check that the entity is of the correct type.
                 */
                bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaAttributeEntity, methodName));

                /*
                 * The initial set of values come from the entity.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));

                bean.setElementPosition(this.removePosition(instanceProperties));
                bean.setMinCardinality(this.removeMinCardinality(instanceProperties));
                bean.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                bean.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
                bean.setOrderedValues(this.removeOrderedValues(instanceProperties));
                bean.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
                bean.setSortOrder(this.removeSortOrder(instanceProperties));
                bean.setMinimumLength(this.removeMinimumLength(instanceProperties));
                bean.setLength(this.removeLength(instanceProperties));
                bean.setSignificantDigits(this.removeSignificantDigits(instanceProperties));
                bean.setNullable(this.removeIsNullable(instanceProperties));
                bean.setNativeJavaClass(this.removeNativeClass(instanceProperties));
                bean.setAliases(this.removeAliases(instanceProperties));

                instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                bean.setDataType(this.removeDataType(instanceProperties));
                bean.setDefaultValue(this.removeDefaultValue(instanceProperties));
                bean.setFixedValue(this.removeFixedValue(instanceProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setTypeName(bean.getElementHeader().getType().getTypeName());
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                if (schemaType instanceof SchemaTypeElement)
                {
                    bean.setSchemaType(((SchemaTypeElement) schemaType).getSchemaTypeProperties());
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
