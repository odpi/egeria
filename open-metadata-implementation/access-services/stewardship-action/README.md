<!-- SPDX-License-Identifier: Apache-2.0 -->

# Stewardship Action Open Metadata Access Service (OMAS)

The Stewardship Action OMAS provides APIs and events for tools and applications
focused on resolving issues detected in the data landscape.

The functions tend to be in groups that detect, report on and support the
resolution of issues.

For example, the Stewardship Action OMAS detects and emits an event if a new asset
is created without an owner.  There is also a service operation to add the
name of the owner to the offending asset.

The module structure for the Stewardship Action OMAS is as follows:

* [stewardship-action-client](stewardship-action-client) supports the client library.
* [stewardship-action-api](stewardship-action-api) supports the common Java classes that are used both by the client and the server.
* [stewardship-action-server](stewardship-action-server) supports in implementation of the access service and its related event management.
* [stewardship-action-spring](stewardship-action-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
