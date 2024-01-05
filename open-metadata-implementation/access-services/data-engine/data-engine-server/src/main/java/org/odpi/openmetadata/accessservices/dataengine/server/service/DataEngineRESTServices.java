/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFlow;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DatabaseSchema;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineOMASAPIRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFlowsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseSchemaRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.EventTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessingStateRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.TopicRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineConnectionAndEndpointHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineEventTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFindHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFolderHierarchyHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.PropertiesResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;

/**
 * The DataEngineRESTServices provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This service provide the functionality to create processes, ports with schema types and corresponding
 * relationships.
 */
public class DataEngineRESTServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineRESTServices.class);
    private static final String DEBUG_MESSAGE_METHOD_DETAILS = "Calling method {} for entity: {}";
    private static final String DEBUG_MESSAGE_METHOD_RETURN = "Returning from method: {} with response: {}";
    private static final String EXCEPTION_WHILE_ADDING_DATA_FLOW = "Exception while adding data flow {} : {}";
    private static final String EXCEPTION_WHILE_CREATING_PROCESS = "Exception while creating process {} : {}";
    private static final String EXCEPTION_WHILE_CREATING_PROCESS_HIERARCHY = "Exception while creating process relationships for process {} : {}";
    private static final String DEBUG_DELETE_MESSAGE = "Data Engine OMAS deleted entity with GUID {} and type {}";
    private static final String PROCESS_UPSERT = "Data Engine OMAS has created or updated a Process with qualified name {} and guid {}";
    private static final String EXTERNAL_ENGINE_WAS_REGISTERED =
            "Data Engine OMAS has registered an external engine with qualified name {} and GUID {}";
    private static final String PROCESS_HIERARCHY_ADDED_BETWEEN_CHILD_AND_PARENT_PROCESS =
            "Data Engine OMAS has added a relationship of type ProcessHierarchy between child process {} and parent process {}";
    private static final String CHILD_PROCESS = "childProcess";
    private static final String DATABASE_SCHEMA_PARAMETER_NAME = "databaseSchema";
    private static final String DATABASE_PARAMETER_NAME = "database";
    private static final String RELATIONAL_TABLE_PARAMETER_NAME = "relationalTable";
    private static final String SCHEMA = "Schema";
    private static final String SCHEMA_SUFFIX = "::schema";
    private static final String EXTERNAL_SOURCE_NAME_PARAMETER_NAME = "externalSourceName";
    private static final String UPSERT_METHOD_CALLS_FOR = "Method {} will take longer. Inside it, upsert method will be called for: {} and/or {}";
    private static final String TOPIC_PARAMETER_NAME = "topic";
    private static final String PROCESSING_STATE = "processingState";
    private static final String EVENT_TYPE_PARAMETER_NAME = "eventType";
    private static final String TOPIC_QUALIFIED_NAME_PARAMETER_NAME = "topicQualifiedName";
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private final DataEngineInstanceHandler instanceHandler = new DataEngineInstanceHandler();

    /**
     * Create the external data engine as engine entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the server
     * @return the unique identifier (guid) of the created external data engine
     */
    public GUIDResponse createExternalDataEngine(String serverName, String userId,
                                                 DataEngineRegistrationRequestBody requestBody) {
        final String methodName = "createExternalDataEngine";

        GUIDResponse response = new GUIDResponse();

        try {
            if (requestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }
            response.setGUID(createExternalDataEngine(userId, serverName, requestBody.getEngine()));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);
        return response;
    }

    /**
     * Get the unique identifier from a external data engine qualified name
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the external data engine
     * @return the unique identifier from an engine definition for an external data engine
     */
    public GUIDResponse getExternalDataEngine(String serverName, String userId, String qualifiedName) {
        final String methodName = "getExternalDataEngineByQualifiedName";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, qualifiedName);

        GUIDResponse response = new GUIDResponse();

        try {
            DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

            response.setGUID(handler.getExternalDataEngine(userId, qualifiedName));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);
        return response;
    }

    /**
     * Delete the external data engine. Not yet implemented, it will throw FunctionNotSupportedException if used
     *
     * @param serverName  name of the service to route the request to
     * @param userId      identifier of calling user
     * @param requestBody properties of the external data engine
     * @return void response
     */
    public VoidResponse deleteExternalDataEngine(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteExternalDataEngine";

        VoidResponse response = new VoidResponse();

        try {
            deleteExternalDataEngine(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete the external data engine. Not yet implemented, it will throw FunctionNotSupportedException if used
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the schema type
     * @param qualifiedName      the qualified name of the schema type
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void deleteExternalDataEngine(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                                         DeleteSemantic deleteSemantic) throws InvalidParameterException, UserNotAuthorizedException,
            PropertyServerException,
            FunctionNotSupportedException {
        final String methodName = "deleteExternalDataEngine";

        DataEngineRegistrationHandler dataEngineRegistrationHandler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

        Optional<String> dataEngineGUID = Optional.ofNullable(guid);
        if (dataEngineGUID.isEmpty()) {
            dataEngineGUID = Optional.ofNullable(dataEngineRegistrationHandler.getExternalDataEngine(userId, qualifiedName));
        }

        if (dataEngineGUID.isEmpty()) {
            return;
        }

        dataEngineRegistrationHandler.removeExternalDataEngine(userId, qualifiedName, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, dataEngineGUID, OpenMetadataType.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
    }

    /**
     * Get the unique identifier of an entity
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the port
     * @param typeName      the type name of the entity
     * @return the unique identifier of the entity or empty optional
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<String> getEntityGUID(String serverName, String userId, String qualifiedName, String typeName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        return getEntityDetails(serverName, userId, qualifiedName, typeName).map(InstanceHeader::getGUID);
    }

    /**
     * Get the entity details of an entity
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the port
     * @param typeName      the type name of the entity
     * @return the entity details of the entity or empty optional
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> getEntityDetails(String serverName, String userId, String qualifiedName, String typeName) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "getEntityDetails";
        if (StringUtils.isEmpty(qualifiedName)) {
            return Optional.empty();
        }

        DataEngineCommonHandler dataEngineCommonHandler = instanceHandler.getCommonHandler(userId, serverName, methodName);
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, typeName);
    }

    /**
     * Create the SchemaType with schema attributes and corresponding relationships
     *
     * @param serverName            name of server instance to call
     * @param userId                the name of the calling user
     * @param schemaTypeRequestBody properties of the schema type
     * @return the unique identifier (guid) of the created schema type
     */
    public GUIDResponse upsertSchemaType(String userId, String serverName, SchemaTypeRequestBody schemaTypeRequestBody) {
        final String methodName = "upsertSchemaType";

        GUIDResponse response = new GUIDResponse();

        try {
            validateRequestBody(userId, serverName, schemaTypeRequestBody, methodName);

            String externalSourceName = schemaTypeRequestBody.getExternalSourceName();
            String schemasTypeGUID = upsertSchemaType(userId, serverName, null, schemaTypeRequestBody.getSchemaType(),
                    externalSourceName);

            response.setGUID(schemasTypeGUID);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the SchemaType with schema attributes and corresponding relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the schema type
     * @return void response
     */
    public VoidResponse deleteSchemaType(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteSchemaType";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteSchemaType(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the SchemaType with schema attributes and corresponding relationships
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the schema type
     * @param qualifiedName      the qualified name of the schema type
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteSchemaType(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                                 DeleteSemantic deleteSemantic) throws InvalidParameterException, UserNotAuthorizedException,
            PropertyServerException, FunctionNotSupportedException,
            EntityNotDeletedException {
        final String methodName = "deleteSchemaType";

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        String schemaTypeGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME, methodName);
        dataEngineSchemaTypeHandler.removeSchemaType(userId, schemaTypeGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, schemaTypeGUID, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME);
    }

    /**
     * Create or update the Port Implementation with a PortSchema relationship
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse upsertPortImplementation(String userId, String serverName, PortImplementationRequestBody portImplementationRequestBody) {
        final String methodName = "upsertPortImplementation";

        GUIDResponse response = new GUIDResponse();
        try {
            validateRequestBody(userId, serverName, portImplementationRequestBody, methodName);

            String processGUID = getEntityGUID(serverName, userId, portImplementationRequestBody.getProcessQualifiedName(), OpenMetadataType.PROCESS.typeName)
                    .orElse(null);
            String externalSourceName = portImplementationRequestBody.getExternalSourceName();
            PortImplementation portImplementation = portImplementationRequestBody.getPortImplementation();

            updateProcessStatus(userId, serverName, processGUID, InstanceStatus.DRAFT, externalSourceName);

            String portImplementationGUID = upsertPortImplementation(userId, serverName, portImplementation, processGUID, externalSourceName);
            response.setGUID(portImplementationGUID);
            upsertSchemaType(userId, serverName, portImplementationGUID, portImplementation.getSchemaType(), externalSourceName);

            updateProcessStatus(userId, serverName, processGUID, InstanceStatus.ACTIVE, externalSourceName);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the Port with the associated schema type and relationships
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the port
     * @param portType    the type of the port
     * @return void response
     */
    public VoidResponse deletePort(String userId, String serverName, DeleteRequestBody requestBody, String portType) {
        final String methodName = "deletePort";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deletePort(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(), portType,
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete the  Port with the associated schema type and relationships
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the port
     * @param qualifiedName      the qualified name of the port
     * @param portType           the port type
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deletePort(String userId, String serverName, String externalSourceName, String guid, String qualifiedName, String portType,
                           DeleteSemantic deleteSemantic) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException,
            FunctionNotSupportedException, EntityNotDeletedException {
        final String methodName = "deletePort";

        String portGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.PORT_TYPE_NAME, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        if (OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME.equalsIgnoreCase(portType)) {
            Optional<EntityDetail> schemaType = dataEnginePortHandler.findSchemaTypeForPort(userId, portGUID);
            if (schemaType.isPresent()) {
                deleteSchemaType(userId, serverName, externalSourceName, schemaType.get().getGUID(), null, deleteSemantic);
            }
        }

        dataEnginePortHandler.removePort(userId, portGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, portGUID, OpenMetadataType.PORT_TYPE_NAME);
    }


    /**
     * Add the provided ProcessHierarchy relationship
     *
     * @param serverName                  name of server instance to call
     * @param userId                      the name of the calling user
     * @param processHierarchyRequestBody properties of the process hierarchy
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     */
    public GUIDResponse addProcessHierarchy(String userId, String serverName, ProcessHierarchyRequestBody processHierarchyRequestBody) {
        final String methodName = "addProcessHierarchy";

        GUIDResponse response = new GUIDResponse();

        try {
            validateRequestBody(userId, serverName, processHierarchyRequestBody, methodName);

            response.setGUID(addProcessHierarchyToProcess(userId, serverName, processHierarchyRequestBody.getProcessHierarchy(),
                    processHierarchyRequestBody.getExternalSourceName()));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Create or update the process with ports, schema types and data flows
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param processRequestBody properties of the process
     * @return a list unique identifiers (GUIDs) of the created/updated process
     */
    public GUIDResponse upsertProcess(String userId, String serverName, ProcessRequestBody processRequestBody) {
        final String methodName = "upsertProcess";
        GUIDResponse response = new GUIDResponse();
        try {
            validateRequestBody(userId, serverName, processRequestBody, methodName);

            Process process = processRequestBody.getProcess();
            if (process == null) {
                restExceptionHandler.handleMissingValue("process", methodName);
                return response;
            }
            return upsertProcess(userId, serverName, process, processRequestBody.getExternalSourceName());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete a process, with the associated port implementations and data flows
     *
     * @param userId      the name of the calling user
     * @param serverName  name of server instance to call
     * @param requestBody properties of the processes
     * @return void response
     */
    public VoidResponse deleteProcess(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteProcess";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteProcess(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete a process, with the associated port implementations and data flows
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the process
     * @param qualifiedName      the qualified name of the process
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteProcess(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                              DeleteSemantic deleteSemantic) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException,
            FunctionNotSupportedException, EntityNotDeletedException {
        final String methodName = "deleteProcess";

        String processGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.PROCESS.typeName, methodName);

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

        Set<EntityDetail> portImplementations = processHandler.getPortsForProcess(userId, processGUID, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME);
        for (EntityDetail port : portImplementations) {
            deletePort(userId, serverName, externalSourceName, port.getGUID(), null, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME, deleteSemantic);
        }

        processHandler.removeProcess(userId, processGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, processGUID, OpenMetadataType.PROCESS.typeName);
    }

    /**
     * Add a a ProcessHierarchy relationship to the process
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param processHierarchy   the process hierarchy values
     * @param externalSourceName the unique name of the external source
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String addProcessHierarchyToProcess(String userId, String serverName, ProcessHierarchy processHierarchy, String externalSourceName) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "addProcessHierarchyToProcess";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, processHierarchy);

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

        Optional<EntityDetail> childProcessEntity = processHandler.findProcessEntity(userId, processHierarchy.getChildProcess());

        String childProcessGUID;
        if (childProcessEntity.isPresent()) {
            childProcessGUID = childProcessEntity.get().getGUID();
            ParentProcess parentProcess = new ParentProcess();
            parentProcess.setQualifiedName(processHierarchy.getParentProcess());
            parentProcess.setProcessContainmentType(processHierarchy.getProcessContainmentType());
            processHandler.upsertProcessHierarchyRelationship(userId, parentProcess, childProcessGUID, externalSourceName);
        } else {
            throw new InvalidParameterException(DataEngineErrorCode.PROCESS_NOT_FOUND.getMessageDefinition(processHierarchy.getChildProcess()),
                    this.getClass().getName(), methodName, CHILD_PROCESS);
        }

        log.info(PROCESS_HIERARCHY_ADDED_BETWEEN_CHILD_AND_PARENT_PROCESS, processHierarchy.getChildProcess(),
                processHierarchy.getParentProcess());

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, childProcessGUID);
        return childProcessGUID;
    }

    /**
     * Create or update a Port Implementation with an associated SchemaType
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param portImplementation the port implementation values
     * @param processGUID        the unique identifier of the process
     * @param externalSourceName the unique name of the external source
     * @return the unique identifier (guid) of the created port implementation
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public String upsertPortImplementation(String userId, String serverName, PortImplementation portImplementation, String processGUID,
                                           String externalSourceName) throws InvalidParameterException, PropertyServerException,
            UserNotAuthorizedException, FunctionNotSupportedException {
        final String methodName = "upsertPortImplementation";
        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, portImplementation);

        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Optional<EntityDetail> portEntity = dataEnginePortHandler.findPortImplementationEntity(userId, portImplementation.getQualifiedName());
        String portImplementationGUID;
        if (portEntity.isEmpty()) {
            portImplementationGUID = dataEnginePortHandler.createPortImplementation(userId, portImplementation, processGUID, externalSourceName);
        } else {
            portImplementationGUID = portEntity.get().getGUID();
            dataEnginePortHandler.updatePortImplementation(userId, portEntity.get(), portImplementation, externalSourceName);

            if (portImplementation.getUpdateSemantic() == UpdateSemantic.REPLACE) {
                Optional<EntityDetail> schemaTypeForPort = dataEnginePortHandler.findSchemaTypeForPort(userId, portImplementationGUID);
                if (schemaTypeForPort.isPresent()) {
                    String oldSchemaTypeQualifiedName =
                            schemaTypeForPort.get().getProperties().getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name).valueAsString();
                    deleteObsoleteSchemaType(userId, serverName, portImplementation.getSchemaType().getQualifiedName(), oldSchemaTypeQualifiedName,
                            externalSourceName);
                }
            }
        }

        log.trace(DEBUG_MESSAGE_METHOD_RETURN, methodName, portImplementationGUID);
        return portImplementationGUID;
    }

    /**
     * Create the external data engine as engine entity
     *
     * @param userId     the name of the calling user
     * @param serverName name of server instance to call
     * @param engine     the engine values
     * @return he unique identifier (guid) of the created external data engine
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createExternalDataEngine(String userId, String serverName, Engine engine) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "createExternalDataEngine";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, engine);

        if (engine == null) {
            return null;
        }

        DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

        String externalDataEngineGUID = handler.upsertExternalDataEngine(userId, engine);

        log.info(EXTERNAL_ENGINE_WAS_REGISTERED, engine.getQualifiedName(), externalDataEngineGUID);
        return externalDataEngineGUID;
    }

    /**
     * Create DataFlows relationships between schema attributes
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param dataFlows          the list of daa flows to be created
     * @param response           the response object that will capture the exceptions that might occur during
     *                           parallel processing
     * @param externalSourceName the unique name of the external source
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addDataFlows(String userId, String serverName, List<DataFlow> dataFlows, FFDCResponseBase response,
                             String externalSourceName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "addDataFlows";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, dataFlows);

        if (CollectionUtils.isEmpty(dataFlows)) {
            return;
        }

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        dataFlows.parallelStream().forEach(dataFlow -> {
            try {
                dataEngineSchemaTypeHandler.addDataFlowRelationship(userId, dataFlow.getDataSupplier(),
                        dataFlow.getDataConsumer(), externalSourceName, dataFlow.getFormula(), dataFlow.getDescription());
            } catch (Exception error) {
                log.error(EXCEPTION_WHILE_ADDING_DATA_FLOW, dataFlow.toString(), error.toString());
                restExceptionHandler.captureExceptions(response, error, methodName);
            }
        });
    }

    /**
     * Create DataFlow relationships between schema attributes
     *
     * @param userId               the name of the calling user
     * @param serverName           ame of server instance to call
     * @param dataFlowsRequestBody list of data flows
     * @return void response
     */
    public VoidResponse addDataFlows(String userId, String serverName, DataFlowsRequestBody dataFlowsRequestBody) {
        final String methodName = "addDataFlows";

        VoidResponse response = new VoidResponse();
        try {
            if (dataFlowsRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            addDataFlows(userId, serverName, dataFlowsRequestBody.getDataFlows(), response,
                    dataFlowsRequestBody.getExternalSourceName());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

        return response;
    }

    /**
     * Retrieve in topic connection details from the service instance hosting Data Engine access service
     *
     * @param serverName the name of server instance to call
     * @param userId     the name/identifier of the calling user
     * @return OCF API ConnectionResponse object describing the details for the input topic connection used
     * or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition
     */
    public ConnectionResponse getInTopicConnection(String serverName, String userId) {

        final String methodName = "getInTopicConnection";
        ConnectionResponse response = new ConnectionResponse();

        try {
            response.setConnection(instanceHandler.getInTopicConnection(userId, serverName, methodName));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }


    /**
     * Create or update a SchemaType
     *
     * @param userId                 the name of the calling user
     * @param serverName             name of server instance to call
     * @param portImplementationGUID the unique identifier of the port implementation
     * @param schemaType             the schema type values
     * @param externalSourceName     the unique name of the external source
     * @return the unique identifier (guid) of the created schema type
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertSchemaType(String userId, String serverName, String portImplementationGUID, SchemaType schemaType,
                                   String externalSourceName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "upsertSchemaType";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, schemaType);

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String schemaTypeGUID = dataEngineSchemaTypeHandler.upsertSchemaType(userId, schemaType, portImplementationGUID, externalSourceName);
        if (StringUtils.isNotEmpty(portImplementationGUID)) {
            dataEnginePortHandler.addPortSchemaRelationship(userId, portImplementationGUID, schemaTypeGUID, methodName);
        }
        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, schemaTypeGUID);

        return schemaTypeGUID;
    }

    /**
     * Create or update the Database with corresponding associated schema type and relationship
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param databaseRequestBody properties of the database
     * @return the unique identifier (guid) of the created database
     */
    public GUIDResponse upsertDatabase(String userId, String serverName, DatabaseRequestBody databaseRequestBody) {
        final String methodName = "upsertDatabase";

        GUIDResponse response = new GUIDResponse();
        try {
            validateDatabaseRequestBody(userId, serverName, databaseRequestBody, methodName);

            String databaseGUID = upsertDatabase(userId, serverName, databaseRequestBody.getDatabase(), databaseRequestBody.getExternalSourceName());
            response.setGUID(databaseGUID);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Create or update the Database with corresponding associated schema type and relationship
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param database           the database values
     * @param externalSourceName the unique name of the external source
     * @return the unique identifier (guid) of the created database
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDatabase(String userId, String serverName, Database database, String externalSourceName) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        final String methodName = "upsertDatabase";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, database);
        DatabaseSchema databaseSchema = database.getDatabaseSchema();
        List<RelationalTable> tables = database.getTables();
        if (databaseSchema != null || CollectionUtils.isNotEmpty(tables)) {
            log.debug(UPSERT_METHOD_CALLS_FOR, methodName, databaseSchema, tables);
        }

        DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);
        String databaseGUID = dataEngineRelationalDataHandler.upsertDatabase(userId, database, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, databaseGUID);
        return databaseGUID;
    }

    /**
     * Create or update the Database Schema with corresponding related entities and classifications
     *
     * @param serverName                name of server instance to call
     * @param userId                    the name of the calling user
     * @param databaseSchemaRequestBody RequestBody properties of the database
     * @return the unique identifier (guid) of the created database
     */
    public GUIDResponse upsertDatabaseSchema(String userId, String serverName, DatabaseSchemaRequestBody databaseSchemaRequestBody) {
        final String methodName = "upsertDatabaseSchema";

        GUIDResponse response = new GUIDResponse();
        try {
            validateDatabaseSchemaRequestBody(userId, serverName, databaseSchemaRequestBody, methodName);

            DatabaseSchema databaseSchema = databaseSchemaRequestBody.getDatabaseSchema();
            String databaseGUID = upsertDatabaseSchema(userId, serverName, databaseSchemaRequestBody.getDatabaseQualifiedName(), databaseSchema,
                    databaseSchemaRequestBody.getExternalSourceName());
            response.setGUID(databaseGUID);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Create or update the DatabaseSchema with corresponding relationship to the database, if provided and not virtual
     *
     * @param userId                the name of the calling user
     * @param serverName            name of server instance to call
     * @param databaseQualifiedName the database entity to which the database schema will be linked, if it exists
     * @param databaseSchema        the database schema values
     * @param externalSourceName    the unique name of the external source
     * @return the unique identifier (guid) of the created database schema
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDatabaseSchema(String userId, String serverName, String databaseQualifiedName, DatabaseSchema databaseSchema,
                                       String externalSourceName) throws InvalidParameterException, UserNotAuthorizedException,
            PropertyServerException {

        final String methodName = "upsertDatabaseSchema";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, databaseSchema);

        DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId,
                serverName, methodName);

        Optional<EntityDetail> databaseEntityOptional = getEntityDetails(serverName, userId, databaseQualifiedName, OpenMetadataType.DATABASE_TYPE_NAME);
        String databaseGUID = null;
        if (databaseEntityOptional.isPresent()) {
            databaseGUID = databaseEntityOptional.get().getGUID();
        }

        String databaseSchemaGUID = dataEngineRelationalDataHandler.upsertDatabaseSchema(userId, databaseGUID, databaseSchema, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, databaseSchemaGUID);
        return databaseSchemaGUID;
    }

    /**
     * Delete the Database with all the associated relational tables
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the database
     * @return void response
     */
    public VoidResponse deleteDatabase(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteDatabase";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteDatabase(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the Database with all the associated relational tables
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the database
     * @param qualifiedName      the qualified name of the database
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteDatabase(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                               DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
            EntityNotDeletedException, FunctionNotSupportedException {

        final String methodName = "deleteDatabase";

        DataEngineRelationalDataHandler relationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

        String databaseGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.DATABASE_TYPE_NAME, methodName);
        relationalDataHandler.removeDatabase(userId, databaseGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, databaseGUID, OpenMetadataType.DATABASE_TYPE_NAME);
    }

    /**
     * Delete the DatabaseSchema with all the associated relational tables
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the database schema
     * @return void response
     */
    public VoidResponse deleteDatabaseSchema(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteDatabaseSchema";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteDatabaseSchema(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(),
                    requestBody.getQualifiedName(), requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the DatabaseSchema with all the associated relational tables
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the database schema
     * @param qualifiedName      the qualified name of the database schema
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteDatabaseSchema(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                                     DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException,
            UserNotAuthorizedException,
            EntityNotDeletedException, FunctionNotSupportedException {

        final String methodName = "deleteDatabaseSchema";

        DataEngineRelationalDataHandler relationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);
        String databaseSchemaGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, methodName);
        relationalDataHandler.removeDatabaseSchema(userId, databaseSchemaGUID, externalSourceName, deleteSemantic);

        log.debug(DEBUG_DELETE_MESSAGE, databaseSchemaGUID, OpenMetadataType.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME);
    }

    /**
     * Create the Relational Table with Relational Columns and corresponding relationships
     *
     * @param serverName                 name of server instance to call
     * @param userId                     the name of the calling user
     * @param relationalTableRequestBody properties of the relational table
     * @return the unique identifier (guid) of the created relational table
     */
    public GUIDResponse upsertRelationalTable(String userId, String serverName, RelationalTableRequestBody relationalTableRequestBody) {
        final String methodName = "upsertRelationalTable";

        GUIDResponse response = new GUIDResponse();

        try {
            validateRelationalTableRequestBody(userId, serverName, relationalTableRequestBody, methodName);

            String relationalTableGUID = upsertRelationalTable(userId, serverName, relationalTableRequestBody.getDatabaseSchemaQualifiedName(),
                    relationalTableRequestBody.getRelationalTable(), relationalTableRequestBody.getExternalSourceName());
            response.setGUID(relationalTableGUID);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Create the Relational Table with Relational Columns and corresponding relationships
     *
     * @param userId                      the name of the calling user
     * @param serverName                  name of server instance to call
     * @param databaseSchemaQualifiedName the unique name of the database
     * @param relationalTable             the relational table values
     * @param externalSourceName          the unique name of the external source
     * @return the unique identifier (guid) of the created relational table
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertRelationalTable(String userId, String serverName, String databaseSchemaQualifiedName, RelationalTable relationalTable,
                                        String externalSourceName) throws InvalidParameterException,
            UserNotAuthorizedException, PropertyServerException {
        final String methodName = "upsertRelationalTable";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, relationalTable);

        DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId,
                serverName, methodName);

        String relationalTableGUID = dataEngineRelationalDataHandler.upsertRelationalTable(userId, databaseSchemaQualifiedName,
                relationalTable, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, relationalTableGUID);
        return relationalTableGUID;
    }

    /**
     * Delete the Relational Table with all the associated Relational Columns
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the relational table
     * @return void response
     */
    public VoidResponse deleteRelationalTable(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteRelationalTable";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteRelationalTable(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the Relational Table with all the associated Relational Columns
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the relational table
     * @param qualifiedName      the qualified name of the relational table
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteRelationalTable(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                                      DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException,
            UserNotAuthorizedException, EntityNotDeletedException,
            FunctionNotSupportedException {

        final String methodName = "deleteRelationalTable";

        DataEngineRelationalDataHandler relationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);

        String relationalTableGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME, methodName);
        relationalDataHandler.removeRelationalTable(userId, relationalTableGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, relationalTableGUID, OpenMetadataType.RELATIONAL_TABLE_TYPE_NAME);
    }


    /**
     * Updates or inserts a DataFile or CSVFile, along with its schema, columns and folder hierarchy
     *
     * @param userId              the name of the calling user
     * @param serverName          name of server instance to call
     * @param dataFileRequestBody properties of the data file
     * @return the unique identifier (guid) of the created data file
     */
    public GUIDResponse upsertDataFile(String serverName, String userId, DataFileRequestBody dataFileRequestBody) {
        String methodName = "upsertDataFile";
        GUIDResponse response = new GUIDResponse();
        String guid;

        try {
            validateRequestBody(userId, serverName, dataFileRequestBody, methodName);

            guid = upsertDataFile(userId, serverName, dataFileRequestBody.getDataFile(), dataFileRequestBody.getExternalSourceName());
            response.setGUID(guid);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Updates or inserts a DataFile or CSVFile, along with its schema, columns and folder hierarchy
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param file               the data file properties
     * @param externalSourceName the unique name of the external source
     * @return the unique identifier (guid) of the created data file
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDataFile(String userId, String serverName, DataFile file, String externalSourceName) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        String methodName = "upsertDataFile";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, file);

        DataEngineDataFileHandler dataFileHandler = instanceHandler.getDataFileHandler(userId, serverName, methodName);
        DataEngineRegistrationHandler registrationHandler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

        String externalSourceGuid = registrationHandler.getExternalDataEngine(userId, externalSourceName);

        List<Attribute> columns = file.getColumns();
        SchemaType schemaType = getDefaultSchemaTypeIfAbsentAndAddAttributes(file, file.getSchema(), columns);

        Map<String, Object> extendedProperties = getExtendedProperties(file);
        String fileTypeGuid = file instanceof CSVFile ? OpenMetadataType.CSV_FILE_TYPE_GUID : OpenMetadataType.DATA_FILE_TYPE_GUID;
        String fileTypeName = file instanceof CSVFile ? OpenMetadataType.CSV_FILE_TYPE_NAME : OpenMetadataType.DATA_FILE_TYPE_NAME;
        file.setFileType(fileTypeName);

        if (CollectionUtils.isNotEmpty(columns)) {
            columns.forEach(column -> {
                column.setTypeName(OpenMetadataType.TABULAR_FILE_COLUMN_TYPE_NAME);
                column.setTypeGuid(OpenMetadataType.TABULAR_FILE_COLUMN_TYPE_GUID);
            });
        }

        String guid = dataFileHandler.upsertFileAssetIntoCatalog(fileTypeName, fileTypeGuid, file, schemaType,
                extendedProperties, externalSourceGuid, externalSourceName, userId, methodName);
        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, guid);
        return guid;
    }

    /**
     * Delete the Data File with all the associated Tabular Columns
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the data file
     * @return void response
     */
    public VoidResponse deleteDataFile(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteDataFile";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteDataFile(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the Data File with all the associated Tabular Columns
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the data file
     * @param qualifiedName      the qualified name of the data file
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteDataFile(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                               DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, EntityNotDeletedException,
            UserNotAuthorizedException, FunctionNotSupportedException {
        final String methodName = "deleteDataFile";

        DataEngineDataFileHandler dataFileHandler = instanceHandler.getDataFileHandler(userId, serverName, methodName);
        DataEngineRegistrationHandler registrationHandler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

        String dataFileGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.DATA_FILE_TYPE_NAME, methodName);

        String externalSourceGuid = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        dataFileHandler.removeDataFile(userId, dataFileGUID, externalSourceName, externalSourceGuid, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, dataFileGUID, OpenMetadataType.DATA_FILE_TYPE_NAME);
    }

    /**
     * Delete the File Folder
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the folder
     * @return void response
     */
    public VoidResponse deleteFolder(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteFolder";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteFolder(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the File Folder
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the folder
     * @param qualifiedName      the qualified name of the folder
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteFolder(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                             DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
            EntityNotDeletedException, FunctionNotSupportedException {

        final String methodName = "deleteFolder";
        String folderGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.FILE_FOLDER_TYPE_NAME, methodName);

        DataEngineFolderHierarchyHandler folderHierarchyHandler = instanceHandler.getFolderHierarchyHandler(userId, serverName, methodName);
        folderHierarchyHandler.removeFolder(userId, folderGUID, deleteSemantic, externalSourceName);

        log.debug(DEBUG_DELETE_MESSAGE, folderGUID, OpenMetadataType.FILE_FOLDER_TYPE_NAME);
    }

    /**
     * Delete the Connection
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the connection
     * @return void response
     */
    public VoidResponse deleteConnection(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteConnection";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteConnection(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the Connection
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the connection
     * @param qualifiedName      the qualified name of the connection
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteConnection(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                                 DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, EntityNotDeletedException,
            UserNotAuthorizedException, FunctionNotSupportedException {

        final String methodName = "deleteConnection";
        String connectionGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.CONNECTION_TYPE_NAME, methodName);

        DataEngineConnectionAndEndpointHandler connectionAndEndpointHandler = instanceHandler.getConnectionAndEndpointHandler(userId, serverName,
                methodName);

        DataEngineRegistrationHandler registrationHandler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);
        String externalSourceGuid = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        connectionAndEndpointHandler.removeConnection(userId, connectionGUID, deleteSemantic, externalSourceName, externalSourceGuid);

        log.debug(DEBUG_DELETE_MESSAGE, connectionGUID, OpenMetadataType.CONNECTION_TYPE_NAME);
    }

    /**
     * Delete the Endpoint
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the endpoint
     * @return void response
     */
    public VoidResponse deleteEndpoint(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteEndpoint";

        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteEndpoint(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Delete the Endpoint
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the endpoint
     * @param qualifiedName      the qualified name of the endpoint
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteEndpoint(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                               DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, EntityNotDeletedException,
            UserNotAuthorizedException, FunctionNotSupportedException {

        final String methodName = "deleteEndpoint";
        String endpointGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.ENDPOINT_TYPE_NAME, methodName);

        DataEngineConnectionAndEndpointHandler connectionAndEndpointHandler = instanceHandler.getConnectionAndEndpointHandler(userId, serverName,
                methodName);
        DataEngineRegistrationHandler registrationHandler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);
        String externalSourceGuid = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        connectionAndEndpointHandler.removeEndpoint(userId, endpointGUID, deleteSemantic, externalSourceName, externalSourceGuid);

        log.debug(DEBUG_DELETE_MESSAGE, endpointGUID, OpenMetadataType.ENDPOINT_TYPE_NAME);
    }

    private String getEntityGUID(String userId, String serverName, String guid, String qualifiedName, String entityTypeName, String methodName) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            EntityNotDeletedException {
        Optional<String> entityGUIDOptional = Optional.ofNullable(guid);
        if (entityGUIDOptional.isEmpty()) {
            entityGUIDOptional = getEntityGUID(serverName, userId, qualifiedName, entityTypeName);
        }
        if (entityGUIDOptional.isPresent()) {
            return entityGUIDOptional.get();
        } else {
            throwEntityNotDeletedException(userId, serverName, methodName, qualifiedName);
        }
        return null;
    }

    private void deleteObsoleteSchemaType(String userId, String serverName, String schemaTypeQualifiedName, String oldSchemaTypeQualifiedName,
                                          String externalSourceName) throws InvalidParameterException, UserNotAuthorizedException,
            PropertyServerException, FunctionNotSupportedException {
        final String methodName = "deleteObsoleteSchemaType";

        if (oldSchemaTypeQualifiedName.equalsIgnoreCase(schemaTypeQualifiedName)) {
            return;
        }

        Optional<String> schemaTypeGUID = getEntityGUID(serverName, userId, oldSchemaTypeQualifiedName, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME);
        if (schemaTypeGUID.isEmpty()) {
            return;
        }

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);
        dataEngineSchemaTypeHandler.removeSchemaType(userId, schemaTypeGUID.get(), externalSourceName, DeleteSemantic.SOFT);
    }

    /**
     * Update the process status
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param processGUID        the GUID of the process
     * @param instanceStatus     the {@link org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus}
     * @param externalSourceName the name of the external source
     * @return void response
     */
    public VoidResponse updateProcessStatus(String userId, String serverName, String processGUID, InstanceStatus instanceStatus,
                                            String externalSourceName) {
        final String methodName = "updateProcessStatus";

        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, processGUID);

        VoidResponse response = new VoidResponse();
        try {
            DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            processHandler.updateProcessStatus(userId, processGUID, instanceStatus, externalSourceName);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        log.trace(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

        return response;
    }

    /**
     * Create the process with ports, schema types and data flows
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param process            properties of the process
     * @param externalSourceName the name of the external source
     * @return the unique identifier (guid) of the created process
     */
    public GUIDResponse upsertProcess(String userId, String serverName, Process process, String externalSourceName) {
        final String methodName = "upsertProcess";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, process);

        String qualifiedName = process.getQualifiedName();
        List<PortImplementation> portImplementations = process.getPortImplementations();
        UpdateSemantic updateSemantic = process.getUpdateSemantic();

        GUIDResponse response = new GUIDResponse();

        try {
            DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            Optional<EntityDetail> processEntity = processHandler.findProcessEntity(userId, qualifiedName);
            String processGUID;
            if (processEntity.isEmpty()) {
                processGUID = processHandler.createProcess(userId, process, externalSourceName);
            } else {
                processGUID = processEntity.get().getGUID();
                processHandler.updateProcess(userId, processEntity.get(), process, externalSourceName);
                processHandler.updateProcessStatus(userId, processGUID, InstanceStatus.DRAFT, externalSourceName);

                if (updateSemantic == UpdateSemantic.REPLACE) {
                    deleteObsoletePorts(userId, serverName, portImplementations, processGUID, response,
                            externalSourceName);
                }
            }

            String collectionGUID = createCollection(userId, serverName, process.getCollection(), externalSourceName);
            if (collectionGUID != null) {
                addProcessCollectionRelationship(userId, serverName, processGUID, collectionGUID, externalSourceName);
            }

            upsertPortImplementations(userId, serverName, portImplementations, processGUID, response, externalSourceName);

            if (response.getRelatedHTTPCode() == HttpStatus.OK.value()) {
                processHandler.updateProcessStatus(userId, processGUID, InstanceStatus.ACTIVE, externalSourceName);
                addProcessHierarchyRelationships(userId, serverName, process, processGUID, response, externalSourceName);
            }

            log.info(PROCESS_UPSERT, qualifiedName, processGUID);
            response.setGUID(processGUID);
        } catch (Exception error) {
            log.error(EXCEPTION_WHILE_CREATING_PROCESS, qualifiedName, error.toString());
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

        return response;
    }

    private String createCollection(String userId, String serverName, Collection collection, String externalSourceName) throws
            UserNotAuthorizedException,
            PropertyServerException,
            InvalidParameterException {
        final String methodName = "createCollection";
        DataEngineCollectionHandler dataEngineCollectionHandler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

        if (collection == null) {
            return null;
        }

        String collectionGUID;
        String collectionQualifiedName = collection.getQualifiedName();
        Optional<EntityDetail> collectionEntity = dataEngineCollectionHandler.findCollectionEntity(userId, collectionQualifiedName);
        if (collectionEntity.isEmpty()) {
            collectionGUID = dataEngineCollectionHandler.createCollection(userId, collection, externalSourceName);
        } else {
            collectionGUID = collectionEntity.get().getGUID();
        }
        return collectionGUID;
    }

    private void addProcessHierarchyRelationships(String userId, String serverName, Process process, String processGUID, GUIDResponse response,
                                                  String externalSourceName) {
        final String methodName = "addProcessHierarchyRelationships";

        List<ParentProcess> parentProcesses = process.getParentProcesses();
        if (CollectionUtils.isNotEmpty(parentProcesses)) {
            try {
                DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
                for (ParentProcess parentProcess : parentProcesses) {
                    processHandler.upsertProcessHierarchyRelationship(userId, parentProcess, processGUID, externalSourceName);
                }
            } catch (Exception error) {
                log.error(EXCEPTION_WHILE_CREATING_PROCESS_HIERARCHY, process.getQualifiedName(), error.toString());
                restExceptionHandler.captureExceptions(response, error, methodName);
            }
        }
    }

    private void addProcessCollectionRelationship(String userId, String serverName, String processGUID, String collectionGUID,
                                                  String externalSourceName) throws InvalidParameterException, PropertyServerException,
            UserNotAuthorizedException {

        final String methodName = "addProcessCollectionRelationship";

        DataEngineCollectionHandler dataEngineCollectionHandler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

        dataEngineCollectionHandler.addCollectionMembershipRelationship(userId, collectionGUID, processGUID, externalSourceName);
    }

    private void deleteObsoletePorts(String userId, String serverName, List<? extends Port> ports, String processGUID,
                                     GUIDResponse response, String externalSourceName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "deleteObsoletePorts";
        if (CollectionUtils.isEmpty(ports)) {
            return;
        }

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Set<EntityDetail> existingPorts = processHandler.getPortsForProcess(userId, processGUID, OpenMetadataType.PORT_IMPLEMENTATION_TYPE_NAME);
        Set<String> portQualifiedNames = existingPorts.stream()
                .map(entityDetail -> entityDetail.getProperties().getPropertyValue(OpenMetadataProperty.QUALIFIED_NAME.name).valueAsString())
                .collect(Collectors.toSet());
        Set<String> newPortQualifiedNames = ports.stream().map(Referenceable::getQualifiedName).collect(Collectors.toSet());

        // delete ports that are not in the process payload anymore
        List<String> obsoletePortQualifiedNames =
                portQualifiedNames.stream().collect(partitioningBy(newPortQualifiedNames::contains)).get(Boolean.FALSE);
        obsoletePortQualifiedNames.forEach(portQualifiedName -> {
            try {
                Optional<String> portGUID = getEntityGUID(serverName, userId, portQualifiedName, OpenMetadataType.PORT_TYPE_NAME);
                if (portGUID.isPresent()) {
                    dataEnginePortHandler.removePort(userId, portGUID.get(), externalSourceName, DeleteSemantic.SOFT);
                }
            } catch (Exception error) {
                restExceptionHandler.captureExceptions(response, error, methodName);
            }
        });
    }


    private void upsertPortImplementations(String userId, String serverName, List<PortImplementation> portImplementations, String processGUID,
                                           GUIDResponse response, String externalSourceName) {
        final String methodName = "upsertPortImplementations";
        if (CollectionUtils.isEmpty(portImplementations)) {
            return;
        }

        Map<String, SchemaType> schemaTypeMap = new HashMap<>();
        // first create port implementations sequentially
        try {
            for (PortImplementation portImplementation : portImplementations) {
                if (portImplementation == null) {
                    continue;
                }
                String portGUID = upsertPortImplementation(userId, serverName, portImplementation, processGUID, externalSourceName);
                schemaTypeMap.put(portGUID, portImplementation.getSchemaType());
            }
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        // then create the schema types with attributes in parallel
        schemaTypeMap.keySet().parallelStream().forEach(portGUID ->
        {
            try {
                upsertSchemaType(userId, serverName, portGUID, schemaTypeMap.get(portGUID), externalSourceName);
            } catch (Exception error) {
                restExceptionHandler.captureExceptions(response, error, methodName);
            }
        });
    }

    private void validateDatabaseRequestBody(String userId, String serverName, DatabaseRequestBody databaseRequestBody, String methodName) throws
            InvalidParameterException {
        validateRequestBody(userId, serverName, databaseRequestBody, methodName);

        if (databaseRequestBody.getDatabase() == null) {
            restExceptionHandler.handleMissingValue(DATABASE_PARAMETER_NAME, methodName);
        }
    }

    private void validateDatabaseSchemaRequestBody(String userId, String serverName, DatabaseSchemaRequestBody databaseSchemaRequestBody,
                                                   String methodName) throws InvalidParameterException {
        validateRequestBody(userId, serverName, databaseSchemaRequestBody, methodName);

        if (databaseSchemaRequestBody.getDatabaseSchema() == null) {
            restExceptionHandler.handleMissingValue(DATABASE_SCHEMA_PARAMETER_NAME, methodName);
        }
    }

    private void validateRelationalTableRequestBody(String userId, String serverName, RelationalTableRequestBody relationalTableRequestBody,
                                                    String methodName) throws InvalidParameterException {
        validateRequestBody(userId, serverName, relationalTableRequestBody, methodName);

        if (relationalTableRequestBody.getRelationalTable() == null) {
            restExceptionHandler.handleMissingValue(RELATIONAL_TABLE_PARAMETER_NAME, methodName);
        }

    }

    private void validateRequestBody(String userId, String serverName, DataEngineOMASAPIRequestBody requestBody, String methodName)
            throws InvalidParameterException {
        if (requestBody == null) {
            restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
        }
        if (StringUtils.isEmpty(requestBody.getExternalSourceName())) {
            restExceptionHandler.handleMissingValue(EXTERNAL_SOURCE_NAME_PARAMETER_NAME, methodName);
        }
    }

    private SchemaType getDefaultSchemaTypeIfAbsentAndAddAttributes(DataFile file, SchemaType schemaType, List<Attribute> attributes) {
        if (schemaType == null) {
            schemaType = new SchemaType();
            schemaType.setQualifiedName(file.getQualifiedName() + SCHEMA_SUFFIX);
            schemaType.setDisplayName(SCHEMA);
        }
        schemaType.setAttributeList(attributes);
        return schemaType;
    }

    private HashMap<String, Object> getExtendedProperties(DataFile file) {
        HashMap<String, Object> extendedProperties = new HashMap<>();

        if (file instanceof CSVFile) {
            CSVFile csvFile = (CSVFile) file;
            extendedProperties.put(OpenMetadataType.FILE_TYPE_PROPERTY_NAME, csvFile.getFileType());
            extendedProperties.put(OpenMetadataType.DELIMITER_CHARACTER_PROPERTY_NAME, csvFile.getDelimiterCharacter());
            extendedProperties.put(OpenMetadataType.QUOTE_CHARACTER_PROPERTY_NAME, csvFile.getQuoteCharacter());
        } else {
            extendedProperties.put(OpenMetadataType.FILE_TYPE_PROPERTY_NAME, file.getFileType());
        }
        return extendedProperties;
    }

    private void throwEntityNotDeletedException(String userId, String serverName, String methodName, String qualifiedName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException,
            EntityNotDeletedException {
        DataEngineCommonHandler dataEngineCommonHandler = instanceHandler.getCommonHandler(userId, serverName, methodName);
        dataEngineCommonHandler.throwEntityNotDeletedException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, qualifiedName);
    }

    /**
     * Performs a find for a DataEngine related object
     *
     * @param userId          user id
     * @param serverName      server name
     * @param findRequestBody contains find criteria
     * @return a list of GUIDs
     */
    public GUIDListResponse find(String userId, String serverName, FindRequestBody findRequestBody) {

        String methodName = "find";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, findRequestBody);

        GUIDListResponse findResponse = new GUIDListResponse();
        try {
            DataEngineFindHandler findHandler = instanceHandler.getFindHandler(userId, serverName, methodName);
            findResponse = findHandler.find(findRequestBody, userId, methodName);
        } catch (Exception e) {
            restExceptionHandler.captureExceptions(findResponse, e, methodName);
        }

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, findResponse);
        return findResponse;
    }

    /**
     * Create or update the Topic with corresponding event types and relationship
     *
     * @param serverName       name of server instance to call
     * @param userId           the name of the calling user
     * @param topicRequestBody properties of the topic
     * @return the unique identifier (guid) of the created topic
     */
    public GUIDResponse upsertTopic(String userId, String serverName, TopicRequestBody topicRequestBody) {
        final String methodName = "upsertTopic";

        GUIDResponse response = new GUIDResponse();
        try {
            if (!isTopicRequestBodyValid(userId, serverName, topicRequestBody, methodName)) return response;

            String topicGUID = upsertTopic(userId, serverName, topicRequestBody.getTopic(), topicRequestBody.getExternalSourceName());
            response.setGUID(topicGUID);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Create or update the Topic with corresponding event types and relationship
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param topic              the topic values
     * @param externalSourceName the unique name of the external source
     * @return the unique identifier (guid) of the created topic
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertTopic(String userId, String serverName, Topic topic, String externalSourceName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "upsertTopic";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, topic);

        DataEngineTopicHandler dataEngineTopicHandler = instanceHandler.getTopicHandler(userId, serverName, methodName);
        String topicGUID = dataEngineTopicHandler.upsertTopic(userId, topic, externalSourceName);

        upsertEventTypes(userId, serverName, topic.getEventTypes(), topicGUID, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, topicGUID);
        return topicGUID;
    }

    /**
     * /**
     * Create or update a list of EventTypes with corresponding event schema attributes
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param eventTypes         the event type list
     * @param topicGUID          the unique identifier of the topic
     * @param externalSourceName the unique name of the external source
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void upsertEventTypes(String userId, String serverName, List<EventType> eventTypes, String topicGUID, String externalSourceName) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        if (CollectionUtils.isEmpty(eventTypes)) {
            return;
        }
        for (EventType eventType : eventTypes) {
            upsertEventType(userId, serverName, eventType, topicGUID, externalSourceName);
        }
    }

    /**
     * Delete the Topic with all the associated event types
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the topic
     * @return void response
     */
    public VoidResponse deleteTopic(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteTopic";
        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteTopic(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete the Topic with all the associated event types
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the topic
     * @param qualifiedName      the qualified name of the topic
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteTopic(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                            DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
            EntityNotDeletedException, FunctionNotSupportedException {
        final String methodName = "deleteTopic";

        DataEngineTopicHandler dataEngineTopicHandler = instanceHandler.getTopicHandler(userId, serverName, methodName);

        String topicGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.TOPIC_TYPE_NAME, methodName);
        dataEngineTopicHandler.removeTopic(userId, topicGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, topicGUID, OpenMetadataType.TOPIC_TYPE_NAME);
    }

    /**
     * Create or update the EventType with corresponding associated event schema attributes
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param eventTypeRequestBody properties of the event type
     * @return the unique identifier (guid) of the created event type
     */
    public GUIDResponse upsertEventType(String userId, String serverName, EventTypeRequestBody eventTypeRequestBody) {
        final String methodName = "upsertEventType";

        GUIDResponse response = new GUIDResponse();
        try {
            if (!isEventTypeRequestBodyValid(userId, serverName, eventTypeRequestBody, methodName)) return response;

            String topicGUID = getTopicGUID(userId, serverName, eventTypeRequestBody.getTopicQualifiedName(), methodName);

            String eventTypeGUID = upsertEventType(userId, serverName, eventTypeRequestBody.getEventType(),
                    topicGUID, eventTypeRequestBody.getExternalSourceName());
            response.setGUID(eventTypeGUID);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Get the unique identifier of a topic
     *
     * @param serverName         name of the service to route the request to
     * @param userId             identifier of calling user
     * @param topicQualifiedName qualified name of the topic
     * @param methodName         the name of the calling method
     * @return the unique identifier of the entity
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String getTopicGUID(String userId, String serverName, String topicQualifiedName, String methodName) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        DataEngineTopicHandler dataEngineTopicHandler = instanceHandler.getTopicHandler(userId, serverName, methodName);
        DataEngineCommonHandler dataEngineCommonHandler = instanceHandler.getCommonHandler(userId, serverName, methodName);
        Optional<EntityDetail> topicEntity = dataEngineTopicHandler.findTopicEntity(userId, topicQualifiedName);
        if (topicEntity.isEmpty()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.TOPIC_NOT_FOUND, methodName, topicQualifiedName);
        }
        return topicEntity.get().getGUID();
    }

    /**
     * Create or update the EventType with corresponding event schema attributes
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param eventType          the event type values
     * @param topicGUID          the unique identifier of the topic
     * @param externalSourceName the unique name of the external source
     * @return the unique identifier (guid) of the created event type
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertEventType(String userId, String serverName, EventType eventType, String topicGUID, String externalSourceName) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "upsertEventType";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, eventType);

        DataEngineEventTypeHandler dataEngineEventTypeHandler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);
        String eventTypeGUID = dataEngineEventTypeHandler.upsertEventType(userId, eventType, topicGUID, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, eventTypeGUID);
        return eventTypeGUID;
    }

    /**
     * Delete the EventType with all the associated event schema attributes
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the event type
     * @return void response
     */
    public VoidResponse deleteEventType(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteEventType";
        VoidResponse response = new VoidResponse();

        try {
            validateRequestBody(userId, serverName, requestBody, methodName);

            deleteEventType(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuid(), requestBody.getQualifiedName(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete the EventType with all the associated event schema attributes
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param externalSourceName the unique name of the external source
     * @param guid               the unique identifier of the event type
     * @param qualifiedName      the qualified name of the event type
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteEventType(String userId, String serverName, String externalSourceName, String guid, String qualifiedName,
                                DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException,
            UserNotAuthorizedException, EntityNotDeletedException,
            FunctionNotSupportedException {

        final String methodName = "deleteEventType";

        DataEngineEventTypeHandler dataEngineEventTypeHandler = instanceHandler.getEventTypeHandler(userId, serverName, methodName);

        String eventTypeGUID = getEntityGUID(userId, serverName, guid, qualifiedName, OpenMetadataType.EVENT_TYPE_TYPE_NAME, methodName);
        dataEngineEventTypeHandler.removeEventType(userId, eventTypeGUID, qualifiedName, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, eventTypeGUID, OpenMetadataType.TOPIC_TYPE_NAME);
    }

    /**
     * Create or update the ProcessingState with provided map of critical elements and sync states
     *
     * @param serverName                 name of server instance to call
     * @param userId                     the name of the calling user
     * @param processingStateRequestBody map of critical elements and sync states
     * @return void response
     */
    public VoidResponse upsertProcessingState(String userId, String serverName, ProcessingStateRequestBody processingStateRequestBody) {
        final String methodName = "upsertProcessingState";

        VoidResponse response = new VoidResponse();
        try {
            validateRequestBody(userId, serverName, processingStateRequestBody, methodName);

            ProcessingState processingState = processingStateRequestBody.getProcessingState();
            if (processingState == null) {
                restExceptionHandler.handleMissingValue(PROCESSING_STATE, methodName);
                return response;
            }
            return upsertProcessingState(userId, serverName, processingState, processingStateRequestBody.getExternalSourceName());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    public PropertiesResponse getProcessingState(String userId, String serverName, String externalSourceName) {
        final String methodName = "getProcessingState";

        PropertiesResponse response = new PropertiesResponse();
        try {
            DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);
            ProcessingState processingState = handler.getProcessingStateClassification(userId, externalSourceName);
            Map<String, Object> properties = new HashMap<>(processingState.getSyncDatesByKey());
            response.setProperties(properties);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Create or update the ProcessingState with provided map of critical elements and sync states
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processingState    map of critical elements and sync states
     * @param externalSourceName the unique name of the external source
     * @return void response
     */
    public VoidResponse upsertProcessingState(String userId, String serverName, ProcessingState processingState, String externalSourceName) {
        final String methodName = "upsertProcessingState";

        VoidResponse response = new VoidResponse();
        try {
            DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);
            handler.upsertProcessingStateClassification(userId, processingState, externalSourceName);
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    private boolean isTopicRequestBodyValid(String userId, String serverName, TopicRequestBody topicRequestBody, String methodName) throws
            InvalidParameterException {
        validateRequestBody(userId, serverName, topicRequestBody, methodName);

        if (topicRequestBody.getTopic() == null) {
            restExceptionHandler.handleMissingValue(TOPIC_PARAMETER_NAME, methodName);
            return false;
        }
        return true;
    }

    private boolean isEventTypeRequestBodyValid(String userId, String serverName, EventTypeRequestBody eventTypeRequestBody, String methodName) throws
            InvalidParameterException {
        validateRequestBody(userId, serverName, eventTypeRequestBody, methodName);

        if (eventTypeRequestBody.getTopicQualifiedName() == null) {
            restExceptionHandler.handleMissingValue(TOPIC_QUALIFIED_NAME_PARAMETER_NAME, methodName);
            return false;
        }
        if (eventTypeRequestBody.getEventType() == null) {
            restExceptionHandler.handleMissingValue(EVENT_TYPE_PARAMETER_NAME, methodName);
            return false;
        }
        return true;
    }
}
