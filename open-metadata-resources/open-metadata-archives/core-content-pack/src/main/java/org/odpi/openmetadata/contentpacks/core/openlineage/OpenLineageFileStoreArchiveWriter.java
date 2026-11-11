/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.openlineage;

import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;


/**
 * OpenLineageFileStoreArchiveWriter creates the open metadata archive that adds the OpenLineageFilePublisher
 * integration connector to the Open Lineage integration group.  The connector saves every open lineage event that
 * Egeria receives or generates as a JSON file in the platform's logs/openlineage directory.
 * <br>
 * It is separate from the OpenLineageContentPack so that a deployment can choose whether to keep the file store:
 * Egeria's default runtime loads it, whereas a deployment that keeps its open lineage events in a database
 * leaves it out, since the file store has no retention and grows without bound.
 */
public class OpenLineageFileStoreArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public OpenLineageFileStoreArchiveWriter()
    {
        super(ContentPackDefinition.OPEN_LINEAGE_FILE_STORE_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.OPEN_LINEAGE_FILE_STORE_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.OPEN_LINEAGE_FILE_STORE_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.OPEN_LINEAGE_FILE_STORE_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive(),
                                        new OpenLineageArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * The file publisher is defined in the OpenLineageContentPack.  Registering it with the Open Lineage
         * integration group is what makes the integration daemon run it.
         */
        super.addIntegrationConnectorRegistrations(ContentPackDefinition.OPEN_LINEAGE_FILE_STORE_CONTENT_PACK);

        /*
         * Describe the solution that this content pack switches on.
         */
        super.addSolutionBlueprints(ContentPackDefinition.OPEN_LINEAGE_FILE_STORE_CONTENT_PACK, null);

        /*
         * Saving the GUIDs means that the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Main program to initiate the archive writer.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            OpenLineageFileStoreArchiveWriter archiveWriter = new OpenLineageFileStoreArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
