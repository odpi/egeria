/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalNoteMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalNote;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PersonalNoteConverter generates a PersonalNote from an NoteEntry entity.
 */
public class PersonalNoteConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(PersonalNoteConverter.class);

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param componentName name of this component
     */
    PersonalNoteConverter(EntityDetail         entity,
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
    public PersonalNote getBean()
    {
        final String methodName = "getBean";

        PersonalNote  bean = new PersonalNote();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, PersonalNoteMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setTitle(repositoryHelper.removeStringProperty(serviceName, PersonalNoteMapper.TITLE_PROPERTY_NAME, instanceProperties, methodName));
                bean.setText(repositoryHelper.removeStringProperty(serviceName, PersonalNoteMapper.TEXT_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
