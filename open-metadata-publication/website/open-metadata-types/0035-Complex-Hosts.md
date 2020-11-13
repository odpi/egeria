<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0035 Complex Hosts

In today's systems, hardware is managed to get the maximum use out of it.
Therefore the concept of a Host is
typically virtualized to allow a single computer to be used for many
hosts and for multiple computers to collectively support a single host.

The complex hosts handle environments where many nodes are acting together as
a cluster, and where virtualized containers (such as Docker) are being used.

![UML](0035-Complex-Hosts.png#pagewidth)

A HostCluster describes a collection of hosts that together are providing a service.
Clusters are often used to provide horizontal scaling of services.

A VirtualContainer provides the services of a host to the software servers
deployed on it (see 0040 below).
When the server makes requests for storage, network access etc,
the VirtualContainer delegates the requests to the equivalent services
of the actual host it is deployed on.

VirtualContainers can be hosted on other VirtualContainers,
but to actually run they need to ultimately be deployed onto a
real physical Host.


Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.