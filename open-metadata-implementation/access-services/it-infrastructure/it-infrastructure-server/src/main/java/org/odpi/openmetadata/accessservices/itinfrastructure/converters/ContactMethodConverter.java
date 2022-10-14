/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.converters;


import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ContactMethodElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.ContactMethodProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


import java.lang.reflect.InvocationTargetException;

/**
 * ContactMethodConverter generates a ContactMethodProperties bean from a ContactMethodProperties entity.
 */
public class ContactMethodConverter<B> extends ITInfrastructureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ContactMethodConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof ContactMethodElement)
            {
                ContactMethodElement    bean                    = (ContactMethodElement) returnBean;
                ContactMethodProperties contactMethodProperties = new ContactMethodProperties();

                bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                InstanceProperties entityProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    entityProperties = new InstanceProperties(entity.getProperties());

                    contactMethodProperties.setName(this.removeName(entityProperties));
                    contactMethodProperties.setContactType(this.removeContactType(entityProperties));
                    contactMethodProperties.setContactMethodType(this.getContactMethodTypeFromProperties(entityProperties));
                    contactMethodProperties.setContactMethodService(this.removeContactMethodService(entityProperties));
                    contactMethodProperties.setContactMethodValue(this.removeContactMethodValue(entityProperties));
                    contactMethodProperties.setEffectiveFrom(entityProperties.getEffectiveFromTime());
                    contactMethodProperties.setEffectiveTo(entityProperties.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    contactMethodProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    contactMethodProperties.setExtendedProperties(this.getRemainingExtendedProperties(entityProperties));
                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                bean.setProperties(contactMethodProperties);
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
    @SuppressWarnings(value = "unused")
    @Override
    public B getNewBean(Class<B>     beanClass,
                        EntityDetail entity,
                        Relationship relationship,
                        String       methodName) throws PropertyServerException
    {
        return this.getNewBean(beanClass, entity, methodName);
    }
}
