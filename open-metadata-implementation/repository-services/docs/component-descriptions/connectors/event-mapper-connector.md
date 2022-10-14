<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Local Repository's Event Mapper Connector

The Event Mapper connector provides a common API for
specific implementations of OMRS Event Mappers to implement.

Event mappers are needed in [repository proxy](https://egeria-project.org/concepts/repository-proxy)
servers if the third party technology that it is
integrating into the open metadata repository cohort
also has its own mechanisms for maintaining metadata.

The event mapper's role is to notify the cohort of any changes to
the metadata mastered in the third party repository
that has occurred through the third party technology's own mechanisms.

Since each event mapper is tied to a third party
technology, core Egeria does not supply any implementations of
this connector.  There are, however, the following
implementations available:

* **[Event Mapper for Apache Atlas](https://github.com/odpi/egeria-connector-hadoop-ecosystem)**
* **[Event Mapper for IBM Information Governance Catalog](https://github.com/odpi/egeria-connector-ibm-information-server)**


----
Return to [repository services connectors](.).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
