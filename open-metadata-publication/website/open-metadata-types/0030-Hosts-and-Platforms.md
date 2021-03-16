<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0030 Hosts and Platforms

The host and platform metadata entities provide a simple model for the
IT infrastructure (nodes, computers, etc) that data resources are hosted on.

![UML](0030-Hosts-and-Platforms.png#pagewidth)

**ITInfrastructure** is a type of [Asset](0010-Base-Model.md).

A **Host** is an IT Infrastructure concept associated with the hardware running the systems.
It provides a mechanism for describing a unit of hardware that provides the ability to host software servers.

The host can be linked to its location through the [AssetLocation](0025-Locations.md) relationship.

The **OperatingPlatform** is an informational structure to describe the
operating system of the host.
Many hosts could have the same operating platform.


Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.