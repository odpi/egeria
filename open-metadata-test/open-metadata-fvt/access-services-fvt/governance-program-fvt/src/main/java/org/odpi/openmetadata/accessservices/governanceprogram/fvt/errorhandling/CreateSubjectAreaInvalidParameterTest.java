/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.SubjectAreaManager;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateSubjectAreaInvalidParameterTest tests the possibilities for invalid parameters on the createSubjectArea method
 */
class CreateSubjectAreaInvalidParameterTest
{
    private final String testCaseName = "CreateSubjectAreaInvalidParameterTest";

    /**
     * Test combinations of invalid parameters passed to createSubjectArea.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateSubjectAreaInvalidParameterTest(String             userId,
                                          SubjectAreaManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateSubjectArea";

        try
        {
            testCreateSubjectAreaNoUserId(client);
            testCreateSubjectAreaNoProperties(client, userId);
            testCreateSubjectAreaNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createSubjectArea.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateSubjectAreaNoUserId(SubjectAreaManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateSubjectAreaNoUserId";

        SubjectAreaProperties properties = new SubjectAreaProperties();

        try
        {
            client.createSubjectArea(null, properties);
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
     * Test null properties passed to createSubjectArea.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateSubjectAreaNoProperties(SubjectAreaManager client,
                                                   String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateSubjectAreaNoProperties";

        try
        {
            client.createSubjectArea(userId, null);
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
     * Test null properties passed to createSubjectArea.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateSubjectAreaNoQualifiedName(SubjectAreaManager client,
                                                      String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateSubjectAreaNoQualifiedName";

        try
        {
            SubjectAreaProperties properties = new SubjectAreaProperties();

            client.createSubjectArea(userId, properties);
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
