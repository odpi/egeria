/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.governanceprogram;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.ActorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.ProjectDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ProjectStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The ProjectDefinition is used to feed the definition of the projects for Coco Pharmaceuticals scenarios.
 */
public enum CocoProjectDefinition implements ProjectDefinition
{
    /**
     * CAMP-MM - Project to upgrade the manufacturing process to support personalized medicine.
     */
    MANUFACTURING_MOD("Campaign::Manufacturing Modernization",
                      "CAMP-MM",
                      "Manufacturing Modernization Project",
                      "Project to upgrade the manufacturing process to support personalized medicine.",
                      "This project is focused on factory automation to allow the production of small batches of medicine on demand.",
                      null,
                      true,
                      false,
                      null,
                      ProjectStatus.ACTIVATING,
                      null,
                      null,
                      PersonDefinition.STEW_FASTER,
                      null,
                      PersonDefinition.STEVE_STARTER),

    /**
     * CAMP-CT - Developing efficient ways to manage each clinical trial.
     */
    CLINICAL_TRIALS("Campaign::Clinical Trials Management",
                    "CAMP-CT",
                    "Clinical Trials Management",
                    "Developing efficient ways to manage each clinical trial.",
                    "This project is focused on developing and operating a clinical trials management system that can be used by the researchers, business leaders and regulators.",
                    "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/receiving-patient-data-from-a-hospital/overview/",
                    true,
                    false,
                    OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                    ProjectStatus.ACTIVE,
                    null,
                    null,
                    PersonDefinition.TANYA_TIDIE,
                    null,
                    PersonDefinition.TERRI_DARING),

    /**
     * CAMP-CDH - Enabling different business units to exchange data in a systematic and timely manner.
     */
    CROSS_BUSINESS_DATA_SHARING("Campaign::Cross-Business Data Sharing",
                                "CAMP-CDH",
                                "Cross-Business Data Sharing",
                                "Enabling different business units to exchange data in a systematic and timely manner.",
                                """
                                        Personalized medicine is going to require a lot of data to be shared between different business units. This project is focused on developing a data sharing mechanism that can be used by the different business units to share data with each other.
                                        
                                        The main concept is to build a data hub that contains data platforms hosting authoritative data.  The business unit systems are connected directly to the data hub to send/receive the data they need.
                                        """,
                                "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/defining-new-systems-architecture/overview/",
                                true,
                                false,
                                null,
                                ProjectStatus.ACTIVE,
                                null,
                                null,
                                PersonDefinition.POLLY_TASKER,
                                null,
                                PersonDefinition.JULES_KEEPER),

    /**
     * CDH-DEFINITION - Coco Data Hub Definition
     */
    DATA_HUB_DEFINITION("Project::Coco Data Hub Definition",
                        "CDH-DEFINITION",
                        "Coco Data Hub Definition",
                        "Creating the definition of the data hub to support the clinical trials and sustainability information supply chains.",
                        """
                                This project creates the definition of the data hub ans adds the data stores needed to support the clinical trials and sustainability information supply chain.
                                """,
                        "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/defining-new-systems-architecture/overview/",
                        false,
                        false,
                        null,
                        ProjectStatus.ACTIVE,
                        CROSS_BUSINESS_DATA_SHARING,
                        null,
                        PersonDefinition.PETER_PROFILE,
                        null,
                        PersonDefinition.POLLY_TASKER),

    /**
     * CAMP-CSM - Enabling different business units to exchange data in a systematic and timely manner.
     */
    SECURITY_MODERNIZATION("Campaign::Cyber-security Modernization",
                           "CAMP-CSM",
                           "Cyber-security Modernization",
                           "To upgrade cyber security technology and practices across the organization.",
                           """
                                   Up to this point, the IT team have focused on using security best practices (certificates, a firewall and SSL on the internal network, along with strong passwords on administrative user accounts) on a system-by-system basis.  The organization needs to take a more holistic approach to counter the rising sophistication in the technology used by cyber attachers.
                                   
                                   This campaign tracks the steps taken to make this transformation.
                                   """,
                           "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/building-a-data-security-strategy/overview/",
                           true,
                           false,
                           OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                           ProjectStatus.ACTIVE,
                           null,
                           null,
                           PersonDefinition.IVOR_PADLOCK,
                           null,
                           PersonDefinition.ZACH_NOW),

    /**
     * CSM-RECRUIT - To recruit a full-time, permanent cyber expert to join the organization.
     */
    SECURITY_STRATEGY("Project::Define the security strategy for Coco Pharmaceuticals",
                      "CSM-STRATEGY",
                      "Define the security strategy for Coco Pharmaceuticals",
                      "To lay out how Coco Pharmaceuticals will protect its data and assets from cyber threats.",
                      """
                              The security team need to lay out how they are going to protect Coco Pharmaceuticals' data and assets from cyber threats.  This includes defining the governance principles, threat, approaches and metrics that will define how Coco Pharmaceuticals will build out and maintain its protection against cyber threats.
                              """,
                      "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/building-a-data-security-strategy/overview/",
                      false,
                      false,
                      OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                      ProjectStatus.COMPLETED,
                      SECURITY_MODERNIZATION,
                      null,
                      PersonDefinition.IVOR_PADLOCK,
                      new PersonDefinition[]{PersonDefinition.SIDNEY_SEEKER, PersonDefinition.GARY_GEEKE, PersonDefinition.LEMMIE_STAGE},
                      PersonDefinition.ZACH_NOW),

    /**
     * CSM-SYSTEMS-INVENTORY - Creating an IT Systems Inventory
     */
    SYSTEMS_INVENTORY("Project::Creating an IT Systems Inventory",
                      "CSM-SYSTEMS-INVENTORY",
                      "Creating an IT Systems Inventory",
                      "To ensure all IT systems are defined in the IT Systems inventory so that they can be governed.",
                      """
                              Coco Pharmaceuticals needs an inventory of their IT systems to ensure they are properly maintained and secured.  It is also needed for capital planning and budgeting.  This project creates the inventory.
                              """,
                      "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/cataloguing-infrastructure/overview/",
                      false,
                      false,
                      OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                      ProjectStatus.COMPLETED,
                      SECURITY_MODERNIZATION,
                      null,
                      PersonDefinition.GARY_GEEKE,
                      null,
                      PersonDefinition.ZACH_NOW),

    /**
     * CSM-RECRUIT - To recruit a full-time, permanent cyber expert to join the organization.
     */
    RECRUIT_CYBER_EXPERT("Project::Recruit a Cyber Expert",
                         "CSM-RECRUIT",
                         "Recruit a Cyber Expert",
                         "To recruit a full-time, permanent cyber expert to join the organization.",
                         """
                                 Coco Pharmaceuticals needs expertise to develop and operate a cyber security monitoring and response capability.  For this they need an expert in cyber security to build and maintain this capability.  This requires the appointee to maintain an ongoing focus on the changing security landscape and the tools Coco Pharmaceuticals needs to operate safely.
                                 """,
                         "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/building-a-data-security-strategy/overview/",
                         false,
                         false,
                         OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                         ProjectStatus.COMPLETED,
                         SECURITY_MODERNIZATION,
                         null,
                         PersonDefinition.IVOR_PADLOCK,
                         new PersonDefinition[]{PersonDefinition.SIDNEY_SEEKER},
                         PersonDefinition.ZACH_NOW),

    /**
     * CSM-FOUNDATION - Add cyber monitoring and response foundation
     */
    ADD_CYBER_TOOLS_FOUNDATION("Project::Add cyber monitoring and response foundation",
                               "CSM-FOUNDATION",
                               "Add cyber monitoring and response foundation",
                               "To develop a cyber security monitoring and response capability.",
                               """
                                       This project designs and deploys a cyber security monitoring and response capability for Coco Pharmaceuticals.  This first stage establishes a foundation for the technology needed to support the data security strategy.
                                       """,
                               "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/building-a-data-security-strategy/overview/",
                               false,
                               false,
                               OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                               ProjectStatus.COMPLETED,
                               SECURITY_MODERNIZATION,
                               new CocoProjectDefinition[]{RECRUIT_CYBER_EXPERT, SECURITY_STRATEGY},
                               null,
                               new PersonDefinition[]{PersonDefinition.SIDNEY_SEEKER, PersonDefinition.LEMMIE_STAGE, PersonDefinition.GARY_GEEKE, PersonDefinition.BOB_NITTER},
                               PersonDefinition.IVOR_PADLOCK),

    /**
     * CSM-USER-AUDIT - Review user audit
     */
    REVIEW_USER_AUDIT("Project::Review user audit",
                      "CSM-USER-AUDIT",
                      "Review user audit",
                      "To run a monthly meeting to review the user audit findings and identify any gaps in the process.",
                      """
                              This project runs a monthly meeting to review the user audit findings and identify any gaps in the process.
                              """,
                      "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/auditing-it-system-users/overview/",
                      false,
                      false,
                      OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                      ProjectStatus.PLANNED,
                      SECURITY_MODERNIZATION,
                      new CocoProjectDefinition[]{ADD_CYBER_TOOLS_FOUNDATION},
                      null,
                      new PersonDefinition[]{PersonDefinition.SIDNEY_SEEKER, PersonDefinition.LEMMIE_STAGE, PersonDefinition.GARY_GEEKE, PersonDefinition.BOB_NITTER},
                      PersonDefinition.IVOR_PADLOCK),

    /**
     * CSM-CYBER-ATTACK-RESPONSE - Develop cyber attack response
     */
    CYBER_ATTACK_RESPONSE("Project::Develop cyber attack response",
                          "CSM-CYBER-ATTACK-RESPONSE",
                          "Develop cyber attack response",
                          "To define roles, responsibilities and actions to take in the event of a cyber attack.",
                          """
                                  This project runs considers each category of cyber attack, and lays out how it is likely to be detected, who is responsible for driving the response along with the procedures that they, and any other named person, must perform.  This includes any required notification to external regulators or governance authorities.
                                  """,
                          "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/building-a-data-security-strategy/overview/",
                          false,
                          false,
                          OpenMetadataType.GOVERNANCE_PROJECT_CLASSIFICATION.typeName,
                          ProjectStatus.PLANNED,
                          SECURITY_MODERNIZATION,
                          new CocoProjectDefinition[]{RECRUIT_CYBER_EXPERT, SECURITY_STRATEGY},
                          PersonDefinition.IVOR_PADLOCK,
                          new PersonDefinition[]{PersonDefinition.SIDNEY_SEEKER, PersonDefinition.LEMMIE_STAGE, PersonDefinition.GARY_GEEKE, PersonDefinition.FAITH_BROKER, PersonDefinition.TESSA_TUBE, PersonDefinition.STEW_FASTER},
                          PersonDefinition.ZACH_NOW),

    ;

    private final String                  qualifiedName;
    private final String                  identifier;
    private final String                  displayName;
    private final String                  mission;
    private final String                  description;
    private final String                  url;
    private final boolean                 isCampaign;
    private final boolean                 isTask;
    private final String                  projectTypeClassification;
    private final ProjectStatus           projectStatus;
    private final CocoProjectDefinition   controllingProject;
    private final CocoProjectDefinition[] dependentOn;
    private final PersonDefinition        leader;
    private final PersonDefinition[]      members;
    private final PersonDefinition        sponsor;


    /**
     * The constructor creates an instance of the enum
     *
     * @param qualifiedName             unique id for the  project
     * @param identifier                unique id for the project
     * @param displayName               name for the project
     * @param mission                   purpose of the project
     * @param description               background, approach, other interesting details
     * @param url                       link to the scenario
     * @param isCampaign                is this a collection of related projects
     * @param isTask                    is this a task within a project
     * @param projectTypeClassification should a classification be added to describe the type of project
     * @param projectStatus             what is the status of the project
     * @param controllingProject        project that this project reports to - can be null if the project is standalone or a campaign
     * @param dependentOn               projects that this project is dependent on
     * @param leader                    person to link into the leadership role
     * @param members                   list of people who are members of the team or organization
     */
    CocoProjectDefinition(String qualifiedName,
                          String identifier,
                          String displayName,
                          String mission,
                          String description,
                          String url,
                          boolean isCampaign,
                          boolean isTask,
                          String projectTypeClassification,
                          ProjectStatus projectStatus,
                          CocoProjectDefinition controllingProject,
                          CocoProjectDefinition[] dependentOn,
                          PersonDefinition leader,
                          PersonDefinition[] members,
                          PersonDefinition sponsor)
    {
        this.qualifiedName             = qualifiedName;
        this.identifier                = identifier;
        this.displayName               = displayName;
        this.mission                   = mission;
        this.description               = description;
        this.url                       = url;
        this.isCampaign                = isCampaign;
        this.isTask                    = isTask;
        this.projectTypeClassification = projectTypeClassification;
        this.projectStatus             = projectStatus;
        this.dependentOn               = dependentOn;
        this.controllingProject        = controllingProject;
        this.leader                    = leader;
        this.members                   = members;
        this.sponsor                   = sponsor;
    }


    /**
     * Return the qualified name for the project.
     *
     * @return string name
     */
    @Override
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Return the short identifier for the project.
     *
     * @return string name
     */
    @Override
    public String getIdentifier()
    {
        return identifier;
    }


    /**
     * Return the display name for the project.
     *
     * @return string name
     */
    @Override
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the mission for the project.
     *
     * @return string
     */
    @Override
    public String getMission()
    {
        return mission;
    }


    /**
     * Return the description for the project.
     *
     * @return text
     */
    @Override
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL for the project.
     *
     * @return string
     */
    @Override
    public String getURL()
    {
        return url;
    }


    /**
     * Is this project a campaign?
     *
     * @return boolean
     */
    @Override
    public boolean isCampaign()
    {
        return isCampaign;
    }


    /**
     * Is the project a task?
     *
     * @return boolean
     */
    @Override
    public boolean isTask()
    {
        return isTask;
    }


    /**
     * How should this project be classified?
     *
     * @return classification
     */
    @Override
    public String getProjectTypeClassification()
    {
        return projectTypeClassification;
    }


    /**
     * What is the project's current status?
     *
     * @return status
     */
    @Override
    public ProjectStatus getProjectStatus()
    {
        return projectStatus;
    }


    /**
     * Which project provides direction?
     *
     * @return project
     */
    @Override
    public ProjectDefinition getControllingProject()
    {
        return controllingProject;
    }


    /**
     * Which projects is this project dependent on?
     *
     * @return list of projects
     */
    @Override
    public List<ProjectDefinition> getDependentOn()
    {
        if (dependentOn != null)
        {
            return Arrays.asList(dependentOn);
        }

        return null;
    }


    /**
     * Who is the leader for this project?
     *
     * @return person
     */
    @Override
    public ActorDefinition getLeader()
    {
        return leader;
    }


    /**
     * Return the team members.
     *
     * @return list of people
     */
    @Override
    public List<ActorDefinition> getMembers()
    {
        if (members != null)
        {
            return Arrays.asList(members);
        }

        return null;
    }


    /**
     * Who is the exec sponsor for this project?
     *
     * @return person
     */
    @Override
    public ActorDefinition getSponsor()
    {
        return sponsor;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProjectDefinition{" + displayName + '}';
    }
}
