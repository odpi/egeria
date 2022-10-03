<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Governance Program Open Metadata Access Service (OMAS)

The Governance Program OMAS provides APIs and events for tools and applications
focused on defining a data strategy, planning support for a regulation and/or
developing a governance program for the data landscape.

It assumes an organization is operating an active governance program that is
iteratively reviewed and developed.

It covers:

* Understanding the business drivers and regulations that provide the motivation and direction
to the governance program.

* Laying down the governance policies (principles, obligations and approaches)
that frame the governance program.

* Planning and defining the governance controls that detail how these governance policies will be implemented in
the organization, and enumerating the implications of these decisions and the expected outcomes.

* Defining the organization's roles and responsibilities that will support the governance program.

* Defining the classifications and governance zones that will organize the assets being governed.

* Defining the subject areas that will organize the data-oriented definitions such as
  glossary terms, valid values and quality rules.

* Reviewing the impact of the governance program.
  * adjusting governance definitions and metrics as necessary.
  
* Reviewing the strategy, business and regulatory landscape.
  * adjusting the governance definitions and metrics as necessary.

* [Documentation](https://egeria-project.org/services/omas/governance-program/overview)

## Design Information

The Governance Program OMAS provides both a Java and a REST API for managing the definitions
for a governance program. It has the following modules:

* [governance-program-api](governance-program-api) defines the Java API and the common Java classes
that are used both by the client and the server.
* [governance-program-client](governance-program-client) supports the client library.
This is used by tools that help organizations to plan and manage their governance program.
* [governance-program-server](governance-program-server) supports in implementation
of the access service and its related event management.
* [governance-program-spring](governance-program-spring) supports the REST API using
the [Spring](../../../developer-resources/Spring.md) libraries.



----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

