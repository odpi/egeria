/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;


import org.odpi.openmetadata.accessservices.communityprofile.properties.Like;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LikeConverter generates an Like bean from an Like entity and its attachment to a Referenceable.
 */
public class LikeConverter extends CommonHeaderConverter
{
    private static final Logger log = LoggerFactory.getLogger(LikeConverter.class);

    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    LikeConverter(EntityDetail         entity,
                  Relationship         relationship,
                  OMRSRepositoryHelper repositoryHelper,
                  String               componentName)
    {
        super(entity, relationship, repositoryHelper, componentName);
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
            bean.setUserId(relationship.getCreatedBy());
        }

        log.debug("Bean: " + bean.toString());

        return bean;
    }
}
