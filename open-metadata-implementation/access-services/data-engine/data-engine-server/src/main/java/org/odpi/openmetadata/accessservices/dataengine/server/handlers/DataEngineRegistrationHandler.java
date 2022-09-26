/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.ProcessingState;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;
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
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper.GUID_PROPERTY_NAME;
import static org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESSING_STATE_CLASSIFICATION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.PROCESSING_STATE_CLASSIFICATION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME;

/**
 * DataEngineRegistrationHandler manages SoftwareServerCapability objects from external data engines. It runs
 * server-side in the DataEngine OMAS and creates software server capability entities through the
 * SoftwareCapabilityHandler.
 */
public class DataEngineRegistrationHandler {

    private static final String EXTERNAL_ENGINE_PARAMETER_NAME = "externalSourceGUID";
    public static final String SYNC_DATES_BY_KEY = "syncDatesByKey";
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final SoftwareCapabilityHandler<SoftwareServerCapability> softwareServerCapabilityHandler;
    private final InvalidParameterHandler invalidParameterHandler;
    private final ClockService clockService;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName                       name of this service
     * @param serverName                        name of the local server
     * @param invalidParameterHandler           handler for managing parameter errors
     * @param repositoryHelper                  provides utilities for manipulating the repository services objects
     * @param softwareServerCapabilityHandler   handler for the creation of SoftwareServerCapability objects
     */
    public DataEngineRegistrationHandler(String serviceName, String serverName,
                                         InvalidParameterHandler invalidParameterHandler,
                                         OMRSRepositoryHelper repositoryHelper,
                                         SoftwareCapabilityHandler<SoftwareServerCapability> softwareServerCapabilityHandler,
                                         ClockService clockService) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.softwareServerCapabilityHandler = softwareServerCapabilityHandler;
        this.clockService = clockService;
    }

    /**
     * Create the software server capability entity from an external data engine
     *
     * @param userId                   the name of the calling user
     * @param softwareServerCapability the entity of external data engine
     *
     * @return unique identifier of the external data engine in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertExternalDataEngine(String userId, SoftwareServerCapability softwareServerCapability) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException {
        final String methodName = "upsertExternalDataEngine";

        invalidParameterHandler.validateUserId(userId, methodName);

        String externalEngineName = softwareServerCapability.getQualifiedName();
        invalidParameterHandler.validateName(externalEngineName, QUALIFIED_NAME_PROPERTY_NAME,
                methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);

        String externalEngineGUID = getExternalDataEngine(userId, externalEngineName);
        if (externalEngineGUID == null) {
            externalEngineGUID = softwareServerCapabilityHandler.createSoftwareCapability(userId, null,
                    null,  entityTypeDef.getName(), null, externalEngineName,
                    softwareServerCapability.getName(), softwareServerCapability.getDescription(), softwareServerCapability.getEngineType(),
                    softwareServerCapability.getEngineVersion(), softwareServerCapability.getPatchLevel(), softwareServerCapability.getSource(),
                    softwareServerCapability.getAdditionalProperties(), null, null, null,
                    null, false, false, clockService.getNow(), methodName);
        } else {
            ExternalDataEnginePropertiesBuilder builder = getExternalDataEnginePropertiesBuilder(softwareServerCapability);
            InstanceProperties properties = builder.getInstanceProperties(methodName);

            softwareServerCapabilityHandler.updateBeanInRepository(userId, externalEngineGUID, externalEngineName, externalEngineGUID,
                    GUID_PROPERTY_NAME, entityTypeDef.getGUID(), entityTypeDef.getName(), properties, true, methodName);
        }

        return externalEngineGUID;
    }

    /**
     * Return the guid of a software server capability entity from an external data engine
     *
     * @param userId        identifier of calling user
     * @param qualifiedName qualified name of the external data engine
     *
     * @return the guid of the external data engine
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem retrieving the discovery engine definition
     */
    public String getExternalDataEngine(String userId, String qualifiedName) throws InvalidParameterException, UserNotAuthorizedException,
                                                                                    PropertyServerException {
        final String methodName = "getExternalDataEngineByQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
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

    ExternalDataEnginePropertiesBuilder getExternalDataEnginePropertiesBuilder(SoftwareServerCapability softwareServerCapability) {
        return new ExternalDataEnginePropertiesBuilder(softwareServerCapability.getQualifiedName(),
                softwareServerCapability.getName(), softwareServerCapability.getDescription(), softwareServerCapability.getEngineType(),
                softwareServerCapability.getEngineVersion(), softwareServerCapability.getPatchLevel(), softwareServerCapability.getSource(),
                softwareServerCapability.getAdditionalProperties(), repositoryHelper, serviceName, serverName);
    }

    public void createDataEngineClassification(String userId, ProcessingState processingState, String externalSourceName) throws
            InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException, EntityNotKnownException {
        final String methodName = "createDataEngineClassification";

        invalidParameterHandler.validateUserId(userId, methodName);

        String externalEngineGUID = this.getExternalDataEngine(userId, externalSourceName);
        if (externalEngineGUID == null) {
            ExceptionMessageDefinition messageDefinition = DataEngineErrorCode.SOFTWARE_SERVER_CAPABILITY_NOT_FOUND.getMessageDefinition(externalSourceName);
            throw new EntityNotKnownException(messageDefinition, this.getClass().getName(),
                    messageDefinition.getUserAction());
        }

        //Check if the entity has this classification and if it does then merge the syncDatesByKey

        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, SOFTWARE_SERVER_CAPABILITY_TYPE_NAME);
        EntityDetail retrievedEntity = softwareServerCapabilityHandler.getEntityByValue(userId, externalSourceName, CommonMapper.QUALIFIED_NAME_PROPERTY_NAME,
                entityTypeDef.getGUID(), entityTypeDef.getName(), Collections.singletonList(CommonMapper.QUALIFIED_NAME_PROPERTY_NAME),
                false, false, null, methodName);

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

        if (newSyncDatesByKey.isEmpty()) {
            newSyncDatesByKey = processingState.getSyncDatesByKey();
        }

        InstanceProperties instanceProperties = new InstanceProperties();
        instanceProperties = repositoryHelper.addLongMapPropertyToInstance(null, instanceProperties, SYNC_DATES_BY_KEY,
                newSyncDatesByKey, methodName);

        softwareServerCapabilityHandler.setClassificationInRepository(userId,
                null,
                null,
                externalEngineGUID,
                EXTERNAL_ENGINE_PARAMETER_NAME,
                SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                PROCESSING_STATE_CLASSIFICATION_TYPE_GUID,
                PROCESSING_STATE_CLASSIFICATION_TYPE_NAME,
                instanceProperties,
                true,
                false,
                false,
                null,
                methodName);
    }
}