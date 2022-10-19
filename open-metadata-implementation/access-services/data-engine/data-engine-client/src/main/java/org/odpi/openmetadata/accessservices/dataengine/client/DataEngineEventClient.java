/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.odpi.openmetadata.accessservices.dataengine.connectors.intopic.DataEngineInTopicClientConnector;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineEventType;
import org.odpi.openmetadata.accessservices.dataengine.event.DataEngineRegistrationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DataFileEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DatabaseEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DatabaseSchemaEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.DeleteEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.EventTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.LineageMappingsEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortAliasEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.PortImplementationEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessHierarchyEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.ProcessingStateEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.RelationalTableEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.SchemaTypeEvent;
import org.odpi.openmetadata.accessservices.dataengine.event.TopicEvent;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.List;
import java.util.Map;


/***
 * DataEngineEventClient implements Data Engine client side events interface using provided topic connector.
 * For more information see {@link DataEngineClient} interface definition.
 */
public class DataEngineEventClient implements DataEngineClient {

    private final DataEngineInTopicClientConnector topicConnector;
    private String externalSource;
    private DeleteSemantic deleteSemantic = DeleteSemantic.SOFT;

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

    @Override
    public String createOrUpdateProcess(String userId, Process process) throws InvalidParameterException, ConnectorCheckedException {
        ProcessEvent event = new ProcessEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.PROCESS_EVENT);
        event.setProcess(process);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    @Override
    public void deleteProcess(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = new DeleteEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.DELETE_PROCESS_EVENT);
        event.setQualifiedName(qualifiedName);
        event.setGuid(guid);
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
    public String createExternalDataEngine(String userId, Engine engine) throws InvalidParameterException,
                                                                                                                    ConnectorCheckedException {
        DataEngineRegistrationEvent event = new DataEngineRegistrationEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.DATA_ENGINE_REGISTRATION_EVENT);
        event.setEngine(engine);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteExternalDataEngine(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                                  ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_DATA_ENGINE_EVENT);

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
        event.setDataEngineEventType(DataEngineEventType.SCHEMA_TYPE_EVENT);
        event.setSchemaType(schemaType);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSchemaType(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_SCHEMA_TYPE_EVENT);

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
        event.setDataEngineEventType(DataEngineEventType.PORT_IMPLEMENTATION_EVENT);
        event.setPortImplementation(portImplementation);
        event.setProcessQualifiedName(processQualifiedName);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePortImplementation(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                                  ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_PORT_IMPLEMENTATION_EVENT);

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
        event.setDataEngineEventType(DataEngineEventType.PORT_ALIAS_EVENT);
        event.setPortAlias(portAlias);
        event.setProcessQualifiedName(processQualifiedName);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePortAlias(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_PORT_ALIAS_EVENT);

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
        event.setDataEngineEventType(DataEngineEventType.PROCESS_HIERARCHY_EVENT);
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
        event.setDataEngineEventType(DataEngineEventType.LINEAGE_MAPPINGS_EVENT);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertDatabase(String userId, Database database) throws InvalidParameterException, ConnectorCheckedException {
        DatabaseEvent event = new DatabaseEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.DATABASE_EVENT);
        event.setDatabase(database);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertDatabaseSchema(String userId, DatabaseSchema databaseSchema, String databaseQualifiedName) throws InvalidParameterException,
                                                                                                                          ConnectorCheckedException {
        DatabaseSchemaEvent event = new DatabaseSchemaEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.DATABASE_SCHEMA_EVENT);
        event.setDatabaseSchema(databaseSchema);
        event.setDatabaseQualifiedName(databaseQualifiedName);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertRelationalTable(String userId, RelationalTable relationalTable, String databaseSchemaQualifiedName) throws
                                                                                                                            InvalidParameterException,
                                                                                                                            ConnectorCheckedException {
        RelationalTableEvent event = new RelationalTableEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.RELATIONAL_TABLE_EVENT);
        event.setRelationalTable(relationalTable);
        event.setDatabaseSchemaQualifiedName(databaseSchemaQualifiedName);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertDataFile(String userId, DataFile dataFile) throws InvalidParameterException, ConnectorCheckedException {
        DataFileEvent event = new DataFileEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.DATA_FILE_EVENT);
        event.setDataFile(dataFile);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDatabase(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_DATABASE_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDatabaseSchema(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_DATABASE_SCHEMA_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRelationalTable(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_RELATIONAL_TABLE_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDataFile(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_DATA_FILE_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFolder(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_FOLDER_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteConnection(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_CONNECTION_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEndpoint(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_ENDPOINT_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GUIDListResponse find(String userId, FindRequestBody findRequestBody) throws FunctionNotSupportedException {
        String methodName = "find";

        throw new FunctionNotSupportedException(DataEngineErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition(methodName),
                this.getClass().getName(), methodName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertTopic(String userId, Topic topic) throws InvalidParameterException, ConnectorCheckedException {
        TopicEvent event = new TopicEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.TOPIC_EVENT);
        event.setTopic(topic);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertEventType(String userId, EventType eventType, String topicQualifiedName) throws InvalidParameterException,
                                                                                                        ConnectorCheckedException {
        EventTypeEvent event = new EventTypeEvent();
        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.EVENT_TYPE_EVENT);
        event.setTopicQualifiedName(topicQualifiedName);
        event.setEventType(eventType);

        topicConnector.sendEvent(event);

        //async interaction
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTopic(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_TOPIC_EVENT);

        topicConnector.sendEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEventType(String userId, String qualifiedName, String guid) throws InvalidParameterException, ConnectorCheckedException {
        DeleteEvent event = getDeleteEvent(userId, qualifiedName, guid);
        event.setDataEngineEventType(DataEngineEventType.DELETE_EVENT_TYPE_EVENT);

        topicConnector.sendEvent(event);
    }

    @Override
    public void upsertProcessingState(String userId, Map<String, Long> properties) throws InvalidParameterException, ConnectorCheckedException {

        ProcessingState processingState = new ProcessingState(properties);

        ProcessingStateEvent event = new ProcessingStateEvent();

        event.setUserId(userId);
        event.setExternalSourceName(externalSource);
        event.setDataEngineEventType(DataEngineEventType.PROCESSING_STATE_TYPE_EVENT);
        event.setProcessingState(processingState);

        topicConnector.sendEvent(event);
    }

    @Override
    public Map<String, Long> getProcessingState(String userId) {
        //async interaction
        return null;
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
