package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.TabularColumn;
import org.odpi.openmetadata.accessservices.dataengine.model.metadataelements.FileElement;
import org.odpi.openmetadata.accessservices.dataengine.model.metadataelements.FileSystemElement;
import org.odpi.openmetadata.accessservices.dataengine.model.metadataelements.FolderElement;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.FilesAndFoldersHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.FILE_TYPE_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_COLUMN_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TABULAR_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME;

public class DataEngineDataFileHandler extends FilesAndFoldersHandler<FileSystemElement, FolderElement, FileElement> {

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param fileSystemConverter     specific converter for the FILESYSTEM bean class
     * @param fileSystemBeanClass     name of bean class that is represented by the generic class FILESYSTEM
     * @param folderConverter         specific converter for the FOLDER bean class
     * @param folderBeanClass         name of bean class that is represented by the generic class FOLDER
     * @param fileConverter           specific converter for the FILE bean class
     * @param fileBeanClass           name of bean class that is represented by the generic class FILE
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param localServerUserId       userId for this server
     * @param securityVerifier        open metadata security services verifier
     * @param supportedZones          list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones            list of zones that the access service should set in all new Asset instances.
     * @param publishZones            list of zones that the access service sets up in published Asset instances.
     * @param auditLog                destination for audit log events.
     */
    public DataEngineDataFileHandler(OpenMetadataAPIGenericConverter<FileSystemElement> fileSystemConverter, Class<FileSystemElement> fileSystemBeanClass,
                                     OpenMetadataAPIGenericConverter<FolderElement> folderConverter, Class<FolderElement> folderBeanClass,
                                     OpenMetadataAPIGenericConverter<FileElement> fileConverter, Class<FileElement> fileBeanClass,
                                     String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                     RepositoryHandler repositoryHandler, OMRSRepositoryHelper repositoryHelper,
                                     String localServerUserId, OpenMetadataServerSecurityVerifier securityVerifier,
                                     List<String> supportedZones, List<String> defaultZones, List<String> publishZones, AuditLog auditLog) {
        super(fileSystemConverter, fileSystemBeanClass, folderConverter, folderBeanClass, fileConverter, fileBeanClass,
                serviceName, serverName, invalidParameterHandler, repositoryHandler, repositoryHelper, localServerUserId,
                securityVerifier, supportedZones, defaultZones, publishZones, auditLog);
    }


    public String addOrUpdateFileAssetToCatalog(DataFile dataFile, SchemaType schemaType, List<TabularColumn> columns,
                                                String externalSourceGuid, String externalSourceName, String userId,
                                                String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        validateParameters(dataFile, schemaType, columns, userId, methodName);

        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(FILE_TYPE_PROPERTY_NAME, dataFile.getFileType());

        String dataFileGuid =  fileHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                dataFile.getQualifiedName(), dataFile.getName(), dataFile.getDescription(), dataFile.getZoneMembership(),
                dataFile.getOwner(), dataFile.getOwnerType().getOrdinal(), null,
                null, null, dataFile.getAdditionalProperties(),
                DATA_FILE_TYPE_GUID, DATA_FILE_TYPE_NAME, extendedProperties, methodName);

        createEndpointConnectionAndPath(dataFile, externalSourceGuid, externalSourceName, dataFileGuid, userId, methodName);
        createTabularSchemaTypeWithColumns(schemaType, columns, externalSourceGuid, externalSourceName, dataFileGuid,
                "dataFileGuid", DATA_FILE_TYPE_NAME, userId, methodName);

        return dataFileGuid;
    }

    public String addOrUpdateFileAssetToCatalog(CSVFile csvFile, SchemaType schemaType, List<TabularColumn> columns,
                                                String externalSourceGuid, String externalSourceName, String userId,
                                                final String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        validateParameters(csvFile, schemaType, columns, userId, methodName);

        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(FILE_TYPE_PROPERTY_NAME, csvFile.getFileType());
        extendedProperties.put(DELIMITER_CHARACTER_PROPERTY_NAME, csvFile.getDelimiterCharacter());
        extendedProperties.put(QUOTE_CHARACTER_PROPERTY_NAME, csvFile.getQuoteCharacter());

        String csvFileGuid =  fileHandler.createAssetInRepository(userId, externalSourceGuid, externalSourceName,
                csvFile.getQualifiedName(), csvFile.getName(), csvFile.getDescription(), csvFile.getZoneMembership(),
                csvFile.getOwner(), csvFile.getOwnerType().getOrdinal(), null,
                null, null, csvFile.getAdditionalProperties(),
                CSV_FILE_TYPE_GUID, CSV_FILE_TYPE_NAME, extendedProperties, methodName);

        createEndpointConnectionAndPath(csvFile, externalSourceGuid, externalSourceName, csvFileGuid, userId, methodName);
        createTabularSchemaTypeWithColumns(schemaType, columns, externalSourceGuid, externalSourceName, csvFileGuid,
                "csvFileGuid", CSV_FILE_TYPE_NAME, userId, methodName);

        return csvFileGuid;
    }

    private void createEndpointConnectionAndPath(DataFile dataFile, String externalSourceGUID, String externalSourceName,
                                                 String dataFileGuid, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String connectionName = dataFile.getPath() + " File Store Connection";
        String connectorTypeGUID = getConnectorType(userId, externalSourceGUID, externalSourceName,
                BasicFileStoreProvider.class.getName(), methodName);

        String endpointName = "FileStore.Endpoint." + dataFile.getPath();
        String endpointDescription = "Access information to connect to the actual asset: " + dataFile.getPath();
        String endpointTypeGuid = endpointHandler.getEndpointForConnection(userId, externalSourceGUID, externalSourceName,
                dataFileGuid, endpointName, endpointName, endpointDescription, dataFile.getName(), null,
                null, null, methodName);

        connectionHandler.createConnection(userId, externalSourceGUID, externalSourceName, dataFileGuid,
                "dataFileGuid", null, connectionName, connectionName, dataFile.getDescription(),
                dataFile.getAdditionalProperties(), null, null, null,
                null, null, connectorTypeGUID, "connectorTypeGuid",
                endpointTypeGuid, "endpointTypeGuid", methodName);

        addFileAssetPath(userId, externalSourceGUID, externalSourceName, dataFileGuid, "dataFileGuid",
                dataFile.getPath(), "dataFilePath", methodName);
    }

    public void createTabularSchemaTypeWithColumns(SchemaType schemaType, List<TabularColumn> columns, String externalSourceGuid,
                                                   String externalSourceName, String fileAssetGuid, String fileAssetGuidParameterName,
                                                   String assetTypeName, String userId, String methodName)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        String schemaTypeGuid = schemaAttributeHandler.getAssetSchemaTypeGUID(userId, externalSourceGuid, externalSourceName,
                fileAssetGuid, fileAssetGuidParameterName, assetTypeName, TABULAR_SCHEMA_TYPE_TYPE_GUID,
                TABULAR_SCHEMA_TYPE_TYPE_NAME, methodName);

        for(TabularColumn tabularColumn : columns){

            SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(schemaType.getQualifiedName(),
                    TABULAR_COLUMN_TYPE_TYPE_GUID, TABULAR_COLUMN_TYPE_TYPE_NAME, repositoryHelper, serviceName, serverName);
            schemaTypeBuilder.setDataType("String");

            SchemaAttributeBuilder schemaAttributeBuilder = new SchemaAttributeBuilder(tabularColumn.getQualifiedName(),
                    tabularColumn.getDisplayName(), tabularColumn.getDescription(), tabularColumn.getPosition(),
                    tabularColumn.getMinCardinality(), tabularColumn.getMaxCardinality(), false,
                    tabularColumn.getDefaultValueOverride(), tabularColumn.isAllowsDuplicateValues(), tabularColumn.isOrderedValues(),
                    tabularColumn.getDataItemSortOrder().getOpenTypeOrdinal(), 0, 0, 0, false,
                    tabularColumn.getNativeClass(), Arrays.asList(tabularColumn.getAliases()),
                    tabularColumn.getAdditionalProperties(), TABULAR_COLUMN_TYPE_GUID, TABULAR_COLUMN_TYPE_NAME,
                    null, repositoryHelper, serviceName, serverName);

            schemaAttributeBuilder.setAnchors(userId, fileAssetGuid, methodName);
            schemaAttributeBuilder.setSchemaType(userId, schemaTypeBuilder, methodName);

            schemaAttributeHandler.createNestedSchemaAttribute(userId, externalSourceGuid, externalSourceName, schemaTypeGuid,
                    "schemaTypeGuid", TABULAR_SCHEMA_TYPE_TYPE_NAME, TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID,
                    TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME, tabularColumn.getQualifiedName(),
                    "qualifiedName", schemaAttributeBuilder, methodName);
        }
    }

    private void validateParameters(DataFile dataFile, SchemaType schemaType, List<TabularColumn> columns, String userId, String methodName) throws org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(dataFile, "dataFile", methodName);
        invalidParameterHandler.validateName(dataFile.getQualifiedName(), "dataFile.qualifiedName", methodName);
        if( schemaType != null) {
            invalidParameterHandler.validateObject(schemaType.getQualifiedName(), "schema.qualifiedName", methodName);
        }
        invalidParameterHandler.validateObject(columns, "columns", methodName);
        for(TabularColumn column : columns){
            invalidParameterHandler.validateName(column.getQualifiedName(), "columns.column.qualifiedName", methodName);
        }
        ;
    }

}
