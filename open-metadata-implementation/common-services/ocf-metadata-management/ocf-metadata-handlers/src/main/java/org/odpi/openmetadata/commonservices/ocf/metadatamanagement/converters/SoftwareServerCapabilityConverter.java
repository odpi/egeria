/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.SoftwareServerCapabilityMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SoftwareServerCapability;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * SoftwareServerCapabilityConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a SoftwareServerCapabilities bean.
 */
public class SoftwareServerCapabilityConverter extends ReferenceableConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public SoftwareServerCapabilityConverter(EntityDetail entity,
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
    public SoftwareServerCapability getBean()
    {
        SoftwareServerCapability bean = null;

        if (entity != null)
        {
            bean = updateBean(new SoftwareServerCapability());
        }

        return bean;
    }


    /**
     * Request the bean is extracted from the repository objects
     *
     * @param bean bean to update
     * @return updated bean
     */
    protected SoftwareServerCapability updateBean(SoftwareServerCapability bean)
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
                                                                          SoftwareServerCapabilityMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          SoftwareServerCapabilityMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setTypeDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                              SoftwareServerCapabilityMapper.TYPE_PROPERTY_NAME,
                                                                              instanceProperties,
                                                                              methodName));
                bean.setVersion(repositoryHelper.removeStringProperty(serviceName,
                                                                      SoftwareServerCapabilityMapper.VERSION_PROPERTY_NAME,
                                                                      instanceProperties,
                                                                      methodName));
                bean.setPatchLevel(repositoryHelper.removeStringProperty(serviceName,
                                                                         SoftwareServerCapabilityMapper.PATCH_LEVEL_PROPERTY_NAME,
                                                                         instanceProperties,
                                                                         methodName));
                bean.setSource(repositoryHelper.removeStringProperty(serviceName,
                                                                     SoftwareServerCapabilityMapper.SOURCE_PROPERTY_NAME,
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
