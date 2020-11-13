/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

/**
 * Test that the cloud information model content loads without error.  This is a placeholder until the
 * CIM model is publicly available.
 *
 */
public class TestCloudInformationModelArchiveWriter
{
    private final String  cimModelLocation = "<Unknown>";

    @Test
    public void testOpenMetadataArchiveLoad()
    {
        // todo need to add test content to drive these test cases to avoid pulling content from the internet
        // todo during a build.  Possibly turn this into FVT.
        /*
        try
        {
            CloudInformationModelParser         parser  = new CloudInformationModelParser(cimModelLocation);
            CloudInformationModelArchiveBuilder archive = new CloudInformationModelArchiveBuilder(parser);

            OpenMetadataArchive              archiveProperties = archive.getOpenMetadataArchive();
            OpenMetadataArchiveInstanceStore instanceStore     = archiveProperties.getArchiveInstanceStore();

            assert (instanceStore != null);
        }
        catch (IOException error)
        {
            assert(false);
        }
        */
    }


    @Test
    public void testOpenMetadataArchiveWrite()
    {
        /*
        try
        {
            CloudInformationModelArchiveWriter  archiveWriter  = new CloudInformationModelArchiveWriter(cimModelLocation);

            archiveWriter.writeOpenMetadataArchive();

            File archiveFile = new File(CloudInformationModelArchiveWriter.defaultOpenMetadataArchiveFileName);

            assert (archiveFile.canRead());
        }
        catch (IOException error)
        {
            assert(false);
        }
        */
    }
}

