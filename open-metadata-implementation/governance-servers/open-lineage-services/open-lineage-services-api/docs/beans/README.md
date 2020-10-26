<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

## Open Lineage Services Beans

The Open Lineage Services beans provide the object structures for sending related collections of properties.
The beans are used on APIs.

Each bean is annotated with Jackson databind annotations so it can be serialized between JSON and Java.

The beans use on APIs are:

* LineageEdge -  contains a relationship between two vertices
* LineageQueryParameters - contains querying parameters for lineage
* LineageVertex - represents a single node in the lineage graph with self contained properties
* LineageVerticesAndEdges - represents a lineage subgraph in the lineage graph with self contained properties.
* Scope - represents the lineage graph type that will be returned. Allowed values: SOURCE_AND_DESTINATION, ULTIMATE_SOURCE, 
ULTIMATE_DESTINATION, END_TO_END, VERTICAL.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.