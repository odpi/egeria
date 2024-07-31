/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationship;
import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * MetadataRelationshipConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * Relationship object into a MetadataRelationship bean.
 */
public class MetadataRelationshipConverter<B> extends OMFConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public MetadataRelationshipConverter(OMRSRepositoryHelper repositoryHelper,
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
     * @return bean populated with properties from the entity supplied
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

            if (returnBean instanceof MetadataElementSummary bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, entity, OpenMetadataType.OPEN_METADATA_ROOT.typeName, methodName);

                /*
                 * Save the properties as a map.
                 */
                if (entity.getProperties() != null)
                {
                    Iterator<String> propertyNames = entity.getProperties().getPropertyNames();

                    Map<String, String> propertyValues = new HashMap<>();

                    while (propertyNames.hasNext())
                    {
                        String propertyName = propertyNames.next();

                        InstancePropertyValue instancePropertyValue = entity.getProperties().getPropertyValue(propertyName);

                        propertyValues.put(propertyName, instancePropertyValue.valueAsString());
                    }

                    bean.setProperties(propertyValues);
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


    /**
     * Using the supplied relationship, return a new instance of the bean.  It is used for beans that
     * represent a simple relationship between two entities.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship linking the entities
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unused")
    public B getNewRelationshipBean(Class<B>     beanClass,
                                    Relationship relationship,
                                    String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof MetadataRelationship bean)
            {
                /*
                 * Check that the entity is of the correct type.
                 */
                this.setUpElementHeader(bean, relationship, null, methodName);

                /*
                 * Save the properties as a map.
                 */
                bean.setProperties(this.getRemainingExtendedProperties(relationship.getProperties()));
                bean.setEnd1(this.getElementStub(beanClass, relationship.getEntityOneProxy(), methodName));
                bean.setEnd2(this.getElementStub(beanClass, relationship.getEntityTwoProxy(), methodName));
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
