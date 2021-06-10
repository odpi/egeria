/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataFile;
import org.odpi.openmetadata.accessservices.dataengine.model.DataItemSortOrder;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.CSV_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DATA_FILE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DELIMITER_CHARACTER_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUOTE_CHARACTER_PROPERTY_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineDataFileHandlerTest {

    private static final String USER = "user";
    private static final String METHOD = "method";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String OWNER = "owner";
    private static final String FILE_TYPE = "csv";
    private static final String DESCRIPTION = "description";
    private static final String PATH = "path";
    private static final String AUTHOR = "author";
    private static final String USAGE = "usage";
    private static final int POSITION = 1;
    private static final String NATIVE_CLASS = "nativeClass";
    private static final String ENCODING_STANDARD = "encodingStandard";
    private static final String VERSION_NUMBER = "versionNumber";
    private static final String EXTERNAL_SOURCE_GUID = "externalSourceGuid";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String SCHEMA_TYPE_GUID = "schemaTypeGuid";
    private static final String GUID_VALUE = "1";
    private static final String PROTOCOL = "protocol";
    private static final String NETWORK_ADDRESS = "networkAddress";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private AssetHandler<DataFile> fileHandler;

    @Mock
    private DataEngineSchemaTypeHandler dataEngineSchemaTypeHandler;

    @Mock
    private DataEngineFolderHierarchyHandler dataEngineFolderHierarchyHandler;

    @Mock
    private DataEngineConnectionAndEndpointHandler dataEngineConnectionAndEndpointHandler;

    @InjectMocks
    private DataEngineDataFileHandler dataEngineDataFileHandler;

    @Test
    void insertCsvFileToCatalog() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        CSVFile csvFile = getCsvFile();
        SchemaType schemaType = getTabularSchema();
        List<Attribute> columns = getTabularColumns();
        mockDataEngineCommonHandler(false);
        mockDataEngineSchemaTypeHandler();

        String guid = dataEngineDataFileHandler.upsertFileAssetIntoCatalog(CSV_FILE_TYPE_NAME, CSV_FILE_TYPE_GUID, csvFile,
                schemaType, columns, getExtendedProperties(), EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, QUALIFIED_NAME, CSV_FILE_TYPE_NAME);
        verify(fileHandler, times(1)).
                createAssetInRepository(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, QUALIFIED_NAME, NAME, DESCRIPTION,
                        null, OWNER, 0, null, null,
                        null, null, CSV_FILE_TYPE_GUID, CSV_FILE_TYPE_NAME,
                        getExtendedProperties(), METHOD);
        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, schemaType, EXTERNAL_SOURCE_NAME);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, guid, SCHEMA_TYPE_GUID,
                ASSET_TO_SCHEMA_TYPE_TYPE_NAME, CSV_FILE_TYPE_NAME, EXTERNAL_SOURCE_NAME, null);
        verify(dataEngineFolderHierarchyHandler, times(1)).upsertFolderHierarchy(guid, PATH, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, USER, METHOD);
        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                CSV_FILE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);
    }

    @Test
    void updateCsvFileToCatalog() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        CSVFile csvFile = getCsvFile();
        SchemaType schemaType = getTabularSchema();
        List<Attribute> columns = getTabularColumns();
        mockDataEngineCommonHandler(true);
        mockRepositoryHelper();
        mockDataEngineSchemaTypeHandler();

        String guid = dataEngineDataFileHandler.upsertFileAssetIntoCatalog(CSV_FILE_TYPE_NAME, CSV_FILE_TYPE_GUID, csvFile, schemaType, columns,
                getExtendedProperties(), EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineCommonHandler, times(1)).findEntity(USER, QUALIFIED_NAME, CSV_FILE_TYPE_NAME);
        verify(fileHandler, times(1)).
                updateAsset(USER, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, GUID_VALUE, CommonMapper.GUID_PROPERTY_NAME,
                        QUALIFIED_NAME, NAME, DESCRIPTION, null, CSV_FILE_TYPE_GUID, CSV_FILE_TYPE_NAME,
                        getExtendedProperties(), METHOD);
        verify(dataEngineSchemaTypeHandler, times(1)).upsertSchemaType(USER, schemaType, EXTERNAL_SOURCE_NAME);
        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, guid, SCHEMA_TYPE_GUID,
                ASSET_TO_SCHEMA_TYPE_TYPE_NAME, CSV_FILE_TYPE_NAME, EXTERNAL_SOURCE_NAME, null);
        verify(dataEngineFolderHierarchyHandler, times(1)).upsertFolderHierarchy(guid, PATH, EXTERNAL_SOURCE_GUID,
                EXTERNAL_SOURCE_NAME, USER, METHOD);
        verify(dataEngineConnectionAndEndpointHandler, times(1)).upsertConnectionAndEndpoint(QUALIFIED_NAME,
                CSV_FILE_TYPE_NAME, PROTOCOL, NETWORK_ADDRESS, EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);
    }

    private void mockDataEngineCommonHandler(boolean update)
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        if (update) {
            EntityDetail entityDetail = new EntityDetail();
            entityDetail.setGUID(GUID_VALUE);

            when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, CSV_FILE_TYPE_NAME)).thenReturn(Optional.of(entityDetail));
            return;
        }
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, CSV_FILE_TYPE_NAME)).thenReturn(Optional.empty());
    }

    private void mockRepositoryHelper() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(entityTypeDef.getGUID()).thenReturn(CSV_FILE_TYPE_GUID);
        when(entityTypeDef.getName()).thenReturn(CSV_FILE_TYPE_NAME);

        when(repositoryHelper.getTypeDefByName(USER, DATA_FILE_TYPE_NAME)).thenReturn(entityTypeDef);
    }

    private void mockDataEngineSchemaTypeHandler() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineSchemaTypeHandler.upsertSchemaType(USER, getTabularSchema(), EXTERNAL_SOURCE_NAME)).thenReturn(SCHEMA_TYPE_GUID);
    }

    private CSVFile getCsvFile() {
        CSVFile csvFile = new CSVFile();
        csvFile.setQualifiedName(QUALIFIED_NAME);
        csvFile.setDisplayName(NAME);
        csvFile.setOwner(OWNER);
        csvFile.setFileType(FILE_TYPE);
        csvFile.setDescription(DESCRIPTION);
        csvFile.setPathName(PATH);
        csvFile.setDelimiterCharacter(",");
        csvFile.setQuoteCharacter("\"");
        csvFile.setProtocol(PROTOCOL);
        csvFile.setNetworkAddress(NETWORK_ADDRESS);

        return csvFile;
    }

    private SchemaType getTabularSchema() {
        SchemaType tabularSchema = new SchemaType();
        tabularSchema.setQualifiedName(QUALIFIED_NAME);
        tabularSchema.setDisplayName(NAME);
        tabularSchema.setAuthor(AUTHOR);
        tabularSchema.setUsage(USAGE);
        tabularSchema.setEncodingStandard(ENCODING_STANDARD);
        tabularSchema.setVersionNumber(VERSION_NUMBER);

        return tabularSchema;
    }

    private List<Attribute> getTabularColumns() {
        List<Attribute> tabularColumns = new ArrayList<>();

        Attribute tabularColumn = new Attribute();
        tabularColumn.setQualifiedName(QUALIFIED_NAME);
        tabularColumn.setDisplayName(NAME);
        tabularColumn.setDescription(DESCRIPTION);
        tabularColumn.setPosition(POSITION);
        tabularColumn.setNativeClass(NATIVE_CLASS);
        tabularColumn.setSortOrder(DataItemSortOrder.ASCENDING);

        tabularColumns.add(tabularColumn);

        return tabularColumns;
    }

    private Map<String, Object> getExtendedProperties() {
        Map<String, Object> extendedProperties = new HashMap<>();
        extendedProperties.put(FILE_TYPE, FILE_TYPE);
        extendedProperties.put(DELIMITER_CHARACTER_PROPERTY_NAME, ",");
        extendedProperties.put(QUOTE_CHARACTER_PROPERTY_NAME, "'");

        return extendedProperties;
    }

}
