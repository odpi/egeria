/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.dcatfvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adapters.connectors.integration.dcat.controls.DcatConfigurationProperty;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMapper;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMappingResult;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatCatalog;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataset;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.client.IntegrationDaemon;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that the DCAT Publisher writes the description of each asset in the DCAT Publishing Catalog, and of the
 * catalog itself, and withdraws the description of an asset that is taken out of the catalog.  The publishing catalog
 * is the collection that the Core content pack defines, which the publisher is configured with by default.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class PublisherFVT
{
    private static final String IMPORTED_DATASET_IRI = "https://data.example.gov/dcat-fvt/publisher/rainfall";


    @Test
    @DisplayName("Assets in the DCAT Publishing Catalog, and its collection folders, are published and withdrawn")
    public void testPublishingCatalog() throws Exception
    {
        ConnectorContextBase context           = ConnectorContextFactory.newContext();
        CollectionClient     collectionClient  = context.getCollectionClient();
        IntegrationDaemon    integrationDaemon = OMAGPlatformExtension.getIntegrationDaemonClient();
        String               publisherName     = IntegrationConnectorDefinition.DCAT_PUBLISHER.getConnectorName();

        OpenMetadataElement publishingCatalog = context.getOpenMetadataStore().getMetadataElementByUniqueName(DcatConfigurationProperty.DEFAULT_PUBLISHING_CATALOG, "qualifiedName");

        assertNotNull(publishingCatalog, "The DCAT Publishing Catalog is not in the repository - it should be supplied by the Core content pack.");
        assertEquals(OpenMetadataType.ROOT_COLLECTION.typeName, publishingCatalog.getType().getTypeName(), "The DCAT Publishing Catalog should be a root collection.");

        /*
         * One asset comes from a DCAT description and one is created directly in open metadata.
         */
        DcatMappingResult imported = new DcatMapper(context).catalogueDocument(DcatDocumentFormatter.parseDocument("""
                {
                  "@context": { "dcat": "http://www.w3.org/ns/dcat#", "dct": "http://purl.org/dc/terms/" },
                  "@id": "%s",
                  "@type": "dcat:Dataset",
                  "dct:title": "Rainfall measurements",
                  "dcat:keyword": "rainfall",
                  "dcat:distribution": { "@type": "dcat:Distribution", "dcat:downloadURL": { "@id": "https://data.example.gov/files/rainfall.csv" }, "dct:format": "CSV" }
                }
                """.formatted(IMPORTED_DATASET_IRI)), "dcat-fvt");

        String importedGUID = imported.getElementGUIDs().get(0);

        DataSetProperties nativeProperties = new DataSetProperties();

        nativeProperties.setQualifiedName("DataSet::dcat-fvt::river-levels");
        nativeProperties.setDisplayName("River levels");
        nativeProperties.setDescription("Readings from the river level gauges.");

        String nativeGUID = context.getAssetClient(OpenMetadataType.DATA_SET.typeName).createAsset(new NewElementOptions(context.getAssetClient().getMetadataSourceOptions()), null, nativeProperties, null);

        /*
         * The imported data set goes directly in the publishing catalog; the native one goes in a collection folder
         * nested in it.
         */
        CollectionFolderProperties folderProperties = new CollectionFolderProperties();

        folderProperties.setQualifiedName("CollectionFolder::dcat-fvt::hydrology");
        folderProperties.setDisplayName("Hydrology");

        String folderGUID = context.getCollectionClient(OpenMetadataType.COLLECTION_FOLDER.typeName).createCollection(new NewElementOptions(collectionClient.getMetadataSourceOptions()), null, folderProperties, null);

        collectionClient.addToCollection(publishingCatalog.getElementGUID(), importedGUID, collectionClient.getMakeAnchorOptions(false), new CollectionMembershipProperties());
        collectionClient.addToCollection(publishingCatalog.getElementGUID(), folderGUID, collectionClient.getMakeAnchorOptions(false), new CollectionMembershipProperties());
        collectionClient.addToCollection(folderGUID, nativeGUID, collectionClient.getMakeAnchorOptions(false), new CollectionMembershipProperties());

        integrationDaemon.refreshConnector(publisherName);

        File catalogFile    = new File(DcatFvtTestSupport.STORE_DIRECTORY, "catalog.jsonld");
        File datasetsFolder = new File(DcatFvtTestSupport.STORE_DIRECTORY, "datasets");

        DcatFvtTestSupport.waitFor("The publisher writing " + catalogFile.getPath(), catalogFile::isFile);

        DcatDocument published = DcatDocumentFormatter.parseDocument(Files.readString(catalogFile.toPath(), StandardCharsets.UTF_8));

        assertEquals(1, published.getCatalogs().size(), "The catalog file should describe one catalog.");

        DcatCatalog catalog = published.getCatalogs().get(0);

        assertEquals("DCAT Publishing Catalog", catalog.getTitle());
        assertEquals(2, catalog.getDatasetIRIs().size(), "The catalog should list the two published data sets: " + catalog.getDatasetIRIs());
        assertTrue(catalog.getDatasetIRIs().contains(IMPORTED_DATASET_IRI), "The imported data set should keep its IRI.");
        assertTrue(catalog.getDatasetIRIs().contains("urn:uuid:" + nativeGUID), "The native data set should be identified by its GUID.");
        assertEquals(2, published.getDatasets().size(), "The catalog file should describe both data sets.");

        List<String> titles = new ArrayList<>();

        for (DcatDataset dataset : published.getDatasets())
        {
            titles.add(dataset.getTitle());

            if (IMPORTED_DATASET_IRI.equals(dataset.getIri()))
            {
                assertEquals(List.of("rainfall"), dataset.getKeywords());
                assertEquals("https://data.example.gov/files/rainfall.csv", dataset.getDistributions().get(0).getDownloadURL());
            }
        }

        assertTrue(titles.contains("Rainfall measurements") && titles.contains("River levels"), "Unexpected data sets in the catalog: " + titles);

        File[] datasetFiles = datasetsFolder.listFiles();

        assertNotNull(datasetFiles, "The publisher did not create " + datasetsFolder.getPath());
        assertEquals(2, datasetFiles.length, "There should be one file for each published data set.");

        File importedFile = null;

        for (File datasetFile : datasetFiles)
        {
            DcatDocument datasetDocument = DcatDocumentFormatter.parseDocument(Files.readString(datasetFile.toPath(), StandardCharsets.UTF_8));

            assertEquals(1, datasetDocument.getDatasets().size(), datasetFile.getPath() + " should describe one data set.");

            if (IMPORTED_DATASET_IRI.equals(datasetDocument.getDatasets().get(0).getIri()))
            {
                importedFile = datasetFile;
            }
        }

        assertNotNull(importedFile, "No file describes the imported data set.");

        /*
         * Nothing has changed, so a second refresh leaves the files alone.
         */
        long catalogLastModified = catalogFile.lastModified();

        integrationDaemon.refreshConnector(publisherName);

        assertEquals(catalogLastModified, catalogFile.lastModified(), "The catalog file was rewritten although nothing had changed.");

        /*
         * Taking the imported data set out of the publishing catalog withdraws its description.
         */
        collectionClient.removeFromCollection(publishingCatalog.getElementGUID(), importedGUID, collectionClient.getDeleteOptions(false));

        integrationDaemon.refreshConnector(publisherName);

        final File withdrawnFile = importedFile;

        DcatFvtTestSupport.waitFor("The publisher removing " + withdrawnFile.getPath(), () -> ! withdrawnFile.exists());

        published = DcatDocumentFormatter.parseDocument(Files.readString(catalogFile.toPath(), StandardCharsets.UTF_8));

        assertEquals(List.of("urn:uuid:" + nativeGUID), published.getCatalogs().get(0).getDatasetIRIs(), "Only the native data set should still be published.");
        assertFalse(Files.readString(catalogFile.toPath(), StandardCharsets.UTF_8).contains(IMPORTED_DATASET_IRI), "The withdrawn data set is still described in the catalog file.");
    }
}
