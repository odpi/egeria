/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

/**
 * MessageSetCategory organises the message sets into the directories of the documentation.  Each category
 * matches a part of the Egeria source tree so that a message set's documentation page sits in the same
 * relative place as the module that defines it.  The categories are tested in order and the first one whose
 * source path prefix matches is used, so more specific prefixes must appear before more general ones.
 */
public enum MessageSetCategory
{
    /**
     * Message sets defined by the Open Metadata and Governance (OMAG) frameworks.
     */
    FRAMEWORKS("open-metadata-implementation/frameworks",
               "frameworks",
               "Frameworks",
               "The frameworks define the interfaces and base classes that connectors, governance services and " +
                       "clients are built on.  Their message sets are inherited by every component that builds " +
                       "on them, so these messages appear widely."),

    /**
     * Message sets defined by the common services that are shared by the other Egeria services.
     */
    COMMON_SERVICES("open-metadata-implementation/common-services",
                    "common-services",
                    "Common Services",
                    "The common services provide the shared function - such as parameter validation, metadata " +
                            "security and the generic metadata handlers - that the rest of the Egeria services " +
                            "call.  Their messages surface through whichever service is running at the time."),

    /**
     * Message sets defined by the Open Metadata Access Services (OMASs).
     */
    ACCESS_SERVICES("open-metadata-implementation/access-services",
                    "access-services",
                    "Access Services",
                    "The access services provide the domain-specific APIs and events that run in a metadata " +
                            "access server."),

    /**
     * Message sets defined by the generic Open Metadata View Services (OMVSs).
     */
    GENERIC_VIEW_SERVICES("open-metadata-implementation/view-server-generic-services",
                          "view-server-generic-services",
                          "Generic View Services",
                          "The generic view services provide the REST APIs used by user interfaces to work with " +
                                  "any type of open metadata element."),

    /**
     * Message sets defined by the Open Metadata View Services (OMVSs).
     */
    VIEW_SERVICES("open-metadata-implementation/view-services",
                  "view-services",
                  "View Services",
                  "The view services provide the REST APIs used by user interfaces such as Egeria UI.  Each view " +
                          "service supports a particular type of user or task."),

    /**
     * Message sets defined by the Open Metadata Engine Services (OMESs).
     */
    ENGINE_SERVICES("open-metadata-implementation/engine-services",
                    "engine-services",
                    "Engine Services",
                    "The engine services run the governance services of a particular governance service type in " +
                            "an Engine Host server."),

    /**
     * Message sets defined by the governance servers that host connectors and governance services.
     */
    GOVERNANCE_SERVER_SERVICES("open-metadata-implementation/governance-server-services",
                               "governance-server-services",
                               "Governance Server Services",
                               "The governance server services host the connectors and governance services that " +
                                       "run outside of a metadata access server - such as the integration " +
                                       "daemon and the engine host."),

    /**
     * Message sets defined by the Open Metadata Repository Services (OMRS).
     */
    REPOSITORY_SERVICES("open-metadata-implementation/repository-services",
                        "repository-services",
                        "Repository Services",
                        "The Open Metadata Repository Services (OMRS) manage the exchange of metadata between " +
                                "the repositories of an open metadata repository cohort.  This is the oldest and " +
                                "largest set of messages in Egeria."),

    /**
     * Message sets defined by the administration services.
     */
    ADMIN_SERVICES("open-metadata-implementation/admin-services",
                   "admin-services",
                   "Administration Services",
                   "The administration services configure and control the servers running on the OMAG Server " +
                           "Platform."),

    /**
     * Message sets defined by the server operations services.
     */
    SERVER_OPERATIONS("open-metadata-implementation/server-operations",
                      "server-operations",
                      "Server Operations",
                      "The server operations services report on the servers that are running on an OMAG Server " +
                              "Platform."),

    /**
     * Message sets defined by the user security services.
     */
    USER_SECURITY("open-metadata-implementation/user-security",
                  "user-security",
                  "User Security",
                  "The user security services authenticate the callers of the OMAG Server Platform's REST APIs."),

    /**
     * Message sets defined by the connectors that catalog and survey data managers.
     */
    DATA_MANAGER_CONNECTORS("open-metadata-implementation/adapters/open-connectors/data-manager-connectors",
                            "connectors/data-manager-connectors",
                            "Data Manager Connectors",
                            "These connectors catalog and survey the contents of database servers and other data " +
                                    "managers."),

    /**
     * Message sets defined by the connectors that access the contents of data stores.
     */
    DATA_STORE_CONNECTORS("open-metadata-implementation/adapters/open-connectors/data-store-connectors",
                          "connectors/data-store-connectors",
                          "Data Store Connectors",
                          "These connectors provide access to the contents of files, folders and databases."),

    /**
     * Message sets defined by the integration connectors.
     */
    INTEGRATION_CONNECTORS("open-metadata-implementation/adapters/open-connectors/integration-connectors",
                           "connectors/integration-connectors",
                           "Integration Connectors",
                           "Integration connectors run in an integration daemon.  They keep the open metadata " +
                                   "ecosystem synchronized with the third party technologies that they monitor."),

    /**
     * Message sets defined by the connectors that call third party systems.
     */
    SYSTEM_CONNECTORS("open-metadata-implementation/adapters/open-connectors/system-connectors",
                      "connectors/system-connectors",
                      "System Connectors",
                      "These connectors call the APIs of third party systems such as Apache Atlas, Apache Kafka " +
                              "and the Egeria runtime itself."),

    /**
     * Message sets defined by the connectors used by the repository services.
     */
    REPOSITORY_SERVICES_CONNECTORS("open-metadata-implementation/adapters/open-connectors/repository-services-connectors",
                                   "connectors/repository-services-connectors",
                                   "Repository Services Connectors",
                                   "These connectors provide the pluggable implementations used by the repository " +
                                           "services - the metadata repositories, the audit log destinations, the " +
                                           "cohort registry stores and the open metadata archive stores."),

    /**
     * Message sets defined by the event bus connectors.
     */
    EVENT_BUS_CONNECTORS("open-metadata-implementation/adapters/open-connectors/event-bus-connectors",
                         "connectors/event-bus-connectors",
                         "Event Bus Connectors",
                         "These connectors send and receive events over the event bus - typically Apache Kafka."),

    /**
     * Message sets defined by the governance action connectors.
     */
    GOVERNANCE_ACTION_CONNECTORS("open-metadata-implementation/adapters/open-connectors/governance-action-connectors",
                                 "connectors/governance-action-connectors",
                                 "Governance Action Connectors",
                                 "These governance services run in an engine host to make changes to the open " +
                                         "metadata ecosystem and the resources it describes."),

    /**
     * Message sets defined by the survey action connectors that analyse files.
     */
    FILE_SURVEY_CONNECTORS("open-metadata-implementation/adapters/open-connectors/file-survey-connectors",
                           "connectors/file-survey-connectors",
                           "File Survey Connectors",
                           "These survey action services analyse the content of files and folders and record " +
                                   "what they find in a survey report."),

    /**
     * Message sets defined by the connectors that capture observability data about the open metadata ecosystem.
     */
    NANNY_CONNECTORS("open-metadata-implementation/adapters/open-connectors/nanny-connectors",
                     "connectors/nanny-connectors",
                     "Nanny Connectors",
                     "The nanny connectors harvest observability data from the open metadata ecosystem into a " +
                             "database so that the operation of Egeria itself can be analysed."),

    /**
     * Message sets defined by the connectors that generate insight reports.
     */
    LOVELACE_INSIGHTS("open-metadata-implementation/adapters/open-connectors/lovelace-insights",
                      "connectors/lovelace-insights",
                      "Lovelace Insights",
                      "These connectors analyse the harvested observability data and turn it into insight " +
                              "reports."),

    /**
     * Message sets defined by the connectors that generate documents from open metadata.
     */
    REPORT_GENERATING_CONNECTORS("open-metadata-implementation/adapters/open-connectors/report-generating-connectors",
                                 "connectors/report-generating-connectors",
                                 "Report Generating Connectors",
                                 "These connectors turn the contents of the open metadata ecosystem into " +
                                         "human-readable documents."),

    /**
     * Message sets defined by the connectors that retrieve secrets.
     */
    SECRETS_STORE_CONNECTORS("open-metadata-implementation/adapters/open-connectors/secrets-store-connectors",
                             "connectors/secrets-store-connectors",
                             "Secrets Store Connectors",
                             "These connectors supply the credentials that other connectors need when they call " +
                                     "a third party technology."),

    /**
     * Message sets defined by the connectors that enforce the metadata security rules.
     */
    METADATA_SECURITY_CONNECTORS("open-metadata-implementation/adapters/open-connectors/metadata-security-connectors",
                                 "connectors/metadata-security-connectors",
                                 "Metadata Security Connectors",
                                 "These connectors implement an organization's authorization rules for the " +
                                         "OMAG Server Platform and its servers."),

    /**
     * Message sets defined by the connectors that store the server configuration documents.
     */
    CONFIGURATION_STORE_CONNECTORS("open-metadata-implementation/adapters/open-connectors/configuration-store-connectors",
                                   "connectors/configuration-store-connectors",
                                   "Configuration Store Connectors",
                                   "These connectors store and retrieve the configuration documents of the " +
                                           "servers running on an OMAG Server Platform."),

    /**
     * Message sets defined by the connectors that issue REST API calls.
     */
    REST_CLIENT_CONNECTORS("open-metadata-implementation/adapters/open-connectors/rest-client-connectors",
                           "connectors/rest-client-connectors",
                           "REST Client Connectors",
                           "These connectors issue the REST API calls that Egeria's clients make to a remote " +
                                   "OMAG Server Platform."),

    /**
     * Message sets defined by the remaining connectors.
     */
    OTHER_CONNECTORS("open-metadata-implementation/adapters",
                     "connectors/other-connectors",
                     "Other Connectors",
                     "The remaining connectors shipped with Egeria."),

    /**
     * Message sets defined by the conformance suite.
     */
    CONFORMANCE_SUITE("open-metadata-conformance-suite",
                      "conformance-suite",
                      "Conformance Suite",
                      "The conformance suite tests whether a technology conforms to the open metadata " +
                              "specifications."),

    /**
     * Message sets defined by the samples.
     */
    SAMPLES("open-metadata-resources/open-metadata-samples",
            "samples",
            "Samples",
            "The sample connectors and governance services that are shipped with Egeria to illustrate how the " +
                    "interfaces are used."),

    /**
     * Message sets that do not match any of the other categories.
     */
    OTHER("",
          "other",
          "Other",
          "Message sets that are defined outside of the areas listed above."),

    ;

    private final String sourcePathPrefix;
    private final String directoryName;
    private final String displayName;
    private final String description;


    /**
     * Constructor.
     *
     * @param sourcePathPrefix start of the path (relative to the repository root) of the modules in this category
     * @param directoryName location of the documentation, relative to the root of the documentation directory
     * @param displayName name of the category used in the documentation
     * @param description explanation of the category used in the documentation
     */
    MessageSetCategory(String sourcePathPrefix, String directoryName, String displayName, String description)
    {
        this.sourcePathPrefix = sourcePathPrefix;
        this.directoryName    = directoryName;
        this.displayName      = displayName;
        this.description      = description;
    }


    /**
     * Return the start of the path (relative to the repository root) of the modules in this category.
     *
     * @return path prefix
     */
    public String getSourcePathPrefix() { return sourcePathPrefix; }


    /**
     * Return the location of the documentation, relative to the root of the documentation directory.
     *
     * @return directory name, which may include a "/" to create a nested directory
     */
    public String getDirectoryName() { return directoryName; }


    /**
     * Return the name of the category used in the documentation.
     *
     * @return display name
     */
    public String getDisplayName() { return displayName; }


    /**
     * Return the explanation of the category used in the documentation.
     *
     * @return description
     */
    public String getDescription() { return description; }


    /**
     * Return the category that the supplied source file belongs to.  The categories are tested in declaration
     * order and the first matching prefix wins, so the more specific categories are declared first.
     *
     * @param sourcePath path of the message set's source file, relative to the repository root
     * @return matching category - OTHER if nothing else matches
     */
    public static MessageSetCategory getCategory(String sourcePath)
    {
        for (MessageSetCategory category : MessageSetCategory.values())
        {
            if (sourcePath.startsWith(category.sourcePathPrefix))
            {
                return category;
            }
        }

        return OTHER;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MessageSetCategory{" + directoryName + "}";
    }
}
