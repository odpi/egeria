<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer Open Metadata Access Service (OMAS)

The Asset Consumer OMAS provides services to an individual that wants to work
with assets such as:

* data stores, data sets and data feeds
* reports
* APIs
* functions such as analytical services

It supports:

* the retrieval of open metadata connection objects.
* the creation of a connector based on the properties in a connection object.
* the retrieval of properties about an asset.
* the adding of feedback (comments, ratings, tags and likes) to specific assets.
* the adding of an audit log record for the asset.
* the management of personal collections of assets that are of interest to the individual.
* the management of notifications relating to changes to the assets in these collections.
* the publishing of notifications about assets as configured above.

Using the Asset Consumer OMAS results in Karma Points being awarded
to the individual.  These are maintained in the individual's profile.
A karma point is awarded for each contribution of metadata
through the API.

The connectors returned by the Asset Consumer OMAS are Open Connector
Framework (OCF) connectors.  The caller can use the connector to access
the contents of the asset itself and the properties about the
asset it is accessing.   This service is provided by the
[Connected Asset OMAS](../connected-asset/README.md).

## Internals

The module structure for the Asset Consumer OMAS is as follows:

* [asset-consumer-client](asset-consumer-client) supports the client library.
* [asset-consumer-api](asset-consumer-api) supports the common Java classes that are used both by the client and the server.
* [asset-consumer-server](asset-consumer-server) supports in implementation of the access service and its related event management.
* [asset-consumer-spring](asset-consumer-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

