/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.client;

import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFlow;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineOMASAPIRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.EventTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFlowsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessingStateRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.TopicRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.client.OCFRESTClient;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Data Engine Open Metadata Access Service (OMAS) provides an interface for data engine tools to create
 * processes with ports, schemas and relationships. See interface definition for more explanation.
 */
public class DataEngineRESTClient extends OCFRESTClient implements DataEngineClient {
    private static final String DATA_ENGINE_PATH = "/servers/{0}/open-metadata/access-services/data-engine/users/{1}/";
    private static final String PROCESS_URL_TEMPLATE = DATA_ENGINE_PATH + "processes";
    private static final String DATA_ENGINE_REGISTRATION_URL_TEMPLATE = DATA_ENGINE_PATH + "registration";
    private static final String SCHEMA_TYPE_URL_TEMPLATE = DATA_ENGINE_PATH + "schema-types";
    private static final String PORT_IMPLEMENTATION_URL_TEMPLATE = DATA_ENGINE_PATH + "port-implementations";
    private static final String PROCESS_HIERARCHY_URL_TEMPLATE = DATA_ENGINE_PATH + "process-hierarchies";
    private static final String DATA_FLOWS_URL_TEMPLATE = DATA_ENGINE_PATH + "data-flows";
    private static final String DATABASE_URL_TEMPLATE = DATA_ENGINE_PATH + "databases";
    private static final String DATABASE_SCHEMA_URL_TEMPLATE = DATA_ENGINE_PATH + "database-schemas";
    private static final String RELATIONAL_TABLE_URL_TEMPLATE = DATA_ENGINE_PATH + "relational-tables";
    private static final String DATA_FILE_URL_TEMPLATE = DATA_ENGINE_PATH + "data-files";
    private static final String FOLDER_URL_TEMPLATE = DATA_ENGINE_PATH + "folders";
    private static final String CONNECTION_URL_TEMPLATE = DATA_ENGINE_PATH + "connections";
    private static final String ENDPOINT_URL_TEMPLATE = DATA_ENGINE_PATH + "endpoints";
    private static final String FIND_URL_TEMPLATE = DATA_ENGINE_PATH + "find";
    private static final String TOPIC_URL_TEMPLATE = DATA_ENGINE_PATH + "topics";
    private static final String EVENT_TYPE_URL_TEMPLATE = DATA_ENGINE_PATH + "event-types";
    private static final String PROCESSING_STATE_URL_TEMPLATE = DATA_ENGINE_PATH + "processing-state";

    private static final String PROCESS_METHOD_NAME = "createOrUpdateProcess";
    private static final String PROCESS_DELETE_METHOD_NAME = "deleteProcess";
    private static final String EXTERNAL_DATA_ENGINE_METHOD_NAME = "createExternalDataEngine";
    private static final String EXTERNAL_DATA_ENGINE_DELETE_METHOD_NAME = "deleteExternalDataEngine";
    private static final String SCHEMA_TYPE_METHOD_NAME = "createOrUpdateSchemaType";
    private static final String SCHEMA_TYPE_DELETE_METHOD_NAME = "deleteSchemaType";
    private static final String PORT_IMPLEMENTATION_METHOD_NAME = "createOrUpdatePortImplementation";
    private static final String PORT_IMPLEMENTATION_DELETE_METHOD_NAME = "deletePortImplementation";
    private static final String PROCESS_HIERARCHY_METHOD_NAME = "createOrUpdateProcessHierarchy";
    private static final String DATA_FLOWS_METHOD_NAME = "addDataFlows";
    private static final String DATABASE_METHOD_NAME = "upsertDatabase";
    private static final String DATABASE_SCHEMA_METHOD_NAME = "upsertDatabaseSchema";
    private static final String RELATIONAL_TABLE_METHOD_NAME = "upsertRelationalTable";
    private static final String DATA_FILE_METHOD_NAME = "upsertDataFile";
    private static final String DATABASE_DELETE_METHOD_NAME = "deleteDatabase";
    private static final String DATABASE_SCHEMA_DELETE_METHOD_NAME = "deleteDatabaseSchema";
    private static final String RELATIONAL_TABLE_DELETE_METHOD_NAME = "deleteRelationalTable";
    private static final String DATA_FILE_DELETE_METHOD_NAME = "deleteDataFile";
    private static final String FOLDER_DELETE_METHOD_NAME = "deleteFolder";
    private static final String CONNECTION_DELETE_METHOD_NAME = "deleteConnection";
    private static final String ENDPOINT_DELETE_METHOD_NAME = "deleteEndpoint";
    private static final String FIND_METHOD_NAME = "find";
    private static final String TOPIC_METHOD_NAME = "upsertTopic";
    private static final String EVENT_TYPE_METHOD_NAME = "upsertEventType";
    private static final String TOPIC_DELETE_METHOD_NAME = "deleteTopic";
    private static final String EVENT_TYPE_DELETE_METHOD_NAME = "deleteEventType";

    private final String serverPlatformRootURL;
    private String externalSourceName;
    private DeleteSemantic deleteSemantic = DeleteSemantic.SOFT;
    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Create a new client without authentication.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineRESTClient(String serverName, String serverPlatformRootURL) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
    }

    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException null URL or server name
     */
    public DataEngineRESTClient(String serverName, String serverPlatformRootURL, String userId, String password) throws InvalidParameterException {
        super(serverName, serverPlatformRootURL, userId, password);

        this.serverName = serverName;
        this.serverPlatformRootURL = serverPlatformRootURL;
    }

    public String getExternalSourceName() {
        return externalSourceName;
    }

    public void setExternalSourceName(String externalSourceName) {
        this.externalSourceName = externalSourceName;
    }

    public DeleteSemantic getDeleteSemantic() {
        return deleteSemantic;
    }

    public void setDeleteSemantic(DeleteSemantic deleteSemantic) {
        this.deleteSemantic = deleteSemantic;
    }

    @Override
    public String createOrUpdateProcess(String userId, Process process) throws InvalidParameterException, PropertyServerException,
                                                                               UserNotAuthorizedException {
        invalidParameterHandler.validateUserId(userId, PROCESS_METHOD_NAME);

        ProcessRequestBody requestBody = new ProcessRequestBody();
        requestBody.setProcess(process);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, PROCESS_METHOD_NAME, PROCESS_URL_TEMPLATE, requestBody);
    }

    @Override
    public void deleteProcess(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, PROCESS_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setGuid(guid);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setDeleteSemantic(deleteSemantic);

        callVoidDeleteRESTCall(userId, PROCESS_DELETE_METHOD_NAME, PROCESS_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createExternalDataEngine(String userId, Engine engine) throws
                                                                                                             InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException {

        invalidParameterHandler.validateUserId(userId, EXTERNAL_DATA_ENGINE_METHOD_NAME);

        DataEngineRegistrationRequestBody requestBody = new DataEngineRegistrationRequestBody();
        requestBody.setEngine(engine);

        return callGUIDPostRESTCall(userId, EXTERNAL_DATA_ENGINE_METHOD_NAME, DATA_ENGINE_REGISTRATION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteExternalDataEngine(String userId, String qualifiedName, String guid) throws InvalidParameterException,
                                                                                                  PropertyServerException {
        invalidParameterHandler.validateUserId(userId, EXTERNAL_DATA_ENGINE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, EXTERNAL_DATA_ENGINE_DELETE_METHOD_NAME, DATA_ENGINE_REGISTRATION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdateSchemaType(String userId, SchemaType schemaType) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException {

        invalidParameterHandler.validateUserId(userId, SCHEMA_TYPE_METHOD_NAME);

        SchemaTypeRequestBody requestBody = new SchemaTypeRequestBody();
        requestBody.setSchemaType(schemaType);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, SCHEMA_TYPE_METHOD_NAME, SCHEMA_TYPE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteSchemaType(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, SCHEMA_TYPE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, SCHEMA_TYPE_DELETE_METHOD_NAME, SCHEMA_TYPE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String createOrUpdatePortImplementation(String userId, PortImplementation portImplementation, String processQualifiedName) throws
                                                                                                                                      InvalidParameterException,
                                                                                                                                      UserNotAuthorizedException,
                                                                                                                                      PropertyServerException {
        invalidParameterHandler.validateUserId(userId, PORT_IMPLEMENTATION_METHOD_NAME);

        PortImplementationRequestBody requestBody = new PortImplementationRequestBody();
        requestBody.setPortImplementation(portImplementation);
        requestBody.setProcessQualifiedName(processQualifiedName);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, PORT_IMPLEMENTATION_METHOD_NAME, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePortImplementation(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, PORT_IMPLEMENTATION_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, PORT_IMPLEMENTATION_DELETE_METHOD_NAME, PORT_IMPLEMENTATION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String addProcessHierarchy(String userId, ProcessHierarchy processHierarchy) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException {
        final String methodName = PROCESS_HIERARCHY_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        ProcessHierarchyRequestBody requestBody = new ProcessHierarchyRequestBody();
        requestBody.setProcessHierarchy(processHierarchy);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, PROCESS_HIERARCHY_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDataFlows(String userId, List<DataFlow> dataFlows) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException {
        final String methodName = DATA_FLOWS_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        DataFlowsRequestBody requestBody = new DataFlowsRequestBody();
        requestBody.setDataFlows(dataFlows);
        requestBody.setExternalSourceName(externalSourceName);

        callVoidPostRESTCall(userId, methodName, DATA_FLOWS_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertDatabase(String userId, Database database) throws InvalidParameterException,
                                                                          UserNotAuthorizedException, PropertyServerException {
        final String methodName = DATABASE_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        DatabaseRequestBody requestBody = new DatabaseRequestBody();
        requestBody.setDatabase(database);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, DATABASE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertDatabaseSchema(String userId, DatabaseSchema databaseSchema, String databaseQualifiedName) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException {
        final String methodName = DATABASE_SCHEMA_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        DatabaseSchemaRequestBody requestBody = new DatabaseSchemaRequestBody();
        requestBody.setDatabaseSchema(databaseSchema);
        requestBody.setDatabaseQualifiedName(databaseQualifiedName);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, DATABASE_SCHEMA_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertRelationalTable(String userId, RelationalTable relationalTable, String databaseSchemaQualifiedName) throws
                                                                                                                            InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException {
        final String methodName = RELATIONAL_TABLE_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        RelationalTableRequestBody requestBody = new RelationalTableRequestBody();
        requestBody.setRelationalTable(relationalTable);
        requestBody.setDatabaseSchemaQualifiedName(databaseSchemaQualifiedName);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, RELATIONAL_TABLE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertDataFile(String userId, DataFile dataFile) throws InvalidParameterException,
                                                                          UserNotAuthorizedException, PropertyServerException {
        final String methodName = DATA_FILE_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        DataFileRequestBody requestBody = new DataFileRequestBody();
        requestBody.setDataFile(dataFile);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, DATA_FILE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDatabase(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, DATABASE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, DATABASE_DELETE_METHOD_NAME, DATABASE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDatabaseSchema(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, DATABASE_SCHEMA_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, DATABASE_SCHEMA_DELETE_METHOD_NAME, DATABASE_SCHEMA_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRelationalTable(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, RELATIONAL_TABLE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, RELATIONAL_TABLE_DELETE_METHOD_NAME, RELATIONAL_TABLE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteDataFile(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, DATA_FILE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, DATA_FILE_DELETE_METHOD_NAME, DATA_FILE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteFolder(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, FOLDER_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, FOLDER_DELETE_METHOD_NAME, FOLDER_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteConnection(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, CONNECTION_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, CONNECTION_DELETE_METHOD_NAME, CONNECTION_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEndpoint(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, ENDPOINT_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, ENDPOINT_DELETE_METHOD_NAME, ENDPOINT_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GUIDListResponse find(String userId, FindRequestBody findRequestBody) throws ConnectorCheckedException, InvalidParameterException,
                                                                                        UserNotAuthorizedException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, FIND_METHOD_NAME);

        return callGUIDListPostRESTCall(FIND_METHOD_NAME, serverPlatformRootURL + FIND_URL_TEMPLATE, findRequestBody, serverName, userId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertTopic(String userId, Topic topic) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = TOPIC_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        TopicRequestBody requestBody = new TopicRequestBody();
        requestBody.setTopic(topic);
        requestBody.setExternalSourceName(externalSourceName);

        return callGUIDPostRESTCall(userId, methodName, TOPIC_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String upsertEventType(String userId, EventType eventType, String topicQualifiedName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException {
        final String methodName = EVENT_TYPE_METHOD_NAME;

        invalidParameterHandler.validateUserId(userId, methodName);

        EventTypeRequestBody requestBody = new EventTypeRequestBody();
        requestBody.setEventType(eventType);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setTopicQualifiedName(topicQualifiedName);

        return callGUIDPostRESTCall(userId, methodName, EVENT_TYPE_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTopic(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, TOPIC_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, TOPIC_DELETE_METHOD_NAME, TOPIC_URL_TEMPLATE, requestBody);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEventType(String userId, String qualifiedName, String guid) throws InvalidParameterException, PropertyServerException {
        invalidParameterHandler.validateUserId(userId, EVENT_TYPE_DELETE_METHOD_NAME);

        DeleteRequestBody requestBody = getDeleteRequestBody(qualifiedName, guid);

        callVoidDeleteRESTCall(userId, EVENT_TYPE_DELETE_METHOD_NAME, EVENT_TYPE_URL_TEMPLATE, requestBody);
    }

    @Override
    public void upsertProcessingState(String userId, Map<String, Long> properties) throws PropertyServerException,
            InvalidParameterException, UserNotAuthorizedException {
        String methodName = "upsertProcessingState";

        ProcessingState processingState = new ProcessingState(properties);

        ProcessingStateRequestBody requestBody = new ProcessingStateRequestBody(processingState);
        requestBody.setExternalSourceName(externalSourceName);

        callVoidPostRESTCall(userId, methodName, PROCESSING_STATE_URL_TEMPLATE, requestBody);
    }

    @Override
    public Map<String, Long> getProcessingState(String userId) throws PropertyServerException {
        String methodName = "getProcessingState";

        PropertiesResponse restResult =  this.callGetRESTCall(methodName, PropertiesResponse.class,
                serverPlatformRootURL + PROCESSING_STATE_URL_TEMPLATE, serverName, userId, externalSourceName);

        if(restResult == null || MapUtils.isEmpty(restResult.getProperties())) {
            return Collections.emptyMap();
        }
        return restResult.getProperties().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> (Long) e.getValue()));
    }

    private void callVoidPostRESTCall(String userId, String methodName, String urlTemplate, DataEngineOMASAPIRequestBody requestBody,
                                      Object... params) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        super.callVoidPostRESTCall(methodName, serverPlatformRootURL + urlTemplate, requestBody, serverName, userId, params);
    }

    private String callGUIDPostRESTCall(String userId, String methodName, String urlTemplate, DataEngineOMASAPIRequestBody requestBody,
                                        Object... params) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        GUIDResponse restResult = super.callGUIDPostRESTCall(methodName, serverPlatformRootURL + urlTemplate, requestBody, serverName,
                userId, params);

        exceptionHandler.detectAndThrowInvalidParameterException(restResult);
        exceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
        exceptionHandler.detectAndThrowPropertyServerException(restResult);

        return restResult.getGUID();
    }

    private void callVoidDeleteRESTCall(String userId, String methodName, String urlTemplate, DataEngineOMASAPIRequestBody requestBody,
                                        Object... params) throws PropertyServerException {
        super.callDeleteRESTCall(methodName, VoidResponse.class, serverPlatformRootURL + urlTemplate, requestBody, serverName, userId, params);
    }

    private DeleteRequestBody getDeleteRequestBody(String qualifiedName, String guid) {
        DeleteRequestBody requestBody = new DeleteRequestBody();
        requestBody.setQualifiedName(qualifiedName);
        requestBody.setGuid(guid);
        requestBody.setExternalSourceName(externalSourceName);
        requestBody.setDeleteSemantic(deleteSemantic);

        return requestBody;
    }
}

