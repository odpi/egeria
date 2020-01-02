/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;


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
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
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
    private final DataEngineRegistrationHandler dataEngineRegistrationHandler;
    private final AssetHandler assetHandler;

    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();

    private List<String> supportedZones;
    private List<String> defaultZones;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                   name of this service
     * @param serverName                    name of the local server
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param repositoryHandler             manages calls to the repository services
     * @param repositoryHelper              provides utilities for manipulating the repository services objects
     * @param dataEngineRegistrationHandler provides calls for retrieving external data engine guid
     * @param assetHandler                  provides utilities for manipulating the repository services assets
     * @param defaultZones                  setting of the default zones for the handler
     * @param supportedZones                setting of the supported zones for the handler
     **/
    public ProcessHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                          RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                          DataEngineRegistrationHandler dataEngineRegistrationHandler, AssetHandler assetHandler,
                          List<String> defaultZones, List<String> supportedZones) {

        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.dataEngineRegistrationHandler = dataEngineRegistrationHandler;
        this.assetHandler = assetHandler;
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
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String createProcess(String userId, Process process, String externalSourceName) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException {
        final String methodName = "createProcess";

        setAssetType(process, methodName);

        /*
         * Initialize the asset's zone membership
         */
        List<String> zoneMembership = securityVerifier.initializeAssetZones(defaultZones, process);
        process.setZoneMembership(zoneMembership);

        // todo validate the zone to ensure it is a defined zone and in the supported zones list.
        securityVerifier.validateUserForAssetCreate(userId, process);

        validateProcessParameters(userId, process.getQualifiedName(), methodName);

        ProcessPropertiesBuilder builder = new ProcessPropertiesBuilder(process.getQualifiedName(), process.getName(), process.getDisplayName(),
                process.getDescription(), process.getOwner(), process.getOwnerType(), zoneMembership, process.getLatestChange(),
                process.getFormula(), null, null, repositoryHelper, serverName, serviceName);

        String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
        String processGUID = repositoryHandler.createExternalEntity(userId, entityTypeDef.getGUID(), entityTypeDef.getName(), externalSourceGUID,
                externalSourceName, builder.getInstanceProperties(methodName), InstanceStatus.DRAFT, methodName);

        addAssetClassifications(userId, process, builder, processGUID);

        return processGUID;
    }

    /**
     * Update the process
     *
     * @param userId         the name of the calling user
     * @param processGUID    the guid of the process to be updated
     * @param updatedProcess the new values of the process
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updateProcess(String userId, String processGUID, Process updatedProcess) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException {
        final String methodName = "updateProcess";

        EntityDetail processEntity = repositoryHandler.getEntityByGUID(userId, processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, methodName);

        ProcessConverter processConverter = new ProcessConverter(processEntity, null, repositoryHelper, methodName);
        Process originalProcess = processConverter.getProcessBean();

        if (originalProcess != null) {
            invalidParameterHandler.validateAssetInSupportedZone(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME,
                    originalProcess.getZoneMembership(), supportedZones, serviceName, methodName);

            setAssetType(updatedProcess, methodName);

            assetHandler.updateAsset(userId, originalProcess, processConverter.getAssetAuditHeader(), updatedProcess, null, null, null, methodName);
        }
    }

    /**
     * Find out if the Process object is already stored in the repository. It uses the fully qualified name
     * to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return unique identifier of the process or null
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public String findProcess(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                          PropertyServerException,
                                                                          InvalidParameterException {
        final String methodName = "findProcess";

        validateProcessParameters(userId, qualifiedName, methodName);

        qualifiedName = repositoryHelper.getExactMatchRegex(qualifiedName);

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ProcessPropertiesMapper.PROCESS_TYPE_NAME);
        EntityDetail retrievedEntity = repositoryHandler.getUniqueEntityByName(userId, qualifiedName,
                ProcessPropertiesMapper.QUALIFIED_NAME_PROPERTY_NAME, properties, entityTypeDef.getGUID(),
                entityTypeDef.getName(), methodName);

        if (retrievedEntity == null) {
            return null;
        }

        return retrievedEntity.getGUID();
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

        final String methodName = "addProcessPortRelationship";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateGUID(portGUID, ProcessPropertiesMapper.GUID_PROPERTY_NAME, methodName);

        TypeDef relationshipTypeDef = repositoryHelper.getTypeDefByName(userId, ProcessPropertiesMapper.PROCESS_PORT_TYPE_NAME);

        Relationship relationship = repositoryHandler.getRelationshipBetweenEntities(userId, processGUID,
                ProcessPropertiesMapper.PROCESS_TYPE_NAME, portGUID, relationshipTypeDef.getGUID(),
                relationshipTypeDef.getName(), methodName);

        if (relationship == null) {
            String externalSourceGUID = dataEngineRegistrationHandler.getExternalDataEngineByQualifiedName(userId, externalSourceName);

            repositoryHandler.createExternalRelationship(userId, relationshipTypeDef.getGUID(), externalSourceGUID, externalSourceName,
                    processGUID, portGUID, null, methodName);
        }
    }

    /**
     * Update the process instance status
     *
     * @param userId         the name of the calling user
     * @param guid           the guid name of the process
     * @param instanceStatus the status of the process
     *
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void updateProcessStatus(String userId, String guid, InstanceStatus instanceStatus) throws
                                                                                               InvalidParameterException,
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
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
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

    private void addAssetClassifications(String userId, Process process, ProcessPropertiesBuilder builder, String processGUID) throws
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException,
                                                                                                                               InvalidParameterException {
        final String methodName = "addAssetClassifications";

        if (!CollectionUtils.isEmpty(process.getZoneMembership())) {
            repositoryHandler.classifyEntity(userId, processGUID, AssetMapper.ASSET_ZONES_CLASSIFICATION_GUID,
                    AssetMapper.ASSET_ZONES_CLASSIFICATION_NAME, builder.getZoneMembershipProperties(methodName), methodName);
        }

        if (!StringUtils.isEmpty(process.getOwner())) {
            repositoryHandler.classifyEntity(userId, processGUID, AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_GUID,
                    AssetMapper.ASSET_OWNERSHIP_CLASSIFICATION_NAME, builder.getOwnerProperties(methodName), methodName);
        }
    }

    private void setAssetType(Process process, String methodName) throws InvalidParameterException {
        ElementType elementType = new ElementType();
        String assetTypeGUID = invalidParameterHandler.validateTypeName(ProcessPropertiesMapper.PROCESS_TYPE_NAME, AssetMapper.ASSET_TYPE_NAME,
                serviceName, methodName, repositoryHelper);
        elementType.setElementOrigin(ElementOrigin.LOCAL_COHORT);
        elementType.setElementTypeId(assetTypeGUID);
        elementType.setElementTypeName(ProcessPropertiesMapper.PROCESS_TYPE_NAME);

        process.setType(elementType);
    }

}
