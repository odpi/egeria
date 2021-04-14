package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.OwnerType;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.CommonMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
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

public class DataEngineDataFileHandler {

    private final String serviceName;
    private final String serverName;
    private final InvalidParameterHandler invalidParameterHandler;
    private final RepositoryHandler repositoryHandler;
    private final OMRSRepositoryHelper repositoryHelper;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final AssetHandler<DataFile> fileHandler;
//    private final ConnectionHandler<Connection> connectionHandler;
//    private final EndpointHandler<Endpoint> endpointHandler;
    private final DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     */
    public DataEngineDataFileHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                     RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                                     DataEngineCommonHandler dataEngineCommonHandler, AssetHandler<DataFile> fileHandler,
                                     DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.fileHandler = fileHandler;
//        this.connectionHandler = connectionHandler;
//        this.endpointHandler = endpointHandler;
        this.dataEngineSchemaTypeHandler = dataEngineSchemaTypeHandler;
    }


    public String upsertFileAssetIntoCatalog(String fileTypeName, String fileTypeGuid, DataFile file, SchemaType schemaType,
                                             List<Attribute> columns, Map<String, Object> extendedProperties,
                                             String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        validateParameters(file, schemaType, columns, userId, methodName);

        Optional<EntityDetail> fileAsEntity = dataEngineCommonHandler.findEntity(userId, file.getQualifiedName(), fileTypeName);

        String fileGuid;
        if(fileAsEntity.isPresent()){
            fileGuid = updateFileInRepository(userId, externalSourceGuid, externalSourceName, fileAsEntity.get(), file,
                    extendedProperties, methodName);
        } else {
            fileGuid = createFileInRepository(fileTypeName, fileTypeGuid, file, schemaType, columns, extendedProperties,
                    externalSourceGuid, externalSourceName, userId, methodName);
        }
        String schemaTypeGuid = dataEngineSchemaTypeHandler.upsertSchemaType(userId, schemaType, externalSourceName);
        dataEngineCommonHandler.upsertExternalRelationship(userId, fileGuid, schemaTypeGuid, ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                fileTypeName, externalSourceName, null);

//            createEndpointConnectionAndPath(dataFile, externalSourceGuid, externalSourceName, fileGuid, userId, methodName);

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

    private String createFileInRepository(String typeName, String typeGuid, DataFile file, SchemaType schemaType,
                                          List<Attribute> columns, Map<String, Object> extendedProperties,
                                          String externalSourceGuid, String externalSourceName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        int ownerType = getDefaultOwnerTypeIfAbsent(file);

        return fileHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                file.getQualifiedName(), file.getDisplayName(), file.getDescription(), file.getZoneMembership(),
                file.getOwner(), ownerType, null,
                null, file.getOtherOriginValues(), file.getAdditionalProperties(),
                typeGuid, typeName, extendedProperties, methodName);
    }

    private int getDefaultOwnerTypeIfAbsent(DataFile file) {
        return file.getOwnerType() == null ? OwnerType.USER_ID.getOpenTypeOrdinal()
                : file.getOwnerType().getOpenTypeOrdinal();
    }

//    private void createEndpointConnectionAndPath(DataFile file, String externalSourceGUID, String externalSourceName,
//                                                 String dataFileGuid, String userId, String methodName)
//            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
//
//        String connectionName = file.getDisplayName() + " File Store Connection";
//        String connectorTypeGUID = getConnectorType(userId, externalSourceGUID, externalSourceName,
//                BasicFileStoreProvider.class.getName(), methodName);
//
//        String endpointName = "FileStore.Endpoint." + file.getDisplayName();
//        String endpointDescription = "Access information to connect to the actual asset: " + file.getDisplayName();
//        String endpointTypeGuid = endpointHandler.getEndpointForConnection(userId, externalSourceGUID, externalSourceName,
//                dataFileGuid, endpointName, endpointName, endpointDescription, file.getDisplayName(), null,
//                null, null, methodName);
//
//        connectionHandler.createConnection(userId, externalSourceGUID, externalSourceName, dataFileGuid,
//                "dataFileGuid", null, connectionName, connectionName, file.getDescription(),
//                file.getAdditionalProperties(), null, null, null,
//                null, null, connectorTypeGUID, "connectorTypeGuid",
//                endpointTypeGuid, "endpointTypeGuid", methodName);
//
//        addFileAssetPath(userId, externalSourceGUID, externalSourceName, dataFileGuid, "dataFileGuid",
//                file.getPathName(), "dataFilePath", methodName);
//    }

    private void validateParameters(DataFile file, SchemaType schemaType, List<Attribute> columns, String userId,
                                    String methodName) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(file, "dataFile", methodName);
        invalidParameterHandler.validateName(file.getQualifiedName(), "dataFile.qualifiedName", methodName);
        if( schemaType != null) {
            invalidParameterHandler.validateObject(schemaType.getQualifiedName(), "schema.qualifiedName", methodName);
        }
        invalidParameterHandler.validateObject(columns, "columns", methodName);
        for(Attribute column : columns){
            invalidParameterHandler.validateName(column.getQualifiedName(), "columns.column.qualifiedName", methodName);
        }
    }



}
