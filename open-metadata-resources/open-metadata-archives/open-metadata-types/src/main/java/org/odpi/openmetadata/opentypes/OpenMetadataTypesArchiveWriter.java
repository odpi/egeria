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
     * Generates and writes out an open metadata archive containing all of the open metadata types.
     */
    public void writeOpenMetadataTypesArchive()
    {
        OpenMetadataTypesArchive openMetadataTypesArchive = new OpenMetadataTypesArchive();

        super.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName,
                                       openMetadataTypesArchive.getOpenMetadataArchive());
    }

}
