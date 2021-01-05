<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata Collection Store Connectors

The open metadata collection store connectors connect the
**[Open Metadata Repository Services (OMRS)](../../../../repository-services)** to
an existing metadata repository.
This enables the metadata repository to join
an **[Open Metadata Repository Cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md)**.

There are two types of connector:
* an open metadata repository connector, and
* an optional open metadata repository event mapper.

There is **[more documentation](docs/README.md)** on these connectors to
help developers understand more about building their own.

Below are some pre-built repository connectors from Egeria that natively manage
the open metadata types and instances and so can provide the local repository
implementation in a [Metadata Server](../../../../admin-services/docs/concepts/metadata-server.md).

* **[graph-repository-connector](graph-repository-connector)** -
provides a local repository that uses a graph store as its persistence store.

* **[inmemory-repository-connector](inmemory-repository-connector)** -
provides a local repository that is entirely in memory.  It is useful for
testing/developing OMASs and demos.  This module also contains the  **read-only-repository-connector** that
provides a read only in-memory repository that can be used to host fixed content from an
[Open Metadata Archive](../../../../../open-metadata-resources/open-metadata-archives).
The fact that it is read only means that no new content can be mastered in it.

Finally there is  the **[omrs-rest-repository-connector](omrs-rest-repository-connector)**
which  uses the OMRS REST API to call an open metadata-compliant repository.
This connector is used to support the
[federated queries](../../../../repository-services/docs/component-descriptions/enterprise-connector-manager.md) across a cohort.
It is embedded in the [enterprise repository connector](../../../../repository-services/docs/component-descriptions/enterprise-repository-connector.md)
that provides metadata to
the [access services](../../../../access-services).




----
Return to [repository-services-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.