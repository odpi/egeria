/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;

import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;

import java.io.IOException;


/**
 * OpenMetadataTypesArchiveWriter creates a physical open metadata archive file for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.
 */
public class CloudInformationModelArchiveWriter extends OMRSArchiveWriter
{
    static final String defaultOpenMetadataArchiveFileName = "CloudInformationModel.json";

    private String cimModelLocation;


    /**
     * Default constructor
     *
     * @param cimModelLocation directory name for the CIM model's JSON-LD files.
     */
    CloudInformationModelArchiveWriter(String cimModelLocation)
    {
        this.cimModelLocation = cimModelLocation;
    }


    /**
     * Generates and writes out an open metadata archive containing all of the open metadata types.
     */
    void writeOpenMetadataArchive() throws IOException
    {
        CloudInformationModelParser         cloudInformationModelParser         = new CloudInformationModelParser(cimModelLocation);
        CloudInformationModelArchiveBuilder
                                            cloudInformationModelArchiveBuilder = new CloudInformationModelArchiveBuilder(cloudInformationModelParser);

        super.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName,
                                       cloudInformationModelArchiveBuilder.getOpenMetadataArchive());
    }


    /**
     * Main program to initiate the archive writer.
     *
     * @param args list of arguments - first one should be the directory where the model
     *             content is located.  Any other arguments passed are ignored.
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.err.println("USAGE: filename");
            System.exit(-1);
        }

        try
        {
            CloudInformationModelArchiveWriter archiveWriter = new CloudInformationModelArchiveWriter(args[0]);

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Throwable error)
        {
            System.err.println("Exception: " + error.toString());
            System.exit(-1);
        }
    }
}
