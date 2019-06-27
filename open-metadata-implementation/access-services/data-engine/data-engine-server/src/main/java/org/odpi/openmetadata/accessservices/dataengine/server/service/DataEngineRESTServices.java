/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.collections.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.rest.LineageMappingsRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortAliasRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SchemaTypeRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.SoftwareServerRegistrationHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        if (requestBody == null) {
            return null;
        }
        GUIDResponse response = new GUIDResponse();

        try {
            SoftwareServerRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName,
                    methodName);

            response.setGUID(handler.createSoftwareServerCapability(userId, requestBody.getQualifiedName(),
                    requestBody.getDisplayName(), requestBody.getDescription(), requestBody.getEngineType(),
                    requestBody.getEngineVersion(), requestBody.getPatchLevel(), requestBody.getSource()));

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
            instanceHandler.getExceptionHandler().captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            instanceHandler.getExceptionHandler().capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            instanceHandler.getExceptionHandler().captureUserNotAuthorizedException(response, error);
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

        if (schemaTypeRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
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

        if (portImplementationRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
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

        if (portAliasRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
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
     * Create the Port Alias with a PortDelegation relationship to a Port Alias
     *
     * @param serverName           name of server instance to call
     * @param userId               the name of the calling user
     * @param portAliasRequestBody properties of the port
     * @param portAliasGUID        the unique identifier of the port alias
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPortAliasOfPortAlias(String userId, String serverName,
                                                   PortAliasRequestBody portAliasRequestBody, String portAliasGUID) {
        final String methodName = "createPortAliasOfPortAliasGUID";

        log.debug("Calling method: {}", methodName);

        if (portAliasRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);
            PortAlias portAlias = portAliasRequestBody.getPort();

            String newPortAliasGUID = portHandler.createPortAliasWithDelegation(userId, portAlias.getQualifiedName(),
                    portAlias.getDisplayName(), portAlias.getPortType(), portAlias.getDelegatesTo());

            portHandler.addPortDelegationRelationship(userId, portAliasGUID, newPortAliasGUID);

            response.setGUID(newPortAliasGUID);

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
     * Create the Port Implementation with a PortDelegation relationship to a Port Alias
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port
     * @param portAliasGUID                 the unique identifier of the port alias
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPortImplementationOfPortAlias(String userId, String serverName,
                                                            PortImplementationRequestBody portImplementationRequestBody,
                                                            String portAliasGUID) {
        final String methodName = "createPortImplementationOfPortAlias";

        log.debug("Calling method: {}", methodName);

        if (portImplementationRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            String portImplementationGUID = createPortImplementationWithSchemaType(userId, serverName,
                    portImplementationRequestBody.getPortImplementation());

            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);
            portHandler.addPortDelegationRelationship(userId, portAliasGUID, portImplementationGUID);

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
     * Create the process with ports and wires
     *
     * @param serverName         name of server instance to call
     * @param userId             the name of the calling user
     * @param processRequestBody properties of the process
     *
     * @return the unique identifier (guid) of the created process
     */
    public GUIDResponse createProcess(String userId, String serverName, ProcessRequestBody processRequestBody) {
        final String methodName = "createProcess";

        log.debug("Calling method: {}", methodName);

        if (processRequestBody == null) {
            return null;
        }

        org.odpi.openmetadata.accessservices.dataengine.model.Process process = processRequestBody.getProcess();
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
            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);
            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

            List<String> portGuids = createPortImplementations(userId, serverName, portImplementations);

            portGuids.addAll(createPortAliases(userId, portAliases, portHandler));

            String processGuid = processHandler.createProcess(userId, qualifiedName, processName, description,
                    latestChange, zoneMembership, displayName, formula, owner, ownerType);

            for (String portGUID : portGuids) {
                processHandler.addProcessPortRelationship(userId, processGuid, portGUID);
            }

            if (CollectionUtils.isNotEmpty(lineageMappings)) {
                for (LineageMapping lineageMapping : lineageMappings) {
                    createLineageMapping(userId, serverName, lineageMapping);
                }
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

        if (portListRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
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

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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

        if (lineageMappingsRequestBody == null) {
            return null;
        }

        VoidResponse response = new VoidResponse();
        try {
            for (LineageMapping lineageMapping : lineageMappingsRequestBody.getLineageMappings()) {
                createLineageMapping(userId, serverName, lineageMapping);
            }
        } catch (InvalidParameterException error) {
            restExceptionHandler.captureInvalidParameterException(response, error);
        } catch (PropertyServerException error) {
            restExceptionHandler.capturePropertyServerException(response, error);
        } catch (UserNotAuthorizedException error) {
            restExceptionHandler.captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }

    private List<String> createPortImplementations(String userId, String serverName,
                                                   List<PortImplementation> portImplementations) throws
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException {
        if (CollectionUtils.isEmpty(portImplementations)) {
            return new ArrayList<>();
        }

        List<String> portImplementationGUIDs = new ArrayList<>();
        for (PortImplementation portImplementation : portImplementations) {
            portImplementationGUIDs.add(createPortImplementationWithSchemaType(userId, serverName, portImplementation));
        }

        return portImplementationGUIDs;
    }

    private List<String> createPortAliases(String userId, List<PortAlias> portAliases, PortHandler portHandler) throws
                                                                                                                InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException {
        if (CollectionUtils.isEmpty(portAliases)) {
            return new ArrayList<>();
        }

        List<String> portAliasGUIDs = new ArrayList<>();
        for (PortAlias portAlias : portAliases) {
            portAliasGUIDs.add(portHandler.createPortAliasWithDelegation(userId, portAlias.getQualifiedName(),
                    portAlias.getDisplayName(), portAlias.getPortType(), portAlias.getDelegatesTo()));
        }

        return portAliasGUIDs;
    }

    private void createLineageMapping(String userId, String serverName, LineageMapping lineageMapping) throws
                                                                                                       InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException {
        final String methodName = "createLineageMapping";

        log.debug("Calling method: {}", methodName);

        DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

        dataEngineSchemaTypeHandler.addLineageMappingRelationship(userId, lineageMapping.getSourceColumn(),
                lineageMapping.getTargetColumn());
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
                schemaType.getUsage(), schemaType.getVersionNumber(), schemaType.getColumnList());
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