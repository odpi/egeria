/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonRoleMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonRole;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * PersonRoleConverter generates a PersonRole bean from an PersonRole entity and the relationships connected to it.
 */
public class PersonRoleConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(PersonRoleConverter.class);

    private List<Relationship> personRoleRelationships;

    /**
     * Constructor captures the initial content with relationships
     *
     * @param entity properties to convert
     * @param relationships properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    PersonRoleConverter(EntityDetail         entity,
                        List<Relationship>   relationships,
                        OMRSRepositoryHelper repositoryHelper,
                        String               componentName)
    {
        super(entity, repositoryHelper, componentName);
        this.personRoleRelationships = relationships;
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public PersonRole getBean()
    {
        final String methodName = "getBean";

        PersonRole  bean = new PersonRole();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * role properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, PersonRoleMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, PersonRoleMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, PersonRoleMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));

                if (instanceProperties.getPropertyValue(PersonRoleMapper.HEAD_COUNT_PROPERTY_NAME) != null)
                {
                    bean.setHeadCountLimitSet(true);
                    bean.setHeadCount(repositoryHelper.removeIntProperty(serviceName, PersonRoleMapper.HEAD_COUNT_PROPERTY_NAME, instanceProperties, methodName));
                }

                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, PersonRoleMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                bean.setClassifications(super.getClassificationsFromEntity());
            }
        }

        if (personRoleRelationships != null)
        {
            int  appointeeCount = 0;

            for (Relationship relationship : personRoleRelationships)
            {
                if (relationship != null)
                {
                    InstanceType instanceType = relationship.getType();

                    if (instanceType != null)
                    {
                        if (PersonRoleMapper.PERSON_ROLE_APPOINTMENT_TYPE_NAME.equals(instanceType.getTypeDefName()))
                        {
                            appointeeCount ++;
                        }
                    }
                }
            }

            bean.setAppointmentCount(appointeeCount);
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
