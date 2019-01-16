/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.TeamMemberMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TeamMember;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TeamMemberConverter generates a TeamMember bean from an TeamMember entity
 * and a relationship to it.
 */
public class TeamMemberConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(TeamMemberConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    TeamMemberConverter(EntityDetail         entity,
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
    public TeamMember getBean()
    {
        final String methodName = "getBean";

        TeamMember  bean = new TeamMember();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, TeamMemberMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, TeamMemberMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, TeamMemberMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, TeamMemberMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setTeamGUID(repositoryHelper.getEnd2EntityGUID(relationship));

            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setMembershipPosition(repositoryHelper.getStringProperty(serviceName, TeamMemberMapper.MEMBERSHIP_POSITION_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
