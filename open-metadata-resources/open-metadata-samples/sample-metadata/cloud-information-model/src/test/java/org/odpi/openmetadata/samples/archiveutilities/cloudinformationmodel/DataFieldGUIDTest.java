/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * The GUID of the data field that represents a link comes from the GUID map when the map already holds one, so it
 * stays the same from one build of the archive to the next.  It used to be overwritten by the link's GUID from the
 * model - or by a random one where the model had none - so the GUID map refused to write the archive.
 */
public class DataFieldGUIDTest
{
    @Test
    public void testGUIDMapWins()
    {
        assertEquals(CloudInformationModelArchiveWriter.chooseDataFieldGUID("mapped-guid", "model-guid"), "mapped-guid");
        assertEquals(CloudInformationModelArchiveWriter.chooseDataFieldGUID("mapped-guid", null), "mapped-guid");
    }


    @Test
    public void testModelGUIDIsUsedForANewDataField()
    {
        assertEquals(CloudInformationModelArchiveWriter.chooseDataFieldGUID(null, "model-guid"), "model-guid");
    }


    @Test
    public void testNeitherKnownMeansANewGUID()
    {
        assertNull(CloudInformationModelArchiveWriter.chooseDataFieldGUID(null, null));
    }
}
