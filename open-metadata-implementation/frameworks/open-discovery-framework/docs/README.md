<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Discovery Framework (ODF) Documentation

The Open Discovery Framework (ODF) provides components and interfaces for building
discovery services.   

A [Discovery Service](discovery-service.md) provides specific analysis of the metadata and contents of
an [Asset](../../../../open-metadata-implementation/access-services/docs/concepts/assets) on request.
The result is one or more sets of related properties that the discovery service has
discovered about the asset, its metadata, structure and/or content.
These are stored in a set of [Discovery Annotations](discovery-annotation.md) linked off of a
[Discovery Analysis Report](discovery-analysis-report.md).  The discovery analysis report
is linked off of the asset definition in the open metadata repository.

Discovery services run in a [Discovery Engine](discovery-engine.md) that is hosted in a
[Discovery Server](discovery-server.md).


## Core Concepts

* [Discovery Analysis Report](discovery-analysis-report.md)
* [Discovery Analysis Report Store](discovery-analysis-report-store.md)
* [Discovery Annotation](discovery-annotation.md)
* [Discovery Annotation Store](discovery-annotation-store.md)
* [Discovery Asset Catalog Store](discovery-asset-catalog-store.md)
* [Discovery Asset Store](discovery-asset-store.md)
* [Discovery Configuration Server](discovery-configuration-server.md)
* [Discovery Context](discovery-context.md)
* [Discovery Engine](discovery-engine.md)
* [Discovery Pipeline](discovery-pipeline.md)
* [Discovery Request Type](discovery-request-type.md)
* [Discovery Server](discovery-server.md)
* [Discovery Service](discovery-service.md)

## Egeria implementation

Egeria provides a full implementation of the ODF.  It provides a
[discovery server](../../../engine-services/asset-analysis)
as well as an implementation of the metadata server APIs by the
[Discovery Engine OMAS](../../../access-services/discovery-engine).
There are also implementations of discovery services in the
[discovery-service-connectors](../../../adapters/open-connectors/discovery-service-connectors)
module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.