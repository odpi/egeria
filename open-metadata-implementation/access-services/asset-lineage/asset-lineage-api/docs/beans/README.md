<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

## Asset Lineage OMAS Beans

The Asset Lineage OMAS beans provide the object structures for sending related collections of properties.
The beans are used on APIs and in events.

Each event bean is annotated with Jackson databind annotations so it can be serialized between JSON and Java.

The event beans are all related and ultimately inherit from the **AssetLineageEventHeader**.
This bean with is abstract class.
The others appear in the API or form part of an event structure.

The beans used for events are:
* LineageEvent
* LineageRelationshipEvent

The beans use on APIs are:

* AssetContext - provides the entire lineage graph with vertices and edges
* GraphContext - contains two LineageEntity and the relationship between them
* LineageEntity - represents the single node in lineage graph with self contained properties
* LineageRelationship - represents the a lineage relationship in the lineage graph with self contained properties.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.