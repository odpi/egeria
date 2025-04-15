/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.leadership;

import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.UserIdentityManagement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ActorProfileElement;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceRoleManager;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceAppointee;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceRoleAppointee;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceRoleElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceRoleHistory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDomain;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceRoleProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ProfileIdentityElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
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
    /*
     * Data values to use in the sample
     */
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
     * These properties belong to the Person type and are stored as extended properties in an actor profile.
     */
    private static final String titlePropertyName             = "title";
    private static final String initialsPropertyName          = "initials";
    private static final String givenNamesPropertyName        = "givenNames";
    private static final String surnamePropertyName           = "surname";
    private static final String fullNamePropertyName          = "fullName";
    private static final String pronounsPropertyName          = "pronouns";
    private static final String jobTitlePropertyName          = "jobTitle";
    private static final String employeeNumberPropertyName    = "employeeNumber";
    private static final String employeeTypePropertyName      = "employeeType";
    private static final String preferredLanguagePropertyName = "preferredLanguage";
    private static final String isPublicPropertyName          = "isPublic";

    /*
     * This is the name of a properties that is not represented in the open metadata types, and so it is stored as additional properties.
     */
    private static final String workLocationPropertyName = "workLocation";

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
    private void  printPersonalProfile(OrganizationManagement client,
                                       String                 clientUserId,
                                       String                 guid) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        ActorProfileElement profile = client.getActorProfileByGUID(clientUserId, guid);

        System.out.println("----------------------------");
        System.out.println("Profile: " + guid);

        List<ProfileIdentityElement> userIdentities = profile.getUserIdentities();
        if (userIdentities == null)
        {
            System.out.println("  UserId: null <ERROR>");
        }
        else if (userIdentities.isEmpty())
        {
            System.out.println("  UserIds: empty <ERROR>");
        }
        else if (userIdentities.size() == 1)
        {
            ProfileIdentityElement firstUser    = userIdentities.get(0);
            UserIdentityElement    userIdentity = firstUser.getUserIdentity();
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
                    System.out.print(" " + userIdentity.getUserIdentity().getProperties().getQualifiedName());
                }
            }
            System.out.println();
        }

        Map<String, Object> extendedProperties = profile.getProfileProperties().getExtendedProperties();

        System.out.println("  Employee Id: " + extendedProperties.get(employeeNumberPropertyName));
        System.out.println("  Full Name: " + extendedProperties.get(fullNamePropertyName));
        System.out.println("  Known Name: " + profile.getProfileProperties().getKnownName());
        System.out.println("  Job Title: " + extendedProperties.get(jobTitlePropertyName));
        System.out.println("  Job Description: " + profile.getProfileProperties().getDescription());

        if (profile.getProfileProperties().getAdditionalProperties() != null)
        {
            System.out.println("  Work Location: " + profile.getProfileProperties().getAdditionalProperties().get(workLocationPropertyName));
        }

        System.out.println("  Employee Type: " + extendedProperties.get(employeeTypePropertyName));

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


    /**
     * Create a profile for a person linked to their user identifier.
     *
     * @param orgClient client for working with profiles
     * @param uidClient client for working with uer identities
     * @param clientUserId userId for calling the server
     * @param actorUserId userId for the profile
     * @param actorEmployeeNo unique employee identifier
     * @param pronouns pronouns to use when addressing the individual
     * @param title formal courtesy title
     * @param initials initials of the given name
     * @param givenNames given names
     * @param surname family name
     * @param fullName full legal name for travel docs etc
     * @param knownName name the individual is known as
     * @param jobTitle job title
     * @param jobDescription short description of what they do
     * @param workLocation where they work
     * @return unique identifier of the profile
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private String createPersonalProfile(OrganizationManagement  orgClient,
                                         UserIdentityManagement  uidClient,
                                         String                  clientUserId,
                                         String                  actorUserId,
                                         String                  actorEmployeeNo,
                                         String                  pronouns,
                                         String                  title,
                                         String                  initials,
                                         String                  givenNames,
                                         String                  surname,
                                         String                  fullName,
                                         String                  knownName,
                                         String                  jobTitle,
                                         String                  jobDescription,
                                         String                  workLocation) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String typeName = "Person";

        ActorProfileProperties actorProfileProperties = new ActorProfileProperties();

        actorProfileProperties.setTypeName(typeName);
        actorProfileProperties.setQualifiedName(typeName + ":" + actorEmployeeNo);
        actorProfileProperties.setKnownName(knownName);
        actorProfileProperties.setDescription(jobDescription);

        Map<String, String>  additionalProperties = new HashMap<>();

        additionalProperties.put(workLocationPropertyName, workLocation);
        actorProfileProperties.setAdditionalProperties(additionalProperties);

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(titlePropertyName, title);
        extendedProperties.put(initialsPropertyName, initials);
        extendedProperties.put(givenNamesPropertyName, givenNames);
        extendedProperties.put(surnamePropertyName, surname);
        extendedProperties.put(fullNamePropertyName, fullName);
        extendedProperties.put(pronounsPropertyName, pronouns);
        extendedProperties.put(jobTitlePropertyName, jobTitle);
        extendedProperties.put(employeeNumberPropertyName, actorEmployeeNo);
        extendedProperties.put(employeeTypePropertyName, "Employee");
        extendedProperties.put(preferredLanguagePropertyName, "English");
        extendedProperties.put(isPublicPropertyName, true);

        actorProfileProperties.setExtendedProperties(extendedProperties);

        String profileGUID = orgClient.createActorProfile(clientUserId, null, null, actorProfileProperties, null);

        /*
         * Create a userId for the profile.
         */
        UserIdentityProperties userIdentityProperties = new UserIdentityProperties();

        userIdentityProperties.setQualifiedName("UserIdentity:" + actorUserId);
        userIdentityProperties.setUserId(actorUserId);

        /*
         * This method anchors the new user identity to the profile so that when the profile is deleted, the user identity is too.
         * If you want the user identity to be independent of the profile, use createUserIdentity() followed by addIdentityToProfile().
         */
        uidClient.createUserIdentityForProfile(clientUserId, null, null, profileGUID, userIdentityProperties);

        return profileGUID;
    }


    /**
     * Delete the metadata element for an individual's user identifier.
     *
     * @param uidClient client to work with user identities
     * @param clientUserId user id to call the server
     * @param actorUserId user id of the individual
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    private void deleteUserIdentity(UserIdentityManagement uidClient,
                                    String                 clientUserId,
                                    String                 actorUserId) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        List<UserIdentityElement> userIdentityElements = uidClient.getUserIdentitiesByName(clientUserId, actorUserId, 0, 0);

        for (UserIdentityElement userIdentity : userIdentityElements)
        {
            uidClient.deleteUserIdentity(clientUserId, null, null, userIdentity.getElementHeader().getGUID());
        }
    }


    /**
     * This method runs the sample.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws PropertyServerException  there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void run() throws InvalidParameterException,
                             PropertyServerException,
                             UserNotAuthorizedException
    {
        OrganizationManagement orgClient = new OrganizationManagement(serverName, serverURLRoot, 100);
        UserIdentityManagement uidClient = new UserIdentityManagement(serverName, serverURLRoot, 100);
        GovernanceRoleManager  gplClient = new GovernanceRoleManager(serverName, serverURLRoot);

        GovernanceRoleProperties governanceRoleProperties;

        System.out.println("Creating profiles for Jules and Ivor");

        String julesKeeperProfileGUID = this.createPersonalProfile(orgClient,
                                                                   uidClient,
                                                                   clientUserId,
                                                                   julesKeeperUserId,
                                                                   julesKeeperEmpNo,
                                                                   "He/him/his",
                                                                   "Mr",
                                                                   "J",
                                                                   "Julian",
                                                                   "Keeper",
                                                                   "Mr Julian Keeper",
                                                                   "Jules Keeper",
                                                                   "Chief Data and Privacy Role",
                                                                   "Ensuring CocoP makes the best use of data.",
                                                                   "2");

        this.printPersonalProfile(orgClient, clientUserId, julesKeeperProfileGUID);

        String ivorPadlockProfileGUID  = this.createPersonalProfile(orgClient,
                                                                    uidClient,
                                                                    clientUserId,
                                                                    ivorPadlockUserId,
                                                                    ivorPadlockEmpNo,
                                                                    "He/him/his",
                                                                    "Mr",
                                                                    "I",
                                                                    "Ivor",
                                                                    "Padlock",
                                                                    "Mr Ivor Padlock",
                                                                    "Ivor Padlock",
                                                                    "Security Executive",
                                                                    "Manages security for Coco Pharmaceuticals.",
                                                                    "1");

        this.printPersonalProfile(orgClient, clientUserId, ivorPadlockProfileGUID);

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

        String faithBrokerProfileGUID  = this.createPersonalProfile(orgClient,
                                                                    uidClient,
                                                                    clientUserId,
                                                                    faithBrokerUserId,
                                                                    faithBrokerEmpNo,
                                                                    "She/her/hers",
                                                                    "Mrs",
                                                                    "F C J",
                                                                    "Faith Charity Jean",
                                                                    "Broker",
                                                                    "Mrs Faith Charity Jean Broker",
                                                                    "Faith Broker",
                                                                    "Human Resources Director",
                                                                    "Providing support to Coco Pharmaceutical employees.",
                                                                    "1");

        this.printPersonalProfile(orgClient, clientUserId, faithBrokerProfileGUID);

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
        ActorProfileProperties updateProperties = new ActorProfileProperties();

        Map<String, Object> updateExtendedProperties = new HashMap<>();

        updateExtendedProperties.put(jobTitlePropertyName, "Chief Data Role");
        updateProperties.setExtendedProperties(updateExtendedProperties);

        orgClient.updateActorProfile(clientUserId,
                                     null,
                                     null,
                                     julesKeeperProfileGUID,
                                     true,
                                     updateProperties,
                                     null);

        this.printPersonalProfile(orgClient, clientUserId, julesKeeperProfileGUID);

        /*
         * Appointing Erin Overview as the CDO for IT
         */
        System.out.println("Creating profile for Erin");

        String erinOverviewProfileGUID  = this.createPersonalProfile(orgClient,
                                                                     uidClient,
                                                                     clientUserId,
                                                                     erinOverviewUserId,
                                                                     erinOverviewEmpNo,
                                                                     "She/her/hers",
                                                                     "Dr",
                                                                     "E",
                                                                     "Erin",
                                                                     "Overview",
                                                                     "Dr Erin Overview",
                                                                     "Erin Overview",
                                                                     "Information Architect",
                                                                     "Manages all information architecture and standards for Coco Pharmaceuticals IT systems.",
                                                                     "2");

        this.printPersonalProfile(orgClient, clientUserId, erinOverviewProfileGUID);

        System.out.println("Creating CDO for IT governance officer");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cdoForITAppointmentId);
        governanceRoleProperties.setRoleId(cdoForITAppointmentId);
        governanceRoleProperties.setScope("IT Systems");
        governanceRoleProperties.setTitle("Chief Data Role (CDO) for IT");

        String cdoForInfoTechGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        System.out.println("Appointing Erin as CDO for IT");

        gplClient.appointGovernanceRole(clientUserId,
                                           cdoForInfoTechGUID,
                                           erinOverviewProfileGUID,
                                           new Date());

        this.printGovernanceRole(gplClient, clientUserId, cdoForInfoTechGUID);


        /*
         * Appointing Gary Geeke as the governance officer for IT infrastructure.
         */
        System.out.println("Creating profile for Gary");

        String garyGeekeProfileGUID  = this.createPersonalProfile(orgClient,
                                                                  uidClient,
                                                                  clientUserId,
                                                                  garyGeekeUserId,
                                                                  garyGeekeEmpNo,
                                                                  "He/him/his",
                                                                  "Mr",
                                                                  "G",
                                                                  "Gary",
                                                                  "Geeke",
                                                                  "Mr Gary Geeke",
                                                                  "Gary Geeke",
                                                                  "Infrastructure Architect",
                                                                  "Manages all the IT infrastructure for Coco Pharmaceuticals.",
                                                                  "1");

        this.printPersonalProfile(orgClient, clientUserId, garyGeekeProfileGUID);
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

        String pollyTaskerProfileGUID  = this.createPersonalProfile(orgClient,
                                                                    uidClient,
                                                                    clientUserId,
                                                                    pollyTaskerUserId,
                                                                    pollyTaskerEmpNo,
                                                                    "She/her/hers",
                                                                    "Ms",
                                                                    "P",
                                                                    "Polly",
                                                                    "Tasker",
                                                                    "Ms Polly Tasker",
                                                                    "Polly Tasker",
                                                                    "Lead Project Manager for IT",
                                                                    "Manages IT projects for Coco Pharmaceuticals.",
                                                                    "1");

        this.printPersonalProfile(orgClient, clientUserId, pollyTaskerProfileGUID);
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

        String reggieMintProfileGUID  = this.createPersonalProfile(orgClient,
                                                                   uidClient,
                                                                   clientUserId,
                                                                   reggieMintUserId,
                                                                   reggieMintEmpNo,
                                                                   "He/him/his",
                                                                   "Mr",
                                                                   "R S P",
                                                                   "Reginald Sidney",
                                                                   "Mint",
                                                                   "Mr Reginald S Mint",
                                                                   "Reggie Mint",
                                                                   "Chief Finance Role",
                                                                   "Manages finance for Coco Pharmaceuticals.",
                                                                   "1");

        this.printPersonalProfile(orgClient, clientUserId, pollyTaskerProfileGUID);
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

        /*
         * Check that the userIdentities are created.
         */
        List<UserIdentityElement> activeUserIdentities = uidClient.findUserIdentities(clientUserId, ".*", 0, 0);

        if (activeUserIdentities == null)
        {
            System.out.println("No user identities have been created");
            System.exit(-1);
        }
        else if (activeUserIdentities.size() != 7)
        {
            System.out.println(activeUserIdentities.size() + " number of active user identities rather than 7: " + activeUserIdentities);
            System.exit(-1);
        }

        System.out.println("Deleting all profiles and governance officers");

        /*
         * Delete all the governance officers
         */
        gplClient.deleteGovernanceRole(clientUserId, cdoGUID);
        gplClient.deleteGovernanceRole(clientUserId, cdoForInfoTechGUID);
        gplClient.deleteGovernanceRole(clientUserId, cpoGUID);
        gplClient.deleteGovernanceRole(clientUserId, csoGUID);
        gplClient.deleteGovernanceRole(clientUserId, corpGUID);
        gplClient.deleteGovernanceRole(clientUserId, projLeadForITGUID);
        gplClient.deleteGovernanceRole(clientUserId, infraGovForITGUID);

        this.deleteUserIdentity(uidClient, clientUserId, julesKeeperUserId);
        this.deleteUserIdentity(uidClient, clientUserId, ivorPadlockUserId);
        this.deleteUserIdentity(uidClient, clientUserId, faithBrokerUserId);
        this.deleteUserIdentity(uidClient, clientUserId, erinOverviewUserId);
        this.deleteUserIdentity(uidClient, clientUserId, garyGeekeUserId);
        this.deleteUserIdentity(uidClient, clientUserId, reggieMintUserId);
        this.deleteUserIdentity(uidClient, clientUserId, pollyTaskerUserId);

        /*
         * Delete all the personal profiles
         */
        orgClient.deleteActorProfile(clientUserId, null, null, julesKeeperProfileGUID);
        orgClient.deleteActorProfile(clientUserId, null, null, ivorPadlockProfileGUID);
        orgClient.deleteActorProfile(clientUserId, null, null, faithBrokerProfileGUID);
        orgClient.deleteActorProfile(clientUserId, null, null, erinOverviewProfileGUID);
        orgClient.deleteActorProfile(clientUserId, null, null, garyGeekeProfileGUID);
        orgClient.deleteActorProfile(clientUserId, null, null, reggieMintProfileGUID);
        orgClient.deleteActorProfile(clientUserId, null, null, pollyTaskerProfileGUID);


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

        /*
         * Check that the userIdentities are deleted.  The user identities are anchored to the profiles, so they should be deleted with their
         * corresponding profile.
         */
        List<UserIdentityElement> abandonedUserIdentities = uidClient.findUserIdentities(clientUserId, ".*", 0, 0);

        if (abandonedUserIdentities != null)
        {
            System.out.println(abandonedUserIdentities.size() + " abandoned user identities: " + abandonedUserIdentities);
            System.exit(-1);
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

        HttpHelper.noStrictSSL();


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
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
