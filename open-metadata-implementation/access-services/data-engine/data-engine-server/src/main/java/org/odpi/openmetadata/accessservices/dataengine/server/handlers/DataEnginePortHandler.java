/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Port;
import org.odpi.openmetadata.accessservices.dataengine.model.PortImplementation;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PortBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.PortHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_IMPLEMENTATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_SCHEMA_RELATIONSHIP_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PORT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SCHEMA_TYPE_TYPE_NAME;


/**
 * PortHandler manages Port objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates port entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEnginePortHandler {
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final PortHandler<Port> portHandler;
    private final DataEngineRegistrationHandler registrationHandler;

    private static final String PROCESS_GUID_PARAMETER_NAME = "processGUID";
    private static final String PORT_GUID_PARAMETER_NAME = "portGUID";
    private static final String SCHEMA_TYPE_GUID_PARAMETER_NAME = "schemaTypeGUID";

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param dataEngineCommonHandler provides utilities for manipulating entities
     * @param portHandler             provides utilities for manipulating the repository services ports
     * @param registrationHandler     provides utilities for manipulating engine entities
     */
    public DataEnginePortHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                 OMRSRepositoryHelper repositoryHelper, DataEngineCommonHandler dataEngineCommonHandler,
                                 PortHandler<Port> portHandler, DataEngineRegistrationHandler registrationHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.portHandler = portHandler;
        this.registrationHandler = registrationHandler;
    }

    /**
     * Create the port implementation attached to a process.
     *
     * @param userId             the name of the calling user
     * @param port               the port implementation values
     * @param processGUID        the unique identifier of the process
     * @param externalSourceName the unique name of the external source
     * @return unique identifier of the port implementation in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createPortImplementation(String userId, PortImplementation port, String processGUID, String externalSourceName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "createPortImplementation";
        validatePortParameters(userId, port.getQualifiedName(), port.getDisplayName(), methodName);
        invalidParameterHandler.validateGUID(processGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        return portHandler.createPort(userId, externalSourceGUID, externalSourceName, processGUID, PROCESS_GUID_PARAMETER_NAME, port.getQualifiedName(),
                port.getDisplayName(), port.getPortType().getOrdinal(), port.getAdditionalProperties(), PORT_IMPLEMENTATION_TYPE_NAME,
                null, false, false, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Update the port implementation
     *
     * @param userId             the name of the calling user
     * @param originalPortEntity the created port entity
     * @param port               the port implementation new values
     * @param externalSourceName the external data engine
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updatePortImplementation(String userId, EntityDetail originalPortEntity, PortImplementation port,
                                         String externalSourceName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "updatePortImplementation";
        validatePortParameters(userId, port.getQualifiedName(), port.getDisplayName(), methodName);
        String portGUID = originalPortEntity.getGUID();

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        PortBuilder updatedPortBuilder = new PortBuilder(port.getQualifiedName(), port.getDisplayName(), port.getPortType().getOrdinal(),
                port.getAdditionalProperties(), externalSourceGUID, externalSourceName, null, repositoryHelper, serviceName, serverName);
        EntityDetail updatedPortEntity = dataEngineCommonHandler.buildEntityDetail(portGUID, updatedPortBuilder.getInstanceProperties(methodName));
        EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalPortEntity, updatedPortEntity, true);
        if (!entityDetailDifferences.hasInstancePropertiesDifferences()) {
            return;
        }

        portHandler.updatePort(userId, externalSourceGUID, externalSourceName, portGUID, PORT_GUID_PARAMETER_NAME, port.getQualifiedName(),
                port.getDisplayName(), port.getPortType().getOrdinal(), port.getAdditionalProperties(), PORT_IMPLEMENTATION_TYPE_NAME, null,
                null, null, false, false, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Create a PortSchema relationship between a Port and the corresponding SchemaType. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId             the name of the calling user
     * @param portGUID           the unique identifier of the port
     * @param schemaTypeGUID     the unique identifier of the schema type
     * @param externalSourceName the unique name of the external source
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addPortSchemaRelationship(String userId, String portGUID, String schemaTypeGUID, String externalSourceName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        final String methodName = "addPortSchemaRelationship";
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(portGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(schemaTypeGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        Optional<Relationship> relationship = dataEngineCommonHandler.findRelationship(userId, portGUID, schemaTypeGUID,
                PORT_TYPE_NAME, SCHEMA_TYPE_TYPE_NAME, PORT_SCHEMA_RELATIONSHIP_TYPE_NAME);
        if (relationship.isEmpty()) {
            String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
            portHandler.setupPortSchemaType(userId, externalSourceGUID, externalSourceName, portGUID, PORT_GUID_PARAMETER_NAME,
                    schemaTypeGUID, SCHEMA_TYPE_GUID_PARAMETER_NAME, null, null, false, false,
                    dataEngineCommonHandler.getNow(), methodName);
        }
    }

    /**
     * Retrieve the schema type that is linked to the port
     *
     * @param userId   the name of the calling user
     * @param portGUID the unique identifier of the port
     * @return retrieved entity or an empty optional
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findSchemaTypeForPort(String userId, String portGUID) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        return dataEngineCommonHandler.getEntityForRelationship(userId, portGUID, PORT_SCHEMA_RELATIONSHIP_TYPE_NAME,
                PORT_TYPE_NAME);
    }

    /**
     * Remove the port
     *
     * @param userId             the name of the calling user
     * @param portGUID           unique identifier of the port to be removed
     * @param externalSourceName the external data engine
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removePort(String userId, String portGUID, String externalSourceName, DeleteSemantic deleteSemantic) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException,
            FunctionNotSupportedException {
        final String methodName = "removePort";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        portHandler.removePort(userId, externalSourceGUID, externalSourceName, portGUID, PORT_GUID_PARAMETER_NAME,
                false, false, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Find out if the PortImplementation object is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     * @return optional with entity details if found, empty optional if not found
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findPortImplementationEntity(String userId, String qualifiedName) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, PORT_IMPLEMENTATION_TYPE_NAME);
    }

    private void validatePortParameters(String userId, String qualifiedName, String displayName, String methodName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(displayName, DISPLAY_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }
}
