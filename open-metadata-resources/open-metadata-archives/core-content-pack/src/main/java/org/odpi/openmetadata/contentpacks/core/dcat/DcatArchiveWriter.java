/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.dcat;

import org.odpi.openmetadata.adapters.connectors.integration.dcat.DcatPublisherIntegrationProvider;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationConnectorDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;


/**
 * DcatArchiveWriter creates an open metadata archive that includes the integration connectors that receive,
 * catalog and publish descriptions of data assets in the W3C Data Catalog Vocabulary (DCAT).  It also includes the
 * DCAT Publishing Catalog collection, which is attached to the DCAT Publisher as its catalog target.
 */
public class DcatArchiveWriter extends ContentPackBaseArchiveWriter
{
    private static final String DCAT_PUBLISHING_CATALOG_GUID = "9b07a6ef-4d24-40a8-bf7a-c6fda6b360a8";

    /**
     * Default constructor initializes the archive.
     */
    public DcatArchiveWriter()
    {
        super(ContentPackDefinition.DCAT_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.DCAT_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.DCAT_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.DCAT_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.DCAT_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.DCAT_CONTENT_PACK);

        /*
         * Create the integration group and its connectors.
         */
        super.addIntegrationGroups(ContentPackDefinition.DCAT_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.DCAT_CONTENT_PACK);

        /*
         * Add the collection that lists the assets to publish, and attach it to the DCAT Publisher.
         */
        this.addDcatPublishingCatalog();

        /*
         * Governance engines, services and request types (none defined at present, but the content pack is
         * processed in the standard way so they can be added later).
         */
        super.createGovernanceEngines(ContentPackDefinition.DCAT_CONTENT_PACK);
        super.createGovernanceServices(ContentPackDefinition.DCAT_CONTENT_PACK);
        super.createRequestTypes(ContentPackDefinition.DCAT_CONTENT_PACK);

        /*
         * Define the solution components for this solution.
         */
        super.addSolutionBlueprints(ContentPackDefinition.DCAT_CONTENT_PACK, new ArrayList<>());
        super.addSolutionLinkingWires(ContentPackDefinition.DCAT_CONTENT_PACK);

        /*
         * Saving the GUIDs means that the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Add the DCAT Publishing Catalog.  This is the root collection that an organization uses to choose which of its
     * assets are described to the outside world using the W3C Data Catalog Vocabulary (DCAT).  It is empty: an asset is
     * published when it is made a member of this collection, or of a collection folder nested in it.  The collection
     * is attached to the DCAT Publisher integration connector as a catalog target.  Other collections (of any type)
     * can be attached to the connector in the same way, and each is published as a separate DCAT catalog.
     */
    private void addDcatPublishingCatalog()
    {
        archiveHelper.setGUID(DcatPublisherIntegrationProvider.DEFAULT_PUBLISHING_CATALOG, DCAT_PUBLISHING_CATALOG_GUID);

        archiveHelper.addCollection(OpenMetadataType.ROOT_COLLECTION.typeName,
                                    DCAT_PUBLISHING_CATALOG_GUID,
                                    OpenMetadataType.ROOT_COLLECTION.typeName,
                                    OpenMetadataType.COLLECTION.typeName,
                                    null,
                                    null,
                                    DcatPublisherIntegrationProvider.DEFAULT_PUBLISHING_CATALOG,
                                    "DCAT Publishing Catalog",
                                    "The assets that are published using the W3C Data Catalog Vocabulary (DCAT).  Add an asset to this collection, or to a collection folder nested in it, and the DCAT Publisher integration connector writes its DCAT description, along with a DCAT catalog that lists every published asset, to its destination directory.  Remove the asset from the collection and its description is withdrawn.",
                                    null,
                                    null,
                                    null,
                                    null);

        archiveHelper.addCatalogTargetRelationship(IntegrationConnectorDefinition.DCAT_PUBLISHER.getGUID(),
                                                   DCAT_PUBLISHING_CATALOG_GUID,
                                                   DcatPublisherIntegrationProvider.CATALOG_TARGET_NAME,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null);
    }


    /**
     * Main program to initiate the archive writer for the DCAT content pack.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            DcatArchiveWriter archiveWriter = new DcatArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
