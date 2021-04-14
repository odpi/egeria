/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.Database;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessHierarchy;
import org.odpi.openmetadata.accessservices.dataengine.model.RelationalTable;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineOMASAPIRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataFileRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.DatabaseRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessHierarchyRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.RelationalTableRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineDataFileHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEnginePortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRelationalDataHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ConnectionResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME;

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

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
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
    public GUIDResponse getExternalDataEngineByQualifiedName(String serverName, String userId, String qualifiedName) {
        final String methodName = "getExternalDataEngineByQualifiedName";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, qualifiedName);

        GUIDResponse response = new GUIDResponse();

        try {
            DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

            response.setGUID(handler.getExternalDataEngineByQualifiedName(userId, qualifiedName));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

        return response;
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


            String newSchemaTypeGUID = upsertSchemaType(userId, serverName, schemaTypeRequestBody.getSchemaType(),
                    schemaTypeRequestBody.getExternalSourceName());

            response.setGUID(newSchemaTypeGUID);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        return response;
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
    public GUIDResponse upsertPortImplementation(String userId, String serverName,
                                                 PortImplementationRequestBody portImplementationRequestBody) {
        final String methodName = "upsertPortImplementation";

        GUIDResponse response = new GUIDResponse();
        try {
            if (isRequestBodyInvalid(userId, serverName, portImplementationRequestBody, methodName)) return response;

            String portImplementationGUID = upsertPortImplementationWithSchemaType(userId, serverName,
                    portImplementationRequestBody.getPortImplementation(), portImplementationRequestBody.getExternalSourceName());

            response.setGUID(portImplementationGUID);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
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

            response.setGUID(upsertPortAliasWithDelegation(userId, serverName, portAliasRequestBody.getPortAlias(),
                    portAliasRequestBody.getExternalSourceName()));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        return response;
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
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
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
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }
        return response;
    }

    /**
     * Create or update a Port Alias with a PortDelegation relationship
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param portAlias          the port alias values
     * @param externalSourceName the unique name of the external source
     *
     * @return the unique identifier (guid) of the created port alias
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertPortAliasWithDelegation(String userId, String serverName, PortAlias portAlias, String externalSourceName) throws
                                                                                                                                  InvalidParameterException,
                                                                                                                                  PropertyServerException,
                                                                                                                                  UserNotAuthorizedException {
        final String methodName = "upsertPortAliasWithDelegation";

        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, portAlias);

        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Optional<EntityDetail> portEntity = dataEnginePortHandler.findPortAliasEntity(userId, portAlias.getQualifiedName());

        String portAliasGUID;
        if (!portEntity.isPresent()) {
            portAliasGUID = dataEnginePortHandler.createPortAlias(userId, portAlias, externalSourceName);
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
     * @param externalSourceName the unique name of the external source
     *
     * @return the unique identifier (guid) of the created port alias
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertPortImplementationWithSchemaType(String userId, String serverName, PortImplementation portImplementation,
                                                         String externalSourceName) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException {
        final String methodName = "upsertPortImplementationWithSchemaType";

        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, portImplementation);

        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String schemaTypeGUID = upsertSchemaType(userId, serverName, portImplementation.getSchemaType(), externalSourceName);

        Optional<EntityDetail> portEntity = dataEnginePortHandler.findPortImplementationEntity(userId, portImplementation.getQualifiedName());

        String portImplementationGUID;
        if (!portEntity.isPresent()) {
            portImplementationGUID = dataEnginePortHandler.createPortImplementation(userId, portImplementation, externalSourceName);
        } else {
            portImplementationGUID = portEntity.get().getGUID();
            dataEnginePortHandler.updatePortImplementation(userId, portEntity.get(), portImplementation, externalSourceName);

            if (portImplementation.getUpdateSemantic() == UpdateSemantic.REPLACE) {
                deleteObsoleteSchemaType(userId, serverName, schemaTypeGUID,
                        dataEnginePortHandler.findSchemaTypeForPort(userId, portImplementationGUID), externalSourceName);
            }
        }

        dataEnginePortHandler.addPortSchemaRelationship(userId, portImplementationGUID, schemaTypeGUID, externalSourceName);

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
     * Create ProcessPort relationships for an existing Process
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param processGUID        the process entity unique identifier(guid)
     * @param portQualifiedNames the list of qualified names for the port entities
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addPortsToProcess(String userId, String serverName, String processGUID, List<String> portQualifiedNames, String externalSourceName) throws
                                                                                                                                                    InvalidParameterException,
                                                                                                                                                    PropertyServerException,
                                                                                                                                                    UserNotAuthorizedException {
        final String methodName = "addPortsToProcess";

        if (CollectionUtils.isEmpty(portQualifiedNames)) {
            return;
        }

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        for (String portQualifiedName : portQualifiedNames) {
            Optional<EntityDetail> portEntity = dataEnginePortHandler.findPortEntity(userId, portQualifiedName);
            if (portEntity.isPresent()) {
                processHandler.addProcessPortRelationship(userId, processGUID, portEntity.get().getGUID(), externalSourceName);
            }
        }
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
            } catch (InvalidParameterException error) {
                log.error(EXCEPTION_WHILE_ADDING_LINEAGE_MAPPING, lineageMapping.toString(), error.toString());
                restExceptionHandler.captureInvalidParameterException(response, error);
            } catch (PropertyServerException error) {
                log.error(EXCEPTION_WHILE_ADDING_LINEAGE_MAPPING, lineageMapping.toString(), error.toString());
                restExceptionHandler.capturePropertyServerException(response, error);
            } catch (UserNotAuthorizedException error) {
                log.error(EXCEPTION_WHILE_ADDING_LINEAGE_MAPPING, lineageMapping.toString(), error.toString());
                restExceptionHandler.captureUserNotAuthorizedException(response, error);
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
                VoidResponse updateStatusResponse = updateProcessStatus(userId, serverName, process, InstanceStatus.ACTIVE,
                        externalSourceName);
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
     * Create ProcessPort relationships for an existing Process
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param processGuid         the guid of the process
     * @param portListRequestBody list of port qualified names
     *
     * @return the unique identifier (guid) of the updated process entity
     */
    public GUIDResponse addPortsToProcess(String userId, String serverName, String processGuid, PortListRequestBody portListRequestBody) {
        final String methodName = "addPortsToProcess";

        GUIDResponse response = new GUIDResponse();

        try {
            if (portListRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            addPortsToProcess(userId, serverName, processGuid, portListRequestBody.getPorts(), portListRequestBody.getExternalSourceName());

            response.setGUID(processGuid);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.trace(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

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
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
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
        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }


    /**
     * Create or update a SchemaType
     *
     * @param userId             the name of the calling user
     * @param serverName         name of server instance to call
     * @param schemaType         the schema type values
     * @param externalSourceName the unique name of the external source
     *
     * @return the unique identifier (guid) of the created schema type
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertSchemaType(String userId, String serverName, SchemaType schemaType, String externalSourceName) throws
                                                                                                                       InvalidParameterException,
                                                                                                                       UserNotAuthorizedException,
                                                                                                                       PropertyServerException {
        final String methodName = "upsertSchemaType";

        log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, schemaType);

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        String schemaTypeGUID = dataEngineSchemaTypeHandler.upsertSchemaType(userId, schemaType, externalSourceName);

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

            Database database = databaseRequestBody.getDatabase();
            log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, database);

            DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName,
                    methodName);
            String databaseGUID = dataEngineRelationalDataHandler.upsertDatabase(userId, database, databaseRequestBody.getExternalSourceName());

            log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, databaseGUID);
            response.setGUID(databaseGUID);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        return response;
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

            RelationalTable relationalTable = relationalTableRequestBody.getRelationalTable();
            log.debug(DEBUG_MESSAGE_METHOD_DETAILS, methodName, relationalTable);

            DataEngineRelationalDataHandler dataEngineRelationalDataHandler = instanceHandler.getRelationalDataHandler(userId, serverName,
                    methodName);
            String relationalTableGUID = dataEngineRelationalDataHandler.upsertRelationalTable(userId,
                    relationalTableRequestBody.getDatabaseQualifiedName(), relationalTable, relationalTableRequestBody.getExternalSourceName());

            log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, relationalTableGUID);
            response.setGUID(relationalTableGUID);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        return response;
    }

    private void deleteObsoleteSchemaType(String userId, String serverName, String schemaTypeGUID, String oldSchemaTypeGUID,
                                          String externalSourceName) throws
                                                                     InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException {
        final String methodName = "deleteObsoleteSchemaType";

        if (!oldSchemaTypeGUID.equalsIgnoreCase(schemaTypeGUID)) {
            DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

            dataEngineSchemaTypeHandler.removeSchemaType(userId, oldSchemaTypeGUID, externalSourceName);
        }
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

    private VoidResponse updateProcessStatus(String userId, String serverName, Process process,
                                             InstanceStatus instanceStatus, String externalSourceName) {
        final String methodName = "updateProcessStatus";

        log.trace(DEBUG_MESSAGE_METHOD_DETAILS, methodName, process.getGUID());

        VoidResponse response = new VoidResponse();
        try {
            DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            processHandler.updateProcessStatus(userId, process.getGUID(), instanceStatus, externalSourceName);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
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
            Set<String> portImplementationGUIDs = upsertPortImplementations(userId, serverName, portImplementations, response,
                    externalSourceName);

            Set<String> portAliasGUIDs = upsertPortAliases(userId, serverName, portAliases, response, externalSourceName);

            //check intermediary status of the response after creating the ports
            if (response.getRelatedHTTPCode() != HttpStatus.OK.value()) {
                return response;
            }

            DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            Optional<EntityDetail> processEntity = processHandler.findProcessEntity(userId, qualifiedName);
            String processGUID;
            if (!processEntity.isPresent()) {
                processGUID = processHandler.createProcess(userId, process, externalSourceName);

                List<Attribute> schemaAttributes = getAttributes(portImplementations);

                addAnchorGUID(userId, serverName, processGUID, schemaAttributes, externalSourceName);
            } else {
                processGUID = processEntity.get().getGUID();
                processHandler.updateProcess(userId, processEntity.get(), process, externalSourceName);
                processHandler.updateProcessStatus(userId, processGUID, InstanceStatus.DRAFT, externalSourceName);

                if (updateSemantic == UpdateSemantic.REPLACE) {
                    deleteObsoletePorts(userId, serverName, portImplementationGUIDs, processGUID,
                            PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, response, externalSourceName);
                    deleteObsoletePorts(userId, serverName, portAliasGUIDs, processGUID, PortPropertiesMapper.PORT_ALIAS_TYPE_NAME,
                            response, externalSourceName);
                }
            }

            addProcessPortRelationships(userId, serverName, processGUID,
                    Stream.concat(portImplementationGUIDs.stream(), portAliasGUIDs.stream()).collect(Collectors.toSet()), response,
                    externalSourceName);

            addLineageMappings(userId, serverName, lineageMappings, response, externalSourceName);

            log.info("Data Engine OMAS has created or updated a Process with qualified name {} and guid {}", qualifiedName, processGUID);

            response.setGUID(processGUID);
        } catch (InvalidParameterException error) {
            log.error(EXCEPTION_WHILE_CREATING_PROCESS, qualifiedName, error.toString());
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            log.error(EXCEPTION_WHILE_CREATING_PROCESS, qualifiedName, error.toString());
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            log.error(EXCEPTION_WHILE_CREATING_PROCESS, qualifiedName, error.toString());
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug(DEBUG_MESSAGE_METHOD_RETURN, methodName, response);

        return response;
    }

    private List<Attribute> getAttributes(List<PortImplementation> portImplementations) {
        if (CollectionUtils.isEmpty(portImplementations)) {
            return new ArrayList<>();
        }
        return portImplementations.stream().map(portImplementation -> portImplementation.getSchemaType().getAttributeList())
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    private void addAnchorGUID(String userId, String serverName, String processGUID, List<Attribute> schemaAttributes,
                               String externalSourceName) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "addAnchorGUID";

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler = instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);
        for (Attribute attribute : schemaAttributes) {
            dataEngineSchemaTypeHandler.addAnchorGUID(userId, attribute, processGUID, externalSourceName);
        }
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
                } catch (InvalidParameterException error) {
                    log.error(EXCEPTION_WHILE_CREATING_PROCESS_HIERARCHY, process.getQualifiedName(), error.toString());
                    restExceptionHandler.captureInvalidParameterException(response, error);
                } catch (PropertyServerException error) {
                    log.error(EXCEPTION_WHILE_CREATING_PROCESS_HIERARCHY, process.getQualifiedName(), error.toString());
                    restExceptionHandler.capturePropertyServerException(response, error);
                } catch (UserNotAuthorizedException error) {
                    log.error(EXCEPTION_WHILE_CREATING_PROCESS_HIERARCHY, process.getQualifiedName(), error.toString());
                    restExceptionHandler.captureUserNotAuthorizedException(response, error);
                }
            }
        });
    }

    private void addProcessPortRelationships(String userId, String serverName, String processGUID, Set<String> portGUIDs, GUIDResponse response,
                                             String externalSourceName) throws InvalidParameterException, PropertyServerException,
                                                                               UserNotAuthorizedException {

        final String methodName = "addProcessPortRelationships";

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

        portGUIDs.parallelStream().forEach(portGUID -> {
            try {
                processHandler.addProcessPortRelationship(userId, processGUID, portGUID, externalSourceName);
            } catch (InvalidParameterException error) {
                restExceptionHandler.captureInvalidParameterException(response, error);
            } catch (PropertyServerException error) {
                restExceptionHandler.capturePropertyServerException(response, error);
            } catch (UserNotAuthorizedException error) {
                restExceptionHandler.captureUserNotAuthorizedException(response, error);
            }
        });
    }

    private void deleteObsoletePorts(String userId, String serverName, Set<String> newPortGUIDs, String processGUID, String portTypeName,
                                     GUIDResponse response, String externalSourceName) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException {
        final String methodName = "deleteObsoletePorts";

        if (CollectionUtils.isEmpty(newPortGUIDs)) {
            return;
        }

        DataEngineProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
        DataEnginePortHandler dataEnginePortHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Set<String> oldPortGUIDs = processHandler.getPortsForProcess(userId, processGUID, portTypeName);

        // delete ports that are not in the process payload anymore
        List<String> obsoletePorts = oldPortGUIDs.parallelStream().collect(partitioningBy(newPortGUIDs::contains)).get(Boolean.FALSE);
        obsoletePorts.parallelStream().forEach(portGUID -> {
            try {
                dataEnginePortHandler.removePort(userId, portGUID, portTypeName, externalSourceName);
            } catch (InvalidParameterException error) {
                restExceptionHandler.captureInvalidParameterException(response, error);
            } catch (PropertyServerException error) {
                restExceptionHandler.capturePropertyServerException(response, error);
            } catch (UserNotAuthorizedException error) {
                restExceptionHandler.captureUserNotAuthorizedException(response, error);
            }
        });

    }


    private Set<String> upsertPortImplementations(String userId, String serverName, List<PortImplementation> portImplementations,
                                                  GUIDResponse response, String externalSourceName) {
        Set<String> portImplementationGUIDs = new HashSet<>();

        if (CollectionUtils.isNotEmpty(portImplementations)) {
            portImplementations.parallelStream().forEach(portImplementation ->
            {
                try {
                    portImplementationGUIDs.add(upsertPortImplementationWithSchemaType(userId, serverName, portImplementation,
                            externalSourceName));
                } catch (InvalidParameterException error) {
                    restExceptionHandler.captureInvalidParameterException(response, error);
                } catch (PropertyServerException error) {
                    restExceptionHandler.capturePropertyServerException(response, error);
                } catch (UserNotAuthorizedException error) {
                    restExceptionHandler.captureUserNotAuthorizedException(response, error);
                }
            });
        }

        return portImplementationGUIDs;
    }

    private Set<String> upsertPortAliases(String userId, String serverName, List<PortAlias> portAliases, GUIDResponse response,
                                          String externalSourceName) {
        Set<String> portAliasGUIDs = new HashSet<>();

        if (CollectionUtils.isNotEmpty(portAliases)) {
            portAliases.parallelStream().forEach(portAlias -> {
                try {
                    portAliasGUIDs.add(upsertPortAliasWithDelegation(userId, serverName, portAlias, externalSourceName));
                } catch (InvalidParameterException error) {
                    restExceptionHandler.captureInvalidParameterException(response, error);
                } catch (PropertyServerException error) {
                    restExceptionHandler.capturePropertyServerException(response, error);
                } catch (UserNotAuthorizedException error) {
                    restExceptionHandler.captureUserNotAuthorizedException(response, error);
                }
            });
        }

        return portAliasGUIDs;
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

        if (relationalTableRequestBody.getDatabaseQualifiedName() == null) {
            restExceptionHandler.handleMissingValue("databaseQualifiedName", methodName);
            return false;
        }
        return true;
    }

    private boolean isRequestBodyInvalid(String userId, String serverName, DataEngineOMASAPIRequestBody requestBody, String methodName) throws
                                                                                                                                        InvalidParameterException {
        if (requestBody == null) {
            restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            return true;
        }
        if (requestBody.getExternalSourceName() == null) {
            restExceptionHandler.handleMissingValue("externalSourceName", methodName);
            return true;
        }
        return false;
    }

    public GUIDResponse upsertDataFile(String serverName, String userId, DataFileRequestBody dataFileRequestBody) {

        String methodName = "createDataFileAndSchema";
        GUIDResponse response = new GUIDResponse();
        String guid;

        try {
            DataEngineDataFileHandler dataFileHandler = instanceHandler.getDataFileHandler(userId, serverName, methodName);
            DataEngineRegistrationHandler registrationHandler = instanceHandler.getRegistrationHandler(userId, serverName, methodName);

            String externalSourceName = dataFileRequestBody.getExternalSourceName();
            String externalSourceGuid = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

            DataFile file = dataFileRequestBody.getDataFile();
            List<Attribute> columns = file.getColumns();
            SchemaType schemaType = getDefaultSchemaTypeIfAbsentAndAddAttributes(file, file.getSchema(), columns);

            Map<String, Object> extendedProperties = getExtendedProperties(file);
            String fileTypeName = file instanceof CSVFile ? CSV_FILE_TYPE_NAME : DATA_FILE_TYPE_NAME ;
            String fileTypeGuid = file instanceof CSVFile ? CSV_FILE_TYPE_GUID : DATA_FILE_TYPE_GUID ;

            guid = dataFileHandler.upsertFileAssetIntoCatalog(fileTypeName, fileTypeGuid, file, schemaType, columns,
                    extendedProperties, externalSourceGuid, externalSourceName, userId, methodName);

            response.setGUID(guid);

        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }
        return response;
    }

    private SchemaType getDefaultSchemaTypeIfAbsentAndAddAttributes(DataFile file, SchemaType schemaType, List<Attribute> attributes){
        if(schemaType == null){
            schemaType = new SchemaType();
            schemaType.setQualifiedName(file.getQualifiedName() +"::schema");
            schemaType.setDisplayName("Schema");
        }
        schemaType.setAttributeList(attributes);
        return schemaType;
    }

    private HashMap<String, Object> getExtendedProperties(DataFile file){
        HashMap<String, Object> extendedProperties = new HashMap<>();

        if(file instanceof CSVFile){
            CSVFile csvFile = (CSVFile) file;
            extendedProperties.put(FILE_TYPE_PROPERTY_NAME, csvFile.getFileType());
            extendedProperties.put(DELIMITER_CHARACTER_PROPERTY_NAME, csvFile.getDelimiterCharacter());
            extendedProperties.put(QUOTE_CHARACTER_PROPERTY_NAME, csvFile.getQuoteCharacter());
        } else {
            extendedProperties.put(FILE_TYPE_PROPERTY_NAME, file.getFileType());
        }
        return extendedProperties;
    }

}