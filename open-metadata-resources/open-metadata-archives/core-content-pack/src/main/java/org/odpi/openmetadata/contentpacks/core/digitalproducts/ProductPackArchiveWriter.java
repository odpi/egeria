/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.digitalproducts;

import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;


/**
 * ProductPackArchiveWriter creates an open metadata archive that includes the connectors that work with
 * digital products, plus other useful metadata.
 */
public class ProductPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public ProductPackArchiveWriter()
    {
        super(ContentPackDefinition.PRODUCTS_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.PRODUCTS_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.PRODUCTS_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.PRODUCTS_CONTENT_PACK.getArchiveFileName(),
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
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.PRODUCTS_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.PRODUCTS_CONTENT_PACK);
        this.addTabularDataSetCatalogTemplates(ContentPackDefinition.PRODUCTS_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.PRODUCTS_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.PRODUCTS_CONTENT_PACK,
                                       IntegrationGroupDefinition.AUTO_PRODUCT_MANAGER);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.PRODUCTS_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.PRODUCTS_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.PRODUCTS_CONTENT_PACK);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.PRODUCTS_CONTENT_PACK);
        this.addSolutionLinkingWires(ContentPackDefinition.PRODUCTS_CONTENT_PACK);

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            ProductPackArchiveWriter archiveWriter = new ProductPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}