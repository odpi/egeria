/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.archiveutilities.openconnectors.apacheatlas.ApacheAtlasPackArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.apachekafka.ApacheKafkaPackArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.postgres.PostgresPackArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.unitycatalog.UnityCatalogPackArchiveWriter;

/**
 * CocoArchivesWriter provides the main method to run the open metadata archive writers
 * that create each of the open metadata archives used in the open metadata labs and
 * other scenarios with Coco Pharmaceuticals.
 */
public class CorePacksArchivesWriter
{
    /**
     * Main program to initiate the archive writers for the core content packs.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            String folderName = "content-packs";

            CorePackArchiveWriter corePackArchiveWriter = new CorePackArchiveWriter();
            corePackArchiveWriter.writeOpenMetadataArchive(folderName);

            UnityCatalogPackArchiveWriter unityCatalogPackArchiveWriter = new UnityCatalogPackArchiveWriter();
            unityCatalogPackArchiveWriter.writeOpenMetadataArchive(folderName);

            ApacheAtlasPackArchiveWriter apacheAtlasPackArchiveWriter = new ApacheAtlasPackArchiveWriter();
            apacheAtlasPackArchiveWriter.writeOpenMetadataArchive(folderName);

            ApacheKafkaPackArchiveWriter apacheKafkaPackArchiveWriter = new ApacheKafkaPackArchiveWriter();
            apacheKafkaPackArchiveWriter.writeOpenMetadataArchive(folderName);

            PostgresPackArchiveWriter postgresPackArchiveWriter = new PostgresPackArchiveWriter();
            postgresPackArchiveWriter.writeOpenMetadataArchive(folderName);
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
