<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Governance Program Open Metadata Access Service (OMAS)

The Governance Program OMAS provides APIs and events for tools and applications
focused on defining a data strategy, planning support for a regulation and/or
developing a governance program for the data landscape.

It assumes an organization is operating an active governance program that is
iteratively reviewed and developed.

It covers:

* Understanding the business drivers that provide requirements and direction
to the governance program.
* Laying down the governance policies (principles, obligations and approaches)
that frame the governance program.
* Planning and defining the governance controls that detail how these governance
policies will be implemented in
the organization, and enumerating the implications of these decisions and
the expected outcomes.
* Defining the organization's roles and responsibilities that will support
the program.
* Commissioning campaigns and projects to implement the governance controls
and the collection of measurements to assess the success of the program.
* Reviewing the impact of the governance program and making adjustments.
  * --> additional campaigns or projects
  * --> adjusting the policies and controls
* Reviewing the strategy, business and regulatory landscape.
  * --> adjusting the business drivers

The module structure for the Governance Program OMAS is as follows:

* [governance-program-client](governance-program-client) supports the client library.
This is used by tools that help organizations to plan and manage their governance program.
* [governance-program-api](governance-program-api) supports the common Java classes
that are used both by the client and the server.
* [governance-program-server](governance-program-server) supports in implementation
of the access service and its related event management.
* [governance-program-spring](governance-program-spring) supports the REST API using
the [Spring](../../../developer-resources/Spring.md) libraries.



----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

