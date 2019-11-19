package org.odpi.openmetadata.accessservices.dataengine.server.service;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.PortAlias;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
import org.odpi.openmetadata.accessservices.dataengine.model.UpdateSemantic;
import org.odpi.openmetadata.accessservices.dataengine.server.admin.DataEngineInstanceHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineRegistrationHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineSchemaTypeHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.PortHandler;
import org.odpi.openmetadata.accessservices.dataengine.server.handlers.ProcessHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.List;

class DataEngineServices {
    private static final Logger log = LoggerFactory.getLogger(DataEngineServices.class);

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private final DataEngineInstanceHandler instanceHandler = new DataEngineInstanceHandler();


    public String createOrUpdatePortAliasWithDelegation(String userId, String serverName, PortAlias portAlias,
                                                        String externalSourceName) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException {
        final String methodName = "createOrUpdatePortAliasWithDelegation";

        log.debug("Calling method: {}", methodName);

        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String portAliasGUID = portHandler.findPortAlias(userId, portAlias.getQualifiedName());

        if (StringUtils.isEmpty(portAliasGUID)) {
            portAliasGUID = portHandler.createPortAlias(userId, portAlias, externalSourceName);
        } else {
            portHandler.updatePortAlias(userId, portAliasGUID, portAlias);
        }

        if (!StringUtils.isEmpty(portAlias.getDelegatesTo())) {
            portHandler.addPortDelegationRelationship(userId, portAliasGUID, portAlias.getPortType(),
                    portAlias.getDelegatesTo(), externalSourceName);
        }

        log.debug("Returning from method: {} with response: {}", methodName, portAliasGUID);

        return portAliasGUID;
    }

    public String createOrUpdatePortImplementationWithSchemaType(String userId, String serverName,
                                                                 PortImplementation portImplementation,
                                                                 String externalSourceName) throws
                                                                                            InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException {
        final String methodName = "createOrUpdatePortImplementationWithSchemaType";

        log.debug("Calling method: {}", methodName);

        PortHandler portHandler = instanceHandler.getPortHandler(userId, serverName, methodName);

        String schemaTypeGUID = createOrUpdateSchemaType(userId, serverName, portImplementation.getSchemaType(),
                externalSourceName);

        String portImplementationGUID = portHandler.findPortImplementation(userId,
                portImplementation.getQualifiedName());

        if (StringUtils.isEmpty(portImplementationGUID)) {
            portImplementationGUID = portHandler.createPortImplementation(userId, portImplementation,
                    externalSourceName);
        } else {
            portHandler.updatePortImplementation(userId, portImplementationGUID, portImplementation);

            if (portImplementation.getUpdateSemantic() == UpdateSemantic.REPLACE) {
                deleteObsoleteSchemaType(userId, serverName, schemaTypeGUID, portHandler.findSchemaTypeForPort(userId,
                        portImplementationGUID));
            }
        }

        portHandler.addPortSchemaRelationship(userId, portImplementationGUID, schemaTypeGUID, externalSourceName);

        log.debug("Returning from method: {} with response: {}", methodName, portImplementationGUID);

        return portImplementationGUID;
    }

    String createOrUpdateSchemaType(String userId, String serverName, SchemaType schemaType,
                                    String externalSourceName) throws InvalidParameterException,
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

    public String createExternalDataEngine(String userId, String serverName,
                                           SoftwareServerCapability softwareServerCapability) throws
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException {
        final String methodName = "createExternalDataEngine";

        log.debug("Calling method: {}", methodName);

        if (softwareServerCapability == null) {
            return null;
        }

        DataEngineRegistrationHandler handler = instanceHandler.getRegistrationHandler(userId, serverName,
                methodName);

        return handler.createExternalDataEngine(userId, softwareServerCapability);
    }

    public void addPortsToProcess(String userId, String serverName, String processGUID, List<String> portGUIDs,
                                  String externalSourceName) throws InvalidParameterException, PropertyServerException,
                                                                    UserNotAuthorizedException {
        final String methodName = "addPortsToProcess";

        log.debug("Calling method: {}", methodName);

        if (CollectionUtils.isEmpty(portGUIDs)) {
            return;
        }
        ProcessHandler processHandler = instanceHandler.getProcessHandler(userId, serverName, methodName);

        for (String portGUID : portGUIDs) {
            processHandler.addProcessPortRelationship(userId, processGUID, portGUID, externalSourceName);
        }
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

    public void addLineageMappings(String userId, String serverName, List<LineageMapping> lineageMappings,
                                   FFDCResponseBase response, String externalSourceName) throws
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException {
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
            }
        });
    }
}
