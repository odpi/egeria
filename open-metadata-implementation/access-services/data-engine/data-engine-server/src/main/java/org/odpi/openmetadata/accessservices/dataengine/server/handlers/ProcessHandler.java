/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessContainmentType;
import org.odpi.openmetadata.accessservices.dataengine.model.ParentProcess;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ProcessPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.converters.ProcessConverter;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.ProcessPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.handlers.AssetHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.AssetMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetailDifferences;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ProcessHandler manages Process objects from the property server.  It runs server-side in the DataEngine OMAS
 * and creates process entities and relationships through the OMRSRepositoryConnector.
 */
public class ProcessHandler {
    private final String serviceName;
    private final String serverName;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final AssetHandler assetHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;

    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

    private List<String> supportedZones;
    private List<String> defaultZones;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param assetHandler            provides utilities for manipulating the repository services assets
     * @param dataEngineCommonHandler provides utilities for manipulating entities
     * @param defaultZones            setting of the default zones for the handler
     * @param supportedZones          setting of the supported zones for the handler
     **/
    public ProcessHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                          RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper, AssetHandler assetHandler,
                          DataEngineCommonHandler dataEngineCommonHandler, List<String> defaultZones, List<String> supportedZones) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.assetHandler = assetHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.supportedZones = supportedZones;
        this.defaultZones = defaultZones;
    }

    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     * <p>
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier) {
        this.securityVerifier = securityVerifier;
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

        initializeAssetZoneMembership(process);
        securityVerifier.validateUserForAssetCreate(userId, process);
        validateProcessParameters(userId, process.getQualifiedName(), methodName);

        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(process.getQualifiedName(), process.getName(), process.getDisplayName(),
                process.getDescription(), process.getOwner(), process.getOwnerType(), process.getZoneMembership(), process.getLatestChange(),
                process.getFormula(), null, null, repositoryHelper, serverName, serviceName);

        String processGUID = dataEngineCommonHandler.createExternalEntity(userId, builder.getInstanceProperties(methodName), InstanceStatus.DRAFT,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, externalSourceName);

        classifyAsset(userId, process, processGUID);

        return processGUID;
    }

    /**
     * Update the process
     *
     * @param userId                the name of the calling user
     * @param originalProcessEntity the created process entity
     * @param updatedProcess        the new values of the process
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateProcess(String userId, EntityDetail originalProcessEntity, Process updatedProcess) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException {

        final String methodName = "updateProcess";

        initializeAssetZoneMembership(updatedProcess);
        validateProcessParameters(userId, updatedProcess.getQualifiedName(), methodName);

        String processGUID = originalProcessEntity.getGUID();
        ProcessConverter processConverter = new ProcessConverter(originalProcessEntity, null, repositoryHelper, methodName);
        Process originalProcess = processConverter.getProcessBean();

        validateZoneMembership(userId, originalProcess, updatedProcess, processConverter.getAssetAuditHeader());

        ProcessPropertiesBuilder updatedProcessBuilder = new ProcessPropertiesBuilder(updatedProcess.getQualifiedName(), updatedProcess.getName(),
                updatedProcess.getDisplayName(), updatedProcess.getDescription(), updatedProcess.getOwner(), updatedProcess.getOwnerType(),
                updatedProcess.getZoneMembership(), updatedProcess.getLatestChange(), updatedProcess.getFormula(), null, null, repositoryHelper,
                serverName, serviceName);

        assetHandler.reclassifyAsset(userId, originalProcess, updatedProcess, updatedProcessBuilder.getZoneMembershipProperties(methodName),
                updatedProcessBuilder.getOwnerProperties(methodName), methodName);

        EntityDetail updatedProcessEntity = dataEngineCommonHandler.buildEntityDetail(processGUID,
                updatedProcessBuilder.getInstanceProperties(methodName));
        EntityDetailDifferences entityDetailDifferences = repositoryHelper.getEntityDetailDifferences(originalProcessEntity, updatedProcessEntity,
                true);
        if (!entityDetailDifferences.hasInstancePropertiesDifferences()) {
            return;
        }

        dataEngineCommonHandler.updateEntity(userId, processGUID, updatedProcessBuilder.getInstanceProperties(methodName),
                ProcessPropertiesMapper.PROCESS_TYPE_NAME);
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
     */
    public void addProcessPortRelationship(String userId, String processGUID, String portGUID, String externalSourceName) throws
                                                                                                                          InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException {
        dataEngineCommonHandler.createOrUpdateExternalRelationship(userId, processGUID, portGUID, ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, externalSourceName, null);
    }

    /**
     * Update the process instance status
     *
     * @param userId         the name of the calling user
     * @param guid           the guid name of the process
     * @param instanceStatus the status of the process
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateProcessStatus(String userId, String guid, InstanceStatus instanceStatus) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException {

        final String methodName = "updateProcessStatus";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(guid, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
        repositoryHandler.updateEntityStatus(userId, guid, entityTypeDef.getGUID(), entityTypeDef.getName(), instanceStatus, methodName);
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

    private void validateZoneMembership(String userId, Process originalProcess, Process updatedProcess, AssetAuditHeader assetAuditHeader) throws
                                                                                                                                           InvalidParameterException,
                                                                                                                                           PropertyServerException,
                                                                                                                                           UserNotAuthorizedException {
        String methodName = "validateZoneMembership";

        invalidParameterHandler.validateAssetInSupportedZone(updatedProcess.getGUID(), ProcessPropertiesMapper.GUID_PROPERTY_NAME,
                originalProcess.getZoneMembership(), supportedZones, serviceName, methodName);

        updatedProcess.setZoneMembership(securityVerifier.verifyAssetZones(defaultZones, securityVerifier.setSupportedZonesForUser(supportedZones,
                serviceName, userId), originalProcess, updatedProcess));

        securityVerifier.validateUserForAssetDetailUpdate(userId, originalProcess, assetAuditHeader, updatedProcess);
    }

    private void validateProcessParameters(String userId, String qualifiedName, String methodName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    private void classifyAsset(String userId, Process process, String processGUID) throws UserNotAuthorizedException,
                                                                                          PropertyServerException,
                                                                                          InvalidParameterException {
        final String methodName = "classifyAsset";

        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(process.getQualifiedName(), process.getName(), process.getDisplayName(),
                process.getDescription(), process.getOwner(), process.getOwnerType(), process.getZoneMembership(), process.getLatestChange(),
                process.getFormula(), null, null, repositoryHelper, serverName, serviceName);

        if (!CollectionUtils.isEmpty(process.getZoneMembership())) {
            InstanceProperties zoneMembershipProperties = builder.getZoneMembershipProperties(methodName);
            repositoryHandler.classifyEntity(userId, processGUID, AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                    AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME, zoneMembershipProperties, methodName);
        }

        if (!StringUtils.isEmpty(process.getOwner())) {
            InstanceProperties ownerProperties = builder.getOwnerProperties(methodName);
            repositoryHandler.classifyEntity(userId, processGUID, AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                    AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, ownerProperties, methodName);
        }
    }

    public void createOrUpdateProcessHierarchyRelationship(String userId, ParentProcess parentProcess, String processGUID,
                                                           String externalSourceName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException {
        final String methodName = "createOrUpdateProcessHierarchyRelationship";

        ProcessContainmentType processContainmentType = parentProcess.getProcessContainmentType();
        InstanceProperties relationshipProperties = repositoryHelper.addEnumPropertyToInstance(serviceName, null,
                ProcessPropertiesMapper.CONTAINMENT_TYPE, processContainmentType.getOrdinal(), processContainmentType.getName(),
                processContainmentType.getDescription(), methodName);

        Optional<EntityDetail> parentProcessEntity = findProcessEntity(userId, parentProcess.getQualifiedName());
        if (parentProcessEntity.isPresent()) {
            dataEngineCommonHandler.createOrUpdateExternalRelationship(userId, parentProcessEntity.get().getGUID(), processGUID,
                    ProcessPropertiesMapper.PROCESS_HIERARCHY_TYPE_NAME, ProcessPropertiesMapper.PROCESS_TYPE_NAME, externalSourceName,
                    relationshipProperties);
        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.PROCESS_NOT_FOUND, methodName,
                    parentProcess.getQualifiedName());
        }
    }

    private void initializeAssetZoneMembership(Process updatedProcess) throws InvalidParameterException, PropertyServerException {
        List<String> zoneMembership = securityVerifier.initializeAssetZones(defaultZones, updatedProcess);
        updatedProcess.setZoneMembership(zoneMembership);
    }
}
