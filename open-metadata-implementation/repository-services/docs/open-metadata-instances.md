<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Instances


An open metadata instance describes a single element of metadata.
There are two types of metadata instances in open metadata:

* **Entities** - these are the nodes (vertices) in the metadata graph.  They typically describe concepts, people,
places, things.

* **Relationships** - these are links (edges) in the metadata graph that show how the entities are related.

Entities can also be decorated with **classifications**.  These describe additional attributes of an entity
and can be used to identify entities that are similar in a specific aspect.

Every metadata instance is linked to an [open metadata type definition (TypeDef)](open-metadata-type-definitions.md)
that describes what it represents and the properties that may be stored in it.

Although not proper metadata instances, the classifications are also linked to a TypeDef to ensure they
are have a well defined meaning.


----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.