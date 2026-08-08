/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core;

import org.odpi.openmetadata.contentpacks.core.apacheatlas.ApacheAtlasPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.apachekafka.ApacheKafkaPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.apis.APIPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.digitalproducts.ProductPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.egeria.EgeriaArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.files.FilesArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.mssql.MSSQLPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.db2luw.DB2LUWPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.duckdb.DuckDBPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.oracle.OraclePackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.openlineage.OpenLineageArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.organizationinsight.OrganizationInsightArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.postgres.PostgresPackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.unitycatalog.UnityCatalogPackArchiveWriter;

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

            EgeriaArchiveWriter egeriaArchiveWriter = new EgeriaArchiveWriter();
            egeriaArchiveWriter.writeOpenMetadataArchive(folderName);

            OrganizationInsightArchiveWriter organizationInsightArchiveWriter = new OrganizationInsightArchiveWriter();
            organizationInsightArchiveWriter.writeOpenMetadataArchive(folderName);

            FilesArchiveWriter filesArchiveWriter = new FilesArchiveWriter();
            filesArchiveWriter.writeOpenMetadataArchive(folderName);

            ApacheKafkaPackArchiveWriter apacheKafkaPackArchiveWriter = new ApacheKafkaPackArchiveWriter();
            apacheKafkaPackArchiveWriter.writeOpenMetadataArchive(folderName);

            OpenLineageArchiveWriter openLineageArchiveWriter = new OpenLineageArchiveWriter();
            openLineageArchiveWriter.writeOpenMetadataArchive(folderName);

            PostgresPackArchiveWriter postgresPackArchiveWriter = new PostgresPackArchiveWriter();
            postgresPackArchiveWriter.writeOpenMetadataArchive(folderName);

            MSSQLPackArchiveWriter mssqlPackArchiveWriter = new MSSQLPackArchiveWriter();
            mssqlPackArchiveWriter.writeOpenMetadataArchive(folderName);

            OraclePackArchiveWriter oraclePackArchiveWriter = new OraclePackArchiveWriter();
            oraclePackArchiveWriter.writeOpenMetadataArchive(folderName);

            DB2LUWPackArchiveWriter db2luwPackArchiveWriter = new DB2LUWPackArchiveWriter();
            db2luwPackArchiveWriter.writeOpenMetadataArchive(folderName);

            DuckDBPackArchiveWriter duckDBPackArchiveWriter = new DuckDBPackArchiveWriter();
            duckDBPackArchiveWriter.writeOpenMetadataArchive(folderName);

            ProductPackArchiveWriter productPackArchiveWriter = new ProductPackArchiveWriter();
            productPackArchiveWriter.writeOpenMetadataArchive(folderName);

            UnityCatalogPackArchiveWriter unityCatalogPackArchiveWriter = new UnityCatalogPackArchiveWriter();
            unityCatalogPackArchiveWriter.writeOpenMetadataArchive(folderName);

            ApacheAtlasPackArchiveWriter apacheAtlasPackArchiveWriter = new ApacheAtlasPackArchiveWriter();
            apacheAtlasPackArchiveWriter.writeOpenMetadataArchive(folderName);

            APIPackArchiveWriter apiPackArchiveWriter = new APIPackArchiveWriter();
            apiPackArchiveWriter.writeOpenMetadataArchive(folderName);
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
