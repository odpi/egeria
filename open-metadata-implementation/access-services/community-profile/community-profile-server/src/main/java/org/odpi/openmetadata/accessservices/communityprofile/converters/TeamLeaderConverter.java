/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.accessservices.communityprofile.mappers.TeamLeaderMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.TeamLeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TeamLeaderConverter generates a TeamLeader bean from an TeamLeader entity
 * and a relationship to it.
 */
public class TeamLeaderConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(TeamLeaderConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    TeamLeaderConverter(EntityDetail         entity,
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
    public TeamLeader getBean()
    {
        final String methodName = "getBean";

        TeamLeader  bean = new TeamLeader();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, TeamLeaderMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, TeamLeaderMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, TeamLeaderMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, TeamLeaderMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setTeamGUID(repositoryHelper.getEnd2EntityGUID(relationship));

            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setLeadershipPosition(repositoryHelper.getStringProperty(serviceName, TeamLeaderMapper.LEADERSHIP_POSITION_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
