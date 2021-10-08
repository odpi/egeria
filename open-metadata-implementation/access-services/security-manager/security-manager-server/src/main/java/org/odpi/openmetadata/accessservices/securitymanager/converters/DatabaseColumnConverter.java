/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.converters;

import org.odpi.openmetadata.accessservices.securitymanager.metadataelements.DatabaseColumnElement;
import org.odpi.openmetadata.accessservices.securitymanager.metadataelements.DatabaseColumnTypeElement;
import org.odpi.openmetadata.accessservices.securitymanager.properties.DatabaseColumnProperties;
import org.odpi.openmetadata.accessservices.securitymanager.properties.SchemaTypeProperties;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * TabularColumnConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DatabaseColumn bean.
 */
public class DatabaseColumnConverter<B> extends SecurityManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public DatabaseColumnConverter(OMRSRepositoryHelper repositoryHelper,
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
            if (returnBean instanceof DatabaseColumnElement)
            {
                DatabaseColumnElement bean                         = (DatabaseColumnElement) returnBean;
                DatabaseColumnProperties  databaseColumnProperties = new DatabaseColumnProperties();

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

                    databaseColumnProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    databaseColumnProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    databaseColumnProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    databaseColumnProperties.setDescription(this.removeDescription(instanceProperties));

                    databaseColumnProperties.setElementPosition(this.removePosition(instanceProperties));
                    databaseColumnProperties.setMinCardinality(this.removeMinCardinality(instanceProperties));
                    databaseColumnProperties.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                    databaseColumnProperties.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
                    databaseColumnProperties.setOrderedValues(this.removeOrderedValues(instanceProperties));
                    databaseColumnProperties.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
                    databaseColumnProperties.setSortOrder(this.removeSortOrder(instanceProperties));
                    databaseColumnProperties.setMinimumLength(this.removeMinimumLength(instanceProperties));
                    databaseColumnProperties.setLength(this.removeLength(instanceProperties));
                    databaseColumnProperties.setPrecision(this.removePrecision(instanceProperties));
                    databaseColumnProperties.setIsNullable(this.removeIsNullable(instanceProperties));
                    databaseColumnProperties.setNativeJavaClass(this.removeNativeClass(instanceProperties));
                    databaseColumnProperties.setAliases(this.removeAliases(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    databaseColumnProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    databaseColumnProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    instanceProperties = new InstanceProperties(super.getClassificationProperties(OpenMetadataAPIMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                                                                  schemaAttributeEntity));

                    databaseColumnProperties.setDataType(this.removeDataType(instanceProperties));
                    databaseColumnProperties.setDefaultValue(this.removeDefaultValue(instanceProperties));
                    databaseColumnProperties.setFixedValue(this.removeFixedValue(instanceProperties));

                    if (schemaType instanceof DatabaseColumnTypeElement)
                    {
                        databaseColumnProperties.setSchemaType((SchemaTypeProperties) schemaType);
                    }

                    bean.setDatabaseColumnProperties(databaseColumnProperties);
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
