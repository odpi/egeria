/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;


/**
 * SchemaTypeConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an SchemaType bean.
 */
public class SchemaTypeConverter<B> extends OpenMetadataAPIGenericConverter<B> {
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName      name of this component
     * @param serverName       local server name
     */
    public SchemaTypeConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }

    /**
     * Return the converted bean.  This is a special method used for schema types since they are stored
     * as a collection of instances.
     *
     * @param beanClass          name of the class to create
     * @param schemaRootHeader   unique identifier of the schema element that holds the root information
     * @param schemaTypeTypeName name of type of the schema type to create
     * @param instanceProperties properties describing the schema type
     * @param attributeCount     number of attributes (for a complex schema type)
     * @param validValueSetGUID  unique identifier of the set of valid values (for an enum schema type)
     * @param externalSchemaType bean containing the properties of the schema type that is shared by multiple attributes/assets
     * @param mapFromSchemaType  bean containing the properties of the schema type that is part of a map definition
     * @param mapToSchemaType    bean containing the properties of the schema type that is part of a map definition
     * @param schemaTypeOptions  list of schema types that could be the type for this attribute
     * @param methodName         calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewSchemaTypeBean(Class<B> beanClass, InstanceHeader schemaRootHeader, String schemaTypeTypeName,
                                  InstanceProperties instanceProperties, List<Classification> schemaRootClassifications,
                                  int attributeCount, String validValueSetGUID, B externalSchemaType, B mapFromSchemaType,
                                  B mapToSchemaType, List<B> schemaTypeOptions, String methodName) throws PropertyServerException {
        try {
            B returnBean = beanClass.newInstance();

            if (returnBean instanceof SchemaType) {
                SchemaType bean = (SchemaType) returnBean;
                bean.setQualifiedName(this.removeQualifiedName(instanceProperties));
                bean.setDisplayName(this.removeDisplayName(instanceProperties));
                bean.setAuthor(this.removeAuthor(instanceProperties));
                bean.setUsage(this.removeUsage(instanceProperties));
                bean.setEncodingStandard(this.removeEncodingStandard(instanceProperties));
                bean.setVersionNumber(this.removeVersionNumber(instanceProperties));
                bean.setType(this.removeDataType(instanceProperties));
            }

            return returnBean;
        } catch (IllegalAccessException | InstantiationException | ClassCastException error) {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }
        return null;
    }
}
