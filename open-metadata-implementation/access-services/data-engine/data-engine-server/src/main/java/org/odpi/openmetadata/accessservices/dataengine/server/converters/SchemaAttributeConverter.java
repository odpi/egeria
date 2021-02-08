/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.SchemaAttribute;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;


/**
 * LicenseConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an License bean.
 */
public class SchemaAttributeConverter<B> extends OpenMetadataAPIGenericConverter<B> {

    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       local server name
     */
    public SchemaAttributeConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

    /**
     * Extract the properties from the schema attribute entity.  Each API creates a specialization of this method for its beans.
     *
     * @param beanClass                    name of the class to create
     * @param schemaAttributeEntity        entity containing the properties for the main schema attribute
     * @param typeClass                    name of type used to describe the schema type
     * @param schemaType                   bean containing the properties of the schema type - this is filled out by the schema type converter
     * @param schemaAttributeRelationships relationships containing the links to other schema attributes
     * @param <T>                          bean type used to create the schema type
     * @param methodName                   calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public <T> B getNewSchemaAttributeBean(Class<B> beanClass, EntityDetail schemaAttributeEntity, Class<T> typeClass,
                                           T schemaType, List<Relationship> schemaAttributeRelationships, String methodName) throws PropertyServerException {
        try {
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof SchemaAttribute) {
                SchemaAttribute bean = (SchemaAttribute) returnBean;

                bean.setGUID(schemaAttributeEntity.getGUID());

                InstanceProperties instanceProperties = new InstanceProperties(schemaAttributeEntity.getProperties());

                bean.setDataType(this.removeDataType(instanceProperties));
                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setAnchorGUID(this.removeAnchorGUID(instanceProperties));
                bean.setPosition(this.removePosition(instanceProperties));
                bean.setMinCardinality(this.removeMinCardinality(instanceProperties));
                bean.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                bean.setAllowsDuplicateValues(this.removeAllowsDuplicateValues(instanceProperties));
                bean.setOrderedValues(this.removeOrderedValues(instanceProperties));
                bean.setDefaultValueOverride(this.removeDefaultValueOverride(instanceProperties));
                bean.setDefaultValue(this.removeDefaultValue(instanceProperties));
            }

            return returnBean;
        } catch (IllegalAccessException | InstantiationException | ClassCastException error) {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }
        return null;
    }
}
