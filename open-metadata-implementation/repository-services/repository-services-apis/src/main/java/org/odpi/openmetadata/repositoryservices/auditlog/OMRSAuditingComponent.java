/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDevelopmentStatus;

/**
 * OMRSAuditingComponent provides identifying and background information about the many of the core components writing log records
 * to the OMRS Audit log.  This is to help a consumer understand the OMRS Audit Log records.
 */
public enum OMRSAuditingComponent implements ComponentDescription
{
    /**
     * Unknown - Uninitialized component name
     */
    UNKNOWN (0,
             ComponentDevelopmentStatus.IN_DEVELOPMENT,
             "Unknown", "Uninitialized component name", "https://egeria-project.org/services/"),

    /**
     * Audit Log - Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.
     */
    AUDIT_LOG (1,
               ComponentDevelopmentStatus.STABLE,
               "Audit Log",
               "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
               "https://egeria-project.org/services/omrs/component-descriptions/audit-log/"),

    /**
     * Operational Services - Supports the administration services for the Open Metadata Repository Services (OMRS).
     */
    OPERATIONAL_SERVICES (2,
                          ComponentDevelopmentStatus.STABLE,
                          "Operational Services",
                          "Supports the platform services for the Open Metadata Repository Services (OMRS).",
                          "https://egeria-project.org/services/omrs/component-descriptions/operational-services/"),

    /**
     * Archive Manager - Supports the platform services for the Open Metadata Repository Services (OMRS).
     */
    ARCHIVE_MANAGER (3,
                     ComponentDevelopmentStatus.STABLE,
                     "Archive Manager",
                     "Manages the loading of Open Metadata Archives into an open metadata repository.",
                     "https://egeria-project.org/services/omrs/component-descriptions/archive-manager/"),

    /**
     * Enterprise Connector Manager - Manages the list of open metadata repositories that the Enterprise OMRS Repository Connector
     * should call to retrieve an enterprise view of the metadata collections supported by these repositories.
     */
    ENTERPRISE_CONNECTOR_MANAGER (4,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Enterprise Connector Manager",
                                  "Manages the list of open metadata repositories that the Enterprise OMRS Repository Connector " +
                                          "should call to retrieve an enterprise view of the metadata collections " +
                                          "supported by these repositories.",
                                  "https://egeria-project.org/services/omrs/component-descriptions/enterprise-repository-connector/"),

    /**
     * Enterprise Repository Connector - Supports enterprise access to the list of open metadata repositories registered with the OMRS Enterprise Connector Manager.
     */
    ENTERPRISE_REPOSITORY_CONNECTOR (5,
                                     ComponentDevelopmentStatus.STABLE,
                                     "Enterprise Repository Connector",
                                     "Supports enterprise access to the list of open metadata repositories registered " +
                                             "with the OMRS Enterprise Connector Manager.",
                                     "https://egeria-project.org/services/omrs/component-descriptions/enterprise-repository-connector/"),

    /**
     * Local Repository Connector - Supports access to metadata stored in the local server's repository and ensures repository events are generated when metadata changes in the local repository.
     */
    LOCAL_REPOSITORY_CONNECTOR (6,
                                ComponentDevelopmentStatus.STABLE,
                                "Local Repository Connector",
                                "Supports access to metadata stored in the local server's repository and ensures " +
                                        "repository events are generated when metadata changes in the local repository.",
                                "https://egeria-project.org/services/omrs/component-descriptions/local-repository-connector/"),

    /**
     * Repository Content Manager - Supports an in-memory cache for open metadata type definitions (TypeDefs) used for verifying TypeDefs in use
     * in other open metadata repositories and for constructing new metadata instances.
     */
    REPOSITORY_CONTENT_MANAGER(7,
                               ComponentDevelopmentStatus.STABLE,
                               "Repository Content Manager",
                               "Supports an in-memory cache for open metadata type definitions (TypeDefs) used for " +
                                       "verifying TypeDefs in use in other open metadata repositories and for " +
                                       "constructing new metadata instances.",
                               "https://egeria-project.org/services/omrs/repository-content-manager/"),

    /**
     * Local Inbound Instance Event Processor - Supports the loading of reference metadata into the local repository that has come from other
     * members of the local server's cohorts and open metadata archives.
     */
    INSTANCE_EVENT_PROCESSOR (8,
                              ComponentDevelopmentStatus.STABLE,
                              "Local Inbound Instance Event Processor",
                              "Supports the loading of reference metadata into the local repository that has come from other members of the local server's cohorts and open metadata archives.",
                              "https://egeria-project.org/services/omrs/component-descriptions/local-repository-instance-event-processor/"),

    /**
     * Repository Event Manager - Distribute repository events (TypeDefs, Entity and Instance events) between internal OMRS components within a server.
     */
    REPOSITORY_EVENT_MANAGER (9,
                              ComponentDevelopmentStatus.STABLE,
                              "Repository Event Manager",
                              "Distribute repository events (TypeDefs, Entity and Instance events) between internal OMRS components within a server.",
                              "https://egeria-project.org/services/omrs/component-descriptions/event-manager/"),

    /**
     * Repository REST Services - Provides the server-side support of the OMRS Repository Services REST API.
     */
    REST_SERVICES (10,
                   ComponentDevelopmentStatus.STABLE,
                   "Repository REST Services",
                   "Provides the server-side support of the OMRS Repository Services REST API.",
                   "https://egeria-project.org/services/omrs/component-descriptions/omrs-rest-services/"),

    /**
     * Metadata Highway Manager - Manages the initialization and shutdown of the components that connector to each of the cohorts that the local server is a member of.
     */
    METADATA_HIGHWAY_MANAGER (11,
                              ComponentDevelopmentStatus.STABLE,
                              "Metadata Highway Manager",
                              "Manages the initialization and shutdown of the components that connector to each of the cohorts that the local server is a member of.",
                              "https://egeria-project.org/services/omrs/component-descriptions/metadata-highway-manager/"),

    /**
     * Cohort Manager - Manages the initialization and shutdown of the server's connectivity to a cohort.
     */
    COHORT_MANAGER  (12,
                     ComponentDevelopmentStatus.STABLE,
                     "Cohort Manager",
                     "Manages the initialization and shutdown of the server's connectivity to a cohort.",
                     "https://egeria-project.org/services/omrs/component-descriptions/cohort-manager/"),

    /**
     * Cohort Registry - Manages the registration requests send and received from this local repository.
     */
    COHORT_REGISTRY(13,
                    ComponentDevelopmentStatus.STABLE,
                    "Cohort Registry",
                    "Manages the registration requests send and received from this local repository.",
                    "https://egeria-project.org/services/omrs/component-descriptions/cohort-registry/"),

    /**
     * Registry Store - Stores information about the repositories registered in the open metadata repository cohort.
     */
    REGISTRY_STORE  (14,
                     ComponentDevelopmentStatus.STABLE,
                     "Registry Store",
                     "Stores information about the repositories registered in the open metadata repository cohort.",
                     "https://egeria-project.org/concepts/cohort-registry-store-connector/"),

    /**
     * Event Publisher - Manages the publishing of events that this repository sends to the OMRS topic.
     */
    EVENT_PUBLISHER (15,
                     ComponentDevelopmentStatus.STABLE,
                     "Event Publisher",
                     "Manages the publishing of events that this repository sends to the OMRS topic.",
                     "https://egeria-project.org/services/omrs/component-descriptions/event-publisher/"),

    /**
     * Event Listener - Manages the receipt of incoming OMRS events.
     */
    EVENT_LISTENER  (16,
                     ComponentDevelopmentStatus.STABLE,
                     "Event Listener",
                     "Manages the receipt of incoming OMRS events.",
                     "https://egeria-project.org/services/omrs/component-descriptions/event-listener/"),

    /**
     * OMRS Topic Connector - Provides access to the OMRS Topic that is used to exchange events between members of a cohort,
     * or to notify Open Metadata Access Services (OMASs) of changes to metadata in the enterprise.
     */
    OMRS_TOPIC_CONNECTOR(17,
                         ComponentDevelopmentStatus.STABLE,
                         "OMRS Topic Connector",
                         "Provides access to the OMRS Topic that is used to exchange events between members of a cohort, " +
                                 "or to notify Open Metadata Access Services (OMASs) of changes to " +
                                 "metadata in the enterprise.",
                         "https://egeria-project.org/services/omrs/component-descriptions/connectors/omrs-topic-connector/"),

    /**
     * Open Metadata Topic Connector - Provides access to an event bus to exchange events with participants in the open metadata ecosystem.
     */
    OPEN_METADATA_TOPIC_CONNECTOR(18,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Open Metadata Topic Connector",
                                  "Provides access to an event bus to exchange events with participants in the open metadata ecosystem.",
                                  "https://egeria-project.org/concepts/open-metadata-topic-connector/"),

    /**
     * Local Repository Event Mapper Connector - Provides access to an event bus to process events from a specific local repository.
     */
    LOCAL_REPOSITORY_EVENT_MAPPER(19,
                                  ComponentDevelopmentStatus.STABLE,
                                  "Local Repository Event Mapper Connector",
                                  "Provides access to an event bus to process events from a specific local repository.",
                                  "https://egeria-project.org/concepts/event-mapper-connector/"),

    /**
     * Open Metadata Archive Store Connector - Reads and writes open metadata types and instances to an open metadata archive.
     */
    ARCHIVE_STORE_CONNECTOR(20,
                            ComponentDevelopmentStatus.STABLE,
                            "Open Metadata Archive Store Connector",
                            "Reads and writes open metadata types and instances to an open metadata archive.",
                            "https://egeria-project.org/concepts/open-metadata-archive-store-connector/"),

    /**
     * Cohort Member Client Open Metadata Repository Connector - Provides access to open metadata located in a remote repository for remote members of a cohort.
     */
    REMOTE_REPOSITORY_CONNECTOR(21,
                                ComponentDevelopmentStatus.STABLE,
                                "Cohort Member Client Open Metadata Repository Connector",
                                "Provides access to open metadata located in a remote repository for remote members of a cohort.",
                                "https://egeria-project.org/concepts/cohort-member-client-connector/"),


    /**
     * Enterprise Topic Listener - Receives events from the open metadata repository cohorts that this server is registered with and distributes
     * them to the Open Metadata Access Services (OMASs).
     */
    ENTERPRISE_TOPIC_LISTENER(22,
                              ComponentDevelopmentStatus.STABLE,
                              "Enterprise Topic Listener",
                              "Receives events from the open metadata repository cohorts that this server is registered with and distributes " +
                                      "them to the Open Metadata Access Services (OMASs).",
                              "https://egeria-project.org/concepts/cohort-events/"),

    /**
     * OMRS Repository Connector - Maps open metadata calls to a metadata repository.
     */
    REPOSITORY_CONNECTOR(23,
                         ComponentDevelopmentStatus.STABLE,
                         "OMRS Repository Connector",
                         "Maps open metadata calls to a metadata repository.",
                         "https://egeria-project.org/concepts/repository-connector/"),
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
    @Override
    public int getComponentId()
    {
        return componentId;
    }


    /**
     * Return the development status of the component.
     *
     * @return enum describing the status
     */
    @Override
    public ComponentDevelopmentStatus getComponentDevelopmentStatus()
    {
        return componentDevelopmentStatus;
    }


    /**
     * Return the name of the component.  This is the name used in the audit log records.
     *
     * @return String component name
     */
    @Override
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
    @Override
    public String getComponentDescription()
    {
        return componentDescription;
    }


    /**
     * URL to the wiki page that describes this component.  This provides more information to the log reader
     * on the operation of the component.
     *
     * @return String URL
     */
    @Override
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
                       ", componentType='" + getComponentDescription() + '\'' +
                       '}';
    }}
