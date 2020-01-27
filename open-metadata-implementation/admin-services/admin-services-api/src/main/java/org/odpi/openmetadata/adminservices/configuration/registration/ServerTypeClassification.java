/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adminservices.configuration.registration;

/**
 * ServerTypeClassification manages a list of different server types.
 */
public enum ServerTypeClassification
{
    OMAG_SERVER("Open Metadata and Governance (OMAG) Server",
                "Generic name for a server that runs on the OMAG Platform",
                null),
    METADATA_SERVER("Metadata Server",
                    "Server that manages metadata from open metadata repositories either" +
                            "locally managed or connected through a cohort",
                    ServerTypeClassification.OMAG_SERVER),
    REPOSITORY_PROXY("Repository Proxy",
                     "Hosting environment for a repository connector acting as an adapter to a third party metadata server",
                     ServerTypeClassification.OMAG_SERVER),
    CONFORMANCE_SERVER("Conformance Test Server",
                       "Server that hosts the conformance test suite",
                       ServerTypeClassification.OMAG_SERVER),
    GOVERNANCE_SERVER("Governance Server",
                      "Server that hosts integration, management or governance function",
                      ServerTypeClassification.OMAG_SERVER),
    INTEGRATION_DAEMON("Integration Daemon",
                       "Governance server that hosts connectors that are exchanging metadata with third party technology.  " +
                               "These servers typically do not have their own API which is why they are called daemons",
                       ServerTypeClassification.GOVERNANCE_SERVER),
    ENGINE_HOST("Governance Engine Host",
                       "Governance server that hosts a specific engine that is managing workloads for an aspect of governance",
                       ServerTypeClassification.GOVERNANCE_SERVER),
    DISCOVERY_SERVER("Discovery Server",
                     "Server that hosts one of more discovery engines used to analyze asset contents" +
                             "and the associated metadata",
                     ServerTypeClassification.ENGINE_HOST),
    STEWARDSHIP_SERVER("Stewardship Server",
                       "Server that manages the selection and execution of stewardship actions",
                       ServerTypeClassification.ENGINE_HOST),
    OPEN_LINEAGE_SERVER("Open Lineage Server",
                       "Server that manages a warehouse of lineage information",
                       ServerTypeClassification.GOVERNANCE_SERVER),
    DATA_PLATFORM_SERVER("Data Platform Integration Server",
                        "Server that manages the extraction of technical metadata from data platforms " +
                                "and then stores it into a metadata server",
                        ServerTypeClassification.INTEGRATION_DAEMON),
    DATA_ENGINE_PROXY("Data Engine Proxy",
                         "Server that manages the extraction of metadata from a single data engine",
                         ServerTypeClassification.INTEGRATION_DAEMON),
    SECURITY_SYNC_SERVER("Security Sync Server",
                      "Server that manages the configuration for a security service using metadata settings",
                      ServerTypeClassification.INTEGRATION_DAEMON),
    SECURITY_OFFICER_SERVER("Security Officer Server",
                        "Server that manages complex security settings for assets",
                        ServerTypeClassification.GOVERNANCE_SERVER),
    VIRTUALIZER_SERVER("Virtualizer Server",
                         "Server that manages the configuration for a data virtualization platform",
                         ServerTypeClassification.INTEGRATION_DAEMON),
    ;

    private String serverTypeName;
    private String serverTypeDescription;
    private ServerTypeClassification superType;


    ServerTypeClassification(String                     serverTypeName,
                             String                     serverTypeDescription,
                             ServerTypeClassification   superType)
    {
        this.serverTypeName        = serverTypeName;
        this.serverTypeDescription = serverTypeDescription;
        this.superType             = superType;
    }


    /**
     * Returns the unique identifier for the error message.
     *
     * @return logMessageId
     */
    public String getServerTypeName()
    {
        return serverTypeName;
    }


    /**
     * Returns description of server type
     *
     * @return userAction String
     */
    public String getServerTypeDescription()
    {
        return serverTypeDescription;
    }


    /**
     * Returns super type of server - null for top level.
     *
     * @return systemAction String
     */
    public ServerTypeClassification getSuperType()
    {
        return superType;
    }
}
