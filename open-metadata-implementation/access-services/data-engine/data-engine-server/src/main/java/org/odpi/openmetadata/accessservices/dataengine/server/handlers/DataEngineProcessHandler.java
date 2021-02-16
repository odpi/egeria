/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ProcessConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
    import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine OMAS
 * and creates process entities and relationships through the OMRSRepositoryConnector.
 */
public class DataEngineProcessHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final DataEngineAssetHandler<Process> dataEngineAssetHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param dataEngineAssetHandler            provides utilities for manipulating the repository services assets
     * @param dataEngineCommonHandler provides utilities for manipulating entities
     * @param registrationHandler     creates software server capability entities
     **/
    public DataEngineProcessHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                    RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                                    DataEngineAssetHandler<Process> dataEngineAssetHandler, DataEngineRegistrationHandler registrationHandler,
                                    DataEngineCommonHandler dataEngineCommonHandler) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.dataEngineAssetHandler = dataEngineAssetHandler;
        this.registrationHandler = registrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Create the process
     *
     * @param userId             the name of the calling user
     * @param process            the values of the process
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the process in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String createProcess(String userId, Process process, String externalSourceName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException {
        final String methodName = "createProcess";

        validateProcessParameters(userId, process.getQualifiedName(), methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        return dataEngineAssetHandler.createAssetInRepository(userId, externalSourceGUID, externalSourceName, process.getName(),
                process.getQualifiedName(), process.getName(), process.getDescription(), process.getZoneMembership(),
                process.getOwner(), process.getOwnerType(), process.getAdditionalProperties(),
                ProcessPropertiesMapper.PROCESS_TYPE_GUID, ProcessPropertiesMapper.PROCESS_TYPE_NAME, process.getFormula(),
                process.getExtendedProperties(), InstanceStatus.DRAFT, methodName);
    }

    /**
     * Update the process
     *
     * @param userId                the name of the calling user
     * @param originalProcessEntity the created process entity
     * @param updatedProcess        the new values of the process
     * @param externalSourceName the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateProcess(String userId, EntityDetail originalProcessEntity, Process updatedProcess, String externalSourceName) throws InvalidParameterException,
            UserNotAuthorizedException, PropertyServerException{

        final String methodName = "updateProcess";

        validateProcessParameters(userId, updatedProcess.getQualifiedName(), methodName);

        String processGUID = originalProcessEntity.getGUID();
        Process originalProcess = getProcess(originalProcessEntity);

        ProcessPropertiesBuilder updatedProcessBuilder = getProcessPropertiesBuilder(updatedProcess, methodName, userId);

        EntityDetail updatedProcessEntity = dataEngineCommonHandler.buildEntityDetail(processGUID,
                updatedProcessBuilder.getInstanceProperties(methodName));
        EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalProcessEntity,
                updatedProcessEntity, true);
        if (!entityDetailDifferences.hasInstancePropertiesDifferences()) {
            return;
        }

        String externalSourceGUID = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        dataEngineAssetHandler.updateAsset(userId, externalSourceGUID, externalSourceName, processGUID,
                ProcessPropertiesMapper.PROCESS_GUID_PROPERTY_NAME, originalProcess.getQualifiedName(), originalProcess.getName(),
                originalProcess.getDescription(), originalProcess.getAdditionalProperties(), ProcessPropertiesMapper.PROCESS_TYPE_GUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, originalProcess.getExtendedProperties(), true, methodName);
    }

    Process getProcess(EntityDetail originalProcessEntity) throws PropertyServerException {
        ProcessConverter processConverter = new ProcessConverter(repositoryHelper, serviceName, serverName);
        return processConverter.getProcessBean(Process.class, originalProcessEntity);
    }

    /**
     * Find out if the Process object is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findProcessEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                                PropertyServerException,
                                                                                                InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
    }

    /**
     * Create ProcessPort relationships between a Process asset and the corresponding Ports. Verifies that the
     * relationship is not present before creating it
     *
     * @param userId             the name of the calling user
     * @param processGUID        the unique identifier of the process
     * @param portGUID           the unique identifier of the port
     * @param externalSourceName the unique name of the external source
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addProcessPortRelationship(String userId, String processGUID, String portGUID, String externalSourceName)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        dataEngineCommonHandler.upsertExternalRelationship(userId, processGUID, portGUID,
                ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME, externalSourceName,
                null);
    }

    /**
     * Update the process instance status
     *
     * @param userId                the name of the calling user
     * @param processGUID           the process GUID
     * @param instanceStatus        the status of the process
     * @param externalSourceName    the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateProcessStatus(String userId, String processGUID, InstanceStatus instanceStatus, String externalSourceName)
            throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {

        final String methodName = "updateProcessStatus";
        final String processGUIDParameterName = "processGUID";
        final String newProcessStatusParameterName = "newProcessStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
        String externalSourceGUID = registrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        dataEngineAssetHandler.updateBeanStatusInRepository(userId, externalSourceGUID, externalSourceName, processGUID,
                processGUIDParameterName, entityTypeDef.getGUID(), entityTypeDef.getName(), instanceStatus,
                newProcessStatusParameterName, methodName);
    }

    /**
     * Retrieve all port objects that are connected to the process
     *
     * @param userId       the name of the calling user
     * @param processGUID  the unique identifier of the process
     * @param portTypeName the type of the port to be retrieved
     *
     * @return A set of unique identifiers for the retrieved ports or an empty set
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Set<String> getPortsForProcess(String userId, String processGUID, String portTypeName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException {
        final String methodName = "getPortsForProcess";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME);

        List<EntityDetail> entities = repositoryHandler.getEntitiesForRelationshipType(userId, processGUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, relationshipTypeDef.getGUID(), relationshipTypeDef.getName(), 0, 0, methodName);

        if (CollectionUtils.isEmpty(entities)) {
            return new HashSet<>();
        }

        return entities.parallelStream().filter(entityDetail -> entityDetail.getType().getTypeDefName().equalsIgnoreCase(portTypeName))
                .map(InstanceHeader::getGUID).collect(Collectors.toSet());
    }

    private void validateProcessParameters(String userId, String qualifiedName, String methodName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    ProcessPropertiesBuilder getProcessPropertiesBuilder(Process process, String methodName, String userId) throws InvalidParameterException {
        return new ProcessPropertiesBuilder(process.getQualifiedName(), process.getDisplayName(), process.getName(),
                process.getDescription(),process.getZoneMembership(), process.getOwner(), process.getOwnerType(),
                process.getTypeGUID(), process.getTypeName(), process.getFormula(), process.getAdditionalProperties(),
                process.getExtendedProperties(), InstanceStatus.DRAFT, repositoryHelper, serverName, serviceName, userId, methodName);
    }

    public void upsertProcessHierarchyRelationship(String userId, ParentProcess parentProcess, String processGUID,
                                                   String externalSourceName) throws InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "upsertProcessHierarchyRelationship";

        ProcessContainmentType processContainmentType = parentProcess.getProcessContainmentType();
        InstanceProperties relationshipProperties = repositoryHelper.addEnumPropertyToInstance(serviceName, null,
                ProcessPropertiesMapper.CONTAINMENT_TYPE, processContainmentType.getOrdinal(), processContainmentType.getName(),
                processContainmentType.getDescription(), methodName);

        Optional<EntityDetail> parentProcessEntity = findProcessEntity(userId, parentProcess.getQualifiedName());
        if (parentProcessEntity.isPresent()) {
            dataEngineCommonHandler.upsertExternalRelationship(userId, parentProcessEntity.get().getGUID(), processGUID,
                    ProcessPropertiesMapper.PROCESS_HIERARCHY_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME, externalSourceName,
                    relationshipProperties);
        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.PROCESS_NOT_FOUND, methodName,
                    parentProcess.getQualifiedName());
        }
    }
}
