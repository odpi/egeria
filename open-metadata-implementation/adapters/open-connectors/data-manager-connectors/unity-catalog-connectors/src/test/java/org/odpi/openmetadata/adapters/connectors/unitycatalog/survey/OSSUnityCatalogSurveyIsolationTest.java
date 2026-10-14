/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.unitycatalog.survey;

import org.odpi.openmetadata.adapters.connectors.unitycatalog.ffdc.UCErrorCode;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.ColumnInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.FunctionInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.RegisteredModelInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.TableInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.properties.VolumeInfo;
import org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


/**
 * Verify that a failed call to Unity Catalog for one type of resource in a schema is skipped, and the other
 * resources in the schema are still surveyed.
 */
public class OSSUnityCatalogSurveyIsolationTest
{
    /**
     * Resource connector whose volume and function list calls fail, as they would if the survey's userId could
     * not read them.
     */
    private static class PartlyFailingResourceConnector extends OSSUnityCatalogResourceConnector
    {
        @Override
        public List<VolumeInfo> listVolumes(String catalogName,
                                            String schemaName) throws PropertyServerException
        {
            throw new PropertyServerException(UCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "403", "listVolumes", "Forbidden"),
                                              this.getClass().getName(),
                                              "listVolumes");
        }


        @Override
        public List<TableInfo> listTables(String catalogName,
                                          String schemaName)
        {
            ColumnInfo column1 = new ColumnInfo();
            column1.setName("c1");
            ColumnInfo column2 = new ColumnInfo();
            column2.setName("c2");

            TableInfo tableInfo = new TableInfo();
            tableInfo.setCatalog_name(catalogName);
            tableInfo.setSchema_name(schemaName);
            tableInfo.setName("t1");
            tableInfo.setColumns(List.of(column1, column2));

            return List.of(tableInfo);
        }


        @Override
        public List<RegisteredModelInfo> listRegisteredModels(String catalogName,
                                                              String schemaName)
        {
            RegisteredModelInfo registeredModelInfo = new RegisteredModelInfo();
            registeredModelInfo.setFull_name(catalogName + "." + schemaName + ".m1");

            return List.of(registeredModelInfo);
        }


        @Override
        public List<FunctionInfo> listFunctions(String catalogName,
                                                String schemaName) throws PropertyServerException
        {
            throw new PropertyServerException(UCErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition("test", "403", "listFunctions", "Forbidden"),
                                              this.getClass().getName(),
                                              "listFunctions");
        }
    }


    /**
     * The tables, columns and registered models are surveyed even though the volumes and functions are not.
     */
    @Test public void testFailedCallIsIsolated()
    {
        OSSUnityCatalogServerSurveyBase                surveyService  = new OSSUnityCatalogServerSurveyBase();
        OSSUnityCatalogServerSurveyBase.SchemaContents schemaContents = new OSSUnityCatalogServerSurveyBase.SchemaContents();

        surveyService.surveySchemaContents(new PartlyFailingResourceConnector(), "cat", "sch", schemaContents);

        assertEquals(schemaContents.tableCount, 1L);
        assertEquals(schemaContents.columnCount, 2L);
        assertEquals(schemaContents.modelCount, 1L);
        assertEquals(schemaContents.volumeCount, 0L);
        assertEquals(schemaContents.functionCount, 0L);
        assertTrue(schemaContents.tableList.containsKey("cat.sch.t1"));
        assertTrue(schemaContents.columnList.containsKey("cat.sch.t1.c2"));
        assertTrue(schemaContents.modelList.containsKey("cat.sch.m1"));
    }
}
