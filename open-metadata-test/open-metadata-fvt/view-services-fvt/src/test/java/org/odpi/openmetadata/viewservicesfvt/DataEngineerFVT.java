/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.TabularDataSetReportResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularDataSetReport;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.ConnectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DataEngineerFVT covers the Data Engineer OMVS's one request, the tabular data set report.  A CSV file is
 * written to the build directory and catalogued as a TabularDataSet using the Core Content Pack's CSV tabular
 * data set template (through the Automated Curation's catalog-template request, which honours the requested
 * subtype), so the report is produced by the CSV Tabular Data Set Connector reading that file.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataEngineerFVT
{
    private static final ViewServiceClient dataEngineer       = new ViewServiceClient(ViewServiceDescription.DATA_ENGINEER);
    private static final ViewServiceClient automatedCuration  = new ViewServiceClient(ViewServiceDescription.AUTOMATED_CURATION);
    private static final ViewServiceClient connectionMaker    = new ViewServiceClient(ViewServiceDescription.CONNECTION_MAKER);

    /**
     * The Core Content Pack's template for a CSV tabular data set - CSVFileTemplateType.CSV_TABULAR_DATA_SET_TEMPLATE.
     */
    private static final String CSV_TABULAR_DATA_SET_TEMPLATE_GUID = "29ee189f-34db-4c3a-966c-40b04ad45103";

    private static final String TABLE_NAME = "customers-" + OMAGPlatformExtension.RUN_ID;

    private static final List<String> HEADER = List.of("id", "name", "city");
    private static final List<List<String>> ROWS = List.of(List.of("1", "Ada", "London"),
                                                         List.of("2", "Grace", "New York"),
                                                         List.of("3", "Alan", "Manchester"));

    private static Path   directory    = null;
    private static String dataSetGUID  = null;


    /**
     * Write the CSV file and catalog it from the template.
     */
    @Test
    @Order(1)
    void catalogCSVFile() throws IOException
    {
        directory = Path.of("build", "view-services-fvt-data", "data-engineer").toAbsolutePath();

        Files.createDirectories(directory);

        StringBuilder content = new StringBuilder(String.join(",", HEADER)).append("\n");

        for (List<String> row : ROWS)
        {
            content.append(String.join(",", row)).append("\n");
        }

        Files.writeString(directory.resolve(TABLE_NAME + ".csv"), content.toString());

        TemplateRequestBody requestBody = templateRequest("1.0");

        requestBody.setMetadataElementSubtypeName(OpenMetadataType.TABULAR_DATA_SET.typeName);

        dataSetGUID = automatedCuration.create("/catalog-templates/new-element", requestBody);

        assertEquals(OpenMetadataType.TABULAR_DATA_SET.typeName,
                     ClassificationExplorerFvtFixtures.readElement(dataSetGUID).getElementHeader().getType().getTypeName());
    }


    /**
     * The template should configure the CSV connector with the table name supplied in the tableName placeholder,
     * so that it reads {directoryPathName}/{tableName}.csv.  It configures the literal string "tableName" instead
     * because TabularDataSetTemplateDefinition puts PlaceholderProperty.TABLE_NAME.getName() (and
     * TABLE_DESCRIPTION.getName()) into the connection's configuration properties rather than getPlaceholder().
     */
    @Test
    @Order(2)
    void templateConfiguresTheTableName()
    {
        Map<String, Object> configurationProperties = connectionProperties().getConfigurationProperties();

        assertNotNull(configurationProperties);
        assertEquals(TABLE_NAME, configurationProperties.get("tableName"));
        assertEquals("Customers and the city they live in.", configurationProperties.get("tableDescription"));
    }


    /**
     * Until the template bug above is fixed, point the connector at the file by setting the table name in the
     * connection's configuration properties with the Connection Maker, as a user would after cataloguing.
     */
    @Test
    @Order(3)
    void setTheTableNameOnTheConnection()
    {
        ConnectionProperties properties = new ConnectionProperties();

        properties.setConfigurationProperties(Map.of("directoryPathName", directory.toString(),
                                                     "tableName", TABLE_NAME,
                                                     "tableDescription", "Customers and the city they live in."));

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setMergeUpdate(true);
        requestBody.setProperties(properties);

        connectionMaker.post("/connections/" + connectionGUID() + "/update", requestBody, BooleanResponse.class);

        assertEquals(TABLE_NAME, connectionProperties().getConfigurationProperties().get("tableName"));
    }


    /**
     * The report describes the file's table and returns the rows asked for.
     */
    @Test
    @Order(4)
    void tabularDataSetReport()
    {
        TabularDataSetReport report = report(1, 2);

        assertEquals(TABLE_NAME, report.getTableName());
        assertEquals(ROWS.size(), report.getRecordCount());

        List<String> columnNames = report.getColumnDescriptions().stream().map(TabularColumnDescription::columnName).toList();

        assertEquals(HEADER, columnNames);
        assertEquals(Map.of("1", ROWS.get(1), "2", ROWS.get(2)), report.getDataRecords());
    }


    /**
     * maxRowCount 0 (the default) returns the description and no rows.
     */
    @Test
    @Order(5)
    void tabularDataSetReportWithoutRows()
    {
        TabularDataSetReport report = report(0, 0);

        assertEquals(ROWS.size(), report.getRecordCount());
        assertTrue((report.getDataRecords() == null) || report.getDataRecords().isEmpty(),
                   "maxRowCount=0 should return no rows: " + report.getDataRecords());
    }


    /**
     * The Asset Maker's own create-from-template request should create an asset of the template's type (here a
     * TabularDataSet, classified with DataAssetEncoding).  It creates a plain Asset instead and so fails when
     * the template's classifications are copied.
     */
    @Test
    @Order(6)
    void assetMakerFromTemplateKeepsTheTemplatesType()
    {
        String guid = ClassificationExplorerFvtFixtures.assetMaker.create("/assets/from-template", templateRequest("2.0"));

        try
        {
            assertEquals(OpenMetadataType.TABULAR_DATA_SET.typeName,
                         ClassificationExplorerFvtFixtures.readElement(guid).getElementHeader().getType().getTypeName());
        }
        finally
        {
            ClassificationExplorerFvtFixtures.deleteAsset(guid);
        }
    }


    @AfterAll
    static void cleanUp() throws IOException
    {
        if (dataSetGUID != null)
        {
            /*
             * The template's connection is anchored to the asset and goes with it; its embedded file store
             * connection is deleted explicitly if it is still there.
             */
            String connectionGUID = connectionGUID();

            List<String> embeddedConnections = ClassificationExplorerFvtFixtures.relatedGUIDs(connectionGUID,
                                                                                               OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName);

            ClassificationExplorerFvtFixtures.deleteAsset(dataSetGUID);

            ViewServicesFvtTestSupport.assertGone(connectionMaker, "/connections/" + connectionGUID + "/retrieve");

            for (String guid : embeddedConnections)
            {
                OpenMetadataRootElementResponse embedded = connectionMaker.postUnchecked("/connections/" + guid + "/retrieve",
                                                                                         new GetRequestBody(),
                                                                                         OpenMetadataRootElementResponse.class);

                if ((embedded.getRelatedHTTPCode() == 200) && (embedded.getElement() != null))
                {
                    connectionMaker.call("/connections/" + guid + "/delete", new DeleteElementRequestBody());
                }

                ViewServicesFvtTestSupport.assertGone(connectionMaker, "/connections/" + guid + "/retrieve");
            }
        }

        if (directory != null)
        {
            Files.deleteIfExists(directory.resolve(TABLE_NAME + ".csv"));
        }
    }


    private static String connectionGUID()
    {
        List<String> connections = ClassificationExplorerFvtFixtures.relatedGUIDs(dataSetGUID, OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName);

        assertEquals(1, connections.size(), "the data set should have one connection: " + connections);

        return connections.get(0);
    }


    private static ConnectionProperties connectionProperties()
    {
        return assertInstanceOf(ConnectionProperties.class,
                                ClassificationExplorerFvtFixtures.readElement(connectionGUID()).getProperties());
    }


    private static TemplateRequestBody templateRequest(String versionIdentifier)
    {
        Map<String, String> placeholders = new HashMap<>();

        placeholders.put(PlaceholderProperty.DISPLAY_NAME.getName(), "Customers");
        placeholders.put(PlaceholderProperty.DESCRIPTION.getName(), "Customer list for the data engineer FVT.");
        placeholders.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), directory.toString());
        placeholders.put(PlaceholderProperty.TABLE_NAME.getName(), TABLE_NAME);
        placeholders.put(PlaceholderProperty.TABLE_DESCRIPTION.getName(), "Customers and the city they live in.");
        placeholders.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), versionIdentifier);

        TemplateRequestBody requestBody = new TemplateRequestBody();

        requestBody.setTemplateGUID(CSV_TABULAR_DATA_SET_TEMPLATE_GUID);
        requestBody.setIsOwnAnchor(true);
        requestBody.setPlaceholderPropertyValues(placeholders);

        return requestBody;
    }


    private static TabularDataSetReport report(int startFromRow,
                                               int maxRowCount)
    {
        TabularDataSetReport report = dataEngineer.get("/tabular-data-sets/" + dataSetGUID + "/report?startFromRow=" + startFromRow + "&maxRowCount=" + maxRowCount,
                                                       TabularDataSetReportResponse.class).getTabularDataSetReport();

        assertNotNull(report, "the data engineer returned no report for " + dataSetGUID);

        return report;
    }
}
