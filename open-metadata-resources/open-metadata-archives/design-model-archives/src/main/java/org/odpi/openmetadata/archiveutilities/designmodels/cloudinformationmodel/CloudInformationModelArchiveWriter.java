/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;

import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;


/**
 * OpenMetadataTypesArchiveWriter create a physical open metadata archive file for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.
 */
public class CloudInformationModelArchiveWriter extends OMRSArchiveWriter
{
    private static final String defaultOpenMetadataArchiveFileName = "CloudInformationModel.json";

    /**
     * Default constructor
     */
    private CloudInformationModelArchiveWriter()
    {
    }


    /**
     * Generates and writes out an open metadata archive containing all of the open metadata types.
     */
    private void writeOpenMetadataTypesArchive()
    {
        CloudInformationModelArchive cloudInformationModelArchive = new CloudInformationModelArchive();

        this.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName,
                                      cloudInformationModelArchive.getOpenMetadataArchive());
    }


    /**
     * Main program to control the archive writer.
     *
     * @param args ignored arguments
     */
    public static void main(String[] args)
    {
        CloudInformationModelArchiveWriter archiveWriter = new CloudInformationModelArchiveWriter();

        archiveWriter.writeOpenMetadataTypesArchive();
    }
}
