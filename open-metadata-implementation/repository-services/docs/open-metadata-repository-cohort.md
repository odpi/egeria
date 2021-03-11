<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Repository Cohort

An **open metadata repository cohort** is a collection of [servers](../../admin-services/docs/concepts/cohort-member.md)
sharing metadata using the **[Open Metadata Repository Services (OMRS)](..)**.
This sharing is peer-to-peer.
Once a server becomes a member of the cohort, it can share
metadata with, and receive metadata from, any other member.

OMRS needs to be flexible to support different performance and availability requirements.
For example, where metadata is changing
rapidly (such as in a data lake), this metadata should be dynamically queried
from the repository where it was created and is being maintained  because
the rate of updates mean it would cost a lot of network traffic to keep a
copy of this metadata up to date.  The repository where a piece of metadata
(metadata instance) was created and where it is maintained is called
its **[home metadata repository](home-metadata-repositories.md)**.

On the other hand, governance definitions
(such as policies) and glossary terms rarely change.
They are often administered centrally by the governance team and then
linked to all metadata that describes the organization's data resources.
Thus it makes sense for this metadata to be replicated across the repositories
within the cohort.
These copies are called **[reference copies](home-metadata-repositories.md)** of
the metadata and they are read-only.

The role of the OMRS is to optimize access to metadata across the cohort by using
a combination of replication and federated queries, driven by the
the metadata workload from the connected tools.

## Cohort membership

To join an open metadata repository cohort, a metadata repository must integrate
with the OMRS module. Egeria provides a number or pre-built
[cohort members](../../admin-services/docs/concepts/cohort-member.md).

One of them, the [repository proxy](../../admin-services/docs/concepts/repository-proxy.md)
provides a simple way to integrate a third party server into a cohort 
by creating an [OMRS Repository Connector and optional Event Mapper Connector](../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)
to map between the third party APIs/events and the repository service's equivalents

A more bespoke integration involves:

* Creating an [OMRS Repository Connector and optional Event Mapper Connector](../../adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)
* Designing how to configure the OMRS Services for your metadata repository.
Typically this is done by extending the existing administration services of the
metadata repository, but Egeria also offers
some pre-built **[administration services](../../admin-services)** that
can be used or modified.
* Plugging the OMRS and any administration services into the metadata repository's security
module so that requests to the server can be secured against unauthorized access.
* Integrating the OMRS, administration and security capability into your product.

There are different integration patterns available to help you choose the best approach for your product.
Each method is optimized for specific use cases and so the metadata repository can only play a full role in the open 
metadata use cases if it supports all integration methods.  These are:

* Support for an OMRS repository connector to allow open metadata API calls to the 
repository to create, query, update and delete metadata stored in the repository.  
The OMRS connectors support the Open Connector Framework (OCF) to provide a call interface to 
the metadata repositories.  The OMRS Repository Connector API is a standard interface for all 
metadata repositories.  This enables services such as the Enterprise OMRS Repository Connector 
to interact with 1 or many metadata repositories through the same interface.  
The connection configuration it passes to the OCF determines which type of OMRS connector is 
returned by the OCF.

* Support for the OMRS event notifications that are used to synchronize selective metadata 
between the metadata repositories. 

## Cohort registration

The OMRS protocols are peer-to-peer.
Each repository in the cohort has an [OMRS Cohort Registry](component-descriptions/cohort-registry.md) that 
supports the registration of the metadata
repositories across the cohort.   Through the registration process, each OMRS Cohort Registry 
assembles a list of all of the members of the cohort.  This is saved in the 
[OMRS Cohort Registry Store](component-descriptions/connectors/cohort-registry-store-connector.md).  The list of connections to the remote members of the cohort are passed to the OMRS Enterprise Connector Manager that in turn manages the configuration of the Enterprise OMRS Repository Connectors.

The Enterprise OMRS Connector provides federated query support across the metadata cohort
for the Open Metadata Access Services (OMAS).

When a metadata repository registers with the OMRS Cohort Registry,
the administrator may either supply a unique server identifier, or ask the OMRS to generate one.
This server identifier (called the metadata collection identifier) is used in the OMRS event notifications,
and on OMRS repository connector calls to identify the location of the home copy of the metadata entities and
to identify which repository is requesting a service or supports a particular function.

Once the metadata repository has registered with the OMRS Cohort Registry,
it is a member of the metadata repository cohort and it can synchronize and share metadata
with other repositories in the cohort through the OMRS Topic.

Note: A single metadata repository can register with multiple metadata cohorts as long as its server
identifier is unique across all cohorts that it joins and it manages the posting of events to the
appropriate OMRS Topic for each cohort it register with.
This capability is useful for a metadata repository that is aggregating reference
copies of metadata from multiple open metadata repository cohorts.


----

* Return to the [repository services overview](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
