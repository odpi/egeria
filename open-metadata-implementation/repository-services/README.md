<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../images/egeria-content-status-released.png#pagewidth)

# Open Metadata Repository Services (OMRS)

The Open Metadata Repository Services (OMRS) enable metadata repositories to exchange metadata
irrespective of the technology, or technology supplier.

## Overview

Traditional metadata management technology tends to centralize metadata into a single repository.
An organization often begins with a single metadata repository, typically deployed to support a
single project or initiative.
However, over time, depending on the tools they buy, the projects they run or the political structures
within the organization, the number of deployed metadata repositories grows, creating multiple metadata silos.
So for example, an organization may have:

* A metadata repository and tools for its governance team.
This metadata repository may host the canonical glossary, and the governance policies, rules and classifications.

* A metadata repository for its data lake.
This metadata repository has the details of the data repositories in the data lake and the
movement of data between them.

* A metadata repository for its data integration tools that continuously extract data
from the operational systems and sends them to the data lake.

The role of the OMRS is to bring these metadata repositories together so this metadata can be linked
and used together across the organization.
It enables these metadata repositories to act as an aggregated source of metadata.
The metadata repositories using OMRS may be a mixture of repositories from different
vendors that support the OMRS integration interfaces.

## Peer-to-peer operation

The OMRS supports peer-to-peer operation.  This means there is an instance of the OMRS
running with each metadata repository.
This OMRS instance acts as the metadata repository's interface
to the wider open metadata ecosystem.  This includes distributing metadata to other
repositories through the event bus and supporting metadata API requests.

An instance of the OMRS is communicating with other OMRS instances, each located in different
metadata repositories.  The collection of metadata repositories communicating via
their local OMRS instances is called
an **[Open Metadata Repository Cohort](docs/open-metadata-repository-cohort.md)**.

## Where is metadata stored?

One of the principles of open metadata is that metadata should be managed
as close to its source as possible but it should also be easily accessible
through standard open APIs and notifications.

Another principle is that only one repository has write access to a **[specific piece of
metadata](docs/metadata-instances.md)**.  Other copies of this metadata are read-only.

Taking these two principles together, an instance of the OMRS aims to store any new metadata
it receives in its local repository.
Only if the local repository is not able to store it, will the OMRS seek an alternative
location for it.  The local OMRS then supplements this metadata with read-only copies of
metadata from other repositories that are of interest to its local users.

Whichever repository is first used to store a piece of metadata, becomes 
its **[home repository](docs/home-metadata-repositories.md)**.
The home repository is the only repository able to update this metadata.
The read-only copies stored in other repositories are called reference copies.

## How is metadata represented?

Egeria's **[meta-model](docs/metadata-meta-model.md)** defines the 
standard way for metadata to be represented and communicated, fundamentally
consisting of Entities, Classifications and Relationships.

## Understanding more of the design

See the [Documentation](https://egeria-project.org/services/omrs).

## What about the code?

The code for OMRS is organized into five modules:

* **[repository-services-apis](repository-services-apis)** contains the Java client interfaces,
connector interfaces and
event structures for the repository services.

* **[repository-services-archive-utilities](repository-services-archive-utilities)** contains the 
common utilities used to build and
process [Open Metadata Archives](../../open-metadata-resources/open-metadata-archives).

* **[repository-services-client](repository-services-client)** contains the Java interfaces and
for calling remote repository services.

* **[repository-services-implementation](repository-services-implementation)** contains the
support for the peer-to-peer metadata exchange and federation.

* **[repository-services-spring](repository-services-spring)** uses spring to create the OMRS REST services.

----

Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
