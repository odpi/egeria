/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.origin;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Retrieves the origin id from the server
 */
public class TestOpenMetadataOrigin extends OpenMetadataOriginTestCase
{
    private static final  String testUserId = "ComplianceTestUser";

    private static final  String testCaseId = "repository-origin";
    private static final  String testCaseName = "Repository origin test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = "Origin descriptor retrieved from repository.";


    /**
     * Default constructor sets up superclass
     *
     * @param workbenchId name of the calling workbench
     */
    public TestOpenMetadataOrigin(String   workbenchId)
    {
        super(workbenchId, testCaseId, testCaseName);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        final String urlTemplate = "/servers/" + super.getServerName() + "/open-metadata/admin-services/users/" + testUserId + "/server-origin";

        RestTemplate restTemplate = new RestTemplate();

        String restResult = restTemplate.getForObject(super.getServerURLRoot() + urlTemplate, String.class);

        assertCondition((restResult != null), assertion1, assertionMsg1);

        super.result.setSuccessMessage("Repository origin descriptor successfully retrieved");

        Map<String, Object>  discoveredProperties = new HashMap<>();
        discoveredProperties.put("Repository origin id", restResult);

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
