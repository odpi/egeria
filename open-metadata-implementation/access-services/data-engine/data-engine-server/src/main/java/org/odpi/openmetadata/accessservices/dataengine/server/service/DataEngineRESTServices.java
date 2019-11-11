/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.NoSchemaAttributeException;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.rest.*;
import org.odpi.openmetadata.accessservices.dataengine.rest.DataEngineRegistrationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;

/**
 * The DataEngineRESTServices provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This service provide the functionality to create processes, ports with schema types and corresponding
 * relationships.
 */
public class DataEngineRESTServices {

    private static final Logger log = LoggerFactory.getLogger(DataEngineRESTServices.class);

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private final DataEngineInstanceHandler instanceHandler = new DataEngineInstanceHandler();

    /**
     * Default constructor
     */
    public DataEngineRESTServices() {
    }

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

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (requestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName,
                    methodName);

            SoftwareServerCapability softwareServerCapability = requestBody.getSoftwareServerCapability();
            response.setGUID(handler.createExternalDataEngine(userId, softwareServerCapability.getQualifiedName(),
                    softwareServerCapability.getDisplayName(), softwareServerCapability.getDescription(),
                    softwareServerCapability.getEngineType(), softwareServerCapability.getEngineVersion(),
                    softwareServerCapability.getPatchLevel(), softwareServerCapability.getSource()));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

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

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName,
                    methodName);

            response.setGUID(handler.getExternalDataEngineByQualifiedName(userId, qualifiedName));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

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
    public GUIDResponse createOrUpdateSchemaType(String userId, String serverName,
                                                 SchemaTypeRequestBody schemaTypeRequestBody) {
        final String methodName = "createOrUpdateSchemaType";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (schemaTypeRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            String newSchemaTypeGUID = createOrUpdateSchemaType(userId, serverName,
                    schemaTypeRequestBody.getSchemaType(), schemaTypeRequestBody.getExternalSourceName());

            response.setGUID(newSchemaTypeGUID);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

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
    public GUIDResponse createOrUpdatePortImplementation(String userId, String serverName,
                                                         PortImplementationRequestBody portImplementationRequestBody) {
        final String methodName = "createOrUpdatePortImplementation";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();
        try {
            if (portImplementationRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            String portImplementationGUID = createOrUpdatePortImplementationWithSchemaType(userId, serverName,
                    portImplementationRequestBody.getPortImplementation(),
                    portImplementationRequestBody.getExternalSourceName());

            response.setGUID(portImplementationGUID);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

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
    public GUIDResponse createOrUpdatePortAlias(String userId, String serverName,
                                                PortAliasRequestBody portAliasRequestBody) {
        final String methodName = "createOrUpdatePortAliasWithDelegation";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (portAliasRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            response.setGUID(createOrUpdatePortAliasWithDelegation(userId, serverName, portAliasRequestBody.getPort(),
                    portAliasRequestBody.getExternalSourceName()));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

        return response;
    }

    /**
     * Create or update the processes with ports, schema types and lineage mappings
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param processesRequestBody properties of the processes
     *
     * @return a list unique identifiers (GUIDs) of the created/updated processes
     */
    public ProcessListResponse createOrUpdateProcesses(String userId, String serverName,
                                                       ProcessesRequestBody processesRequestBody) {
        final String methodName = "createOrUpdateProcesses";

        log.debug("Calling method: {}", methodName);

        ProcessListResponse response = new ProcessListResponse();

        try {
            if (processesRequestBody == null || CollectionUtils.isEmpty(processesRequestBody.getProcesses())) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            List<GUIDResponse> guidResponses = createOrUpdateProcesses(userId, serverName,
                    processesRequestBody.getProcesses(),
                    processesRequestBody.getExternalSourceName());

            Predicate<? super GUIDResponse> processStatusPredicate =
                    guidResponse -> guidResponse.getRelatedHTTPCode() == HttpStatus.OK.value();
            Map<Boolean, List<GUIDResponse>> mappedResponses =
                    guidResponses.parallelStream().collect(partitioningBy(processStatusPredicate));

            List<GUIDResponse> createdProcesses = getGuidResponses(response, mappedResponses.get(Boolean.TRUE));

            handleFailedProcesses(response, mappedResponses.get(Boolean.FALSE));

            createdProcesses.parallelStream().forEach(guidResponse -> updateProcessStatus(userId, serverName,
                    guidResponse, InstanceStatus.ACTIVE));
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

        return response;
    }

    public void handleFailedProcesses(ProcessListResponse response, List<GUIDResponse> failedProcesses) {
        response.setFailedGUIDs((failedProcesses.parallelStream().map(GUIDResponse::getGUID).collect(Collectors.toList())));
        failedProcesses.parallelStream().forEach(guidResponse -> captureException(guidResponse, response));
    }

    public List<GUIDResponse> getGuidResponses(ProcessListResponse response, List<GUIDResponse> createdProcesses) {
        response.setGUIDs(createdProcesses.parallelStream().map(GUIDResponse::getGUID).collect(Collectors.toList()));

        return createdProcesses;
    }

    private void captureException(FFDCResponseBase guidResponse, GUIDListResponse response) {
        response.setExceptionErrorMessage(guidResponse.getExceptionErrorMessage());
        response.setExceptionClassName(guidResponse.getExceptionClassName());
        response.setExceptionSystemAction(guidResponse.getExceptionSystemAction());
        response.setExceptionUserAction(guidResponse.getExceptionUserAction());
        response.setRelatedHTTPCode(guidResponse.getRelatedHTTPCode());
        response.setExceptionProperties(guidResponse.getExceptionProperties());
    }

    public void updateProcessStatus(String userId, String serverName, GUIDResponse response,
                                     InstanceStatus instanceStatus) {
        final String methodName = "updateProcessStatus";

        log.debug("Calling method: {}", methodName);

        try {
            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            processHandler.updateProcessStatus(userId, response.getGUID(), instanceStatus);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());
    }

    public List<GUIDResponse> createOrUpdateProcesses(String userId, String serverName, List<Process> processes,
                                                       String externalSourceName) {
        Predicate<? super Process> hasPortImplementationsPredicate =
                process -> CollectionUtils.isNotEmpty(process.getPortImplementations());
        Map<Boolean, List<Process>> partitionedProcesses =
                processes.parallelStream().collect(partitioningBy(hasPortImplementationsPredicate));

        List<GUIDResponse> guidResponses = new ArrayList<>();

        Consumer<Process> processConsumer = process -> guidResponses.add(createOrUpdateProcess(userId, serverName,
                process, externalSourceName));
        partitionedProcesses.get(Boolean.TRUE).parallelStream().forEach(processConsumer);
        partitionedProcesses.get(Boolean.FALSE).parallelStream().forEach(processConsumer);

        return guidResponses;
    }

    /**
     * Create ProcessPort relationships for an existing Process
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param portListRequestBody guids of ports
     *
     * @return the unique identifier (guid) of the updated process entity
     */
    public GUIDResponse addPortsToProcess(String userId, String serverName, String processGuid,
                                          PortListRequestBody portListRequestBody) {
        final String methodName = "addPortsToProcess";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (portListRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            for (String portGUID : portListRequestBody.getPorts()) {
                processHandler.addProcessPortRelationship(userId, processGuid, portGUID, portListRequestBody.getExternalSourceName());
            }

            response.setGUID(processGuid);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

        return response;
    }

    /**
     * Create LineageMappings relationships between schema types
     *
     * @param userId                     the name of the calling user
     * @param serverName                 ame of server instance to call
     * @param lineageMappingsRequestBody list of lineage mappings
     *
     * @return void response
     */
    public VoidResponse addLineageMappings(String userId, String serverName,
                                           LineageMappingsRequestBody lineageMappingsRequestBody) {
        final String methodName = "addLineageMappings";

        log.debug("Calling method: {}", methodName);

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

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

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
    public GUIDResponse createOrUpdateProcess(String userId, String serverName, Process process,
                                               String externalSourceName) {
        final String methodName = "createOrUpdateProcess";

        log.debug("Calling method: {}", methodName);

        String qualifiedName = process.getQualifiedName();
        String processName = process.getName();
        String description = process.getDescription();
        String latestChange = process.getLatestChange();
        List<String> zoneMembership = process.getZoneMembership();
        String displayName = process.getDisplayName();
        String formula = process.getFormula();
        String owner = process.getOwner();
        OwnerType ownerType = process.getOwnerType();
        List<PortImplementation> portImplementations = process.getPortImplementations();
        List<PortAlias> portAliases = process.getPortAliases();
        List<LineageMapping> lineageMappings = process.getLineageMappings();
        UpdateSemantic updateSemantic = process.getUpdateSemantic();

        GUIDResponse response = new GUIDResponse();

        try {
            Set<String> portImplementationGUIDs = createOrUpdatePortImplementations(userId, serverName,
                    portImplementations, response, externalSourceName);

            Set<String> portAliasGUIDs = createOrUpdatePortAliases(userId, serverName, portAliases, response, externalSourceName);

            //check intermediary status of the response after creating the ports
            if (response.getRelatedHTTPCode() != HttpStatus.OK.value()) {
                return response;
            }

            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            String processGUID = processHandler.findProcess(userId, qualifiedName);

            if (StringUtils.isEmpty(processGUID)) {
                processGUID = processHandler.createProcess(userId, qualifiedName, processName, description,
                        latestChange, zoneMembership, displayName, formula, owner, ownerType, externalSourceName);
            } else {
                processHandler.updateProcess(userId, processGUID, qualifiedName, processName, description,
                        latestChange, zoneMembership, displayName, formula, owner, ownerType);
                processHandler.updateProcessStatus(userId, processGUID, InstanceStatus.DRAFT);

                if (updateSemantic == UpdateSemantic.REPLACE) {
                    deleteObsoletePorts(userId, serverName, portImplementationGUIDs, processGUID,
                            PortPropertiesMapper.PORT_IMPLEMENTATION_TYPE_NAME, response);

                    deleteObsoletePorts(userId, serverName, portAliasGUIDs, processGUID,
                            PortPropertiesMapper.PORT_ALIAS_TYPE_NAME, response);
                }
            }

            addProcessPortRelationships(userId, serverName, processGUID,
                    Stream.concat(portImplementationGUIDs.stream(), portAliasGUIDs.stream()).collect(Collectors.toSet()), response,
                    externalSourceName);

            addLineageMappings(userId, serverName, lineageMappings, response, externalSourceName);

            response.setGUID(processGUID);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {} with response: {}", methodName, response.toString());

        return response;
    }

    private void addProcessPortRelationships(String userId, String serverName, String processGUID,
                                             Set<String> portGUIDs, GUIDResponse response,
                                             String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        final String methodName = "addProcessPortRelationships";

        ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

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

    private void deleteObsoletePorts(String userId, String serverName, Set<String> newPortGUIDs,
                                     String processGUID, String portTypeName, GUIDResponse response) throws
                                                                                                     InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException {
        final String methodName = "deleteObsoletePorts";

        log.debug("Calling method: {}", methodName);

        if (CollectionUtils.isEmpty(newPortGUIDs)) {
            return;
        }

        ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        Set<String> oldPortGUIDs = processHandler.getPortsForProcess(userId, processGUID);

        // delete ports that are not in the process payload anymore
        List<String> obsoletePorts =
                oldPortGUIDs.parallelStream().collect(partitioningBy(newPortGUIDs::contains)).get(Boolean.FALSE);
        obsoletePorts.parallelStream().forEach(portGUID -> {
            try {
                portHandler.removePort(userId, portGUID, portTypeName);
            } catch (InvalidParameterException error) {
                restExceptionHandler.captureInvalidParameterException(response, error);
            } catch (PropertyServerException error) {
                restExceptionHandler.capturePropertyServerException(response, error);
            } catch (UserNotAuthorizedException error) {
                restExceptionHandler.captureUserNotAuthorizedException(response, error);
            }
        });

        log.debug("Returning from method: {} with void response", methodName);
    }


    private Set<String> createOrUpdatePortImplementations(String userId, String serverName,
                                                          List<PortImplementation> portImplementations,
                                                          GUIDResponse response,
                                                          String externalSourceName) {
        final String methodName = "createOrUpdatePortImplementations";

        log.debug("Calling method: {}", methodName);

        Set<String> portImplementationGUIDs = new HashSet<>();

        if (CollectionUtils.isNotEmpty(portImplementations)) {
            portImplementations.parallelStream().forEach(portImplementation ->
            {
                try {
                    portImplementationGUIDs.add(createOrUpdatePortImplementationWithSchemaType(userId, serverName,
                            portImplementation, externalSourceName));
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

    private Set<String> createOrUpdatePortAliases(String userId, String serverName, List<PortAlias> portAliases,
                                                  GUIDResponse response, String externalSourceName) {
        final String methodName = "createOrUpdatePortAliases";

        log.debug("Calling method: {}", methodName);

        Set<String> portAliasGUIDs = new HashSet<>();

        if (CollectionUtils.isNotEmpty(portAliases)) {

            // port aliases can not be processed in parallel, as multiple processes can define the same port alias
            portAliases.forEach(portAlias ->
            {
                try {
                    portAliasGUIDs.add(createOrUpdatePortAliasWithDelegation(userId, serverName, portAlias, externalSourceName));
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

    private void addLineageMappings(String userId, String serverName, List<LineageMapping> lineageMappings,
                                    FFDCResponseBase response, String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "addLineageMappings";

        log.debug("Calling method: {}", methodName);

        if (CollectionUtils.isEmpty(lineageMappings)) {
            return;
        }

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        lineageMappings.parallelStream().forEach(lineageMapping -> {
            try {
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(userId, lineageMapping.getSourceAttribute(),
                        lineageMapping.getTargetAttribute(), externalSourceName);
            } catch (InvalidParameterException error) {
                restExceptionHandler.captureInvalidParameterException(response, error);
            } catch (PropertyServerException error) {
                restExceptionHandler.capturePropertyServerException(response, error);
            } catch (UserNotAuthorizedException error) {
                restExceptionHandler.captureUserNotAuthorizedException(response, error);
            } catch (NoSchemaAttributeException error) {
                captureNoSchemaAttributeException(response, error);
            }
        });
    }

    private void captureNoSchemaAttributeException(FFDCResponseBase response, NoSchemaAttributeException error) {
        {
            response.setRelatedHTTPCode(error.getReportedHTTPCode());
            response.setExceptionClassName(NoSchemaAttributeException.class.getName());
            response.setExceptionErrorMessage(error.getErrorMessage());
            response.setExceptionSystemAction(error.getReportedSystemAction());
            response.setExceptionUserAction(error.getReportedUserAction());
        }

    }

    private String createOrUpdateSchemaType(String userId, String serverName, SchemaType schemaType,
                                            String externalSourceName) throws
                                                                                                 InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException {
        final String methodName = "createOrUpdateSchemaType";

        log.debug("Calling method: {}", methodName);

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        String schemaTypeGUID = dataEngineSchemaTypeHandler.createOrUpdateSchemaType(userId,
                schemaType.getQualifiedName(), schemaType.getDisplayName(), schemaType.getAuthor(),
                schemaType.getEncodingStandard(), schemaType.getUsage(), schemaType.getVersionNumber(),
                schemaType.getAttributeList(), externalSourceName);

        log.debug("Returning from method: {} with response: {}", methodName, schemaTypeGUID);

        return schemaTypeGUID;
    }

    public String createOrUpdatePortImplementationWithSchemaType(String userId,
                                                                  String serverName,
                                                                  PortImplementation portImplementation,
                                                                  String externalSourceName )throws
                                                                                             InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException {
        final String methodName = "createOrUpdatePortImplementationWithSchemaType";

        log.debug("Calling method: {}", methodName);

        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String schemaTypeGUID = createOrUpdateSchemaType(userId, serverName, portImplementation.getSchemaType(), externalSourceName);

        String portImplementationGUID = portHandler.findPortImplementation(userId,
                portImplementation.getQualifiedName());

        if (StringUtils.isEmpty(portImplementationGUID)) {
            portImplementationGUID = portHandler.createPortImplementation(userId, portImplementation.getQualifiedName(),
                    portImplementation.getDisplayName(), portImplementation.getPortType(), externalSourceName);
        } else {
            portHandler.updatePortImplementation(userId, portImplementationGUID, portImplementation.getQualifiedName(),
                    portImplementation.getDisplayName(), portImplementation.getPortType());

            if (portImplementation.getUpdateSemantic() == UpdateSemantic.REPLACE) {
                deleteObsoleteSchemaType(userId, serverName, schemaTypeGUID, portHandler.getSchemaTypeForPort(userId,
                        portImplementationGUID));
            }
        }

        portHandler.addPortSchemaRelationship(userId, portImplementationGUID, schemaTypeGUID, externalSourceName);

        log.debug("Returning from method: {} with response: {}", methodName, portImplementationGUID);

        return portImplementationGUID;
    }

    private void deleteObsoleteSchemaType(String userId, String serverName, String schemaTypeGUID,
                                          String oldSchemaTypeGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException {
        final String methodName = "deleteObsoleteSchemaType";

        log.debug("Calling method: {}", methodName);

        if (!oldSchemaTypeGUID.equalsIgnoreCase(schemaTypeGUID)) {
            DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                    instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

            dataEngineSchemaTypeHandler.removeSchemaType(userId, oldSchemaTypeGUID);
        }

        log.debug("Returning from method: {} with void response: {}", methodName);
    }

    public String createOrUpdatePortAliasWithDelegation(String userId, String serverName, PortAlias portAlias,
                                                         String externalSourceName) throws
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException {
        final String methodName = "createOrUpdatePortAliasWithDelegation";

        log.debug("Calling method: {}", methodName);

        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String portAliasGUID = portHandler.findPortAlias(userId, portAlias.getQualifiedName());

        if (StringUtils.isEmpty(portAliasGUID)) {
            portAliasGUID = portHandler.createPortAlias(userId, portAlias.getQualifiedName(),
                    portAlias.getDisplayName(), portAlias.getPortType(), externalSourceName);
        } else {
            portHandler.updatePortAlias(userId, portAliasGUID, portAlias.getQualifiedName(), portAlias.getDisplayName(),
                    portAlias.getPortType());
        }

        if (!StringUtils.isEmpty(portAlias.getDelegatesTo())) {
            portHandler.addPortDelegationRelationship(userId, portAliasGUID, portAlias.getPortType(),
                    portAlias.getDelegatesTo(), externalSourceName);
        }

        log.debug("Returning from method: {} with response: {}", methodName, portAliasGUID);

        return portAliasGUID;
    }

}