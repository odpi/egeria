/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.testng.annotations.Test;

/**
 * Test that the cloud information model content loads without error.
 */
public class TestCloudInformationModelArchive
{
    @Test
    public void testOpenMetadataTypesLoad()
    {
        CloudInformationModelArchive archive = new CloudInformationModelArchive();

        OpenMetadataArchive              archiveProperties = archive.getOpenMetadataArchive();
        OpenMetadataArchiveInstanceStore instanceStore     = archiveProperties.getArchiveInstanceStore();

        // assert (instanceStore != null);
    }
}

