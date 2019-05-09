/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.AssetConverter;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * DiscoveryServicePropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DiscoveryServiceProperties bean.
 */
public class DiscoveryServicePropertiesConverter extends AssetConverter
{
    /**
     * Constructor captures the initial content with connectionToAssetRelationship
     *
     * @param discoveryServiceEntity properties to convert
     * @param connectionToAssetRelationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship objects
     * @param serviceName name of this component
     */
    public DiscoveryServicePropertiesConverter(EntityDetail         discoveryServiceEntity,
                                               Relationship         connectionToAssetRelationship,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName)
    {
        super(discoveryServiceEntity,
              connectionToAssetRelationship,
              repositoryHelper,
              serviceName);
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public DiscoveryServiceProperties getDiscoveryServiceBean()
    {
        DiscoveryServiceProperties  bean = null;

        if (entity != null)
        {
            bean = new DiscoveryServiceProperties();

            updateBean(bean);
        }

        return bean;
    }
}
