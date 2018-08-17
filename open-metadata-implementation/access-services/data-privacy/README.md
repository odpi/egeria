<!-- SPDX-License-Identifier: Apache-2.0 -->

# Data Privacy Open Metadata Access Service (OMAS)

The Data Privacy OMAS provides APIs and events for tools that support a data
privacy program.  This includes:

* Reviewing the regulations and governance requirements defined in the governance
program.
* Maintaining the definition of personal data.
* Retrieving information about the location and protection of
personal data.
* Retrieving information about the digital services in order to assess their
compliance to the data privacy program.
* Recording data processing impact assessments.
* Managing incidents relating to data privacy.

The module structure for the Data Privacy OMAS is as follows:

* [data-privacy-client](data-privacy-client) supports the client libraries for different languages.
* [data-privacy-api](data-privacy-api) supports the common Java classes that are used both by the client and the server.
* [data-privacy-server](data-privacy-server) supports in implementation of the access service and its related event management.
* [data-privacy-spring](data-privacy-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.
