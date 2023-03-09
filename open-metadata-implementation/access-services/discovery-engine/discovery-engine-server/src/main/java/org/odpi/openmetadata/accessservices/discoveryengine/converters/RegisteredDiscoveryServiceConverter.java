/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * RegisteredDiscoveryServiceConverter transfers the relevant properties from a DiscoveryServiceProperties bean
 * and the Open Metadata Repository Services (OMRS) Relationship object into a RegisteredDiscoveryService bean.
 */
public class RegisteredDiscoveryServiceConverter
{
    private final OMRSRepositoryHelper       repositoryHelper;
    private final String                     serviceName;

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
     * @param relationships list of relationships with the request types
     * @return output bean
     */
    public RegisteredDiscoveryService getBean(DiscoveryServiceProperties discoveryServiceProperties,
                                              List<Relationship>         relationships)
    {
        final String  methodName = "getBean";

        RegisteredDiscoveryService  bean = new RegisteredDiscoveryService(discoveryServiceProperties);

        if (relationships != null)
        {
            Map<String, Map<String, String>> requestTypeMappings = new HashMap<>();

            for (Relationship  relationship : relationships)
            {
                if (relationship != null)
                {
                    /*
                     * The properties are removed from the instance properties and stowed in the bean.
                     * Any remaining properties are stored in extendedProperties.
                     */
                    InstanceProperties instanceProperties = relationship.getProperties();

                    if (instanceProperties != null)
                    {
                        String requestType = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataAPIMapper.REQUEST_TYPE_PROPERTY_NAME,
                                                                                instanceProperties,
                                                                                methodName);
                        Map<String, String> analysisParameters = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                                           OpenMetadataAPIMapper.REQUEST_PARAMETERS_PROPERTY_NAME,
                                                                                                           instanceProperties,
                                                                                                           methodName);

                        if (requestType != null)
                        {
                            requestTypeMappings.put(requestType, analysisParameters);
                        }
                    }
                }
            }

            if (!requestTypeMappings.isEmpty())
            {
                bean.setDiscoveryRequestTypes(requestTypeMappings);
            }
        }

        return bean;
    }
}
