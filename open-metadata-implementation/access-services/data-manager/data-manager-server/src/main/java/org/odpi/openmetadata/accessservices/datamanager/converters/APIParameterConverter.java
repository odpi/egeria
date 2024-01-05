/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.APIParameterElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.SchemaTypeElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIParameterProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * APIParameterConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a APIParameterElement bean.
 */
public class APIParameterConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public APIParameterConverter(OMRSRepositoryHelper repositoryHelper,
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
            if (returnBean instanceof APIParameterElement)
            {
                APIParameterElement    bean       = (APIParameterElement) returnBean;
                APIParameterProperties properties = new APIParameterProperties();

                if (schemaAttributeEntity != null)
                {
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
                    properties.setParameterType(this.removeParameterType(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are assumed to be defined in a subtype.
                     */
                    properties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));


                    if (schemaType instanceof SchemaTypeElement)
                    {
                        SchemaTypeElement schemaTypeElement = (SchemaTypeElement) schemaType;

                        this.addSchemaTypeToAttribute(schemaTypeElement, properties);
                    }

                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, schemaAttributeEntity, methodName));

                    properties.setTypeName(bean.getElementHeader().getType().getTypeName());

                    bean.setProperties(properties);
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
}
