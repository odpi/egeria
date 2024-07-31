/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.ExternalReferenceManager;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateExternalReferenceInvalidParameterTest tests the possibilities for invalid parameters on the createExternalReference method
 */
class CreateExternalReferenceInvalidParameterTest
{
    private final String testCaseName   = "CreateExternalReferenceInvalidParameterTest";
    private final String testAnchorGUID = "TestAnchorGUID";

    /**
     * Test combinations of invalid parameters passed to createExternalReference.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateExternalReferenceInvalidParameterTest(String                   userId,
                                                ExternalReferenceManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalReference";

        try
        {
            testCreateExternalReferenceNoUserId(client);
            testCreateExternalReferenceNoProperties(client, userId);
            testCreateExternalReferenceNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createExternalReference.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalReferenceNoUserId(ExternalReferenceManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalReferenceNoUserId";

        ExternalReferenceProperties properties = new ExternalReferenceProperties();

        try
        {
            client.createExternalReference(null, testAnchorGUID, properties);
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
     * Test null properties passed to createExternalReference.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalReferenceNoProperties(ExternalReferenceManager client,
                                                         String                   userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalReferenceNoProperties";

        try
        {
            client.createExternalReference(userId, testAnchorGUID,null);
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
     * Test null properties passed to createExternalReference.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateExternalReferenceNoQualifiedName(ExternalReferenceManager client,
                                                            String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateExternalReferenceNoQualifiedName";

        try
        {
            ExternalReferenceProperties properties = new ExternalReferenceProperties();

            client.createExternalReference(userId, testAnchorGUID, properties);
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
