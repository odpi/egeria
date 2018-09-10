/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.governanceprogram.samples;

import org.apache.log4j.varia.NullAppender;
import org.odpi.openmetadata.accessservices.governanceprogram.client.GovernanceProgramLeadership;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UnrecognizedGUIDException;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficer;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceOfficerAppointee;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PersonalProfile;

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


    /*
     * These are the names of the properties that are not represented in the open metadata types
     * and so they are stored as additional properties.
     */
    private static final String   workLocationPropertyName = "WorkLocation";
    private static final String   contactTypePropertyName  = "ContactType";

    private String serverURLRoot;
    private String clientUserId;



    /**
     * Constructor
     *
     * @param serverURLRoot server to connect to.
     * @param clientUserId user id to use to access metadata.
     */
    private GovernanceLeadershipSample(String serverURLRoot, String clientUserId)
    {
        this.serverURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * Extract and print out a personal profile
     *
     * @param client the client to call.
     * @param clientUserId the user id to use on the call.
     * @param guid the unique identifier for the personal profile.
     * @throws UnrecognizedGUIDException the guid is not recognized.
     * @throws PropertyServerException the property server is not available.
     * @throws UserNotAuthorizedException the user id is not authorized to access the personal profile.
     */
    private void  printPersonalProfile(GovernanceProgramLeadership     client,
                                       String                          clientUserId,
                                       String                          guid) throws UnrecognizedGUIDException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        PersonalProfile  profile = client.getPersonalProfileByGUID(clientUserId, guid);

        System.out.println("----------------------------");
        System.out.println("Profile: " + guid);
        System.out.println("  Employee Id: " + profile.getEmployeeNumber());
        System.out.println("  Full Name: " + profile.getFullName());
        System.out.println("  Known Name: " + profile.getKnownName());
        System.out.println("  Job Title: " + profile.getJobTitle());
        System.out.println("  Job Description: " + profile.getJobRoleDescription());
        System.out.println("  Work Location: " + profile.getAdditionalProperties().get(workLocationPropertyName));
        System.out.println("  Contact Type: " + profile.getAdditionalProperties().get(contactTypePropertyName));
        System.out.println("----------------------------");
    }


    /**
     * Extract and print out a governance office
     *
     * @param client the client to call.
     * @param clientUserId the user id to use on the call.
     * @param guid the unique identifier for the governance officer.
     * @throws UnrecognizedGUIDException the guid is not recognized.
     * @throws PropertyServerException the property server is not available.
     * @throws UserNotAuthorizedException the user id is not authorized to access the personal profile.
     */
    private void  printGovernanceOfficer(GovernanceProgramLeadership     client,
                                         String                          clientUserId,
                                         String                          guid) throws UnrecognizedGUIDException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        GovernanceOfficer  governanceOfficer = client.getGovernanceOfficerByGUID(clientUserId, guid);

        System.out.println("----------------------------");
        System.out.println("Governance Officer: " + guid);
        System.out.println("  Domain: " + governanceOfficer.getGovernanceDomain());
        System.out.println("  Appointment Id: " + governanceOfficer.getAppointmentId());
        System.out.println("  Appointment Context: " + governanceOfficer.getAppointmentContext());
        System.out.println("  Title: " + governanceOfficer.getTitle());

        GovernanceOfficerAppointee appointee = governanceOfficer.getAppointee();

        if (appointee != null)
        {
            System.out.println("  Appointee: " + appointee.getProfile().getKnownName());
            System.out.println("  Start Date: " + appointee.getStartDate());
            System.out.println("  End Date: " + appointee.getEndDate());
        }
        else
        {
            System.out.println("  Appointee: <None> ");
        }
        System.out.println("----------------------------");
    }



    private void run() throws UnrecognizedGUIDException,
                              InvalidParameterException,
                              PropertyServerException,
                              UserNotAuthorizedException
    {
        GovernanceProgramLeadership  client = new GovernanceProgramLeadership(serverURLRoot);

        System.out.println("Creating profiles for Jules and Ivor");

        Map<String, Object>  julesAdditionalProperties = new HashMap<>();
        julesAdditionalProperties.put(workLocationPropertyName, 2);
        julesAdditionalProperties.put(contactTypePropertyName, "Employee");

        String julesKeeperProfileGUID = client.createPersonalProfile(clientUserId,
                                                                     julesKeeperEmpNo,
                                                                     "Julian Keeper",
                                                                     "Jules Keeper",
                                                                     "Chief Data and Privacy Officer",
                                                                     "Ensuring CocoP makes the best use of data.",
                                                                     julesAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, julesKeeperProfileGUID);


        Map<String, Object>  ivorAdditionalProperties = new HashMap<>();
        ivorAdditionalProperties.put(workLocationPropertyName, 1);
        ivorAdditionalProperties.put(contactTypePropertyName, "Employee");

        String ivorPadlockProfileGUID  = client.createPersonalProfile(clientUserId,
                                                                      ivorPadlockEmpNo,
                                                                      null,  /* optional property */
                                                                      "Ivor Padlock",
                                                                      "Security Executive",
                                                                      "Manages security for Coco Pharmaceuticals.",
                                                                      ivorAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, ivorPadlockProfileGUID);

        System.out.println("Creating CDO, CPO and CSO governance officers");

        String cdoGUID  = client.createGovernanceOfficer(clientUserId,
                                                         GovernanceDomain.DATA,
                                                         cdoAppointmentId,
                                                         null,
                                                         "Chief Data Officer (CDO)",
                                                         null,
                                                         null);

        this.printGovernanceOfficer(client, clientUserId, cdoGUID);


        String cpoGUID  = client.createGovernanceOfficer(clientUserId,
                                                         GovernanceDomain.PRIVACY,
                                                         cpoAppointmentId,
                                                         null,
                                                         "Chief Privacy Officer (CPO)",
                                                         null,
                                                         null);

        this.printGovernanceOfficer(client, clientUserId, cpoGUID);


        String csoGUID  = client.createGovernanceOfficer(clientUserId,
                                                         GovernanceDomain.SECURITY,
                                                         csoAppointmentId,
                                                         null,
                                                         "Chief Security Officer (CSO)",
                                                         null,
                                                         null);

        this.printGovernanceOfficer(client, clientUserId, csoGUID);

        List<GovernanceOfficer> governanceOfficers = client.getGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " governance officers");

        System.out.println("Appointing CDO, CPO and CSO governance officers");

        client.appointGovernanceOfficer(clientUserId,
                                        cdoGUID,
                                        julesKeeperProfileGUID,
                                        null);

        this.printGovernanceOfficer(client, clientUserId, cdoGUID);

        client.appointGovernanceOfficer(clientUserId,
                                        cpoGUID,
                                        julesKeeperProfileGUID,
                                        null);

        this.printGovernanceOfficer(client, clientUserId, cpoGUID);

        client.appointGovernanceOfficer(clientUserId,
                                        csoGUID,
                                        ivorPadlockProfileGUID,
                                        null);

        this.printGovernanceOfficer(client, clientUserId, csoGUID);

        System.out.println("Changing CPO to Faith");

        Map<String, Object>  faithAdditionalProperties = new HashMap<>();
        faithAdditionalProperties.put(workLocationPropertyName, 1);
        faithAdditionalProperties.put(contactTypePropertyName, "Employee");

        String faithBrokerProfileGUID  = client.createPersonalProfile(clientUserId,
                                                                     faithBrokerEmpNo,
                                                                     "Faith Charity Broker",
                                                                     "Faith Broker",
                                                                     "Human Resources Director",
                                                                     "Providing support to Coco Pharmaceutical employees.",
                                                                     faithAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, faithBrokerProfileGUID);

        long   handoverTime = new Date().getTime();
        Date   handoverDate = new Date(handoverTime + 100);

        client.relieveGovernanceOfficer(clientUserId, cpoGUID, julesKeeperProfileGUID, handoverDate);

        this.printGovernanceOfficer(client, clientUserId, cpoGUID);

        client.appointGovernanceOfficer(clientUserId,
                                        cpoGUID,
                                        faithBrokerProfileGUID,
                                        handoverDate);

        this.printGovernanceOfficer(client, clientUserId, cpoGUID);

        governanceOfficers = client.getGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " governance officers");

        governanceOfficers = client.getActiveGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " active governance officers");

        for (GovernanceOfficer governanceOfficer : governanceOfficers)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getKnownName() + " is the " + governanceOfficer.getTitle());
        }

        governanceOfficers = client.getGovernanceOfficersByDomain(clientUserId, GovernanceDomain.PRIVACY);

        System.out.println(governanceOfficers.size() + " privacy governance officers");

        for (GovernanceOfficer governanceOfficer : governanceOfficers)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getKnownName() +
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

        governanceOfficers = client.getActiveGovernanceOfficers(clientUserId);

        System.out.println(governanceOfficers.size() + " active governance officers");

        for (GovernanceOfficer governanceOfficer : governanceOfficers)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getKnownName() + " is the " + governanceOfficer.getTitle());
        }

        /*
         * Update Jule's job title
         */
        client.updatePersonalProfile(clientUserId,
                                     julesKeeperProfileGUID,
                                     julesKeeperEmpNo,
                                     "Julian Keeper",
                                     "Jules Keeper",
                                     "Chief Data Officer",
                                     "Ensuring CocoP makes the best use of data.",
                                     julesAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, julesKeeperProfileGUID);

        /*
         * Appointing Erin Overview as the CDO for IT
         */
        System.out.println("Creating profile for Erin");

        Map<String, Object>  erinAdditionalProperties = new HashMap<>();
        erinAdditionalProperties.put(workLocationPropertyName, 2);
        erinAdditionalProperties.put(contactTypePropertyName, "Employee");
        String erinOverviewProfileGUID  = client.createPersonalProfile(clientUserId,
                                                                       erinOverviewEmpNo,
                                                                      null,  /* optional property */
                                                                      "Erin Overview",
                                                                      "Information Architect",
                                                                      "Manages all information architecture and standards for Coco Pharmaceuticals IT systems.",
                                                                       erinAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, erinOverviewProfileGUID);

        System.out.println("Creating CDO for IT governance officer");

        String cdoForITGUID  = client.createGovernanceOfficer(clientUserId,
                                                         GovernanceDomain.DATA,
                                                         cdoForITAppointmentId,
                                                         "IT Systems",
                                                         "Chief Data Officer (CDO) for IT",
                                                         null,
                                                         null);

        System.out.println("Appointing Erin as CDO for IT");

        client.appointGovernanceOfficer(clientUserId,
                                        cdoForITGUID,
                                        erinOverviewProfileGUID,
                                        new Date());

        this.printGovernanceOfficer(client, clientUserId, cdoForITGUID);


        /*
         * Appointing Gary Geeke as as the governance officer for IT infrastructure.
         */
        System.out.println("Creating profile for Gary");

        Map<String, Object>  garyAdditionalProperties = new HashMap<>();
        garyAdditionalProperties.put(workLocationPropertyName, 1);
        garyAdditionalProperties.put(contactTypePropertyName, "Employee");
        String garyGeekeProfileGUID  = client.createPersonalProfile(clientUserId,
                                                                    garyGeekeEmpNo,
                                                                    null,  /* optional property */
                                                                    "Gary Geeke",
                                                                    "Infrastructure Architect",
                                                                    "Manages all the IT infrastructure for Coco Pharmaceuticals.",
                                                                    garyAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, garyGeekeProfileGUID);
        System.out.println("Creating IT governance officer");

        String infraGovForITGUID  = client.createGovernanceOfficer(clientUserId,
                                                                   GovernanceDomain.IT_INFRASTRUCTURE,
                                                                   infraGovForITAppointmentId,
                                                                  null,
                                                                  "Chief Infrastructure Architect",
                                                                  null,
                                                                  null);

        System.out.println("Appointing Gary as gov officer for IT");

        client.appointGovernanceOfficer(clientUserId,
                                        infraGovForITGUID,
                                        garyGeekeProfileGUID,
                                        null);

        this.printGovernanceOfficer(client, clientUserId, infraGovForITGUID);


        /*
         * Appointing Polly Tasker as as the governance officer for software development.
         */
        System.out.println("Creating profile for Gary");

        Map<String, Object>  pollyAdditionalProperties = new HashMap<>();
        pollyAdditionalProperties.put(workLocationPropertyName, 1);
        pollyAdditionalProperties.put(contactTypePropertyName, "Employee");
        String pollyTaskerProfileGUID  = client.createPersonalProfile(clientUserId,
                                                                    pollyTaskerEmpNo,
                                                                    null,  /* optional property */
                                                                    "Polly Tasker",
                                                                    "Lead Project Manager for IT",
                                                                    "Manages IT projects for Coco Pharmaceuticals.",
                                                                    pollyAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating SDLC governance officer");

        String projLeadForITGUID  = client.createGovernanceOfficer(clientUserId,
                                                                   GovernanceDomain.SOFTWARE_DEVELOPMENT,
                                                                   projLeadForITAppointmentId,
                                                                   null,
                                                                   "Chief Project Lead for Software",
                                                                   null,
                                                                   null);

        System.out.println("Appointing Polly as gov officer for Software Development");

        client.appointGovernanceOfficer(clientUserId,
                                        projLeadForITGUID,
                                        pollyTaskerProfileGUID,
                                        null);

        this.printGovernanceOfficer(client, clientUserId, projLeadForITGUID);


        /*
         * Appointing Reggie Mint as as the corporate governance officer.
         */
        System.out.println("Creating profile for Reggie");

        Map<String, Object>  reggieAdditionalProperties = new HashMap<>();
        reggieAdditionalProperties.put(workLocationPropertyName, 1);
        reggieAdditionalProperties.put(contactTypePropertyName, "Employee");
        String reggieMintProfileGUID  = client.createPersonalProfile(clientUserId,
                                                                      reggieMintEmpNo,
                                                                      "Reginald S P Mint",  /* optional property */
                                                                      "Reggie Mint",
                                                                      "Chief Finance Officer",
                                                                      "Manages finance for Coco Pharmaceuticals.",
                                                                      reggieAdditionalProperties);

        this.printPersonalProfile(client, clientUserId, pollyTaskerProfileGUID);
        System.out.println("Creating corporate governance officer");

        String corpGUID  = client.createGovernanceOfficer(clientUserId,
                                                          GovernanceDomain.CORPORATE,
                                                          corpAppointmentId,
                                                          null,
                                                          "Corporate Governance Officer",
                                                          null,
                                                          null);

        System.out.println("Appointing Reggie as corporate gov officer");

        client.appointGovernanceOfficer(clientUserId,
                                        corpGUID,
                                        reggieMintProfileGUID,
                                        null);

        this.printGovernanceOfficer(client, clientUserId, corpGUID);



        System.out.println("Update the CSO to the CISO");

        client.updateGovernanceOfficer(clientUserId,
                                       csoGUID,
                                       GovernanceDomain.SECURITY,
                                       csoAppointmentId,
                                       null,
                                       "Chief Information Security Officer (CISO)",
                                       null,
                                       null);

        governanceOfficers = client.getGovernanceOfficers(clientUserId);

        /*
         * See the team
         */
        System.out.println(governanceOfficers.size() + " governance officers");

        for (GovernanceOfficer governanceOfficer : governanceOfficers)
        {
            System.out.println(governanceOfficer.getAppointee().getProfile().getKnownName() + " is the " + governanceOfficer.getTitle());
        }

        /*
         * Delete all of the governance officers
         */
        client.deleteGovernanceOfficer(clientUserId, cdoGUID, cdoAppointmentId, GovernanceDomain.DATA);
        client.deleteGovernanceOfficer(clientUserId, cdoForITGUID, cdoForITAppointmentId, GovernanceDomain.DATA);
        client.deleteGovernanceOfficer(clientUserId, cpoGUID, cpoAppointmentId, GovernanceDomain.PRIVACY);
        client.deleteGovernanceOfficer(clientUserId, csoGUID, csoAppointmentId, GovernanceDomain.SECURITY);
        client.deleteGovernanceOfficer(clientUserId, corpGUID, corpAppointmentId, GovernanceDomain.CORPORATE);
        client.deleteGovernanceOfficer(clientUserId, projLeadForITGUID, projLeadForITAppointmentId, GovernanceDomain.SOFTWARE_DEVELOPMENT);
        client.deleteGovernanceOfficer(clientUserId, infraGovForITGUID, infraGovForITAppointmentId, GovernanceDomain.IT_INFRASTRUCTURE);


        /*
         * Delete all of the personal profiles
         */
        client.deletePersonalProfile(clientUserId, julesKeeperProfileGUID, julesKeeperEmpNo);
        client.deletePersonalProfile(clientUserId, ivorPadlockProfileGUID, ivorPadlockEmpNo);
        client.deletePersonalProfile(clientUserId, faithBrokerProfileGUID, faithBrokerEmpNo);
        client.deletePersonalProfile(clientUserId, erinOverviewProfileGUID, erinOverviewEmpNo);
        client.deletePersonalProfile(clientUserId, garyGeekeProfileGUID, garyGeekeEmpNo);
        client.deletePersonalProfile(clientUserId, reggieMintProfileGUID, reggieMintEmpNo);
        client.deletePersonalProfile(clientUserId, pollyTaskerProfileGUID, pollyTaskerEmpNo);


        /*
         * Should be all gone
         */
        governanceOfficers = client.getGovernanceOfficers(clientUserId);

        if (governanceOfficers != null)
        {
            System.out.println(governanceOfficers.size() + " governance officers");

            for (GovernanceOfficer governanceOfficer : governanceOfficers)
            {
                System.out.println(
                        governanceOfficer.getAppointee().getProfile().getKnownName() + " is the " + governanceOfficer.getTitle());
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
     * @param args URL root for the server
     */
    public static void main(String[] args)
    {
        String  serverURLRoot;
        String  clientUserId;

        org.apache.log4j.BasicConfigurator.configure(new NullAppender());

        if ((args == null) || (args.length < 2))
        {
            System.out.println("Please specify the server's URL root in the first parameter" +
                                       " and the caller's userId in the second parameter");
            System.exit(-1);
        }

        serverURLRoot = args[0];
        clientUserId = args[1];

        System.out.println("===============================");
        System.out.println("Governance Leadership Sample   ");
        System.out.println("===============================");
        System.out.println("Running against server: " + serverURLRoot);
        System.out.println("Using userId: " + clientUserId);


        try
        {
            GovernanceLeadershipSample   sample = new GovernanceLeadershipSample(serverURLRoot, clientUserId);

            sample.run();
        }
        catch (Throwable  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
