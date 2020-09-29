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
    private static final String defaultOpenMetadataArchiveFileName = "OpenMetadataTypes.json";

    /**
     * Default constructor
     */
    private OpenMetadataTypesArchiveWriter()
    {
    }


    /**
     * Generates and writes out an open metadata archive containing all of the open metadata types.
     */
    private void writeOpenMetadataTypesArchive()
    {
        OpenMetadataTypesArchive openMetadataTypesArchive = new OpenMetadataTypesArchive();

        this.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName,
                                      openMetadataTypesArchive.getOpenMetadataArchive());
    }


    /**
     * Main program to control the archive writer.
     * Note: See issue #3392 if logging is needed
     *
     * @param args ignored arguments
     */
    public static void main(String[] args)
    {
        OpenMetadataTypesArchiveWriter archiveWriter = new OpenMetadataTypesArchiveWriter();

        archiveWriter.writeOpenMetadataTypesArchive();
    }
}
