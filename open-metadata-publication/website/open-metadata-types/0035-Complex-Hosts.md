<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0035 Complex Hosts

In the virtualized hardware world there are three broad categories of
host.

 * A **BareMetalComputer** describes a connected set of physical hardware.
The open metadata types today do not attempt to model hardware in detail
but this could be easily added if a contributor with the
appropriate expertise was willing to work on it.

 * A **VirtualMachine** provides virtualized hardware through
a hypervisor that allows a single physical bare metal computer
to run multiple virtual machines.

 * A **VirtualContainer** provides the services of a virtualized operating system to the software
processes running in it.
When the server makes operating system requests,
the VirtualContainer delegates the requests to the equivalent services
of the actual host it is deployed on.
**DockerContainer** provides a specific type of a popular container type called
[docker](https://www.docker.com/).

The hosts can actually be virtualized through many levels.
The **HostedHost** relationship is used to represent the
layers of virtualized hosts. 

![UML](0035-Complex-Hosts.png#pagewidth)


A **HostCluster** describes a collection of hosts that together are providing a service.
Clusters are often used to provide horizontal scaling of services.
The host cluster is linked to the hosts it is managing using the **HostClusterMember**.
There are two specific types of host clusters defined:

* **HadoopCluster** - describes a [Hadoop cluster](https://hadoop.apache.org/) - that uses multiple bare metal computers/virtual machines to
manage big data workloads.

* **KubernetesCluster** - describes a [Kubernetes cluster](https://kubernetes.io/) - that manages containerized applications
across multiple bare metal computers/virtual machines.

In both Hadoop and Kubernetes the hosts that they manage are often
referred to as "nodes".  The containerized applications managed by Kubernetes
are represented as **VirtualContainer**s.

Within the host cluster is typically a special host (node) that is controlling the
execution of the other members.  This host is modelled with a
[SoftwareServerPlatform](0037-Software-Server-Platforms.md) that describes the cluster management platform,
a [SoftwareServer](0040-Software-Servers.md) that groups
the [SoftwareServerCapabilities](0042-Software-Server-Capabilities.md)
needed to manage the cluster.  These software server capabilities
are linked to the [ITInfrastructure](0030-Hosts-and-Platforms.md)
assets that are being managed by the cluster using the [ServerAssetUse](0045-Servers-and-Assets.md) relationship.

## Related Models

[0030 Hosts and Platforms](0030-Hosts-and-Platforms.md) describes how the software installed on a host is represented.
[0037 Software Server Platform](0037-Software-Server-Platforms.md) describes the software process that run on a host.
[0045 Servers and Assets](0045-Servers-and-Assets.md) describes the relationships that manage assets such as ITInfrastructure.


## Deprecated Types

* **DeployedVirtualContainer** - use **HostedHost** which is more general.

----

* Return to [Area 0](Area-0-models.md).
* Return to [Overview](.).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.