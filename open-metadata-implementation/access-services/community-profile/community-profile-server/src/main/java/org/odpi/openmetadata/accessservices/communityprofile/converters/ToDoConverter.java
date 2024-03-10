/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ToDoElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoProperties;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoStatus;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ToDoConverter generates a To Do bean from an To Do entity.
 */
public class ToDoConverter<B> extends CommunityProfileOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ToDoConverter(OMRSRepositoryHelper repositoryHelper,
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

            if (returnBean instanceof ToDoElement)
            {
                ToDoElement    bean           = (ToDoElement) returnBean;
                ToDoProperties toDoProperties = new ToDoProperties();

                InstanceProperties instanceProperties;

                /*
                 * The initial set of values come from the entity.
                 */
                if (entity != null)
                {
                    bean.setElementHeader(super.getMetadataElementHeader(beanClass, entity, methodName));

                    /*
                     * The initial set of values come from the entity.
                     */
                    instanceProperties = new InstanceProperties(entity.getProperties());

                    toDoProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    toDoProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));
                    toDoProperties.setName(this.removeName(instanceProperties));
                    toDoProperties.setDescription(this.removeDescription(instanceProperties));
                    toDoProperties.setCreationTime(this.removeCreationTime(instanceProperties));
                    toDoProperties.setPriority(this.removeIntPriority(instanceProperties));
                    toDoProperties.setLastReviewTime(this.removeLastReviewTime(instanceProperties));
                    toDoProperties.setDueTime(this.removeDueTime(instanceProperties));
                    toDoProperties.setCompletionTime(this.removeCompletionTime(instanceProperties));
                    toDoProperties.setStatus(this.getToDoStatusFromProperties(instanceProperties));
                    toDoProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    toDoProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    toDoProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    toDoProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                bean.setProperties(toDoProperties);
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

        if (returnBean instanceof ToDoElement)
        {
            ToDoElement bean = (ToDoElement) returnBean;

            bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
        }

        return returnBean;
    }


    /**
     * Retrieve the ToDoStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ToDoStatus  enum value
     */
    private ToDoStatus getToDoStatusFromProperties(InstanceProperties   properties)
    {
        final String methodName = "getToDoStatusFromProperties";

        ToDoStatus   toDoStatus = ToDoStatus.ABANDONED;

        if (properties != null)
        {
            int ordinal = repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                     OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                     properties,
                                                                     methodName);

            switch (ordinal)
            {
                case 0:
                    toDoStatus = ToDoStatus.OPEN;
                    break;

                case 1:
                    toDoStatus = ToDoStatus.IN_PROGRESS;
                    break;

                case 2:
                    toDoStatus = ToDoStatus.WAITING;
                    break;

                case 3:
                    toDoStatus = ToDoStatus.COMPLETE;
                    break;

                case 99:
                    toDoStatus = ToDoStatus.ABANDONED;
                    break;

            }

        }

        return toDoStatus;
    }
}
