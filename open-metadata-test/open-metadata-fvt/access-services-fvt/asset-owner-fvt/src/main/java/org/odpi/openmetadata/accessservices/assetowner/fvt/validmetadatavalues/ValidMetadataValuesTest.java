/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.fvt.validmetadatavalues;

import org.odpi.openmetadata.accessservices.assetowner.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ValidMetadataValueDetail;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;


/**
 * ValidMetadataValuesTest sets up a list of valid values for the projectType property of ProjectCharter.
 */
public class ValidMetadataValuesTest
{
    private final static String testCaseName       = "ValidMetadataValuesTest";

    /**
     * Run all the defined tests and capture the results.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @return  results of running test
     */
    public static FVTResults performFVT(String   serverName,
                                        String   serverPlatformRootURL,
                                        String   userId)
    {
        FVTResults results = new FVTResults(testCaseName);

        results.incrementNumberOfTests();
        try
        {
            ValidMetadataValuesTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        return results;
    }


    /**
     * Run the tests in this class.
     *
     * @param serverPlatformRootURL root url of the server
     * @param serverName name of the server
     * @param userId calling user
     * @param auditLogDestination logging destination
     * @throws FVTUnexpectedCondition the test case failed
     */
    private static void runIt(String                 serverPlatformRootURL,
                              String                 serverName,
                              String                 userId,
                              FVTAuditLogDestination auditLogDestination) throws FVTUnexpectedCondition
    {
        ValidMetadataValuesTest thisTest = new ValidMetadataValuesTest();

        thisTest.testValidMetadataValues(serverName, serverPlatformRootURL, userId);
    }



    /**
     * Create a client using each of its constructors.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void testValidMetadataValues(String   serverName,
                                         String   serverPlatformRootURL,
                                         String   userId) throws FVTUnexpectedCondition
    {
        String activityName = "testValidMetadataValues";

        try
        {
            OpenMetadataStoreClient client     = new OpenMetadataStoreClient(serverName, serverPlatformRootURL, 100);

            activityName = "Add Valid Metadata Values";
            ValidMetadataValue validMetadataValue = new ValidMetadataValue();

            validMetadataValue.setDisplayName("Incident Investigation");
            validMetadataValue.setPreferredValue("incident-investigation");
            validMetadataValue.setDescription("An investigation into the causes, effects and remedies for a detected incident.");

            client.setUpValidMetadataValue(userId, "ProjectCharter", "projectType", validMetadataValue);

            validMetadataValue.setDisplayName("Clinical Trial");
            validMetadataValue.setPreferredValue("clinical-trial");
            validMetadataValue.setDescription("A controlled validation of the efficacy of a particular treatment with selected patients.");

            client.setUpValidMetadataValue(userId, "ProjectCharter", "projectType", validMetadataValue);

            activityName = "Get Valid Metadata Value";
            validMetadataValue = client.getValidMetadataValue(userId, "ProjectCharter", "projectType", "clinical-trial");

            if (validMetadataValue == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " returned null");
            }

            if (! "Clinical Trial".equals(validMetadataValue.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " has bad display name of " + validMetadataValue.getDisplayName());
            }

            if (! "A controlled validation of the efficacy of a particular treatment with selected patients.".equals(validMetadataValue.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " has bad description of " + validMetadataValue.getDescription());
            }

            activityName = "Get Valid Metadata Value Set";
            List<ValidMetadataValueDetail> validMetadataValues = client.getValidMetadataValues(userId, "ProjectCharter", "projectType", 0, 0);

            if (validMetadataValues == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " returned null");
            }

            if (validMetadataValues.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " has size of " + validMetadataValues.size());
            }

            System.out.println("Valid values for property projectType in entity ProjectCharter");
            for (ValidMetadataValue retrievedValue : validMetadataValues)
            {
                System.out.println(" ==> " + retrievedValue.getPreferredValue() + " means " + retrievedValue.getDisplayName() + ": " + retrievedValue.getDescription());
            }

            activityName = "Validate Metadata Value";

            if (! client.validateMetadataValue(userId, "ProjectCharter", "projectType", "incident-investigation"))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + " of incident-investigation returns false");
            }
        }
        catch (FVTUnexpectedCondition testCaseError)
        {
            throw testCaseError;
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


}
