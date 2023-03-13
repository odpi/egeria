/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.LocationElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.LocationProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * LocationConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from LocationElement.
 */
public class LocationConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public LocationConverter(OMRSRepositoryHelper repositoryHelper,
                             String serviceName,
                             String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof LocationElement)
            {
                LocationElement bean = (LocationElement) returnBean;

                this.updateSimpleMetadataElement(beanClass, bean, entity, methodName);

                bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
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

            if (returnBean instanceof  LocationElement)
            {
                this.updateSimpleMetadataElement(beanClass, (LocationElement) returnBean, entity, methodName);
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
     * Extract the properties from the entity.  Each concrete DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param beanClass name of the class to create
     * @param bean output bean
     * @param entity entity containing the properties
     * @param methodName calling method
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    void updateSimpleMetadataElement(Class<B>        beanClass,
                                     LocationElement bean,
                                     EntityDetail    entity,
                                     String          methodName)  throws PropertyServerException
    {
        if (entity != null)
        {
            bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));
            LocationProperties locationProperties = new LocationProperties();

            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            locationProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
            locationProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
            locationProperties.setIdentifier(this.removeIdentifier(instanceProperties));
            locationProperties.setDisplayName(this.removeDisplayName(instanceProperties));
            locationProperties.setDescription(this.removeDescription(instanceProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            locationProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
            locationProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            bean.setLocationProperties(locationProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
    }
}
