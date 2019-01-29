/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.converters;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Like;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LikeConverter generates an Like bean from an Like entity and its attachment to a Referenceable.
 */
public class LikeConverter extends ConnectedAssetElementConverter
{
    private static final Logger log = LoggerFactory.getLogger(LikeConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
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
     * Return the bean constructed from the repository content.
     *
     * @return bean
     */
    public Like getBean()
    {
        final String methodName = "getBean";

        Like  bean = new Like();

        super.updateBean(bean);

        if (relationship != null)
        {
            bean.setUser(relationship.getCreatedBy());
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
