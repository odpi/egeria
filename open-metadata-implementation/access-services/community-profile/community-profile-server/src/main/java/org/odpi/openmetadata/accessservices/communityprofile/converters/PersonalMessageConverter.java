/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalMessageMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalMessage;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PersonalMessageConverter generates an PersonalMessage bean from an PersonalMessage entity and its attachment to a Referenceable.
 */
public class PersonalMessageConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(PersonalMessageConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    PersonalMessageConverter(EntityDetail         entity,
                             Relationship         relationship,
                             OMRSRepositoryHelper repositoryHelper,
                             String               componentName)
    {
        super(entity, relationship, repositoryHelper, componentName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public PersonalMessage getBean()
    {
        final String methodName = "getBean";

        PersonalMessage  bean = new PersonalMessage();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * user identity properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, PersonalMessageMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setText(repositoryHelper.removeStringProperty(serviceName, PersonalMessageMapper.TEXT_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, PersonalMessageMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setUserId(relationship.getCreatedBy());
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
