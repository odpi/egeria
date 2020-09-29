/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.converters;

import org.odpi.openmetadata.accessservices.assetconsumer.elements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.LikeProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.RatingProperties;
import org.odpi.openmetadata.accessservices.assetconsumer.properties.StarRating;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;


/**
 * LikeConverter provides common methods for transferring relevant properties from an Open Metadata Repository Services (OMRS)
 * EntityDetail object into a bean that inherits from RatingProperties.
 */
public class LikeConverter<B> extends AssetConsumerOMASConverter<B>
{
    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public LikeConverter(OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /**
     * Extract the properties from the entity.  Each DataManager OMAS converter implements this method.
     * The top level fills in the header
     *
     * @param metadataElement output bean
     * @param entity entity containing the properties
     * @param relationship optional relationship containing the properties
     */
    public void updateMetadataElement(MetadataElement metadataElement, EntityDetail entity, Relationship relationship)
    {
        metadataElement.setElementHeader(this.getMetadataElementHeader(entity));

        if (metadataElement instanceof LikeProperties)
        {
            LikeProperties bean = (LikeProperties) metadataElement;

            /*
             * The initial set of values come from the entity.
             */
            InstanceProperties instanceProperties = new InstanceProperties(entity.getProperties());

            bean.setUser(entity.getCreatedBy());

            if (relationship != null)
            {
                instanceProperties = new InstanceProperties(relationship.getProperties());

                bean.setPublic(this.getIsPublic(instanceProperties));
            }
        }
    }
}
