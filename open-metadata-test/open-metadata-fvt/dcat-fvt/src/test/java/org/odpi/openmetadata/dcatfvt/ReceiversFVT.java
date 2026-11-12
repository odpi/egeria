/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dcatfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMappingBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Checks the two ways that a DCAT document reaches the cataloguer without an event broker: a file dropped into the
 * loading bay that the DCAT Files Receiver monitors, and a call to the integration daemon's REST API.  In both cases
 * the DCAT Cataloguer, which is listening in the same integration daemon, is the connector that creates the metadata.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ReceiversFVT
{
    private static final String DATASET_DOCUMENT = "air-quality-dataset.jsonld";
    private static final String CATALOG_DOCUMENT = "transport-catalog.jsonld";


    @Test
    @DisplayName("A dataset description dropped into the loading bay is catalogued as a data set with its distributions")
    public void testDatasetArrivesThroughFiles() throws Exception
    {
        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = context.getOpenMetadataStore();

        File dropped = new File(DcatFvtTestSupport.LOADING_BAY_DIRECTORY, DATASET_DOCUMENT);

        Files.writeString(dropped.toPath(), DcatFvtTestSupport.readSampleDocument(DATASET_DOCUMENT), StandardCharsets.UTF_8);

        /*
         * The receiver scans its directory on refresh.  The refresh is synchronous and the cataloguer is called from
         * within it, so the data set is normally there by the time the call returns.
         */
        OMAGPlatformExtension.getIntegrationDaemonClient().refreshConnector(IntegrationConnectorDefinition.DCAT_FILES_RECEIVER.getConnectorName());

        OpenMetadataElement dataset = DcatFvtTestSupport.waitForElement(openMetadataStore,
                                                                        DcatMappingBase.DATASET_QUALIFIED_NAME_PREFIX + "https://data.example.gov/dataset/air-quality-2025",
                                                                        "Cataloguing the dropped dataset description");

        assertEquals(OpenMetadataType.DATA_SET.typeName, dataset.getType().getTypeName(), "The dataset was not catalogued as a DataSet.");

        OpenMetadataElement csv = openMetadataStore.getMetadataElementByUniqueName(DcatMappingBase.DISTRIBUTION_QUALIFIED_NAME_PREFIX + "https://data.example.gov/dataset/air-quality-2025/csv", "qualifiedName");
        OpenMetadataElement api = openMetadataStore.getMetadataElementByUniqueName(DcatMappingBase.DISTRIBUTION_QUALIFIED_NAME_PREFIX + "https://data.example.gov/dataset/air-quality-2025/api", "qualifiedName");

        assertNotNull(csv, "The distribution with a download URL was not catalogued.");
        assertNotNull(api, "The distribution with only an access URL was not catalogued.");
        assertEquals(OpenMetadataType.DATA_FILE.typeName, csv.getType().getTypeName(), "A distribution with a download URL should be a DataFile.");
        assertEquals(OpenMetadataType.DATA_STORE.typeName, api.getType().getTypeName(), "A distribution with only an access URL should be a DataStore.");

        RelatedMetadataElementList content = openMetadataStore.getRelatedMetadataElements(dataset.getElementGUID(), 1, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, 0, 0);

        assertNotNull(content, "The data set has no content.");
        assertEquals(2, content.getElementList().size(), "The data set should be linked to its two distributions.");

        assertNotNull(openMetadataStore.getMetadataElementByUniqueName(OpenMetadataType.ORGANIZATION.typeName + DcatMappingBase.SEPARATOR + "Environment Agency", "qualifiedName"),
                      "The publisher was not catalogued as an organization.");
    }


    @Test
    @DisplayName("A catalog sent to the integration daemon's REST API is catalogued as a collection of data sets and a deployed API")
    public void testCatalogArrivesThroughTheDaemon() throws Exception
    {
        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        OpenMetadataStore    openMetadataStore = context.getOpenMetadataStore();

        OMAGPlatformExtension.getIntegrationDaemonClient().publishDcatDocument(DcatFvtTestSupport.readSampleDocument(CATALOG_DOCUMENT));

        OpenMetadataElement catalog = DcatFvtTestSupport.waitForElement(openMetadataStore,
                                                                        DcatMappingBase.CATALOG_QUALIFIED_NAME_PREFIX + "https://transport.example.gov/catalog",
                                                                        "Cataloguing the catalog sent to the integration daemon");

        assertEquals(OpenMetadataType.COLLECTION_FOLDER.typeName, catalog.getType().getTypeName(), "The catalog was not catalogued as a CollectionFolder.");

        RelatedMetadataElementList members = openMetadataStore.getRelatedMetadataElements(catalog.getElementGUID(), 1, OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, 0, 0);

        assertNotNull(members, "The catalog's collection has no members.");
        assertEquals(4, members.getElementList().size(), "The catalog's collection should hold the dataset series, its two datasets and the data service.");

        OpenMetadataElement series  = openMetadataStore.getMetadataElementByUniqueName(DcatMappingBase.DATASET_QUALIFIED_NAME_PREFIX + "https://transport.example.gov/dataset/bus-timetables", "qualifiedName");
        OpenMetadataElement service = openMetadataStore.getMetadataElementByUniqueName(DcatMappingBase.DATA_SERVICE_QUALIFIED_NAME_PREFIX + "https://transport.example.gov/service/timetable-api", "qualifiedName");

        assertNotNull(series, "The dataset series was not catalogued.");
        assertNotNull(service, "The data service was not catalogued.");
        assertEquals(OpenMetadataType.DEPLOYED_API.typeName, service.getType().getTypeName(), "The data service was not catalogued as a DeployedAPI.");

        RelatedMetadataElementList seriesMembers = openMetadataStore.getRelatedMetadataElements(series.getElementGUID(), 1, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, 0, 0);

        assertNotNull(seriesMembers, "The dataset series is not linked to its members.");
        assertEquals(2, seriesMembers.getElementList().size(), "The dataset series should be linked to its two datasets.");

        RelatedMetadataElementList served = openMetadataStore.getRelatedMetadataElements(service.getElementGUID(), 2, OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, 0, 0);

        assertNotNull(served, "The data service is not linked to the datasets it serves.");
        assertEquals(2, served.getElementList().size(), "The data service should be linked to the two datasets it serves.");
    }
}
