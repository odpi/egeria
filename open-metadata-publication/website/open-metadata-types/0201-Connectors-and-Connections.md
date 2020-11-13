<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0201 Connectors and Connections

In [Area 0](Area-0-models.md) we introduced the definitions for a
server with an endpoint ([model 0040](0040-Software-Servers.md)).
The server could host data and APIs.
The [Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework/README.md)
provides client java classes called connectors to enable an application,
tool or engine to access this data and other deployed functions.

A **Connection** metadata entity contains the configuration
information to allow the OCF's Connector Broker to select and
configure the appropriate a client application or tool to connect
to a particular [Endpoint](0040-Software-Servers.md).
The **ConnectorType** defines which connector implementation
should be used to connect to the endpoint.
The **securedProperties** holds authentication properties
such as userId and password.
They are securely stored to protect the assets.
If they are missing then the security credentials of the current
user are used with the connection.

![UML](0201-Connectors-and-Connections.png#pagewidth)


Return to [Area 2](Area-2-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.