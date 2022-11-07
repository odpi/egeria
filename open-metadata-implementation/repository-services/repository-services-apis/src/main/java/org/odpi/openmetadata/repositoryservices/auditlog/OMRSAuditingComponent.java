/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

/**
 * OMRSAuditingComponent provides identifying and background information about the components writing log records
 * to the OMRS Audit log.  This is to help a consumer understand the OMRS Audit Log records.
 */
public enum OMRSAuditingComponent implements ComponentDescription
{
    UNKNOWN (0,
             ComponentDevelopmentStatus.IN_DEVELOPMENT,
             "<Unknown>", "Uninitialized component name", "https://egeria-project.org/services/"),

    AUDIT_LOG (1,
               ComponentDevelopmentStatus.STABLE,
               "Audit Log",
               "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
               "https://egeria-project.org/services/omrs/component-descriptions/audit-log/"),

    OPERATIONAL_SERVICES (3,
                          ComponentDevelopmentStatus.STABLE,
                          "Operational Services",
                          "Supports the administration services for the Open Metadata Repository Services (OMRS).",
                          "https://egeria-project.org/services/omrs/component-descriptions/operational-services/"),

    ARCHIVE_MANAGER (4,
                     ComponentDevelopmentStatus.STABLE,
                     "Archive Manager",
                     "Manages the loading of Open Metadata Archives into an open metadata repository.",
                     "https://egeria-project.org/services/omrs/component-descriptions/archive-manager/"),

    ENTERPRISE_CONNECTOR_MANAGER (5,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Enterprise Connector Manager",
                                  "Manages the list of open metadata repositories that the Enterprise OMRS Repository Connector " +
                                          "should call to retrieve an enterprise view of the metadata collections " +
                                          "supported by these repositories",
                                  "https://egeria-project.org/services/omrs/component-descriptions/enterprise-repository-connector/"),

    ENTERPRISE_REPOSITORY_CONNECTOR (6,
                                     ComponentDevelopmentStatus.STABLE,
                                     "Enterprise Repository Connector",
                                     "Supports enterprise access to the list of open metadata repositories registered " +
                                             "with the OMRS Enterprise Connector Manager.",
                                     "https://github.com/odpi/egeria/tree/main/open-metadata-implementation/repository-services/docs/component-descriptions/enterprise-repository-connector.md"),

    LOCAL_REPOSITORY_CONNECTOR (7,
                                ComponentDevelopmentStatus.STABLE,
                                "Local Repository Connector",
                                "Supports access to metadata stored in the local server's repository and ensures " +
                                        "repository events are generated when metadata changes in the local repository",
                                "https://egeria-project.org/services/omrs/component-descriptions/local-repository-connector/"),

    REPOSITORY_CONTENT_MANAGER(8,
                               ComponentDevelopmentStatus.STABLE,
                               "Repository Content Manager",
                               "Supports an in-memory cache for open metadata type definitions (TypeDefs) used for " +
                                       "verifying TypeDefs in use in other open metadata repositories and for " +
                                       "constructing new metadata instances.",
                               "https://egeria-project.org/services/omrs/repository-content-manager/"),

    INSTANCE_EVENT_PROCESSOR (9,
                              ComponentDevelopmentStatus.STABLE,
                              "Local Inbound Instance Event Processor",
                              "Supports the loading of reference metadata into the local repository that has come from other members of the local server's cohorts and open metadata archives.",
                              "https://egeria-project.org/services/omrs/component-descriptions/local-repository-instance-event-processor/"),

    REPOSITORY_EVENT_MANAGER (10,
                              ComponentDevelopmentStatus.STABLE,
                              "Repository Event Manager",
                              "Distribute repository events (TypeDefs, Entity and Instance events) between internal OMRS components within a server.",
                              "https://egeria-project.org/services/omrs/component-descriptions/event-manager/"),

    REST_SERVICES (11,
                   ComponentDevelopmentStatus.STABLE,
                   "Repository REST Services",
                   "Provides the server-side support of the OMRS Repository Services REST API.",
                   "https://egeria-project.org/services/omrs/component-descriptions/omrs-rest-services/"),

    REST_REPOSITORY_CONNECTOR (12,
                               ComponentDevelopmentStatus.STABLE,
                               "REST Repository Connector",
                               "Supports an OMRS Repository Connector for calling the OMRS Repository REST API in a remote open metadata repository.",
                               "https://egeria-project.org/concepts/repository-connector"),

    METADATA_HIGHWAY_MANAGER (13,
                              ComponentDevelopmentStatus.STABLE,
                              "Metadata Highway Manager",
                              "Manages the initialization and shutdown of the components that connector to each of the cohorts that the local server is a member of.",
                              "https://egeria-project.org/services/omrs/component-descriptions/metadata-highway-manager/"),

    COHORT_MANAGER  (14,
                     ComponentDevelopmentStatus.STABLE,
                     "Cohort Manager",
                     "Manages the initialization and shutdown of the server's connectivity to a cohort.",
                     "https://egeria-project.org/services/omrs/component-descriptions/cohort-manager/"),

    COHORT_REGISTRY(15,
                    ComponentDevelopmentStatus.STABLE,
                    "Cohort Registry",
                    "Manages the registration requests send and received from this local repository.",
                    "https://egeria-project.org/services/omrs/component-descriptions/cohort-registry/"),

    REGISTRY_STORE  (16,
                     ComponentDevelopmentStatus.STABLE,
                     "Registry Store",
                     "Stores information about the repositories registered in the open metadata repository cohort.",
                     "https://egeria-project.org/concepts/cohort-registry-store-connector/"),

    EVENT_PUBLISHER (17,
                     ComponentDevelopmentStatus.STABLE,
                     "Event Publisher",
                     "Manages the publishing of events that this repository sends to the OMRS topic.",
                     "https://egeria-project.org/services/omrs/component-descriptions/event-publisher/"),

    EVENT_LISTENER  (18,
                     ComponentDevelopmentStatus.STABLE,
                     "Event Listener",
                     "Manages the receipt of incoming OMRS events.",
                     "https://egeria-project.org/services/omrs/component-descriptions/event-listener/"),

    OMRS_TOPIC_CONNECTOR(19,
                         ComponentDevelopmentStatus.STABLE,
                         "OMRS Topic Connector",
                         "Provides access to the OMRS Topic that is used to exchange events between members of a cohort, " +
                                 "or to notify Open Metadata Access Services (OMASs) of changes to " +
                                 "metadata in the enterprise.",
                         "https://egeria-project.org/services/omrs/component-descriptions/connectors/omrs-topic-connector/"),

    OPEN_METADATA_TOPIC_CONNECTOR(20,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Open Metadata Topic Connector",
                                  "Provides access to an event bus to exchange events with participants in the open metadata ecosystem.",
                                  "https://egeria-project.org/concepts/open-metadata-topic-connector/"),

    LOCAL_REPOSITORY_EVENT_MAPPER(21,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Local Repository Event Mapper Connector",
                                  "Provides access to an event bus to process events from a specific local repository.",
                                  "https://egeria-project.org/concepts/event-mapper-connector/"),

    ARCHIVE_STORE_CONNECTOR(22,
                            ComponentDevelopmentStatus.IN_DEVELOPMENT,
                            "Open Metadata Archive Store Connector",
                            "Reads and writes open metadata types and instances to an open metadata archive.",
                            "https://egeria-project.org/concepts/open-metadata-archive-store-connector/"),

    REMOTE_REPOSITORY_CONNECTOR(23,
                                ComponentDevelopmentStatus.STABLE,
                                "Cohort Member Client Open Metadata Repository Connector",
                                "Provides access to open metadata located in a remote repository for remote members of a cohort.",
                                "https://egeria-project.org/concepts/cohort-member-client-connector/"),

    OMAS_OUT_TOPIC(24,
                   ComponentDevelopmentStatus.STABLE,
                   "Open Metadata Access Service (OMAS) Out Topic",
                   "Publishes events from a specific access service.",
                   "https://egeria-project.org/concepts/out-topic/"),

    OMAS_IN_TOPIC(25,
                  ComponentDevelopmentStatus.STABLE,
                  "Open Metadata Access Service (OMAS) In Topic",
                  "Receives events from external servers and tools directed at a specific access service.",
                  "https://egeria-project.org/concepts/in-topic/"),

    ENTERPRISE_TOPIC_LISTENER(26,
                              ComponentDevelopmentStatus.STABLE,
                              "Enterprise Topic Listener",
                              "Receives events from the open metadata repository cohorts that this server is registered with and distributes " +
                                      "them to the Open Metadata Access Services (OMASs).",
                              "https://egeria-project.org/concepts/cohort-events/"),

    REPOSITORY_CONNECTOR(27,
                         ComponentDevelopmentStatus.STABLE,
                         "OMRS Repository Connector",
                         "Maps open metadata calls to a metadata repository.",
                         "https://egeria-project.org/concepts/repository-connector/"),

    OPEN_DISCOVERY_SERVICE_CONNECTOR(28,
                                     ComponentDevelopmentStatus.TECHNICAL_PREVIEW,
                                     "Open Discovery Service Connector",
                                     "A connector that analyzing the contents of a digital resource.",
                                     "https://egeria-project.org/guides/developer/open-discovery-services/overview/"),

    GOVERNANCE_ACTION_SERVICE_CONNECTOR(29,
                                        ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                        "Governance Action Service Connector",
                                        "A connector that coordinates governance of digital resources and metadata.",
                                        "https://egeria-project.org/guides/developer/governance-action-services/overview/"),

    REPOSITORY_GOVERNANCE_SERVICE_CONNECTOR(30,
                                            ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                            "Repository Governance Service Connector",
                                            "A connector that dynamically governs the activity of the open metadata repositories.",
                                            "https://egeria-project.org/guides/developer/repository-governance-services/overview/"),

    INTEGRATION_CONNECTOR(31,
                          ComponentDevelopmentStatus.IN_DEVELOPMENT,
                          "Integration Connector",
                          "Connector that manages metadata exchange with a third party technology.",
                          "https://egeria-project.org/concepts/integration-connector/"),

    PLATFORM_SECURITY_CONNECTOR(32,
                                ComponentDevelopmentStatus.IN_DEVELOPMENT,
                                "Platform Metadata Security Connector",
                                "Connector that manages authorization requests to the OMAG Server Platform.",
                                "https://egeria-project.org/concepts/platform-metadata-security-connector/"),

    SERVER_SECURITY_CONNECTOR(33,
                              ComponentDevelopmentStatus.IN_DEVELOPMENT,
                              "Server Metadata Security Connector",
                              "Connector that manages authorization requests to the OMAG Server.",
                              "https://egeria-project.org/concepts/server-metadata-security-connector/"),
    ;


    private  final int                        componentId;
    private  final ComponentDevelopmentStatus componentDevelopmentStatus;
    private  final String                     componentName;
    private  final String                     componentDescription;
    private  final String                     componentWikiURL;


    /**
     * Set up the values of the enum.
     *
     * @param componentId code number for the component.
     * @param componentName name of the component used in the audit log record.
     * @param componentDescription short description of the component.
     * @param componentWikiURL URL  to the description of the component.
     */
    OMRSAuditingComponent(int                        componentId,
                          ComponentDevelopmentStatus componentDevelopmentStatus,
                          String                     componentName,
                          String                     componentDescription,
                          String                     componentWikiURL)
    {
        this.componentId = componentId;
        this.componentDevelopmentStatus = componentDevelopmentStatus;
        this.componentName = componentName;
        this.componentDescription = componentDescription;
        this.componentWikiURL = componentWikiURL;
    }


    /**
     * Return the numerical code for this component.
     *
     * @return int componentId
     */
    public int getComponentId()
    {
        return componentId;
    }


    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    public ComponentDevelopmentStatus getComponentDevelopmentStatus()
    {
        return componentDevelopmentStatus;
    }


    /**
     * Return the name of the component.  This is the name used in the audit log records.
     *
     * @return String component name
     */
    public String getComponentName()
    {
        return componentName;
    }


    /**
     * Return the short description of the component. This is an English description.  Natural language support for
     * these values can be added to UIs using a resource bundle indexed with the component id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    public String getComponentType()
    {
        return componentDescription;
    }


    /**
     * URL to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @return String URL
     */
    public String getComponentWikiURL()
    {
        return componentWikiURL;
    }


    /**
     * toString, JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSAuditingComponent{" +
                       "componentId=" + componentId +
                       ", componentDevelopmentStatus=" + componentDevelopmentStatus +
                       ", componentName='" + componentName + '\'' +
                       ", componentDescription='" + componentDescription + '\'' +
                       ", componentWikiURL='" + componentWikiURL + '\'' +
                       ", componentType='" + getComponentType() + '\'' +
                       '}';
    }}
