/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.auditlog;

import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;

/**
 * OMRSAuditingComponent provides identifying and background information about the components writing log records
 * to the OMRS Audit log.  This is to help someone reading the OMRS Audit Log understand the records.
 */
public enum OMRSAuditingComponent implements ComponentDescription
{
    UNKNOWN (0,
             "<Unknown>", "Uninitialized component name", "https://egeria.odpi.org"),

    AUDIT_LOG (1,
             "Audit Log",
             "Reads and writes records to the Open Metadata Repository Services (OMRS) audit log.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/audit-log.html"),

    OPERATIONAL_SERVICES (3,
             "Operational Services",
             "Supports the administration services for the Open Metadata Repository Services (OMRS).",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/operational-services.html"),

    ARCHIVE_MANAGER (4,
             "Archive Manager",
             "Manages the loading of Open Metadata Archives into an open metadata repository.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/archive-manager.html"),

    ENTERPRISE_CONNECTOR_MANAGER (5,
             "Enterprise Connector Manager",
             "Manages the list of open metadata repositories that the Enterprise OMRS Repository Connector " +
                                          "should call to retrieve an enterprise view of the metadata collections " +
                                          "supported by these repositories",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/enterprise-connector-manager.html"),

    ENTERPRISE_REPOSITORY_CONNECTOR (6,
             "Enterprise Repository Connector",
             "Supports enterprise access to the list of open metadata repositories registered " +
                                             "with the OMRS Enterprise Connector Manager.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/enterprise-repository-connector.html"),

    LOCAL_REPOSITORY_CONNECTOR (7,
             "Local Repository Connector",
             "Supports access to metadata stored in the local server's repository and ensures " +
                                        "repository events are generated when metadata changes in the local repository",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/local-repository-connector.html"),

    REPOSITORY_CONTENT_MANAGER(8,
             "Repository Content Manager",
             "Supports an in-memory cache for open metadata type definitions (TypeDefs) used for " +
             "verifying TypeDefs in use in other open metadata repositories and for " +
                             "constructing new metadata instances.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/typedef-manager.html"),

    INSTANCE_EVENT_PROCESSOR (9,
             "Local Inbound Instance Event Processor",
             "Supports the loading of reference metadata into the local repository that has come from other members of the local server's cohorts and open metadata archives.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/local-repository-instance-event-processor.html"),

    REPOSITORY_EVENT_MANAGER (10,
             "Repository Event Manager",
             "Distribute repository events (TypeDefs, Entity and Instance events) between internal OMRS components within a server.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/event-manager.html"),

    REST_SERVICES (11,
             "Repository REST Services",
             "Provides the server-side support the the OMRS Repository Services REST API.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/omrs-rest-services.html"),

    REST_REPOSITORY_CONNECTOR (12,
             "REST Repository Connector",
             "Supports an OMRS Repository Connector for calling the OMRS Repository REST API in a remote " +
                                       "open metadata repository.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/rest-repository-connector.html"),

    METADATA_HIGHWAY_MANAGER (13,
             "Metadata Highway Manager",
             "Manages the initialization and shutdown of the components that connector to each of the cohorts that the local server is a member of.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/metadata-highway-manager.html"),

    COHORT_MANAGER  (14,
             "Cohort Manager",
             "Manages the initialization and shutdown of the server's connectivity to a cohort.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/cohort-manager.html"),

    COHORT_REGISTRY(15,
             "Cohort Registry",
             "Manages the registration requests send and received from this local repository.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/cohort-registry.html"),

    REGISTRY_STORE  (16,
             "Registry Store",
             "Stores information about the repositories registered in the open metadata repository cohort.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/cohort-registry-store-connector.html"),

    EVENT_PUBLISHER (17,
             "Event Publisher",
             "Manages the publishing of events that this repository sends to the OMRS topic.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/event-publisher.html"),

    EVENT_LISTENER  (18,
             "Event Listener",
             "Manages the receipt of incoming OMRS events.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/event-listener.html"),

    OMRS_TOPIC_CONNECTOR(19,
             "OMRS Topic Connector",
             "Provides access to the OMRS Topic that is used to exchange events between members of a cohort, " +
                                 "or to notify Open Metadata Access Services (OMASs) of changes to " +
                                 "metadata in the enterprise.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/omrs-topic-connector.html"),

    OPEN_METADATA_TOPIC_CONNECTOR(20,
             "Open Metadata Topic Connector",
             "Provides access to an event bus to exchange events with participants in the open metadata ecosystem.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/open-metadata-topic-connector.html"),

    LOCAL_REPOSITORY_EVENT_MAPPER(21,
             "Local Repository Event Mapper Connector",
             "Provides access to an event bus to process events from a specific local repository.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/event-mapper-connector.html"),

    ARCHIVE_STORE_CONNECTOR(22,
             "Open Metadata Archive Store Connector",
             "Reads and writes open metadata types and instances to an open metadata archive.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/open-metadata-archive-store-connector.html"),

    REMOTE_REPOSITORY_CONNECTOR(23,
             "Remote Open Metadata Repository Connector",
             "Provides access to open metadata located in a remote repository.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/repository-connector.html"),

    OMAS_OUT_TOPIC(24,
             "Open Metadata Access Service (OMAS) Out Topic",
             "Publishes events from a specific access service.",
             "https://egeria.odpi.org/open-metadata-implementation/access-services/docs/concepts/client-server/out-topic.html"),

    OMAS_IN_TOPIC(25,
             "Open Metadata Access Service (OMAS) In Topic",
             "Receives events from external servers and tools directed at a specific access service.",
             "https://egeria.odpi.org/open-metadata-implementation/access-services/docs/concepts/client-server/in-topic.html"),

    ENTERPRISE_TOPIC_LISTENER(26,
             "Enterprise Topic Listener",
             "Receives events from the open metadata repository cohorts that this server is registered with and distributes " +
                                      "them to the Open Metadata Access Services (OMASs).",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/omrs-event-topic.html"),

    REPOSITORY_CONNECTOR(27,
             "OMRS Repository Connector",
             "Maps open metadata calls to a metadata repository.",
             "https://egeria.odpi.org/open-metadata-implementation/repository-services/docs/component-descriptions/connectors/repository-connector.html"),

    GRAPH_REPOSITORY_CONNECTOR(28,
             "Local Graph OMRS Repository Connector",
             "Native open metadata repository connector that maps open metadata calls to a JanusGraph based metadata repository.",
             "https://egeria.odpi.org/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection" +
                                       "-store-connectors/graph-repository-connector/"),

    INMEM_REPOSITORY_CONNECTOR(29,
             "In Memory OMRS Repository Connector",
             "Native open metadata repository connector that maps open metadata calls to a set of in memory hash maps - demo use only.",
             "https://egeria.odpi.org/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection" +
                                       "-store-connectors/inmemory-repository-connector/"),

    READ_ONLY_REPOSITORY_CONNECTOR(30,
             "Read Only OMRS Repository Connector",
             "Native open metadata repository connector that maps open metadata calls to an in memory cache.",
             "https://egeria.odpi.org/open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection" +
                                           "-store-connectors/inmemory-repository-connector/"),

    INTEGRATION_CONNECTOR(31,
             "Integration Connector",
             "Connector that manages metadata exchange with a third party technology.",
             "https://egeria.odpi.org/open-metadata-implementation/governance-servers/integration-daemon-services/"),
    ;


    private  int      componentId;
    private  String   componentName;
    private  String   componentDescription;
    private  String   componentWikiURL;


    /**
     * Set up the values of the enum.
     *
     * @param componentId code number for the component.
     * @param componentName name of the component used in the audit log record.
     * @param componentDescription short description of the component.
     * @param componentWikiURL URL link to the description of the component.
     */
    OMRSAuditingComponent(int    componentId,
                          String componentName,
                          String componentDescription,
                          String componentWikiURL)
    {
        this.componentId = componentId;
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
     * these values can be added to UIs using a resource bundle indexed with the component Id.  This value is
     * provided as a default if the resource bundle is not available.
     *
     * @return String description
     */
    public String getComponentType()
    {
        return componentDescription;
    }

    /**
     * URL link to the wiki page that describes this component.  This provides more information to the log reader
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
                ", componentName='" + componentName + '\'' +
                ", componentDescription='" + componentDescription + '\'' +
                ", componentWikiURL='" + componentWikiURL + '\'' +
                '}';
    }
}
