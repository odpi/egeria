<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Repository Services (OMRS)

The Open Metadata Repository Services (OMRS) enable metadata repositories to exchange metadata.
Traditional metadata management technology tends to centralize metadata into a single repository.
An organization often begins with a single metadata repository, typically deployed to support a
single project or initiative.
However, over time, depending on the tools they buy, the projects they run or the political structures
within the organization, the number of deployed metadata repositories grows, creating multiple metadata silos.
So for example, an organization may have:

* a metadata repository and tools for its governance team.
This metadata repository may host the canonical glossary, and the governance policies, rules and classifications.

* a metadata repository for its data lake.
This metadata repository has the details of the data repositories in the data lake and the
movement of data between them.

* a metadata repository for its data integration tools that continuously extract data
from the operational systems and sends them to the data lake.

The role of the OMRS is to bring these metadata repositories together so this metadata can be linked
and used together across the organization.
It enables these metadata repositories to act as a aggregated source of metadata.
The metadata repositories using OMRS may be a mixture of repositories from different
vendors that support the OMRS integration interfaces.

The code for OMRS is organized into three modules:

* **[repository-services-apis](repository-services-apis)** contains the Java interfaces and event structures for
the repository services.

* **[repository-services-implementation](repository-services-implementation)** contains the support for the peer-to-peer
metadata exchange and federation.

* **[repository-services-spring](repository-services-spring)** uses spring to create the OMRS REST services.