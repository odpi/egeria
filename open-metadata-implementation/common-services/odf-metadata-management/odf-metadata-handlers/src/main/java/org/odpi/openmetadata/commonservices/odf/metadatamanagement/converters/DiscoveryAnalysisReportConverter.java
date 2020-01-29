/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryAnalysisReportMapper;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryAnalysisReport;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryRequestStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * LicenseConverter transfers the relevant properties from some Open Metadata Repository Services (OMRS)
 * EntityDetail object into an License bean.
 */
public class DiscoveryAnalysisReportConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param mainEntity properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public DiscoveryAnalysisReportConverter(EntityDetail             mainEntity,
                                            OMRSRepositoryHelper     repositoryHelper,
                                            String                   serviceName)
    {
        super(mainEntity, repositoryHelper, serviceName);
    }

    
    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public DiscoveryAnalysisReport getBean()
    {
        final String  methodName = "getBean";

        DiscoveryAnalysisReport  bean = new DiscoveryAnalysisReport();

        super.updateBean(bean);

        InstanceProperties instanceProperties = entity.getProperties();

        if (instanceProperties != null)
        {
            bean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                      DiscoveryAnalysisReportMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
            bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                      DiscoveryAnalysisReportMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
            bean.setCreationDate(repositoryHelper.removeDateProperty(serviceName,
                                                                     DiscoveryAnalysisReportMapper.CREATION_DATE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
            bean.setAnalysisParameters(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                    DiscoveryAnalysisReportMapper.ANALYSIS_PARAMS_PROPERTY_NAME,
                                                                                    instanceProperties,
                                                                                    methodName));
            bean.setAnalysisStep(repositoryHelper.removeStringProperty(serviceName,
                                                                       DiscoveryAnalysisReportMapper.ANALYSIS_STEP_PROPERTY_NAME,
                                                                       instanceProperties,
                                                                       methodName));
            bean.setDiscoveryRequestStatus(this.removeRequestStatusFromProperties(instanceProperties));
            bean.setAssetGUID(repositoryHelper.removeStringProperty(serviceName,
                                                                    DiscoveryAnalysisReportMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                    instanceProperties,
                                                                    methodName));
            bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
        }

        return bean;
    }


    /**
     * Retrieve the StarRating enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return   enum value
     */
    private DiscoveryRequestStatus removeRequestStatusFromProperties(InstanceProperties   properties)
    {
        DiscoveryRequestStatus requestStatus = DiscoveryRequestStatus.UNKNOWN_STATUS;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(DiscoveryAnalysisReportMapper.DISCOVERY_REQUEST_STATUS_PROPERTY_NAME);

            if (instancePropertyValue instanceof EnumPropertyValue)
            {
                EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;

                switch (enumPropertyValue.getOrdinal())
                {
                    case 0:
                        requestStatus = DiscoveryRequestStatus.WAITING;
                        break;

                    case 1:
                        requestStatus = DiscoveryRequestStatus.IN_PROGRESS;
                        break;

                    case 2:
                        requestStatus = DiscoveryRequestStatus.FAILED;
                        break;

                    case 3:
                        requestStatus = DiscoveryRequestStatus.COMPLETED;
                        break;

                    default:
                        requestStatus = DiscoveryRequestStatus.ACTIVATING;
                        break;
                }

                instancePropertiesMap.remove(DiscoveryAnalysisReportMapper.DISCOVERY_REQUEST_STATUS_PROPERTY_NAME);

                properties.setInstanceProperties(instancePropertiesMap);
            }
        }

        return requestStatus;
    }
}
