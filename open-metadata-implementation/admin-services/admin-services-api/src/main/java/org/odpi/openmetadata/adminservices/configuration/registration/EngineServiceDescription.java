/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.registration;

import java.io.Serializable;

/**
 * EngineServiceDescription provides a list of registered engine services.
 */
public enum EngineServiceDescription implements Serializable
{
    ASSET_ANALYSIS_OMES(6000,
                        "Asset Analysis",
                        "Asset Analysis OMES",
                        "asset-analysis",
                        "Analyses the content of an asset's real world counterpart, generates annotations " +
                                "in an open discovery report that is attached to the asset in the open metadata repositories.",
                        "https://egeria.odpi.org/open-metadata-implementation/engine-services/asset-analysis/",
                        "Discovery Engine OMAS"),

    METADATA_WATCHDOG_OMES(6001,
                           "Metadata Watchdog",
                           "Metadata Watchdog OMES",
                           "metadata-watchdog",
                           "Monitors changes in the metadata and initiates updates as a result.  One example of a " +
                                   "watchdog service is detection of a new asset. Another example is to monitor the addition of " +
                                   "open discovery reports and kick off action on their content.  Examples of updates include " +
                                   "creating governance actions, governance action processes and incident reports.",
                           "https://egeria.odpi.org/open-metadata-implementation/engine-services/metadata-watchdog/",
                           "Governance Engine OMAS"),

    REQUEST_TRIAGE_OMES(6002,
                        "Request Triage",
                        "Request Triage OMES",
                        "request-triage",
                        "Decides how a particular situation should be handled.  This may involve a human decision maker.",
                        "https://egeria.odpi.org/open-metadata-implementation/engine-services/request-triage/",
                        "Governance Engine OMAS"),

    ISSUE_REMEDIATION_OMES(6003,
                           "Issue Remediation",
                           "Issue Remediation OMES",
                           "issue-remediation",
                           "Monitors for RemediationRequest instances and runs the requested remediation service. " +
                                   "Examples of remediation services are duplicate linking and consolidating.",
                           "https://egeria.odpi.org/open-metadata-implementation/engine-services/issue-remediation/",
                           "Governance Engine OMAS"),

    RULE_VERIFIER_OMES(6004,
                          "Rule Verifier",
                          "Rule Verifier OMES",
                          "rule-verifier",
                          "Validates that specific metadata elements are set up correctly.  For example, it could detect " +
                                  "assets that are not allocated to a governance zone.",
                          "https://egeria.odpi.org/open-metadata-implementation/engine-services/rule-verifier/",
                          "Governance Engine OMAS"),

    ASSET_PROVISIONING_OMES(6005,
                            "Asset Provisioning",
                            "Asset Provisioning OMES",
                            "asset-provisioning",
                            "Invokes a provisioning service whenever a provisioning request is made.  Typically the " +
                                    "provisioning service is an external service.  It may also create lineage metadata to " +
                                    "describe the work of the provisioning engine.",
                            "https://egeria.odpi.org/open-metadata-implementation/engine-services/asset-provisioning/",
                            "Governance Engine OMAS"),
    ;

    private static final long     serialVersionUID    = 1L;

    private int    engineServiceCode;
    private String engineServiceName;
    private String engineServiceFullName;
    private String engineServiceURLMarker;
    private String engineServiceDescription;
    private String engineServiceWiki;
    private String engineServicePartnerOMAS;


    /**
     * Default Constructor
     *
     * @param engineServiceCode ordinal for this engine service
     * @param engineServiceName symbolic name for this engine service
     * @param engineServiceFullName full name for this engine service
     * @param engineServiceURLMarker string used in URLs
     * @param engineServiceDescription short description for this engine service
     * @param engineServiceWiki wiki page for the engine service for this engine service
     * @param engineServicePartnerOMAS name of the OMAS that is partnered with this engine service
     */
    EngineServiceDescription(int    engineServiceCode,
                             String engineServiceName,
                             String engineServiceFullName,
                             String engineServiceURLMarker,
                             String engineServiceDescription,
                             String engineServiceWiki,
                             String engineServicePartnerOMAS)
    {
        /*
         * Save the values supplied
         */
        this.engineServiceCode         = engineServiceCode;
        this.engineServiceName         = engineServiceName;
        this.engineServiceFullName     = engineServiceFullName;
        this.engineServiceURLMarker    = engineServiceURLMarker;
        this.engineServiceDescription  = engineServiceDescription;
        this.engineServiceWiki         = engineServiceWiki;
        this.engineServicePartnerOMAS  = engineServicePartnerOMAS;
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
     * @return String URL name for the wiki page
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
    public String getEngineServicePartnerOMAS()
    {
        return engineServicePartnerOMAS;
    }
}
