/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.projectmanagement.converters;

import org.odpi.openmetadata.accessservices.projectmanagement.properties.ProjectProperties;
import org.odpi.openmetadata.accessservices.projectmanagement.metadataelements.ProjectElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.lang.reflect.InvocationTargetException;


/**
 * ProjectConverter generates an ProjectCollectionMember bean from an Project entity
 * and a ResourceList relationship to it.
 */
public class ProjectConverter<B> extends ProjectManagementOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public ProjectConverter(OMRSRepositoryHelper repositoryHelper,
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
        return this.getNewBean(beanClass, entity, null, methodName);
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

            if (returnBean instanceof ProjectElement bean)
            {
                ProjectProperties projectProperties = new ProjectProperties();

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

                    projectProperties.setQualifiedName(this.removeQualifiedName(instanceProperties));
                    projectProperties.setAdditionalProperties(this.removeAdditionalProperties(instanceProperties));

                    projectProperties.setIdentifier(this.removeIdentifier(instanceProperties));
                    projectProperties.setName(this.removeName(instanceProperties));
                    projectProperties.setDescription(this.removeDescription(instanceProperties));
                    projectProperties.setStartDate(this.removeStartDate(instanceProperties));
                    projectProperties.setPlannedEndDate(this.removePlannedEndDate(instanceProperties));
                    projectProperties.setProjectStatus(this.removeProjectStatus(instanceProperties));
                    projectProperties.setProjectHealth(this.removeProjectHealth(instanceProperties));
                    projectProperties.setProjectPhase(this.removeProjectPhase(instanceProperties));
                    projectProperties.setEffectiveFrom(instanceProperties.getEffectiveFromTime());
                    projectProperties.setEffectiveTo(instanceProperties.getEffectiveToTime());

                    /*
                     * Any remaining properties are returned in the extended properties.  They are
                     * assumed to be defined in a subtype.
                     */
                    projectProperties.setTypeName(bean.getElementHeader().getType().getTypeName());
                    projectProperties.setExtendedProperties(this.getRemainingExtendedProperties(instanceProperties));

                }
                else
                {
                    handleMissingMetadataInstance(beanClass.getName(), TypeDefCategory.ENTITY_DEF, methodName);
                }

                if (relationship != null)
                {
                    bean.setRelatedElement(super.getRelatedElement(beanClass, entity, relationship, methodName));
                }

                bean.setProperties(projectProperties);
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
