/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.ContactMethodMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethod;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethodType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ContactMethodConverter generates a ContactMethod bean from a ContactMethod entity.
 */
public class ContactMethodConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(ContactMethod.class);

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    ContactMethodConverter(EntityDetail         entity,
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
    public ContactMethod getBean()
    {
        final String methodName = "getBean";

        ContactMethod  bean = new ContactMethod();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setType(this.getContactMethodTypeFromProperties(instanceProperties));
                bean.setService(repositoryHelper.getStringProperty(serviceName, ContactMethodMapper.SERVICE_PROPERTY_NAME, instanceProperties, methodName));
                bean.setValue(repositoryHelper.getStringProperty(serviceName, ContactMethodMapper.VALUE_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return ContactMethodType  enum value
     */
    private ContactMethodType getContactMethodTypeFromProperties(InstanceProperties   properties)
    {
        ContactMethodType   contactMethodType = ContactMethodType.OTHER;

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(ContactMethodMapper.TYPE_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        contactMethodType = ContactMethodType.EMAIL;
                        break;

                    case 1:
                        contactMethodType = ContactMethodType.PHONE;
                        break;

                    case 2:
                        contactMethodType = ContactMethodType.CHAT;
                        break;

                    case 3:
                        contactMethodType = ContactMethodType.PROFILE;
                        break;

                    case 4:
                        contactMethodType = ContactMethodType.ACCOUNT;
                        break;

                    case 99:
                        contactMethodType = ContactMethodType.OTHER;
                        break;
                }
            }
        }

        log.debug("ContactMethodType: " + contactMethodType.getName());

        return contactMethodType;
    }
}
