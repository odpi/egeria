/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.CommunityCollectionMemberMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CommunityCollectionMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.WatchStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommunityCollectionMemberConverter generates an CommunityCollectionMember bean from an Community entity
 * and a ResourceList relationship to it.
 */
public class CommunityCollectionMemberConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(CommunityCollectionMemberConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    CommunityCollectionMemberConverter(EntityDetail         entity,
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
    public CommunityCollectionMember getBean()
    {
        final String methodName = "getBean";

        CommunityCollectionMember  bean = new CommunityCollectionMember();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, CommunityCollectionMemberMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, CommunityCollectionMemberMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, CommunityCollectionMemberMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setMission(repositoryHelper.removeStringProperty(serviceName, CommunityCollectionMemberMapper.MISSION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, CommunityCollectionMemberMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setDateAddedToCollection(relationship.getCreateTime());
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setMembershipRationale(repositoryHelper.getStringProperty(serviceName, CommunityCollectionMemberMapper.MEMBERSHIP_RATIONALE_PROPERTY_NAME, instanceProperties, methodName));

                if (instanceProperties.getPropertyValue(CommunityCollectionMemberMapper.WATCH_STATUS_PROPERTY_NAME) != null)
                {
                    if (repositoryHelper.getBooleanProperty(serviceName, CommunityCollectionMemberMapper.WATCH_STATUS_PROPERTY_NAME, instanceProperties, methodName))
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
