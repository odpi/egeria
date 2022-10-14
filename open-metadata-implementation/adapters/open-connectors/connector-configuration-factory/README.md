<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../../images/egeria-content-status-released.png#pagewidth)

# Connector Configuration Factory

The connector configuration factory 
creates **[Connection](../../../frameworks/open-connector-framework/docs/concepts/connection.md)**
objects for the repository services connectors that are directly supported by Egeria.

This includes:

* A connection for the [open metadata server configuration file](../configuration-store-connectors/README.md) called omag.server._serverName_.config".
* A connection for the [default audit log](../repository-services-connectors/audit-log-connectors/README.md) called _serverName_.auditlog.
* A connection for the [open metadata types archive file](../repository-services-connectors/open-metadata-archive-connectors/README.md) called OpenMetadataTypes.json
* A connection for the [default registry store](../repository-services-connectors/cohort-registry-store-connectors/README.md) called _serverName_.cohortName.registrystore.
* A connection for the [graph open metadata repository connector](../repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector/README.md)
* A connection for the [graph repository event mapper connector](../repository-services-connectors/open-metadata-collection-store-connectors/graph-repository-connector/README.md).
* A connection for the [in memory open metadata repository connector](../repository-services-connectors/open-metadata-collection-store-connectors/inmemory-repository-connector/README.md)
* A connection for the [remote open metadata repository connector](../repository-services-connectors/open-metadata-collection-store-connectors/omrs-rest-repository-connector/README.md)
* A connection for an [event bus connector](../event-bus-connectors/README.md)
* A connection for the [OMRS topic connector](../../../repository-services/docs/omrs-event-topic.md) used to pass events from OMRS to the [Open Metadata Access Services (OMASs)](../../../access-services/README.md).
* A connection for the [OMRS topic connector](../../../repository-services/docs/omrs-event-topic.md) used to exchange events with other members of an [open metadata repository cohort](../../../repository-services/docs/open-metadata-repository-cohort.md).


Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
 