/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.accessservices.informationview.reports.ReportHandler;
import org.odpi.openmetadata.accessservices.informationview.events.*;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;


public class ReportCreationTest extends InMemoryRepositoryTest{

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private ReportHandler reportHandler;
    private LookupHelper lookupHelper;

    @BeforeClass
    public void setup() throws Exception {
        super.setup();
        lookupHelper = new LookupHelper(enterpriseConnector, omEntityDao, auditLog);
        reportHandler = new ReportHandler(omEntityDao, lookupHelper, omrsRepositoryHelper, auditLog);
    }


    @Test
    public void testReportCreation() throws Exception {

        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35", true);
        assertNotNull("Report was not created", reportEntity);
        assertEquals("powerbi-server::report_number_35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("report_number_35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.ID)).getPrimitiveValue());
        assertEquals("Employee35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("John Martin", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.LAST_MODIFIER)).getPrimitiveValue());
        assertEquals("http://powerbi-server/reports/rep35", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.URL)).getPrimitiveValue());
        assertEquals("John Martin", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.AUTHOR)).getPrimitiveValue());

        EntityDetail reportTypeEntity = omEntityDao.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "(AssetSchemaType)=report_number_35_type", false);
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between reports and reports type was not created", !relationships.isEmpty() && relationships.size() == 1);


        EntityDetail reportSectionEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1", false);
        assertNotNull("Report section was not created", reportSectionEntity);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("section1", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());

        EntityDetail reportNestedSectionEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaAttribute)=section1.1", false);
        assertNotNull("Nested Report section was not created", reportNestedSectionEntity);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaAttribute)=section1.1", ((PrimitivePropertyValue) reportNestedSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("section1.1", ((PrimitivePropertyValue) reportNestedSectionEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());

        EntityDetail reportNestedSectionTypeEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_TYPE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaType)=section1.1_type", false);
        assertNotNull("Nested Report section type was not created", reportNestedSectionTypeEntity);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaType)=section1.1_type", ((PrimitivePropertyValue) reportNestedSectionTypeEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());


        relationships = omEntityDao.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, reportNestedSectionTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between section and section type was not created", !relationships.isEmpty() && relationships.size() == 1);


        relationships = omEntityDao.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, reportNestedSectionTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("columns for section 1.1 were not created", !relationships.isEmpty() && relationships.size() == 2);


        EntityDetail fullNameColumnEntity = omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaAttribute)=section1.1::(DerivedSchemaAttribute)=Full Name", false);
        assertNotNull("Report column was not created", fullNameColumnEntity);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaAttribute)=section1.1::(DerivedSchemaAttribute)=Full Name", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("Full Name", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("concat", ((PrimitivePropertyValue) fullNameColumnEntity.getProperties().getPropertyValue(Constants.FORMULA)).getPrimitiveValue());

        EntityDetail roleOfTheEmployee = omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaAttribute)=section1.1::(DerivedSchemaAttribute)=Role of the employee", false);
        assertNotNull("Report column was not created", roleOfTheEmployee);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DocumentSchemaAttribute)=section1.1::(DerivedSchemaAttribute)=Role of the employee", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("Role of the employee", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
        assertEquals("upper", ((PrimitivePropertyValue) roleOfTheEmployee.getProperties().getPropertyValue(Constants.FORMULA)).getPrimitiveValue());

    }

    @Test
    public void testReportBasicPropertiesUpdate() throws Exception {
        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        request.setAuthor("test_author");
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35", true);
        assertNotNull("Report was not created", reportEntity);
        assertEquals("test_author", ((PrimitivePropertyValue) reportEntity.getProperties().getPropertyValue(Constants.AUTHOR)).getPrimitiveValue());
    }

    @Test
    public void testReportSectionUpdate() throws Exception {
        String payload = FileUtils.readFileToString(new File("./src/test/resources/report1.json"), "UTF-8");
        ReportRequestBody request = OBJECT_MAPPER.readValue(payload, ReportRequestBody.class);
        request.getReportElements().get(0).setName("SectionA");
        reportHandler.submitReportModel(request);
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35", true);
        assertNotNull("Report was not created", reportEntity);
        EntityDetail reportTypeEntity = omEntityDao.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "(AssetSchemaType)=report_number_35_type", false);
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between reports and reports type was not created", !relationships.isEmpty() && relationships.size() == 1);


        EntityDetail reportSectionEntity = omEntityDao.getEntity(Constants.DOCUMENT_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=SectionA", false);
        assertNotNull("Report section was not created", reportSectionEntity);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=SectionA", ((PrimitivePropertyValue) reportSectionEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
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
        EntityDetail reportEntity = omEntityDao.getEntity(Constants.DEPLOYED_REPORT, "powerbi-server::report_number_35", true);
        assertNotNull("Report was not created", reportEntity);
        EntityDetail reportTypeEntity = omEntityDao.getEntity(Constants.COMPLEX_SCHEMA_TYPE, "(AssetSchemaType)=report_number_35_type", false);
        assertNotNull("Report type was not created", reportTypeEntity);
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, reportTypeEntity.getGUID());
        assertNotNull(relationships);
        assertTrue("Relationship between reports and reports type was not created", !relationships.isEmpty() && relationships.size() == 1);

        EntityDetail reportColumnEntity = omEntityDao.getEntity(Constants.DERIVED_SCHEMA_ATTRIBUTE, "powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DerivedSchemaAttribute)=test_column", false);//powerbi-server::report_number_35::section1::test_column
        assertNotNull("Report column was not created", reportColumnEntity);
        assertEquals("powerbi-server::report_number_35::(DocumentSchemaAttribute)=section1::(DerivedSchemaAttribute)=test_column", ((PrimitivePropertyValue) reportColumnEntity.getProperties().getPropertyValue(Constants.QUALIFIED_NAME)).getPrimitiveValue());
        assertEquals("test_column", ((PrimitivePropertyValue) reportColumnEntity.getProperties().getPropertyValue(Constants.NAME)).getPrimitiveValue());
    }


}
