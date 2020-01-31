/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.DiscoveryEnginePropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.ReferenceableConverter;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * RegisteredDiscoveryServiceConverter transfers the relevant properties from a DiscoveryServiceProperties bean
 * and the Open Metadata Repository Services (OMRS) Relationship object into a RegisteredDiscoveryService bean.
 */
public class RegisteredDiscoveryServiceConverter extends ReferenceableConverter
{
    private DiscoveryServiceProperties discoveryServiceProperties;

    /**
     * Constructor captures the repository content needed to create the endpoint object.
     *
     * @param discoveryServiceProperties properties to convert
     * @param relationship relationship for asset types
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public RegisteredDiscoveryServiceConverter(DiscoveryServiceProperties discoveryServiceProperties,
                                               Relationship               relationship,
                                               OMRSRepositoryHelper       repositoryHelper,
                                               String                     serviceName)
    {
        super(null,
              relationship,
              repositoryHelper,
              serviceName);

        this.discoveryServiceProperties = discoveryServiceProperties;
    }



    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public RegisteredDiscoveryService getBean()
    {
        final String  methodName = "getBean";

        RegisteredDiscoveryService  bean = new RegisteredDiscoveryService(discoveryServiceProperties);

        if (relationship != null)
        {
            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setDiscoveryRequestTypes(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                      DiscoveryEnginePropertiesMapper.DISCOVERY_REQUEST_TYPES_PROPERTY_NAME,
                                                                                      instanceProperties,
                                                                                      methodName));
                bean.setDefaultAnalysisParameters(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                            DiscoveryEnginePropertiesMapper.DEFAULT_ANALYSIS_PARAMETERS_PROPERTY_NAME,
                                                                                            instanceProperties,
                                                                                            methodName));
            }
        }

        return bean;
    }
}
