/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_HIERARCHY_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_PORT_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESS_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;

/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine OMAS
 * and creates process entities and relationships through the OMRSRepositoryConnector.
 */
public class DataEngineProcessHandler {
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final AssetHandler<Process> assetHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;

    public static final String PROCESS_GUID_PARAMETER_NAME = "processGUID";

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param assetHandler            provides utilities for manipulating the repository services assets
     * @param dataEngineCommonHandler provides utilities for manipulating entities
     * @param registrationHandler     provides utilities for manipulating engine entities
     **/
    public DataEngineProcessHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                    OMRSRepositoryHelper repositoryHelper, AssetHandler<Process> assetHandler,
                                    DataEngineRegistrationHandler registrationHandler, DataEngineCommonHandler dataEngineCommonHandler) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.assetHandler = assetHandler;
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

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);

        return assetHandler.createAssetInRepository(userId, externalSourceGUID, externalSourceName, process.getQualifiedName(), process.getName(),
                null, process.getDescription(), process.getZoneMembership(), process.getOwner(),
                dataEngineCommonHandler.getOwnerTypeOrdinal(process.getOwnerType()), process.getOriginBusinessCapabilityGUID(),
                process.getOriginBusinessCapabilityGUID(), process.getOtherOriginValues(), process.getAdditionalProperties(),
                PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, buildProcessExtendedProperties(process), null, null,
                InstanceStatus.DRAFT, dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Update the process
     *
     * @param userId                the name of the calling user
     * @param originalProcessEntity the created process entity
     * @param updatedProcess        the new values of the process
     * @param externalSourceName    the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateProcess(String userId, EntityDetail originalProcessEntity, Process updatedProcess, String externalSourceName) throws
                                                                                                                                    InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    PropertyServerException {

        final String methodName = "updateProcess";

        validateProcessParameters(userId, updatedProcess.getQualifiedName(), methodName);

        String processGUID = originalProcessEntity.getGUID();

        ProcessPropertiesBuilder updatedProcessBuilder = getProcessPropertiesBuilder(updatedProcess);

        InstanceProperties updatedProcessProperties = updatedProcessBuilder.getInstanceProperties(methodName);
        EntityDetail updatedProcessEntity = dataEngineCommonHandler.buildEntityDetail(processGUID, updatedProcessProperties);
        EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalProcessEntity,
                updatedProcessEntity, true);
        if (!entityDetailDifferences.hasInstancePropertiesDifferences()) {
            return;
        }

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        assetHandler.updateAsset(userId, externalSourceGUID, externalSourceName, processGUID, PROCESS_GUID_PARAMETER_NAME,
               updatedProcess.getQualifiedName(), updatedProcess.getName(), null, updatedProcess.getDescription(),
               updatedProcess.getAdditionalProperties(), PROCESS_TYPE_GUID, PROCESS_TYPE_NAME,
               buildProcessExtendedProperties(updatedProcess), null, null, true, false,
                false, dataEngineCommonHandler.getNow(), methodName);
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
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, PROCESS_TYPE_NAME);
    }

    /**
     * Update the process instance status
     *
     * @param userId             the name of the calling user
     * @param processGUID        the process GUID
     * @param instanceStatus     the status of the process
     * @param externalSourceName the external data engine
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateProcessStatus(String userId, String processGUID, InstanceStatus instanceStatus, String externalSourceName) throws
                                                                                                                                 InvalidParameterException,
                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                 PropertyServerException {

        final String methodName = "updateProcessStatus";
        final String processStatusParameterName = "processStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, CommonMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, PROCESS_TYPE_NAME);
        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);

        assetHandler.updateBeanStatusInRepository(userId, externalSourceGUID, externalSourceName, processGUID, PROCESS_GUID_PARAMETER_NAME,
                entityTypeDef.getGUID(), entityTypeDef.getName(), false, false, instanceStatus, processStatusParameterName,
                dataEngineCommonHandler.getNow(), methodName);
    }

    /**
     * Retrieve all port objects that are connected to the process
     *
     * @param userId       the name of the calling user
     * @param processGUID  the unique identifier of the process
     * @param portTypeName the type of the port to be retrieved
     *
     * @return A set of the retrieved ports or an empty set
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Set<EntityDetail> getPortsForProcess(String userId, String processGUID, String portTypeName) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException {
        return dataEngineCommonHandler.getEntitiesForRelationship(userId, processGUID, PROCESS_PORT_TYPE_NAME,
                portTypeName, PROCESS_TYPE_NAME);
    }

    private void validateProcessParameters(String userId, String qualifiedName, String methodName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    ProcessPropertiesBuilder getProcessPropertiesBuilder(Process process) {
        return new ProcessPropertiesBuilder(process.getQualifiedName(), process.getDisplayName(), process.getName(), process.getDescription(),
                PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, process.getFormula(), process.getImplementationLanguage(), process.getAdditionalProperties(),
                repositoryHelper, serverName, serviceName);
    }

    public void upsertProcessHierarchyRelationship(String userId, ParentProcess parentProcess, String processGUID,
                                                   String externalSourceName) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException {
        final String methodName = "upsertProcessHierarchyRelationship";

        ProcessContainmentType processContainmentType = parentProcess.getProcessContainmentType();
        InstanceProperties relationshipProperties = repositoryHelper.addEnumPropertyToInstance(serviceName, null,
                CommonMapper.CONTAINMENT_TYPE, processContainmentType.getOrdinal(), processContainmentType.getName(),
                processContainmentType.getDescription(), methodName);

        Optional<EntityDetail> parentProcessEntity = findProcessEntity(userId, parentProcess.getQualifiedName());
        if (parentProcessEntity.isPresent()) {
            dataEngineCommonHandler.upsertExternalRelationship(userId, parentProcessEntity.get().getGUID(), processGUID,
                    PROCESS_HIERARCHY_TYPE_NAME, PROCESS_TYPE_NAME, PROCESS_TYPE_NAME, externalSourceName, relationshipProperties);
        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.PROCESS_NOT_FOUND, methodName,
                    parentProcess.getQualifiedName());
        }
    }

    /**
     * Build the extendedProperties map for a process, by adding type specific properties to the map
     *
     * @param process the process for which the extended properties map is built
     *
     * @return map containing the process extended properties
     */
    private Map<String, Object> buildProcessExtendedProperties(Process process) {
        Map<String, Object> extendedProperties = new HashMap<>();

        String formula = process.getFormula();
        String implementationLanguage = process.getImplementationLanguage();
        String displayName = process.getDisplayName();

        if (formula != null) {
            extendedProperties.put(FORMULA_PROPERTY_NAME, formula);
        }
        if (implementationLanguage != null) {
            extendedProperties.put(IMPLEMENTATION_LANGUAGE_PROPERTY_NAME, implementationLanguage);
        }
        if (displayName != null) {
            extendedProperties.put(DISPLAY_NAME_PROPERTY_NAME, displayName);
        }
        return extendedProperties;
    }

    /**
     * Remove the process
     *
     * @param userId             the name of the calling user
     * @param processGUID        unique identifier of the port to be removed
     * @param externalSourceName the external data engine
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeProcess(String userId, String processGUID, String externalSourceName, DeleteSemantic deleteSemantic) throws
                                                                                                                           InvalidParameterException,
                                                                                                                           PropertyServerException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           FunctionNotSupportedException {
        final String methodName = "removeProcess";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        assetHandler.deleteBeanInRepository(userId, externalSourceGUID, externalSourceName, processGUID, PROCESS_GUID_PARAMETER_NAME,
                PROCESS_TYPE_GUID, PROCESS_TYPE_NAME, null, null, false,
                false, dataEngineCommonHandler.getNow(), methodName);
    }
}