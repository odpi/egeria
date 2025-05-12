/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceZoneManager;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceZoneProperties;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateZoneInvalidParameterTest tests the possibilities for invalid parameters on the createGovernanceZone method
 */
class CreateZoneInvalidParameterTest
{
    private final String testCaseName = "CreateZoneInvalidParameterTest";
    
    /**
     * Test combinations of invalid parameters passed to createZone.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateZoneInvalidParameterTest(String                userId,
                                   GovernanceZoneManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateZone";

        try
        {
            testCreateZoneNoUserId(client);
            testCreateZoneNoProperties(client, userId);
            testCreateZoneNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createGovernanceZone.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateZoneNoUserId(GovernanceZoneManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateZoneNoUserId";

        GovernanceZoneProperties properties = new GovernanceZoneProperties();

        try
        {
            client.createGovernanceZone(null, properties);
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
     * Test null properties passed to createGovernanceZone.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateZoneNoProperties(GovernanceZoneManager client,
                                            String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateZoneNoProperties";

        try
        {
            client.createGovernanceZone(userId, null);
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
     * Test null properties passed to createGovernanceZone.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateZoneNoQualifiedName(GovernanceZoneManager client,
                                               String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateZoneNoQualifiedName";

        try
        {
            GovernanceZoneProperties properties = new GovernanceZoneProperties();

            client.createGovernanceZone(userId, properties);
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
