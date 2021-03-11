<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0217 Ports

Ports are used to define the interfaces of [Processes](0010-Base-Model.md).

![UML](0217-Ports.png#pagewidth)

- **PortImplementation** - at the most detailed level, a PortImplementation defines the specific
    interface of a process: for example, its expected inputs or produced outputs.
- **PortAlias** - a PortAlias provides a reference point to some other Port (either another PortAlias
    or a more detailed PortImplementation).
- **PortDelegation** - defines the parent-child relationship between Ports: for example, which PortImplementation
    a particular PortAlias delegates to.
- **ProcessPort** - defines the Port(s) that are used by a given Process as its interface(s).

## Further Information

* [Base definition of Process](0010-Base-Model.md)
* [LineageMapping relationships](0770-Lineage-Mapping.md)
* [Process Hierarchies](0215-Software-Components.md)


Return to [Area 2](Area-2-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.