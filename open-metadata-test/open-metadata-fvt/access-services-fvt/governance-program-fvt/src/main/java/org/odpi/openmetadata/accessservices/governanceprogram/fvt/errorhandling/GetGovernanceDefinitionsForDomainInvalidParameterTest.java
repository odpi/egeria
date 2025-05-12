/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceProgramReviewManager;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateRoleInvalidParameterTest tests the possibilities for invalid parameters on the createRole method
 */
class GetGovernanceDefinitionsForDomainInvalidParameterTest
{
    private final String testCaseName = "CreateRoleInvalidParameterTest";
    private final String typeName     = "TestTypeName";

    /**
     * Test combinations of invalid parameters passed to createRole.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    GetGovernanceDefinitionsForDomainInvalidParameterTest(String                         userId,
                                                          GovernanceProgramReviewManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateRole";

        try
        {
            testGetDefinitionForDomainNoUserId(client);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createGovernanceRole.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testGetDefinitionForDomainNoUserId(GovernanceProgramReviewManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testGetDefinitionForDomainNoUserId";

        try
        {
            client.getGovernanceDefinitionsForDomain(null, null, 0, 0, 0);
            throw new FVTUnexpectedCondition(testCaseName, activityName);
        }
        catch (InvalidParameterException expectedException)
        {
            // ignore
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


}
