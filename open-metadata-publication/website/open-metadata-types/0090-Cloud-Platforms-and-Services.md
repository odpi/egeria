<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0090 Cloud Platforms and Services

The cloud platforms and services model show that cloud computing
is not so different from what we have been doing before.
Cloud infrastructure and services are classified as such to show
that the organization is not completely in control of the technology
supporting their data and processes.

![UML](0090-Cloud-Platforms-and-Services.png#pagewidth)

The cloud provider is the organization that provides and runs the
infrastructure for a cloud service.
Typically the host it offers is actually a
**[HostCluster](0035-Complex-Hosts.md)**. 

The cloud provider may offer infrastructure as a service (IaaS),
in which case, an organization can deploy
**[VirtualContainers](0035-Complex-Hosts.md)**
onto the cloud provider's HostCluster.

If the cloud provider is offering platform as a service (PaaS),
an application may deploy server capability onto the cloud platform.

If the cloud provider is offering Software as a Service (SaaS) then
it has provided APIs backed by pre-deployed server capability
that an organization can call as a cloud service.

Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.