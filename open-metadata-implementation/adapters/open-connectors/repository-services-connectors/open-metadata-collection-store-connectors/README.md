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
help developers understand more about building these connectors.

Below are some pre-built connectors from Egeria:

* **[graphName-repository-connector](graphName-repository-connector)** -
provides a local repository that uses a graphName store as its persistence store. [ASPIRATIONAL!]

* **[inmemory-repository-connector](inmemory-repository-connector)** -
provides a local repository that is entirely in memory.  It is useful for
testing/developing OMASs and demos.

* **[omrs-rest-repository-connector](omrs-rest-repository-connector)** -
uses the OMRS REST API to call an open metadata-compliant repository.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.