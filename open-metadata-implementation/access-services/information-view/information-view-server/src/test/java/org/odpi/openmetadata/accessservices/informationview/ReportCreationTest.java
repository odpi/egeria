/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.ReportCreator;
import org.odpi.openmetadata.accessservices.informationview.events.ReportRequestBody;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;
import org.testng.annotations.BeforeMethod;
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
    private OMRSAuditLog auditLog;
    private ReportCreator reportCreator;
    private OMRSRepositoryContentManager localRepositoryContentManager = null;
    private EntitiesCreatorHelper entitiesCreatorHelper;
    private LookupHelper lookupHelper;

    @BeforeMethod
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        entitiesCreatorHelper = new EntitiesCreatorHelper(enterpriseConnector, auditLog);
        lookupHelper = new LookupHelper(enterpriseConnector, entitiesCreatorHelper, auditLog);
        reportCreator = new ReportCreator(entitiesCreatorHelper, lookupHelper, auditLog);
        OMRSRepositoryConnector repositoryConnector = initializeInMemoryRepositoryConnector();
        when(enterpriseConnector.getMetadataCollection()).thenReturn(repositoryConnector.getMetadataCollection());
        when(enterpriseConnector.getRepositoryHelper()).thenReturn(repositoryConnector.getRepositoryHelper());

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
        reportCreator.createReportModel(request);
        EntityDetail reportEntity = entitiesCreatorHelper.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server.report_number_35");
        assertNotNull("Report was not created", reportEntity);
        assertEquals("powerbi-server.report_number_35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("report_number_35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.ID)).getPrimitiveValue());
        assertEquals("Employee35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("John Martin", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.LAST_MODIFIER)).getPrimitiveValue());
        assertEquals("http://powerbi-server/reports/rep35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.URL)).getPrimitiveValue());
        assertEquals("John Martin", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.AUTHOR)).getPrimitiveValue());

        EntityDetail reportTypeEntity = entitiesCreatorHelper.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "powerbi-server.report_number_35_type");
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between report and report type was not created", !relationships.isEmpty() && relationships.size() == 1);


        EntityDetail reportSectionEntity = entitiesCreatorHelper.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server.report_number_35.section1");
        assertNotNull("Report section was not created", reportSectionEntity);
        assertEquals("powerbi-server.report_number_35.section1", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("section1", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());

        EntityDetail reportNestedSectionEntity = entitiesCreatorHelper.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server.report_number_35.section1.section1.1");
        assertNotNull("Nested Report section was not created", reportNestedSectionEntity);
        assertEquals("powerbi-server.report_number_35.section1.section1.1", ((PrimitivePropertyValue) reportNestedSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("section1.1", ((PrimitivePropertyValue) reportNestedSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());

        EntityDetail reportNestedSectionTypeEntity = entitiesCreatorHelper.getEntity(Constants.DOCUMENT_SCHEMA_TYPE, "powerbi-server.report_number_35.section1.section1.1_type");
        assertNotNull("Nested Report section type was not created", reportNestedSectionTypeEntity);
        assertEquals("powerbi-server.report_number_35.section1.section1.1_type", ((PrimitivePropertyValue) reportNestedSectionTypeEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());


        relationships = entitiesCreatorHelper.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, reportNestedSectionTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between section and section type was not created", !relationships.isEmpty() && relationships.size() == 1);


        relationships = entitiesCreatorHelper.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, reportNestedSectionTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("columns for section 1.1 were not created", !relationships.isEmpty() && relationships.size() == 2);


        EntityDetail fullNameColumnEntity = entitiesCreatorHelper.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server.report_number_35.section1.section1.1.Full Name");
        assertNotNull("Report column was not created", fullNameColumnEntity);
        assertEquals("powerbi-server.report_number_35.section1.section1.1.Full Name", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("Full Name", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("concat", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.FORMULA)).getPrimitiveValue());

        EntityDetail roleOfTheEmployee = entitiesCreatorHelper.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server.report_number_35.section1.section1.1.Role of the employee");
        assertNotNull("Report column was not created", roleOfTheEmployee);
        assertEquals("powerbi-server.report_number_35.section1.section1.1.Role of the employee", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("Role of the employee", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("upper", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.FORMULA)).getPrimitiveValue());

    }

}
