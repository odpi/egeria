/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.repositoryservices.archivemanager.opentypes;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.testng.annotations.Test;

/**
 * Test that the open metadata archive types load without error.
 */
public class TestOpenMetadataTypesArchive
{
    @Test
    public void testOpenMetadataTypesLoad()
    {
        OpenMetadataTypesArchive archive = new OpenMetadataTypesArchive();
        //load the archive
        OpenMetadataArchive          archiveProperties = archive.getOpenMetadataArchive();
        OpenMetadataArchiveTypeStore typeStore         = archiveProperties.getArchiveTypeStore();

        assert (typeStore != null);
    }
}

