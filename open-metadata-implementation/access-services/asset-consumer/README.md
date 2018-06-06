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
asset it is accessing.   This service is provided by the ConnectedAsset OMAS.

