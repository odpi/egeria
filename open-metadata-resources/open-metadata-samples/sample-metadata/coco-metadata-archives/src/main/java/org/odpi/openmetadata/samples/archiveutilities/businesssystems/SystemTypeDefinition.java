/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.businesssystems;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

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
          new String[]{OpenMetadataType.APPLICATION.typeName}),

    /**
     * homegrown-application-server - "Server deployed as part of a bespoke application written by Coco Pharmaceuticals.
     */
    HOME_GROWN_APP_SERVER("homegrown-application-server",
         "Homegrown application server",
         "Server deployed as part of a bespoke application written by Coco Pharmaceuticals.",
         "These servers drive specialized aspects of the business.",
         new String[]{OpenMetadataType.APPLICATION.typeName}),


    /**
     * etl-engine - Server deployed to run jobs that copy and transform data from one system to another.
     */
    ETL_ENGINE("etl-engine",
         "Extract-Transform-Load (ETL) engine",
         "Server deployed to run jobs that copy and transform data from one system to another.",
         "These servers are part of a middleware deployment.",
         new String[]{OpenMetadataType.DATA_MOVEMENT_ENGINE.typeName}),

    /**
     * database-server - Database server providing a collection of data that can be flexibly queried.
     */
    DATABASE_SERVER("database-server",
         "Database Server",
         "Database server providing a collection of data that can be flexibly queried.",
         "These servers are part of a middleware deployment.",
          new String[]{OpenMetadataType.DATABASE_MANAGER.typeName}),

    /**
     * event-broker - Event broker server providing event notifications based on subscriptions.
     */
    EVENT_BROKER("event-broker",
         "Event Broker",
         "Event broker server providing event notifications based on subscriptions.",
         "These servers are part of a middleware deployment.",
          new String[]{OpenMetadataType.EVENT_BROKER.typeName}),

    /**
     * reporting-server - Server that delivers business reports.
     */
    REPORTING_SERVER("reporting-server",
         "Reporting Server",
         "Server that delivers business reports.",
         "These servers are part of a middleware deployment.",
         new String[]{OpenMetadataType.REPORTING_ENGINE.typeName}),


    /**
     * cloud-saas-service - Application or service hosted by an external cloud provider.
     */
    CLOUD_SAAS_SERVICE("cloud-saas-service",
               "External Business Cloud Service",
               "Application or service hosted by an external cloud provider.",
               "These servers are managed by an external organization.",
               null),
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

    /**
     * The constructor creates an instance of the enum
     *
     * @param preferredValue   unique id for the enum
     * @param displayName   name for the enum
     * @param description   description of the use of this value
     * @param usage   criteria for use
     * @param softwareServerCapabilities array of software server capabilities to associate with this server
     */
    SystemTypeDefinition(String   preferredValue,
                         String   displayName,
                         String   description,
                         String   usage,
                         String[] softwareServerCapabilities)
    {
        this.preferredValue = preferredValue;
        this.displayName = displayName;
        this.description = description;
        this.usage = usage;
        this.softwareServerCapabilities = softwareServerCapabilities;
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
     * This is the name of the open metadata type to use when creating the Host entity.
     *
     * @return string value
     */
    public String getOpenMetadataTypeName()
    {
        return OpenMetadataType.SOFTWARE_SERVER.typeName;
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
