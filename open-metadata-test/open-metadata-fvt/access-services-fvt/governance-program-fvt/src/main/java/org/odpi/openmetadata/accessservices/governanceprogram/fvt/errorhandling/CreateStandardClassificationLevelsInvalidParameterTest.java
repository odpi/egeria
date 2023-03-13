/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceClassificationLevelManager;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateCertificationTypeInvalidParameterTest tests the possibilities for invalid parameters on the createCertificationType method
 */
class CreateStandardClassificationLevelsInvalidParameterTest
{
    private final String testCaseName = "CreateCertificationTypeInvalidParameterTest";

    /**
     * Test combinations of invalid parameters passed to createCertificationType.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateStandardClassificationLevelsInvalidParameterTest(String                               userId,
                                                           GovernanceClassificationLevelManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationType";

        try
        {
            testCreateCertificationTypeNoUserId(client);
            testCreateCertificationTypeNoClassificationName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createCertificationType.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateCertificationTypeNoUserId(GovernanceClassificationLevelManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationTypeNoUserId";
        final String classificationName = "testClassificationName";

        try
        {
            client.createStandardGovernanceClassificationLevels(null, classificationName);
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


    /**
     * Test null properties passed to createCertificationType.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateCertificationTypeNoClassificationName(GovernanceClassificationLevelManager client,
                                                                 String                               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationTypeNoClassificationName";

        try
        {
            client.createStandardGovernanceClassificationLevels(userId, null);
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
