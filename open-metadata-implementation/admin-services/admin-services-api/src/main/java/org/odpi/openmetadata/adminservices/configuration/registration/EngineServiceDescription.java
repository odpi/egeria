/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;

/**
 * EngineServiceDescription provides a list of registered engine services.  These engine services run on an Engine Host OMAG Server.
 */
public enum EngineServiceDescription
{
    /**
     * Analyses the content of an asset's real world counterpart (resource), generates annotations
     * in a discovery analysis report that is attached to the asset in the open metadata repositories.
     */
    ASSET_ANALYSIS_OMES(400,
                        ComponentDevelopmentStatus.DEPRECATED,
                        "Asset Analysis",
                        "Asset Analysis OMES",
                        "asset-analysis",
                        "Analyses the content of an asset's real world counterpart (resource), generates annotations " +
                                "in a discovery analysis report that is attached to the asset in the open metadata repositories.",
                        "https://egeria-project.org/services/omes/asset-analysis/overview/",
                        AccessServiceDescription.DISCOVERY_ENGINE_OMAS.getAccessServiceFullName(),
                        DeployedImplementationType.OPEN_DISCOVERY_ENGINE.getDeployedImplementationType(),
                        DeployedImplementationType.OPEN_DISCOVERY_SERVICE_CONNECTOR.getDeployedImplementationType()),

    /**
     * Executes requested governance action services to monitor, assess and maintain metadata and its real-world counterparts.
     */
    GOVERNANCE_ACTION_OMES(401,
                           ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                           "Governance Action",
                           "Governance Action OMES",
                           "governance-action",
                           "Executes requested governance action services to monitor, assess and maintain metadata and its " +
                                   "real-world counterparts.",
                           "https://egeria-project.org/services/omes/governance-action/overview/",
                           AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName(),
                           DeployedImplementationType.GOVERNANCE_ACTION_ENGINE.getDeployedImplementationType(),
                           DeployedImplementationType.GOVERNANCE_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),

    /**
     * Dynamically govern open metadata repositories in the connected cohorts.
     */
    REPOSITORY_GOVERNANCE_OMES(402,
                               ComponentDevelopmentStatus.IN_DEVELOPMENT,
                               "Repository Governance",
                               "Repository Governance OMES",
                               "repository-governance",
                               "Dynamically govern open metadata repositories in the connected cohorts.",
                               "https://egeria-project.org/services/omes/repository-governance/overview/",
                               CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(),
                               DeployedImplementationType.REPOSITORY_GOVERNANCE_ENGINE.getDeployedImplementationType(),
                               DeployedImplementationType.REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR.getDeployedImplementationType()),


    /**
     * Executes requested event action services to monitor, assess and maintain context events.
     */
    EVENT_ACTION_OMES(403,
                      ComponentDevelopmentStatus.IN_DEVELOPMENT,
                      "Event Action",
                      "Event Action OMES",
                      "event-action",
                      "Executes requested event action services to monitor, assess and maintain context events.",
                      "https://egeria-project.org/services/omes/event-action/overview/",
                      AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                      DeployedImplementationType.EVENT_ACTION_ENGINE.getDeployedImplementationType(),
                      DeployedImplementationType.EVENT_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),


    /**
     * Analyses the content of an asset's real world counterpart (resource), generates annotations
     * in a discovery analysis report that is attached to the asset in the open metadata repositories.
     */
    SURVEY_ACTION_OMES(400,
                        ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                        "Survey Action",
                        "Survey Action OMES",
                        "survey-action",
                        "Analyses the content of an asset's real world counterpart (resource), generates annotations " +
                                "in a survey report that is attached to the asset in the open metadata repositories.",
                        "https://egeria-project.org/services/omes/survey-action/overview/",
                        AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                        DeployedImplementationType.SURVEY_ACTION_ENGINE.getDeployedImplementationType(),
                        DeployedImplementationType.SURVEY_ACTION_SERVICE_CONNECTOR.getDeployedImplementationType()),
    ;


    private final int                        engineServiceCode;
    private final ComponentDevelopmentStatus engineServiceDevelopmentStatus;
    private final String                     engineServiceName;
    private final String                     engineServiceFullName;
    private final String                     engineServiceURLMarker;
    private final String                     engineServiceDescription;
    private final String                     engineServiceWiki;
    private final String                     engineServicePartnerService;
    private final String                     hostedGovernanceEngineType;
    private final String                     hostedGovernanceServiceType;


    /**
     * Default Constructor
     *
     * @param engineServiceCode ordinal for this engine service
     * @param engineServiceDevelopmentStatus development status
     * @param engineServiceName symbolic name for this engine service
     * @param engineServiceFullName full name for this engine service
     * @param engineServiceURLMarker string used in URLs
     * @param engineServiceDescription short description for this engine service
     * @param engineServiceWiki wiki page for the engine service for this engine service
     * @param engineServicePartnerService name of the OMAS that is partnered with this engine service
     * @param hostedGovernanceEngineType type of governance engine hosted by this service
     * @param hostedGovernanceServiceType type of governance service hosted by this service
     */
    EngineServiceDescription(int                        engineServiceCode,
                             ComponentDevelopmentStatus engineServiceDevelopmentStatus,
                             String                     engineServiceName,
                             String                     engineServiceFullName,
                             String                     engineServiceURLMarker,
                             String                     engineServiceDescription,
                             String                     engineServiceWiki,
                             String                     engineServicePartnerService,
                             String                     hostedGovernanceEngineType,
                             String                     hostedGovernanceServiceType)
    {
        /*
         * Save the values supplied
         */
        this.engineServiceCode              = engineServiceCode;
        this.engineServiceDevelopmentStatus = engineServiceDevelopmentStatus;
        this.engineServiceName              = engineServiceName;
        this.engineServiceFullName          = engineServiceFullName;
        this.engineServiceURLMarker         = engineServiceURLMarker;
        this.engineServiceDescription       = engineServiceDescription;
        this.engineServiceWiki              = engineServiceWiki;
        this.engineServicePartnerService    = engineServicePartnerService;
        this.hostedGovernanceEngineType     = hostedGovernanceEngineType;
        this.hostedGovernanceServiceType    = hostedGovernanceServiceType;
    }


    /**
     * Return the enum that corresponds with the supplied code.
     *
     * @param engineServiceCode requested code
     * @return enum
     */
    public static EngineServiceDescription getEngineServiceDefinition(int engineServiceCode)
    {
        for (EngineServiceDescription description : EngineServiceDescription.values())
        {
            if (engineServiceCode == description.getEngineServiceCode())
            {
                return description;
            }
        }

        return null;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getEngineServiceCode()
    {
        return engineServiceCode;
    }


    /**
     * Return the development status of the service.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getEngineServiceDevelopmentStatus()
    {
        return engineServiceDevelopmentStatus;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getEngineServiceName()
    {
        return engineServiceName;
    }


    /**
     * Return the formal name for this enum instance.
     *
     * @return String default name
     */
    public String getEngineServiceFullName()
    {
        return engineServiceFullName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     *
     * @return String default URL marker
     */
    public String getEngineServiceURLMarker()
    {
        return engineServiceURLMarker;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getEngineServiceDescription()
    {
        return engineServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this engine service.
     *
     * @return String URL for the wiki page
     */
    public String getEngineServiceWiki()
    {
        return engineServiceWiki;
    }


    /**
     * Return the description of the service that this engine service is partnered with.
     *
     * @return  Full name of related service
     */
    public String getEngineServicePartnerService()
    {
        return engineServicePartnerService;
    }


    /**
     * Return the type of governance engine that this engine service supports.
     *
     * @return engine deployed implementation type name
     */
    public String getHostedGovernanceEngineType()
    {
        return hostedGovernanceEngineType;
    }


    /**
     * Return the type of governance service that this engine service supports.
     *
     * @return governance service connector deployed implementation type name
     */
    public String getHostedGovernanceServiceType()
    {
        return hostedGovernanceServiceType;
    }
}
