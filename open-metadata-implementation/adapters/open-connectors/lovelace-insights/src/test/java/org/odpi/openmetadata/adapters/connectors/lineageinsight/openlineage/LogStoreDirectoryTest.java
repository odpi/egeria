/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.lineageinsight.openlineage;

import org.testng.annotations.Test;

import java.io.File;
import java.util.Map;

import static org.testng.Assert.assertEquals;

/**
 * Without a log store action target, the OpenLineage analysis services read the directory named in the
 * logStoreDirectory request parameter.  Engine actions started without that parameter used to fail; they now use
 * the directory the file-based OpenLineage log store publisher writes to by default.
 */
public class LogStoreDirectoryTest
{
    @Test
    public void testRequestParameterNamesTheDirectory()
    {
        File directory = LovelaceOpenLineageAnalysisServiceBase.getLogStoreDirectory(Map.of(OpenLineageAnalysisRequestParameter.LOG_STORE_DIRECTORY.getName(),
                                                                                            "/data/lineage"));

        assertEquals(directory, new File("/data/lineage"));
    }


    @Test
    public void testDefaultIsThePublishersDirectory()
    {
        assertEquals(LovelaceOpenLineageAnalysisServiceBase.getLogStoreDirectory(null), new File("logs/openlineage"));
        assertEquals(LovelaceOpenLineageAnalysisServiceBase.getLogStoreDirectory(Map.of("otherParameter", "value")), new File("logs/openlineage"));
    }
}
