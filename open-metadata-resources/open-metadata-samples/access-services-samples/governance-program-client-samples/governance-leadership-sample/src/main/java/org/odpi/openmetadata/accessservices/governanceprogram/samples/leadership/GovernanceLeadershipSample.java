/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.leadership;

import org.odpi.openmetadata.accessservices.communityprofile.client.PersonalProfileManagement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.PersonalProfileUniverse;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.ProfileIdentityElement;
import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceRoleManager;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceRoleHistory;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceRoleProperties;
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
     * These are the names of the properties that are not represented in the open metadata types,
     * and so they are stored as additional properties.
     */
    private static final String   workLocationPropertyName = "WorkLocation";
    private static final String   contactTypePropertyName  = "ContactType";

    private final String serverName;
    private final String serverURLRoot;
    private final String clientUserId;



    /**
     * Constructor
     *
     * @param serverName server to connect to.
     * @param serverURLRoot server location to connect to.
     * @param clientUserId user id to use to access metadata.
     */
    public GovernanceLeadershipSample(String  serverName, String serverURLRoot, String clientUserId)
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
        PersonalProfileUniverse profile = client.getPersonalProfileByGUID(clientUserId, guid);

        System.out.println("----------------------------");
        System.out.println("Profile: " + guid);

        List<ProfileIdentityElement> userIdentities = profile.getUserIdentities();
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
            ProfileIdentityElement firstUser = userIdentities.get(0);
            UserIdentityElement    userIdentity = firstUser.getProperties();
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
            for (ProfileIdentityElement  userIdentity : userIdentities)
            {
                if (userIdentity == null)
                {
                    System.out.print(" <empty ERROR>");
                }
                else
                {
                    System.out.print(" " + userIdentity.getProperties().getProperties().getQualifiedName());
                }
            }
            System.out.println();
        }

        System.out.println("  Employee Id: " + profile.getProfileProperties().getQualifiedName());
        System.out.println("  Full Name: " + profile.getProfileProperties().getFullName());
        System.out.println("  Known Name: " + profile.getProfileProperties().getKnownName());
        System.out.println("  Job Title: " + profile.getProfileProperties().getJobTitle());
        System.out.println("  Job Description: " + profile.getProfileProperties().getJobTitle());

        if (profile.getProfileProperties().getAdditionalProperties() != null)
        {
            System.out.println("  Work Location: " + profile.getProfileProperties().getAdditionalProperties().get(workLocationPropertyName));
            System.out.println("  Contact Type: " + profile.getProfileProperties().getAdditionalProperties().get(contactTypePropertyName));
        }

        System.out.println("----------------------------");
    }


    /**
     * Extract and print out a governance officer
     *
     * @param client the client to call.
     * @param clientUserId the user id to use on the call.
     * @param guid the unique identifier for the governance officer.
     * @throws InvalidParameterException the guid is not recognized.
     * @throws PropertyServerException the property server is not available.
     * @throws UserNotAuthorizedException the user id is not authorized to access the personal profile.
     */
    private void  printGovernanceRole(GovernanceRoleManager client,
                                      String                clientUserId,
                                      String                guid) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        GovernanceRoleHistory    governanceRoleElement = client.getGovernanceRoleHistoryByGUID(clientUserId, guid);

        printGovernanceRoleHistory(governanceRoleElement);
    }


    /**
     * Extract and print out a governance officer and everyone who has been appointed to that role
     *
     * @param governanceRoleElement details to print out
     */
    private void  printGovernanceRoleHistory(GovernanceRoleHistory governanceRoleElement)
    {
        printGovernanceRole(governanceRoleElement);

        System.out.println("  -- Current Appointees-----");

        printGovernanceAppointees(governanceRoleElement.getCurrentAppointees());

        System.out.println("  -- Predecessors ----------");

        printGovernanceAppointees(governanceRoleElement.getPredecessors());

        System.out.println("  -- Planned Successors-----");

        printGovernanceAppointees(governanceRoleElement.getSuccessors());

        System.out.println("----------------------------");
    }



    /**
     * Extract and print out a governance officer and everyone who has been appointed to that role
     *
     * @param governanceRoleElement details to print out
     */
    private void  printGovernanceRoleAppointee(GovernanceRoleAppointee governanceRoleElement)
    {
        printGovernanceRole(governanceRoleElement);

        printGovernanceAppointees(governanceRoleElement.getCurrentAppointees());
    }


    /**
     * Extract and print out a governance officer
     *
     * @param governanceRoleElement details to print out
     */
    private void  printGovernanceRole(GovernanceRoleElement governanceRoleElement)
    {
        GovernanceRoleProperties governanceRole = governanceRoleElement.getRole();

        System.out.println("----------------------------");
        System.out.println("Governance Role: " + governanceRoleElement.getElementHeader().getGUID());
        if (governanceRole != null)
        {
            System.out.println("  Domain: " + governanceRole.getDomainIdentifier());
            System.out.println("  Appointment Id: " + governanceRole.getRoleId());
            System.out.println("  Appointment Scope: " + governanceRole.getScope());
            System.out.println("  Title: " + governanceRole.getTitle());
        }
        else
        {
            System.out.println("  <no properties>");
        }
    }


    /**
     * Extract and print out a governance officer
     *
     * @param appointees retrieved appointees
     */
    private void printGovernanceAppointees(List<GovernanceAppointee> appointees)
    {
        if (appointees == null)
        {
            System.out.println("  Appointee: <None> ");
        }
        else
        {
            for (GovernanceAppointee appointee : appointees)
            {
                if (appointee != null)
                {
                    System.out.println("  Appointee: " + appointee.getProfile().getProfileProperties().getKnownName());
                    System.out.println("     Start Date: " + appointee.getStartDate());
                    System.out.println("     End Date: " + appointee.getEndDate());
                }
                else
                {
                    System.out.println("  Appointee: <Null> ");
                }
            }
        }
        System.out.println("----------------------------");
    }


    public void run() throws InvalidParameterException,
                             PropertyServerException,
                             UserNotAuthorizedException
    {
        PersonalProfileManagement ppmClient = new PersonalProfileManagement(serverName, serverURLRoot);
        GovernanceRoleManager     gplClient = new GovernanceRoleManager(serverName, serverURLRoot);

        GovernanceRoleProperties governanceRoleProperties;

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
                                                                        "Chief Data and Privacy Role",
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

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cdoAppointmentId);
        governanceRoleProperties.setRoleId(cdoAppointmentId);
        governanceRoleProperties.setTitle("Chief Data Role (CDO)");

        String cdoGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        this.printGovernanceRole(gplClient, clientUserId, cdoGUID);

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.PRIVACY.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cpoAppointmentId);
        governanceRoleProperties.setRoleId(cpoAppointmentId);
        governanceRoleProperties.setTitle("Chief Privacy Role (CPO)");

        String cpoGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        this.printGovernanceRole(gplClient, clientUserId, cpoGUID);

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.SECURITY.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + csoAppointmentId);
        governanceRoleProperties.setRoleId(csoAppointmentId);
        governanceRoleProperties.setTitle("Chief Security Role (CSO)");

        String csoGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        this.printGovernanceRole(gplClient, clientUserId, csoGUID);

        List<GovernanceRoleElement> governanceRoles = gplClient.getGovernanceRolesByDomainId(clientUserId, 0, 0 , 0);

        System.out.println(governanceRoles.size() + " governance officers");

        System.out.println("Appointing CDO, CPO and CSO governance officers");

        gplClient.appointGovernanceRole(clientUserId,
                                           cdoGUID,
                                           julesKeeperProfileGUID,
                                           null);

        this.printGovernanceRole(gplClient, clientUserId, cdoGUID);

        String julesAsCPOAppointmentId = gplClient.appointGovernanceRole(clientUserId,
                                                                         cpoGUID,
                                                                         julesKeeperProfileGUID,
                                                                         null);

        this.printGovernanceRole(gplClient, clientUserId, cpoGUID);

        gplClient.appointGovernanceRole(clientUserId,
                                           csoGUID,
                                           ivorPadlockProfileGUID,
                                           null);

        this.printGovernanceRole(gplClient, clientUserId, csoGUID);

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

        gplClient.relieveGovernanceRole(clientUserId, cpoGUID, julesKeeperProfileGUID, julesAsCPOAppointmentId, handoverDate);

        this.printGovernanceRole(gplClient, clientUserId, cpoGUID);

        gplClient.appointGovernanceRole(clientUserId,
                                           cpoGUID,
                                           faithBrokerProfileGUID,
                                           handoverDate);

        this.printGovernanceRole(gplClient, clientUserId, cpoGUID);

        governanceRoles = gplClient.getGovernanceRolesByDomainId(clientUserId, 0, 0 , 0);

        System.out.println(governanceRoles.size() + " governance officers");

        List<GovernanceRoleAppointee> governanceRoleAppointees = gplClient.getCurrentGovernanceRoleAppointments(clientUserId, 0, 0, 0);

        System.out.println(governanceRoleAppointees.size() + " active governance officers\n");

        for (GovernanceRoleAppointee governanceRole : governanceRoleAppointees)
        {
            printGovernanceRoleAppointee(governanceRole);
        }

        governanceRoleAppointees = gplClient.getCurrentGovernanceRoleAppointments(clientUserId, GovernanceDomain.PRIVACY.getOrdinal(), 0, 0);

        System.out.println(governanceRoles.size() + " privacy governance officers");

        for (GovernanceRoleAppointee governanceRole : governanceRoleAppointees)
        {
            printGovernanceRoleAppointee(governanceRole);
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

        governanceRoleAppointees = gplClient.getCurrentGovernanceRoleAppointments(clientUserId, 0 ,0, 0);

        System.out.println(governanceRoles.size() + " active governance officers");

        for (GovernanceRoleAppointee governanceRole : governanceRoleAppointees)
        {
            printGovernanceRoleAppointee(governanceRole);
        }

        /*
         * Update Jules' job title
         */
        ppmClient.updatePersonalProfile(clientUserId,
                                        null,
                                        null,
                                        julesKeeperProfileGUID,
                                        julesKeeperEmpNo,
                                        "Julian Keeper",
                                        "Jules Keeper",
                                        "Chief Data Role",
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

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cdoForITAppointmentId);
        governanceRoleProperties.setRoleId(cdoForITAppointmentId);
        governanceRoleProperties.setScope("IT Systems");
        governanceRoleProperties.setTitle("Chief Data Role (CDO) for IT");

        String cdoForITGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        System.out.println("Appointing Erin as CDO for IT");

        gplClient.appointGovernanceRole(clientUserId,
                                           cdoForITGUID,
                                           erinOverviewProfileGUID,
                                           new Date());

        this.printGovernanceRole(gplClient, clientUserId, cdoForITGUID);


        /*
         * Appointing Gary Geeke as the governance officer for IT infrastructure.
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

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + infraGovForITAppointmentId);
        governanceRoleProperties.setRoleId(infraGovForITAppointmentId);
        governanceRoleProperties.setTitle("Chief Infrastructure Architect");

        String infraGovForITGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        System.out.println("Appointing Gary as gov officer for IT");

        gplClient.appointGovernanceRole(clientUserId,
                                           infraGovForITGUID,
                                           garyGeekeProfileGUID,
                                           null);

        this.printGovernanceRole(gplClient, clientUserId, infraGovForITGUID);


        /*
         * Appointing Polly Tasker as the governance officer for software development.
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

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.SOFTWARE_DEVELOPMENT.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + projLeadForITAppointmentId);
        governanceRoleProperties.setRoleId(projLeadForITAppointmentId);
        governanceRoleProperties.setTitle("Chief Project Lead for Software");

        String projLeadForITGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        System.out.println("Appointing Polly as gov officer for Software Development");

        gplClient.appointGovernanceRole(clientUserId,
                                           projLeadForITGUID,
                                           pollyTaskerProfileGUID,
                                           null);

        this.printGovernanceRole(gplClient, clientUserId, projLeadForITGUID);

        /*
         * Appointing Reggie Mint as the corporate governance officer.
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
                                                                        "Chief Finance Role",
                                                                        "Manages finance for Coco Pharmaceuticals.",
                                                                        reggieAdditionalProperties);

        this.printPersonalProfile(ppmClient, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating corporate governance officer");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.CORPORATE.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + corpAppointmentId);
        governanceRoleProperties.setRoleId(corpAppointmentId);
        governanceRoleProperties.setTitle("Corporate Governance Role");

        String corpGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        System.out.println("Appointing Reggie as corporate gov officer");

        gplClient.appointGovernanceRole(clientUserId,
                                           corpGUID,
                                           reggieMintProfileGUID,
                                           null);

        this.printGovernanceRole(gplClient, clientUserId, corpGUID);



        System.out.println("Update the CSO to the CISO");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTitle("Chief Information Security Role (CISO)");

        gplClient.updateGovernanceRole(clientUserId,
                                          csoGUID,
                                          true,
                                          governanceRoleProperties);

        governanceRoles = gplClient.getGovernanceRolesByDomainId(clientUserId, 0, 0, 0);

        /*
         * See the team
         */
        System.out.println(governanceRoles.size() + " governance officers");

        for (GovernanceRoleAppointee governanceRole : governanceRoleAppointees)
        {
            printGovernanceRoleAppointee(governanceRole);
        }

        System.out.println("Deleting all profiles and governance officers");

        /*
         * Delete all the governance officers
         */
        gplClient.deleteGovernanceRole(clientUserId, cdoGUID);
        gplClient.deleteGovernanceRole(clientUserId, cdoForITGUID);
        gplClient.deleteGovernanceRole(clientUserId, cpoGUID);
        gplClient.deleteGovernanceRole(clientUserId, csoGUID);
        gplClient.deleteGovernanceRole(clientUserId, corpGUID);
        gplClient.deleteGovernanceRole(clientUserId, projLeadForITGUID);
        gplClient.deleteGovernanceRole(clientUserId, infraGovForITGUID);


        /*
         * Delete all the personal profiles
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
        governanceRoles = gplClient.getGovernanceRolesByDomainId(clientUserId, 0, 0, 0);

        if (governanceRoles != null)
        {
            System.out.println(governanceRoles.size() + " governance officers");

            for (GovernanceRoleElement governanceRole : governanceRoles)
            {
                System.out.println(governanceRole.toString() + " is still defined");
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
