/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.mendel;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;


/**
 * Checks when two path names are taken to identify the same place in a file system, which is what allows
 * two files, or two folders, to be validated as duplicates without a steward.
 */
public class MendelPathNameMatchTest
{
    @Test
    public void testSamePathName()
    {
        assertTrue(MendelAutomatedDuplicateManagerConnector.isSamePathName("/deployments/landing-area/events.parquet",
                                                                            "/deployments/landing-area/events.parquet"));
    }


    @Test
    public void testDifferentPathNames()
    {
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName("/deployments/landing-area/events.parquet",
                                                                             "/Users/someone/landing-area/events.parquet"));
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName("/deployments/landing-area/events.parquet",
                                                                             "/deployments/landing-area/Events.parquet"));
    }


    @Test
    public void testMissingPathNames()
    {
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName(null, null));
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName("", ""));
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName("  ", "  "));
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName("/deployments/landing-area", null));
        assertFalse(MendelAutomatedDuplicateManagerConnector.isSamePathName(null, "/deployments/landing-area"));
    }
}
