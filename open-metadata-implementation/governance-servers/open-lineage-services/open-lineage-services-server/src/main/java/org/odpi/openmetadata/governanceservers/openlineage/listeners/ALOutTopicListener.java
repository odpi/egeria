/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.governanceservers.openlineage.eventprocessors.GraphConstructor;
import org.odpi.openmetadata.governanceservers.openlineage.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.governanceservers.openlineage.responses.ffdc.OpenLineageErrorCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventType;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ALOutTopicListener implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(ALOutTopicListener.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private GraphConstructor graphConstructor;

    public ALOutTopicListener(GraphConstructor gremlinBuilder, OMRSAuditLog auditLog) {

        this.graphConstructor = gremlinBuilder;
        this.auditLog = auditLog;

    }


    /**
     * @param eventAsString contains all the information needed to build asset lineage like connection details, database
     *                      name, schema name, table name, derived columns details
     */
    @Override
    public void processEvent(String eventAsString) {
        try {
            OMRSEventV1 event = OBJECT_MAPPER.readValue(eventAsString, OMRSEventV1.class);
            log.info("Started processing OpenLineageEvent");
            OMRSInstanceEvent omrsInstanceEvent = new OMRSInstanceEvent(event);
            OMRSInstanceEventType instanceEventType = omrsInstanceEvent.getInstanceEventType();
            switch (instanceEventType) {
                case NEW_RELATIONSHIP_EVENT:
                    graphConstructor.addNewRelationship(omrsInstanceEvent);
                    break;
                case UPDATED_RELATIONSHIP_EVENT:
                    break;
            }
        } catch (Exception e) {
        }
        try {
            AssetResponse event = OBJECT_MAPPER.readValue(eventAsString, AssetResponse.class);
            log.info("Started processing OpenLineageEvent");
        } catch (Exception e) {
        }

    }

}
