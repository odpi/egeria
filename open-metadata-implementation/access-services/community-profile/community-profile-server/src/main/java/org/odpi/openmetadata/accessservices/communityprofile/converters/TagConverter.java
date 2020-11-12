/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.TagMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Tag;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TagConverter generates an Tag bean from an InformalTag/Private entity and its attachment to a Referenceable.
 */
public class TagConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(TagConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     */
    public TagConverter(EntityDetail         entity,
                        Relationship         relationship,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public Tag getBean()
    {
        final String methodName = "getBean";

        Tag  bean = new Tag();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setName(repositoryHelper.removeStringProperty(serviceName, TagMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, TagMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setIsPrivate(TagMapper.IS_PRIVATE_TYPE_NAME.equals(bean.getOriginType()));
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
