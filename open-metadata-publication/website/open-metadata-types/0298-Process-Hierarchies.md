<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0298 - Process Hierarchies

Port Schemas describe how the structure of the data passed
through a processes ports is described.  This is important
in understanding how processes can be composed of other processes
and it provides the basis for detailed lineage.

![UML](0298-Process-Hierarchies.png#pagewidth)

**ProcessHierarchy** defines a parent-child relationship between processes, which can be used to define
    more abstract processes that are comprised of lower-level processes; helping to support navigating
    the process hierarchy.

(See also [Ports](0290-Ports.md).)

----
* Return to [Area 2](Area-2-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.