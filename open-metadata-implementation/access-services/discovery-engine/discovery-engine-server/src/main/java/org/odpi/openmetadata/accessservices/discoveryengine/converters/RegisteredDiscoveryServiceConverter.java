/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * RegisteredDiscoveryServiceConverter transfers the relevant properties from a DiscoveryServiceProperties bean
 * and the Open Metadata Repository Services (OMRS) Relationship object into a RegisteredDiscoveryService bean.
 */
public class RegisteredDiscoveryServiceConverter
{
    private OMRSRepositoryHelper       repositoryHelper;
    private String                     serviceName;

    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public RegisteredDiscoveryServiceConverter(OMRSRepositoryHelper       repositoryHelper,
                                               String                     serviceName)
    {
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
    }



    /**
     * Request the bean is extracted from the repository entity.
     *
     * @param discoveryServiceProperties properties to convert
     * @param relationship relationship for asset types
     * @return output bean
     */
    public RegisteredDiscoveryService getBean(DiscoveryServiceProperties discoveryServiceProperties,
                                              Relationship               relationship)
    {
        final String  methodName = "getBean";

        RegisteredDiscoveryService  bean = new RegisteredDiscoveryService(discoveryServiceProperties);

        if (relationship != null)
        {
            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setDiscoveryRequestTypes(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                      OpenMetadataAPIMapper.DISCOVERY_REQUEST_TYPES_PROPERTY_NAME,
                                                                                      instanceProperties,
                                                                                      methodName));
                bean.setDefaultAnalysisParameters(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                            OpenMetadataAPIMapper.DEFAULT_ANALYSIS_PARAMETERS_PROPERTY_NAME,
                                                                                            instanceProperties,
                                                                                            methodName));
            }
        }

        return bean;
    }
}
