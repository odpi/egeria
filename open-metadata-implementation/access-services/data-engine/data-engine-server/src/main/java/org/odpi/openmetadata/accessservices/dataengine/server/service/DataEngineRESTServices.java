/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.odpi.openmetadata.accessservices.dataengine.rest.PortImplementationRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortListRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.PortRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.ProcessRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.SoftwareServerCapabilityRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.SoftwareServerRegistrationHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * The DataEngineRESTServices provides the server-side implementation of the Data Engine Open Metadata Assess Service
 * (OMAS). This service provide the functionality to create processes, ports and wire relationships.
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
                    requestBody.getName(), requestBody.getDescription(), requestBody.getType(),
                    requestBody.getVersion(), requestBody.getPatchLevel(), requestBody.getSource()));

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
     * Return the properties from a discovery engine definition.
     *
     * @param serverName    name of the service to route the request to.
     * @param userId        identifier of calling user.
     * @param qualifiedName unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
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

    public GUIDResponse createSchemaType(String userId, String serverName, String schemaTypeGUID) {
        final String methodName = "createSchemaType";

        GUIDResponse response = new GUIDResponse();

        try {
            DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler =
                    instanceHandler.getDataEngineSchemaTypeHandler(userId, serverName, methodName);

            String newSchemaTypeGUID = dataEngineSchemaTypeHandler.createSchemaType(userId, serverName, schemaTypeGUID);

            dataEngineSchemaTypeHandler.addLineageMappingRelationship(userId, schemaTypeGUID, newSchemaTypeGUID);

            response.setGUID(newSchemaTypeGUID);

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
     * Create the port with a PortSchema relationship
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
            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

            String schemaTypeGUID = portImplementationRequestBody.getSchemaTypeGUID();

            String portGUID = portHandler.createPortImplementation(userId,
                    portImplementationRequestBody.getDisplayName(),
                    portImplementationRequestBody.getPortType());

            portHandler.addPortSchemaRelationship(userId, portGUID, schemaTypeGUID);

            response.setGUID(portGUID);

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
     * Create the port with a PortSchema relationship
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPortAlias(String userId, String serverName, PortRequestBody portRequestBody) {
        final String methodName = "createPortAlias";

        log.debug("Calling method: {}", methodName);

        if (portRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

            response.setGUID(portHandler.createPortAlias(portRequestBody.getDisplayName(), userId));

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
     * Create the port with a PortSchema relationship
     *
     * @param serverName      name of server instance to call
     * @param userId          the name of the calling user
     * @param portRequestBody properties of the port
     *
     * @return the unique identifier (guid) of the created port
     */
    public GUIDResponse createPortAliasOfPortAlias(String userId, String serverName,
                                                   PortRequestBody portRequestBody, String portAliasGUID) {
        final String methodName = "createPortAliasOfPortAliasGUID";

        log.debug("Calling method: {}", methodName);

        if (portRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

            String newPortAliasGUID = portHandler.createPortAlias(portRequestBody.getDisplayName(), userId);

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
     * Create the port with a PortSchema relationship
     *
     * @param serverName                    name of server instance to call
     * @param userId                        the name of the calling user
     * @param portImplementationRequestBody properties of the port
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
            PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

            String newPortImplementationGUID = portHandler.createPortImplementation(userId,
                    portImplementationRequestBody.getDisplayName(), portImplementationRequestBody.getPortType());

            portHandler.addPortSchemaRelationship(userId, newPortImplementationGUID,
                    portImplementationRequestBody.getSchemaTypeGUID());

            portHandler.addPortDelegationRelationship(userId, portAliasGUID, newPortImplementationGUID);

            response.setGUID(newPortImplementationGUID);

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

        if (processRequestBody == null) {
            return null;
        }

        String processName = processRequestBody.getName();
        String description = processRequestBody.getDescription();
        String latestChange = processRequestBody.getLatestChange();
        List<String> zoneMembership = processRequestBody.getZoneMembership();
        String displayName = processRequestBody.getDisplayName();
        String formula = processRequestBody.getFormula();
        String owner = processRequestBody.getOwner();
        OwnerType ownerType = processRequestBody.getOwnerType();
        List<String> ports = processRequestBody.getPorts();

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            String processGuid = processHandler.createProcess(userId, processName, description, latestChange,
                    zoneMembership, displayName, formula, owner, ownerType);

            processHandler.addProcessPortRelationships(userId, processGuid, ports);

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
     * Create ProcessPort relationships for an existing Process
     *
     * @param serverName          name of server instance to call
     * @param userId              the name of the calling user
     * @param portListRequestBody guids of ports and process
     *
     * @return the unique identifier (guid) of the updated process entity
     */
    public GUIDResponse addPortsToProcess(String userId, String serverName, String processGuid,
                                          PortListRequestBody portListRequestBody) {
        final String methodName = "createProcess";

        if (portListRequestBody == null) {
            return null;
        }

        GUIDResponse response = new GUIDResponse();

        try {
            ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

            processHandler.addProcessPortRelationships(userId, processGuid, portListRequestBody.getPorts());

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
}