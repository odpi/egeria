/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.samples;

import java.util.ArrayList;
import java.util.List;

public class CocoPharmaServerSecurityConnector {
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
    List<String> allEmployees = new ArrayList<>();
    List<String> assetOnboarding = new ArrayList<>();
    List<String> allUsers = new ArrayList<>();
    List<String> npaAccounts = new ArrayList<>();
    List<String> serverAdmins = new ArrayList<>();
    List<String> assetOnboardingExit = new ArrayList<>();
    List<String> serverOperators = new ArrayList<>();
    List<String> serverInvestigators = new ArrayList<>();
    List<String> metadataArchitects = new ArrayList<>();
    List<String> externalUsers = new ArrayList<>();

    public void addUsersToAllUsersGroup() {
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
    }

    public void addUsersToAllEmployeesGroup() {
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
    }

    public void addUsersToAssetOnboardingGroup() {
        assetOnboarding.add(peterProfileUserId);
        assetOnboarding.add(erinOverviewUserId);
        assetOnboarding.add(findItDL01UserId);
        assetOnboarding.add(fixItDL01UserId);
    }

    public void addUsersToAssetOnboardingExitGroup() {
        assetOnboardingExit.add(erinOverviewUserId);
    }

    public void addUsersToServerAdminsGroup() {
        serverAdmins.add(garyGeekeUserId);
    }

    public void addUsersToServerOperatorsGroup() {
        serverOperators.add(garyGeekeUserId);
    }

    public void addUsersToServerInvestigatorsGroup() {
        serverInvestigators.add(garyGeekeUserId);
    }

    public void addUsersToMetadataArchitectsGroup() {
        metadataArchitects.add(erinOverviewUserId);
        metadataArchitects.add(peterProfileUserId);
    }

    public void addUsersToExternalUsersGroup() {
        externalUsers.add(grantAbleUserId);
        externalUsers.add(julieStitchedUserId);
        externalUsers.add(angelaCummingUserId);
        externalUsers.add(robbieRecordsUserId);
    }

    public void addUsersToNpaAccountsGroup() {
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
    }
}
