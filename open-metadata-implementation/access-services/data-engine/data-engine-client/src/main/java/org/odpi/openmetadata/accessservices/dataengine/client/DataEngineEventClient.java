/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.connectors.intopic.DataEngineInTopicClientConnector;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineEventType;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DeleteEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessesDeleteEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.LineageMappingsEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortAliasEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortImplementationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessHierarchyEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessesEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.SchemaTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
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
    private DeleteSemantic deleteSemantic = DeleteSemantic.HARD;

    /**
     * Constructor to create DataEngineEventClient with unauthenticated access to the server
     *
     * @param dataEngineInTopicClientConnector topic connector used to publish to InTopic
     */
    public DataEngineEventClient(DataEngineInTopicClientConnector dataEngineInTopicClientConnector) {
        this.topicConnector = dataEngineInTopicClientConnector;
    }

    public DeleteSemantic getDeleteSemantic() {
        return deleteSemantic;
    }

    public void setDeleteSemantic(DeleteSemantic deleteSemantic) {
        this.deleteSemantic = deleteSemantic;
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

    @Override
    public void deleteProcesses(String userId, List<String> qualifiedNames, List<String> guids) throws InvalidParameterException,
                                                                                                       ConnectorCheckedException {
        ProcessesDeleteEvent event = new ProcessesDeleteEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.DELETE_PROCESSES_EVENT);
        event.setQualifiedNames(qualifiedNames);
        event.setGuids(guids);
        event.setDeleteSemantic(deleteSemantic);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createExternalDataEngine(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException,
                                                                                                                    ConnectorCheckedException {
        DataEngineRegistrationEvent event = new DataEngineRegistrationEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.DATA_ENGINE_REGISTRATION_EVENT);
        event.setSoftwareServerCapability(softwareServerCapability);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    @Override
    public void deleteExternalDataEngine(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                                  ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setEventType(DataEngineEventType.DELETE_DATA_ENGINE_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException, ConnectorCheckedException {
        SchemaTypeEvent event = new SchemaTypeEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.SCHEMA_TYPE_EVENT);
        event.setSchemaType(schemaType);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    @Override
    public void deleteSchemaType(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setEventType(DataEngineEventType.DELETE_SCHEMA_TYPE_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation, String processQualifiedName) throws
                                                                                                                                      InvalidParameterException,
                                                                                                                                      ConnectorCheckedException {

        PortImplementationEvent event = new PortImplementationEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PORT_IMPLEMENTATION_EVENT);
        event.setPortImplementation(portImplementation);
        event.setProcessQualifiedName(processQualifiedName);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    @Override
    public void deletePortImplementation(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                                  ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setEventType(DataEngineEventType.DELETE_PORT_IMPLEMENTATION_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws ConnectorCheckedException problem with the underlying connector (if used)
     */
    @Override
    public String createOrUpdatePortAlias(String userId, PortAlias portAlias, String processQualifiedName) throws InvalidParameterException,
                                                                                                                  ConnectorCheckedException {

        PortAliasEvent event = new PortAliasEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setEventType(DataEngineEventType.PORT_ALIAS_EVENT);
        event.setPort(portAlias);
        event.setProcessQualifiedName(processQualifiedName);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    @Override
    public void deletePortAlias(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setEventType(DataEngineEventType.DELETE_PORT_ALIAS_EVENT);

        topicConnector.sendEvent(event);
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

    private DeleteEvent getDeleteEvent(String userId, String qualifiedName, String guid) {
        DeleteEvent event = new DeleteEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setQualifiedName(qualifiedName);
        event.setGuid(guid);
        event.setDeleteSemantic(deleteSemantic);
        return event;
    }
}
