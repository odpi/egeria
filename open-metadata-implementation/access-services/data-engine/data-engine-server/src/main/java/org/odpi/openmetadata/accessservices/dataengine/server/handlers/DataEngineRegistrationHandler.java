/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;
import org.odpi.openmetadata.accessservices.dataengine.model.Engine;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.ExternalDataEnginePropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.accessservices.dataengine.server.service.ClockService;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper.GUID_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ENGINE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESSING_STATE_CLASSIFICATION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESSING_STATE_CLASSIFICATION_TYPE_NAME;

/**
 * DataEngineRegistrationHandler manages Engine objects from external data engines. It runs
 * server-side in the DataEngine OMAS and creates engine entities through the
 * SoftwareCapabilityHandler.
 */
public class DataEngineRegistrationHandler {

    private static final String EXTERNAL_ENGINE_PARAMETER_NAME = "externalSourceGUID";
    public static final String SYNC_DATES_BY_KEY = "syncDatesByKey";
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final SoftwareCapabilityHandler<Engine> softwareServerCapabilityHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final ClockService clockService;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                     name of this service
     * @param serverName                      name of the local server
     * @param invalidParameterHandler         handler for managing parameter errors
     * @param repositoryHelper                provides utilities for manipulating the repository services objects
     * @param softwareServerCapabilityHandler handler for the creation of engine objects
     */
    public DataEngineRegistrationHandler(String serviceName, String serverName,
                                         InvalidParameterHandler invalidParameterHandler,
                                         OMRSRepositoryHelper repositoryHelper,
                                         SoftwareCapabilityHandler<Engine> softwareServerCapabilityHandler,
                                         ClockService clockService) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.softwareServerCapabilityHandler = softwareServerCapabilityHandler;
        this.clockService = clockService;
    }

    /**
     * Create the engine entity from an external data engine
     *
     * @param userId the name of the calling user
     * @param engine the entity of external data engine
     * @return unique identifier of the external data engine in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertExternalDataEngine(String userId, Engine engine) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        final String methodName = "upsertExternalDataEngine";

        invalidParameterHandler.validateUserId(userId, methodName);

        String externalEngineName = engine.getQualifiedName();
        invalidParameterHandler.validateName(externalEngineName, QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ENGINE_TYPE_NAME);

        String externalEngineGUID = getExternalDataEngine(userId, externalEngineName);
        if (externalEngineGUID == null) {
            externalEngineGUID = softwareServerCapabilityHandler.createSoftwareCapability(userId, null,
                    null, entityTypeDef.getName(), null, externalEngineName,
                    engine.getName(), engine.getDescription(), engine.getEngineType(),
                    engine.getEngineVersion(), engine.getPatchLevel(), engine.getSource(),
                    engine.getAdditionalProperties(), null, null, null,
                    null, false, false, clockService.getNow(), methodName);
        } else {
            ExternalDataEnginePropertiesBuilder builder = getExternalDataEnginePropertiesBuilder(engine);
            InstanceProperties properties = builder.getInstanceProperties(methodName);

            softwareServerCapabilityHandler.updateBeanInRepository(userId, externalEngineGUID, externalEngineName, externalEngineGUID,
                    GUID_PROPERTY_NAME, entityTypeDef.getGUID(), entityTypeDef.getName(), properties, true, methodName);
        }

        return externalEngineGUID;
    }

    /**
     * Return the guid of an engine entity from an external data engine
     *
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the external data engine
     * @return the guid of the external data engine
     * @throws InvalidParameterException  one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem retrieving the discovery engine definition
     */
    public String getExternalDataEngine(String userId, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException,
            PropertyServerException {
        final String methodName = "getExternalDataEngineByQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ENGINE_TYPE_NAME);
        EntityDetail retrievedEntity = softwareServerCapabilityHandler.getEntityByValue(userId, qualifiedName, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                entityTypeDef.getGUID(), entityTypeDef.getName(), Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME),
                false, false, clockService.getNow(), methodName);

        if (retrievedEntity == null) {
            return null;
        }

        return retrievedEntity.getGUID();
    }

    public void removeExternalDataEngine(String userId, String qualifiedName, String externalSourceName, DeleteSemantic deleteSemantic) throws
            FunctionNotSupportedException {
        final String methodName = "removeExternalDataEngine";

        throw new FunctionNotSupportedException(OMRSErrorCode.METHOD_NOT_IMPLEMENTED.getMessageDefinition(methodName, this.getClass().getName(),
                serverName), this.getClass().getName(), methodName);
    }

    ExternalDataEnginePropertiesBuilder getExternalDataEnginePropertiesBuilder(Engine engine) {
        return new ExternalDataEnginePropertiesBuilder(engine.getQualifiedName(),
                engine.getName(), engine.getDescription(), engine.getEngineType(),
                engine.getEngineVersion(), engine.getPatchLevel(), engine.getSource(),
                engine.getAdditionalProperties(), repositoryHelper, serviceName, serverName);
    }

    public void upsertProcessingStateClassification(String userId, ProcessingState processingState, String externalSourceName)
            throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        final String methodName = "upsertProcessingStateClassification";

        invalidParameterHandler.validateUserId(userId, methodName);

        String externalSourceGUID = this.getExternalDataEngine(userId, externalSourceName);
        validateExternalDataEngine(externalSourceName, methodName, externalSourceGUID);

        EntityDetail retrievedEntity = getDataEngineEntity(userId, externalSourceName, methodName);

        //Check if the entity has this classification and if it does then merge the syncDatesByKey
        Map<String, Long> newSyncDatesByKey = updateSyncDatesByKey(processingState, retrievedEntity);

        if (newSyncDatesByKey.isEmpty()) {
            newSyncDatesByKey = processingState.getSyncDatesByKey();
        }

        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties = repositoryHelper.addLongMapPropertyToInstance(null, instanceProperties, SYNC_DATES_BY_KEY,
                newSyncDatesByKey, methodName);

        softwareServerCapabilityHandler.setClassificationInRepository(userId, null, null,
                externalSourceGUID, EXTERNAL_ENGINE_PARAMETER_NAME, ENGINE_TYPE_NAME, PROCESSING_STATE_CLASSIFICATION_TYPE_GUID,
                PROCESSING_STATE_CLASSIFICATION_TYPE_NAME, instanceProperties, true, false,
                false, clockService.getNow(), methodName);
    }

    public ProcessingState getProcessingStateClassification(String userId, String externalSourceName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "getProcessingStateClassification";
        invalidParameterHandler.validateUserId(userId, methodName);

        String externalSourceGUID = this.getExternalDataEngine(userId, externalSourceName);
        validateExternalDataEngine(externalSourceName, methodName, externalSourceGUID);

        EntityDetail engineEntity = getDataEngineEntity(userId, externalSourceName, methodName);

        Map<String, Long> syncDatesByKey = getSyncDatesByKey(engineEntity);
        return new ProcessingState(syncDatesByKey);
    }

    private EntityDetail getDataEngineEntity(String userId, String externalSourceName, String methodName) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException {
        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, ENGINE_TYPE_NAME);
        return softwareServerCapabilityHandler.getEntityByValue(userId, externalSourceName, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                entityTypeDef.getGUID(), entityTypeDef.getName(), Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME),
                false, false, clockService.getNow(), methodName);
    }

    private void validateExternalDataEngine(String externalSourceName, String methodName, String externalSourceGUID) throws InvalidParameterException {
        if (externalSourceGUID == null) {
            ExceptionMessageDefinition messageDefinition = DataEngineErrorCode.ENGINE_NOT_FOUND.getMessageDefinition(externalSourceName);
            throw new InvalidParameterException(messageDefinition, this.getClass().getName(), methodName, EXTERNAL_ENGINE_PARAMETER_NAME);
        }
    }

    private Map<String, Long> updateSyncDatesByKey(ProcessingState processingState, EntityDetail retrievedEntity) {
        Map<String, Long> newSyncDatesByKey = new HashMap<>();
        if (retrievedEntity.getClassifications() != null) {
            for (Classification classification : retrievedEntity.getClassifications()) {
                if (classification != null && classification.getName().equals(PROCESSING_STATE_CLASSIFICATION_TYPE_NAME)) {
                    MapPropertyValue syncDatesByKey = (MapPropertyValue) classification.getProperties().getPropertyValue(SYNC_DATES_BY_KEY);
                    for (Map.Entry entry : syncDatesByKey.getMapValues().getInstanceProperties().entrySet()) {
                        newSyncDatesByKey.put(entry.getKey().toString(),
                                ((Long) ((PrimitivePropertyValue) entry.getValue()).getPrimitiveValue()).longValue());
                    }
                    newSyncDatesByKey.putAll(processingState.getSyncDatesByKey());
                }
            }
        }
        return newSyncDatesByKey;
    }

    private Map<String, Long> getSyncDatesByKey(EntityDetail retrievedEntity) {
        Map<String, Long> newSyncDatesByKey = new HashMap<>();
        if (retrievedEntity.getClassifications() != null) {
            for (Classification classification : retrievedEntity.getClassifications()) {
                if (classification != null && classification.getName().equals(PROCESSING_STATE_CLASSIFICATION_TYPE_NAME)) {
                    MapPropertyValue syncDatesByKey = (MapPropertyValue) classification.getProperties().getPropertyValue(SYNC_DATES_BY_KEY);
                    for (Map.Entry entry : syncDatesByKey.getMapValues().getInstanceProperties().entrySet()) {
                        newSyncDatesByKey.put(entry.getKey().toString(),
                                ((Long) ((PrimitivePropertyValue) entry.getValue()).getPrimitiveValue()).longValue());
                    }
                }
            }
        }
        return newSyncDatesByKey;
    }

}