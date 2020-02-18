/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AssetLineagePublisher is the connector responsible for publishing lineage context information about
 * new and changed assets.
 */
public class AssetLineagePublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);
    private OpenMetadataTopicConnector outTopicConnector;


    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *  @param outTopicConnector connection to the out topic
     *
     */
    public AssetLineagePublisher(OpenMetadataTopicConnector outTopicConnector) {
        this.outTopicConnector = outTopicConnector;
    }

    /**
     * Output a new asset event.
     *
     * @param event event to send
     */
    public void publishEvent(AssetLineageEventHeader event) {
        try {
            if (outTopicConnector != null) {
                ObjectMapper objectMapper = new ObjectMapper();
                outTopicConnector.sendEvent(objectMapper.writeValueAsString(event));
            }
        } catch (Throwable error) {
            log.error("Unable to publish new asset event: " + event.toString() + "; error was " + error.toString());
        }
    }

}

