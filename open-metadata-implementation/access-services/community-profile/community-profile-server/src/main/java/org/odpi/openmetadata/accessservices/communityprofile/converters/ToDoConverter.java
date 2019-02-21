/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.ToDoMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDo;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ToDoStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * ToDoConverter generates a To Do bean from an To Do entity.
 */
public class ToDoConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(ToDoConverter.class);

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    ToDoConverter(EntityDetail         entity,
                  OMRSRepositoryHelper repositoryHelper,
                  String               componentName)
    {
        super(entity, repositoryHelper, componentName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public ToDo getBean()
    {
        final String methodName = "getBean";

        ToDo  bean = new ToDo();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * to do properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, ToDoMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, ToDoMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, ToDoMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setCompletionTime(repositoryHelper.removeDateProperty(serviceName, ToDoMapper.CREATION_TIME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setPriority(repositoryHelper.removeIntProperty(serviceName, ToDoMapper.PRIORITY_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDueTime(repositoryHelper.removeDateProperty(serviceName, ToDoMapper.DUE_TIME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setCompletionTime(repositoryHelper.removeDateProperty(serviceName, ToDoMapper.COMPLETION_TIME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setStatus(this.getToDoStatusFromProperties(instanceProperties));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, ToDoMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                bean.setClassifications(super.getClassificationsFromEntity());
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }


    /**
     * Retrieve the ToDoStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ToDoStatus  enum value
     */
    private ToDoStatus getToDoStatusFromProperties(InstanceProperties   properties)
    {
        ToDoStatus   toDoStatus = ToDoStatus.ABANDONED;

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(ToDoMapper.STATUS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
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

            Map<String, InstancePropertyValue>   instancePropertyValueMap = properties.getInstanceProperties();
            instancePropertyValueMap.remove(ToDoMapper.STATUS_PROPERTY_NAME);
            properties.setInstanceProperties(instancePropertyValueMap);
        }

        log.debug("ToDoStatus: " + toDoStatus.getName());

        return toDoStatus;
    }
}
