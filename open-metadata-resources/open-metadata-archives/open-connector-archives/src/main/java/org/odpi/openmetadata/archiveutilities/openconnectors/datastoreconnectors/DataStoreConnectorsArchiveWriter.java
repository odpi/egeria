/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.datastoreconnectors;

import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;


/**
 * DataStoreConnectorsArchiveWriter creates a physical open metadata archive file for the connector
 * types that describe the open connectors to data store assets that are supported by ODPi Egeria.
 */
public class DataStoreConnectorsArchiveWriter extends OMRSArchiveWriter
{
    private static final String defaultOpenMetadataArchiveFileName = "DataStoreConnectorTypes.json";

    /**
     * Default constructor
     */
    private DataStoreConnectorsArchiveWriter()
    {
    }


    /**
     * Generates and writes out an open metadata archive containing all of the connector types
     * describing the ODPi Egeria data store open connectors.
     */
    private void writeOpenMetadataArchive()
    {
        try
        {
            DataStoreConnectorsArchiveBuilder archiveBuilder = new DataStoreConnectorsArchiveBuilder();

            super.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName,
                                           archiveBuilder.getOpenMetadataArchive());
        }
        catch (Throwable  error)
        {
            System.out.println("error is " + error.toString());
        }
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * ODPi Egeria.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            DataStoreConnectorsArchiveWriter archiveWriter = new DataStoreConnectorsArchiveWriter();

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Throwable error)
        {
            System.err.println("Exception: " + error.toString());
            System.exit(-1);
        }
    }
}
