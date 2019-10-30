<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata Conformance Suite Design

The open metadata conformance suite provides a testing framework to help the developers
integrate a specific technology into the open metadata ecosystem.

The initial focus of the conformance suite is on the
behavior of an open metadata repository. 
Future test suites will cover other APIs and event types as well
demonstrate the ability to handle various workloads and also
a performance benchmark test.

Information on how to run the open metadata conformance suite is located in the [docs section](../docs).

The implementation of the open metadata conformance suite is typical of an ODPi Egeria capability.
It has four modules:

* **[open-metadata-conformance-suite-api](../open-metadata-conformance-suite-api)** - common components
used in both the client and the server.  This includes the beans passed in the REST API and
exceptions.

* **[open-metadata-conformance-suite-client](../open-metadata-conformance-suite-client)** - implementation
of a client to retrieve the test case results from the open metadata conformance suite server.

* **[open-metadata-conformance-suite-server](../open-metadata-conformance-suite-server)** - implementation
of the test framework, workbenches and test cases used to validate the behavior of the technology
under test.  The tests run automatically in the server based on the server's configuration.
The server also provides a REST API to retrieve the results.  This REST API is called
by the client.

* **[open-metadata-conformance-suite-spring](../open-metadata-conformance-suite-spring)** - uses
[Spring](../../developer-resources/Spring.md) to provide the REST endpoints for the server's REST API.
This is separated to allow another REST framework to be used for the server.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

