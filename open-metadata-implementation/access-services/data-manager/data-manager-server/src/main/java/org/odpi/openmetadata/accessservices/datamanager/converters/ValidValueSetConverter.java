/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.converters;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.ValidValueSetElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ValidValueSetProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ValidValueConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a ValidValueElement bean.
 */
public class ValidValueSetConverter<B> extends DataManagerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ValidValueSetConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof ValidValueSetElement)
            {
                ValidValueSetElement    bean                 = (ValidValueSetElement) returnBean;
                ValidValueSetProperties validValueProperties = new ValidValueSetProperties();

                if (entity != null)
                {
                    /*
                     * Check that the entity is of the correct type.
                     */
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The property values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    validValueProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    validValueProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    validValueProperties.setDisplayName(this.removeName(instanceProperties));
                    validValueProperties.setDescription(this.removeDescription(instanceProperties));
                    validValueProperties.setCategory(this.removeCategory(instanceProperties));
                    validValueProperties.setUsage(this.removeUsage(instanceProperties));
                    validValueProperties.setScope(this.removeScope(instanceProperties));
                    validValueProperties.setPreferredValue(this.removePreferredValue(instanceProperties));
                    validValueProperties.setIsDeprecated(this.removeIsDeprecated(instanceProperties));
                    validValueProperties.setIsCaseSensitive(this.removeIsCaseSensitive(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    validValueProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    validValueProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(validValueProperties);
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
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
