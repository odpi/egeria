<!-- SPDX-License-Identifier: Apache-2.0 -->

# Asset Consumer Open Metadata Access Service (OMAS)

The Asset Consumer OMAS provides services for an application
accessing assets such as data stores, APIs or functions such as analytical services.

The Asset Consumer REST API supports the retrieval of connection metadata, the
adding of feedback to specific assets and an audit log for the asset.

The Asset Consumer Java client supports all of the operations of the REST API.
It adds the capability to act as a factory for connectors to assets.
The Java client takes the name or id of a connection, looks up the properties
of the connection and, using the Open Connector Framework (OCF), it creates a new
connector instance and returns it to the caller.

In addition it can add and remove feedback (tags, ratings, comments, likes) from
the asset description.

The caller can use the connector to access metadata about the
asset it is accessing.   This service is provided by the
[Connected Asset OMAS](../connected-asset/README.md).

The module structure for the Asset Consumer OMAS is as follows:

* [asset-consumer-client](asset-consumer-client) supports the client library.
* [asset-consumer-api](asset-consumer-api) supports the common Java classes that are used both by the client and the server.
* [asset-consumer-server](asset-consumer-server) supports in implementation of the access service and its related event management.
* [asset-consumer-spring](asset-consumer-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.

