/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.opentypes;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Test that the open metadata archive types load without error.  This archive only contains types.
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
        assert (typeStore.getNewTypeDefs() != null);
    }
}

