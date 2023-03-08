/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceDomainManager;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateGovernanceDomainInvalidParameterTest tests the possibilities for invalid parameters on the createGovernanceDomain method
 */
class CreateGovernanceDomainInvalidParameterTest
{
    private final String testCaseName = "CreateGovernanceDomainInvalidParameterTest";
    private final String setGUID      = "TestSetGUID";

    /**
     * Test combinations of invalid parameters passed to createGovernanceDomain.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateGovernanceDomainInvalidParameterTest(String                      userId,
                                               GovernanceDomainManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceDomain";

        try
        {
            testCreateGovernanceDomainNoUserId(client);
            testCreateGovernanceDomainNoProperties(client, userId);
            testCreateGovernanceDomainNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createGovernanceDomain.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGovernanceDomainNoUserId(GovernanceDomainManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceDomainNoUserId";

        GovernanceDomainProperties properties = new GovernanceDomainProperties();

        try
        {
            client.createGovernanceDomain(null, setGUID ,properties);
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
     * Test null properties passed to createGovernanceDomain.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGovernanceDomainNoProperties(GovernanceDomainManager client,
                                                        String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceDomainNoProperties";

        try
        {
            client.createGovernanceDomain(userId, setGUID,null);
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
     * Test null properties passed to createGovernanceDomain.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateGovernanceDomainNoQualifiedName(GovernanceDomainManager client,
                                                           String                  userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateGovernanceDomainNoQualifiedName";

        try
        {
            GovernanceDomainProperties properties = new GovernanceDomainProperties();

            client.createGovernanceDomain(userId, setGUID, properties);
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
