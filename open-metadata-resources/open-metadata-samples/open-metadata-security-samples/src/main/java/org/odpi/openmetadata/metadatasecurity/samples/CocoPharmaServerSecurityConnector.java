/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataAssetSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataConnectionSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServerSecurity;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServiceSecurity;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.metadatasecurity.properties.Connection;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CocoPharmaServerSecurityConnector provides a specific security connector for Coco Pharmaceuticals
 * users that overrides the default behavior of that open metadata security connector that does
 * not allow any access to anything.
 */
public class CocoPharmaServerSecurityConnector extends OpenMetadataServerSecurityConnector implements OpenMetadataRepositorySecurity,
                                                                                                      OpenMetadataServerSecurity,
                                                                                                      OpenMetadataServiceSecurity,
                                                                                                      OpenMetadataConnectionSecurity,
                                                                                                      OpenMetadataAssetSecurity
{
    /*
     * These variables represent the different groups of user.  Typically these would be
     * implemented as a look up to a user directory such as LDAP rather than in memory lists.
     * The lists are used here to make the demo easier to set up.
     */
    private List<String>              allUsers            = new ArrayList<>();

    private List<String>              assetOnboardingExit = new ArrayList<>();
    private List<String>              serverAdmins        = new ArrayList<>();
    private List<String>              serverOperators     = new ArrayList<>();
    private List<String>              serverInvestigators = new ArrayList<>();
    private List<String>              metadataArchitects  = new ArrayList<>();
    private List<String>              npaAccounts         = new ArrayList<>();
    private List<String>              externalUsers       = new ArrayList<>();

    private List<String>              defaultZoneMembership = new ArrayList<>();
    private List<String>              zonesForExternals     = new ArrayList<>();


    private Map<String, List<String>> zoneAccess   = new HashMap<>();
    private Map<String, String>       ownerZones   = new HashMap<>();

    /*
     * Zones requiring special processing
     */
    private final String personalFilesZoneName   = "personal-files";
    private final String quarantineZoneName      = "quarantine";
    private final String dataLakeZoneName        = "data-lake";
    private final String externalAccessZoneName  = "external-access";


    /**
     * Constructor sets up the security groups
     */
    public CocoPharmaServerSecurityConnector()
    {
        List<String>              allEmployees        = new ArrayList<>();
        List<String>              assetOnboarding     = new ArrayList<>();

        /*
         * Standard zones in use by Coco Pharmaceuticals
         */
        final String researchZoneName        = "research";
        final String hrZoneName              = "human-resources";
        final String financeZoneName         = "finance";
        final String clinicalTrialsZoneName  = "clinical-trials";
        final String infrastructureZoneName  = "infrastructure";
        final String developmentZoneName     = "development";
        final String manufacturingZoneName   = "manufacturing";
        final String governanceZoneName      = "governance";
        final String trashCanZoneName        = "trash-can";

        /*
         * Coco Pharmaceuticals personas
         */
        final String zachNowUserId        = "zach";
        final String steveStarterUserId   = "steves";
        final String terriDaringUserId    = "terri";
        final String tanyaTidieUserId     = "tanyatidie";
        final String pollyTaskerUserId    = "pollytasker";
        final String tessaTubeUserId      = "tessatube";
        final String callieQuartileUserId = "calliequartile";
        final String ivorPadlockUserId    = "ivorpadlock";
        final String bobNitterUserId      = "bobnitter";
        final String faithBrokerUserId    = "faithbroker";
        final String sallyCounterUserId   = "sallycounter";
        final String lemmieStageUserId    = "lemmiestage";
        final String erinOverviewUserId   = "erinoverview";
        final String harryHopefulUserId   = "harryhopeful";
        final String garyGeekeUserId      = "garygeeke";
        final String grantAbleUserId      = "grantable";
        final String robbieRecordsUserId  = "robbierecords";
        final String reggieMintUserId     = "reggiemint";
        final String peterProfileUserId   = "peterprofile";
        final String nancyNoahUserId      = "nancynoah";
        final String sidneySeekerUserId   = "sidneyseeker";
        final String tomTallyUserId       = "tomtally";
        final String julieStitchedUserId  = "juliestitched";
        final String desSignaUserId       = "dessigna";
        final String angelaCummingUserId  = "angelacummings";
        final String julesKeeperUserId    = "jukeskeeper";
        final String stewFasterUserId     = "stewFaster";

        /*
         * System (NPA) accounts
         */

        final String cocoMDS1UserId     = "cocoMDS1npa";
        final String cocoMDS2UserId     = "cocoMDS2npa";
        final String cocoMDS3UserId     = "cocoMDS3npa";
        final String cocoMDS4UserId     = "cocoMDS4npa";
        final String cocoMDS5UserId     = "cocoMDS5npa";
        final String cocoMDS6UserId     = "cocoMDS6npa";
        final String cocoMDSxUserId     = "cocoMDSxnpa";
        final String archiverUserId     = "archiver01";
        final String etlEngineUserId    = "dlETL";
        final String governDL01UserId   = "governDL01npa";
        final String exchangeDL01UserId = "exchangeDL01npa";
        final String findItDL01UserId   = "findItDL01npa";
        final String fixItDL01UserId    = "fixItDL01npa";
        final String onboardDL01UserId  = "onboardDL01npa";
        final String monitorDL01UserId  = "monitorDL01npa";
        final String cocoEngineUserId   = "cocoETLnpa";
        final String generalNPAUserId   = "generalnpa";

        /*
         * This userId is the userId that is expected in the calls to auto-start servers (it is changed with the startup.user variable).
         * Although this function is not part of the Coco Labs the value is included in case someone uses the coco security connectors to test
         * the auto start functions.
         */
        final String serverStartUpUserId = "autoStart01npa";

        /*
         * Set up default zone membership
         */
        defaultZoneMembership.add(quarantineZoneName);

        /*
         * Add all the users into the all group
         */
        allUsers.add(zachNowUserId);
        allUsers.add(steveStarterUserId);
        allUsers.add(terriDaringUserId);
        allUsers.add(tanyaTidieUserId);
        allUsers.add(pollyTaskerUserId);
        allUsers.add(tessaTubeUserId);
        allUsers.add(callieQuartileUserId);
        allUsers.add(ivorPadlockUserId);
        allUsers.add(bobNitterUserId);
        allUsers.add(faithBrokerUserId);
        allUsers.add(sallyCounterUserId);
        allUsers.add(lemmieStageUserId);
        allUsers.add(erinOverviewUserId);
        allUsers.add(harryHopefulUserId);
        allUsers.add(garyGeekeUserId);
        allUsers.add(grantAbleUserId);
        allUsers.add(robbieRecordsUserId);
        allUsers.add(reggieMintUserId);
        allUsers.add(peterProfileUserId);
        allUsers.add(nancyNoahUserId);
        allUsers.add(sidneySeekerUserId);
        allUsers.add(tomTallyUserId);
        allUsers.add(julieStitchedUserId);
        allUsers.add(desSignaUserId);
        allUsers.add(angelaCummingUserId);
        allUsers.add(julesKeeperUserId);
        allUsers.add(stewFasterUserId);
        allUsers.add(archiverUserId);
        allUsers.add(etlEngineUserId);
        allUsers.add(cocoMDS1UserId);
        allUsers.add(cocoMDS2UserId);
        allUsers.add(cocoMDS3UserId);
        allUsers.add(cocoMDS4UserId);
        allUsers.add(cocoMDS5UserId);
        allUsers.add(cocoMDS6UserId);
        allUsers.add(cocoMDSxUserId);
        allUsers.add(governDL01UserId);
        allUsers.add(exchangeDL01UserId);
        allUsers.add(findItDL01UserId);
        allUsers.add(fixItDL01UserId);
        allUsers.add(onboardDL01UserId);
        allUsers.add(monitorDL01UserId);
        allUsers.add(cocoEngineUserId);
        allUsers.add(generalNPAUserId);
        allUsers.add(serverStartUpUserId);

        allEmployees.add(zachNowUserId);
        allEmployees.add(steveStarterUserId);
        allEmployees.add(terriDaringUserId);
        allEmployees.add(tanyaTidieUserId);
        allEmployees.add(pollyTaskerUserId);
        allEmployees.add(tessaTubeUserId);
        allEmployees.add(callieQuartileUserId);
        allEmployees.add(ivorPadlockUserId);
        allEmployees.add(bobNitterUserId);
        allEmployees.add(faithBrokerUserId);
        allEmployees.add(sallyCounterUserId);
        allEmployees.add(lemmieStageUserId);
        allEmployees.add(erinOverviewUserId);
        allEmployees.add(harryHopefulUserId);
        allEmployees.add(garyGeekeUserId);
        allEmployees.add(reggieMintUserId);
        allEmployees.add(peterProfileUserId);
        allEmployees.add(sidneySeekerUserId);
        allEmployees.add(tomTallyUserId);
        allEmployees.add(julesKeeperUserId);
        allEmployees.add(stewFasterUserId);

        assetOnboarding.add(peterProfileUserId);
        assetOnboarding.add(erinOverviewUserId);
        assetOnboarding.add(findItDL01UserId);
        assetOnboarding.add(fixItDL01UserId);
        assetOnboardingExit.add(erinOverviewUserId);

        serverAdmins.add(garyGeekeUserId);
        serverOperators.add(garyGeekeUserId);
        serverOperators.add(serverStartUpUserId);
        serverInvestigators.add(garyGeekeUserId);

        metadataArchitects.add(erinOverviewUserId);
        metadataArchitects.add(peterProfileUserId);

        externalUsers.add(grantAbleUserId);
        externalUsers.add(julieStitchedUserId);
        externalUsers.add(angelaCummingUserId);
        externalUsers.add(robbieRecordsUserId);

        zonesForExternals.add(externalAccessZoneName);

        npaAccounts.add(archiverUserId);
        npaAccounts.add(etlEngineUserId);
        npaAccounts.add(cocoMDS1UserId);
        npaAccounts.add(cocoMDS2UserId);
        npaAccounts.add(cocoMDS3UserId);
        npaAccounts.add(cocoMDS4UserId);
        npaAccounts.add(cocoMDS5UserId);
        npaAccounts.add(cocoMDS6UserId);
        npaAccounts.add(cocoMDSxUserId);
        npaAccounts.add(findItDL01UserId);
        npaAccounts.add(fixItDL01UserId);
        npaAccounts.add(governDL01UserId);
        npaAccounts.add(exchangeDL01UserId);
        npaAccounts.add(onboardDL01UserId);
        npaAccounts.add(monitorDL01UserId);
        npaAccounts.add(cocoEngineUserId);
        npaAccounts.add(generalNPAUserId);
        npaAccounts.add(serverStartUpUserId);
        assetOnboarding.addAll(npaAccounts);

        List<String> zoneSetUp = new ArrayList<>();

        /*
         * Set up the assignment of zones for specific users
         */
        ownerZones.put(tanyaTidieUserId, clinicalTrialsZoneName);

        /*
         * Set up the zones
         */
        zoneAccess.put(trashCanZoneName, npaAccounts);
        zoneAccess.put(personalFilesZoneName, allEmployees);
        zoneAccess.put(quarantineZoneName, assetOnboarding);
        zoneAccess.put(dataLakeZoneName, allUsers);
        zoneAccess.put(externalAccessZoneName, externalUsers);

        zoneSetUp.add(callieQuartileUserId);
        zoneSetUp.add(tessaTubeUserId);

        zoneAccess.put(researchZoneName, zoneSetUp);

        zoneSetUp.add(tanyaTidieUserId);

        zoneAccess.put(clinicalTrialsZoneName, zoneSetUp);

        zoneSetUp = new ArrayList<>();
        zoneSetUp.add(faithBrokerUserId);

        zoneAccess.put(hrZoneName, zoneSetUp);

        zoneSetUp = new ArrayList<>();
        zoneSetUp.add(reggieMintUserId);
        zoneSetUp.add(tomTallyUserId);
        zoneSetUp.add(sallyCounterUserId);

        zoneAccess.put(financeZoneName, zoneSetUp);

        zoneSetUp = new ArrayList<>();
        zoneSetUp.add(garyGeekeUserId);

        zoneAccess.put(infrastructureZoneName, zoneSetUp);

        zoneSetUp = new ArrayList<>();
        zoneSetUp.add(pollyTaskerUserId);
        zoneSetUp.add(bobNitterUserId);
        zoneSetUp.add(lemmieStageUserId);
        zoneSetUp.add(nancyNoahUserId);
        zoneSetUp.add(desSignaUserId);

        zoneAccess.put(developmentZoneName, zoneSetUp);

        zoneSetUp = new ArrayList<>();
        zoneSetUp.add(stewFasterUserId);

        zoneAccess.put(manufacturingZoneName, zoneSetUp);

        zoneSetUp = new ArrayList<>();
        zoneSetUp.add(garyGeekeUserId);
        zoneSetUp.add(erinOverviewUserId);
        zoneSetUp.add(ivorPadlockUserId);
        zoneSetUp.add(julesKeeperUserId);
        zoneSetUp.add(pollyTaskerUserId);
        zoneSetUp.add(faithBrokerUserId);
        zoneSetUp.add(reggieMintUserId);

        zoneAccess.put(governanceZoneName, zoneSetUp);
    }


    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    @Override
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        if (allUsers.contains(userId))
        {
            return;
        }

        super.validateUserForServer(userId);
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    @Override
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        if (serverAdmins.contains(userId))
        {
            return;
        }

        super.validateUserAsServerAdmin(userId);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    @Override
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        if (serverOperators.contains(userId))
        {
            return;
        }

        super.validateUserAsServerOperator(userId);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    @Override
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        if (serverInvestigators.contains(userId))
        {
            return;
        }

        super.validateUserAsServerInvestigator(userId);
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForService(String   userId,
                                        String   serviceName) throws UserNotAuthorizedException
    {
        if (allUsers.contains(userId))
        {
            return;
        }

        super.validateUserForService(userId, serviceName);
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param serviceOperationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        final String  assetOwnerServiceName = "Asset Owner OMAS";
        final String  deleteAssetOperationName = "deleteAsset";

        /*
         * Coco Pharmaceuticals have decided that the assetDelete method from Asset Owner OMAS is too powerful
         * to use and so this test means that only non-personal accounts (NPA) can use it.
         *
         * They delete an asset by moving it to the "trash-can" zone where it is cleaned up by automated
         * processes the next day.
         */
        if ((assetOwnerServiceName.equals(serviceName)) && (deleteAssetOperationName.equals(serviceOperationName)))
        {
            if (npaAccounts.contains(userId))
            {
                return;
            }
        }
        else
        {
            if (allUsers.contains(userId))
            {
                return;
            }
        }

        super.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
    }


    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForConnection(String     userId,
                                           Connection connection) throws UserNotAuthorizedException
    {
        if (connection == null)
        {
            return;
        }

        if ((connection.getClearPassword() == null) &&
            (connection.getEncryptedPassword() == null) &&
            (connection.getSecuredProperties() == null))
        {
            return;
        }

        if (npaAccounts.contains(userId))
        {
            return;
        }

        super.validateUserForConnection(userId, connection);
    }


    /**
     * Select a connection from the list of connections attached to an asset.  Some connections change the userId to
     * provide a higher level of access that a specific user account.  These connections are processed first so that the user gets the
     * most secure connection to use if they are allowed.  In Coco Pharmaceuticals, these types of connections are only available to
     * engines working in the data lake.
     *
     * @param userId calling user
     * @param asset asset requested by caller
     * @param connections list of attached connections
     * @return selected connection or null (pretend there are no connections attached to the asset) or
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public Connection validateUserForAssetConnectionList(String           userId,
                                                         Asset            asset,
                                                         List<Connection> connections) throws UserNotAuthorizedException
    {
        UserNotAuthorizedException caughtException = null;

        if ((connections != null) && (! connections.isEmpty()))
        {
            List<Connection> unsecuredConnections = new ArrayList<>();

            /*
             * Need to process the secured connections first.
             */
            for (Connection connection : connections)
            {
                if (connection != null)
                {
                    if ((connection.getClearPassword() == null) &&
                        (connection.getEncryptedPassword() == null) &&
                        (connection.getSecuredProperties() == null))
                    {
                        /*
                         * Put the unsecured connection by to process after all of the secured connections have been processed.
                         */
                        unsecuredConnections.add(connection);
                    }
                    else
                    {
                        /*
                         * This is a secured connection.
                         */
                        try
                        {
                            validateUserForConnection(userId, connection);
                            return connection;
                        }
                        catch (UserNotAuthorizedException error)
                        {
                            caughtException = error;
                        }
                    }
                }
            }

            /*
             * Now process the secured connections.
             */
            for (Connection connection : unsecuredConnections)
            {
                if (connection != null)
                {
                    try
                    {
                        validateUserForConnection(userId, connection);
                        return connection;
                    }
                    catch (UserNotAuthorizedException error)
                    {
                        caughtException = error;
                    }
                }
            }
        }

        /*
         * No connection is available to this user.  If an exception has been returned then use it.
         */
        if (caughtException != null)
        {
            throw caughtException;
        }

        return null;
    }


    /**
     * Tests for whether a specific user should have access to an asset based on its zones.
     *
     * @param userId identifier of user
     * @param assetZones name of the zones
     * @param updateRequested is the asset to be changed
     * @param zoneMembershipChanged is the asset's zone membership to be changed
     * @param userIsAssetOwner is the user one of the owner's of the asset
     * @return whether the user is authorized to access asset in these zones
     */
    private boolean userHasAccessToAssetZones(String           userId,
                                              List<String>     assetZones,
                                              boolean          updateRequested,
                                              boolean          zoneMembershipChanged,
                                              boolean          userIsAssetOwner)
    {
        List<String> testZones;

        /*
         * The quarantine zone is the default zone if it is not set up
         */
        if ((assetZones == null) || (assetZones.isEmpty()))
        {
            testZones = defaultZoneMembership;
        }
        else
        {
            testZones = assetZones;
        }

        /*
         * If the quarantine zone is included in the list of zones then only the rules for the quarantine
         * zone are considered.
         */
        if (testZones.contains(quarantineZoneName))
        {
            List<String> zoneAccounts = zoneAccess.get(quarantineZoneName);

            if ((zoneAccounts != null) && (zoneAccounts.contains(userId)))
            {
                return true;
            }
        }
        else /* allow access to the asset if any other zone permits it */
        {
            for (String zoneName : testZones)
            {
                if (zoneName != null)
                {
                    /*
                     * The data lake zone is special - only npaAccounts can update assets in the data lake zone.
                     * Another user may update these assets only if they have access via another one of the asset's zone.
                     */
                    if ((zoneName.equals(dataLakeZoneName)) && (updateRequested))
                    {
                        if (npaAccounts.contains(userId))
                        {
                            return true;
                        }
                    }

                    /*
                     * Access to personal files is only permitted by the owner of the asset.  If they assign the
                     * asset to another zone, this may allow others to read and change the asset.
                     */
                    else if (zoneName.equals(personalFilesZoneName))
                    {
                        if (userIsAssetOwner)
                        {
                            return true;
                        }
                    }

                    /*
                     * Standard look up of user's assigned to zones
                     */
                    else
                    {
                        List<String> zoneAccounts = zoneAccess.get(zoneName);

                        if ((zoneAccounts != null) && (zoneAccounts.contains(userId)))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Adds the requested zone to the list of zones for an asset (avoiding duplicates).
     *
     * @param currentZones current list of zones
     * @param zoneToAdd new zone
     * @return new list of zones
     */
    private List<String>  addZoneName(List<String>   currentZones,
                                      String         zoneToAdd)
    {
        if (zoneToAdd == null)
        {
            return currentZones;
        }

        List<String>  resultingZones;

        if (currentZones == null)
        {
            resultingZones = new ArrayList<>();
            resultingZones.add(zoneToAdd);
        }
        else
        {
            resultingZones = currentZones;

            if (! resultingZones.contains(zoneToAdd))
            {
                resultingZones.add(zoneToAdd);
            }
        }

        return resultingZones;
    }


    /**
     * Remove the requested zone from the list of zones if it is present.
     *
     * @param currentZones current list of zones
     * @param zoneToRemove obsolete zone
     * @return new list of zones
     */
    private List<String>  removeZoneName(List<String>   currentZones,
                                         String         zoneToRemove)
    {
        if ((zoneToRemove == null) || (currentZones == null))
        {
            return currentZones;
        }

        List<String>  resultingZones = new ArrayList<>(currentZones);
        resultingZones.remove(zoneToRemove);

        return resultingZones;
    }


    /**
     * Tests for whether a specific user is the owner of an asset.  This test does not cover
     * the use of profile names for asset owner.
     *
     * @param userId identifier of user
     * @param asset asset to test
     * @return  boolean flag
     */
    private boolean  isUserAssetOwner(String     userId,
                                      Asset      asset)
    {
        if (asset != null)
        {
            if (userId != null)
            {
                return userId.equals(asset.getOwner());
            }
        }

        return false;
    }


    /**
     * Test whether the zone has changed because this may need special processing.
     *
     * @param oldZoneMembership zone membership before update
     * @param newZoneMembership zone membership after update
     * @return boolean true if they are different
     */
    private boolean  hasZoneChanged(List<String> oldZoneMembership,
                                    List<String> newZoneMembership)
    {
        /*
         * Are they the same object or both null?
         */
        if (oldZoneMembership == newZoneMembership)
        {
            return false;
        }

        /*
         * If either is null they are different because already tested that they are not both null.
         */
        if ((oldZoneMembership == null) || (newZoneMembership == null))
        {
            return true;
        }

        /*
         * If they each contain the other then they are equal
         */
        return ! ((oldZoneMembership.containsAll(newZoneMembership)) &&
                  (newZoneMembership.containsAll(oldZoneMembership)));
    }


    /**
     * Test to see if a specific zone has been removed.
     *
     * @param zoneName name of zone ot test for
     * @param oldZoneMembership old zone membership
     * @param newZoneMembership new zone membership
     * @return flag - true if the zone has been removed; otherwise false
     */
    private boolean  zoneBeenRemoved(String       zoneName,
                                     List<String> oldZoneMembership,
                                     List<String> newZoneMembership)
    {
        /*
         * Being defensive to prevent NPEs in the server processing.
         */
        if (zoneName == null)
        {
            return false;
        }

        /*
         * Are they the same object or both null?
         */
        if (oldZoneMembership == newZoneMembership)
        {
            return false;
        }

        /*
         * If the old zone is null then nothing could have been removed.
         */
        if (oldZoneMembership == null)
        {
            return false;
        }

        if (oldZoneMembership.contains(zoneName))
        {
            /*
             * Determine if the new zone still contains the zone of interest.
             */
            if (newZoneMembership == null)
            {
                return true;
            }
            else if (newZoneMembership.contains(zoneName))
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Determine the appropriate setting for the supported zones depending on the user and the
     * default supported zones set up for the service.  This is called whenever an asset is accessed.
     *
     * @param supportedZones default setting of the supported zones for the service
     * @param serviceName name of the called service
     * @param user name of the user
     *
     * @return list of supported zones for the user
     * @throws InvalidParameterException one of the parameter values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Override
    public List<String> setSupportedZonesForUser(List<String>  supportedZones,
                                                 String        serviceName,
                                                 String        user) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (externalUsers.contains(user))
        {
            return zonesForExternals;
        }

        return supportedZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * default zones.  This is called whenever a new asset is created.
     *
     * The default behavior is to use the default values, unless the zones have been explicitly set up,
     * in which case, they are left unchanged.
     *
     * @param defaultZones setting of the default zones for the service
     * @param asset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Override
    public List<String> setAssetZonesToDefault(List<String>  defaultZones,
                                               Asset         asset) throws InvalidParameterException,
                                                                           PropertyServerException
    {
        if ((defaultZones == null) || (defaultZones.isEmpty()))
        {
            return super.setAssetZonesToDefault(defaultZoneMembership, asset);
        }

        return super.setAssetZonesToDefault(defaultZones, asset);
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * The default behavior is to keep the updated zones as they are.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param publishZones setting of the supported zones for the service
     * @param originalAsset original values for the asset
     * @param updatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    @Override
    public List<String> verifyAssetZones(List<String>  defaultZones,
                                         List<String>  supportedZones,
                                         List<String>  publishZones,
                                         Asset         originalAsset,
                                         Asset         updatedAsset) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (updatedAsset != null)
        {
            if (updatedAsset.getOwner() != null)
            {
                return addZoneName(updatedAsset.getZoneMembership(),
                                   ownerZones.get(updatedAsset.getOwner()));
            }
            else
            {
                return updatedAsset.getZoneMembership();
            }
        }

        return null;
    }


    /**
     * Test to see that the separation of duty rules are adhered to in the quarantine zone.
     *
     * @param userId calling user
     * @param auditHeader audit header of the asset
     * @return boolean flag - true = all in ok
     */
    private boolean validateSeparationOfDuties(String           userId,
                                               AssetAuditHeader auditHeader)
    {
        if (auditHeader != null)
        {
            if (userId.equals(auditHeader.getCreatedBy()))
            {
                return false;
            }

            return true;
        }

        return false;
    }


    /**
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    @Override
    public void  validateUserForAssetCreate(String     userId,
                                            Asset      asset) throws UserNotAuthorizedException
    {
        if (asset != null)
        {
            if (userHasAccessToAssetZones(userId,
                                          asset.getZoneMembership(),
                                          true,
                                          false,
                                          this.isUserAssetOwner(userId, asset)))
            {
                return;
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetCreate(userId, asset);
    }


    /**
     * Tests for whether a specific user should have read access to a specific asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
    @Override
    public void  validateUserForAssetRead(String     userId,
                                          Asset      asset) throws UserNotAuthorizedException
    {
        if (asset != null)
        {
            if (userHasAccessToAssetZones(userId,
                                          asset.getZoneMembership(),
                                          false,
                                          false,
                                          this.isUserAssetOwner(userId, asset)))
            {
                return;
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetRead(userId, asset);
    }


    /**
     * Tests for whether a specific user should have the right to update an asset.
     * This is used for a general asset update, which may include changes to the
     * zones and the ownership.
     *
     * @param userId identifier of user
     * @param originalAsset original asset details
     * @param originalAssetAuditHeader details of the asset's audit header
     * @param newAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetDetailUpdate(String           userId,
                                                  Asset            originalAsset,
                                                  AssetAuditHeader originalAssetAuditHeader,
                                                  Asset            newAsset) throws UserNotAuthorizedException
    {
        final String methodName = "validateUserForAssetDetailUpdate";
        if (userId != null)
        {
            if ((originalAsset != null) && (newAsset != null))
            {
                if (userHasAccessToAssetZones(userId,
                                              originalAsset.getZoneMembership(),
                                              true,
                                              this.hasZoneChanged(originalAsset.getZoneMembership(),
                                                                  newAsset.getZoneMembership()),
                                              this.isUserAssetOwner(userId, originalAsset)))
                {
                    /*
                     * Do not allow zone to be null
                     */
                    if (newAsset.getZoneMembership() != null)
                    {
                        if (zoneBeenRemoved(quarantineZoneName,
                                            originalAsset.getZoneMembership(),
                                            newAsset.getZoneMembership()))
                        {
                            /*
                             * Perform special processing for quarantine zone. The owner must be specified.
                             * The quarantine zone can only be removed by NPA accounts or a different person
                             * to the person who set up the asset.
                             */
                            if (newAsset.getOwner() != null)
                            {
                                if (npaAccounts.contains(userId) ||
                                            (validateSeparationOfDuties(userId, originalAssetAuditHeader)))
                                {
                                    return;
                                }
                                else
                                {
                                    super.throwUnauthorizedZoneChange(userId,
                                                                      newAsset,
                                                                      originalAsset.getZoneMembership(),
                                                                      newAsset.getZoneMembership(),
                                                                      methodName);
                                }
                            }
                            else
                            {
                                super.throwIncompleteAsset(userId, newAsset, "owner", methodName);
                            }
                        }
                        else
                        {
                            return;
                        }
                    }
                    else
                    {
                        super.throwIncompleteAsset(userId, newAsset, "zoneMembership", methodName);
                    }
                }
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetDetailUpdate(userId, originalAsset, originalAssetAuditHeader, newAsset);
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an asset such as glossary terms, schema and connections.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetAttachmentUpdate(String userId,
                                                      Asset  asset) throws UserNotAuthorizedException
    {
        if (asset != null)
        {
            if (userHasAccessToAssetZones(userId,
                                          asset.getZoneMembership(),
                                          true,
                                          false,
                                          this.isUserAssetOwner(userId, asset)))
            {
                return;
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetAttachmentUpdate(userId, asset);
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the asset.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    public void  validateUserForAssetFeedback(String     userId,
                                              Asset      asset) throws UserNotAuthorizedException
    {
        if (asset != null)
        {
            if (userHasAccessToAssetZones(userId,
                                          asset.getZoneMembership(),
                                          false,
                                          false,
                                          this.isUserAssetOwner(userId, asset)))
            {
                return;
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetAttachmentUpdate(userId, asset);
    }


    /**
     * Tests for whether a specific user should have the right to delete an asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    @Override
    public void  validateUserForAssetDelete(String     userId,
                                            Asset      asset) throws UserNotAuthorizedException
    {
        if (asset != null)
        {
            if (userHasAccessToAssetZones(userId,
                                          asset.getZoneMembership(),
                                          true,
                                          false,
                                          this.isUserAssetOwner(userId, asset)))
            {
                return;
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetDelete(userId, asset);
    }


    /*
     * =========================================================================================================
     * Type security
     *
     * Any valid user can see the types but only the metadata architects can change them.
     * The logic returns if valid access; otherwise it calls the superclass to throw the
     * user not authorized exception.
     */

    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeCreate(userId, metadataCollectionName, attributeTypeDef);
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (allUsers.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (allUsers.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
    }


    /**
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef current typeDef details
     * @param patch proposed changes to type
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, patch);
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
    }




    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        if (localServerUserId != null)
        {
            if (localServerUserId.equals(userId))
            {
                return;
            }
        }

        super.validateUserForTypeReIdentify(userId,
                                            metadataCollectionName,
                                            originalAttributeTypeDef,
                                            newTypeDefGUID,
                                            newTypeDefName);
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a user not authorized exception on all access/update
     * requests.
     */

    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (may be altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public EntityDetail  validateUserForEntityRead(String       userId,
                                                   String       metadataCollectionName,
                                                   EntityDetail instance) throws UserNotAuthorizedException
    {
        if ("cocoMDS2".equals(serverName))
        {
            /*
             * The cocoMDS2 server connects to all cohorts and so its repository may contain entities from all
             * repositories.  As such it does not return reference copy entities.
             */
            if (instance.getMetadataCollectionName() == null)
            {
                /*
                 * An exception here is a configuration or logic error because the metadata collection name should be set up
                 * in the Coco Pharmaceuticals environment.
                 */
                return super.validateUserForEntityRead(userId, metadataCollectionName, instance);
            }
            else if (! instance.getMetadataCollectionName().equals(metadataCollectionName))
            {
                return null;
            }
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to add a classification to an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntitySummary        instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException
    {
    }


     /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to restore a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the type of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneSummary the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoSummary the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return relationship to return (may be altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public Relationship validateUserForRelationshipRead(String          userId,
                                                        String          metadataCollectionName,
                                                        Relationship    instance) throws UserNotAuthorizedException
    {
        return instance;
    }


    /**
     * Tests for whether a specific user should have the right to update a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to restore a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipRestore(String       userId,
                                                    String       metadataCollectionName,
                                                    String       deletedRelationshipGUID) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the type of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     */
    public boolean  validateEntityReferenceCopySave(EntityDetail instance)
    {
        /*
         * The cocoMDS6 server is linked to the manufacturing IOT devices as well as the core cohort
         * and so it does not save reference copies so that the IOT metadata does not clutter the cocoMDS6 repository
         * (and hence becomes visible in the core cohort.
         */
        if ("cocoMDS6".equals(serverName))
        {
            return false;
        }

        return true;
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     */
    public boolean  validateRelationshipReferenceCopySave(Relationship instance)
    {
        /*
         * The cocoMDS6 server is linked to the manufacturing IOT devices as well as the core cohort
         * and so it does not save reference copies so that the IOT metadata does not clutter the cocoMDS6 repository
         * (and hence becomes visible in the core cohort.
         */
        if ("cocoMDS6".equals(serverName))
        {
            return false;
        }

        return true;
    }
}
