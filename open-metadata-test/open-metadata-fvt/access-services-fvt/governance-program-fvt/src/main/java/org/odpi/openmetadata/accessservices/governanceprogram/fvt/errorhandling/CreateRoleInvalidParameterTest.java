/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceRoleManager;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceRoleProperties;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateRoleInvalidParameterTest tests the possibilities for invalid parameters on the createRole method
 */
class CreateRoleInvalidParameterTest
{
    private final String testCaseName = "CreateRoleInvalidParameterTest";

    /**
     * Test combinations of invalid parameters passed to createRole.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateRoleInvalidParameterTest(String                userId,
                                   GovernanceRoleManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateRole";

        try
        {
            testCreateRoleNoUserId(client);
            testCreateRoleNoProperties(client, userId);
            testCreateRoleNoQualifiedName(client, userId);
            testCreateRoleNoTitle(client, userId);
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
    private void testCreateRoleNoUserId(GovernanceRoleManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateRoleNoUserId";

        GovernanceRoleProperties properties = new GovernanceRoleProperties();

        try
        {
            client.createGovernanceRole(null, properties);
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
     * Test null properties passed to createGovernanceRole.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateRoleNoProperties(GovernanceRoleManager client,
                                            String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateRoleNoProperties";

        try
        {
            client.createGovernanceRole(userId, null);
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
     * Test null properties passed to createGovernanceRole.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateRoleNoQualifiedName(GovernanceRoleManager client,
                                               String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateRoleNoQualifiedName";

        try
        {
            GovernanceRoleProperties properties = new GovernanceRoleProperties();

            properties.setDisplayName("TestTitle");

            client.createGovernanceRole(userId, properties);
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
     * Test null properties passed to createGovernanceRole.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateRoleNoTitle(GovernanceRoleManager client,
                                               String                userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateRoleNoQualifiedName";

        try
        {
            GovernanceRoleProperties properties = new GovernanceRoleProperties();

            properties.setIdentifier("TestQualifiedName");


            client.createGovernanceRole(userId, properties);
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
