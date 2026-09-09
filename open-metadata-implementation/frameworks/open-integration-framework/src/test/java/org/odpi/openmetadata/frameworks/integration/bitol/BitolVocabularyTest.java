/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol;

import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractComparisonOperator;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractQualitySeverity;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContractSemanticType;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProductType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Validate the vocabularies added for ODCS v3.2.0 and ODPS v1.1.0.
 */
public class BitolVocabularyTest
{
    /**
     * Default constructor
     */
    public BitolVocabularyTest()
    {
    }


    @Test public void testLookups()
    {
        assertEquals(DataContractSemanticType.fromValue("measure"), DataContractSemanticType.MEASURE);
        assertEquals(DataContractSemanticType.fromValue("DIMENSION"), DataContractSemanticType.DIMENSION, "lookup is case-insensitive");
        assertNull(DataContractSemanticType.fromValue("metric"));

        assertEquals(DataContractComparisonOperator.fromValue("mustBeGreaterOrEqualTo"), DataContractComparisonOperator.MUST_BE_GREATER_OR_EQUAL_TO);
        assertEquals(DataContractComparisonOperator.values().length, 8);

        assertEquals(DataContractQualitySeverity.fromValue("error").getValue(), "error");
        assertEquals(DataProductType.fromValue("consumerAligned"), DataProductType.CONSUMER_ALIGNED);
        assertNull(DataProductType.fromValue("myOwnType"), "organizations may define their own types");
    }
}
