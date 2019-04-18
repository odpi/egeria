/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.converters;

import org.odpi.openmetadata.accessservices.discoveryengine.mappers.DiscoveryEnginePropertiesMapper;
import org.odpi.openmetadata.accessservices.discoveryengine.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * DiscoveryEnginePropertiesConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a DiscoveryEngineProperties bean.
 */
public class DiscoveryEnginePropertiesConverter extends ReferenceableConverter
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
        final String  methodName = "getBean";

        DiscoveryEngineProperties  bean = null;

        if (entity != null)
        {
            bean = new DiscoveryEngineProperties();

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
                                                                          DiscoveryEnginePropertiesMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          DiscoveryEnginePropertiesMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setTypeDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                              DiscoveryEnginePropertiesMapper.TYPE_DESCRIPTION_PROPERTY_NAME,
                                                                              instanceProperties,
                                                                              methodName));
                bean.setVersion(repositoryHelper.removeStringProperty(serviceName,
                                                                      DiscoveryEnginePropertiesMapper.VERSION_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
                bean.setPatchLevel(repositoryHelper.removeStringProperty(serviceName,
                                                                         DiscoveryEnginePropertiesMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setSource(repositoryHelper.removeStringProperty(serviceName,
                                                                     DiscoveryEnginePropertiesMapper.SOURCE_PROPERTY_NAME,
                                                                     instanceProperties,
                                                                     methodName));
                bean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                          ReferenceableMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                          instanceProperties,
                                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }
}
