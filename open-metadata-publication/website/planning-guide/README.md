<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Planning Guide - How to deploy Egeria into your organization

The planning guide provides information to help you plan the deployment of Egeria
in your organization.

Egeria is highly flexible and configurable and so the first
piece of advice is to start small and simple and then expand as your experience
and understanding of your workloads grows.

## Platforms and Servers

Figure 1 shows the diagram from Egeria's home page showing Egeria exchanging metadata
between many types of tools distributed across different data centers and cloud
platforms.

![Figure 1](../images/egeria-distributed-operation.png#pagewidth)
> **Figure 1:** Egeria integrating different tools distributed over many locations

The green clouds represent each of the deployment locations.
The different technologies
are shown as grey boxes and Egeria itself is shown in blue and orange.

The blue rounded boxes are Egeria's Open Metadata and Governance (OMAG) Server Platform.
This platform is the heart of Egeria's implementation.  Typically you would expect to
have at least one OMAG Server Platform deployed in each location.  However,
when you are experimenting with Egeria, it is often sufficient to start with one
OMAG Server Platform and expand the number of platforms
as you expand the technologies being integrated.

The OMAG Server Platform is capable of hosting
one or more Open Metadata and Governance (OMAG) Servers.  The OMAG Servers are the orange
circles in Figure 1.  They manage the connectors to third party technology as well as the
frameworks and intelligence that Egeria brings to distributed metadata management.

It is a simple command to move an OMAG Server from one platform instance to another.
This means you can start experimenting with a single platform and then add more as
your deployment grows.  The platform can also run as a node in container technologies
such as Docker and Kubernetes.

![Figure 2](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-deployment-choices.png)
> **Figure 2:** OMAG Server deployment choices

Different types of technology need different types of integration and so there are different
types of OMAG Servers.
Each type of OMAG Server is focused on the integration of a specific type of
of tool.
Figure 3 shows the OMAG Servers implemented today and how they are related.
This picture will expand as we embrace new types of technology.

![Figure 3](../../../open-metadata-implementation/admin-services/docs/concepts/types-of-omag-servers.png#pagewidth)
> **Figure 3:** Types of OMAG Servers

The **cohort members** communicate with one another
via an [open metadata repository cohort](../../../open-metadata-implementation/repository-services/docs/open-metadata-repository-cohort.md).
This means that they exchange metadata through a low level, fine-grained
API supported by the Open Metadata Repository Services (OMRS).
The governance servers connect to a metadata access point / metadata server.

Figure 4 shows how the servers connect together.

![Figure 4](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-ecosystem.png#pagewidth)
> **Figure 4:** How the OMAG Servers interact

When you are designing the deployment of Egeria, the first step is to identify the technology
that you want to integrate.  This will determine which type of OMAG Servers you need.
Once the OMAG Servers are selected, they are assembled and connected together as shown in Figure 3.

The text above is a very high level overview of the planning process.
More detail will be added to this guide as time permits.


## Deployment checklist

This is a checklist of planning tasks for the deployment of your OMAG Server Platforms
and OMAG Servers.

* Use an encrypted configuration document store for your platforms since
  configuration documents can have certificates and passwords in them.

* Implement the metadata security connectors for your organization to
  ensure only authorized users access metadata.
  
* Choose and configure the audit log destinations for your OMAG servers.

* Ensure you have at least one Egeria metadata server in each of your
  open metadata repository cohorts.
  
* Assign a separate user id for each of your servers and ensure they are
  defined in your user directory and are authorized users according to the
  metadata security connectors.
  
* Consider where you need to have multiple versions of the same server running to give continuous
  availability.
  
* Plan your use of the event bus - which technology to use (Apache Kafka is the default)
  and the names of the topics that your OMAG Servers will use.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.