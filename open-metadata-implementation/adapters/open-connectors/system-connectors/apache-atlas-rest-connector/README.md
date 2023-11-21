<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Apache Atlas REST Connector

Apache Atlas has a REST API that allows external callers to query and create both
types and instances.  This connector provides a simple Java API to this REST API.
It is written without any dependencies on Apache Atlas (or its associated Hadoop components)
so it happily runs in the same version of Java as the rest of Egeria.

This connector is used by other connectors from Egeria, and may also be used
by components from outside Egeria.

The values from the connection used by this connector are:

* Connection.getUserId() and Connection.getClearPassword() for logging in to Apache Atlas.
* Connection.getDisplayName() for the connector name in messages.
* Connection.getEndpoint().getAddress() for the URL root (typically host and port name) of the Apache Atlas server.
* Connection.getConfigurationProperties.get("atlasServerName") for the name of the Apache Atlas server to use in messages.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.