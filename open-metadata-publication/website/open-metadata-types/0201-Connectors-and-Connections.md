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
to a particular [Endpoint](0026-Endpoints.md).
The **ConnectorType** defines which connector implementation
should be used to connect to the endpoint.
The **securedProperties** holds authentication properties
such as userId and password.
They are securely stored to protect the assets.
If they are missing then the security credentials of the current
user are used with the connection.

By default, connector implementations are assume to support the OCF.
However, many vendor platforms have their own connector frameworks.
The **ConnectorCategory** allows equivalent connector types from different connector
frameworks to be gathered together so that the connector type from a connection can
be swapped for an equivalent connector type for the locally supported connector framework.

![UML](0201-Connectors-and-Connections.png#pagewidth)

The picture below shows how the connector category can be used to navigate to
a different connector type implementation.

![ConnectorCategory](0201-Connectors-and-Connections-Illustration-1.png#pagewidth)

The next picture shows the ConnectorTypeDirectory can be used to organize the
ConnectorCategories.

![ConnectorTypeDirectory](0201-Connectors-and-Connections-Illustration-2.png#pagewidth)

----

* Return to [Area 2](Area-2-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.