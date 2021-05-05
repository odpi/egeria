/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.leadership;

import org.odpi.openmetadata.accessservices.communityprofile.client.PersonalProfileManagement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.PersonalProfileElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelement.UserIdentityElement;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceProgramLeadership;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceOfficerHistory;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.http.HttpHelper;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GovernanceLeadershipSample provides a client program that calls the Governance Program OMAS.
 */
public class GovernanceLeadershipSample
{
    private static final String cdoAppointmentId           = "EXEC-CDO";
    private static final String cpoAppointmentId           = "EXEC-CPO";
    private static final String csoAppointmentId           = "EXEC-CSO";
    private static final String corpAppointmentId          = "EXEC-CORP-GOV";
    private static final String cdoForITAppointmentId      = "CHIEF-INFO-ARCH";
    private static final String infraGovForITAppointmentId = "CHIEF-SYS-ARCH";
    private static final String projLeadForITAppointmentId = "CHIEF-SW-PROJ-MGR";

    private static final String julesKeeperEmpNo  = "026";
    private static final String ivorPadlockEmpNo  = "008";
    private static final String faithBrokerEmpNo  = "010";
    private static final String erinOverviewEmpNo = "013";
    private static final String garyGeekeEmpNo    = "015";
    private static final String pollyTaskerEmpNo  = "005";
    private static final String reggieMintEmpNo   = "018";


    private static final String julesKeeperUserId  = "julesKeeper";
    private static final String ivorPadlockUserId  = "ivorPadlock";
    private static final String faithBrokerUserId  = "faithBroker";
    private static final String erinOverviewUserId = "erinOverview";
    private static final String garyGeekeUserId    = "garyGeeke";
    private static final String pollyTaskerUserId  = "pollyTasker";
    private static final String reggieMintUserId   = "reggieMint";


    /*
     * These are the names of the properties that are not represented in the open metadata types
     * and so they are stored as additional properties.
     */
    private static final String   workLocationPropertyName = "WorkLocation";
    private static final String   contactTypePropertyName  = "ContactType";

    private String serverName;
    private String serverURLRoot;
    private String clientUserId;



    /**
     * Constructor
     *
     * @param serverName server to connect to.
     * @param serverURLRoot server location to connect to.
     * @param clientUserId user id to use to access metadata.
     */
    private GovernanceLeadershipSample(String  serverName, String serverURLRoot, String clientUserId)
    {
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * Extract and print out a personal profile
     *
     * @param client the client to call.
     * @param clientUserId the user id to use on the call.
     * @param guid the unique identifier for the personal profile.
     * @throws InvalidParameterException the guid is not recognized.
     * @throws PropertyServerException the property server is not available.
     * @throws UserNotAuthorizedException the user id is not authorized to access the personal profile.
     */
    private void  printPersonalProfile(PersonalProfileManagement     client,
                                       String                        clientUserId,
                                       String                        guid) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        PersonalProfileElement profile = client.getPersonalProfileByGUID(clientUserId, guid);

        System.out.println("----------------------------");
        System.out.println("Profile: " + guid);

        List<UserIdentityElement> userIdentities = profile.getUserIdentities();
        if (userIdentities == null)
        {
            System.out.println("  UserId: null <ERROR>");
        }
        else if (userIdentities.size() == 0)
        {
            System.out.println("  UserIds: empty <ERROR>");
        }
        else if (userIdentities.size() == 1)
        {
            UserIdentityElement   userIdentity = userIdentities.get(0);
            if (userIdentity == null)
            {
                System.out.println("  UserId: empty <ERROR>");
            }
            else
            {
                System.out.println("  UserId: " + userIdentity.getProperties().getQualifiedName());
            }
        }
        else
        {
            System.out.print("  UserIds:");
            for (UserIdentityElement  userIdentity : userIdentities)
            {
                if (userIdentity == null)
                {
                    System.out.print(" <empty ERROR>");
                }
                else
                {
                    System.out.print(" " + userIdentity.getProperties().getQualifiedName());
                }
            }
            System.out.println();
        }

        System.out.println("  Employee Id: " + profile.getPersonalProfileProperties().getQualifiedName());
        System.out.println("  Full Name: " + profile.getPersonalProfileProperties().getFullName());
        System.out.println("  Known Name: " + profile.getPersonalProfileProperties().getKnownName());
        System.out.println("  Job Title: " + profile.getPersonalProfileProperties().getJobTitle());
        System.out.println("  Job Description: " + profile.getPersonalProfileProperties().getJobTitle());

        if (profile.getPersonalProfileProperties().getAdditionalProperties() != null)
        {
            System.out.println("  Work Location: " + profile.getPersonalProfileProperties().getAdditionalProperties().get(workLocationPropertyName));
            System.out.println("  Contact Type: " + profile.getPersonalProfileProperties().getAdditionalProperties().get(contactTypePropertyName));
        }

        System.out.println("----------------------------");
    }


    /**
     * Extract and print out a governance office
     *
     * @param client the client to call.
     * @param clientUserId the user id to use on the call.
     * @param guid the unique identifier for the governance officer.
     * @throws InvalidParameterException the guid is not recognized.
     * @throws PropertyServerException the property server is not available.
     * @throws UserNotAuthorizedException the user id is not authorized to access the personal profile.
     */
    private void  printGovernanceOfficer(GovernanceProgramLeadership     client,
                                         String                          clientUserId,
                                         String                          guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        GovernanceOfficerHistory    governanceOfficerElement = client.getGovernanceOfficerHistoryByGUID(clientUserId, guid);
        GovernanceOfficerProperties governanceOfficer        = governanceOfficerElement.getRole();

        System.out.println("----------------------------");
        System.out.println("Governance Officer: " + guid);
        System.out.println("  Domain: " + governanceOfficer.getDomainIdentifier());
        System.out.println("  Appointment Id: " + governanceOfficer.getRoleId());
        System.out.println("  Appointment Scope: " + governanceOfficer.getScope());
        System.out.println("  Title: " + governanceOfficer.getTitle());

        GovernanceAppointee appointee = governanceOfficerElement.getAppointee();

        if (appointee != null)
        {
            System.out.println("  Appointee: " + appointee.getProfile().getProfileProperties().getKnownName());
            System.out.println("  Start Date: " + appointee.getStartDate());
            System.out.println("  End Date: " + appointee.getEndDate());
        }
        else
        {
            System.out.println("  Appointee: <None> ");
        }
        System.out.println("----------------------------");
    }



    private void run() throws InvalidParameterException,
                              PropertyServerException,
                              UserNotAuthorizedException
    {
        PersonalProfileManagement   ppmClient  = new PersonalProfileManagement(serverName, serverURLRoot);
        GovernanceProgramLeadership gplClient = new GovernanceProgramLeadership(serverName, serverURLRoot);

        GovernanceOfficerProperties governanceOfficerProperties;

        System.out.println("Creating profiles for Jules and Ivor");

        Map<String, String>  julesAdditionalProperties = new HashMap<>();
        julesAdditionalProperties.put(workLocationPropertyName, "2");
        julesAdditionalProperties.put(contactTypePropertyName, "Employee");

        String julesKeeperProfileGUID = ppmClient.createPersonalProfile(clientUserId,
                                                                        null,
                                                                        null,
                                                                        julesKeeperUserId,
                                                                        julesKeeperEmpNo,
                                                                        "Julian Keeper",
                                                                        "Jules Keeper",
                                                                        "Chief Data and Privacy Officer",
                                                                        "Ensuring CocoP makes the best use of data.",
                                                                        julesAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, julesKeeperProfileGUID);


        Map<String, String>  ivorAdditionalProperties = new HashMap<>();
        ivorAdditionalProperties.put(workLocationPropertyName, "1");
        ivorAdditionalProperties.put(contactTypePropertyName, "Employee");

        String ivorPadlockProfileGUID  = ppmClient.createPersonalProfile(clientUserId,
                                                                         null,
                                                                         null,
                                                                         ivorPadlockUserId,
                                                                         ivorPadlockEmpNo,
                                                                         null,  /* optional property */
                                                                         "Ivor Padlock",
                                                                         "Security Executive",
                                                                         "Manages security for Coco Pharmaceuticals.",
                                                                         ivorAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, ivorPadlockProfileGUID);

        System.out.println("Creating CDO, CPO and CSO governance officers");

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceOfficerProperties.setRoleId(cdoAppointmentId);
        governanceOfficerProperties.setTitle("Chief Data Officer (CDO)");

        String cdoGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        this.printGovernanceOfficer(gplClient, clientUserId, cdoGUID);

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.PRIVACY.getOrdinal());
        governanceOfficerProperties.setRoleId(cpoAppointmentId);
        governanceOfficerProperties.setTitle("Chief Privacy Officer (CPO)");

        String cpoGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        this.printGovernanceOfficer(gplClient, clientUserId, cpoGUID);

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.SECURITY.getOrdinal());
        governanceOfficerProperties.setRoleId(csoAppointmentId);
        governanceOfficerProperties.setTitle("Chief Security Officer (CSO)");

        String csoGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        this.printGovernanceOfficer(gplClient, clientUserId, csoGUID);

        List<GovernanceOfficerElement> governanceOfficers = gplClient.getGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " governance officers");

        System.out.println("Appointing CDO, CPO and CSO governance officers");

        gplClient.appointGovernanceOfficer(clientUserId,
                                           cdoGUID,
                                           julesKeeperProfileGUID,
                                           null);

        this.printGovernanceOfficer(gplClient, clientUserId, cdoGUID);

        gplClient.appointGovernanceOfficer(clientUserId,
                                           cpoGUID,
                                           julesKeeperProfileGUID,
                                           null);

        this.printGovernanceOfficer(gplClient, clientUserId, cpoGUID);

        gplClient.appointGovernanceOfficer(clientUserId,
                                           csoGUID,
                                           ivorPadlockProfileGUID,
                                           null);

        this.printGovernanceOfficer(gplClient, clientUserId, csoGUID);

        System.out.println("Changing CPO to Faith");

        Map<String, String>  faithAdditionalProperties = new HashMap<>();
        faithAdditionalProperties.put(workLocationPropertyName, "1");
        faithAdditionalProperties.put(contactTypePropertyName, "Employee");

        String faithBrokerProfileGUID  = ppmClient.createPersonalProfile(clientUserId,
                                                                         null,
                                                                         null,
                                                                         faithBrokerUserId,
                                                                         faithBrokerEmpNo,
                                                                         "Faith Charity Broker",
                                                                         "Faith Broker",
                                                                         "Human Resources Director",
                                                                         "Providing support to Coco Pharmaceutical employees.",
                                                                         faithAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, faithBrokerProfileGUID);

        long   handoverTime = new Date().getTime();
        Date   handoverDate = new Date(handoverTime + 100);

        gplClient.relieveGovernanceOfficer(clientUserId, cpoGUID, julesKeeperProfileGUID, handoverDate);

        this.printGovernanceOfficer(gplClient, clientUserId, cpoGUID);

        gplClient.appointGovernanceOfficer(clientUserId,
                                           cpoGUID,
                                           faithBrokerProfileGUID,
                                           handoverDate);

        this.printGovernanceOfficer(gplClient, clientUserId, cpoGUID);

        governanceOfficers = gplClient.getGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " governance officers");

        List<GovernanceOfficerAppointee> governanceOfficerAppointees = gplClient.getActiveGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " active governance officers");

        for (GovernanceOfficerAppointee governanceOfficer : governanceOfficerAppointees)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getProfileProperties().getKnownName() + " is the " + governanceOfficer.getRole().getRoleId());
        }

        governanceOfficerAppointees = gplClient.getGovernanceOfficersByDomain(clientUserId, GovernanceDomain.PRIVACY.getOrdinal());

        System.out.println(governanceOfficers.size() + " privacy governance officers");

        for (GovernanceOfficerAppointee governanceOfficer : governanceOfficerAppointees)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getProfileProperties().getKnownName() +
                                       " is the privacy officer from " + governanceOfficer.getAppointee().getStartDate()
                                       + " to " + governanceOfficer.getAppointee().getEndDate());
        }

        /*
         * Sleep so that Faith's appointment becomes active.
         */
        try
        {
            System.out.println("Sleeping ... until after Faith's appointment becomes active");
            Thread.sleep(15);
        }
        catch (InterruptedException exc)
        {
            System.out.println("Sleep interrupted");
        }

        governanceOfficerAppointees = gplClient.getActiveGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " active governance officers");

        for (GovernanceOfficerAppointee governanceOfficer : governanceOfficerAppointees)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getProfileProperties().getKnownName() + " is the " + governanceOfficer.getRole().getTitle());
        }

        /*
         * Update Jule's job title
         */
        ppmClient.updatePersonalProfile(clientUserId,
                                        null,
                                        null,
                                        julesKeeperProfileGUID,
                                        julesKeeperEmpNo,
                                        "Julian Keeper",
                                        "Jules Keeper",
                                        "Chief Data Officer",
                                        "Ensuring CocoP makes the best use of data.",
                                        null,
                                        julesAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, julesKeeperProfileGUID);

        /*
         * Appointing Erin Overview as the CDO for IT
         */
        System.out.println("Creating profile for Erin");

        Map<String, String>  erinAdditionalProperties = new HashMap<>();
        erinAdditionalProperties.put(workLocationPropertyName, "2");
        erinAdditionalProperties.put(contactTypePropertyName, "Employee");
        String erinOverviewProfileGUID  = ppmClient.createPersonalProfile(clientUserId,
                                                                          null,
                                                                          null,
                                                                          erinOverviewUserId,
                                                                          erinOverviewEmpNo,
                                                                          null,  /* optional property */
                                                                          "Erin Overview",
                                                                          "Information Architect",
                                                                          "Manages all information architecture and standards for Coco Pharmaceuticals IT systems.",
                                                                          erinAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, erinOverviewProfileGUID);

        System.out.println("Creating CDO for IT governance officer");

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceOfficerProperties.setRoleId(cdoForITAppointmentId);
        governanceOfficerProperties.setScope("IT Systems");
        governanceOfficerProperties.setTitle("Chief Data Officer (CDO) for IT");

        String cdoForITGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        System.out.println("Appointing Erin as CDO for IT");

        gplClient.appointGovernanceOfficer(clientUserId,
                                           cdoForITGUID,
                                           erinOverviewProfileGUID,
                                           new Date());

        this.printGovernanceOfficer(gplClient, clientUserId, cdoForITGUID);


        /*
         * Appointing Gary Geeke as as the governance officer for IT infrastructure.
         */
        System.out.println("Creating profile for Gary");

        Map<String, String>  garyAdditionalProperties = new HashMap<>();
        garyAdditionalProperties.put(workLocationPropertyName, "1");
        garyAdditionalProperties.put(contactTypePropertyName, "Employee");
        String garyGeekeProfileGUID  = ppmClient.createPersonalProfile(clientUserId,
                                                                       null,
                                                                       null,
                                                                       garyGeekeUserId,
                                                                       garyGeekeEmpNo,
                                                                       null,  /* optional property */
                                                                       "Gary Geeke",
                                                                       "Infrastructure Architect",
                                                                       "Manages all the IT infrastructure for Coco Pharmaceuticals.",
                                                                       garyAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, garyGeekeProfileGUID);
        System.out.println("Creating IT governance officer");

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal());
        governanceOfficerProperties.setRoleId(infraGovForITAppointmentId);
        governanceOfficerProperties.setTitle("Chief Infrastructure Architect");

        String infraGovForITGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        System.out.println("Appointing Gary as gov officer for IT");

        gplClient.appointGovernanceOfficer(clientUserId,
                                           infraGovForITGUID,
                                           garyGeekeProfileGUID,
                                           null);

        this.printGovernanceOfficer(gplClient, clientUserId, infraGovForITGUID);


        /*
         * Appointing Polly Tasker as as the governance officer for software development.
         */
        System.out.println("Creating profile for Polly");

        Map<String, String>  pollyAdditionalProperties = new HashMap<>();
        pollyAdditionalProperties.put(workLocationPropertyName, "1");
        pollyAdditionalProperties.put(contactTypePropertyName, "Employee");
        String pollyTaskerProfileGUID  = ppmClient.createPersonalProfile(clientUserId,
                                                                         null,
                                                                         null,
                                                                         pollyTaskerUserId,
                                                                         pollyTaskerEmpNo,
                                                                         null,  /* optional property */
                                                                         "Polly Tasker",
                                                                         "Lead Project Manager for IT",
                                                                         "Manages IT projects for Coco Pharmaceuticals.",
                                                                         pollyAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating SDLC governance officer");

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.SOFTWARE_DEVELOPMENT.getOrdinal());
        governanceOfficerProperties.setRoleId(projLeadForITAppointmentId);
        governanceOfficerProperties.setTitle("Chief Project Lead for Software");

        String projLeadForITGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        System.out.println("Appointing Polly as gov officer for Software Development");

        gplClient.appointGovernanceOfficer(clientUserId,
                                           projLeadForITGUID,
                                           pollyTaskerProfileGUID,
                                           null);

        this.printGovernanceOfficer(gplClient, clientUserId, projLeadForITGUID);

        /*
         * Appointing Reggie Mint as as the corporate governance officer.
         */
        System.out.println("Creating profile for Reggie");

        Map<String, String>  reggieAdditionalProperties = new HashMap<>();
        reggieAdditionalProperties.put(workLocationPropertyName, "1");
        reggieAdditionalProperties.put(contactTypePropertyName, "Employee");
        String reggieMintProfileGUID  = ppmClient.createPersonalProfile(clientUserId,
                                                                        null,
                                                                        null,
                                                                        reggieMintUserId,
                                                                        reggieMintEmpNo,
                                                                        "Reginald S P Mint",  /* optional property */
                                                                        "Reggie Mint",
                                                                        "Chief Finance Officer",
                                                                        "Manages finance for Coco Pharmaceuticals.",
                                                                        reggieAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating corporate governance officer");

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setDomainIdentifier(GovernanceDomain.CORPORATE.getOrdinal());
        governanceOfficerProperties.setRoleId(corpAppointmentId);
        governanceOfficerProperties.setTitle("Corporate Governance Officer");

        String corpGUID  = gplClient.createGovernanceOfficer(clientUserId, governanceOfficerProperties);

        System.out.println("Appointing Reggie as corporate gov officer");

        gplClient.appointGovernanceOfficer(clientUserId,
                                           corpGUID,
                                           reggieMintProfileGUID,
                                           null);

        this.printGovernanceOfficer(gplClient, clientUserId, corpGUID);



        System.out.println("Update the CSO to the CISO");

        governanceOfficerProperties = new GovernanceOfficerProperties();

        governanceOfficerProperties.setTitle("Chief Information Security Officer (CISO)");

        gplClient.updateGovernanceOfficer(clientUserId,
                                          csoGUID,
                                          true,
                                          governanceOfficerProperties);

        governanceOfficers = gplClient.getGovernanceOfficers(clientUserId);

        /*
         * See the team
         */
        System.out.println(governanceOfficers.size() + " governance officers");

        for (GovernanceOfficerAppointee governanceOfficer : governanceOfficerAppointees)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getProfileProperties().getKnownName() + " is the " + governanceOfficer.getRole().getTitle());
        }

        System.out.println("Deleting all profiles and governance officers");

        /*
         * Delete all of the governance officers
         */
        gplClient.deleteGovernanceOfficer(clientUserId, cdoGUID);
        gplClient.deleteGovernanceOfficer(clientUserId, cdoForITGUID);
        gplClient.deleteGovernanceOfficer(clientUserId, cpoGUID);
        gplClient.deleteGovernanceOfficer(clientUserId, csoGUID);
        gplClient.deleteGovernanceOfficer(clientUserId, corpGUID);
        gplClient.deleteGovernanceOfficer(clientUserId, projLeadForITGUID);
        gplClient.deleteGovernanceOfficer(clientUserId, infraGovForITGUID);


        /*
         * Delete all of the personal profiles
         */
        ppmClient.deletePersonalProfile(clientUserId, null, null, julesKeeperProfileGUID, julesKeeperEmpNo);
        ppmClient.deletePersonalProfile(clientUserId, null, null, ivorPadlockProfileGUID, ivorPadlockEmpNo);
        ppmClient.deletePersonalProfile(clientUserId, null, null, faithBrokerProfileGUID, faithBrokerEmpNo);
        ppmClient.deletePersonalProfile(clientUserId, null, null, erinOverviewProfileGUID, erinOverviewEmpNo);
        ppmClient.deletePersonalProfile(clientUserId, null, null, garyGeekeProfileGUID, garyGeekeEmpNo);
        ppmClient.deletePersonalProfile(clientUserId, null, null, reggieMintProfileGUID, reggieMintEmpNo);
        ppmClient.deletePersonalProfile(clientUserId, null, null, pollyTaskerProfileGUID, pollyTaskerEmpNo);

        /*
         * Sleep so that the deletes propagate throughout the cohort.
         */
        try
        {
            System.out.println("Sleeping ... to allow deletes to propagate");
            Thread.sleep(5000);
        }
        catch (InterruptedException exc)
        {
            System.out.println("Sleep interrupted");
        }

        /*
         * Should be all gone
         */
        governanceOfficers = gplClient.getGovernanceOfficers(clientUserId);

        if (governanceOfficers != null)
        {
            System.out.println(governanceOfficers.size() + " governance officers");

            for (GovernanceOfficerElement governanceOfficer : governanceOfficers)
            {
                System.out.println(governanceOfficer.toString() + " is still defined");
            }
            System.exit(-1);
        }
        else
        {
            System.out.println("All governance officers gone");
        }
    }



    /**
     * The main program takes the URL root for the server.
     *
     * @param args 1. server name, 2. URL root for the server, 3. client userId
     */
    public static void main(String[] args)
    {
        String  serverName;
        String  serverURLRoot;
        String  clientUserId;

        if ((args == null) || (args.length < 3))
        {
            System.out.println("Please specify the server's name in the first parameter, " +
                                       "the server's platform URL root in the second parameter " +
                                       "and the caller's userId in the third parameter");
            System.exit(-1);
        }

        serverName = args[0];
        serverURLRoot = args[1];
        clientUserId = args[2];

        HttpHelper.noStrictSSLIfConfigured();


        System.out.println("===============================");
        System.out.println("Governance Leadership Sample   ");
        System.out.println("===============================");
        System.out.println("Running against server: " + serverName + " at " + serverURLRoot);
        System.out.println("Using userId: " + clientUserId);


        try
        {
            GovernanceLeadershipSample   sample = new GovernanceLeadershipSample(serverName, serverURLRoot, clientUserId);

            sample.run();
        }
        catch (Throwable  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
