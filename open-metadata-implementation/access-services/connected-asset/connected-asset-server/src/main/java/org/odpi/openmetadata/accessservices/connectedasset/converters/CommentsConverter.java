/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.converters;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * CommentsConverter builds an Comment bean from an entity retrieved from the open metadata repositories.
 */
public class CommentsConverter
{
    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     */
    public CommentsConverter(EntityDetail         entity,
                             Relationship         relationship,
                             OMRSRepositoryHelper repositoryHelper,
                             String               serviceName)
    {
    }
}
