/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.CollectionMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Classification;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Collection;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * CollectionConverter generates a PersonalRole from an PersonRole entity
 */
public class CollectionConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(CollectionConverter.class);

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    CollectionConverter(EntityDetail         entity,
                        Relationship         relationship,
                        OMRSRepositoryHelper repositoryHelper,
                        String               componentName)
    {
        super(entity, relationship, repositoryHelper, componentName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public Collection getBean()
    {
        final String methodName = "getBean";

        Collection  bean = new Collection();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * role properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, CollectionMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, CollectionMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, CollectionMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, CollectionMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }

            List<Classification>  classifications = super.getClassificationsFromEntity();

            if (classifications != null)
            {
                bean.setClassifications(classifications);

                for (Classification  classification : classifications)
                {
                    if (classification != null)
                    {
                        if (CollectionMapper.FOLDER_TYPE_NAME.equals(classification.getName()))
                        {
                            Map<String, Object>   classificationProperties = classification.getProperties();

                            if (classificationProperties != null)
                            {
                                String  orderBy           = null;
                                String  orderPropertyName = null;

                                for (String propertyName : classificationProperties.keySet())
                                {
                                    if (CollectionMapper.COLLECTION_ORDERING_ENUM_PROPERTY_NAME.equals(propertyName))
                                    {
                                        orderBy = classificationProperties.get(propertyName).toString();
                                    }
                                    else if (CollectionMapper.COLLECTION_ORDERING_OTHER_PROPERTY_NAME.equals(propertyName))
                                    {
                                        orderPropertyName = classificationProperties.get(propertyName).toString();
                                    }
                                }

                                this.setUpOrderByProperty(orderBy, orderPropertyName, bean);
                            }
                        }
                    }
                }

            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }


    /**
     * The order of the collection is stored as a classification on the collection entity.
     * Convert the orderBy and OrderPropertyName properties from the classification to a single
     * CollectionOrder Enum for the collection bean.
     *
     * @param orderBy orderBy classification property
     * @param orderByPropertyName orderPropertyName classification property
     * @param bean been to update
     */
    private void setUpOrderByProperty(String        orderBy,
                                      String        orderByPropertyName,
                                      Collection    bean)
    {
        if (orderBy != null)
        {
            if (CollectionMapper.ORDER_BY_NAME_VALUE.equals(orderBy))
            {
                bean.setCollectionOrdering(CollectionOrder.NAME);
            }
            else if (CollectionMapper.ORDER_BY_OWNER_VALUE.equals(orderBy))
            {
                bean.setCollectionOrdering(CollectionOrder.OWNER);
            }
            else if (CollectionMapper.ORDER_BY_DATE_ADDED_VALUE.equals(orderBy))
            {
                bean.setCollectionOrdering(CollectionOrder.DATE_ADDED);
            }
            else if (CollectionMapper.ORDER_BY_DATE_UPDATED_VALUE.equals(orderBy))
            {
                bean.setCollectionOrdering(CollectionOrder.DATE_UPDATED);
            }
            else if (CollectionMapper.ORDER_BY_DATE_CREATED_VALUE.equals(orderBy))
            {
                bean.setCollectionOrdering(CollectionOrder.DATE_CREATED);
            }
            else if (CollectionMapper.ORDER_BY_OTHER_VALUE.equals(orderBy))
            {
                if (CollectionMapper.QUALIFIED_NAME_PROPERTY_NAME.equals(orderByPropertyName))
                {
                    bean.setCollectionOrdering(CollectionOrder.QUALIFIED_NAME);
                }
                else
                {
                    bean.setCollectionOrdering(CollectionOrder.TYPE_NAME);
                }
            }
        }

        log.debug("OrderBy: " + bean.getCollectionOrdering());
    }
}
