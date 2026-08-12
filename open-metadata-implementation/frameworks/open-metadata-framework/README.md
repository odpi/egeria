<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Open Metadata Framework (OMF)

The Open Metadata Framework (OMF) provides the base definitions for the open metadata type system, and the
client-side classes that connectors and services use to create, retrieve, update and search for open metadata
elements and relationships.  It builds on the [Open Connector Framework (OCF)](../open-connector-framework) and
is, in turn, the foundation used by the other frameworks
([OIF](../open-integration-framework), [OGF](../open-governance-framework), [OSF](../open-survey-framework),
[OWF](../open-watchdog-framework)) to give their connectors access to open metadata.

More information is available on Egeria's [documentation site](https://egeria-project.org/frameworks/omf/overview/).

## Java Implementation

The Java implementation is located in packages under `org.odpi.openmetadata.frameworks.openmetadata`.  The most
significant packages are:

* **types** - the definitions of the open metadata types (entities, relationships and classifications).
* **enums** - the definitions of the EnumDefs that appear in the open metadata type system.
* **properties** - the bean classes that describe the properties passed to the server to create and update
  open metadata elements and relationships, organized into subpackages by subject area (for example
  `properties.assets`, `properties.schema`, `properties.governance`).
* **metadataelements** - the bean classes that describe how metadata is returned from the open metadata
  repositories.
* **connectorcontext** - the client classes used by connectors and services to work with open metadata, one per
  major type family (for example `AssetClient`, `CollectionClient`, `SchemaTypeClient`, `SchemaAttributeClient`,
  `ExternalIdClient`, `PropertyFacetClient`, `GlossaryTermClient`).  `ConnectorContextBase` provides the base
  class that hosts these clients and is extended by the other frameworks' connector contexts.
* **client** - the standard `OpenMetadataClient` interface for direct access to the open metadata repositories,
  implemented by the `OpenMetadataStore` class in **connectorcontext** and used by the connector context clients.
* **handlers** - specialised handlers, built on the Open Metadata Store Services client, that implement common
  patterns (create, update, search, classification management) shared across the connector context clients.
* **search** - classes for building search criteria and query options for open metadata queries.
* **controls** - the beans used to describe the behaviour of a component, such as template types, catalog
  target types and configuration property types.
* **refdata** - enums describing the default reference data (for example resource use, deployed implementation
  types) used by the open connectors supplied with Egeria.
* **definitions** - standard interfaces for working with collections of standard metadata definitions.
* **converters** and **builders** - translate between open metadata repository formats and the OMF bean classes.
* **events** - the definitions of the events that OMF-based services can publish.
* **mermaid** - common routines for building Mermaid Markdown visualizations of open metadata graphs.
* **fileclassifier** and **filelistener** - utilities for classifying files using metadata valid values, and for
  connectors that need to listen for changes to files.
* **ffdc** - the exceptions and audit log messages used by the framework.

----
Return to [frameworks](..).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
