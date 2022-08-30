/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.converters;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.ReferenceDataAssetElement;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.ReferenceDataAssetProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ReferenceDataAssetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from AssetProperties.
 */
public class ReferenceDataAssetConverter<B> extends DigitalArchitectureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ReferenceDataAssetConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof ReferenceDataAssetElement)
            {
                ReferenceDataAssetElement bean = (ReferenceDataAssetElement) returnBean;

                this.updateSimpleMetadataElement(beanClass, bean, entity, methodName);
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

            if (returnBean instanceof ReferenceDataAssetElement)
            {
                ReferenceDataAssetElement bean = (ReferenceDataAssetElement) returnBean;

                this.updateSimpleMetadataElement(beanClass, bean, entity, methodName);
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
    void updateSimpleMetadataElement(Class<B>                  beanClass,
                                     ReferenceDataAssetElement bean,
                                     EntityDetail              entity,
                                     String                    methodName)  throws PropertyServerException
    {
        if (entity != null)
        {
            bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));
            ReferenceDataAssetProperties referenceDataAssetProperties = new ReferenceDataAssetProperties();

            /*
             * The initial set of values come from the entity.
             */
            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            referenceDataAssetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
            referenceDataAssetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
            referenceDataAssetProperties.setDisplayName(this.removeName(instanceProperties));
            referenceDataAssetProperties.setDescription(this.removeDescription(instanceProperties));

            /*
             * Any remaining properties are returned in the extended properties.  They are
             * assumed to be defined in a subtype.
             */
            referenceDataAssetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
            referenceDataAssetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

            bean.setReferenceDataAssetProperties(referenceDataAssetProperties);
        }
        else
        {
            handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
        }
    }
}
