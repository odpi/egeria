/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.ProjectCollectionMemberMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ProjectCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.OwnerType;
import org.odpi.openmetadata.accessservices.communityprofile.properties.WatchStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * ProjectCollectionMemberConverter generates an ProjectCollectionMember bean from an Project entity
 * and a ResourceList relationship to it.
 */
public class ProjectCollectionMemberConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(ProjectCollectionMemberConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    ProjectCollectionMemberConverter(EntityDetail         entity,
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
    public ProjectCollectionMember getBean()
    {
        final String methodName = "getBean";

        ProjectCollectionMember  bean = new ProjectCollectionMember();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * resource properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, ProjectCollectionMemberMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, ProjectCollectionMemberMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, ProjectCollectionMemberMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setStartDate(repositoryHelper.removeDateProperty(serviceName, ProjectCollectionMemberMapper.START_DATE_PROPERTY_NAME, instanceProperties, methodName));
                bean.setPlannedEndDate(repositoryHelper.removeDateProperty(serviceName, ProjectCollectionMemberMapper.PLANNED_END_DATE_PROPERTY_NAME, instanceProperties, methodName));
                bean.setStatus(repositoryHelper.removeStringProperty(serviceName, ProjectCollectionMemberMapper.STATUS_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, ProjectCollectionMemberMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setDateAddedToCollection(relationship.getCreateTime());
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setMembershipRationale(repositoryHelper.getStringProperty(serviceName, ProjectCollectionMemberMapper.MEMBERSHIP_RATIONALE_PROPERTY_NAME, instanceProperties, methodName));

                if (instanceProperties.getPropertyValue(ProjectCollectionMemberMapper.WATCH_STATUS_PROPERTY_NAME) != null)
                {
                    if (repositoryHelper.getBooleanProperty(serviceName, ProjectCollectionMemberMapper.WATCH_STATUS_PROPERTY_NAME, instanceProperties, methodName))
                    {
                        bean.setWatchStatus(WatchStatus.WATCHED);
                    }
                    else
                    {
                        bean.setWatchStatus(WatchStatus.NOT_WATCHED);
                    }
                }
                else
                {
                    bean.setWatchStatus(WatchStatus.USE_DEFAULT);
                }
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
