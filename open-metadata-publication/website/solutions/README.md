<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Solutions

The Egeria solutions illustrate useful integration
solutions that you can create using Egeria.
Each solution describes the scenario and business value,
along with instructions on how to set up Egeria.

The solutions combine different categories of 
[OMAG Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md) that host
the connectors to the third party technologies.  The OMAG Servers
manage the metadata exchange through REST API calls and events.

Figure 1 shows how the different categories of OMAG Servers
are linked together to build the solutions.

![Egeria Solution Composition](egeria-solution-components.png#pagewidth)
> **Figure 1:** OMAG Servers connected together as components in an Egeria solution

The inner ring, titled **Integrated Metadata**, illustrates the exchange of metadata between
third party metadata servers.
Third party metadata servers provide support for a wide
range of metadata and are typically supporting
a suite of tools.  They are connected together through an
[Open Metadata Repository Cohort](../../../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md)
or **cohort** for short.

A cohort can support the exchange of many third party metadata
servers even though only one is shown in Figure 1.

A third party metadata server can embed the Egeria libraries in its
own runtime or, more commonly,
use a special OMAG server called the **Repository Proxy**
to host connectors that map between the events and APIs of the
third party metadata server and the Open Metadata events and APIs.
The repository proxy manages all of the interaction with the other
members of the cohort.

The cohort protocols are peer-to-peer and the membership is dynamic.
When a third party metadata server connects to the cohort, either directly
or through its repository proxy, it automatically begins receiving
metadata from all of the other members.  When it shares metadata,
it is shared with all the other members.   Each member is free to choose what
to share and what to process from the other members of the cohort.

There are other types of OMAG Servers provided by
Egeria that can be members of the cohort.
* The **Conformance Test Server** is used to verify that a member of the
cohort is operating correctly.  It is typically only used in
test environments because it sends out a lot of test metadata on the cohort
and validates the responses from the cohort member it is testing.

* The **Metadata Access Point** supports Egeria's [Open Metadata Access
 Services (OMASs)](../../../open-metadata-implementation/access-services), or access services, for short.  These access services
 provide specialized APIs and events for different types of technologies.
 
* The **Metadata Server** provides a metadata repository
that supports any type of open metadata.  It is a valuable
member of the cohort because it is a metadata gap-filler.  By that we mean
that is can store relationships between metadata
from different third party repositories along with additional types of metadata
not supported by any of the third party metadata repositories.
It may optionally have the access services enabled so it can also
act as a metadata access point.

The metadata access point is the bridge to the governance servers.
The addition of the governance servers provides active metadata
exchange and governance of any type of third party technology,
not just metadata servers.  We call this **Integrated Governance**.

For the most part, Egeria is a background technology.
However, once metadata is being exchanged and linked, new
solutions that bring value directly to individuals
working in an organization.  Therefore we have added the
**View Server** and **Presentation Server** to
support browser based User Interfaces.  The view server
provides REST APIs specifically for user interfaces.
They are consumed by the Egeria UIs but can also be used by
other UIs and tools.


## Specific Solutions

Follow the links below to learn more about specific Egeria solutions.

* [Metadata Server Exchange](metadata-server-exchange) describes how to set up
Egeria to enable third party metadata servers to exchange
metadata.

* [Metadata Repository Conformance Validation](../../../open-metadata-conformance-suite)
describes how to set up a cohort to validate the implementation of
of the connectors for a third party repository.

* [Data Manager Integration](data-manager-integration) covers how to automatically
catalog and distribute metadata about data assets stored in database servers, file/content/document managers and
file systems.

----
* [Return to home page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.