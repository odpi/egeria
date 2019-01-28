/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.CommunityMemberMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CommunityMember;
import org.odpi.openmetadata.accessservices.communityprofile.properties.CommunityMembershipType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CommunityMemberConverter generates a CommunityMember bean from an CommunityMember entity
 * and a relationship to it.
 */
public class CommunityMemberConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(CommunityMemberConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    CommunityMemberConverter(EntityDetail         entity,
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
    public CommunityMember getBean()
    {
        final String methodName = "getBean";

        CommunityMember  bean = new CommunityMember();

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
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, CommunityMemberMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, CommunityMemberMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, CommunityMemberMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, CommunityMemberMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        if (relationship != null)
        {
            bean.setCommunityGUID(repositoryHelper.getEnd1EntityGUID(relationship));

            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setMembershipType(this.getMembershipTypeFromProperties(instanceProperties));
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }


    /**
     * Retrieve the ContactMethodType enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return CommunityMemberType  enum value
     */
    private CommunityMembershipType getMembershipTypeFromProperties(InstanceProperties   properties)
    {
        CommunityMembershipType   contactMethodType = CommunityMembershipType.OTHER;

        if (properties != null)
        {
            InstancePropertyValue instancePropertyValue = properties.getPropertyValue(CommunityMemberMapper.MEMBERSHIP_TYPE_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue)instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        contactMethodType = CommunityMembershipType.CONTRIBUTOR;
                        break;

                    case 1:
                        contactMethodType = CommunityMembershipType.ADMINISTRATOR;
                        break;

                    case 2:
                        contactMethodType = CommunityMembershipType.LEADER;
                        break;

                    case 3:
                        contactMethodType = CommunityMembershipType.OBSERVER;
                        break;

                    case 99:
                        contactMethodType = CommunityMembershipType.OTHER;
                        break;
                }
            }
        }

        log.debug("ContactMethodType: " + contactMethodType.getName());

        return contactMethodType;
    }
}
