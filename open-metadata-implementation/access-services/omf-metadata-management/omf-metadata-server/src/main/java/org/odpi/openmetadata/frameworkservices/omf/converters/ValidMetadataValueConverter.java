/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.converters;

import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ValidMetadataValueConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ValidMetadataValue bean.
 */
public class ValidMetadataValueConverter<B> extends OpenMetadataStoreConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName this server
     */
    public ValidMetadataValueConverter(OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName,
                                       String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
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

            if (returnBean instanceof ValidMetadataValue)
            {
                ValidMetadataValue bean = (ValidMetadataValue) returnBean;

                if (entity != null)
                {
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    bean.setCategory(this.removeCategory(instanceProperties));
                    bean.setDisplayName(this.removeName(instanceProperties));
                    bean.setDescription(this.removeDescription(instanceProperties));
                    bean.setPreferredValue(this.removePreferredValue(instanceProperties));
                    bean.setDataType(this.removeDataType(instanceProperties));
                    bean.setIsCaseSensitive(this.removeIsCaseSensitive(instanceProperties));
                    bean.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    bean.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    bean.setEffectiveTo(instanceProperties.getEffectiveToTime());

                }
                else
                {
                    handleMissingMetadataInstance(bean.getClass().getName(), TypeDefCategory.ENTITY_DEF, methodName);
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
        return getNewBean(beanClass, entity, methodName);
    }
}
