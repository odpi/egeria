/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Map;
import java.util.Optional;


/**
 * DataFileHandler manages DataFile objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates DataFile entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineDataFileHandler {

    private static final String FILE_GUID_PARAMETER_NAME = "fileGUID";

    private final InvalidParameterHandler invalidParameterHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final AssetHandler<DataFile> fileHandler;
    private final DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;
    private final DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler;
    private final DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler                handler for managing parameter errors
     * @param repositoryHelper                       provides utilities for manipulating the repository services objects
     * @param dataEngineCommonHandler                provides common Data Engine Omas utilities
     * @param fileHandler                            provides utilities specific for manipulating DataFile and CSVFile
     * @param dataEngineSchemaTypeHandler            provides utilities specific for manipulating SchemaType
     * @param dataEngineFolderHierarchyHandler       provides utilities specific for manipulating FileFolder
     * @param dataEngineConnectionAndEndpointHandler provides utilities specific for manipulating Connections and Endpoints
     */
    public DataEngineDataFileHandler(InvalidParameterHandler invalidParameterHandler,OMRSRepositoryHelper repositoryHelper,
                                     DataEngineCommonHandler dataEngineCommonHandler, AssetHandler<DataFile> fileHandler,
                                     DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler,
                                     DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler,
                                     DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.fileHandler = fileHandler;
        this.dataEngineSchemaTypeHandler = dataEngineSchemaTypeHandler;
        this.dataEngineFolderHierarchyHandler = dataEngineFolderHierarchyHandler;
        this.dataEngineConnectionAndEndpointHandler = dataEngineConnectionAndEndpointHandler;
    }

    /**
     * Constructs a DataFile or CSVFile, its specific TabularSchemaType and its TabularColumns. It also calls for the creation
     * of its folder structure, the Connection and Endpoint
     *
     * @param fileTypeName       file type name
     * @param fileTypeGuid       file type guid
     * @param file               actual data file
     * @param schemaType         file schema
     * @param extendedProperties extended properties
     * @param externalSourceGuid external source guid
     * @param externalSourceName external source name
     * @param userId             user id
     * @param methodName         method name
     *
     * @return guid of data file
     *
     * @throws InvalidParameterException  if invalid parameters
     * @throws PropertyServerException    if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public String upsertFileAssetIntoCatalog(String fileTypeName, String fileTypeGuid, DataFile file, SchemaType schemaType,
                                             Map<String, Object> extendedProperties, String externalSourceGuid,
                                             String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        validateParameters(file, schemaType, externalSourceGuid, userId, methodName);

        Optional<EntityDetail> fileAsEntity = dataEngineCommonHandler.findEntity(userId, file.getQualifiedName(), file.getFileType());

        String fileGuid;
        if (fileAsEntity.isPresent()) {
            fileGuid = updateFileInRepository(userId, externalSourceGuid, externalSourceName, fileAsEntity.get(), file,
                    extendedProperties, methodName);
        } else {
            fileGuid = createFileInRepository(fileTypeName, fileTypeGuid, file, extendedProperties, externalSourceGuid,
                    externalSourceName, userId, methodName);
        }
        String schemaTypeGuid = dataEngineSchemaTypeHandler.upsertSchemaType(userId, schemaType, fileGuid, externalSourceName);
        dataEngineCommonHandler.upsertExternalRelationship(userId, fileGuid, schemaTypeGuid, OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                           fileTypeName, OpenMetadataType.SCHEMA_TYPE_TYPE_NAME, externalSourceName, null);
        dataEngineFolderHierarchyHandler.upsertFolderHierarchy(fileGuid, file.getFileType(), file.getPathName(),
                externalSourceGuid, externalSourceName, userId, methodName);

        dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(file.getQualifiedName(), fileGuid, fileTypeName,
                file.getProtocol(), file.getNetworkAddress(), externalSourceGuid, externalSourceName, userId);

        if (file.getIncomplete()) {
            fileHandler.setClassificationInRepository(userId, externalSourceGuid, externalSourceName, fileGuid, FILE_GUID_PARAMETER_NAME, fileTypeName,
                                                      OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_GUID, OpenMetadataType.INCOMPLETE_CLASSIFICATION_TYPE_NAME, null,
                                                      true, false, false, dataEngineCommonHandler.getNow(), methodName);
        }

        return fileGuid;
    }

    /**
     * Remove the data file
     *
     * @param userId             the name of the calling user
     * @param dataFileGUID       unique identifier of the file to be removed
     * @param externalSourceName the external data engine name
     * @param externalSourceGUID the external data engine unique identifier
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeDataFile(String userId, String dataFileGUID, String externalSourceName, String externalSourceGUID,
                               DeleteSemantic deleteSemantic) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
                                                                     FunctionNotSupportedException {
        final String methodName = "removeDataFile";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);

        Optional<EntityDetail> schemaType = dataEngineCommonHandler.getEntityForRelationship(userId, dataFileGUID,
                                                                                             OpenMetadataType.ASSET_TO_SCHEMA_TYPE_TYPE_NAME, OpenMetadataType.DATA_FILE.typeName);
        if (schemaType.isPresent()) {
            dataEngineSchemaTypeHandler.removeSchemaType(userId, schemaType.get().getGUID(), externalSourceName, deleteSemantic);

            fileHandler.deleteBeanInRepository(userId, externalSourceGUID, externalSourceName, dataFileGUID, OpenMetadataProperty.GUID.name,
                                               OpenMetadataType.DATA_FILE.typeGUID, OpenMetadataType.DATA_FILE.typeName, null, null, false,
                                               false, dataEngineCommonHandler.getNow(), methodName);
        } else {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.ENTITY_NOT_DELETED, methodName, dataFileGUID);
        }
    }

    private String updateFileInRepository(String userId, String externalSourceGuid, String externalSourceName,
                                          EntityDetail fileAsEntity, DataFile file, Map<String, Object> extendedProperties,
                                          String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, OpenMetadataType.DATA_FILE.typeName);

        fileHandler.updateAsset(userId, externalSourceGuid, externalSourceName, fileAsEntity.getGUID(),
               CommonMapper.GUID_PROPERTY_NAME, file.getQualifiedName(), file.getDisplayName(), null,
               file.getDescription(), file.getAdditionalProperties(), entityTypeDef.getGUID(),
               entityTypeDef.getName(), extendedProperties, null, null, true,
               false, false, dataEngineCommonHandler.getNow(), methodName);
        return fileAsEntity.getGUID();
    }

    private String createFileInRepository(String typeName, String typeGuid, DataFile file, Map<String, Object> extendedProperties,
                                          String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        int ownerType = dataEngineCommonHandler.getOwnerTypeOrdinal(file.getOwnerType());

        return fileHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                file.getQualifiedName(), file.getDisplayName(), null, file.getDescription(), file.getZoneMembership(),
                file.getOwner(), ownerType, file.getOriginOrganizationGUID(),
                file.getOriginBusinessCapabilityGUID(), file.getOtherOriginValues(), file.getAdditionalProperties(),
                typeGuid, typeName, extendedProperties, null, null, InstanceStatus.ACTIVE,
                dataEngineCommonHandler.getNow(), methodName);
    }

    private void validateParameters(DataFile file, SchemaType schemaType, String externalSourceGuid, String userId, String methodName) throws
                                                                                                                                       InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(externalSourceGuid, "externalSourceGuid", methodName);
        invalidParameterHandler.validateObject(file, "file", methodName);
        invalidParameterHandler.validateName(file.getQualifiedName(), "file.qualifiedName", methodName);
        if (schemaType != null) {
            invalidParameterHandler.validateObject(schemaType.getQualifiedName(), "schema.qualifiedName", methodName);
        }
    }
}
