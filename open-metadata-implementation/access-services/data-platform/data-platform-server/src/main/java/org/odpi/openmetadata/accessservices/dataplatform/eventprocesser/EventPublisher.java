package org.odpi.openmetadata.accessservices.dataplatform.eventprocesser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopic;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventPublisher   {

    private static final Logger log = LoggerFactory.getLogger(EventPublisher.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private OpenMetadataTopic dataPlatformTopicConnector;
  //  private ColumnContextEventBuilder columnContextEventBuilder;
    private OMRSAuditLog auditLog;


}
