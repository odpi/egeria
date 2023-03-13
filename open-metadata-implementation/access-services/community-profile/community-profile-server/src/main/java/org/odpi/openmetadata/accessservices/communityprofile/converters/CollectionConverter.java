/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.CollectionElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.PersonalRoleElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionOrder;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CollectionProperties;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;


/**
 * CollectionConverter generates a CollectionElement from a Collection entity
 */
public class CollectionConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CollectionConverter(OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied entity, return a new instance of the bean. This is used for most beans that have
     * a one to one correspondence with the repository instances.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof CollectionElement)
            {
                CollectionElement    bean              = (CollectionElement) returnBean;
                CollectionProperties collectionProperties = new CollectionProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    collectionProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    collectionProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    collectionProperties.setName(this.removeName(instanceProperties));
                    collectionProperties.setDescription(this.removeDescription(instanceProperties));
                    collectionProperties.setCollectionOrdering(this.removeCollectionOrderFromProperties(instanceProperties));
                    collectionProperties.setOrderPropertyName(this.removeOrderPropertyName(instanceProperties));
                    collectionProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    collectionProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    collectionProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    collectionProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                bean.setProperties(collectionProperties);
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
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entity and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entity entity containing the properties
     * @param relationship relationship containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        B returnBean = this.getNewBean(beanClass, entity, methodName);

        if (returnBean instanceof CollectionElement)
        {
            CollectionElement bean = (CollectionElement) returnBean;

            bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
        }

        return returnBean;
    }


    /**
     * Retrieve and delete the CollectionOrder enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return CollectionOrder  enum value
     */
    private CollectionOrder removeCollectionOrderFromProperties(InstanceProperties   properties)
    {
        CollectionOrder collectionOrder = this.getCollectionOrderFromProperties(properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(OpenMetadataAPIMapper.ORDER_BY_PROPERTY_NAME);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return collectionOrder;
    }


    /**
     * Retrieve the CollectionOrder enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return CollectionOrder  enum value
     */
    private CollectionOrder getCollectionOrderFromProperties(InstanceProperties   properties)
    {
        CollectionOrder collectionOrder = CollectionOrder.NAME;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(OpenMetadataAPIMapper.ORDER_PROPERTY_NAME_PROPERTY_NAME);

                if (instancePropertyValue instanceof EnumPropertyValue)
                {
                    EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                    switch (enumPropertyValue.getOrdinal())
                    {
                        case 0:
                            collectionOrder = CollectionOrder.NAME;
                            break;

                        case 1:
                            collectionOrder = CollectionOrder.OWNER;
                            break;

                        case 2:
                            collectionOrder = CollectionOrder.DATE_ADDED;
                            break;

                        case 3:
                            collectionOrder = CollectionOrder.DATE_UPDATED;
                            break;

                        case 4:
                            collectionOrder = CollectionOrder.DATE_CREATED;
                            break;

                        case 99:
                            collectionOrder = CollectionOrder.OTHER;
                            break;
                    }
                }
            }
        }

        return collectionOrder;
    }
}
