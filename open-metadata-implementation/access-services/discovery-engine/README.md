<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![TechPreview](../../../images/egeria-content-status-tech-preview.png#pagewidth)

# Discovery Engine Open Metadata Access Service (OMAS)

The Discovery Engine OMAS provides APIs and events for metadata discovery tools
that are surveying the data landscape and recording information in
metadata repositories.

* [Documentation](https://egeria-project.org/services/omas/discovery-engine/overview)

## Design information

The module structure for the Discovery Engine OMAS is as follows:

* [discovery-engine-client](discovery-engine-client) supports the client library that is used by
the discovery server (and the discovery engines and discovery services it hosts) to
access the Discovery Engine OMAS's REST API and out topic.

* [discovery-engine-api](discovery-engine-api) supports the common Java classes that are used both by the client and the server.
Since the Open Discovery Framework (ODF) defines most of the
interfaces for the Discovery Engine OMAS, this module only needs to provide the
interfaces associated with the out topic.

* [discovery-engine-server](discovery-engine-server) supports in implementation of the metadata interfaces
defined by the Open Discovery Framework (ODF) and its related event management.

* [discovery-engine-spring](discovery-engine-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
This module has no business logic associated with it.
Each REST API endpoint delegates immediately to an
equivalent function in the server module.
It is, however, a useful place to look to get a view of the
REST API supported by this OMAS.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

