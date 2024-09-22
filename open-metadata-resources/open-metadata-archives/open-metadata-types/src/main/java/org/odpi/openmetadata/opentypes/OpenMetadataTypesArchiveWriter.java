/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes;

import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;



/**
 * OpenMetadataTypesArchiveWriter create a physical open metadata archive file for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.
 */
public class OpenMetadataTypesArchiveWriter extends OMRSArchiveWriter
{
    private static final String defaultOpenMetadataArchiveFileName = "OpenMetadataTypes.omarchive";

    /**
     * Default constructor
     */
    public OpenMetadataTypesArchiveWriter()
    {
    }


    /**
     * Generates and writes out the open metadata archive created in the builder.
     *
     * @param folderName name of the folder to add the archive into
     */
    public void writeOpenMetadataTypesArchive(String folderName)
    {
        try
        {
            String pathName = defaultOpenMetadataArchiveFileName;

            if (folderName != null)
            {
                pathName = folderName + "/" + defaultOpenMetadataArchiveFileName;
            }

            OpenMetadataTypesArchive openMetadataTypesArchive = new OpenMetadataTypesArchive();

            System.out.println("Writing to file: " + pathName);
            super.writeOpenMetadataArchive(pathName, openMetadataTypesArchive.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }
    }
}
