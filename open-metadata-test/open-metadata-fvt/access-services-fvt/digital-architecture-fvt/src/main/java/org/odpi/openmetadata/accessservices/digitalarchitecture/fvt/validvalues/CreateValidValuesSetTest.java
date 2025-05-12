/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.validvalues;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ReferenceDataManager;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ValidValueElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.ValidValueProperties;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CreateValidValuesSetTest calls the ReferenceDataManager to create a valid values set with nested definitions
 * and then retrieve the results.
 */
public class CreateValidValuesSetTest
{
    private final static String testCaseName = "CreateValidValuesSetTest";

    private final static int    maxPageSize  = 100;

    /*
     * The values below describe the valid value set that is being built.
     */
    private final static String validValuesSetName         = "TestValidValuesSet";
    private final static String validValuesSetDisplayName  = "TestValidValuesSet displayName";
    private final static String validValuesSetDescription  = "TestValidValuesSet description";
    private final static String validValuesSetUsage        = "TestValidValuesSet usage";
    private final static String validValuesSetScope        = "TestValidValuesSet scope";

    /*
     * These are the two valid value definitions that are part of the set.
     */
    private final static String  validValue1Name                    = "TestValidValue1";
    private final static String  validValue1DisplayName             = "TestValidValue1 displayName";
    private final static String  validValue1Description             = "TestValidValue1 description";
    private final static String  validValue1Usage                   = "TestValidValue1 usage";
    private final static String  validValue1Scope                   = "TestValidValue1 scope";
    private final static String  validValue1PreferredValue          = "TestValidValue1 preferredValue";
    private final static String  validValue1AdditionalPropertyName  = "TestValidValue1 additionalPropertyName";
    private final static String  validValue1AdditionalPropertyValue = "TestValidValue1 additionalPropertyValue";
    private final static String  validValue2Name                    = "TestValidValue2";
    private final static String  validValue2DisplayName             = "TestValidValue2 displayName";
    private final static String  validValue2Description             = "TestValidValue2 description";
    private final static String  validValue2Usage                   = "TestValidValue2 usage";
    private final static String  validValue2Scope                   = "TestValidValue2 scope";
    private final static String  validValue2PreferredValue          = "TestValidValue2 preferredValue";
    private final static String  validValue2NameUpdate              = "TestValidValue2 - updated";

    private final static String  searchString          = ".*Test.*";


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
            CreateValidValuesSetTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
            results.incrementNumberOfSuccesses();
        }
        catch (Exception error)
        {
            results.addCapturedError(error);
        }

        return results;
    }


    /**
     * Run all the tests in this class.
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
        CreateValidValuesSetTest thisTest = new CreateValidValuesSetTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceWiki());

        ReferenceDataManager client         = thisTest.getValidValuesManagerClient(serverName, serverPlatformRootURL, auditLog);
        String               validValuesSet = thisTest.createValidValueSet(client, userId);

        thisTest.createValidValueDefinitions(client, validValuesSet, userId);

        thisTest.deleteValidValuesSet(client, validValuesSet, userId);
    }


    /**
     * Create and return a valid values' manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private ReferenceDataManager getValidValuesManagerClient(String   serverName,
                                                             String   serverPlatformRootURL,
                                                             AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getValidValuesManagerClient";

        try
        {
            DigitalArchitectureRESTClient restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL);

            return new ReferenceDataManager(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a valid value set and return its GUID.
     *
     * @param client interface to Digital Architecture OMAS
     * @param userId calling user
     * @return guid of set
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createValidValueSet(ReferenceDataManager client,
                                       String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createValidValueSet";

        try
        {
            String validValuesSetGUID = client.createValidValueSet(userId,
                                                                   validValuesSetName,
                                                                   validValuesSetDisplayName,
                                                                   validValuesSetDescription,
                                                                   validValuesSetUsage,
                                                                   validValuesSetScope,
                                                                   null,
                                                                   null,
                                                                   null);

            if (validValuesSetGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create)");
            }

            ValidValueElement    retrievedElement = client.getValidValueByGUID(userId, validValuesSetGUID);
            ValidValueProperties retrievedSet     = retrievedElement.getValidValueProperties();

            if (retrievedSet == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values Set from Retrieve)");
            }

            if (! validValuesSetName.equals(retrievedSet.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve)");
            }
            if (! validValuesSetDisplayName.equals(retrievedSet.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve)");
            }
            if (! validValuesSetDescription.equals(retrievedSet.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve)");
            }
            if (! validValuesSetUsage.equals(retrievedSet.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from Retrieve)");
            }
            if (! validValuesSetScope.equals(retrievedSet.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from Retrieve)");
            }
            if (retrievedSet.getPreferredValue() != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from Retrieve)");
            }

            List<ValidValueElement> validValueList = client.getValidValueByName(userId, validValuesSetName, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveByName)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveByName)");
            }
            else if (validValueList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveByName contains" + validValueList.size() +
                                                 " elements)");
            }

            retrievedElement = validValueList.get(0);
            retrievedSet = retrievedElement.getValidValueProperties();

            if (! validValuesSetName.equals(retrievedSet.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName)");
            }
            if (! validValuesSetDisplayName.equals(retrievedSet.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName)");
            }
            if (! validValuesSetDescription.equals(retrievedSet.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName)");
            }
            if (! validValuesSetUsage.equals(retrievedSet.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveByName)");
            }
            if (! validValuesSetScope.equals(retrievedSet.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveByName)");
            }
            if (retrievedSet.getPreferredValue() != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveByName)");
            }

            return validValuesSetGUID;
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


    /**
     * Two valid value definitions for the set using a variety of mechanisms.
     *
     * @param client interface to Digital Architecture OMAS
     * @param validValueSetGUID unique identifier of the valid value set
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   createValidValueDefinitions(ReferenceDataManager client,
                                               String             validValueSetGUID,
                                               String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createValidValueDefinitions";

        try
        {
            Map<String, String>  additionalProperties = new HashMap<>();
            additionalProperties.put(validValue1AdditionalPropertyName, validValue1AdditionalPropertyValue);

            String validValue1GUID = client.createValidValueDefinition(userId,
                                                                       validValueSetGUID,
                                                                       true,
                                                                       validValue1Name,
                                                                       validValue1DisplayName,
                                                                       validValue1Description,
                                                                       validValue1Usage,
                                                                       validValue1Scope,
                                                                       validValue1PreferredValue,
                                                                       null,
                                                                       additionalProperties,
                                                                       null,
                                                                       null);

            if (validValue1GUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create of definition 1)");
            }

            ValidValueElement    retrievedElement  = client.getValidValueByGUID(userId, validValue1GUID);
            ValidValueProperties retrievedDefinition = retrievedElement.getValidValueProperties();

            if (retrievedDefinition == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 1 from Retrieve)");
            }

            if (! validValue1Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve of 1)");
            }
            if (! validValue1DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve of 1)");
            }
            if (! validValue1Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve of 1)");
            }
            if (! validValue1Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from Retrieve of 1)");
            }
            if (! validValue1Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from Retrieve of 1)");
            }
            if (! validValue1PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from Retrieve of 1)");
            }
            if (retrievedDefinition.getAdditionalProperties() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null additionalProperties from Retrieve of 1)");
            }
            else if (! validValue1AdditionalPropertyValue.equals(retrievedDefinition.getAdditionalProperties().get(validValue1AdditionalPropertyName)))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad additionalProperties from Retrieve of 1)");
            }

            List<ValidValueElement> validValueList = client.getValidValueByName(userId, validValue1Name, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveByName of 1)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveByName of 1)");
            }
            else if (validValueList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveByName of 1 contains" + validValueList.size() +
                                                         " elements)");
            }

            retrievedElement = validValueList.get(0);
            retrievedDefinition = retrievedElement.getValidValueProperties();

            if (retrievedDefinition == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 1 from Retrieve)");
            }

            if (! validValue1Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName of 1)");
            }
            if (! validValue1DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName of 1)");
            }
            if (! validValue1Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName of 1)");
            }
            if (! validValue1Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveByName of 1)");
            }
            if (! validValue1Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveByName of 1)");
            }
            if (! validValue1PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveByName of 1)");
            }

            validValueList = client.findValidValues(userId, searchString, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for findValidValues of 1)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for findValidValues of 1)");
            }
            else if (validValueList.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for findValidValues of 1 contains" + validValueList.size() +
                                                         " elements)");
            }

            validValueList = client.getValidValueSetMembers(userId, validValueSetGUID, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveSetMembers of 1)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveSetMembers of 1)");
            }
            else if (validValueList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveSetMembers of 1 contains" + validValueList.size() +
                                                         " elements)");
            }
            else
            {
                retrievedElement = validValueList.get(0);
                retrievedDefinition = retrievedElement.getValidValueProperties();
            }

            if (retrievedDefinition == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 1 from RetrieveOfMembers)");
            }

            if (! validValue1Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveOfMembers of 1)");
            }
            if (! validValue1DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveOfMembers of 1)");
            }
            if (! validValue1Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveOfMembers of 1)");
            }
            if (! validValue1Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveOfMembers of 1)");
            }
            if (! validValue1Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveOfMembers of 1)");
            }
            if (! validValue1PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveOfMembers of 1)");
            }

            /*
             * Check that not possible to create an element with the same qualified name.
             */
            try
            {
                client.createValidValueDefinition(userId,
                                                  validValueSetGUID,
                                                  false,
                                                  validValue1Name,
                                                  validValue1DisplayName,
                                                  validValue1Description,
                                                  validValue1Usage,
                                                  validValue1Scope,
                                                  validValue1PreferredValue,
                                                  null,
                                                  additionalProperties,
                                                  null,
                                                  null);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(duplicate create of valid value definition allowed)");
            }
            catch (InvalidParameterException okResult)
            {
                // nothing to do
            }

            /*
             * Now do the second value
             */
            String validValue2GUID = client.createValidValueDefinition(userId,
                                                                       validValueSetGUID,
                                                                       false,
                                                                       validValue2Name,
                                                                       validValue2DisplayName,
                                                                       validValue2Description,
                                                                       validValue2Usage,
                                                                       validValue2Scope,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null);

            if (validValue2GUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create of definition 2)");
            }

            retrievedElement  = client.getValidValueByGUID(userId, validValue2GUID);
            retrievedDefinition = retrievedElement.getValidValueProperties();

            if (retrievedDefinition == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 2 from Retrieve)");
            }

            if (! validValue2Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve of 2)");
            }
            if (! validValue2DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve of 2)");
            }
            if (! validValue2Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve of 2)");
            }
            if (! validValue2Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from Retrieve of 2)");
            }
            if (! validValue2Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from Retrieve of 2)");
            }
            if (retrievedDefinition.getPreferredValue() != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from Retrieve of 2)");
            }
            if (retrievedDefinition.getIsDeprecated())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad isDeprecated from Retrieve of 2)");
            }

            validValueList = client.getValidValueByName(userId, validValue2Name, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveByName of 2)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveByName of 2)");
            }
            else if (validValueList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveByName of 2 contains" + validValueList.size() +
                                                         " elements)");
            }

            retrievedElement = validValueList.get(0);
            retrievedDefinition = retrievedElement.getValidValueProperties();

            if (! validValue2Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName of 2)");
            }
            if (! validValue2DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName of 2)");
            }
            if (! validValue2Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName of 2)");
            }
            if (! validValue2Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveByName of 2)");
            }
            if (! validValue2Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveByName of 2)");
            }
            if (retrievedDefinition.getPreferredValue() != null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveByName of 2)");
            }
            if (retrievedDefinition.getIsDeprecated())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad isDeprecated from RetrieveByName of 2)");
            }

            validValueList = client.findValidValues(userId, searchString, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for findValidValues of all)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for findValidValues of all)");
            }
            else if (validValueList.size() != 3)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for findValidValues of all contains" + validValueList.size() +
                                                         " elements)");
            }

            validValueList = client.getValidValueSetMembers(userId, validValueSetGUID, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveSetMembers of both)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveSetMembers of both)");
            }
            else if (validValueList.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveSetMembers of both contains" + validValueList.size() +
                                                         " elements)");
            }

            /*
             * Add the preferred value
             */
            client.updateValidValue(userId,
                                    validValue2GUID,
                                    validValue2Name,
                                    validValue2DisplayName,
                                    validValue2Description,
                                    validValue2Usage,
                                    validValue2Scope,
                                    validValue2PreferredValue,
                                    null,
                                    true,
                                    null,
                                    false,
                                    null,
                                    null);

            retrievedElement  = client.getValidValueByGUID(userId, validValue2GUID);
            retrievedDefinition = retrievedElement.getValidValueProperties();

            if (! validValue2Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveUpdate of 2)");
            }
            if (! validValue2DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveUpdate of 2)");
            }
            if (! validValue2Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveUpdate of 2)");
            }
            if (! validValue2Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveUpdate of 2)");
            }
            if (! validValue2Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveUpdate of 2)");
            }
            if (! validValue2PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveUpdate of 2)");
            }
            if (! retrievedDefinition.getIsDeprecated())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad isDeprecated from RetrieveUpdate of 2)");
            }


            /*
             * Update valid value 2 with valid value 1's qualified name - this should fail
             */
            try
            {
                client.updateValidValue(userId,
                                        validValue2GUID,
                                        validValue1Name,
                                        validValue2DisplayName,
                                        validValue2Description,
                                        validValue2Usage,
                                        validValue2Scope,
                                        validValue2PreferredValue,
                                        null,
                                        true,
                                        null,
                                        true,
                                        null,
                                        null);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Duplicate update allowed)");
            }
            catch (InvalidParameterException expectedResult)
            {
                // all ok
            }

            /*
             * Valid value 2 should be exactly as it was
             */
            retrievedElement  = client.getValidValueByGUID(userId, validValue2GUID);
            retrievedDefinition = retrievedElement.getValidValueProperties();

            if (! validValue2Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! validValue2DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! validValue2Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! validValue2Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! validValue2Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! validValue2PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveUpdate of 2)");
            }
            if (! retrievedDefinition.getIsDeprecated())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad isDeprecated from RetrieveUpdate of 2)");
            }


            /*
             * Update valid value 2 with a new qualified name - this should work ok
             */

            client.updateValidValue(userId,
                                        validValue2GUID,
                                        validValue2NameUpdate,
                                        validValue2DisplayName,
                                        validValue2Description,
                                        validValue2Usage,
                                        validValue2Scope,
                                        validValue2PreferredValue,
                                        null,
                                        true,
                                        null,
                                        true,
                                        null,
                                        null);

            /*
             * Valid value 2 should be updated
             */
            retrievedElement  = client.getValidValueByGUID(userId, validValue2GUID);
            retrievedDefinition = retrievedElement.getValidValueProperties();

            if (! validValue2NameUpdate.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! validValue2DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! validValue2Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! validValue2Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! validValue2Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! validValue2PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! retrievedDefinition.getIsDeprecated())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad isDeprecated from RetrieveUpdate of 2 after qualified name change)");
            }


            /*
             * By detaching definition 2, then should only get value 1 back from the set membership request.
             */
            client.detachValidValueFromSet(userId, validValueSetGUID, validValue2GUID);

            validValueList = client.getValidValueSetMembers(userId, validValueSetGUID, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveSetMembers of 1 after detach)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveSetMembers of 1 after detach)");
            }
            else if (validValueList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveSetMembers after detach of 1 contains" + validValueList.size() +
                                                         " elements)");
            }
            else
            {
                retrievedElement = validValueList.get(0);
                retrievedDefinition = retrievedElement.getValidValueProperties();
            }

            if (retrievedDefinition == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 1 from RetrieveOfMembers of 1 after detach)");
            }

            if (! validValue1Name.equals(retrievedDefinition.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveOfMembers of 1 after detach)");
            }
            if (! validValue1DisplayName.equals(retrievedDefinition.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveOfMembers of 1 after detach)");
            }
            if (! validValue1Description.equals(retrievedDefinition.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveOfMembers of 1 after detach)");
            }
            if (! validValue1Usage.equals(retrievedDefinition.getUsage()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad usage from RetrieveOfMembers of 1 after detach)");
            }
            if (! validValue1Scope.equals(retrievedDefinition.getScope()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad scope from RetrieveOfMembers of 1 after detach)");
            }
            if (! validValue1PreferredValue.equals(retrievedDefinition.getPreferredValue()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad preferredValue from RetrieveOfMembers of 1 after detach)");
            }

            /*
             * Now reattach value 2 and check it reappears in the set.
             */
            client.attachValidValueToSet(userId, validValueSetGUID, validValue2GUID, true);

            validValueList = client.getValidValueSetMembers(userId, validValueSetGUID, 0, maxPageSize);

            if (validValueList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no valid value for RetrieveSetMembers of both after reattach)");
            }
            else if (validValueList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty valid value list for RetrieveSetMembers of of both after reattach)");
            }
            else if (validValueList.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Valid value list for RetrieveSetMembers of both after reattach contains" + validValueList.size() +
                                                         " elements)");
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


    /**
     * Delete the set and check that the valid value definitions connected to it are also gone.
     *
     * @param client interface to Digital Architecture OMAS
     * @param validValueSetGUID unique identifier of the valid value set
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void   deleteValidValuesSet(ReferenceDataManager client,
                                        String             validValueSetGUID,
                                        String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "deleteValidValuesSet";

        try
        {
            client.deleteValidValue(userId, validValueSetGUID, validValuesSetName);

            /*
             * Check it has really gone.
             */
            try
            {
                client.getValidValueByGUID(userId, validValueSetGUID);
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Deleted value value set returned)");
            }
            catch (InvalidParameterException expectedException)
            {
                // All ok
            }

            /*
             * Check for no cascaded delete
             */
            List<ValidValueElement> searchResults = client.findValidValues(userId, searchString, 0, maxPageSize);

            if (searchResults == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(cascaded delete occurred)");
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
