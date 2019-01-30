/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.PersonalProfileMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContactMethod;
import org.odpi.openmetadata.accessservices.communityprofile.properties.PersonalProfile;
import org.odpi.openmetadata.accessservices.communityprofile.properties.UserIdentity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * PersonalProfileConverter generates an PersonalProfile bean from an PersonalProfile entity.
 */
public class PersonalProfileConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(PersonalProfileConverter.class);

    private EntityDetail         contributionEntity;
    private List<UserIdentity>   associatedUserIds;
    private List<ContactMethod>  contactDetails;

    /**
     * Constructor captures the initial content
     *
     * @param personEntity properties to convert
     * @param contributionEntity properties to convert
     * @param repositoryHelper helper object to parse entities
     * @param componentName name of this component
     */
    PersonalProfileConverter(EntityDetail         personEntity,
                             EntityDetail         contributionEntity,
                             List<UserIdentity>   associatedUserIds,
                             List<ContactMethod>  contactDetails,
                             OMRSRepositoryHelper repositoryHelper,
                             String               componentName)
    {
        super(personEntity, repositoryHelper, componentName);

        this.contributionEntity = contributionEntity;
        this.associatedUserIds = associatedUserIds;
        this.contactDetails = contactDetails;
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public PersonalProfile getBean()
    {
        final String methodName = "getBean";

        PersonalProfile  bean = new PersonalProfile();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                /*
                 * As properties are retrieved, they are removed from the instance properties object so that what is left going into
                 * community properties.
                 */
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setName(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.DESCRIPTION_PROPERTY_NAME, instanceProperties, methodName));
                bean.setFullName(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.FULL_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setJobTitle(repositoryHelper.removeStringProperty(serviceName, PersonalProfileMapper.JOB_TITLE_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, PersonalProfileMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                bean.setClassifications(super.getClassificationsFromEntity());
            }
        }

        if (contributionEntity != null)
        {
            InstanceProperties instanceProperties = contributionEntity.getProperties();

            if (instanceProperties != null)
            {
                bean.setKarmaPoints(repositoryHelper.getIntProperty(serviceName, PersonalProfileMapper.KARMA_POINTS_PROPERTY_NAME, instanceProperties, methodName));
            }
        }

        bean.setAssociatedUserIds(associatedUserIds);
        bean.setContactDetails(contactDetails);

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
