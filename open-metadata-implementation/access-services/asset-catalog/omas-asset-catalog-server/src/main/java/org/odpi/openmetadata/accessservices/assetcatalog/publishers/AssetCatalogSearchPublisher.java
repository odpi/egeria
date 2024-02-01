/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * AssetCatalogSearchPublisher is publishing asset indexing events.
 */
public class AssetCatalogSearchPublisher {

    private static final Logger LOG = LoggerFactory.getLogger(AssetCatalogSearchPublisher.class);
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    private final OpenMetadataTopicConnector topicConnector;

    /**
     * Instantiates a new AssetCatalogSearchPublisher object.
     *
     * @param outTopicConnector the out topic connector
     */
    public AssetCatalogSearchPublisher(OpenMetadataTopicConnector outTopicConnector)
    {
        this.topicConnector = outTopicConnector;
    }

    /**
     * Publish an asset indexing event.
     *
     * @param event event to send
     */
    public void publishEvent(Serializable event)
    {
        try
        {
            if (topicConnector != null)
            {
                topicConnector.sendEvent(OBJECT_WRITER.writeValueAsString(event));
            }
            else
            {
                LOG.error("Cannot publish event: topic connector is null!");
            }
        }
        catch (JsonProcessingException e)
        {
            LOG.error("Unable to create json for publishing: " + event.toString(), e);
        }
        catch (Exception  e)
        {
            LOG.error("Unable to publish new asset event", e);
        }
    }
}


