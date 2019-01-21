/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.properties.CommonHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * CommonHeaderConverter is responsible for extracting the bean properties for the CommonHeader bean
 */
public class CommonHeaderConverter extends CommunityProfileElementConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    CommonHeaderConverter(EntityDetail         entity,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName)
    {
        super(entity, repositoryHelper, serviceName);
    }


    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     */
    CommonHeaderConverter(EntityDetail         entity,
                          Relationship         relationship,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName)
    {
        super(entity, relationship, repositoryHelper, serviceName);
    }


    /**
     * Extract the properties from the entity.
     */
    void updateBean(CommonHeader  bean)
    {
        if (super.entity != null)
        {
            super.updateBean(bean);
            bean.setGUID(entity.getGUID());
        }
    }
}
