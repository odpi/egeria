/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.MeaningMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * MeaningConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Meaning bean.
 */
public class MeaningConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public MeaningConverter(EntityDetail         entity,
                            OMRSRepositoryHelper repositoryHelper,
                            String               serviceName)
    {
        super(entity, repositoryHelper, serviceName);
    }



    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public Meaning getBean()
    {
        final String  methodName = "getBean";

        Meaning  bean = null;

        if (entity != null)
        {
            bean = new Meaning();

            super.updateBean(bean);

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            InstanceProperties instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setName(repositoryHelper.removeStringProperty(serviceName,
                                                                   MeaningMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));
                bean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                          MeaningMapper.DESCRIPTION_PROPERTY_NAME,
                                                                          instanceProperties,
                                                                          methodName));
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }

}
