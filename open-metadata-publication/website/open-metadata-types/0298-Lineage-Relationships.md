<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0298 Lineage Relationships

Lineage Relationships define the constructs used by Lineage.

![UML](0298-Lineage-Relationships.png#pagewidth)

- **ProcessHierarchy** defines a parent-child relationship between processes, which can be used to define
    more abstract processes that are comprised of lower-level processes; helping to support coarser- and finer-
    grained detail in Lineage.
- The **PortSchema** relationship defines the specific interface for a given Port by means of a SchemaType. This
    allows us to define in detail the specific fields, for example, expected by that interface.
- The **LineageMapping** relationship defines a connection between two SchemaAttributes. This allows us to capture
    the flow of information in Lineage from one Attribute (source) to another (target).

(See also [Ports](0290-Ports.md).)

Return to [Area 2](Area-2-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.