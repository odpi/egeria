/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.reports.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataExchangeRule;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveManager;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ReportCreationTest {


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Mock
    private OMRSRepositoryConnector enterpriseConnector;
    @Mock
    private OMRSRepositoryHelper omrsRepositoryHelper;
    @Mock
    private OMRSAuditLog auditLog;
    private ReportHandler reportHandler;
    private OMRSRepositoryContentManager localRepositoryContentManager = null;
    private OMEntityDao omEntityDao;
    private LookupHelper lookupHelper;

    @BeforeClass
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        omEntityDao = new OMEntityDao(enterpriseConnector, auditLog);
        lookupHelper = new LookupHelper(enterpriseConnector, omEntityDao, auditLog);
        reportHandler = new ReportHandler(omEntityDao, lookupHelper, omrsRepositoryHelper, auditLog);
        OMRSRepositoryConnector repositoryConnector = initializeInMemoryRepositoryConnector();
        when(enterpriseConnector.getMetadataCollection()).thenReturn(repositoryConnector.getMetadataCollection());
        when(enterpriseConnector.getRepositoryHelper()).thenReturn(repositoryConnector.getRepositoryHelper());
        populateRepository();
    }


    private OMRSRepositoryConnector initializeInMemoryRepositoryConnector() throws ConnectorCheckedException, ConnectionCheckedException {

        Connection connection = new Connection();
        ConnectorType connectorType = new ConnectorType();
        connection.setConnectorType(connectorType);
        connectorType.setConnectorProviderClassName(InMemoryOMRSRepositoryConnectorProvider.class.getName());
        ConnectorBroker connectorBroker = new ConnectorBroker();
        Connector connector = connectorBroker.getConnector(connection);
        OMRSRepositoryConnector repositoryConnector = (OMRSRepositoryConnector) connector;

        localRepositoryContentManager = new OMRSRepositoryContentManager(auditLog);


        OMRSRepositoryEventManager localRepositoryEventManager = new OMRSRepositoryEventManager("local repository outbound",
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL,
                        null),
                new OMRSRepositoryContentValidator(localRepositoryContentManager),
                auditLog);

        LocalOMRSRepositoryConnector localOMRSRepositoryConnector = (LocalOMRSRepositoryConnector) new LocalOMRSConnectorProvider("testLocalMetadataCollectionId",
                connection,
                null,
                localRepositoryEventManager,
                localRepositoryContentManager,
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL,
                        null))
                .getConnector(connection);


        localOMRSRepositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
        localOMRSRepositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
        localOMRSRepositoryConnector.setAuditLog(auditLog);
        localOMRSRepositoryConnector.setMetadataCollectionId("1234");
        localRepositoryContentManager.setupEventProcessor(localOMRSRepositoryConnector,
                repositoryConnector,
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL,
                        null),
                localRepositoryEventManager);


        repositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
        repositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
        repositoryConnector.setMetadataCollectionId("testMetadataCollectionId");
        repositoryConnector.start();
        localRepositoryEventManager.start();
        localOMRSRepositoryConnector.start();
        new OMRSArchiveManager(null, auditLog).setLocalRepository(localRepositoryContentManager, localRepositoryEventManager);

        return localOMRSRepositoryConnector;
    }



    @Test
    public void testReportCreation() throws Exception {

        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35");
        assertNotNull("Report was not created", reportEntity);
        assertEquals("powerbi-server::report_number_35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("report_number_35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.ID)).getPrimitiveValue());
        assertEquals("Employee35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("John Martin", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.LAST_MODIFIER)).getPrimitiveValue());
        assertEquals("http://powerbi-server/reports/rep35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.URL)).getPrimitiveValue());
        assertEquals("John Martin", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.AUTHOR)).getPrimitiveValue());

        EntityDetail reportTypeEntity = omEntityDao.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "powerbi-server::report_number_35_type");
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between reports and reports type was not created", !relationships.isEmpty() && relationships.size() == 1);


        EntityDetail reportSectionEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::section1");
        assertNotNull("Report section was not created", reportSectionEntity);
        assertEquals("powerbi-server::report_number_35::section1", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("section1", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());

        EntityDetail reportNestedSectionEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::section1::section1.1");
        assertNotNull("Nested Report section was not created", reportNestedSectionEntity);
        assertEquals("powerbi-server::report_number_35::section1::section1.1", ((PrimitivePropertyValue) reportNestedSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("section1.1", ((PrimitivePropertyValue) reportNestedSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());

        EntityDetail reportNestedSectionTypeEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_TYPE, "powerbi-server::report_number_35::section1::section1.1_type");
        assertNotNull("Nested Report section type was not created", reportNestedSectionTypeEntity);
        assertEquals("powerbi-server::report_number_35::section1::section1.1_type", ((PrimitivePropertyValue) reportNestedSectionTypeEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());


        relationships = omEntityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, reportNestedSectionTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between section and section type was not created", !relationships.isEmpty() && relationships.size() == 1);


        relationships = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, reportNestedSectionTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("columns for section 1.1 were not created", !relationships.isEmpty() && relationships.size() == 2);


        EntityDetail fullNameColumnEntity = omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::section1::section1.1::Full Name");
        assertNotNull("Report column was not created", fullNameColumnEntity);
        assertEquals("powerbi-server::report_number_35::section1::section1.1::Full Name", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("Full Name", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("concat", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.FORMULA)).getPrimitiveValue());

        EntityDetail roleOfTheEmployee = omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::section1::section1.1::Role of the employee");
        assertNotNull("Report column was not created", roleOfTheEmployee);
        assertEquals("powerbi-server::report_number_35::section1::section1.1::Role of the employee", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("Role of the employee", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("upper", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.FORMULA)).getPrimitiveValue());

    }

    @Test
    public void testReportBasicPropertiesUpdate() throws Exception {
        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        request.setAuthor("test_author");
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35");
        assertNotNull("Report was not created", reportEntity);
        assertEquals("test_author", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.AUTHOR)).getPrimitiveValue());
    }

    @Test
    public void testReportSectionUpdate() throws Exception {
        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        request.getReportElements().get(0).setName("SectionA");
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35");
        assertNotNull("Report was not created", reportEntity);
        EntityDetail reportTypeEntity = omEntityDao.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "powerbi-server::report_number_35_type");
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between reports and reports type was not created", !relationships.isEmpty() && relationships.size() == 1);


        EntityDetail reportSectionEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::SectionA");
        assertNotNull("Report section was not created", reportSectionEntity);
        assertEquals("powerbi-server::report_number_35::SectionA", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("SectionA", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
    }

    @Test
    public void testReportColumnUpdate() throws Exception {
        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        ReportColumn column = new ReportColumn();
        column.setName("test_column");
        column.setSources(((ReportColumn)((ReportSection)((ReportSection)request.getReportElements().get(0)).getElements().get(0)).getElements().get(0)).getSources());
        ((ReportSection)request.getReportElements().get(0)).getElements().add(column);
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35");
        assertNotNull("Report was not created", reportEntity);
        EntityDetail reportTypeEntity = omEntityDao.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "powerbi-server::report_number_35_type");
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between reports and reports type was not created", !relationships.isEmpty() && relationships.size() == 1);


        EntityDetail reportColumnEntity = omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::section1::test_column");//powerbi-server::report_number_35::section1::test_column
        assertNotNull("Report column was not created", reportColumnEntity);
        assertEquals("powerbi-server::report_number_35::section1::test_column", ((PrimitivePropertyValue) reportColumnEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("test_column", ((PrimitivePropertyValue) reportColumnEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
    }

    private void populateRepository() throws Exception {

        String qualifiedNameForEndpoint = "host";
        InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NETWORK_ADDRESS, "host")
                .withStringProperty(Constants.PROTOCOL, "")
                .build();
        EntityDetail endpointEntity = omEntityDao.addEntity(Constants.ENDPOINT,
                qualifiedNameForEndpoint,
                endpointProperties);

        String qualifiedNameForConnection = qualifiedNameForEndpoint + "::" ;
        InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                .build();
        EntityDetail connectionEntity = omEntityDao.addEntity(Constants.CONNECTION,
                qualifiedNameForConnection, connectionProperties);

        omEntityDao.addRelationship(Constants.CONNECTION_TO_ENDPOINT,
                endpointEntity.getGUID(),
                connectionEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());


        String qualifiedNameForDataStore = qualifiedNameForConnection + "::" + "XE";
        InstanceProperties dataStoreProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataStore)
                .withStringProperty(Constants.NAME, "XE")
                .build();
        EntityDetail dataStore = omEntityDao.addEntity(Constants.DATA_STORE,
                qualifiedNameForDataStore, dataStoreProperties);


        omEntityDao.addRelationship(Constants.CONNECTION_TO_ASSET,
                connectionEntity.getGUID(),
                dataStore.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());


        String qualifiedNameForInformationView = qualifiedNameForDataStore + "::" + "HR";
        InstanceProperties ivProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForInformationView)
                .withStringProperty(Constants.NAME, "HR")
                .withStringProperty(Constants.OWNER, "")
                .withStringProperty(Constants.DESCRIPTION, "This asset is an " + "information " + "view")
                .build();
        EntityDetail informationViewEntity = omEntityDao.addEntity(Constants.INFORMATION_VIEW,
                qualifiedNameForInformationView, ivProperties);

        omEntityDao.addRelationship(Constants.DATA_CONTENT_FOR_DATASET,
                dataStore.getGUID(),
                informationViewEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());


        String qualifiedNameForDbSchemaType = qualifiedNameForInformationView + Constants.TYPE_SUFFIX;
        InstanceProperties dbSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDbSchemaType)
                .withStringProperty(Constants.DISPLAY_NAME, "HR" + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "").build();
        EntityDetail relationalDbSchemaType = omEntityDao.addEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE,
                qualifiedNameForDbSchemaType,
                dbSchemaTypeProperties);

        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                informationViewEntity.getGUID(),
                relationalDbSchemaType.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());


        String qualifiedNameForTableType = qualifiedNameForInformationView + "::" + "EMPLOYEE" + Constants.TYPE_SUFFIX;
        InstanceProperties tableTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTableType)
                .withStringProperty(Constants.DISPLAY_NAME, "EMPLOYEE" + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "")
                .build();
        EntityDetail tableTypeEntity = omEntityDao.addEntity(Constants.RELATIONAL_TABLE_TYPE,
                qualifiedNameForTableType,
                tableTypeProperties);

        String qualifiedNameForTable = qualifiedNameForInformationView + "::" + "EMPLOYEE";
        InstanceProperties tableProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTable)
                .withStringProperty(Constants.ATTRIBUTE_NAME, "EMPLOYEE")
                .build();
        EntityDetail tableEntity = omEntityDao.addEntity(Constants.RELATIONAL_TABLE,
                qualifiedNameForTable,
                tableProperties);

        omEntityDao.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                tableEntity.getGUID(),
                tableTypeEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());
        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                relationalDbSchemaType.getGUID(),
                tableEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());


        addColumn(tableTypeEntity, qualifiedNameForTable, "FNAME");
        addColumn(tableTypeEntity, qualifiedNameForTable, "LNAME");
        addColumn(tableTypeEntity, qualifiedNameForTable, "ROLE");


    }

    private EntityDetail addColumn(EntityDetail tableTypeEntity, String qualifiedNameForTable, String columnName) throws Exception {
        String qualifiedNameColumnType = qualifiedNameForTable + "::" + columnName + Constants.TYPE_SUFFIX;
        InstanceProperties columnTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameColumnType)
                .withStringProperty(Constants.DISPLAY_NAME, columnName + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "")
                .withStringProperty(Constants.DATA_TYPE, "VARCHAR2")
                .build();
        EntityDetail columnTypeEntity = omEntityDao.addEntity(Constants.RELATIONAL_COLUMN_TYPE,
                qualifiedNameColumnType,
                columnTypeProperties);

        String qualifiedNameForColumn = qualifiedNameForTable + "::" + columnName;
        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, columnName)
                .withStringProperty(Constants.FORMULA, "")
                .withIntegerProperty(Constants.ELEMENT_POSITION_NAME, 0)
                .build();
        EntityDetail derivedColumnEntity = omEntityDao.addEntity(Constants.DERIVED_RELATIONAL_COLUMN,
                qualifiedNameForColumn,
                columnProperties);

        InstanceProperties schemaQueryImplProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUERY, "")
                .build();

        omEntityDao.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                derivedColumnEntity.getGUID(),
                columnTypeEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());
//        omEntityDao.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
//                derivedColumnEntity.getGUID(),
//                derivedColumn.getSourceColumn().getBusinessTerm().getGuid(),
//                Constants.INFORMATION_VIEW_OMAS_NAME,
//                new InstanceProperties());
        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                tableTypeEntity.getGUID(),
                derivedColumnEntity.getGUID(),
                Constants.INFORMATION_VIEW_OMAS_NAME,
                new InstanceProperties());
        return derivedColumnEntity;
    }

}
