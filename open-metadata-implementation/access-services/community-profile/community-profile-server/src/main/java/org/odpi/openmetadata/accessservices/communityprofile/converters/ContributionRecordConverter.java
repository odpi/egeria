/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.mappers.ContributionRecordMapper;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ContributionRecord;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * PersonalProfileConverter generates a PersonalProfile bean from a PersonalProfile entity.
 */
public class ContributionRecordConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(ContributionRecordConverter.class);


    /**
     * Constructor captures the initial content
     *
     * @param contributionEntity properties to convert
     * @param repositoryHelper helper object to parse entities
     * @param serviceName name of this component
     */
    public ContributionRecordConverter(EntityDetail         contributionEntity,
                                       OMRSRepositoryHelper repositoryHelper,
                                       String               serviceName)
    {
        super(contributionEntity, repositoryHelper, serviceName);
    }


    /**
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public ContributionRecord getBean()
    {
        final String methodName = "getBean";

        ContributionRecord  bean = new ContributionRecord();

        super.updateBean(bean);

        if (entity != null)
        {
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName, ContributionRecordMapper.QUALIFIED_NAME_PROPERTY_NAME, instanceProperties, methodName));
                bean.setKarmaPoints(repositoryHelper.removeIntProperty(serviceName, ContributionRecordMapper.KARMA_POINTS_PROPERTY_NAME, instanceProperties, methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName, ContributionRecordMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, instanceProperties, methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
                bean.setClassifications(super.getClassificationsFromEntity());
            }
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
