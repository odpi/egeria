/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;


import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * CocoPharmaServerSecurityConnector provides a specific security connector for Coco Pharmaceuticals
 * users that overrides the default behavior of that open metadata security connector that does
 * not allow any access to anything.
 */
public class CocoPharmaServerSecurityConnector extends OpenMetadataServerSecurityConnector
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

    private Map<String, List<String>> zoneAccess   = new HashMap<>();

    /*
     * Zones requiring special processing
     */
    private final String personalFilesZoneName   = "personal-files";
    private final String quarantineZoneName      = "quarantine";
    private final String dataLakeZoneName        = "data-lake";


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
        final String clinicalTrialsZoneName  = "clinical-trials";
        final String hrZoneName              = "human-resources";
        final String financeZoneName         = "finance";
        final String infrastructureZoneName  = "infrastructure";
        final String developmentZoneName     = "development";
        final String manufacturingZoneName   = "manufacturing";
        final String governanceZoneName      = "governance";
        final String trashCanZoneName        = "trash-can";

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
        final String desSignaUserId       = "designa";
        final String angelaCummingUserId  = "angelacummings";
        final String julesKeeperUserId    = "jukeskeeper";
        final String stewFasterUserId     = "stewFaster";

        final String archiverUserId    = "archiver01";
        final String etlEngineUserId   = "dlETL";

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
        assetOnboardingExit.add(erinOverviewUserId);

        serverAdmins.add(garyGeekeUserId);
        serverOperators.add(garyGeekeUserId);
        serverInvestigators.add(garyGeekeUserId);

        metadataArchitects.add(erinOverviewUserId);
        metadataArchitects.add(peterProfileUserId);

        npaAccounts.add(archiverUserId);
        npaAccounts.add(etlEngineUserId);

        List<String> zoneSetUp = new ArrayList<>();

        /*
         * Set up the zones
         */
        zoneAccess.put(trashCanZoneName, npaAccounts);
        zoneAccess.put(personalFilesZoneName, allEmployees);
        zoneAccess.put(quarantineZoneName, assetOnboarding);
        zoneAccess.put(dataLakeZoneName, allEmployees);

        zoneSetUp.add(callieQuartileUserId);
        zoneSetUp.add(tessaTubeUserId);

        zoneAccess.put(researchZoneName, zoneSetUp);

        zoneSetUp.add(callieQuartileUserId);
        zoneSetUp.add(tessaTubeUserId);
        zoneSetUp.add(grantAbleUserId);
        zoneSetUp.add(julieStitchedUserId);
        zoneSetUp.add(angelaCummingUserId);

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
        zoneSetUp.add(erinOverviewUserId);
        zoneSetUp.add(peterProfileUserId);

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
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        if (allUsers.contains(userId))
        {
            return;
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
     * Tests for whether a specific user should have access to an assets based on its zones.
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
            testZones = new ArrayList<>();
            testZones.add(quarantineZoneName);
        }
        else
        {
            testZones = assetZones;
        }

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
                 * If the asset is in the quarantine zone then there is a restricted list of people that can change
                 * the zone membership.  Other people may change the asset's zones if they have permission
                 * via another one of the asset's zones.
                 */
                else if ((zoneName.equals(quarantineZoneName)) && (zoneMembershipChanged))
                {
                    if (assetOnboardingExit.contains(userId))
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

        return false;
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
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     */
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
     * @param newAsset new asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    public void  validateUserForAssetDetailUpdate(String     userId,
                                                  Asset      originalAsset,
                                                  Asset      newAsset) throws UserNotAuthorizedException
    {
        if ((originalAsset != null) && (newAsset != null))
        {
            if (userHasAccessToAssetZones(userId,
                                          originalAsset.getZoneMembership(),
                                          true,
                                          this.hasZoneChanged(originalAsset.getZoneMembership(), newAsset.getZoneMembership()),
                                          this.isUserAssetOwner(userId, originalAsset)))
            {
                return;
            }
        }

        /*
         * Any other conditions, use superclass to throw user not authorized exception
         */
        super.validateUserForAssetDetailUpdate(userId, originalAsset, newAsset);
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an asset such as schema and connections.
     *
     * @param userId identifier of user
     * @param asset original asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
    public void  validateUserForAssetAttachmentUpdate(String     userId,
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
        super.validateUserForAssetAttachmentUpdate(userId, asset);
    }


    /**
     * Tests for whether a specific user should have the right to delete an asset within a zone.
     *
     * @param userId identifier of user
     * @param asset asset details
     * @throws UserNotAuthorizedException the user is not authorized to change this asset
     */
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
     * Tests for whether a specific user should have the right to create a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
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
     * Tests for whether a specific user should have read access to a specific typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
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
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    public void  validateUserForTypeUpdate(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        super.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef);
    }


    /**
     * Tests for whether a specific user should have the right to delete a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef typeDef details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (metadataArchitects.contains(userId))
        {
            return;
        }

        super.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
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
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForEntityCreate(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
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
    public void  validateUserForEntityRead(String          userId,
                                           String          metadataCollectionName,
                                           EntityDetail    instance) throws UserNotAuthorizedException
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
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classification classification details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForEntityClassificationUpdate(String          userId,
                                                           String          metadataCollectionName,
                                                           EntityDetail    instance,
                                                           Classification  classification) throws UserNotAuthorizedException
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
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
    }


    /**
     * Tests for whether a specific user should have the right to create a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public void  validateUserForRelationshipCreate(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
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
    public void  validateUserForRelationshipRead(String          userId,
                                                 String          metadataCollectionName,
                                                 Relationship    instance) throws UserNotAuthorizedException
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
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
    }
}
