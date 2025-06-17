/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.samples.leadership;

import org.odpi.openmetadata.accessservices.communityprofile.client.UserIdentityManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.ActorProfileManagement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceRoleManager;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDomain;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.UserIdentityHandler;
import org.odpi.openmetadata.http.HttpHelper;


import java.util.*;

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
     * This is the name of a properties that is not represented in the open metadata types, and so it is stored as additional properties.
     */
    private static final String workLocationPropertyName = "workLocation";

    private final String serverName;
    private final String serverURLRoot;
    private final String clientUserId;

    private final PropertyHelper propertyHelper = new PropertyHelper();



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
    private void  printPersonalProfile(ActorProfileHandler client,
                                       String              clientUserId,
                                       String              guid) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        ActorProfileElement profile = client.getActorProfileByGUID(clientUserId, guid, null, false, false, new Date());

        System.out.println("----------------------------");
        System.out.println("Profile: " + guid);

        List<RelatedMetadataElementSummary> relatedElements = profile.getOtherRelatedElements();
        List<RelatedMetadataElementSummary> userIdentities = new ArrayList<>();

        if (relatedElements == null)
        {
            System.out.println("  UserId: null <ERROR>");
        }
        else if (relatedElements.isEmpty())
        {
            System.out.println("  UserIds: empty <ERROR>");
        }
        else
        {
            for (RelatedMetadataElementSummary relatedElement : relatedElements)
            {
                if ((relatedElement != null) && (propertyHelper.isTypeOf(relatedElement.getRelationshipHeader(),
                                                                         OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName)))
                {
                    userIdentities.add(relatedElement);
                }
            }
        }

        if (userIdentities.isEmpty())
        {
            System.out.println("  UserIds: empty <ERROR>");
        }
        else if (userIdentities.size() == 1)
        {
            RelatedMetadataElementSummary firstUser    = userIdentities.get(0);
            if (firstUser == null)
            {
                System.out.println("  UserId: empty <ERROR>");
            }
            else
            {
                System.out.println("  UserId: " + firstUser.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name));
            }
        }
        else
        {
            System.out.print("  UserIds:");
            for (RelatedMetadataElementSummary  userIdentity : userIdentities)
            {
                if (userIdentity != null)
                {
                    System.out.print(" " + userIdentity.getRelatedElement().getProperties().get(OpenMetadataProperty.QUALIFIED_NAME.name));
                }
            }

            System.out.println();
        }

        if (profile.getProfileProperties() instanceof PersonProfileProperties personProfileProperties)
        {
            System.out.println("  Employee Id: " + personProfileProperties.getEmployeeNumber());
            System.out.println("  Full Name: " + personProfileProperties.getFullName());
            System.out.println("  Known Name: " + personProfileProperties.getKnownName());
            System.out.println("  Job Title: " + personProfileProperties.getJobTitle());
            System.out.println("  Job Description: " + profile.getProfileProperties().getDescription());

            if (profile.getProfileProperties().getAdditionalProperties() != null)
            {
                System.out.println("  Work Location: " + personProfileProperties.getAdditionalProperties().get(workLocationPropertyName));
            }

            System.out.println("  Employee Type: " + personProfileProperties.getEmployeeType());
        }
        else
        {
            Map<String, Object> extendedProperties = profile.getProfileProperties().getExtendedProperties();

            System.out.println("  Employee Id: " + extendedProperties.get(OpenMetadataProperty.EMPLOYEE_NUMBER.name));
            System.out.println("  Full Name: " + extendedProperties.get(OpenMetadataProperty.FULL_NAME.name));
            System.out.println("  Known Name: " + profile.getProfileProperties().getKnownName());
            System.out.println("  Job Title: " + extendedProperties.get(OpenMetadataProperty.JOB_TITLE.name));
            System.out.println("  Job Description: " + profile.getProfileProperties().getDescription());

            if (profile.getProfileProperties().getAdditionalProperties() != null)
            {
                System.out.println("  Work Location: " + profile.getProfileProperties().getAdditionalProperties().get(workLocationPropertyName));
            }

            System.out.println("  Employee Type: " + extendedProperties.get(OpenMetadataProperty.EMPLOYEE_TYPE.name));
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
            System.out.println("  Appointment Id: " + governanceRole.getIdentifier());
            System.out.println("  Appointment Scope: " + governanceRole.getScope());
            System.out.println("  Title: " + governanceRole.getName());
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
    private String createPersonalProfile(ActorProfileHandler     orgClient,
                                         UserIdentityHandler     uidClient,
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
        actorProfileProperties.setQualifiedName(typeName + "::" + actorEmployeeNo);
        actorProfileProperties.setKnownName(knownName);
        actorProfileProperties.setDescription(jobDescription);

        Map<String, String>  additionalProperties = new HashMap<>();

        additionalProperties.put(workLocationPropertyName, workLocation);
        actorProfileProperties.setAdditionalProperties(additionalProperties);

        Map<String, Object> extendedProperties = new HashMap<>();

        extendedProperties.put(OpenMetadataProperty.COURTESY_TITLE.name, title);
        extendedProperties.put(OpenMetadataProperty.INITIALS.name, initials);
        extendedProperties.put(OpenMetadataProperty.GIVEN_NAMES.name, givenNames);
        extendedProperties.put(OpenMetadataProperty.SURNAME.name, surname);
        extendedProperties.put(OpenMetadataProperty.FULL_NAME.name, fullName);
        extendedProperties.put(OpenMetadataProperty.PRONOUNS.name, pronouns);
        extendedProperties.put(OpenMetadataProperty.JOB_TITLE.name, jobTitle);
        extendedProperties.put(OpenMetadataProperty.EMPLOYEE_NUMBER.name, actorEmployeeNo);
        extendedProperties.put(OpenMetadataProperty.EMPLOYEE_TYPE.name, "Employee");
        extendedProperties.put(OpenMetadataProperty.PREFERRED_LANGUAGE.name, "English");
        extendedProperties.put(OpenMetadataProperty.IS_PUBLIC.name, true);

        actorProfileProperties.setExtendedProperties(extendedProperties);

        String profileGUID = orgClient.createActorProfile(clientUserId,
                                                          null,
                                                          null,
                                                          null,
                                                          true,
                                                          null,
                                                          actorProfileProperties,
                                                          null,
                                                          null,
                                                          null,
                                                          true,
                                                          false,
                                                          false,
                                                          new Date());

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
        uidClient.createUserIdentity(clientUserId,
                                     null,
                                     null,
                                     profileGUID,
                                     false,
                                     profileGUID,
                                     userIdentityProperties,
                                     profileGUID,
                                     OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                     null,
                                     true,
                                     false,
                                     false,
                                     new Date());

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
        List<UserIdentityElement> userIdentityElements = uidClient.getUserIdentitiesByName(clientUserId,
                                                                                           actorUserId,
                                                                                           TemplateFilter.NO_TEMPLATES,
                                                                                           null,
                                                                                           null,
                                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                                           null,
                                                                                           0,
                                                                                           0,
                                                                                           false,
                                                                                           false,
                                                                                            new Date());

        for (UserIdentityElement userIdentity : userIdentityElements)
        {
            uidClient.deleteUserIdentity(clientUserId, null, null, userIdentity.getElementHeader().getGUID(), false, false, false, new Date());
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
        ActorProfileManagement actorProfileClient = new ActorProfileManagement(this.getClass().getName(),
                                                                            serverName,
                                                                            serverURLRoot,
                                                                            null,
                                                                            100);
        UserIdentityManagement uidClient = new UserIdentityManagement(this.getClass().getName(),
                                                                      serverName,
                                                                      serverURLRoot,
                                                                      null,
                                                                      100);
        GovernanceRoleManager  gplClient = new GovernanceRoleManager(serverName, serverURLRoot);

        GovernanceRoleProperties governanceRoleProperties;

        System.out.println("Creating profiles for Jules and Ivor");

        String julesKeeperProfileGUID = this.createPersonalProfile(actorProfileClient,
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
                                                                   "Chief Data and Privacy Officer",
                                                                   "Ensuring CocoP makes the best use of data.",
                                                                   "2");

        this.printPersonalProfile(actorProfileClient, clientUserId, julesKeeperProfileGUID);

        String ivorPadlockProfileGUID  = this.createPersonalProfile(actorProfileClient,
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

        this.printPersonalProfile(actorProfileClient, clientUserId, ivorPadlockProfileGUID);

        System.out.println("Creating CDO, CPO and CSO governance officers");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cdoAppointmentId);
        governanceRoleProperties.setIdentifier(cdoAppointmentId);
        governanceRoleProperties.setName("Chief Data Officer (CDO) Role");

        String cdoGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        this.printGovernanceRole(gplClient, clientUserId, cdoGUID);

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.PRIVACY.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cpoAppointmentId);
        governanceRoleProperties.setIdentifier(cpoAppointmentId);
        governanceRoleProperties.setName("Chief Privacy Officer (CPO) Role");

        String cpoGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        this.printGovernanceRole(gplClient, clientUserId, cpoGUID);

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.SECURITY.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + csoAppointmentId);
        governanceRoleProperties.setIdentifier(csoAppointmentId);
        governanceRoleProperties.setName("Chief Security Officer (CSO) Role");

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

        String faithBrokerProfileGUID  = this.createPersonalProfile(actorProfileClient,
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

        this.printPersonalProfile(actorProfileClient, clientUserId, faithBrokerProfileGUID);

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

        updateExtendedProperties.put(OpenMetadataProperty.JOB_TITLE.name, "Chief Data Officer");
        updateProperties.setExtendedProperties(updateExtendedProperties);

        actorProfileClient.updateActorProfile(clientUserId,
                                              null,
                                              null,
                                              julesKeeperProfileGUID,
                                              false,
                                              updateProperties,
                                              false,
                                              false,
                                              new Date());

        this.printPersonalProfile(actorProfileClient, clientUserId, julesKeeperProfileGUID);

        /*
         * Appointing Erin Overview as the CDO for IT
         */
        System.out.println("Creating profile for Erin");

        String erinOverviewProfileGUID  = this.createPersonalProfile(actorProfileClient,
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

        this.printPersonalProfile(actorProfileClient, clientUserId, erinOverviewProfileGUID);

        System.out.println("Creating CDO for IT governance officer");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.DATA.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + cdoForITAppointmentId);
        governanceRoleProperties.setIdentifier(cdoForITAppointmentId);
        governanceRoleProperties.setScope("IT Systems");
        governanceRoleProperties.setName("Chief Data Officer (CDO) for IT Role");

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

        String garyGeekeProfileGUID  = this.createPersonalProfile(actorProfileClient,
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

        this.printPersonalProfile(actorProfileClient, clientUserId, garyGeekeProfileGUID);
        System.out.println("Creating IT governance officer");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.IT_INFRASTRUCTURE.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + infraGovForITAppointmentId);
        governanceRoleProperties.setIdentifier(infraGovForITAppointmentId);
        governanceRoleProperties.setName("Chief Infrastructure Architect");

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

        String pollyTaskerProfileGUID  = this.createPersonalProfile(actorProfileClient,
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

        this.printPersonalProfile(actorProfileClient, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating SDLC governance officer");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.SOFTWARE_DEVELOPMENT.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + projLeadForITAppointmentId);
        governanceRoleProperties.setIdentifier(projLeadForITAppointmentId);
        governanceRoleProperties.setName("Chief Project Lead for Software");

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

        String reggieMintProfileGUID  = this.createPersonalProfile(actorProfileClient,
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

        this.printPersonalProfile(actorProfileClient, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating corporate governance officer");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setTypeName("GovernanceOfficer");
        governanceRoleProperties.setDomainIdentifier(GovernanceDomain.CORPORATE.getOrdinal());
        governanceRoleProperties.setQualifiedName("GovernanceOfficer:" + corpAppointmentId);
        governanceRoleProperties.setIdentifier(corpAppointmentId);
        governanceRoleProperties.setName("Corporate Governance Role");

        String corpGUID  = gplClient.createGovernanceRole(clientUserId, governanceRoleProperties);

        System.out.println("Appointing Reggie as corporate gov officer");

        gplClient.appointGovernanceRole(clientUserId,
                                        corpGUID,
                                        reggieMintProfileGUID,
                                        null);

        this.printGovernanceRole(gplClient, clientUserId, corpGUID);



        System.out.println("Update the CSO to the CISO");

        governanceRoleProperties = new GovernanceRoleProperties();

        governanceRoleProperties.setName("Chief Information Security Officer (CISO) Role");

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
        List<UserIdentityElement> activeUserIdentities = uidClient.findUserIdentities(clientUserId,
                                                                                      ".*",
                                                                                      TemplateFilter.ALL,
                                                                                      null,
                                                                                      null,
                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                      null,
                                                                                      0,
                                                                                      0,
                                                                                      false,
                                                                                       false,
                                                                                      new Date());

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
        actorProfileClient.deleteActorProfile(clientUserId, null, null, julesKeeperProfileGUID, true, false, false, new Date());
        actorProfileClient.deleteActorProfile(clientUserId, null, null, ivorPadlockProfileGUID, true, false, false, new Date());
        actorProfileClient.deleteActorProfile(clientUserId, null, null, faithBrokerProfileGUID, true, false, false, new Date());
        actorProfileClient.deleteActorProfile(clientUserId, null, null, erinOverviewProfileGUID, true, false, false, new Date());
        actorProfileClient.deleteActorProfile(clientUserId, null, null, garyGeekeProfileGUID, true, false, false, new Date());
        actorProfileClient.deleteActorProfile(clientUserId, null, null, reggieMintProfileGUID, true, false, false, new Date());
        actorProfileClient.deleteActorProfile(clientUserId, null, null, pollyTaskerProfileGUID, true, false, false, new Date());


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
        List<UserIdentityElement> abandonedUserIdentities = uidClient.findUserIdentities(clientUserId,
                                                                                         ".*",
                                                                                         TemplateFilter.ALL,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         null,
                                                                                         0,
                                                                                         0,
                                                                                         false,
                                                                                         false,
                                                                                         new Date());

        if (abandonedUserIdentities != null)
        {
            System.out.println(abandonedUserIdentities.size() + " abandoned user identities: " + abandonedUserIdentities);
            System.exit(-1);
        }
    }



    /**
     * The main program takes the URL root for the server.
     *
     * @param args 1. server name, 2. URL root for the server, 3. Client userId
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
