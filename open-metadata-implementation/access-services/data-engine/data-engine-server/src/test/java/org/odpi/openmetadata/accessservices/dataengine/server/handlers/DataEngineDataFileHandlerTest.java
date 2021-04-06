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
import org.odpi.openmetadata.accessservices.dataengine.model.CSVFile;
import org.odpi.openmetadata.accessservices.dataengine.model.SchemaType;
import org.odpi.openmetadata.accessservices.dataengine.model.TabularColumn;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIDummyBean;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.DataItemSortOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock
    private SchemaAttributeHandler<OpenMetadataAPIDummyBean, OpenMetadataAPIDummyBean> schemaAttributeHandler;

    @Mock
    private EndpointHandler<OpenMetadataAPIDummyBean> endpointHandler;

    @Mock
    private ConnectionHandler<OpenMetadataAPIDummyBean> connectionHandler;

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private AssetHandler fileHandler;

    @InjectMocks
    private DataEngineDataFileHandler dataEngineDataFileHandler;

    @Test
    void addCsvAssetToCatalog() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        CSVFile csvFile = getCsvFile();
        SchemaType tabularSchemaType = getTabularSchema();
        List<TabularColumn> columns = getTabularColumns();

        dataEngineDataFileHandler.addOrUpdateFileAssetToCatalog(csvFile, tabularSchemaType, columns,
                EXTERNAL_SOURCE_GUID, EXTERNAL_SOURCE_NAME, USER, METHOD);

        verify(dataEngineDataFileHandler, times(1)).
                addOrUpdateFileAssetToCatalog(csvFile, tabularSchemaType, columns, EXTERNAL_SOURCE_GUID,
                        EXTERNAL_SOURCE_NAME, USER, METHOD);
    }



    private void mockTypeDef() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(PortPropertiesMapper.PORT_SCHEMA_TYPE_NAME);
        when(entityTypeDef.getGUID()).thenReturn(PortPropertiesMapper.PORT_SCHEMA_TYPE_GUID);
    }

    private CSVFile getCsvFile(){
        CSVFile csvFile = new CSVFile();
        csvFile.setQualifiedName(QUALIFIED_NAME);
        csvFile.setName(NAME);
        csvFile.setOwner(OWNER);
        csvFile.setFileType(FILE_TYPE);
        csvFile.setDescription(DESCRIPTION);
        csvFile.setPath(PATH);
        csvFile.setDelimiterCharacter(",");
        csvFile.setQuoteCharacter("\"");

        return csvFile;
    }

    private SchemaType getTabularSchema(){
        SchemaType tabularSchema = new SchemaType();
        tabularSchema.setQualifiedName(QUALIFIED_NAME);
        tabularSchema.setDisplayName(NAME);
        tabularSchema.setAuthor(AUTHOR);
        tabularSchema.setUsage(USAGE);
        tabularSchema.setEncodingStandard(ENCODING_STANDARD);
        tabularSchema.setVersionNumber(VERSION_NUMBER);

        return tabularSchema;
    }

    private List<TabularColumn> getTabularColumns(){
        List<TabularColumn> tabularColumns = new ArrayList<>();

        TabularColumn tabularColumn = new TabularColumn();
        tabularColumn.setQualifiedName(QUALIFIED_NAME);
        tabularColumn.setDisplayName(NAME);
        tabularColumn.setDescription(DESCRIPTION);
        tabularColumn.setPosition(POSITION);
        tabularColumn.setNativeClass(NATIVE_CLASS);
        tabularColumn.setDataItemSortOrder(DataItemSortOrder.ASCENDING);

        tabularColumns.add(tabularColumn);

        return tabularColumns;
    }

}