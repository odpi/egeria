/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;


import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainSetProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * GovernanceDomainSetConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from GovernanceDomainSetElement.
 */
public class GovernanceDomainSetConverter<B> extends GovernanceProgramOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public GovernanceDomainSetConverter(OMRSRepositoryHelper repositoryHelper,
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
    @SuppressWarnings(value = "unused")
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

            if (returnBean instanceof GovernanceDomainSetElement)
            {
                GovernanceDomainSetElement bean = (GovernanceDomainSetElement) returnBean;

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, entity.getClassifications(), methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    GovernanceDomainSetProperties governanceDomainSetProperties = new GovernanceDomainSetProperties();

                    governanceDomainSetProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    governanceDomainSetProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    governanceDomainSetProperties.setDisplayName(this.removeName(instanceProperties));
                    governanceDomainSetProperties.setDescription(this.removeDescription(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    governanceDomainSetProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    governanceDomainSetProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    bean.setProperties(governanceDomainSetProperties);
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
}
