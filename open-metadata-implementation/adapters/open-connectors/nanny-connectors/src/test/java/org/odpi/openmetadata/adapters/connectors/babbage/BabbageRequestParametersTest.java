/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.babbage;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Babbage passes its catalog target's configuration properties to the governance action it starts, as request
 * parameters.  It used to pass none.
 */
public class BabbageRequestParametersTest
{
    @Test
    public void testConfigurationPropertiesBecomeRequestParameters()
    {
        Map<String, Object> configurationProperties = new HashMap<>();

        configurationProperties.put("analysisWindowDays", 30);
        configurationProperties.put("includeArchived", true);
        configurationProperties.put("logStoreDirectory", "logs/openlineage");
        configurationProperties.put("zoneNames", List.of("quarantine", "data-lake"));
        configurationProperties.put("unset", null);

        Map<String, String> requestParameters = BabbageAnalyticalEngineTargetProcessor.getRequestParameters(configurationProperties);

        assertEquals(requestParameters.get("analysisWindowDays"), "30");
        assertEquals(requestParameters.get("includeArchived"), "true");
        assertEquals(requestParameters.get("logStoreDirectory"), "logs/openlineage");

        /*
         * A list arrives in the form ConnectorBase.getArrayValue() reads back as a list.
         */
        assertEquals(requestParameters.get("zoneNames"), "[quarantine, data-lake]");

        assertFalse(requestParameters.containsKey("unset"), "A property with no value should be left out");
        assertEquals(requestParameters.size(), 4);
    }


    @Test
    public void testNoConfigurationPropertiesMeansNoRequestParameters()
    {
        assertNull(BabbageAnalyticalEngineTargetProcessor.getRequestParameters(null));
        assertTrue(BabbageAnalyticalEngineTargetProcessor.getRequestParameters(new HashMap<>()).isEmpty());
    }
}
