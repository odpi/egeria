package org.odpi.openmetadata.accessservices.dataengine.server.intopic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineEventHeader;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineServicesInstance;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataEngineInTopicProcessor implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(DataEngineServicesInstance.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private DataEngineServicesInstance instance;
    private OMRSRepositoryHelper repositoryHelper;

    /**
     * The constructor is given the connection to the out topic for Data Engine OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param repositoryHelper  connector to receive incoming events
     * @param instance  server instance
     * @param auditLog audit log
     */
    public DataEngineInTopicProcessor(OMRSRepositoryHelper         repositoryHelper,
                                      DataEngineServicesInstance   instance,
                                      OMRSAuditLog                 auditLog)

    {
        super();
        this.repositoryHelper = repositoryHelper;
        this.auditLog = auditLog;
        this.instance = instance;
    }


    /**
     * The server is shutting down
     */
    public void shutdown()
    {

    }


    /**
     * Method to pass an event received on topic.
     *
     * @param dataEngineEvent inbound event
     */
    @Override
    public void processEvent(String dataEngineEvent) {
        log.debug("Processing instance event", dataEngineEvent);

        if (dataEngineEvent == null) {
            log.debug("Null instance event - ignoring event");
        } else {

            try {
                DataEngineEventHeader dataEngineEventHeader = OBJECT_MAPPER.readValue(dataEngineEvent, DataEngineEventHeader.class);

                if ((dataEngineEventHeader != null) ) {
                    switch (dataEngineEventHeader.getEventType().getEventTypeCode()) {

                        case 2:
                            processDataEngineRegistrationEvent(dataEngineEvent);
                            break;

                        case 3:

                            break;

                        case 4:

                            break;
                        case 5:

                            break;
                        case 6:

                            break;
                        case 7:

                            break;
                    }
                } else {
                    log.debug("Ignored instance event - null data engine event type");
                }
            } catch (JsonProcessingException e) {
                log.debug("Exception processing event from in Data Engine In Topic", e);
                DataEngineErrorCode errorCode = DataEngineErrorCode.PROCESS_EVENT_EXCEPTION;
                auditLog.logException("processEvent",
                        errorCode.getErrorMessageId(),
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                        e.getMessage(),
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            }
        }
    }

    private void processDataEngineRegistrationEvent (String dataEngineEvent) {
        try {
            DataEngineRegistrationEvent dataEngineRegistrationEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DataEngineRegistrationEvent.class);
            instance.getDataEngineRegistrationHandler().createExternalDataEngine(dataEngineRegistrationEvent.getUserId(),dataEngineRegistrationEvent.getSoftwareServerCapability());

        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            log.debug("Exception in parsing event from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PROCESS_EVENT_EXCEPTION;
            auditLog.logException("processDataEngineRegistrationEvent",
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);

        }
    }
}
