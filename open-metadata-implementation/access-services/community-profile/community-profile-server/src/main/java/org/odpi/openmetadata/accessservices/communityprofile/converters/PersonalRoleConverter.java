/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalRoleMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalRole;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PersonalRoleConverter generates a PersonalRole from an PersonRole entity
 */
public class PersonalRoleConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(PersonalRoleConverter.class);

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    PersonalRoleConverter(EntityDetail         entity,
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
    public PersonalRole getBean()
    {
        final String methodName = "getBean";

        PersonalRole  bean = new PersonalRole();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, PersonalRoleMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, PersonalRoleMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, PersonalRoleMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, PersonalRoleMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                bean.setClassifications(super.getClassificationsFromEntity());
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
