<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The Open Lineage Connectors

The [Open Lineage](https://openlineage.io) standard describes the runs of jobs, and the data sets they read and write,
as *run events*.  The connectors in this module acquire, create, process and distribute those events.  They are divided
into two groups:

- the integration connectors that are acquiring or creating the Open Lineage events (the *event receiver* and the
  *governance action publisher*);
- the integration connectors that are processing or distributing them (the *cataloguer* and the two *log stores*).

They are connected to each other by the integration daemon rather than by direct calls, which is what allows a new
source or a new destination of events to be added without changing the others.  The same layering is used for the
[Bitol connectors](../bitol-integration-connectors), which exchange data contract and data product documents.

## How the Open Lineage support is wired

The support is spread over the Open Integration Framework (OIF), the integration daemon, its clients and the view
services.  File references are relative to the repository root.

1. **Beans** - `open-metadata-implementation/frameworks/open-integration-framework/src/main/java/org/odpi/openmetadata/frameworks/integration/openlineage/`.
   `OpenLineageRunEvent` is the root; `OpenLineageFacet` is the abstract base for the facets and carries `_producer`,
   `_schemaURL` and an `additionalProperties` map for extensions.  The beans use Jackson's `PUBLIC_ONLY` visibility,
   `NON_NULL` inclusion and `ignoreUnknown`, so unknown facets are tolerated and round trip through the
   `additionalProperties`.
2. **Interfaces** - `OpenLineageEventListener` has one method that receives both the parsed bean and the raw event
   string; `OpenLineageListenerManager` registers listeners and publishes an event as a raw string or as a bean.
3. **Context manager** - `contextmanager/IntegrationContextManager` implements the listener manager for the whole
   integration daemon.  It parses the raw event with a Jackson `ObjectReader`, logs `OIF-CONNECTOR-0005` on a parse
   failure but still passes the raw string to the listeners with a null bean, and then fans the event out to every
   registered listener, catching and logging `OIF-CONNECTOR-0006` for each one so that one failing connector cannot
   stop the others receiving the event.
4. **Context** - `context/IntegrationContext` exposes `registerOpenLineageListener()` and two
   `publishOpenLineageRunEvent()` overloads to the connectors.  A connector never talks to the context manager
   directly.
5. **REST** - `IntegrationDaemonResource.publishOpenLineageEvent()` (in `integration-daemon-services-spring`) accepts a
   raw event body and publishes it into the daemon.  Above it is a chain that lets a view service reach any daemon by
   the GUID of its software server asset: the `IntegrationDaemon` client (`integration-daemon-services-client`) has
   `publishOpenLineageEvent(String)` and `publishOpenLineageEvent(OpenLineage.RunEvent)`, both posting to the same
   endpoint; `IntegrationDaemonConnector` in `egeria-system-connectors` wraps that client behind an OCF connector; and
   the **Runtime Manager** view service exposes `POST .../integration-daemons/{serverGUID}/open-lineage-events/publish-event-string`
   (raw body) and `.../publish-event` (typed body).  `RuntimeManagerRESTServices.publishOpenLineageEvent()` obtains the
   connector with `ConnectedAssetClient.getConnectorForAsset()`, checks that it is an `IntegrationDaemonConnector`,
   sets the delegating user, starts it, publishes and disconnects.  The typed path uses the official
   `io.openlineage:openlineage-java` client (`OpenLineage.RunEvent`) rather than the OIF beans; the OIF beans are only
   used inside the daemon.
6. **Connectors** - this module:
   * `OpenLineageEventReceiverIntegrationConnector` is a dynamic connector: each Kafka `Topic` catalog target becomes
     an `OpenLineageEventReceiverCatalogTargetProcessor` whose topic listener calls `publishOpenLineageRunEvent(rawEvent)`.
   * `OpenLineageCataloguerIntegrationConnector` registers as a listener and creates `Process` assets for jobs that are
     not yet catalogued.
   * `GovernanceActionOpenLineageIntegrationConnector` is the outbound direction: it listens to the Open Metadata
     OutTopic for `EngineAction` changes and publishes the run events it generates from them.
   * `FileBasedOpenLineageLogStoreConnector` and `APIBasedOpenLineageLogStoreConnector` are listeners that persist or
     forward every event they receive.
7. **Content pack** - `ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK`, `IntegrationGroupDefinition.OPEN_LINEAGE`, the
   five `IntegrationConnectorDefinition` entries, `OpenLineageArchiveWriter` (all in `core-content-pack`), and the
   `logs/openlineage` directory of the platform distribution, described in
   `open-metadata-distribution/omag-server-platform/docs/logs/openlineage/README.md`.

The sections that follow describe each connector and how to configure it.

## Open Lineage Event Receiver Integration Connector

The Open Lineage Event Receiver integration connector receives open lineage events from an event topic and publishes them to the lineage integration connectors with OpenLineage listeners registered in the same integration daemon instance.

![Figure 1](docs/open-lineage-event-receiver-integration-connector.svg)
> **Figure 1:** Operation of the Open Lineage event receiver integration connector

It uses an embedded [Open Metadata Topic Connector](https://egeria-project.org/concepts/open-metadata-topic-connector) to receive events from the topic.

### Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

The connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/servers/by-server-type/configuring-an-integration-daemon) is a *VirtualConnection* with an embedded [OpenMetadataTopicConnection](https://egeria-project.org/concepts/open-metadata-topic-connector).

```json linenums="1" hl_lines="11"
{
   "connection" : 
                { 
                    "class" : "VirtualConnection",
                    "qualifiedName" : "Egeria:IntegrationConnector:Lineage:OpenLineageEventReceiver Connection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationProvider"
                    },
                    "embeddedConnections" : [ {{topicConnection}} ]
                }
}
```
- Add the connection for the open metadata topic connector in the `embeddedConnections` section replacing {{topicConnection}}.  This will have the topic name in the endpoint's `networkAddress`.  The example below shows the connection for the [Kafka open metadata topic connector](https://egeria-project.org/connectors/resource/kafka-open-metadata-topic-connector) which supports events from [Apache Kafka](https://kafka.apache.org/).

```json linenums="1" hl_lines="11-40"
{
   "connection" : 
                { 
                    "class" : "VirtualConnection",
                    "qualifiedName" : "Egeria:IntegrationConnector:Lineage:OpenLineageEventReceiver Connection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationProvider"
                    },
                    "embeddedConnections" : [
                    {
                        "class": "EmbeddedConnection",
                        "embeddedConnection" : 
                        {
                            "class": "Connection",
                            "qualifiedName": "Kafka Open Metadata Topic Connector",
                            "connectorType":
                            {
                                "class": "ConnectorType",
                                "connectorProviderClassName": "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider"      
                            },
                            "endpoint":
                            {
                                "class": "Endpoint",
                                "address": {{openLineageTopicName}}
                            },
                            "configurationProperties": 
                            {
                                "producer": 
                                {
                                    "bootstrap.servers": {{kafkaEndpoint}}
                                },
                                "local.server.id": "{{localServerId}}",
                                "consumer":
                                {
                                    "bootstrap.servers": {{kafkaEndpoint}}
                                }
                            }
                        }
                    }]
                }
}
```

- Add the name of the topic in {{openLineageTopicName}}; the integration daemon's server id in {{localServerId}} and the endpoint for Apache Kafka (for example localhost:9092) in {{kafkaEndpoint}}.


## Open Lineage Cataloguer Integration Connector

The Open Lineage Cataloguer integration connector registers an OpenLineage listener with the integration daemon context and catalogues what the OpenLineage events describe: jobs become processes, datasets become data assets (with their schemas), and the inputs and outputs of each run are linked to the process with lineage relationships (DataFlow, plus LineageMapping for column-level lineage and ControlFlow/ProcessHierarchy for job dependencies and parent jobs).  It always keeps run metrics (counts, timings, data volumes) in the `RunMetrics` classification of the processes.  Optionally it also catalogues each run as an element, captures the statistics and data quality results carried in the events as annotations in a survey report, and maintains the DataScope classification on the data assets written by the runs.  The mapping, and an assessment of what is better derived from a historical log store, is described in [docs/open-lineage-cataloguing.md](docs/open-lineage-cataloguing.md).

The beans used to parse the events (in the Open Integration Framework) follow OpenLineage spec 2-0-2 and the current versions of all the standard facets; custom facets are retained as generic facets.

![Figure 2](docs/open-lineage-cataloguer-integration-connector.svg)
> **Figure 2:** Operation of the Open Lineage cataloguer integration connector


### Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/servers/configuring-an-integration-daemon).

```json linenums="1" hl_lines="14"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "Egeria:IntegrationConnector:Lineage:OpenLineageCataloguer Connection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageCataloguerIntegrationProvider"
                    },
                    "configurationProperties" :
                    {
                        "catalogRuns" : "false",
                        "catalogSchemas" : "true",
                        "captureStatistics" : "true",
                        "captureDataQuality" : "true",
                        "updateDataScope" : "true"
                    }
                }
}
```

The configuration properties are all optional booleans.  All default to `true` except `catalogRuns`, which defaults to `false`:

| Property | Controls |
|----------|----------|
| `catalogRuns` | A `TransientEmbeddedProcess` per run, owned by the job's process (default `false`). |
| `catalogSchemas` | A `TabularSchemaType` and `TabularColumn`s from the schema facet (needed for column-level lineage). |
| `captureStatistics` | `ResourceProfileAnnotation`s from the inputStatistics, outputStatistics and dataQualityMetrics facets. |
| `captureDataQuality` | `QualityAnnotation`s from the dataQualityAssertions and test facets. |
| `updateDataScope` | The `DataScope` classification on the data assets read and written by each run. |

Switch the last three off for high-frequency jobs and derive the equivalent metadata from a log store instead (see the design note).

A dataset reported with a `lifecycleStateChange` of `RENAME` has its asset's names updated in place; one reported with `DROP` has its asset archived or deleted according to the connector's configured delete method.

## Governance Action Open Lineage Integration Connector

The Governance Action Open Lineage integration connector listens for governance actions executing in the open metadata ecosystem and generates open lineage events for them and publish them to any integration connector running in the same integration daemon instance.

![Figure 3](docs/governance-action-open-lineage-integration-connector.svg)
> **Figure 3:** Operation of the File-based Open Lineage log store integration connector


### Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the Lintegration daemon](https://egeria-project.org/guides/admin/servers/by-server-type/configuring-an-integration-daemon).

```json linenums="1" hl_lines="14"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "Egeria:IntegrationConnector:Lineage:GovernanceActionOpenLineage Connection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openlineage.GovernanceActionOpenLineageIntegrationProvider"
                    }
                }
}
```

## File-based Open Lineage Log Store Integration Connector

The File-based Open Lineage Log Store integration connector stores open lineage events to the file system.

![Figure 4](docs/file-based-open-lineage-log-store-integration-connector.svg)
> **Figure 4:** Operation of the File-based Open Lineage log store integration connector


### Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/server/configuring-an-integration-daemon/).

```json linenums="1" hl_lines="14"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "Egeria:IntegrationConnector:Lineage:FileBasedOpenLineageLogStore Connection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openlineage.FileBasedOpenLineageLogStoreProvider"
                    },
                    "endpoint" :
                    {
                        "class" : "Endpoint",
                        "address" : "{{folderName}}"
                    }
                }
}
```

- Replace `{{folderName}}` with the path name of the folder where the files will be located.

## API-based Open Lineage Log Store Integration Connector


The API-based Open Lineage Log Store integration connector calls an OpenLineage compliant API to store open lineage events that have been published to the integration daemon instance where this connector is running.

![Figure 5](docs/api-based-open-lineage-log-store-integration-connector.svg)
> **Figure 5:** Operation of the API-based Open Lineage log store integration connector


### Configuration

This connector runs in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon).

This is its connection definition to use on the [administration commands that configure the integration daemon](https://egeria-project.org/guides/admin/configuring-an-integration-daemon/).

!!! example "Connection configuration"
```json linenums="1" hl_lines="14"
{
   "connection" : 
                { 
                    "class" : "Connection",
                    "qualifiedName" : "Egeria:IntegrationConnector:Lineage:APIBasedOpenLineageLogStore Connection",
                    "connectorType" : 
                    {
                        "class" : "ConnectorType",
                        "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.integration.openlineage.APIBasedOpenLineageLogStoreProvider"
                    },
                    "endpoint" :
                    {
                        "class" : "Endpoint",
                        "address" : "{{logStoreURL}}"
                    }
                }
}
```

    - Replace `{{logStoreURL}}` with the URL of the destination API.


----
* Return to [Integration Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.