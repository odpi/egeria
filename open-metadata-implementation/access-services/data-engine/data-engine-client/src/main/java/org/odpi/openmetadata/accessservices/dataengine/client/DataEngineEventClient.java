/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.connectors.intopic.DataEngineInTopicClientConnector;
import org.odpi.openmetadata.accessservices.dataengine.event.*;
import org.odpi.openmetadata.accessservices.dataengine.model.*;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

import java.util.List;


/***
 * DataEngineEventClient implements Data Engine client side events interface using provided topic connector.
 * For more information see {@link DataEngineClient} interface definition.
 */
public class DataEngineEventClient implements DataEngineClient {

    private DataEngineInTopicClientConnector topicConnector;
    private String externalSource;

    /**
     * Constructor to create DataEngineEventClient with unauthenticated access to the server
     *
     * @param dataEngineInTopicClientConnector topic connector used to publish to InTopic
     */
    public DataEngineEventClient(DataEngineInTopicClientConnector dataEngineInTopicClientConnector) {
        this.topicConnector = dataEngineInTopicClientConnector;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> createOrUpdateProcesses(String userId, List<Process> processes) throws InvalidParameterException, ConnectorCheckedException {

        ProcessesEvent event = new ProcessesEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PROCESSES_EVENT);
        event.setProcesses(processes);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createExternalDataEngine(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException, ConnectorCheckedException {

        DataEngineRegistrationEvent event = new DataEngineRegistrationEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.DATA_ENGINE_REGISTRATION_EVENT);
        event.setSoftwareServerCapability(softwareServerCapability);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException, ConnectorCheckedException  {

        SchemaTypeEvent event = new SchemaTypeEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.SCHEMA_TYPE_EVENT);
        event.setSchemaType(schemaType);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation) throws InvalidParameterException, ConnectorCheckedException {

        PortImplementationEvent event = new PortImplementationEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PORT_IMPLEMENTATION_EVENT);
        event.setPortImplementation(portImplementation);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createOrUpdatePortAlias(String userId, PortAlias portAlias) throws InvalidParameterException, ConnectorCheckedException {

        PortAliasEvent event = new PortAliasEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PORT_ALIAS_EVENT);
        event.setPort(portAlias);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String addProcessHierarchy(String userId, ProcessHierarchy processHierarchy) throws InvalidParameterException, ConnectorCheckedException {

        ProcessHierarchyEvent event = new ProcessHierarchyEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PROCESS_HIERARCHY_EVENT);
        event.setProcessHierarchy(processHierarchy);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public void addLineageMappings(String userId, List<LineageMapping> lineageMappings) throws InvalidParameterException, ConnectorCheckedException {

        LineageMappingsEvent event = new LineageMappingsEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.LINEAGE_MAPPINGS_EVENT);
        event.setLineageMappings(lineageMappings);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public void addPortsToProcess(String userId, List<String> portGUIDs, String processGUID) throws InvalidParameterException, ConnectorCheckedException {

        ProcessToPortListEvent event = new ProcessToPortListEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PROCESS_TO_PORT_LIST_EVENT);
        event.setPorts(portGUIDs);
        event.setProcessGUID(processGUID);

        topicConnector.sendEvent(event);
    }

    /**
     * Sets external source system name using the data engine client
     *
     * @param externalSourceName Source system name
     */
    @Override
    public void setExternalSourceName(String externalSourceName) {
        this.externalSource = externalSourceName;
    }

    /**
     * Returns the name of the source system using data engine client
     *
     * @return Source system name
     */
    @Override
    public String getExternalSourceName() {
        return this.externalSource;
    }
}
