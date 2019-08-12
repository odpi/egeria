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
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessListResponse;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessesRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.SoftwareServerRegistrationHandler;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
     * Create the software server capability entity
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @param requestBody properties of the server
     *
     * @return the unique identifier (guid) of the created server
     */
    public GUIDResponse createSoftwareServer(String serverName, String userId,
                                             SoftwareServerCapabilityRequestBody requestBody) {
        final String methodName = "createSoftwareServer";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (requestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            SoftwareServerRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName,
                    methodName);

            SoftwareServerCapability softwareServerCapability = requestBody.getSoftwareServerCapability();
            response.setGUID(handler.createSoftwareServerCapability(userId, softwareServerCapability.getQualifiedName(),
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

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }


    /**
     * Get the unique identifier from a software server capability definition
     *
     * @param serverName    name of the service to route the request to
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the server
     *
     * @return the unique identifier from a software server capability definition
     */
    public GUIDResponse getSoftwareServerByQualifiedName(String serverName, String userId, String qualifiedName) {
        final String methodName = "getSoftwareServerByQualifiedName";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            SoftwareServerRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName,
                    methodName);

            response.setGUID(handler.getSoftwareServerCapabilityByQualifiedName(userId, qualifiedName));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }

    /**
     * Create the Port Implementation with a PortSchema relationship
     *
     * @param serverName            name of server instance to call
     * @param userId                the name of the calling user
     * @param schemaTypeRequestBody properties of the schema type
     *
     * @return the unique identifier (guid) of the created schema type
     */
    public GUIDResponse createSchemaType(String userId, String serverName,
                                         SchemaTypeRequestBody schemaTypeRequestBody) {
        final String methodName = "createSchemaType";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (schemaTypeRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            String newSchemaTypeGUID = createSchemaType(userId, serverName, schemaTypeRequestBody.getSchemaType());
            response.setGUID(newSchemaTypeGUID);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }

    /**
     * Create the Port Implementation with a PortSchema relationship
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPortImplementation(String userId, String serverName,
                                                 PortImplementationRequestBody portImplementationRequestBody) {
        final String methodName = "createPortImplementation";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();
        try {
            if (portImplementationRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            String portImplementationGUID = createPortImplementationWithSchemaType(userId, serverName,
                    portImplementationRequestBody.getPortImplementation());

            response.setGUID(portImplementationGUID);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }


    /**
     * Create the Port Alias with a PortDelegation relationship
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param portAliasRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPortAlias(String userId, String serverName, PortAliasRequestBody portAliasRequestBody) {
        final String methodName = "createPortAliasWithDelegation";

        log.debug("Calling method: {}", methodName);

        GUIDResponse response = new GUIDResponse();

        try {
            if (portAliasRequestBody == null) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);
            PortAlias portAlias = portAliasRequestBody.getPort();

            response.setGUID(portHandler.createPortAliasWithDelegation(userId, portAlias.getQualifiedName(),
                    portAlias.getDisplayName(), portAlias.getPortType(), portAlias.getDelegatesTo()));

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }

    /**
     * Create the processes with ports, schema types and lineage mappings
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param processesRequestBody properties of the processes
     *
     * @return a list unique identifiers (GUIDs) of the created processes
     */
    public ProcessListResponse createProcesses(String userId, String serverName,
                                               ProcessesRequestBody processesRequestBody) {
        final String methodName = "createProcesses";

        log.debug("Calling method: {}", methodName);

        ProcessListResponse response = new ProcessListResponse();

        try {
            if (processesRequestBody == null || CollectionUtils.isEmpty(processesRequestBody.getProcesses())) {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
                return response;
            }

            List<GUIDResponse> guidResponses = createProcesses(userId, serverName, processesRequestBody.getProcesses());

            Predicate<? super GUIDResponse> processStatusPredicate =
                    guidResponse -> guidResponse.getRelatedHTTPCode() == HttpStatus.OK.value();
            Map<Boolean, List<GUIDResponse>> mappedResponses =
                    guidResponses.parallelStream().collect(Collectors.partitioningBy(processStatusPredicate));

            List<GUIDResponse> createdProcesses = getGuidResponses(response, mappedResponses);

            handleFailedProcesses(response, mappedResponses);

            createLineageMappings(userId, serverName, processesRequestBody.getLineageMappings(), response);

            createdProcesses.parallelStream().forEach(guidResponse -> updateProcessStatus(userId, serverName,
                    guidResponse.getGUID(), InstanceStatus.ACTIVE));
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }

    private void handleFailedProcesses(ProcessListResponse response, Map<Boolean, List<GUIDResponse>> mappedResponses) {
        List<GUIDResponse> failedProcesses = mappedResponses.get(Boolean.FALSE);

        response.setFailedGUIDs((failedProcesses.parallelStream().map(GUIDResponse::getGUID).collect(Collectors.toList())));
        failedProcesses.parallelStream().forEach(guidResponse -> captureException(guidResponse, response));
    }

    private List<GUIDResponse> getGuidResponses(ProcessListResponse response, Map<Boolean, List<GUIDResponse>> mappedResponses) {
        List<GUIDResponse> createdProcesses = mappedResponses.get(Boolean.TRUE);

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

    private VoidResponse updateProcessStatus(String userId, String serverName, String processGuid,
                                             InstanceStatus instanceStatus) {
        final String methodName = "updateProcessStatus";

        log.debug("Calling method: {}", methodName);

        VoidResponse response = new VoidResponse();

        try {
            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            processHandler.updateProcessStatus(userId, processGuid, instanceStatus);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }

    private List<GUIDResponse> createProcesses(String userId, String serverName, List<Process> processes) {
        Predicate<? super Process> hasPortImplementationsPredicate =
                process -> CollectionUtils.isNotEmpty(process.getPortImplementations());
        Map<Boolean, List<Process>> partitionedProcesses =
                processes.parallelStream().collect(Collectors.partitioningBy(hasPortImplementationsPredicate));

        List<GUIDResponse> guidResponses = new ArrayList<>();

        Consumer<Process> processConsumer = process -> guidResponses.add(createProcess(userId, serverName, process));
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
                processHandler.addProcessPortRelationship(userId, processGuid, portGUID);
            }

            response.setGUID(processGuid);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

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

            createLineageMappings(userId, serverName, lineageMappingsRequestBody.getLineageMappings(), response);
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

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
    private GUIDResponse createProcess(String userId, String serverName, Process process) {
        final String methodName = "createProcess";

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

        GUIDResponse response = new GUIDResponse();

        try {
            List<String> portGUIDs = createPortImplementations(userId, serverName, portImplementations);

            portGUIDs.addAll(createPortAliases(userId, portAliases, serverName));

            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            String processGuUID = processHandler.createProcess(userId, qualifiedName, processName, description,
                    latestChange, zoneMembership, displayName, formula, owner, ownerType);

            for (String portGUID : portGUIDs) {
                processHandler.addProcessPortRelationship(userId, processGuUID, portGUID);
            }

            createLineageMappings(userId, serverName, lineageMappings, response);

            response.setGUID(processGuUID);

            log.debug("Returning from method: {1} with response: {2}", methodName, response);

        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: {1} with response: {2}", methodName, response.toString());

        return response;
    }

    private List<String> createPortImplementations(String userId, String serverName,
                                                   List<PortImplementation> portImplementations) throws
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException {
        List<String> portImplementationGUIDs = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(portImplementations)) {
            for (PortImplementation portImplementation : portImplementations) {
                portImplementationGUIDs.add(createPortImplementationWithSchemaType(userId, serverName,
                        portImplementation));
            }
        }
        return portImplementationGUIDs;
    }

    private List<String> createPortAliases(String userId, List<PortAlias> portAliases, String serverName) throws
                                                                                                          InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException {
        final String methodName = "createPortAliases";

        log.debug("Calling method: {}", methodName);

        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        List<String> portAliasGUIDs = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(portAliases)) {
            for (PortAlias portAlias : portAliases) {
                portAliasGUIDs.add(portHandler.createPortAliasWithDelegation(userId, portAlias.getQualifiedName(),
                        portAlias.getDisplayName(), portAlias.getPortType(), portAlias.getDelegatesTo()));
            }
        }
        return portAliasGUIDs;
    }

    private void createLineageMappings(String userId, String serverName, List<LineageMapping> lineageMappings,
                                       FFDCResponseBase response) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException {
        final String methodName = "createLineageMappings";

        log.debug("Calling method: {}", methodName);

        if (CollectionUtils.isEmpty(lineageMappings)) {
            return;
        }

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        lineageMappings.parallelStream().forEach(lineageMapping -> {
            try {
                dataEngineSchemaTypeHandler.addLineageMappingRelationship(userId, lineageMapping.getSourceAttribute(),
                        lineageMapping.getTargetAttribute());
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

    private String createSchemaType(String userId, String serverName, SchemaType schemaType) throws
                                                                                             InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException {
        final String methodName = "createSchemaType";

        log.debug("Calling method: {}", methodName);

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        return dataEngineSchemaTypeHandler.createSchemaType(userId, schemaType.getQualifiedName(),
                schemaType.getDisplayName(), schemaType.getAuthor(), schemaType.getEncodingStandard(),
                schemaType.getUsage(), schemaType.getVersionNumber(), schemaType.getAttributeList());
    }

    private String createPortImplementationWithSchemaType(String userId, String serverName,
                                                          PortImplementation portImplementation) throws
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException {
        final String methodName = "createPortImplementationWithSchemaType";

        log.debug("Calling method: {}", methodName);

        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String schemaTypeGUID = createSchemaType(userId, serverName, portImplementation.getSchemaType());

        String portImplementationGUID = portHandler.createPortImplementation(userId,
                portImplementation.getQualifiedName(), portImplementation.getDisplayName(),
                portImplementation.getPortType());

        portHandler.addPortSchemaRelationship(userId, portImplementationGUID, schemaTypeGUID);

        return portImplementationGUID;
    }
}