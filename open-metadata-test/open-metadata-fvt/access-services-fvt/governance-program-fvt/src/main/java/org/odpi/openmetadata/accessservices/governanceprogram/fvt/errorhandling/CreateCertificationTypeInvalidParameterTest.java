/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.CertificationManager;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationTypeProperties;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateCertificationTypeInvalidParameterTest tests the possibilities for invalid parameters on the createCertificationType method
 */
class CreateCertificationTypeInvalidParameterTest
{
    private final String testCaseName = "CreateCertificationTypeInvalidParameterTest";

    /**
     * Test combinations of invalid parameters passed to createCertificationType.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateCertificationTypeInvalidParameterTest(String              userId,
                                                CertificationManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationType";

        try
        {
            testCreateCertificationTypeNoUserId(client);
            testCreateCertificationTypeNoProperties(client, userId);
            testCreateCertificationTypeNoQualifiedName(client, userId);
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
    private void testCreateCertificationTypeNoUserId(CertificationManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationTypeNoUserId";

        CertificationTypeProperties properties = new CertificationTypeProperties();

        try
        {
            client.createCertificationType(null, properties, GovernanceDefinitionStatus.ACTIVE);
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
    private void testCreateCertificationTypeNoProperties(CertificationManager client,
                                                         String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationTypeNoProperties";

        try
        {
            client.createCertificationType(userId, null, GovernanceDefinitionStatus.ACTIVE);
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
    private void testCreateCertificationTypeNoQualifiedName(CertificationManager client,
                                                            String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateCertificationTypeNoQualifiedName";

        try
        {
            CertificationTypeProperties properties = new CertificationTypeProperties();

            client.createCertificationType(userId, properties, GovernanceDefinitionStatus.ACTIVE);
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
