/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


/**
 * The SystemTypeDefinition is used to set up the deployedImplementationType and cloud classifications.
 */
public enum SystemTypeDefinition
{
    /**
     * cots-application-server - Custom off-the-shelf application server
     */
    COTS_SERVER("cots-application-server",
         "Custom off-the-shelf application server",
         "Server deployed as part of a COTS application package.  Coco Pharmaceuticals has minimal control over the structure and naming of this type of software server.",
         "These servers drive the business.  They provide standard business functions.",
          new String[]{"Application"},
         "ApplicationServer"),

    /**
     * homegrown-application-server - "Server deployed as part of a bespoke application written by Coco Pharmaceuticals.
     */
    HOME_GROWN_APP_SERVER("homegrown-application-server",
         "Homegrown application server",
         "Server deployed as part of a bespoke application written by Coco Pharmaceuticals.",
         "These servers drive specialized aspects of the business.",
         new String[]{"Application"},
         "ApplicationServer"),


    /**
     * etl-engine - Server deployed to run jobs that copy and transform data from one system to another.
     */
    ETL_ENGINE("etl-engine",
         "Extract-Transform-Load (ETL) engine",
         "Server deployed to run jobs that copy and transform data from one system to another.",
         "These servers are part of a middleware deployment.",
         new String[]{"DataMovementEngine"},
         "IntegrationServer"),

    /**
     * database-server - Database server providing a collection of data that can be flexibly queried.
     */
    DATABASE_SERVER("database-server",
         "Database Server",
         "Database server providing a collection of data that can be flexibly queried.",
         "These servers are part of a middleware deployment.",
          new String[]{"DatabaseManager"},
         "DatabaseServer"),

    /**
     * event-broker - Event broker server providing event notifications based on subscriptions.
     */
    EVENT_BROKER("event-broker",
         "Event Broker",
         "Event broker server providing event notifications based on subscriptions.",
         "These servers are part of a middleware deployment.",
          new String[]{"EventBroker"},
         "IntegrationServer"),

    /**
     * reporting-server - Server that delivers business reports.
     */
    REPORTING_SERVER("reporting-server",
         "Reporting Server",
         "Server that delivers business reports.",
         "These servers are part of a middleware deployment.",
         new String[]{"ReportingEngine"},
         "ApplicationServer"),


    /**
     * metadata-access-store - Metadata server with a repository that is part of the open metadata ecosystem.
     */
    OPEN_METADATA_ACCESS_STORE("metadata-access-store",
        "Open Metadata Access Store",
        "Metadata server with a repository that is part of the open metadata ecosystem.",
        "These servers are part of a middleware deployment.",
         new String[]{"MetadataAccessService", "MetadataRepositoryService", "CohortMember"},
        "MetadataServer"),

    /**
     * metadata-access-point - Server supporting Open Metadata Access Services (OMASs) but no metadata repository. It is connected to the open metadata ecosystem via a cohort.
     */
    OPEN_METADATA_ACCESS_POINT("metadata-access-point",
         "Open Metadata Access Point",
         "Server supporting Open Metadata Access Services (OMASs) but no metadata repository. It is connected to the open metadata ecosystem via a cohort.",
         "These servers are part of a middleware deployment.",
           new String[]{"MetadataAccessService", "CohortMember"},
         null),


    /**
     * engine-host - Server hosting governance engine. It is part of the open metadata ecosystem.
     */
    ENGINE_HOST_SERVER("engine-host",
         "Open Metadata Engine Host Server",
         "Server hosting governance engine. It is part of the open metadata ecosystem.",
         "These servers are part of a middleware deployment.",
         new String[]{"EngineHostingService"},
         "GovernanceDaemon"),

    /**
     * integration-daemon - Server hosting integration connectors responsible for synchronizing metadata. It is part of the open metadata ecosystem.
     */
    INTEGRATION_DAEMON("integration-daemon",
         "Open Metadata Integration Daemon",
         "Server hosting integration connectors responsible for synchronizing metadata. It is part of the open metadata ecosystem.",
         "These servers are part of a middleware deployment.",
         new String[]{"MetadataIntegrationService"},
         "GovernanceDaemon"),

    /**
     * repository-proxy - Server hosting a repository connector responsible for synchronizing metadata from a third party metadata repository. It is part of the open metadata ecosystem.
     */
    REPOSITORY_PROXY("repository-proxy",
         "Open Metadata Repository Proxy",
         "Server hosting a repository connector responsible for synchronizing metadata from a third party metadata repository. It is part of the open metadata ecosystem.",
         "These servers are part of a middleware deployment.",
         new String[]{"MetadataIntegrationService", "CohortMember"},
         "RepositoryProxy"),

    /**
     * view-server - Open Metadata View Server
     */
    VIEW_SERVER("view-server",
                "Open Metadata View Server",
                "Server providing REST API implementations for a UI.",
                "These servers are part of a middleware deployment.",
                new String[]{"MetadataViewService"},
                null),

    /**
     * view-client - UI server supplying UI code (javascript/typescript) to the browser.
     */
    VIEW_CLIENT("view-client",
                "Open Metadata View Client",
                "UI server supplying UI code (javascript/typescript) to the browser.",
                "These servers are part of a middleware deployment.",
                null,
                "null"),

    /**
     * cloud-saas-service - Application or service hosted by an external cloud provider.
     */
    CLOUD_SAAS_SERVICE("cloud-saas-service",
               "External Business Cloud Service",
               "Application or service hosted by an external cloud provider.",
               "These servers are managed by an external organization.",
               null,
               "ApplicationServer"),
     ;

    public static final String validValueSetName = "SystemType";
    public static final String validValueSetDescription = "Describes the type of system.";
    public static final String validValueSetUsage = "Used to identify the types of software capabilities that are associated with this type of system.";
    public static final String validValueSetScope = "Used to tag system definitions.";

    private final String   preferredValue;
    private final String   displayName;
    private final String   description;
    private final String   usage;
    private final String[] softwareServerCapabilities;
    private final String   serverPurpose;

    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param usage   criteria for use
     * @param softwareServerCapabilities array of software server capabilities to associate with this server
     * @param serverPurpose purpose of this server
     */
    SystemTypeDefinition(String   preferredValue,
                         String   displayName,
                         String   description,
                         String   usage,
                         String[] softwareServerCapabilities,
                         String   serverPurpose)
    {
        this.preferredValue = preferredValue;
        this.displayName = displayName;
        this.description = description;
        this.usage = usage;
        this.softwareServerCapabilities = softwareServerCapabilities;
        this.serverPurpose = serverPurpose;
    }



    /**
     * This is the preferred value that applications should use for this valid value.
     *
     * @return string value
     */
    public String getQualifiedName()
    {
        return validValueSetName + "." + preferredValue;
    }



    /**
     * This is the preferred value that applications should use for this valid value.
     *
     * @return string value
     */
    public String getPreferredValue()
    {
        return preferredValue;
    }


    /**
     * Return the printable name.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the description of the value's meaning.
     *
     * @return string text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return how the value should be used.
     *
     * @return string text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Return the list of software server capabilities that should be associated with this system.
     *
     * @return array of strings (type names)
     */
    public String[] getSoftwareServerCapabilities()
    {
        return softwareServerCapabilities;
    }


    /**
     * Return the purpose of this system.  This is the type name of the server purpose classification.
     *
     * @return string type name
     */
    public String getServerPurpose()
    {
        return serverPurpose;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "SystemTypeDefinition{" + displayName + '}';
    }
}
