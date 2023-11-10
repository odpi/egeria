/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

import java.io.Serializable;

/**
 * EngineServiceDescription provides a list of registered engine services.
 */
public enum EngineServiceDescription implements Serializable
{
    /**
     * Analyses the content of an asset's real world counterpart, generates annotations
     * in an open discovery report that is attached to the asset in the open metadata repositories.
     */
    ASSET_ANALYSIS_OMES(400,
                        ComponentDevelopmentStatus.IN_DEVELOPMENT,
                        "Asset Analysis",
                        "Asset Analysis OMES",
                        "asset-analysis",
                        "Analyses the content of an asset's real world counterpart, generates annotations " +
                                "in an open discovery report that is attached to the asset in the open metadata repositories.",
                        "https://egeria-project.org/services/omes/asset-analysis/overview/",
                        "Discovery Engine OMAS"),

    /**
     * Executes requested governance action services to monitor, assess and maintain metadata and its
     * real-world counterparts.
     */
    GOVERNANCE_ACTION_OMES(401,
                           ComponentDevelopmentStatus.IN_DEVELOPMENT,
                           "Governance Action",
                           "Governance Action OMES",
                           "governance-action",
                           "Executes requested governance action services to monitor, assess and maintain metadata and its " +
                                   "real-world counterparts.",
                           "https://egeria-project.org/services/omes/governance-action/overview/",
                           "Governance Engine OMAS"),

    /**
     * Dynamically governance open metadata repositories in the connected cohorts.
     */
    REPOSITORY_GOVERNANCE_OMES(402,
                               ComponentDevelopmentStatus.IN_DEVELOPMENT,
                               "Repository Governance",
                               "Repository Governance OMES",
                               "repository-governance",
                               "Dynamically governance open metadata repositories in the connected cohorts.",
                               "https://egeria-project.org/services/omes/repository-governance/overview/",
                               "Open Metadata Repository Services (OMRS)"),
    ;

    private static final long     serialVersionUID    = 1L;

    private final int                        engineServiceCode;
    private final ComponentDevelopmentStatus engineServiceDevelopmentStatus;
    private final String                     engineServiceName;
    private final String                     engineServiceFullName;
    private final String                     engineServiceURLMarker;
    private final String                     engineServiceDescription;
    private final String                     engineServiceWiki;
    private final String                     engineServicePartnerService;


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
     */
    EngineServiceDescription(int                        engineServiceCode,
                             ComponentDevelopmentStatus engineServiceDevelopmentStatus,
                             String                     engineServiceName,
                             String                     engineServiceFullName,
                             String                     engineServiceURLMarker,
                             String                     engineServiceDescription,
                             String                     engineServiceWiki,
                             String                     engineServicePartnerService)
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
     * Return the full name of the Open Metadata Access Service (OMAS) that this engine service is partnered with.
     *
     * @return  Full name of OMAS
     */
    public String getEngineServicePartnerService()
    {
        return engineServicePartnerService;
    }
}
