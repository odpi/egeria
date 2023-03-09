/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.converters;


import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * ElementStubConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityProxy object into an ElementStub bean.
 */
public class ElementStubConverter<B> extends ITInfrastructureOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ElementStubConverter(OMRSRepositoryHelper repositoryHelper,
                                String               serviceName,
                                String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an entityProxy and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param entityProxy entityProxy containing the properties
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    @SuppressWarnings(value = "unchecked")
    private B getNewBean(Class<B>    beanClass,
                         EntityProxy entityProxy,
                         String      methodName) throws PropertyServerException
    {
        try
        {
            /*
             * This is initial confirmation that the generic converter has been initialized with an appropriate bean class.
             */
            B returnBean = beanClass.getDeclaredConstructor().newInstance();

            if (returnBean instanceof ElementStub)
            {
                returnBean = (B)super.getElementStub(beanClass, entityProxy, methodName);

                return returnBean;
            }

            return null;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }

        return null;
    }


    /**
     * Using the supplied instances, return a new instance of the bean. This is used for beans that
     * contain a combination of the properties from an relationship and that of a connected relationship.
     *
     * @param beanClass name of the class to create
     * @param relationship relationship containing the properties
     * @param useEnd1 should the
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public B getNewBean(Class<B>     beanClass,
                        Relationship relationship,
                        boolean      useEnd1,
                        String       methodName) throws PropertyServerException
    {
        if (relationship != null)
        {
            if (useEnd1)
            {
                return getNewBean(beanClass, relationship.getEntityOneProxy(), methodName);
            }
            else
            {
                return getNewBean(beanClass, relationship.getEntityTwoProxy(), methodName);
            }
        }

        return null;
    }


    /**
     * Using the supplied instances, return list of new instances of the bean.
     *
     * @param beanClass name of the class to create
     * @param relationships list of relationships containing the properties
     * @param useEnd1 should the
     * @param methodName calling method
     * @return bean populated with properties from the instances supplied
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    public List<B> getNewBean(Class<B>           beanClass,
                              List<Relationship> relationships,
                              boolean            useEnd1,
                              String             methodName) throws PropertyServerException
    {
        if (relationships != null)
        {
            List<B> beans = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    B bean = getNewBean(beanClass, relationship, useEnd1, methodName);

                    if (bean != null)
                    {
                        beans.add(bean);
                    }
                }
            }

            if (! beans.isEmpty())
            {
                return beans;
            }
        }

        return null;
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
    @SuppressWarnings(value = "unchecked")
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

            EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(serviceName, entity);

            if (returnBean instanceof ElementStub)
            {
                returnBean = (B)super.getElementStub(beanClass, entityProxy, methodName);

                return returnBean;
            }

            return null;
        }
        catch (IllegalAccessException | InstantiationException | ClassCastException | NoSuchMethodException | InvocationTargetException error)
        {
            super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
        }
        catch (Exception error)
        {
            super.handleBadEntity(beanClass.getName(), entity, methodName);
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
