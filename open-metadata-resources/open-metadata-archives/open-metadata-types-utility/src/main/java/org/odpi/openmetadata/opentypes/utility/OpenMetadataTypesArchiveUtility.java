/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.opentypes.utility;

import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchiveWriter;



/**
 * OpenMetadataTypesArchiveWriter create a physical open metadata archive file for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.
 */
public class OpenMetadataTypesArchiveUtility
{
    /**
     * Main program to control the archive writer.
     * Note: See issue #3392 if logging is needed
     *
     * @param args ignored arguments
     */
    public static void main(String[] args)
    {
        OpenMetadataTypesArchiveWriter archiveWriter = new OpenMetadataTypesArchiveWriter();

        archiveWriter.writeOpenMetadataTypesArchive("content-packs");
    }
}
