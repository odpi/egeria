/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors;

import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;

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
            CorePackArchiveWriter corePackArchiveWriter = new CorePackArchiveWriter();
            corePackArchiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
