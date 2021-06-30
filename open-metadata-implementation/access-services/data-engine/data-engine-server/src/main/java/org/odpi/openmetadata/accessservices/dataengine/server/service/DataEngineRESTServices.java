/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineOMASAPIRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesDeleteRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCollectionHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineCommonHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineConnectionAndEndpointHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineFolderHierarchyHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.partitioningBy;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CONNECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATABASE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_FOLDER_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_ALIAS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;

/**
 * The DataEngineRESTServices provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This service provide the functionality to create processes, ports with schema types and corresponding
 * relationships.
 */
public class DataEngineRESTServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineRESTServices.class);
    private static final String DEBUG_MESSAGE_METHOD_DETAILS = "Calling method {} for entity: {}";
    private static final String DEBUG_MESSAGE_METHOD_RETURN = "Returning from method: {} with response: {}";
    public static final String EXCEPTION_WHILE_ADDING_LINEAGE_MAPPING = "Exception while adding lineage mapping {} : {}";
    public static final String EXCEPTION_WHILE_CREATING_PROCESS = "Exception while creating process {} : {}";
    public static final String EXCEPTION_WHILE_CREATING_PROCESS_HIERARCHY = "Exception while creating process relationships for process {} : {}";
    private static final String DEBUG_DELETE_MESSAGE = "Data Engine OMAS deleted entity with GUID {} and type {}";
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private final DataEngineInstanceHandler instanceHandler = new DataEngineInstanceHandler();

    /**
     * Create the external data engine as software server capability entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the server
     *
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
            response.setGUID(createExternalDataEngine(userId, serverName, requestBody.getSoftwareServerCapability()));
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
     *
     * @return the unique identifier from a software server capability definition for an external data engine
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
     *
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
     *
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
        if (!dataEngineGUID.isPresent()) {
            dataEngineGUID = Optional.ofNullable(dataEngineRegistrationHandler.getExternalDataEngine(userId, qualifiedName));
        }

        if (!dataEngineGUID.isPresent()) {
            return;
        }

        dataEngineRegistrationHandler.removeExternalDataEngine(userId, qualifiedName, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, dataEngineGUID, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
    }

    /**
     * Get the unique identifier of an entity
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the port
     * @param typeName      the type name of the entity
     *
     * @return the unique identifier of the entity or empty optional
     *
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
     *
     * @return the entity details of the entity or empty optional
     *
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
     *
     * @return the unique identifier (guid) of the created schema type
     */
    public GUIDResponse upsertSchemaType(String userId, String serverName, SchemaTypeRequestBody schemaTypeRequestBody) {
        final String methodName = "upsertSchemaType";

        GUIDResponse response = new GUIDResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, schemaTypeRequestBody, methodName)) return response;

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
     *
     * @return void response
     */
    public VoidResponse deleteSchemaType(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteSchemaType";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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

        String schemaTypeGUID = getEntityGUID(userId, serverName, guid, qualifiedName, SCHEMA_TYPE_TYPE_NAME, methodName);
        dataEngineSchemaTypeHandler.removeSchemaType(userId, schemaTypeGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, schemaTypeGUID, SCHEMA_TYPE_TYPE_NAME);
    }

    /**
     * Create or update the Port Implementation with a PortSchema relationship
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse upsertPortImplementation(String userId, String serverName, PortImplementationRequestBody portImplementationRequestBody) {
        final String methodName = "upsertPortImplementation";

        GUIDResponse response = new GUIDResponse();
        try {
            if (isRequestBodyInvalid(userId, serverName, portImplementationRequestBody, methodName)) return response;

            String processGUID = getEntityGUID(serverName, userId, portImplementationRequestBody.getProcessQualifiedName(), PROCESS_TYPE_NAME)
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
     * Create or update the Port Alias with a PortDelegation relationship
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param portAliasRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse upsertPortAlias(String userId, String serverName, PortAliasRequestBody portAliasRequestBody) {
        final String methodName = "upsertPortAliasWithDelegation";

        GUIDResponse response = new GUIDResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, portAliasRequestBody, methodName)) return response;

            String processGUID = getEntityGUID(serverName, userId, portAliasRequestBody.getProcessQualifiedName(), PROCESS_TYPE_NAME)
                    .orElse(null);
            String externalSourceName = portAliasRequestBody.getExternalSourceName();

            updateProcessStatus(userId, serverName, processGUID, InstanceStatus.DRAFT, externalSourceName);
            response.setGUID(upsertPortAliasWithDelegation(userId, serverName, portAliasRequestBody.getPortAlias(), processGUID, externalSourceName));
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
     *
     * @return void response
     */
    public VoidResponse deletePort(String userId, String serverName, DeleteRequestBody requestBody, String portType) {
        final String methodName = "deletePort";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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

        String portGUID = getEntityGUID(userId, serverName, guid, qualifiedName, PORT_TYPE_NAME, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        if (PORT_IMPLEMENTATION_TYPE_NAME.equalsIgnoreCase(portType)) {
            Optional<EntityDetail> schemaType = dataEnginePortHandler.findSchemaTypeForPort(userId, portGUID);
            if (schemaType.isPresent()) {
                deleteSchemaType(userId, serverName, externalSourceName, schemaType.get().getGUID(), null, deleteSemantic);
            }
        }

        dataEnginePortHandler.removePort(userId, portGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, portGUID, PORT_TYPE_NAME);
    }


    /**
     * Add the provided ProcessHierarchy relationship
     *
     * @param serverName                  name of server instance to call
     * @param userId                      the name of the calling user
     * @param processHierarchyRequestBody properties of the process hierarchy
     *
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     */
    public GUIDResponse addProcessHierarchy(String userId, String serverName, ProcessHierarchyRequestBody processHierarchyRequestBody) {
        final String methodName = "addProcessHierarchy";

        GUIDResponse response = new GUIDResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, processHierarchyRequestBody, methodName)) return response;

            response.setGUID(addProcessHierarchyToProcess(userId, serverName, processHierarchyRequestBody.getProcessHierarchy(),
                    processHierarchyRequestBody.getExternalSourceName()));
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }

        return response;
    }

    /**
     * Create or update the processes with ports, schema types and lineage mappings
     *
     * @param userId               the name of the calling user
     * @param serverName           name of server instance to call
     * @param processesRequestBody properties of the processes
     *
     * @return a list unique identifiers (GUIDs) of the created/updated processes
     */
    public ProcessListResponse upsertProcesses(String userId, String serverName, ProcessesRequestBody processesRequestBody) {
        final String methodName = "upsertProcesses";

        ProcessListResponse response = new ProcessListResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, processesRequestBody, methodName)) return response;

            if (CollectionUtils.isEmpty(processesRequestBody.getProcesses())) {
                restExceptionHandler.handleMissingValue("processes", methodName);
                return response;
            }

            return upsertProcesses(userId, serverName, processesRequestBody.getProcesses(), processesRequestBody.getExternalSourceName());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete a list of processes, with the associated port implementations, port aliases and lineage mappings
     *
     * @param userId      the name of the calling user
     * @param serverName  name of server instance to call
     * @param requestBody properties of the processes
     *
     * @return void response
     */
    public VoidResponse deleteProcesses(String userId, String serverName, ProcessesDeleteRequestBody requestBody) {
        final String methodName = "deleteProcesses";

        VoidResponse response = new VoidResponse();

        try {
            if (!isDeleteProcessesRequestBodyValid(userId, serverName, requestBody, methodName)) return response;

            deleteProcesses(userId, serverName, requestBody.getExternalSourceName(), requestBody.getGuids(), requestBody.getQualifiedNames(),
                    requestBody.getDeleteSemantic());
        } catch (Exception error) {
            restExceptionHandler.captureExceptions(response, error, methodName);
        }
        return response;
    }

    /**
     * Delete a list of processes, with the associated port implementations, port aliases and lineage mappings.
     *
     * @param userId             the name of the calling user
     * @param serverName         the server name
     * @param externalSourceName the unique name of the external source
     * @param guids              the unique identifiers of the processes
     * @param qualifiedNames     the qualified names of the processes
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     * @throws EntityNotDeletedException     the entity could not be deleted
     */
    public void deleteProcesses(String userId, String serverName, String externalSourceName, List<String> guids, List<String> qualifiedNames,
                                DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                                      FunctionNotSupportedException, EntityNotDeletedException {
        final String methodName = "deleteProcesses";
        if (CollectionUtils.isNotEmpty(qualifiedNames)) {
            for (String qualifiedName : qualifiedNames) {
                Optional<String> processGUIDOptional = getEntityGUID(serverName, userId, qualifiedName, PROCESS_TYPE_NAME);
                if (processGUIDOptional.isPresent()) {
                    deleteProcess(userId, serverName, externalSourceName, processGUIDOptional.get(), deleteSemantic);
                } else {
                    throwEntityNotDeletedException(userId, serverName, methodName, qualifiedName);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(guids)) {
            for (String guid : guids) {
                deleteProcess(userId, serverName, externalSourceName, guid, deleteSemantic);
            }
        }
    }

    private void deleteProcess(String userId, String serverName, String externalSourceName, String processGUID, DeleteSemantic deleteSemantic) throws
                                                                                                                                               InvalidParameterException,
                                                                                                                                               UserNotAuthorizedException,
                                                                                                                                               PropertyServerException,
                                                                                                                                               FunctionNotSupportedException,
                                                                                                                                               EntityNotDeletedException {
        final String methodName = "deleteProcess";

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

        Set<EntityDetail> portImplementations = processHandler.getPortsForProcess(userId, processGUID, PORT_IMPLEMENTATION_TYPE_NAME);
        for (EntityDetail port : portImplementations) {
            deletePort(userId, serverName, externalSourceName, port.getGUID(), null, PORT_IMPLEMENTATION_TYPE_NAME, deleteSemantic);
        }

        Set<EntityDetail> portAliases = processHandler.getPortsForProcess(userId, processGUID, PORT_ALIAS_TYPE_NAME);
        for (EntityDetail port : portAliases) {
            deletePort(userId, serverName, externalSourceName, port.getGUID(), null, PORT_ALIAS_TYPE_NAME, deleteSemantic);
        }
        processHandler.removeProcess(userId, processGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, processGUID, PROCESS_TYPE_NAME);
    }

    /**
     * Create or update a Port Alias with a PortDelegation relationship
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param portAlias          the port alias values
     * @param processGUID        the unique identifier of the process
     * @param externalSourceName the unique name of the external source
     *
     * @return the unique identifier (guid) of the created port alias
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertPortAliasWithDelegation(String userId, String serverName, PortAlias portAlias, String processGUID,
                                                String externalSourceName) throws InvalidParameterException, PropertyServerException,
                                                                                  UserNotAuthorizedException {
        final String methodName = "upsertPortAliasWithDelegation";

        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, portAlias);

        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Optional<EntityDetail> portEntity = dataEnginePortHandler.findPortAliasEntity(userId, portAlias.getQualifiedName());

        String portAliasGUID;
        if (!portEntity.isPresent()) {
            portAliasGUID = dataEnginePortHandler.createPortAlias(userId, portAlias, processGUID, externalSourceName);
        } else {
            portAliasGUID = portEntity.get().getGUID();
            dataEnginePortHandler.updatePortAlias(userId, portEntity.get(), portAlias, externalSourceName);
        }

        if (!StringUtils.isEmpty(portAlias.getDelegatesTo())) {
            dataEnginePortHandler.addPortDelegationRelationship(userId, portAliasGUID, portAlias.getPortType(), portAlias.getDelegatesTo(),
                    externalSourceName);
        }

        log.trace(DEBUG_MESSAGE_METHOD_RETURN, methodName, portAliasGUID);
        return portAliasGUID;
    }

    /**
     * Add a a ProcessHierarchy relationship to the process
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param processHierarchy   the process hierarchy values
     * @param externalSourceName the unique name of the external source
     *
     * @return the unique identifier (guid) of the child of the process hierarchy that was updated
     *
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
                    this.getClass().getName(), methodName, "childProcess");
        }

        log.info("Data Engine OMAS has added a relationship of type ProcessHierarchy between child process {} and parent process {}",
                processHierarchy.getChildProcess(), processHierarchy.getParentProcess());

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
     *
     * @return the unique identifier (guid) of the created port alias
     *
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
        if (!portEntity.isPresent()) {
            portImplementationGUID = dataEnginePortHandler.createPortImplementation(userId, portImplementation, processGUID, externalSourceName);
        } else {
            portImplementationGUID = portEntity.get().getGUID();
            dataEnginePortHandler.updatePortImplementation(userId, portEntity.get(), portImplementation, externalSourceName);

            if (portImplementation.getUpdateSemantic() == UpdateSemantic.REPLACE) {
                Optional<EntityDetail> schemaTypeForPort = dataEnginePortHandler.findSchemaTypeForPort(userId, portImplementationGUID);
                if (schemaTypeForPort.isPresent()) {
                    String oldSchemaTypeQualifiedName =
                            schemaTypeForPort.get().getProperties().getPropertyValue(QUALIFIED_NAME_PROPERTY_NAME).valueAsString();
                    deleteObsoleteSchemaType(userId, serverName, portImplementation.getSchemaType().getQualifiedName(), oldSchemaTypeQualifiedName,
                            externalSourceName);
                }
            }
        }

        log.trace(DEBUG_MESSAGE_METHOD_RETURN, methodName, portImplementationGUID);
        return portImplementationGUID;
    }

    /**
     * Create the external data engine as software server capability entity
     *
     * @param userId                   the name of the calling user
     * @param serverName               name of server instance to call
     * @param softwareServerCapability the software server values
     *
     * @return he unique identifier (guid) of the created external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createExternalDataEngine(String userId, String serverName, SoftwareServerCapability softwareServerCapability) throws
                                                                                                                                InvalidParameterException,
                                                                                                                                PropertyServerException,
                                                                                                                                UserNotAuthorizedException {
        final String methodName = "createExternalDataEngine";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, softwareServerCapability);

        if (softwareServerCapability == null) {
            return null;
        }

        DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

        String softwareServerCapabilityGUID = handler.upsertExternalDataEngine(userId, softwareServerCapability);

        log.info("Data Engine OMAS has registered an external engine with qualified name {} and GUID {}",
                softwareServerCapability.getQualifiedName(), softwareServerCapabilityGUID);
        return softwareServerCapabilityGUID;
    }

    /**
     * Create LineageMappings relationships between schema attributes
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param lineageMappings    the list of lineage mappings to be created
     * @param response           the response object that will capture the exceptions that might occur during
     *                           parallel processing
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addLineageMappings(String userId, String serverName, List<LineageMapping> lineageMappings, FFDCResponseBase response,
                                   String externalSourceName) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException {
        final String methodName = "addLineageMappings";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, lineageMappings);

        if (CollectionUtils.isEmpty(lineageMappings)) {
            return;
        }

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        lineageMappings.parallelStream().forEach(lineageMapping -> {
            try {
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(userId, lineageMapping.getSourceAttribute(),
                        lineageMapping.getTargetAttribute(), externalSourceName);
            } catch (Exception error) {
                log.error(EXCEPTION_WHILE_ADDING_LINEAGE_MAPPING, lineageMapping.toString(), error.toString());
                restExceptionHandler.captureExceptions(response, error, methodName);
            }
        });
    }

    /**
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param processes          list of processes to be created
     * @param externalSourceName the unique name of the external source
     *
     * @return a list unique identifiers (GUIDs) of the created/updated processes
     */
    public ProcessListResponse upsertProcesses(String userId, String serverName, List<Process> processes, String externalSourceName) {
        final String methodName = "upsertProcesses";

        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, processes);

        Predicate<? super Process> hasPortImplementationsPredicate = process -> CollectionUtils.isNotEmpty(process.getPortImplementations());
        Map<Boolean, List<Process>> partitionedProcesses = processes.parallelStream().collect(partitioningBy(hasPortImplementationsPredicate));

        List<GUIDResponse> createdProcesses = new ArrayList<>();
        List<GUIDResponse> failedProcesses = new ArrayList<>();
        Consumer<Process> processConsumer = process ->
        {
            GUIDResponse guidResponse = upsertProcess(userId, serverName, process, externalSourceName);
            if (guidResponse.getRelatedHTTPCode() == HttpStatus.OK.value()) {
                String processGUID = guidResponse.getGUID();
                process.setGUID(processGUID);
                VoidResponse updateStatusResponse = updateProcessStatus(userId, serverName, processGUID, InstanceStatus.ACTIVE, externalSourceName);
                if (updateStatusResponse.getRelatedHTTPCode() != 200) {
                    captureException(updateStatusResponse, guidResponse);
                }
                createdProcesses.add(guidResponse);
            } else {
                failedProcesses.add(guidResponse);
            }
        };

        partitionedProcesses.get(Boolean.TRUE).parallelStream().forEach(processConsumer);
        // processes that have port aliases can not be processed in parallel, as multiple processes can define the same port alias
        partitionedProcesses.get(Boolean.FALSE).forEach(processConsumer);

        ProcessListResponse response = new ProcessListResponse();
        response.setGUIDs(createdProcesses.parallelStream().map(GUIDResponse::getGUID).collect(Collectors.toList()));
        handleFailedProcesses(response, failedProcesses);

        addProcessHierarchyRelationships(userId, serverName, processes, response, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

        return response;
    }

    /**
     * Create LineageMappings relationships between schema attributes
     *
     * @param userId                     the name of the calling user
     * @param serverName                 ame of server instance to call
     * @param lineageMappingsRequestBody list of lineage mappings
     *
     * @return void response
     */
    public VoidResponse addLineageMappings(String userId, String serverName, LineageMappingsRequestBody lineageMappingsRequestBody) {
        final String methodName = "addLineageMappings";

        VoidResponse response = new VoidResponse();
        try {
            if (lineageMappingsRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            addLineageMappings(userId, serverName, lineageMappingsRequestBody.getLineageMappings(), response,
                    lineageMappingsRequestBody.getExternalSourceName());
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
     *
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
     *
     * @return the unique identifier (guid) of the created schema type
     *
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

        String schemaTypeGUID = dataEngineSchemaTypeHandler.upsertSchemaType(userId, schemaType, externalSourceName);
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
     *
     * @return the unique identifier (guid) of the created database
     */
    public GUIDResponse upsertDatabase(String userId, String serverName, DatabaseRequestBody databaseRequestBody) {
        final String methodName = "upsertDatabase";

        GUIDResponse response = new GUIDResponse();
        try {
            if (!isDatabaseRequestBodyValid(userId, serverName, databaseRequestBody, methodName)) return response;

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
     *
     * @return the unique identifier (guid) of the created database
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertDatabase(String userId, String serverName, Database database, String externalSourceName) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException {
        final String methodName = "upsertDatabase";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, database);

        DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName, methodName);
        String databaseGUID = dataEngineRelationalDataHandler.upsertDatabase(userId, database, externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, databaseGUID);
        return databaseGUID;
    }

    /**
     * Delete the Database with all the associated relational tables
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the database
     *
     * @return void response
     */
    public VoidResponse deleteDatabase(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteDatabase";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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

        String databaseGUID = getEntityGUID(userId, serverName, guid, qualifiedName, DATABASE_TYPE_NAME, methodName);
        relationalDataHandler.removeDatabase(userId, databaseGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, databaseGUID, DATABASE_TYPE_NAME);
    }

    /**
     * Create the Relational Table with Relational Columns and corresponding relationships
     *
     * @param serverName                 name of server instance to call
     * @param userId                     the name of the calling user
     * @param relationalTableRequestBody properties of the relational table
     *
     * @return the unique identifier (guid) of the created relational table
     */
    public GUIDResponse upsertRelationalTable(String userId, String serverName, RelationalTableRequestBody relationalTableRequestBody) {
        final String methodName = "upsertRelationalTable";

        GUIDResponse response = new GUIDResponse();

        try {
            if (!isRelationalTableRequestBodyValid(userId, serverName, relationalTableRequestBody, methodName)) return response;

            String relationalTableGUID = upsertRelationalTable(userId, serverName, relationalTableRequestBody.getDatabaseQualifiedName(),
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
     * @param userId                the name of the calling user
     * @param serverName            name of server instance to call
     * @param databaseQualifiedName the unique name of the database
     * @param relationalTable       the relational table values
     * @param externalSourceName    the unique name of the external source
     *
     * @return the unique identifier (guid) of the created relational table
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertRelationalTable(String userId, String serverName, String databaseQualifiedName, RelationalTable relationalTable,
                                        String externalSourceName) throws InvalidParameterException, UserNotAuthorizedException,
                                                                          PropertyServerException {
        final String methodName = "upsertRelationalTable";
        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, relationalTable);

        DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName,
                methodName);

        String relationalTableGUID = dataEngineRelationalDataHandler.upsertRelationalTable(userId, databaseQualifiedName, relationalTable,
                externalSourceName);

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, relationalTableGUID);
        return relationalTableGUID;
    }

    /**
     * Delete the Relational Table with all the associated Relational Columns
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the relational table
     *
     * @return void response
     */
    public VoidResponse deleteRelationalTable(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteRelationalTable";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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

        String relationalTableGUID = getEntityGUID(userId, serverName, guid, qualifiedName, RELATIONAL_TABLE_TYPE_NAME, methodName);
        relationalDataHandler.removeRelationalTable(userId, relationalTableGUID, externalSourceName, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, relationalTableGUID, RELATIONAL_TABLE_TYPE_NAME);
    }


    /**
     * Updates or inserts a DataFile or CSVFile, along with its schema, columns and folder hierarchy
     *
     * @param userId              the name of the calling user
     * @param serverName          name of server instance to call
     * @param dataFileRequestBody properties of the data file
     *
     * @return the unique identifier (guid) of the created data file
     */
    public GUIDResponse upsertDataFile(String serverName, String userId, DataFileRequestBody dataFileRequestBody) {
        String methodName = "upsertDataFile";
        GUIDResponse response = new GUIDResponse();
        String guid;

        try {
            if (isRequestBodyInvalid(userId, serverName, dataFileRequestBody, methodName)) {
                return response;
            }

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
     *
     * @return the unique identifier (guid) of the created data file
     *
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
        String fileTypeGuid = file instanceof CSVFile ? CSV_FILE_TYPE_GUID : DATA_FILE_TYPE_GUID;
        String fileTypeName = file instanceof CSVFile ? CSV_FILE_TYPE_NAME : DATA_FILE_TYPE_NAME;
        file.setFileType(fileTypeName);

        String guid = dataFileHandler.upsertFileAssetIntoCatalog(fileTypeName, fileTypeGuid, file, schemaType, columns, extendedProperties,
                externalSourceGuid, externalSourceName, userId, methodName);
        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, guid);
        return guid;
    }

    /**
     * Delete the Data File with all the associated Tabular Columns
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the data file
     *
     * @return void response
     */
    public VoidResponse deleteDataFile(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteDataFile";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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

        String dataFileGUID = getEntityGUID(userId, serverName, guid, qualifiedName, DATA_FILE_TYPE_NAME, methodName);

        String externalSourceGuid = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        dataFileHandler.removeDataFile(userId, dataFileGUID, externalSourceName, externalSourceGuid, deleteSemantic);
        log.debug(DEBUG_DELETE_MESSAGE, dataFileGUID, DATA_FILE_TYPE_NAME);
    }

    /**
     * Delete the File Folder
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the folder
     *
     * @return void response
     */
    public VoidResponse deleteFolder(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteFolder";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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
        String folderGUID = getEntityGUID(userId, serverName, guid, qualifiedName, FILE_FOLDER_TYPE_NAME, methodName);

        DataEngineFolderHierarchyHandler folderHierarchyHandler = instanceHandler.getFolderHierarchyHandler(userId, serverName, methodName);
        folderHierarchyHandler.removeFolder(userId, folderGUID, deleteSemantic, externalSourceName);

        log.debug(DEBUG_DELETE_MESSAGE, folderGUID, FILE_FOLDER_TYPE_NAME);
    }

    /**
     * Delete the Connection
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the connection
     *
     * @return void response
     */
    public VoidResponse deleteConnection(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteConnection";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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
        String connectionGUID = getEntityGUID(userId, serverName, guid, qualifiedName, CONNECTION_TYPE_NAME, methodName);

        DataEngineConnectionAndEndpointHandler connectionAndEndpointHandler = instanceHandler.getConnectionAndEndpointHandler(userId, serverName,
                methodName);
        connectionAndEndpointHandler.removeConnection(userId, connectionGUID, deleteSemantic, externalSourceName);

        log.debug(DEBUG_DELETE_MESSAGE, connectionGUID, CONNECTION_TYPE_NAME);
    }

    /**
     * Delete the Endpoint
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the endpoint
     *
     * @return void response
     */
    public VoidResponse deleteEndpoint(String userId, String serverName, DeleteRequestBody requestBody) {
        final String methodName = "deleteEndpoint";

        VoidResponse response = new VoidResponse();

        try {
            if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return response;

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
     *
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
        String endpointGUID = getEntityGUID(userId, serverName, guid, qualifiedName, ENDPOINT_TYPE_NAME, methodName);

        DataEngineConnectionAndEndpointHandler connectionAndEndpointHandler = instanceHandler.getConnectionAndEndpointHandler(userId, serverName,
                methodName);
        connectionAndEndpointHandler.removeEndpoint(userId, endpointGUID, deleteSemantic, externalSourceName);

        log.debug(DEBUG_DELETE_MESSAGE, endpointGUID, ENDPOINT_TYPE_NAME);
    }

    private String getEntityGUID(String userId, String serverName, String guid, String qualifiedName, String entityTypeName, String methodName) throws
                                                                                                                                                InvalidParameterException,
                                                                                                                                                PropertyServerException,
                                                                                                                                                UserNotAuthorizedException,
                                                                                                                                                EntityNotDeletedException {
        Optional<String> entityGUIDOptional = Optional.ofNullable(guid);
        if (!entityGUIDOptional.isPresent()) {
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

        Optional<String> schemaTypeGUID = getEntityGUID(serverName, userId, oldSchemaTypeQualifiedName, SCHEMA_TYPE_TYPE_NAME);
        if (!schemaTypeGUID.isPresent()) {
            return;
        }

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);
        dataEngineSchemaTypeHandler.removeSchemaType(userId, schemaTypeGUID.get(), externalSourceName, DeleteSemantic.SOFT);
    }

    private void handleFailedProcesses(ProcessListResponse response, List<GUIDResponse> failedProcesses) {
        response.setFailedGUIDs((failedProcesses.parallelStream().map(GUIDResponse::getGUID).collect(Collectors.toList())));
        failedProcesses.parallelStream().forEach(guidResponse -> captureException(guidResponse, response));
    }

    private void captureException(FFDCResponseBase initialResponse, FFDCResponseBase response) {
        response.setExceptionErrorMessage(initialResponse.getExceptionErrorMessage());
        response.setExceptionClassName(initialResponse.getExceptionClassName());
        response.setExceptionSystemAction(initialResponse.getExceptionSystemAction());
        response.setExceptionUserAction(initialResponse.getExceptionUserAction());
        response.setRelatedHTTPCode(initialResponse.getRelatedHTTPCode());
        response.setExceptionProperties(initialResponse.getExceptionProperties());
    }

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
     * Create the process with ports, schema types and lineage mappings
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param process    properties of the process
     *
     * @return the unique identifier (guid) of the created process
     */
    private GUIDResponse upsertProcess(String userId, String serverName, Process process, String externalSourceName) {
        final String methodName = "upsertProcess";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, process);

        String qualifiedName = process.getQualifiedName();
        List<PortImplementation> portImplementations = process.getPortImplementations();
        List<PortAlias> portAliases = process.getPortAliases();
        List<LineageMapping> lineageMappings = process.getLineageMappings();
        UpdateSemantic updateSemantic = process.getUpdateSemantic();

        GUIDResponse response = new GUIDResponse();

        try {
            DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            Optional<EntityDetail> processEntity = processHandler.findProcessEntity(userId, qualifiedName);
            String processGUID;
            if (!processEntity.isPresent()) {
                processGUID = processHandler.createProcess(userId, process, externalSourceName);
            } else {
                processGUID = processEntity.get().getGUID();
                processHandler.updateProcess(userId, processEntity.get(), process, externalSourceName);
                processHandler.updateProcessStatus(userId, processGUID, InstanceStatus.DRAFT, externalSourceName);

                if (updateSemantic == UpdateSemantic.REPLACE) {
                    deleteObsoletePorts(userId, serverName, portImplementations, processGUID, PORT_IMPLEMENTATION_TYPE_NAME, response,
                            externalSourceName);
                    deleteObsoletePorts(userId, serverName, portAliases, processGUID, PORT_ALIAS_TYPE_NAME, response, externalSourceName);
                }
            }

            String collectionGUID = createCollection(userId, serverName, process.getCollection(), externalSourceName);
            if (collectionGUID != null) {
                addProcessCollectionRelationship(userId, serverName, processGUID, collectionGUID, externalSourceName);
            }

            upsertPortImplementations(userId, serverName, portImplementations, processGUID, response, externalSourceName);
            upsertPortAliases(userId, serverName, portAliases, processGUID, response, externalSourceName);

            addLineageMappings(userId, serverName, lineageMappings, response, externalSourceName);

            log.info("Data Engine OMAS has created or updated a Process with qualified name {} and guid {}", qualifiedName, processGUID);
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
        if (!collectionEntity.isPresent()) {
            collectionGUID = dataEngineCollectionHandler.createCollection(userId, collection, externalSourceName);
        } else {
            collectionGUID = collectionEntity.get().getGUID();
        }
        return collectionGUID;
    }

    private void addProcessHierarchyRelationships(String userId, String serverName, List<Process> processes, ProcessListResponse response,
                                                  String externalSourceName) {
        final String methodName = "addProcessHierarchyRelationships";

        // add the ProcessHierarchy relationships only for successfully created processes
        processes.parallelStream().filter(process -> response.getGUIDs().contains(process.getGUID())).forEach(process -> {
            List<ParentProcess> parentProcesses = process.getParentProcesses();
            String processGUID = process.getGUID();
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
        });
    }

    private void addProcessCollectionRelationship(String userId, String serverName, String processGUID, String collectionGUID,
                                                  String externalSourceName) throws InvalidParameterException, PropertyServerException,
                                                                                    UserNotAuthorizedException {

        final String methodName = "addProcessCollectionRelationship";

        DataEngineCollectionHandler dataEngineCollectionHandler = instanceHandler.getCollectionHandler(userId, serverName, methodName);

        dataEngineCollectionHandler.addCollectionMembershipRelationship(userId, collectionGUID, processGUID, externalSourceName);
    }

    private void deleteObsoletePorts(String userId, String serverName, List<? extends Port> ports, String processGUID, String portTypeName,
                                     GUIDResponse response, String externalSourceName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException {
        final String methodName = "deleteObsoletePorts";
        if (CollectionUtils.isEmpty(ports)) {
            return;
        }

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Set<EntityDetail> existingPorts = processHandler.getPortsForProcess(userId, processGUID, portTypeName);
        Set<String> portQualifiedNames = existingPorts.stream()
                .map(entityDetail -> entityDetail.getProperties().getPropertyValue(QUALIFIED_NAME_PROPERTY_NAME).valueAsString())
                .collect(Collectors.toSet());
        Set<String> newPortQualifiedNames = ports.stream().map(Referenceable::getQualifiedName).collect(Collectors.toSet());

        // delete ports that are not in the process payload anymore
        List<String> obsoletePortQualifiedNames =
                portQualifiedNames.stream().collect(partitioningBy(newPortQualifiedNames::contains)).get(Boolean.FALSE);
        obsoletePortQualifiedNames.forEach(portQualifiedName -> {
            try {
                Optional<String> portGUID = getEntityGUID(serverName, userId, portQualifiedName, PORT_TYPE_NAME);
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

    private void upsertPortAliases(String userId, String serverName, List<PortAlias> portAliases, String processGUID, GUIDResponse response,
                                   String externalSourceName) {
        final String methodName = "upsertPortAliases";
        if (CollectionUtils.isNotEmpty(portAliases)) {
            portAliases.forEach(portAlias -> {
                try {
                    upsertPortAliasWithDelegation(userId, serverName, portAlias, processGUID, externalSourceName);
                } catch (Exception error) {
                    restExceptionHandler.captureExceptions(response, error, methodName);
                }
            });
        }
    }

    private boolean isDatabaseRequestBodyValid(String userId, String serverName, DatabaseRequestBody databaseRequestBody, String methodName) throws
                                                                                                                                             InvalidParameterException {
        if (isRequestBodyInvalid(userId, serverName, databaseRequestBody, methodName)) return false;

        if (databaseRequestBody.getDatabase() == null) {
            restExceptionHandler.handleMissingValue("database", methodName);
            return false;
        }
        return true;
    }

    private boolean isRelationalTableRequestBodyValid(String userId, String serverName, RelationalTableRequestBody relationalTableRequestBody,
                                                      String methodName) throws InvalidParameterException {
        if (isRequestBodyInvalid(userId, serverName, relationalTableRequestBody, methodName)) return false;

        if (StringUtils.isEmpty(relationalTableRequestBody.getDatabaseQualifiedName())) {
            restExceptionHandler.handleMissingValue("databaseQualifiedName", methodName);
            return false;
        }
        return true;
    }

    private boolean isRequestBodyInvalid(String userId, String serverName, DataEngineOMASAPIRequestBody requestBody, String methodName)
            throws InvalidParameterException {
        if (requestBody == null) {
            restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            return true;
        }
        if (StringUtils.isEmpty(requestBody.getExternalSourceName())) {
            restExceptionHandler.handleMissingValue("externalSourceName", methodName);
            return true;
        }
        return false;
    }

    private boolean isDeleteProcessesRequestBodyValid(String userId, String serverName, ProcessesDeleteRequestBody requestBody,
                                                      String methodName) throws
                                                                         InvalidParameterException {
        if (isRequestBodyInvalid(userId, serverName, requestBody, methodName)) return false;

        if (CollectionUtils.isEmpty(requestBody.getQualifiedNames()) && CollectionUtils.isEmpty(requestBody.getGuids())) {
            restExceptionHandler.handleMissingValue("qualifiedNames", methodName);
            return false;
        }
        return true;
    }

    private SchemaType getDefaultSchemaTypeIfAbsentAndAddAttributes(DataFile file, SchemaType schemaType, List<Attribute> attributes) {
        if (schemaType == null) {
            schemaType = new SchemaType();
            schemaType.setQualifiedName(file.getQualifiedName() + "::schema");
            schemaType.setDisplayName("Schema");
        }
        schemaType.setAttributeList(attributes);
        return schemaType;
    }

    private HashMap<String, Object> getExtendedProperties(DataFile file) {
        HashMap<String, Object> extendedProperties = new HashMap<>();

        if (file instanceof CSVFile) {
            CSVFile csvFile = (CSVFile) file;
            extendedProperties.put(FILE_TYPE_PROPERTY_NAME, csvFile.getFileType());
            extendedProperties.put(DELIMITER_CHARACTER_PROPERTY_NAME, csvFile.getDelimiterCharacter());
            extendedProperties.put(QUOTE_CHARACTER_PROPERTY_NAME, csvFile.getQuoteCharacter());
        } else {
            extendedProperties.put(FILE_TYPE_PROPERTY_NAME, file.getFileType());
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
}
