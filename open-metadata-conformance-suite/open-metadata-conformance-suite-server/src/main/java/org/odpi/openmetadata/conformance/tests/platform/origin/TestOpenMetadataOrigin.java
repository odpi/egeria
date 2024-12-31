/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.platform.origin;

import org.odpi.openmetadata.conformance.tests.platform.OpenMetadataPlatformTestCase;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.platform.PlatformConformanceWorkPad;
import org.springframework.web.client.RestTemplate;


/**
 * Retrieves the origin id from the server
 */
public class TestOpenMetadataOrigin extends OpenMetadataPlatformTestCase
{
    private static final  String testCaseId = "platform-origin";
    private static final  String testCaseName = "Platform origin test case";

    private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = "Origin descriptor retrieved from server platform.";


    /**
     * Default constructor sets up superclass
     *
     * @param workPad work space
     */
    public TestOpenMetadataOrigin(PlatformConformanceWorkPad workPad)
    {
        super(workPad, testCaseId, testCaseName);
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        final String urlTemplate = "/open-metadata/platform-services/users/" + workPad.getLocalServerUserId() +  "/server-platform/origin";

        RestTemplate restTemplate = new RestTemplate();

        String restResult = restTemplate.getForObject(platformConformanceWorkPad.getTutPlatformURLRoot() + urlTemplate, String.class);

        assertCondition((restResult != null),
                        assertion1,
                        assertionMsg1,
                        PlatformConformanceProfileRequirement.ORIGIN_IDENTIFIER.getProfileId(),
                        PlatformConformanceProfileRequirement.ORIGIN_IDENTIFIER.getRequirementId());

        super.addDiscoveredProperty("Platform origin id",
                                    restResult,
                                    PlatformConformanceProfileRequirement.ORIGIN_IDENTIFIER.getProfileId(),
                                    PlatformConformanceProfileRequirement.ORIGIN_IDENTIFIER.getRequirementId());

        super.setSuccessMessage("Platform origin descriptor successfully retrieved");
    }
}
