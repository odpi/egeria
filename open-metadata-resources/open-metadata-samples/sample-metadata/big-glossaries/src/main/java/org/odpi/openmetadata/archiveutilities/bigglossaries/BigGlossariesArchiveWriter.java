/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.bigglossaries;

import org.odpi.openmetadata.archiveutilities.bigglossaries.catalogcontent.BigGlossaryArchiveBuilder;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * BigGlossariesArchiveWriter creates 10 glossaries, each containing 10,000 unique terms.
 */
public class BigGlossariesArchiveWriter extends OMRSArchiveWriter
{
    private static final String openMetadataArchiveRootName  = "BigGlossary";
    private static final String bigGlossaryArchiveFileNamePostFix   = ".json";



    /**
     * Generates and writes out an open metadata archive containing all the content for the simple catalogs.
     */
    private void writeOpenMetadataArchives()
    {
        String indexes[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        List<String> glossaryIdentifiers = new ArrayList<>(Arrays.asList(indexes));
        try
        {
            List<OpenMetadataArchive> dependentArchives = new ArrayList<>();
            OpenMetadataArchive       newArchive;

            for (String glossaryIdentifier : glossaryIdentifiers)
            {
                String glossaryFileName = openMetadataArchiveRootName + glossaryIdentifier + bigGlossaryArchiveFileNamePostFix;

                System.out.println("Creating glossary: " + glossaryIdentifier);

                /*
                 * This value allows the simple archive to be based on the existing open metadata types
                 */
                dependentArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

                BigGlossaryArchiveBuilder apiArchiveBuilder = new BigGlossaryArchiveBuilder(glossaryIdentifier);

                newArchive = apiArchiveBuilder.getOpenMetadataArchive();
                dependentArchives.add(newArchive);

                super.writeOpenMetadataArchive(glossaryFileName, newArchive);
            }
        }
        catch (Exception  error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Main program to initiate the archive writer for the simple catalogs.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            BigGlossariesArchiveWriter archiveWriter = new BigGlossariesArchiveWriter();

            archiveWriter.writeOpenMetadataArchives();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
