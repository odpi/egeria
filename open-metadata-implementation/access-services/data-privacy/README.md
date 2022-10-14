<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Data Privacy Open Metadata Access Service (OMAS)

The Data Privacy OMAS provides APIs and events for tools that supports the operational side of a data
privacy program.  This includes:

* Reviewing the regulations and governance requirements defined in the [governance
program](../governance-program) that related to privacy.
* Maintaining the definitions for personal data.
* Retrieving information about the location and protection of
personal data.
* Retrieving information about the digital services in order to assess their
compliance to the data privacy program.
* Recording data processing impact assessments.
* Managing incidents relating to data privacy.

* [Documentation](https://egeria-project.org/services/omas/data-privacy/overview)

## Design Information

The module structure for the Data Privacy OMAS is as follows:

* [data-privacy-client](data-privacy-client) supports the client libraries for different languages.
* [data-privacy-api](data-privacy-api) supports the common Java classes that are used both by the client and the server.
* [data-privacy-server](data-privacy-server) supports in implementation of the access service and its related event management.
* [data-privacy-spring](data-privacy-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

