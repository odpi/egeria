/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.event;

import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the event listener for the virtualizer. When the out topic of the IV OMAS is received,
 * the listener will consume the event and process correspondingly.
 */

public class VirtualizerTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(VirtualizerTopicListener.class);

    private OpenMetadataTopicConnector ivInTopicConnector;

    /**
     * Default constructor.
     * @param ivInTopicConnector the event connector to sent out the processed topic
     */
    public VirtualizerTopicListener(OpenMetadataTopicConnector ivInTopicConnector){
        this.ivInTopicConnector = ivInTopicConnector;
    }

    /**
     * Process the received event.
     * @param event inbound event
     */
    @Override
    public void processEvent (String event){
        log.info("The following event is received: " + event);
        try{
            ivInTopicConnector.sendEvent(event);
        }catch (Exception e){
            log.error("Error in sending event: ", e);
        }
    }

}
