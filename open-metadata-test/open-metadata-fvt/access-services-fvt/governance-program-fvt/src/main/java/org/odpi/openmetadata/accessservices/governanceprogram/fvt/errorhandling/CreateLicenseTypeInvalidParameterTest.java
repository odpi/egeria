/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.fvt.errorhandling;

import org.odpi.openmetadata.accessservices.governanceprogram.client.RightsManager;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LicenseTypeProperties;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

/**
 * CreateLicenseTypeInvalidParameterTest tests the possibilities for invalid parameters on the createLicenseType method
 */
class CreateLicenseTypeInvalidParameterTest
{
    private final String testCaseName = "CreateLicenseTypeInvalidParameterTest";

    /**
     * Test combinations of invalid parameters passed to createLicenseType.
     *
     * @param userId calling user
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    CreateLicenseTypeInvalidParameterTest(String              userId,
                                          RightsManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateLicenseType";

        try
        {
            testCreateLicenseTypeNoUserId(client);
            testCreateLicenseTypeNoProperties(client, userId);
            testCreateLicenseTypeNoQualifiedName(client, userId);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Test null userId passed to createLicenseType.
     *
     * @param client client to call
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateLicenseTypeNoUserId(RightsManager client) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateLicenseTypeNoUserId";

        LicenseTypeProperties properties = new LicenseTypeProperties();

        try
        {
            client.createLicenseType(null, properties, GovernanceDefinitionStatus.ACTIVE);
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
     * Test null properties passed to createLicenseType.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateLicenseTypeNoProperties(RightsManager client,
                                                         String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateLicenseTypeNoProperties";

        try
        {
            client.createLicenseType(userId, null, GovernanceDefinitionStatus.ACTIVE);
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
     * Test null properties passed to createLicenseType.
     *
     * @param client client to call
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testCreateLicenseTypeNoQualifiedName(RightsManager client,
                                                            String               userId) throws FVTUnexpectedCondition
    {
        final String activityName = "testCreateLicenseTypeNoQualifiedName";

        try
        {
            LicenseTypeProperties properties = new LicenseTypeProperties();

            client.createLicenseType(userId, properties, GovernanceDefinitionStatus.ACTIVE);
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
