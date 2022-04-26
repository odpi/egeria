/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.simplecatalogs;

import org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent.SimpleAPICatalogArchiveBuilder;
import org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent.SimpleDataCatalogArchiveBuilder;
import org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent.SimpleEventCatalogArchiveBuilder;
import org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent.SimpleGovernanceCatalogArchiveBuilder;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;
import java.util.List;


/**
 * SimpleCatalogsArchiveWriter creates four physical open metadata archive files for each of the somple catalogs.
 */
public class SimpleCatalogsArchiveWriter extends OMRSArchiveWriter
{
    private static final String openMetadataArchiveRootName           = "SimpleCatalog";
    private static final String eventOpenMetadataArchiveFileName      = "SimpleEventCatalog.json";
    private static final String apiOpenMetadataArchiveFileName        = "SimpleAPICatalog.json";
    private static final String dataOpenMetadataArchiveFileName       = "SimpleDataCatalog.json";
    private static final String governanceOpenMetadataArchiveFileName = "SimpleGovernanceCatalog.json";

    /**
     * Default constructor
     */
    private SimpleCatalogsArchiveWriter()
    {
    }


    /**
     * Generates and writes out an open metadata archive containing all the content for the simple catalogs.
     */
    private void writeOpenMetadataArchives()
    {
        try
        {
            List<OpenMetadataArchive> dependentArchives = new ArrayList<>();
            OpenMetadataArchive       newArchive;

            /*
             * This value allows the simple archive to be based on the existing open metadata types
             */
            dependentArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

            SimpleEventCatalogArchiveBuilder eventArchiveBuilder = new SimpleEventCatalogArchiveBuilder(openMetadataArchiveRootName);

            newArchive = eventArchiveBuilder.getOpenMetadataArchive();
            dependentArchives.add(newArchive);

            super.writeOpenMetadataArchive(eventOpenMetadataArchiveFileName, newArchive);

            SimpleAPICatalogArchiveBuilder apiArchiveBuilder = new SimpleAPICatalogArchiveBuilder(openMetadataArchiveRootName);

            newArchive = apiArchiveBuilder.getOpenMetadataArchive();
            dependentArchives.add(newArchive);

            super.writeOpenMetadataArchive(apiOpenMetadataArchiveFileName, newArchive);

            SimpleDataCatalogArchiveBuilder dataArchiveBuilder = new SimpleDataCatalogArchiveBuilder(openMetadataArchiveRootName);

            newArchive = dataArchiveBuilder.getOpenMetadataArchive();
            dependentArchives.add(newArchive);

            super.writeOpenMetadataArchive(dataOpenMetadataArchiveFileName, newArchive);

            SimpleGovernanceCatalogArchiveBuilder governanceArchiveBuilder = new SimpleGovernanceCatalogArchiveBuilder(openMetadataArchiveRootName, dependentArchives);

            super.writeOpenMetadataArchive(governanceOpenMetadataArchiveFileName, governanceArchiveBuilder.getOpenMetadataArchive());
        }
        catch (Exception  error)
        {
            System.out.println("error is " + error.toString());
        }
    }


    /**
     * Main program to initiate the archive writer for the simpe catalogs.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            SimpleCatalogsArchiveWriter archiveWriter = new SimpleCatalogsArchiveWriter();

            archiveWriter.writeOpenMetadataArchives();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error.toString());
            System.exit(-1);
        }
    }
}
