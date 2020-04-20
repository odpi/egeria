/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SchemaElementMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * SchemaLinkConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into an SchemaLink bean.
 */
public class SchemaLinkConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity schema type entity
     * @param linkedTypeRelationship number of attributes nested inside this schema type
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public SchemaLinkConverter(EntityDetail             entity,
                               Relationship             linkedTypeRelationship,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName)
    {
        super(entity, linkedTypeRelationship, repositoryHelper, serviceName);

        if (entity != null)
        {
            InstanceType type = entity.getType();
            if (type != null)
            {
                this.typeName = type.getTypeDefName();
            }
        }
    }

    
    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public SchemaLink getBean()
    {
        final String  methodName = "getBean";

        SchemaLink bean = null;

        if (entity != null)
        {
            bean = new SchemaLink();

            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                            SchemaElementMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          SchemaElementMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDeprecated(repositoryHelper.removeBooleanProperty(serviceName,
                                                                          SchemaElementMapper.IS_DEPRECATED_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setLinkName(repositoryHelper.removeStringProperty(serviceName,
                                                                       SchemaElementMapper.LINK_NAME_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));
                bean.setLinkProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                     SchemaElementMapper.LINK_PROPERTIES_PROPERTY_NAME,
                                                                                     instanceProperties,
                                                                                     methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));

                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }

            bean.setLinkType(typeName);
            if (relationship != null)
            {
                EntityProxy end2 = relationship.getEntityTwoProxy();
                if (end2 != null)
                {
                    bean.setLinkedSchemaTypeGUID(end2.getGUID());
                    if (end2.getType() != null)
                    {
                        bean.setLinkedSchemaTypeName(end2.getType().getTypeDefName());
                    }
                }
            }
        }

        return bean;
    }


    /**
     * Set up the properties found in every schema type.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the entity
     * @param methodName calling method
     */
    private void updateBasicSchemaTypeProperties(SchemaType         bean,
                                                 InstanceProperties instanceProperties,
                                                 String             methodName)
    {

        if (instanceProperties != null)
        {
            bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                        ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));
            bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                      SchemaElementMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));

            bean.setVersionNumber(repositoryHelper.removeStringProperty(serviceName,
                                                                        SchemaElementMapper.VERSION_NUMBER_PROPERTY_NAME,
                                                                        instanceProperties,
                                                                        methodName));

            bean.setAuthor(repositoryHelper.removeStringProperty(serviceName,
                                                                 SchemaElementMapper.AUTHOR_PROPERTY_NAME,
                                                                 instanceProperties,
                                                                 methodName));

            bean.setUsage(repositoryHelper.removeStringProperty(serviceName,
                                                                SchemaElementMapper.USAGE_PROPERTY_NAME,
                                                                instanceProperties,
                                                                methodName));

            bean.setEncodingStandard(repositoryHelper.removeStringProperty(serviceName,
                                                                           SchemaElementMapper.ENCODING_STANDARD_PROPERTY_NAME,
                                                                           instanceProperties,
                                                                           methodName));

            bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                      ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                      instanceProperties,
                                                                                      methodName));
        }
    }


    /**
     * Set up the common properties for a literal schema type.  Processed properties are removed from the instance
     * properties object.
     *
     * @param bean bean to fill
     * @param instanceProperties properties from the repository
     * @param methodName calling method
     */
    private void updateLiteralSchemaTypeProperties(LiteralSchemaType  bean,
                                                   InstanceProperties instanceProperties,
                                                   String             methodName)
    {
        if (instanceProperties != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            this.updateBasicSchemaTypeProperties(bean, instanceProperties, methodName);

            bean.setDataType(repositoryHelper.removeStringProperty(serviceName,
                                                                   SchemaElementMapper.DATA_TYPE_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));
            bean.setFixedValue(repositoryHelper.removeStringProperty(serviceName,
                                                                     SchemaElementMapper.FIXED_VALUE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
        }
    }
}
