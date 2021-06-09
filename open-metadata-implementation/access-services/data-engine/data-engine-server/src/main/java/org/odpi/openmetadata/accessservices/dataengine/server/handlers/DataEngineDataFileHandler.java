/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;

/**
 * DataFileHandler manages DataFile objects from the property server. It runs server-side in the DataEngine OMAS
 * and creates DataFile entities with wire relationships through the OMRSRepositoryConnector.
 */
public class DataEngineDataFileHandler {

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
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param dataEngineCommonHandler provides common Data Engine Omas utilities
     * @param fileHandler provides utilities specific for manipulating DataFile and CSVFile
     * @param dataEngineSchemaTypeHandler provides utilities specific for manipulating SchemaType
     * @param dataEngineFolderHierarchyHandler provides utilities specific for manipulating FileFolder
     */
    public DataEngineDataFileHandler(InvalidParameterHandler invalidParameterHandler, OMRSRepositoryHelper repositoryHelper,
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
     * @param fileTypeName file type name
     * @param fileTypeGuid file type guid
     * @param file actual data file
     * @param schemaType file schema
     * @param columns file columns
     * @param extendedProperties extended properties
     * @param externalSourceGuid external source guid
     * @param externalSourceName external source name
     * @param userId user id
     * @param methodName method name
     *
     * @return guid of data file
     *
     * @throws InvalidParameterException if invalid parameters
     * @throws PropertyServerException if errors in repository
     * @throws UserNotAuthorizedException if user not authorized
     */
    public String upsertFileAssetIntoCatalog(String fileTypeName, String fileTypeGuid, DataFile file, SchemaType schemaType,
                                             List<Attribute> columns, Map<String, Object> extendedProperties,
                                             String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        validateParameters(file, schemaType, columns, externalSourceGuid, userId, methodName);

        Optional<EntityDetail> fileAsEntity = dataEngineCommonHandler.findEntity(userId, file.getQualifiedName(), fileTypeName);

        String fileGuid;
        if(fileAsEntity.isPresent()){
            fileGuid = updateFileInRepository(userId, externalSourceGuid, externalSourceName, fileAsEntity.get(), file,
                    extendedProperties, methodName);
        } else {
            fileGuid = createFileInRepository(fileTypeName, fileTypeGuid, file, extendedProperties, externalSourceGuid,
                    externalSourceName, userId, methodName);
        }
        String schemaTypeGuid = dataEngineSchemaTypeHandler.upsertSchemaType(userId, schemaType, externalSourceName);
        dataEngineCommonHandler.upsertExternalRelationship(userId, fileGuid, schemaTypeGuid, ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                fileTypeName, externalSourceName, null);
        dataEngineFolderHierarchyHandler.upsertFolderHierarchy(fileGuid, file.getPathName(), externalSourceGuid, externalSourceName,
                userId, methodName);

        if(file.getProtocol() != null && file.getNetworkAddress() != null) {
            dataEngineConnectionAndEndpointHandler.upsertConnectionAndEndpoint(file.getQualifiedName(), fileTypeName, file.getProtocol(),
                    file.getNetworkAddress(), externalSourceGuid, externalSourceName, userId, methodName);
        }

        return fileGuid;
    }

    private String updateFileInRepository(String userId, String externalSourceGuid, String externalSourceName,
                                        EntityDetail fileAsEntity, DataFile file, Map<String, Object> extendedProperties,
                                        String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        TypeDef entityTypeDef = repositoryHelper.getTypeDefByName(userId, DATA_FILE_TYPE_NAME);

        fileHandler.updateAsset(userId, externalSourceGuid, externalSourceName, fileAsEntity.getGUID(),
                CommonMapper.GUID_PROPERTY_NAME, file.getQualifiedName(), file.getDisplayName(),
                file.getDescription(), file.getAdditionalProperties(), entityTypeDef.getGUID(),
                entityTypeDef.getName(), extendedProperties, methodName);
        return fileAsEntity.getGUID();
    }

    private String createFileInRepository(String typeName, String typeGuid, DataFile file, Map<String, Object> extendedProperties,
                                          String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        int ownerType = dataEngineCommonHandler.getOwnerTypeOrdinal(file.getOwnerType());

        return fileHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                file.getQualifiedName(), file.getDisplayName(), file.getDescription(), file.getZoneMembership(),
                file.getOwner(), ownerType, file.getOriginOrganizationGUID(),
                file.getOriginBusinessCapabilityGUID(), file.getOtherOriginValues(), file.getAdditionalProperties(),
                typeGuid, typeName, extendedProperties, methodName);
    }

    private void validateParameters(DataFile file, SchemaType schemaType, List<Attribute> columns, String externalSourceGuid,
                                    String userId, String methodName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(externalSourceGuid, "externalSourceGuid", methodName);
        invalidParameterHandler.validateObject(file, "file", methodName);
        invalidParameterHandler.validateName(file.getQualifiedName(), "file.qualifiedName", methodName);
        invalidParameterHandler.validateName(file.getPathName(), "file.pathName", methodName);
        if( schemaType != null) {
            invalidParameterHandler.validateObject(schemaType.getQualifiedName(), "schema.qualifiedName", methodName);
        }
        invalidParameterHandler.validateObject(columns, "columns", methodName);
        for(Attribute column : columns){
            invalidParameterHandler.validateName(column.getQualifiedName(), "columns.column.qualifiedName", methodName);
        }
    }

}
