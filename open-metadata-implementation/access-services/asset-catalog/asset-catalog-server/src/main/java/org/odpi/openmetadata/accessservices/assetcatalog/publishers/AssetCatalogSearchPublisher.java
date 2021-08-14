/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * AssetCatalogSearchPublisher is publishing asset indexing events
 */
public class AssetCatalogSearchPublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetCatalogSearchPublisher.class);

    private OpenMetadataTopicConnector  topicConnector;

    public AssetCatalogSearchPublisher( OpenMetadataTopicConnector outTopicConnector) throws OMAGConfigurationErrorException
    {
        this.topicConnector = outTopicConnector;
    }

    /**
     * Publish an  event.
     *
     * @param event event to send
     */
    public void publishEvent(Serializable event)
    {
        try
        {
            if (topicConnector != null)
            {
                ObjectMapper objectMapper = new ObjectMapper();
                topicConnector.sendEvent(objectMapper.writeValueAsString(event));
            }
            else
            {
                log.error("Cannot publish event: topic connector is null!");
            }
        }
        catch (JsonProcessingException e)
        {
            log.error("Unable to create json for publishing: " + event.toString(), e);
        }
        catch (Exception  e)
        {
            log.error("Unable to publish new asset event", e);
        }
    }
}


