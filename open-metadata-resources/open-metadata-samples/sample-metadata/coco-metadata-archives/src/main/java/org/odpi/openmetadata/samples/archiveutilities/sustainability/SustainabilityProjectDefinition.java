/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.sustainability;

import org.odpi.openmetadata.frameworks.openmetadata.definitions.ActorDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.definitions.ProjectDefinition;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ProjectStatus;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoProjectDefinition;
import org.odpi.openmetadata.samples.archiveutilities.organization.PersonDefinition;

import java.util.Arrays;
import java.util.List;

/**
 * The ProjectDefinition is used to feed the definition of the projects and teams for Coco Pharmaceuticals scenarios.
 */
public enum SustainabilityProjectDefinition implements ProjectDefinition
{
    /**
     * Campaign coordinating the sustainability initiatives in Coco Pharmaceuticals.
     */
    CAMPAIGN("Campaign::Sustainability",
             "CAMP-SUS",
             "Sustainability Campaign",
             "Campaign coordinating the sustainability initiatives in Coco Pharmaceuticals.",
             "",
             "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
             true,
             false,
             null,
             ProjectStatus.ACTIVE,
             null,
             null,
             PersonDefinition.TOM_TALLY,
             new PersonDefinition[]{PersonDefinition.ERIN_OVERVIEW, PersonDefinition.STEW_FASTER},
             PersonDefinition.REGGIE_MINT),

    /**
     * Project to establish a sustainability focus and practices in Coco Pharmaceuticals.
     */
    BOOTSTRAP("Project::Sustainability Bootstrap",
              "SUS-BOOTSTRAP",
              "Sustainability Bootstrap Project",
              "Project to establish a sustainability focus and practices in Coco Pharmaceuticals.",
              "The aim is to show how Coco Pharmaceuticals can improve their sustainability by establishing a sustainability focus and practices.",
              "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
              false,
              false,
              null,
              ProjectStatus.ACTIVE,
              SustainabilityProjectDefinition.CAMPAIGN,
              null,
              PersonDefinition.TOM_TALLY,
              new PersonDefinition[]{PersonDefinition.ERIN_OVERVIEW, PersonDefinition.STEW_FASTER},
              PersonDefinition.REGGIE_MINT),

    /**
     * Define Scope and Approach for the Sustainability Initiative
     */
    GOVERNANCE_DEFINITIONS("Project::Define Scope and Approach for the Sustainability Initiative",
                           "SUS-GOVERNANCE-DEFINITIONS",
                           "Define Scope and Approach for the Sustainability Initiative",
                           "Define Scope and Approach for the Sustainability Initiative",
                           "Create the governance definitions for the sustainability initiative.  This includes the scope, approach, and governance roles.",
                           "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                           false,
                           true,
                           null,
                           ProjectStatus.ACTIVE,
                           SustainabilityProjectDefinition.BOOTSTRAP,
                           null,
                           PersonDefinition.TOM_TALLY,
                           null,
                           PersonDefinition.REGGIE_MINT),

    /**
     * Define Scope and Approach for the Sustainability Initiative
     */
    GLOSSARY("Project::Define the Sustainability Glossary",
                           "SUS-GLOSSARY",
                           "Define the Sustainability Glossary",
                           "Establish a glossary of terms covering sustainability related terminology that Coco employees need to understand.",
                           "Requires a review of the key sustainability topic and GHG Protocol documentation to extract key terms and convert them into Glossary terms.",
                           "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                           false,
                           true,
                           null,
                           ProjectStatus.ACTIVE,
                           SustainabilityProjectDefinition.BOOTSTRAP,
                           null,
                           PersonDefinition.TOM_TALLY,
                           new PersonDefinition[]{PersonDefinition.ERIN_OVERVIEW},
                           PersonDefinition.REGGIE_MINT),

    /**
     * Add bucharest location definitions
     */
    ADD_BUCHAREST("Task::Add bucharest location definitions",
                  "SUS-BUCHAREST",
                  "Add bucharest location definitions",
                  "Ensure that details of the Bucharest location are captured in the location definitions and associates reference data sets.",
                  "The Bucharest location is a very recent acquisition that has just closed.  It needs to be added to Egeria.",
                  "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                  false,
                  true,
                  null,
                  ProjectStatus.ACTIVE,
                  SustainabilityProjectDefinition.BOOTSTRAP,
                  null,
                  PersonDefinition.TOM_TALLY,
                  null,
                  PersonDefinition.REGGIE_MINT),

    /**
     * Add the sustainability information supply chain
     */
    ISC("Project::Add the sustainability information supply chain",
                  "SUS-ISC",
                  "Add the sustainability information supply chain",
                  "Create the sustainability information supply chain that delivers the sustainability dashboards.",
                  "Using existing data collected by the business, create a sustainability dashboard that can be used to show our current position. Over time, it would allow us to track the progress of the initiative.",
                  "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                  false,
                  false,
                  null,
                  ProjectStatus.ACTIVE,
                  SustainabilityProjectDefinition.BOOTSTRAP,
                  new ProjectDefinition[]{GLOSSARY},
                  PersonDefinition.POLLY_TASKER,
                  new PersonDefinition[]{PersonDefinition.BOB_NITTER, PersonDefinition.CALLIE_QUARTILE, PersonDefinition.PETER_PROFILE},
                  PersonDefinition.TOM_TALLY),

    /**
     * Design the sustainability information supply chain
     */
    COCO_SUS_DESIGN("Task::Design the Sustainability ODS",
                    "SUS-ODS-DESIGN",
                    "Design the sustainability ODS",
                    "Create the schema for the Operational Data Store (ODS) that supports sustainability reporting.",
                    "Working backwards from the needs of the sustainability dashboards, identify the data that is needed and organize it in a database schema.",
                    "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                    false,
                    false,
                    null,
                    ProjectStatus.ACTIVE,
                    SustainabilityProjectDefinition.ISC,
                    new ProjectDefinition[]{GLOSSARY, CocoProjectDefinition.DATA_HUB_DEFINITION},
                    PersonDefinition.POLLY_TASKER,
                    new PersonDefinition[]{PersonDefinition.BOB_NITTER, PersonDefinition.PETER_PROFILE, PersonDefinition.ERIN_OVERVIEW},
                    PersonDefinition.TOM_TALLY),

    /**
     * Design the sustainability information supply chain
     */
    ISC_DESIGN("Task::Design the sustainability information supply chain",
                  "SUS-ISC-DESIGN",
                  "Design the sustainability information supply chain",
                  "Create the outline of sustainability information supply chain that delivers the sustainability dashboards.",
                  "Identify the data that is needed, which systems this data comes from, where it will be assembled, and how with the data be delivered as a dashboard.",
                  "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                  false,
                  false,
                  null,
                  ProjectStatus.ACTIVE,
                  SustainabilityProjectDefinition.ISC,
                  new ProjectDefinition[]{GLOSSARY, COCO_SUS_DESIGN},
                  PersonDefinition.POLLY_TASKER,
                  new PersonDefinition[]{PersonDefinition.BOB_NITTER, PersonDefinition.CALLIE_QUARTILE, PersonDefinition.PETER_PROFILE},
                  PersonDefinition.TOM_TALLY),



    /**
     * Design the sustainability information supply chain
     */
    GHG_CALCULATIONS("Task::Implement the GHG Calculators",
                 "SUS-GHG-CALCULATORS",
                     "Implement the GHG Calculators",
                     "Create the python programs to calculate GHG emissions.",
                     "The GHG calculators defined by the GHG Protocols will be used to calculate GHG emissions.",
                     "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                     false,
                     false,
                     null,
                     ProjectStatus.ACTIVE,
                     SustainabilityProjectDefinition.ISC,
                     new ProjectDefinition[]{GLOSSARY, COCO_SUS_DESIGN},
                     PersonDefinition.CALLIE_QUARTILE,
                     null,
                     PersonDefinition.TOM_TALLY),

    /**
     * Integrate business systems into the sustainability information supply chain
     */
    SYSTEM_INTEGRATION("Task::Integrate business systems into the sustainability information supply chain",
               "SUS-SYSTEM-INTEGRATION",
               "Integrate business systems into the sustainability information supply chain",
               "Build data pipelines to load data from identified business systems into coco-ods.",
               "The `coco-ods` is a new database schema in the `coco-data-hub`.  It needs to be populated from the existing business systems using Apache Airflow data pipelines (DAGs).",
               "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
               false,
               true,
               null,
               ProjectStatus.ACTIVE,
               SustainabilityProjectDefinition.ISC,
               new ProjectDefinition[]{ISC_DESIGN, COCO_SUS_DESIGN},
               PersonDefinition.BOB_NITTER,
               null,
               PersonDefinition.POLLY_TASKER),

    /**
     * Build and load sustainability dashboard
     */
    DASHBOARD("Task::Build and load sustainability dashboard",
                       "SUS-DASHBOARD",
                       "Build and load sustainability dashboard",
                       "Extract sustainability information from coco-sus",
                       "The `coco-ods` is a new database schema in the `coco-data-hub`.  It needs to be populated from the existing business systems using Apache Airflow data pipelines (DAGs).",
                       "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                       false,
                       true,
                       null,
                       ProjectStatus.ACTIVE,
                       SustainabilityProjectDefinition.ISC,
                       new ProjectDefinition[]{SYSTEM_INTEGRATION, COCO_SUS_DESIGN},
                       PersonDefinition.PETER_PROFILE,
                       null,
                       PersonDefinition.POLLY_TASKER),



    /**
     * Set up the sustainability community
     */
    ADD_COMMUNITY("Task::Set up the sustainability community",
                  "SUS-COMMUNITY",
                  "Set up the sustainability community",
                  "Establish the sustainability community as a means to provide education and inspiration on the value of improving sustainability to Coco Pharmaceuticals employees .",
                  "Sustainability requires cross-company collaboration.  The sustainability community is a place to share ideas and best practices.",
                  "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                  false,
                  true,
                  null,
                  ProjectStatus.ACTIVE,
                  SustainabilityProjectDefinition.BOOTSTRAP,
                  new ProjectDefinition[]{ADD_BUCHAREST, GLOSSARY},
                  PersonDefinition.TOM_TALLY,
                  null,
                  PersonDefinition.FAITH_BROKER),

    /**
     * Set up the sustainability community
     */
    CFC_FREE("Project::Run the CFC-Free Initiative",
                  "SUS-CFC-FREE",
                  "Run the CFC-Free Initiative",
                  "Run an initiative to remove all CFCs from refrigeration units found on Coco premises.",
                  "This initiative is the first official step in the campaign to reduce the environmental impact of Coco Pharmaceuticals.  There is funding available to replace any non-compliant refrigeration units.  This project needs to identify the units that need replacing and arrange for their replacement.",
                  "https://egeria-project.org/practices/coco-pharmaceuticals/scenarios/sustainability-initiative/overview/",
                  false,
                  false,
                  null,
                  ProjectStatus.ACTIVE,
                  SustainabilityProjectDefinition.BOOTSTRAP,
                  new ProjectDefinition[]{ISC},
                  PersonDefinition.TOM_TALLY,
                  null,
                  PersonDefinition.REGGIE_MINT),


    ;


    private final String              qualifiedName;
    private final String              identifier;
    private final String              displayName;
    private final String              mission;
    private final String              description;
    private final String              url;
    private final boolean             isCampaign;
    private final boolean             isTask;
    private final String              projectTypeClassification;
    private final ProjectStatus       projectStatus;
    private final ProjectDefinition   controllingProject;
    private final ProjectDefinition[] dependentOn;
    private final PersonDefinition    leader;
    private final PersonDefinition[]  members;
    private final PersonDefinition    sponsor;

    /**
     * The constructor creates an instance of the enum
     *
     * @param qualifiedName   unique id for the  entity
     * @param identifier   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param isCampaign is this a collection of related projects
     * @param isTask is this a task within a project
     * @param projectTypeClassification should a classification be added to describe the type of project
     * @param projectStatus what is the status of the project
     * @param controllingProject project that this project reports to - can be null if the project is standalone or a campaign
     * @param dependentOn projects that this project is dependent on
     * @param leader person to link into the leadership role
     * @param members list of people who are members of the team or organization
     */
    SustainabilityProjectDefinition(String              qualifiedName,
                                    String              identifier,
                                    String              displayName,
                                    String              mission,
                                    String              description,
                                    String              url,
                                    boolean             isCampaign,
                                    boolean             isTask,
                                    String              projectTypeClassification,
                                    ProjectStatus       projectStatus,
                                    ProjectDefinition   controllingProject,
                                    ProjectDefinition[] dependentOn,
                                    PersonDefinition    leader,
                                    PersonDefinition[]  members,
                                    PersonDefinition    sponsor)
    {
        this.qualifiedName = qualifiedName;
        this.identifier = identifier;
        this.displayName = displayName;
        this.mission = mission;
        this.description = description;
        this.url = url;
        this.isCampaign = isCampaign;
        this.isTask = isTask;
        this.projectTypeClassification = projectTypeClassification;
        this.projectStatus = projectStatus;
        this.dependentOn = dependentOn;
        this.controllingProject = controllingProject;
        this.leader = leader;
        this.members = members;
        this.sponsor = sponsor;
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
    public boolean isCampaign() { return isCampaign; }


    /**
     * Is the project a task?
     *
     * @return boolean
     */
    @Override
    public boolean isTask() { return isTask; }


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
        return "SustainabilityProjectDefinition{" + displayName + '}';
    }
}
