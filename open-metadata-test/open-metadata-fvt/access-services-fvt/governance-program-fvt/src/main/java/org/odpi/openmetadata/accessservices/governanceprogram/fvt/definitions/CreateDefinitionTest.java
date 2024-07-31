/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.fvt.definitions;

import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceDomainManager;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDomainElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDomainSetElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDomainProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDomainSetProperties;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.List;


/**
 * CreateDefinitionTest manages governance definitions.
 */
public class CreateDefinitionTest
{
    private final static String testCaseName       = "CreateDefinitionTest";
    private final static int    maxPageSize        = 100;

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
            CreateDefinitionTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        final String  governanceDomainSetName = "CocoPharmaceuticals";

        CreateDefinitionTest thisTest = new CreateDefinitionTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.GOVERNANCE_PROGRAM_OMAS.getAccessServiceWiki());

        GovernanceDomainManager domainManagerClient = thisTest.getDomainManagerClient(serverName, serverPlatformRootURL);

        String activityName = "newDomain";
        try
        {
            String newDomainGUID = thisTest.createGovernanceDomain(domainManagerClient, userId, governanceDomainSetName);

            thisTest.addStandardGovernanceDomains(domainManagerClient, userId, governanceDomainSetName);

        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create and return a governance domain manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private GovernanceDomainManager getDomainManagerClient(String   serverName,
                                                           String   serverPlatformRootURL) throws FVTUnexpectedCondition
    {
        final String activityName = "getDomainManagerClient";

        try
        {
            GovernanceProgramRESTClient restClient = new GovernanceProgramRESTClient(serverName, serverPlatformRootURL);

            return new GovernanceDomainManager(serverName, serverPlatformRootURL, restClient, maxPageSize);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Set up the standard domains and add them to Coco's domain set.
     *
     * @param domainManagerClient client to manage domains
     * @param userId userId for requests
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void addStandardGovernanceDomains(GovernanceDomainManager domainManagerClient,
                                              String                  userId,
                                              String                  domainSetName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException,
                                                                                            FVTUnexpectedCondition
    {
        final String activityName = "addStandardGovernanceDomains";
        final String standardGovernanceDomainSetName = "EgeriaStandardDomains";


        List<GovernanceDomainSetElement> domainSets = domainManagerClient.findGovernanceDomainSets(userId,
                                                                                                   ".*",
                                                                                                   0,
                                                                                                   0);

        if (domainSets == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No domain sets returned)");
        }
        if (domainSets.size() != 1)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Too many domain sets returned)");
        }


        GovernanceDomainSetElement cocoDomainSet = domainSets.get(0);
        if (cocoDomainSet == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No " + domainSetName + " domain set returned)");
        }
        if (! domainSetName.equals(cocoDomainSet.getProperties().getDisplayName()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad values in " + domainSetName + ": " + cocoDomainSet + ")");
        }

        String standardDomainSetGUID = domainManagerClient.createStandardGovernanceDomains(userId);

        GovernanceDomainSetElement standardDomainSet = domainManagerClient.getGovernanceDomainSetByGUID(userId, standardDomainSetGUID);

        if (standardDomainSet == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No new domain set)");
        }
        if (standardDomainSet.getElementHeader() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No new domain set header: " + standardDomainSet + ")");
        }
        if (standardDomainSet.getProperties() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No new domain set properties: " + standardDomainSet + ")");
        }
        if (! standardDomainSetGUID.equals(standardDomainSet.getElementHeader().getGUID()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad set GUID: " + standardDomainSet + ")");
        }
        if (standardDomainSet.getDomains() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No domains in set: " + standardDomainSet + ")");
        }
        if (standardDomainSet.getDomains().isEmpty())
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty domains in set: " + standardDomainSet + ")");
        }


    }


    /**
     * Set up a new domain for product assurance.
     *
     * @param domainManagerClient client to manage domains
     * @param userId userId for requests
     * @return unique identifier of the governance domain
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createGovernanceDomain(GovernanceDomainManager domainManagerClient,
                                          String                  userId,
                                          String                  domainSetName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException,
                                                                                        FVTUnexpectedCondition
    {
        final String activityName = "createGovernanceDomain";

        final String  domainSetDescription = "Governance Domains for Coco Pharmaceuticals";

        final int     governanceDomainIdentifier = 8;
        final String  governanceDomainName = "Product Assurance";
        final String  governanceDomainDescription = "Ensuring Coco Pharmaceutical products are developed in an ethical manner that complies with the regulations and provides efficacious treatments.";

        List<GovernanceDomainSetElement> domainSets = domainManagerClient.getGovernanceDomainSetsByName(userId, domainSetName, 0, 0);
        if (domainSets != null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Too many domain sets returned: " + domainSets +")");
        }

        GovernanceDomainSetProperties setProperties = new GovernanceDomainSetProperties();
        setProperties.setQualifiedName("GovernanceDomainSet:" + domainSetName);
        setProperties.setDisplayName(domainSetName);
        setProperties.setDescription(domainSetDescription);

        String domainSetGUID = domainManagerClient.createGovernanceDomainSet(userId, setProperties);

        GovernanceDomainProperties properties = new GovernanceDomainProperties();

        properties.setDomainIdentifier(governanceDomainIdentifier);
        properties.setQualifiedName("GovernanceDomain:" + governanceDomainIdentifier);
        properties.setDisplayName(governanceDomainName);
        properties.setDescription(governanceDomainDescription);

        String domainGUID = domainManagerClient.createGovernanceDomain(userId, domainSetGUID, properties);

        GovernanceDomainElement newDomain = domainManagerClient.getGovernanceDomainByGUID(userId, domainGUID);

        if (newDomain == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No new domain)");
        }
        if (newDomain.getElementHeader() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No new domain header: " + newDomain + ")");
        }
        if (newDomain.getProperties() == null)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(No new domain properties: " + newDomain + ")");
        }
        if (! domainGUID.equals(newDomain.getElementHeader().getGUID()))
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad GUID: " + newDomain + ")");
        }

        return domainGUID;
    }

}
