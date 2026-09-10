<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![Released](../../../images/egeria-content-status-released.png)

# Open Integration Framework (OIF)

The [Open Integration Framework (OIF)](https://egeria-project.org/frameworks/oif/overview/) provides interface definitions
and classes to enable connectors to exchange metadata with third party technologies.

## Integration connector interfaces

The OIF is built on the [Open Connector Framework (OCF)](https://egeria-project.org/frameworks/ocf/overview).  As such, it offers interfaces for the integration connector and the associated connector provider that extend the *Connector* and *ConnectorProvider* OCF interfaces respectively.

Specifically it offers:

* *IntegrationConnector* is the interface that is specific to an integration connector.
* *IntegrationConnectorBase* is the base class for an integration connector.  It provides implementations of the *initialize()*, *start()* and *disconnect()* methods as well as useful functions needed by many integration connectors.
* *IntegrationConnectorProvider* is the base class for the integration connector's [connector provider](https://egeria-project.org/concepts/connector-provider).

## Integration context

The *IntegrationContext* class provides the base class for the context object that provides open metadata services to the integration connector.  The integration context is passed to the integration connector between *initialize()* and *start()* method calls.

## Catalog target

A catalog target is an open metadata element that the integration connector should use as a root element for its work.

## Permitted synchronization

The *PermittedSynchronization* enumeration can be used to limit the activities of a particular integration connector.  It is set in the integration connector's configuration and enforced by the context.

## Integration context manager

The context manager is responsible for setting up the integration context for the integration connector.

## Open Lineage support

[OpenLineage](https://openlineage.io/) is the LF AI and Data Foundation's open standard for reporting the runs of
data pipelines: which job ran, when, what it read and what it wrote.  The OIF is where Egeria's support for the
standard lives, so that any integration connector can receive, inspect, create and forward OpenLineage events
without depending on the OpenLineage client libraries.

The `openlineage` package provides:

* **Beans for the OpenLineage event model** at core spec 2-0-2: `OpenLineageRunEvent` (with `OpenLineageRun`,
  `OpenLineageJob`, `OpenLineageInputDataSet` and `OpenLineageOutputDataSet`), plus `OpenLineageJobEvent` and
  `OpenLineageDataSetEvent` for the job and dataset events that describe a pipeline independently of a run.
* **Beans for every standard facet** in the OpenLineage `spec/facets` directory, at its current version, named
  `OpenLineage<Facet><Kind>Facet` - run facets (parent, nominalTime, processing_engine, errorMessage,
  externalQuery, extractionError, jobDependencies, environmentVariables, executionParameters, tags, test), job
  facets (documentation, sql, sourceCode, sourceCodeLocation, jobType, ownership, tags, lineage), dataset facets
  (documentation, dataSource, schema, catalog, columnLineage, datasetType, version, lifecycleStateChange,
  ownership, storage, symlinks, tags, hierarchy, lineage, dataQualityMetrics), input facets (dataQualityAssertions,
  dataQualityMetrics, inputStatistics, subset) and output facets (outputStatistics, subset).
* **Beans for the custom facets registered in the OpenLineage facet registry** (`spec/registry`): Google Cloud's
  `gcp_composer_job`, `gcp_composer_run`, `gcp_dataproc` and `gcp_lineage`, and Apache Iceberg's
  `icebergScanReport` and `icebergCommitReport`.
* **Facet containers** (`OpenLineageRunFacets`, `OpenLineageJobFacets`, `OpenLineageDataSetFacets`,
  `OpenLineageInputDataSetInputFacets`, `OpenLineageOutputDataSetOutputFacets`) that hold the modelled facets in
  named properties and keep any other facet - an unregistered custom facet, or a standard facet newer than these
  beans - as a generic facet in `additionalProperties`.  Unknown properties inside a modelled facet are kept the
  same way, so an event survives a round trip through the beans without loss.  The facet keys that are not
  valid Java identifiers (`processing_engine`, `ordinal_position`, `trigger_rule`, ...) are mapped with Jackson
  annotations.
* **Listener and publisher interfaces**: an integration connector implements `OpenLineageEventListener` and
  registers it through the integration context to receive every event that reaches the integration daemon
  (run events through `processOpenLineageRunEvent`, job and dataset events through default methods so existing
  listeners are unaffected).  A connector that creates events publishes them through the context's
  `publishOpenLineageRunEvent`, `publishOpenLineageJobEvent` and `publishOpenLineageDataSetEvent` methods; the
  `OpenLineageListenerManager` implemented by the integration context manager parses raw JSON events, works out
  which kind of event they are, and fans them out to the registered listeners.

The unit tests in this module exercise every bean's contract (accessors, equality, JSON round trip, retention of
unknown properties) and parse a run event carrying every standard and registered facet.

The [Open Lineage integration connectors](../../adapters/open-connectors/integration-connectors/openlineage-integration-connectors)
build on this package: an event receiver for Kafka, log stores that forward events to files or an OpenLineage API,
a publisher that turns Egeria's own governance actions into OpenLineage events, and a cataloguer that maps the
events into open metadata.

## Bitol document support

The `bitol` package provides the bean classes for the [Bitol](https://github.com/bitol-io) standards:
the Open Data Contract Standard (ODCS, `bitol.odcs.DataContract`) and the Open Data Product Standard
(ODPS, `bitol.odps.DataProduct`), with the shared elements in `bitol.common`.  Both documents extend
`BitolDocument`, which selects the right subclass from the document's `kind` property.  The documents are
typically YAML; a Jackson `ObjectMapper` built on a `YAMLFactory` reads both YAML and JSON forms.

`bitol.BitolDocumentFormatter` parses and formats the documents; `bitol.BitolDocumentListener` is the interface an
integration connector implements to receive documents published to the integration daemon (via
`IntegrationContext.registerBitolListener`), and `IntegrationContext.publishDataContract`/`publishDataProduct` publish them.

The `bitol.mapping` package holds the mapping between the documents and open metadata.  `DataContractMapper` and
`DataProductMapper` catalog a document as an `Agreement` (classified as a `DataSharingAgreement`) or a `DigitalProduct`;
`DataContractGenerator` and `DataProductGenerator` reverse the mapping to produce a document from an element.  All four
work through a `ConnectorContextBase`, so they can be used from an integration connector or from a view service.

----
Return to [frameworks](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.