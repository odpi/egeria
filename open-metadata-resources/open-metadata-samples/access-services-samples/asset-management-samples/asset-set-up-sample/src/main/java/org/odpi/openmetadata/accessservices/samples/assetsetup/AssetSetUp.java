/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.samples.assetsetup;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.ExternalAssetManagerClient;
import org.odpi.openmetadata.accessservices.assetowner.client.CSVFileAssetOwner;
import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ExternalReferenceManagerClient;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ConnectionManager;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.LocationManager;
import org.odpi.openmetadata.accessservices.communityprofile.properties.ActorProfileProperties;
import org.odpi.openmetadata.accessservices.digitalarchitecture.client.ReferenceDataManager;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceZoneManager;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceZoneProperties;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.CapabilityManagerClient;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.http.HttpHelper;
import org.odpi.openmetadata.platformservices.client.PlatformServicesClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * AssetSetUp illustrates the use of a variety of OMAS APIs to catalog a file in the open metadata ecosystem.
 */
public class AssetSetUp
{
    private static final String cocoOrg = "Coco Pharmaceuticals";
    private static final String oakDeanOrg = "Oak Dene Hospital";
    private static final String oldMarketOrg = "Old Market Hospital";

    private static final String fileName1  = "sample-data/oak-dene-drop-foot-weekly-measurements/week1.csv";
    private static final String fileName2  = "sample-data/oak-dene-drop-foot-weekly-measurements/week2.csv";
    private static final String fileName3  = "sample-data/oak-dene-drop-foot-weekly-measurements/week3.csv";
    private static final String fileName4  = "sample-data/oak-dene-drop-foot-weekly-measurements/week4.csv";
    private static final String fileName5  = "sample-data/oak-dene-drop-foot-weekly-measurements/week5.csv";
    private static final String fileName6  = "sample-data/old-market-drop-foot-weekly-measurements/week1.csv";
    private static final String fileName7  = "sample-data/old-market-drop-foot-weekly-measurements/week2.csv";
    private static final String fileName8  = "sample-data/old-market-drop-foot-weekly-measurements/week3.csv";
    private static final String fileName9  = "sample-data/old-market-drop-foot-weekly-measurements/week4.csv";
    private static final String fileName10 = "sample-data/old-market-drop-foot-weekly-measurements/week5.csv";

    private final String serverName;
    private final String platformURLRoot;
    private final String clientUserId;

    private AssetConsumer                  assetConsumerClient            = null;
    private CSVFileAssetOwner              csvOnboardingClient            = null;
    private OrganizationManagement         organizationManagement     = null;
    private ExternalAssetManagerClient     externalAssetManagerClient = null;
    private DatabaseManagerClient          databaseManagerClient      = null;
    private ExternalReferenceManagerClient externalReferenceManagerClient = null;
    private ConnectionManager              connectionManager              = null;
    private LocationManager                locationManager                = null;
    private ReferenceDataManager           validValuesManager             = null;
    private CapabilityManagerClient        capabilityManagerClient        = null;
    private GovernanceZoneManager          governanceZoneManager          = null;

    private final Map<String, String> assetGUIDMap = new HashMap<>();
    private final Map<String, String> assetQNMap   = new HashMap<>();
    private final Map<String, String> orgMap       = new HashMap<>();




    /**
     * Set up the parameters for the utility.
     *
     * @param serverName server to call
     * @param platformURLRoot location of server
     * @param clientUserId userId to access the server
     */
    private AssetSetUp(String serverName,
                       String platformURLRoot,
                       String clientUserId)
    {
        this.serverName = serverName;
        this.platformURLRoot = platformURLRoot;
        this.clientUserId = clientUserId;

        try
        {
            csvOnboardingClient = new CSVFileAssetOwner(serverName, platformURLRoot);
            assetConsumerClient = new AssetConsumer(serverName, platformURLRoot);
            organizationManagement = new OrganizationManagement(serverName, platformURLRoot);
            externalAssetManagerClient = new ExternalAssetManagerClient(serverName, platformURLRoot);
            databaseManagerClient = new DatabaseManagerClient(serverName, platformURLRoot);
            externalReferenceManagerClient = new ExternalReferenceManagerClient(serverName, platformURLRoot);
            connectionManager = new ConnectionManager(serverName, platformURLRoot);
            locationManager = new LocationManager(serverName, platformURLRoot);
            capabilityManagerClient = new CapabilityManagerClient(serverName, platformURLRoot);
            governanceZoneManager = new GovernanceZoneManager(serverName, platformURLRoot);
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when creating the clients.  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Retrieve the version of the platform.  This fails if the platform is not running or the endpoint is populated by a service that is not an
     * OMAG Server Platform.
     *
     * @return platform version or null
     */
    private String getPlatformOrigin()
    {
        try
        {
            /*
             * This client is from the platform services module and queries the runtime state of the platform and the servers that are running on it.
             */
            PlatformServicesClient platformServicesClient = new PlatformServicesClient("MyPlatform", platformURLRoot);

            /*
             * This is the first call to the platform and determines the version of the software.
             * If the platform is not running, or the remote service is not an OMAG Server Platform,
             * the utility fails at this point.
             */
            return platformServicesClient.getPlatformOrigin(clientUserId);
        }
        catch (Exception error)
        {
            System.out.println("\n\nThere was an " + error.getClass().getName() + " exception when calling the platform.  Error message is: " + error.getMessage());
            System.out.println("Ensure the platform URl is correct and the platform is running");
        }

        return null;
    }


    /**
     * Maintain a list of assets that this utility knows about.
     *
     * @param assetGUID unique identifier of newly discovered asset
     */
    private void addAssetToMaps(String assetGUID)
    {
        try
        {
            AssetUniverse asset = assetConsumerClient.getAssetProperties(clientUserId, assetGUID);

            if (asset != null)
            {
                String existingQualifiedName = assetGUIDMap.put(assetGUID, asset.getQualifiedName());

                if ((existingQualifiedName != null) && (! asset.getQualifiedName().equals(existingQualifiedName)))
                {
                    System.out.println("Unexpected conflict in asset map for asset " + assetGUID + ": existing qualifiedName is " + existingQualifiedName + " and new qualifiedName is " + asset.getQualifiedName());
                }

                String existingGUID = assetQNMap.put(asset.getQualifiedName(), assetGUID);

                if ((existingGUID != null) && (! assetGUID.equals(existingGUID)))
                {
                    System.out.println("Unexpected conflict in asset map: existing GUID is " + existingGUID + " and new GUID is " + assetGUID);
                }
            }
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when retrieving asset by GUID.  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Pass a list of assetGUIDs to be saved to the maps.
     *
     * @param assetGUIDs list of guids
     * @return return the last asset guid in the list
     */
    private String addAssetsToMaps(List<String> assetGUIDs)
    {
        String lastGUID = null;

        if (assetGUIDs != null)
        {
            for (String assetGUID : assetGUIDs)
            {
                if (assetGUID != null)
                {
                    this.addAssetToMaps(assetGUID);
                    lastGUID = assetGUID;
                }
            }
        }

        return lastGUID;
    }


    /**
     * Catalog a CSV file digital resource. This creates an asset for the file and every directory (folder) that are listed in its name.
     * The new assets are added to the asset maps.
     *
     * @param fileName file name of digital resource
     */
    private void addCSVAsset(String fileName,
                             String organizationName)
    {
        try
        {
            /*
             * This creates the asset for the file - and also assets for the directories above if they do not already exist.  The call to
             * addAssetsToMaps saves all the guids for later processing.
             */
            String assetGUID = this.addAssetsToMaps(csvOnboardingClient.addCSVFileToCatalog(clientUserId,
                                                                                            fileName,
                                                                                            "This is a new file asset created by AssetSetUp.",
                                                                                            fileName));


            Map<String, String> otherOriginValues = new HashMap<>();
            otherOriginValues.put("sourceProgram", this.getClass().getName());

            csvOnboardingClient.addAssetOrigin(clientUserId, assetGUID, orgMap.get(organizationName), null, otherOriginValues);

            // todo
            //csvOnboardingClient.addSecurityTags(clientUserId, assetGUID, null, null, null);
            //csvOnboardingClient.addSemanticAssignment(clientUserId, assetGUID, null, null);
            csvOnboardingClient.publishAsset(clientUserId, assetGUID);

        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when creating the csv file: " + fileName + ".  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Create a metadata element that represents an organization.  This is used when setting the origin of digital resource.
     * The unique identifier (guid) of the resulting metadata element is store in the org map.
     * This method uses the Community Profile OMAS.  The organization elements are normally created as organizations are onboarded into the
     * open metadata ecosystem.
     *
     * @param organizationName name of org - all other properties are derived from this
     */
    private void createOrganization(String organizationName)
    {
        try
        {
            ActorProfileProperties profileProperties = new ActorProfileProperties();

            profileProperties.setQualifiedName("Organization:" + organizationName);
            profileProperties.setKnownName(organizationName);
            profileProperties.setTypeName("Organization");

            String orgGUID = organizationManagement.createActorProfile(clientUserId,
                                                                       null,
                                                                       null,
                                                                       profileProperties,
                                                                       null);
            orgMap.put(organizationName, orgGUID);
        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when creating the " + organizationName + "'s profile.  Error message is: " + error.getMessage());
            System.exit(-1);
        }
    }


    /**
     * Sets up three organization, one for Coco Pharmaceuticals which is the owning organization, and one for each of the two hospitals that they are
     * collaborating with.
     */
    private void createOrganizations()
    {
        createOrganization(cocoOrg);
        createOrganization(oakDeanOrg);
        createOrganization(oldMarketOrg);
    }


    /**
     * Set up a new zone
     *
     * @param zoneName qualified name
     * @param displayName display name
     * @param description longer description
     * @param criteria what types of assets are found in this zone
     * @throws InvalidParameterException bad parameters passed to client
     * @throws UserNotAuthorizedException userId is not allowed to create zones
     * @throws PropertyServerException service is not running - or is in trouble
     */
    private void createZone(String     zoneName,
                            String     displayName,
                            String     description,
                            String     criteria) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        GovernanceZoneProperties zoneProperties = new GovernanceZoneProperties();

        zoneProperties.setQualifiedName(zoneName);
        zoneProperties.setDisplayName(displayName);
        zoneProperties.setDescription(description);
        zoneProperties.setCriteria(criteria);

        governanceZoneManager.createGovernanceZone(clientUserId, zoneProperties);
    }


    /**
     * Set up some zone definitions.
     */
    public void createZones()
    {
        try
        {
            GovernanceZoneDefinitions[] zoneSampleDefinitions = GovernanceZoneDefinitions.values();

            for (GovernanceZoneDefinitions zoneDefinition : zoneSampleDefinitions)
            {
                createZone(zoneDefinition.getZoneName(),
                           zoneDefinition.getDisplayName(),
                           zoneDefinition.getDescription(),
                           zoneDefinition.getCriteria());
            }


        }
        catch (Exception error)
        {
            System.out.println("There was an exception when calling the GovernanceZoneManager client.  Error message is: " + error.getMessage());
        }
    }


    /**
     * This runs the utility
     */
    private void run()
    {
        try
        {
            /*
             * Set up metadata that describes the governance program and participating organizations
             */
            createOrganizations();
            createZones();

            /*
             * Create the CSV File assets
             */
            this.addCSVAsset(fileName1, oakDeanOrg);
            this.addCSVAsset(fileName2, oakDeanOrg);
            this.addCSVAsset(fileName3, oakDeanOrg);
            this.addCSVAsset(fileName4, oakDeanOrg);
            this.addCSVAsset(fileName5, oakDeanOrg);
            this.addCSVAsset(fileName6, oldMarketOrg);
            this.addCSVAsset(fileName7, oldMarketOrg);
            this.addCSVAsset(fileName8, oldMarketOrg);
            this.addCSVAsset(fileName9, oldMarketOrg);
            this.addCSVAsset(fileName10, oldMarketOrg);


        }
        catch (Exception error)
        {
            System.out.println("There was a " + error.getClass().getName() + " exception when calling the OMAG Server Platform.  Error message is: " + error.getMessage());
        }
    }


    /**
     * Main program that initiates the operation of the AssetSetUp utility.  The parameters are optional.  They are passed space separated.
     * They are used to override the utility's default values.
     *
     * @param args 1. service platform URL root, 2. client userId, 3. server name
     */
    public static void main(String[] args)
    {
        String  platformURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";
        String  serverName = "active-metadata-store";

        if (args.length > 0)
        {
            platformURLRoot = args[0];
        }

        if (args.length > 1)
        {
            clientUserId = args[1];
        }

        if (args.length > 2)
        {
            serverName = args[2];
        }

        System.out.println("===============================");
        System.out.println("Asset Set Up Utility:          ");
        System.out.println("===============================");
        System.out.println("Running against platform: " + platformURLRoot);
        System.out.println("Focused on server: " + serverName);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSL();

        try
        {
            AssetSetUp assetSetUp = new AssetSetUp(serverName, platformURLRoot, clientUserId);

            String platformOrigin = assetSetUp.getPlatformOrigin();

            if (platformOrigin != null)
            {
                System.out.print(" - " + platformOrigin);
            }
            else
            {
                System.out.println();
                System.exit(-1);
            }

            assetSetUp.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
