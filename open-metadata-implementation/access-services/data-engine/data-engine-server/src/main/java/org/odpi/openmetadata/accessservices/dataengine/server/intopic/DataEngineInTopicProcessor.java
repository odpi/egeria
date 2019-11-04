/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.intopic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.event.*;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.NoSchemaAttributeException;
import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineServicesInstance;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.service.DataEngineRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicListener;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.partitioningBy;

public class DataEngineInTopicProcessor implements OpenMetadataTopicListener {

    private static final Logger log = LoggerFactory.getLogger(DataEngineServicesInstance.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final OMRSAuditLog auditLog;
    private DataEngineServicesInstance instance;
    private String serverName;

    private DataEngineRESTServices dataEngineRESTServices = new DataEngineRESTServices();

    /**
     * The constructor is given the connection to the out topic for Data Engine OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param instance server instance
     * @param auditLog audit log
     */
    public DataEngineInTopicProcessor(DataEngineServicesInstance instance,
                                      OMRSAuditLog auditLog) {
        super();
        this.auditLog = auditLog;
        this.instance = instance;
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
                this.serverName = instance.getServerName();

                DataEngineEventHeader dataEngineEventHeader = OBJECT_MAPPER.readValue(dataEngineEvent, DataEngineEventHeader.class);

                if ((dataEngineEventHeader != null)) {
                    switch (dataEngineEventHeader.getEventType()) {

                        case DATA_ENGINE_REGISTRATION_EVENT:
                            processDataEngineRegistrationEvent(dataEngineEvent);
                            break;
                        case LINEAGE_MAPPINGS_EVENT:
                            processLineageMappingsEvent(dataEngineEvent);
                            break;
                        case PORT_ALIAS_EVENT:
                            processPortAliasEvent(dataEngineEvent);
                            break;
                        case PORT_IMPLEMENTATION_EVENT:
                            processPortImplementationEvent(dataEngineEvent);
                            break;
                        case PROCESS_TO_PORT_LIST_EVENT:
                            processProcessToPortListEvent(dataEngineEvent);
                            break;
                        case PROCESSES_EVENT:
                            processProcessesEvent(dataEngineEvent);
                            break;
                    }
                } else {
                    log.debug("Ignored instance event - null Data Engine event type");
                }
            } catch (JsonProcessingException | NewInstanceException e) {
                log.debug("Exception processing event from in Data Engine In Topic", e);
                DataEngineErrorCode errorCode = DataEngineErrorCode.PROCESS_EVENT_EXCEPTION;
                auditLog.logException("process Data Engine inTopic Event",
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

    private void processDataEngineRegistrationEvent(String dataEngineEvent) {

        final String methodName = "processDataEngineRegistrationEvent";
        try {
            DataEngineRegistrationEvent dataEngineRegistrationEvent = OBJECT_MAPPER.readValue(dataEngineEvent, DataEngineRegistrationEvent.class);
            instance.getDataEngineRegistrationHandler().createExternalDataEngine(dataEngineRegistrationEvent.getUserId(), dataEngineRegistrationEvent.getSoftwareServerCapability());

        } catch (JsonProcessingException | UserNotAuthorizedException | PropertyServerException | InvalidParameterException e) {
            log.debug("Exception in parsing DataEngineRegistrationEvent from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
            auditLog.logException(methodName,
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);

        }
    }

    private void processLineageMappingsEvent(String dataEngineEvent) {

        final String methodName = "processLineageMappingsEvent";
        try {
            LineageMappingsEvent lineageMappingsEvent = OBJECT_MAPPER.readValue(dataEngineEvent, LineageMappingsEvent.class);

            log.debug("Calling method: {}", methodName);

            if (CollectionUtils.isEmpty(lineageMappingsEvent.getLineageMappings())) {
                return;
            }
            addLineageMappings(lineageMappingsEvent.getUserId(), lineageMappingsEvent.getLineageMappings(),
                    lineageMappingsEvent.getExternalSourceName());
        } catch (JsonProcessingException e) {
            log.debug("Exception in parsing LineageMappingsEvent from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
            auditLog.logException(methodName,
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
    }


    private void processPortAliasEvent(String dataEngineEvent) {

        final String methodName = "processPortAliasEvent";
        try {
            PortAliasEvent portAliasEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortAliasEvent.class);

            log.debug("Calling method: {}", methodName);

            dataEngineRESTServices.createOrUpdatePortAliasWithDelegation(portAliasEvent.getUserId(),
                    serverName, portAliasEvent.getPort(), portAliasEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            log.debug("Exception in parsing PortAliasEvent from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
            auditLog.logException(methodName,
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
    }

    private void processPortImplementationEvent(String dataEngineEvent) {

        final String methodName = "processPortImplementationEvent";
        try {
            PortImplementationEvent portImplementationEvent = OBJECT_MAPPER.readValue(dataEngineEvent, PortImplementationEvent.class);

            log.debug("Calling method: {}", methodName);

            dataEngineRESTServices.createOrUpdatePortImplementationWithSchemaType(
                    portImplementationEvent.getUserId(),
                    serverName,
                    portImplementationEvent.getPortImplementation(),
                    portImplementationEvent.getExternalSourceName());

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            log.debug("Exception in parsing PortImplementationEvent from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
            auditLog.logException(methodName,
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
    }

    private void processProcessToPortListEvent(String dataEngineEvent) {

        final String methodName = "processProcessToPortListEvent";
        try {
            ProcessToPortListEvent processToPortListEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessToPortListEvent.class);

            log.debug("Calling method: {}", methodName);

            for (String portGUID : processToPortListEvent.getPorts()) {
                instance.getProcessHandler().addProcessPortRelationship(
                        processToPortListEvent.getUserId(),
                        processToPortListEvent.getProcessGUID(),
                        portGUID,
                        processToPortListEvent.getExternalSourceName());
            }

        } catch (JsonProcessingException | PropertyServerException | UserNotAuthorizedException | InvalidParameterException e) {
            log.debug("Exception in parsing ProcessToPortListEvent from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
            auditLog.logException(methodName,
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
    }

    private void processProcessesEvent(String dataEngineEvent) {

        final String methodName = "processProcessesEvent";
        try {
            ProcessesEvent processesEvent = OBJECT_MAPPER.readValue(dataEngineEvent, ProcessesEvent.class);

            createOrUpdateProcesses(processesEvent);

            log.debug("Calling method: {}", methodName);

        } catch (JsonProcessingException e) {
            log.debug("Exception in parsing event from in Data Engine In Topic", e);
            DataEngineErrorCode errorCode = DataEngineErrorCode.PARSE_EVENT_EXCEPTION;
            auditLog.logException(methodName,
                    errorCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    errorCode.getFormattedErrorMessage(dataEngineEvent, e.getMessage()),
                    e.getMessage(),
                    errorCode.getSystemAction(),
                    errorCode.getUserAction(),
                    e);
        }
    }

    private void addLineageMappings(String userId, List<LineageMapping> lineageMappings, String externalSouceName) {

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instance.getDataEngineSchemaTypeHandler();

        lineageMappings.parallelStream().forEach(lineageMapping -> {
            try {
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(userId, lineageMapping.getSourceAttribute(),
                        lineageMapping.getTargetAttribute(), externalSouceName);
            } catch (PropertyServerException | NoSchemaAttributeException | UserNotAuthorizedException | InvalidParameterException
                    e) {
                log.debug("Exception in parsing event from in Data Engine In Topic", e);
                DataEngineErrorCode errorCode = DataEngineErrorCode.PROCESS_EVENT_EXCEPTION;
                auditLog.logException("add lineage mappings from LineageMappingsEvent",
                        errorCode.getErrorMessageId(),
                        OMRSAuditLogRecordSeverity.EXCEPTION,
                        errorCode.getFormattedErrorMessage(lineageMappings.toString(), e.getMessage()),
                        e.getMessage(),
                        errorCode.getSystemAction(),
                        errorCode.getUserAction(),
                        e);
            }
        });
    }

    private void createOrUpdateProcesses(ProcessesEvent processesEvent) {


        final String methodName = "createOrUpdateProcesses";

        log.debug("Calling method: {}", methodName);

        ProcessListResponse response = new ProcessListResponse();

        if (processesEvent == null || CollectionUtils.isEmpty(processesEvent.getProcesses())) {

        }

        List<GUIDResponse> guidResponses = dataEngineRESTServices.createOrUpdateProcesses(
                processesEvent.getUserId(),
                serverName,
                processesEvent.getProcesses(),
                processesEvent.getExternalSourceName());

        Predicate<? super GUIDResponse> processStatusPredicate =
                guidResponse -> guidResponse.getRelatedHTTPCode() == HttpStatus.OK.value();

        Map<Boolean, List<GUIDResponse>> mappedResponses =
                guidResponses.parallelStream().collect(partitioningBy(processStatusPredicate));

        List<GUIDResponse> createdProcesses = dataEngineRESTServices.getGuidResponses(response, mappedResponses.get(Boolean.TRUE));

        dataEngineRESTServices.handleFailedProcesses(response, mappedResponses.get(Boolean.FALSE));

        createdProcesses.parallelStream().forEach(guidResponse -> dataEngineRESTServices.updateProcessStatus(processesEvent.getUserId(),
                serverName, guidResponse, InstanceStatus.ACTIVE));

    }
}
