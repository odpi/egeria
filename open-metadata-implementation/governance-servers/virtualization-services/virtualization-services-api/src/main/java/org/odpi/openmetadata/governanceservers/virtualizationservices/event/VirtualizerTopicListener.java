/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.virtualizationservices.event;

import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualizerTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(VirtualizerTopicListener.class);

    private OpenMetadataTopicConnector ivInTopicConnector;

    public VirtualizerTopicListener(OpenMetadataTopicConnector ivInTopicConnector){
        this.ivInTopicConnector = ivInTopicConnector;
    }

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
