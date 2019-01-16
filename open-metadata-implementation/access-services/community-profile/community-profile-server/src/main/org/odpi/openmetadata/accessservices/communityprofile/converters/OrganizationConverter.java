/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.converters;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;

/**
 * OrganizationConverter is a TeamConverter in disguise.
 */
public class OrganizationConverter extends TeamConverter
{
    /**
     * Constructor captures the initial content with relationships
     *
     * @param entity properties to convert
     * @param relationships properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param componentName name of this component
     */
    OrganizationConverter(EntityDetail         entity,
                          List<Relationship>   relationships,
                          OMRSRepositoryHelper repositoryHelper,
                          String               componentName)
    {
        super(entity, relationships, repositoryHelper, componentName);
    }
}
