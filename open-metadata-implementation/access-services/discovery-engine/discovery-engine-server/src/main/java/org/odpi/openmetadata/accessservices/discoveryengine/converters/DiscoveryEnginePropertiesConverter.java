/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters.SoftwareServerCapabilityConverter;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * DiscoveryEnginePropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DiscoveryEngineProperties bean.
 */
public class DiscoveryEnginePropertiesConverter extends SoftwareServerCapabilityConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public DiscoveryEnginePropertiesConverter(EntityDetail entity,
                                              OMRSRepositoryHelper repositoryHelper,
                                              String               serviceName)
    {
        super(entity, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @return output bean
     */
    public DiscoveryEngineProperties getBean()
    {
        DiscoveryEngineProperties  bean = null;

        if (entity != null)
        {
            bean = new DiscoveryEngineProperties();

            super.updateBean(bean);
        }

        return bean;
    }
}
