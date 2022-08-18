/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.converters;


import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ExternalReferenceConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from ExternalReferenceElement.
 */
public class ExternalReferenceConverter<B> extends GovernanceProgramOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ExternalReferenceConverter(OMRSRepositoryHelper repositoryHelper,
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
        return getNewBean(beanClass, entity, null, methodName);
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

            if (returnBean instanceof ExternalReferenceElement)
            {
                ExternalReferenceElement bean = (ExternalReferenceElement) returnBean;

                if (entity != null)
                {
                    bean.setElementHeader(this.getMetadataElementHeader(beanClass, entity, entity.getClassifications(), methodName));
                    ExternalReferenceProperties externalReferenceProperties = new ExternalReferenceProperties();

                    /*
                     * The initial set of values come from the entity.
                     */
                    InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

                    externalReferenceProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    externalReferenceProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    externalReferenceProperties.setDisplayName(this.removeDisplayName(instanceProperties));
                    externalReferenceProperties.setURI(this.removeURL(instanceProperties));
                    externalReferenceProperties.setVersion(this.removeReferenceVersion(instanceProperties));
                    externalReferenceProperties.setResourceDescription(this.removeDescription(instanceProperties));
                    externalReferenceProperties.setOrganization(this.removeOrganization(instanceProperties));

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    externalReferenceProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    externalReferenceProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                    if (relationship != null)
                    {
                        instanceProperties = relationship.getProperties();

                        externalReferenceProperties.setLinkDescription(this.getDescription(instanceProperties));
                        externalReferenceProperties.setLocalReferenceId(this.getReferenceId(instanceProperties));
                    }

                    bean.setProperties(externalReferenceProperties);
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
