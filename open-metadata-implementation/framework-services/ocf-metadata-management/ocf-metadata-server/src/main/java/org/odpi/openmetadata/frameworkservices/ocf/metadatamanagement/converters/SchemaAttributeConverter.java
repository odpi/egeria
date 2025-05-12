/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * LicenseConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into a License bean.
 */
public class SchemaAttributeConverter<B> extends OMFConverter<B>
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
     * @param <T> bean type used to create the schema type
     * @param methodName calling method
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

            if (returnBean instanceof SchemaAttribute bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, schemaAttributeEntity, OpenMetadataType.SCHEMA_ATTRIBUTE.typeName, methodName);

                /*
                 * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
                 * properties, leaving any subclass properties to be stored in extended properties.
                 */
                InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setDescription(this.removeDescription(instanceProperties));
                bean.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
                bean.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
                bean.setOrderedValues(this.removeOrderedValues(instanceProperties));
                bean.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
                bean.setMinimumLength(this.removeMinimumLength(instanceProperties));
                bean.setLength(this.removeLength(instanceProperties));
                bean.setPrecision(this.removePrecision(instanceProperties));
                bean.setIsNullable(this.removeIsNullable(instanceProperties));
                bean.setNativeJavaClass(this.removeNativeClass(instanceProperties));
                bean.setAliases(this.removeAliases(instanceProperties));
                bean.setSortOrder(this.removeSortOrder(instanceProperties));

                if (schemaAttributeRelationships != null)
                {
                    for (Relationship relationship : schemaAttributeRelationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null) &&
                                ((relationship.getType().getTypeDefName().equals(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName)) ||
                                 (relationship.getType().getTypeDefName().equals(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName))))
                        {
                            bean.setElementPosition(this.getPosition(instanceProperties));
                            bean.setMinCardinality(this.getMinCardinality(instanceProperties));
                            bean.setMaxCardinality(this.getMaxCardinality(instanceProperties));
                        }
                    }
                }

                instanceProperties = super.getClassificationProperties(OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName, schemaAttributeEntity);

                bean.setIsCalculatedValue(instanceProperties != null);
                bean.setExpression(this.getFormula(instanceProperties));
                bean.setExpressionType(this.getFormulaType(instanceProperties));

                bean.setAttributeType((SchemaType) schemaType);
                bean.setAttributeRelationships(this.getSchemaAttributeRelationships(schemaAttributeEntity.getGUID(), schemaAttributeRelationships));
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
     * Retrieve the relationships to other schema attributes.
     *
     * @param schemaAttributeGUID unique identifier of the root schema attribute
     * @param relationships relationships retrieved from the
     * @return list of bean relationships
     */
    private List<SchemaAttributeRelationship>  getSchemaAttributeRelationships(String             schemaAttributeGUID,
                                                                               List<Relationship> relationships)
    {
        List<SchemaAttributeRelationship> schemaAttributeRelationships = new ArrayList<>();

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if ((relationship != null) && (relationship.getType() != null) &&
                        ((relationship.getType().getTypeDefName().equals(OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName)) ||
                         (relationship.getType().getTypeDefName().equals(OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName))))
                {
                    SchemaAttributeRelationship schemaAttributeRelationship = new SchemaAttributeRelationship();

                    schemaAttributeRelationship.setLinkType(relationship.getType().getTypeDefName());
                    schemaAttributeRelationship.setLinkedAttributeName(repositoryHelper.getOtherEndName(serviceName, schemaAttributeGUID, relationship));

                    EntityProxy otherEnd = relationship.getEntityOneProxy();

                    if ((otherEnd != null) && (schemaAttributeGUID.equals(otherEnd.getGUID())))
                    {
                        otherEnd = relationship.getEntityTwoProxy();
                    }

                    if (otherEnd != null)
                    {
                        schemaAttributeGUID = otherEnd.getGUID();
                    }

                    schemaAttributeRelationship.setLinkProperties(repositoryHelper.getInstancePropertiesAsMap(relationship.getProperties()));

                    schemaAttributeRelationships.add(schemaAttributeRelationship);
                }
            }
        }

        if (schemaAttributeRelationships.isEmpty())
        {
            return null;
        }

        return schemaAttributeRelationships;
    }
}
