/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceMetricsManager;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceMetricProperties;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateGovernanceMetricInvalidParameterTest tests the possibilities for invalid parameters on the createGovernanceMetric method
 */
class CreateGovernanceMetricInvalidParameterTest
{
    private final String testCaseName   = "CreateGovernanceMetricInvalidParameterTest";

    /**
     * Test combinations of invalid parameters passed to createGovernanceMetric.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateGovernanceMetricInvalidParameterTest(String                   userId,
                                               GovernanceMetricsManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceMetric";

        try
        {
            testCreateGovernanceMetricNoUserId(client);
            testCreateGovernanceMetricNoProperties(client, userId);
            testCreateGovernanceMetricNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createGovernanceMetric.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGovernanceMetricNoUserId(GovernanceMetricsManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceMetricNoUserId";

        GovernanceMetricProperties properties = new GovernanceMetricProperties();

        try
        {
            client.createGovernanceMetric(null, properties);
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
     * Test null properties passed to createGovernanceMetric.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGovernanceMetricNoProperties(GovernanceMetricsManager client,
                                                        String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceMetricNoProperties";

        try
        {
            client.createGovernanceMetric(userId,null);
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
     * Test null properties passed to createGovernanceMetric.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGovernanceMetricNoQualifiedName(GovernanceMetricsManager client,
                                                           String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceMetricNoQualifiedName";

        try
        {
            GovernanceMetricProperties properties = new GovernanceMetricProperties();

            client.createGovernanceMetric(userId, properties);
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
