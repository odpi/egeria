/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalarchitecture.fvt.locations;


import org.odpi.openmetadata.accessservices.digitalarchitecture.client.LocationManager;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.rest.DigitalArchitectureRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.LocationElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.locations.LocationProperties;
import org.odpi.openmetadata.fvt.utilities.FVTResults;
import org.odpi.openmetadata.fvt.utilities.auditlog.FVTAuditLogDestination;
import org.odpi.openmetadata.fvt.utilities.exceptions.FVTUnexpectedCondition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CreateLocationsTest calls the LocationManagement to create a site location with nested facilities
 * and then retrieve the results.
 */
public class CreateLocationsTest
{
    private final static String testCaseName = "CreateLocationTest";

    private final static int    maxPageSize  = 100;

    /*
     * The values below describe the location  set that is being built.
     */
    private final static String siteName         = "Site:London";
    private final static String siteIdentifier   = "London identifier";
    private final static String siteDisplayName  = "London displayName";
    private final static String siteDescription  = "London description";

    /*
     * These are the two location  definitions that are part of the set.
     */
    private final static String  facility1Name                    = "Facility:London Data Centre";
    private final static String  facility1Identifier              = "London Data Centre identifier";
    private final static String  facility1DisplayName             = "London Data Centre displayName";
    private final static String  facility1Description             = "London Data Centre description";
    private final static String  facility1AdditionalPropertyName  = "London Data Centre additionalPropertyName";
    private final static String  facility1AdditionalPropertyValue = "London Data Centre additionalPropertyValue";
    private final static String  facility2Name                    = "Facility:London Office";
    private final static String  facility2Identifier              = "London Office identifier";
    private final static String  facility2DisplayName             = "London Office displayName";
    private final static String  facility2Description             = "London Office description";
    private final static String  facility2NameUpdate              = "Facility:London Office - updated";

    private final static String  searchString          = ".*London.*";


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
            CreateLocationsTest.runIt(serverPlatformRootURL, serverName, userId, results.getAuditLogDestination());
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
        CreateLocationsTest thisTest = new CreateLocationsTest();

        AuditLog auditLog = new AuditLog(auditLogDestination,
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceCode(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDevelopmentStatus(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceName(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceDescription(),
                                         AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS.getAccessServiceWiki());

        LocationManager client   = thisTest.getLocationManagerClient(serverName, serverPlatformRootURL, auditLog);
        String          siteGUID = thisTest.createSite(client, userId);

        thisTest.createFacilities(client, siteGUID, userId);

        thisTest.deleteLocations(client, siteGUID, userId);
    }


    /**
     * Create and return a location s' manager client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformRootURL the network address of the server running the OMAS REST servers
     * @param auditLog logging destination
     * @return client
     * @throws FVTUnexpectedCondition the test case failed
     */
    private LocationManager getLocationManagerClient(String   serverName,
                                                     String   serverPlatformRootURL,
                                                     AuditLog auditLog) throws FVTUnexpectedCondition
    {
        final String activityName = "getLocationManagerClient";

        try
        {
            DigitalArchitectureRESTClient restClient = new DigitalArchitectureRESTClient(serverName, serverPlatformRootURL);

            return new LocationManager(serverName, serverPlatformRootURL, restClient, maxPageSize, auditLog);
        }
        catch (Exception unexpectedError)
        {
            throw new FVTUnexpectedCondition(testCaseName, activityName, unexpectedError);
        }
    }


    /**
     * Create a location  set and return its GUID.
     *
     * @param client interface to Digital Architecture OMAS
     * @param userId calling user
     * @return guid of set
     * @throws FVTUnexpectedCondition the test case failed
     */
    private String createSite(LocationManager client,
                              String          userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createSite";

        try
        {
            LocationProperties locationProperties = new LocationProperties();
            locationProperties.setQualifiedName(siteName);
            locationProperties.setIdentifier(siteIdentifier);
            locationProperties.setDisplayName(siteDisplayName);
            locationProperties.setDescription(siteDescription);
            
            String siteGUID = client.createLocation(userId, locationProperties);
            
            if (siteGUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create Site)");
            }

            client.setLocationAsFixedPhysical(userId, siteGUID, null, null, null, "GMT");

            LocationElement    retrievedElement = client.getLocationByGUID(userId, siteGUID);
            LocationProperties retrievedSite    = retrievedElement.getLocationProperties();

            if (retrievedSite == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Site from Retrieve)");
            }

            if (! siteName.equals(retrievedSite.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Site Retrieve)");
            }
            if (! siteDisplayName.equals(retrievedSite.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Site Retrieve)");
            }
            if (! siteDescription.equals(retrievedSite.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Site Retrieve)");
            }
            
            List<LocationElement> siteList = client.getLocationsByName(userId, siteName, 0, maxPageSize);

            if (siteList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no site for RetrieveByName)");
            }
            else if (siteList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty location  list for Site RetrieveByName)");
            }
            else if (siteList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Site for RetrieveByName contains" + siteList.size() +
                                                 " elements)");
            }

            retrievedElement = siteList.get(0);
            retrievedSite = retrievedElement.getLocationProperties();

            if (! siteName.equals(retrievedSite.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Site RetrieveByName)");
            }
            if (! siteDisplayName.equals(retrievedSite.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Site RetrieveByName)");
            }
            if (! siteDescription.equals(retrievedSite.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Site RetrieveByName)");
            }

            return siteGUID;
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
     * Two location  definitions for the set using a variety of mechanisms.
     *
     * @param client interface to Digital Architecture OMAS
     * @param siteGUID unique identifier of the location  set
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void createFacilities(LocationManager client,
                                  String          siteGUID,
                                  String          userId) throws FVTUnexpectedCondition
    {
        final String activityName = "createFacilities";

        try
        {
            Map<String, String>  additionalProperties = new HashMap<>();
            additionalProperties.put(facility1AdditionalPropertyName, facility1AdditionalPropertyValue);

            LocationProperties locationProperties = new LocationProperties();
            locationProperties.setQualifiedName(facility1Name);
            locationProperties.setIdentifier(facility1Identifier);
            locationProperties.setDisplayName(facility1DisplayName);
            locationProperties.setDescription(facility1Description);
            locationProperties.setAdditionalProperties(additionalProperties);
            
            String facility1GUID = client.createLocation(userId, locationProperties);

            if (facility1GUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create of definition 1)");
            }

            client.setLocationAsFixedPhysical(userId, facility1GUID, null, null, null, "GMT");

            client.setupNestedLocation(userId, siteGUID, facility1GUID);
            
            LocationElement    retrievedElement  = client.getLocationByGUID(userId, facility1GUID);
            LocationProperties retrievedProperties = retrievedElement.getLocationProperties();

            if (retrievedProperties == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 1 from Retrieve)");
            }

            if (! facility1Name.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve of 1)");
            }
            if (! facility1DisplayName.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve of 1)");
            }
            if (! facility1Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve of 1)");
            }
            if (retrievedProperties.getAdditionalProperties() == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(null additionalProperties from Retrieve of 1)");
            }
            else if (! facility1AdditionalPropertyValue.equals(retrievedProperties.getAdditionalProperties().get(facility1AdditionalPropertyName)))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(bad additionalProperties from Retrieve of 1)");
            }

            List<LocationElement> facilityList = client.getLocationsByName(userId, facility1Name, 0, maxPageSize);

            if (facilityList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no facility for RetrieveByName of 1)");
            }
            else if (facilityList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty facility list for RetrieveByName of 1)");
            }
            else if (facilityList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Facility list for RetrieveByName of 1 contains" + facilityList.size() +
                                                         " elements)");
            }

            retrievedElement = facilityList.get(0);
            retrievedProperties = retrievedElement.getLocationProperties();

            if (retrievedProperties == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Facility 1 from Retrieve)");
            }

            if (! facility1Name.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Facility 1 RetrieveByName of 1)");
            }
            if (! facility1DisplayName.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Facility 1 RetrieveByName of 1)");
            }
            if (! facility1Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Facility 1 RetrieveByName of 1)");
            }
  
            facilityList = client.findLocations(userId, searchString, 0, maxPageSize);

            if (facilityList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no location  for findLocations of 1)");
            }
            else if (facilityList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty location  list for findLocations of 1)");
            }
            else if (facilityList.size() != 2)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Locations for findLocations of 1 contains" + facilityList.size() +
                                                         " elements)");
            }


            /*
             * Check that not possible to create an element with the same qualified name.
             */
            try
            {
                client.createLocation(userId, locationProperties);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(duplicate create of facility allowed)");
            }
            catch (InvalidParameterException okResult)
            {
                // nothing to do
            }

            /*
             * Now do the second value
             */
            /*
             * Now do the second value
             */
            locationProperties = new LocationProperties();
            locationProperties.setQualifiedName(facility2Name);
            locationProperties.setIdentifier(facility2Identifier);
            locationProperties.setDisplayName(facility2DisplayName);
            locationProperties.setDescription(facility2Description);
            locationProperties.setAdditionalProperties(additionalProperties);

            String facility2GUID = client.createLocation(userId, locationProperties);

            if (facility2GUID == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no GUID for Create of definition 2)");
            }

            retrievedElement  = client.getLocationByGUID(userId, facility2GUID);
            retrievedProperties = retrievedElement.getLocationProperties();

            if (retrievedProperties == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no Valid Values definition 2 from Retrieve)");
            }

            if (! facility2Name.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from Retrieve of 2)");
            }
            if (! facility2DisplayName.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from Retrieve of 2)");
            }
            if (! facility2Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from Retrieve of 2)");
            }

            facilityList = client.getLocationsByName(userId, facility2Name, 0, maxPageSize);

            if (facilityList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no location  for RetrieveByName of 2)");
            }
            else if (facilityList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty location  list for RetrieveByName of 2)");
            }
            else if (facilityList.size() != 1)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Locations for RetrieveByName of 2 contains" + facilityList.size() +
                                                         " elements)");
            }

            retrievedElement = facilityList.get(0);
            retrievedProperties = retrievedElement.getLocationProperties();

            if (! facility2Name.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveByName of 2)");
            }
            if (! facility2DisplayName.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveByName of 2)");
            }
            if (! facility2Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveByName of 2)");
            }

            facilityList = client.findLocations(userId, searchString, 0, maxPageSize);

            if (facilityList == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(no location  for findLocations of all)");
            }
            else if (facilityList.isEmpty())
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Empty location  list for findLocations of all)");
            }
            else if (facilityList.size() != 3)
            {
                throw new FVTUnexpectedCondition(testCaseName,
                                                 activityName + "(Locations for findLocations of all contains" + facilityList.size() +
                                                         " elements)");
            }


            /*
             * Add the new name
             */
            locationProperties = new LocationProperties();
            locationProperties.setDisplayName(facility2NameUpdate);
            client.updateLocation(userId, true, facility2GUID, locationProperties);

            retrievedElement  = client.getLocationByGUID(userId, facility2GUID);
            retrievedProperties = retrievedElement.getLocationProperties();

            if (! facility2Name.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveUpdate of 2)");
            }
            if (! facility2NameUpdate.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveUpdate of 2)");
            }
            if (! facility2Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveUpdate of 2)");
            }
            if (! facility2Identifier.equals(retrievedProperties.getIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad identifier from RetrieveUpdate of 2)");
            }

            /*
             * Update location  2 with location  1's qualified name - this should fail
             */
            try
            {
                locationProperties = new LocationProperties();
                locationProperties.setQualifiedName(facility1Name);
                client.updateLocation(userId, true, facility2GUID, locationProperties);

                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Duplicate update allowed)");
            }
            catch (InvalidParameterException expectedResult)
            {
                // all ok
            }

            /*
             * Location 2 should be exactly as it was
             */
            retrievedElement  = client.getLocationByGUID(userId, facility2GUID);
            retrievedProperties = retrievedElement.getLocationProperties();

            if (! facility2Name.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! facility2NameUpdate.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! facility2Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveUpdate of 2 after qualified name failed change)");
            }
            if (! facility2Identifier.equals(retrievedProperties.getIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad identifier from RetrieveUpdate of 2)");
            }

            /*
             * Update location  2 with a new qualified name - this should work ok
             */
            locationProperties = new LocationProperties();
            locationProperties.setQualifiedName(facility2NameUpdate);
            client.updateLocation(userId, true, facility2GUID, locationProperties);


            /*
             * Location 2 should be updated
             */
            retrievedElement  = client.getLocationByGUID(userId, facility2GUID);
            retrievedProperties = retrievedElement.getLocationProperties();

            if (! facility2NameUpdate.equals(retrievedProperties.getQualifiedName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad qualifiedName from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! facility2NameUpdate.equals(retrievedProperties.getDisplayName()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad displayName from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! facility2Description.equals(retrievedProperties.getDescription()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad description from RetrieveUpdate of 2 after qualified name change)");
            }
            if (! facility2Identifier.equals(retrievedProperties.getIdentifier()))
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Bad identifier from RetrieveUpdate of 2 after qualified name change)");
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
     * Delete the set and check that the location  definitions connected to it are also gone.
     *
     * @param client interface to Digital Architecture OMAS
     * @param siteGUID unique identifier of the location  set
     * @param userId calling user
     * @throws FVTUnexpectedCondition the test case failed
     */
    private void deleteLocations(LocationManager client,
                                 String             siteGUID,
                                 String             userId) throws FVTUnexpectedCondition
    {
        final String activityName = "deleteValidValuesSet";

        try
        {
            client.removeLocation(userId, siteGUID);

            /*
             * Check it has really gone.
             */
            try
            {
                client.getLocationByGUID(userId, siteGUID);
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(Deleted site returned)");
            }
            catch (InvalidParameterException expectedException)
            {
                // All ok
            }

            /*
             * Check for no cascaded delete
             */
            List<LocationElement> searchResults = client.findLocations(userId, searchString, 0, maxPageSize);

            if (searchResults == null)
            {
                throw new FVTUnexpectedCondition(testCaseName, activityName + "(cascaded delete occurred)");
            }
            else
            {
                for (LocationElement locationElement: searchResults)
                {
                    client.removeLocation(userId, locationElement.getElementHeader().getGUID());
                }
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
