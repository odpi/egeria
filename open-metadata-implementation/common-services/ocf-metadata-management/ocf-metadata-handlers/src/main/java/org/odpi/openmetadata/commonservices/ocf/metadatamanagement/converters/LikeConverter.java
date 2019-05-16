/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.LikeMapper;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Like;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * LikeConverter transfers the relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a Like bean.
 */
public class LikeConverter extends ElementHeaderConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public LikeConverter(EntityDetail         entity,
                         Relationship         relationship,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Request the bean is extracted from the repository entity.
     *
     * @return output bean
     */
    public Like getBean()
    {
        final String  methodName = "getBean";

        Like  bean = null;

        if ((relationship != null) && (entity != null))
        {
            bean = new Like();

            super.updateBean(bean);

            bean.setUser(relationship.getCreatedBy());

            InstanceProperties instanceProperties = relationship.getProperties();

            if (instanceProperties != null)
            {
                bean.setPublic(repositoryHelper.getBooleanProperty(serviceName,
                                                                   LikeMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                   instanceProperties,
                                                                   methodName));
            }

            /*
             * The properties are removed from the instance properties and stowed in the bean.
             * Any remaining properties are stored in extendedProperties.
             */
            instanceProperties = entity.getProperties();

            if (instanceProperties != null)
            {
                bean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(instanceProperties));
            }
        }

        return bean;
    }
}
