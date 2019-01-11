/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.TeamMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.Team;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * TeamConverter generates a Team bean from an Team entity and the relationships connected to it.
 */
public class TeamConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(TeamConverter.class);

    private List<Relationship> teamRelationships;

    /**
     * Constructor captures the initial content with relationships
     *
     * @param entity properties to convert
     * @param relationships properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    TeamConverter(EntityDetail         entity,
                  List<Relationship>   relationships,
                  OMRSRepositoryHelper repositoryHelper,
                  String               componentName)
    {
        super(entity, repositoryHelper, componentName);
        this.teamRelationships = relationships;
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public Team getBean()
    {
        final String methodName = "getBean";

        Team  bean = new Team();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, TeamMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, TeamMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, TeamMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setTeamType(repositoryHelper.removeStringProperty(serviceName, TeamMapper.TEAM_TYPE_PROPERTY_NAME, instanceProperties, methodName));

                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, TeamMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                bean.setClassifications(super.getClassificationsFromEntity());
            }
        }

        if (teamRelationships != null)
        {
            List<String>  subTeamList = new ArrayList<>();
            String        myGUID = entity.getGUID();

            if (myGUID != null)
            {
                for (Relationship relationship : teamRelationships)
                {
                    if (relationship != null)
                    {
                        InstanceType instanceType = relationship.getType();

                        if (instanceType != null)
                        {
                            if (TeamMapper.TEAM_STRUCTURE_TYPE_NAME.equals(instanceType.getTypeDefName()))
                            {
                                String superTeamGUID = repositoryHelper.getEnd1EntityGUID(relationship);
                                String subTeamGUID   = repositoryHelper.getEnd2EntityGUID(relationship);

                                if (myGUID.equals(superTeamGUID))
                                {
                                    subTeamList.add(subTeamGUID);
                                }
                                else if (myGUID.equals(subTeamGUID))
                                {
                                    bean.setSuperTeam(superTeamGUID);
                                }

                            }
                        }
                    }
                }
            }

            if (! subTeamList.isEmpty())
            {
                bean.setSubTeams(subTeamList);
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
