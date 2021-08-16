<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0030 Hosts and Platforms

The host and platform metadata entities provide a simple model for the
IT infrastructure (nodes, computers, etc) that data resources are hosted on.

![UML](0030-Hosts-and-Platforms.png#pagewidth)

**ITInfrastructure** is a type of [Asset](0010-Base-Model.md) that supports the running
of software systems.

In today's systems, hardware is managed to get the maximum use out of it.
Therefore the concept of a **Host** is abstracted to describe a deployment environment
that has access to hardware and has a basic software stack, typically including the operating systems.

The host can be linked to its location through the [AssetLocation](0025-Locations.md) relationship.

The **OperatingPlatform** is an informational structure to describe the
hardware characteristics and software stack (operating system etc) of the host.
Details of the software stack can be captured in a [Collection](0021-Collections.md)
linked to the operating platform using the **OperatingPlatformManifest**.
The collection may contain many different types of details such as configuration files and software packages that can
organized into nested collections.
Collections that list software packages can be classified with the **SoftwarePackageManifest**
classification.

Many hosts could have the same operating platform.  This means it can be used to
represent standardized software stacks and which hosts
they have been deployed to.  Pipelines that manage the software stacks on these machines can use these elements to
manage the rollout and update of the different software packages.

## Related Models

[0035 Complex Hosts](0035-Complex-Hosts.md) describes how hardware is virtualized.
[0037 Software Server Platform](0037-Software-Server-Platforms.md) describes the software process that run on a host.

----

* Return to [Area 0](Area-0-models.md).
* Return to [Overview](.).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.