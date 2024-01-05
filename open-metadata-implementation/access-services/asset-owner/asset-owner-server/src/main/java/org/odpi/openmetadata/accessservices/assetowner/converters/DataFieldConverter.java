/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.converters;

import org.odpi.openmetadata.commonservices.generichandlers.ODFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.discovery.properties.DataField;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * DataFieldConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DataField bean.  It should be passed the data field entity and all relationships linked to it.
 */
public class DataFieldConverter<B> extends ODFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName this server
     */
    public DataFieldConverter(OMRSRepositoryHelper repositoryHelper,
                              String               serviceName,
                              String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean.  It is used for beans such as
     * a connection bean which made up of 3 entities (Connection, ConnectorType and Endpoint) plus the
     * relationships between them.  The relationships may be omitted if they do not have any properties.
     *
     * @param beanClass name of the class to create
     * @param primaryEntity entity that is the root of the collection of entities that make up the
     *                      content of the bean
     * @param supplementaryEntities entities connected to the primary entity by the relationships
     * @param relationships relationships linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewComplexBean(Class<B>           beanClass,
                               EntityDetail       primaryEntity,
                               List<EntityDetail> supplementaryEntities,
                               List<Relationship> relationships,
                               String             methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof DataField)
            {
                DataField bean = (DataField) returnBean;

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, primaryEntity, methodName));

                InstanceProperties instanceProperties = null;

                /*
                 * The initial set of values come from the entity.
                 */
                if (primaryEntity != null)
                {
                    instanceProperties = new InstanceProperties(primaryEntity.getProperties());
                }

                bean.setDataFieldName(this.removeDataFieldName(instanceProperties));
                bean.setDataFieldType(this.removeDataFieldType(instanceProperties));
                bean.setDataFieldDescription(this.removeDataFieldDescription(instanceProperties));
                bean.setDataFieldNamespace(this.removeDataFieldNamespace(instanceProperties));
                bean.setDataFieldAliases(this.removeDataFieldAliases(instanceProperties));
                bean.setDataFieldSortOrder(this.removeSortOrder(instanceProperties));
                bean.setMinCardinality(this.removeMinCardinality(instanceProperties));
                bean.setMaxCardinality(this.removeMaxCardinality(instanceProperties));
                bean.setIsNullable(this.removeIsNullable(instanceProperties));
                bean.setMinimumLength(this.removeMinimumLength(instanceProperties));
                bean.setLength(this.removeLength(instanceProperties));
                bean.setPrecision(this.removePrecision(instanceProperties));
                bean.setSignificantDigits(this.removeSignificantDigits(instanceProperties));
                bean.setDefaultValue(this.removeDefaultValue(instanceProperties));
                bean.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
                bean.setVersion(this.removeVersion(instanceProperties));
                bean.setVersionIdentifier(this.removeVersionIdentifier(instanceProperties));
                bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                if ((relationships != null) && (! relationships.isEmpty()))
                {
                    int nestedDataFields    = 0;
                    int linkedDataFields    = 0;
                    int attachedAnnotations = 0;

                    for (Relationship relationship : relationships)
                    {
                        if ((relationship != null) && (relationship.getType() != null) && (relationship.getType().getTypeDefName() != null))
                        {

                            if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.DISCOVERED_DATA_FIELD_TYPE_NAME))
                            {
                                bean.setDataFieldPosition(repositoryHelper.getIntProperty(serviceName,
                                                                                          OpenMetadataType.DATA_FIELD_POSITION_PROPERTY_NAME,
                                                                                          relationship.getProperties(),
                                                                                          methodName));
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.DISCOVERED_NESTED_DATA_FIELD_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if ((endOne != null) && (endOne.getGUID() != null) && (endOne.getGUID().equals(primaryEntity.getGUID())))
                                {
                                    nestedDataFields++;
                                }
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.DATA_FIELD_ANALYSIS_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if ((endOne != null) && (endOne.getGUID() != null) && (endOne.getGUID().equals(primaryEntity.getGUID())))
                                {
                                    attachedAnnotations++;
                                }
                            }
                            else if (repositoryHelper.isTypeOf(serviceName, relationship.getType().getTypeDefName(), OpenMetadataType.DISCOVERED_LINKED_DATA_FIELD_TYPE_NAME))
                            {
                                EntityProxy endOne = relationship.getEntityOneProxy();

                                if ((endOne != null) && (endOne.getGUID() != null) && (endOne.getGUID().equals(primaryEntity.getGUID())))
                                {
                                    linkedDataFields++;
                                }
                            }
                        }
                    }

                    bean.setNestedDataFields(nestedDataFields);
                    bean.setLinkedDataFields(linkedDataFields);
                    bean.setDataFieldAnnotations(attachedAnnotations);
                }

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                bean.setTypeName(bean.getElementHeader().getType().getTypeName());
                bean.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
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
