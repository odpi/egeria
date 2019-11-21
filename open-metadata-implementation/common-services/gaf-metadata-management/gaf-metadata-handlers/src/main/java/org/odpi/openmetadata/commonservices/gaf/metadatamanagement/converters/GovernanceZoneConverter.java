/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.gaf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.mappers.GovernanceZoneMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceZone;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * GovernanceZoneConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a GovernanceZone bean.
 */
public class GovernanceZoneConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param zoneEntity properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public GovernanceZoneConverter(EntityDetail         zoneEntity,
                                   OMRSRepositoryHelper repositoryHelper,
                                   String               serviceName)
    {
        super(zoneEntity, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public GovernanceZone getZoneBean()
    {
        GovernanceZone  bean = null;

        if (entity != null)
        {
            bean = new GovernanceZone();

            updateBean(bean);
        }

        return bean;
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @param bean output bean
     */
    private void updateBean(GovernanceZone bean)
    {
        final String  methodName = "updateBean";

        if (entity != null)
        {
            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                            ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                            instanceProperties,
                                                                            methodName));
                bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                          GovernanceZoneMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          GovernanceZoneMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setCriteria(repositoryHelper.removeStringProperty(serviceName,
                                                                       GovernanceZoneMapper.CRITERIA_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));

                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));            }
        }
    }
}
