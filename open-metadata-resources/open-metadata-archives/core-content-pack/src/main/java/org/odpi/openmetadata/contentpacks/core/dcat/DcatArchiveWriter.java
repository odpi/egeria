/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.dcat;

import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;


/**
 * DcatArchiveWriter creates an open metadata archive that includes the integration connectors that receive,
 * catalog and publish descriptions of data assets in the W3C Data Catalog Vocabulary (DCAT).
 */
public class DcatArchiveWriter extends ContentPackBaseArchiveWriter
{
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
