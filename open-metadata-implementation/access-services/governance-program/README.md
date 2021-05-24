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

## Related OMASs

* The [Community Profile OMAS](../community-profile) supports the definition of the profiles for people and teams that
will support the governance program.  These are linked to the governance roles defined by the
governance program.

* The [Project Management OMAS](../project-management) supports the rollout of the governance program
by commissioning campaigns and projects to implement the governance controls
and the collection of measurements to assess the success of the program.

* The [Digital Architecture OMAS](../digital-architecture) provides the set up of the digital landscape that
supports the governance program.  This includes the definitions of the information supply chains and solution
components that support the organization's activities.

* The [Digital Service OMAS](../digital-service) documents the business capabilities along with
their digital services that are supported by the governance program.

* The [Governance Engine OMAS](../governance-engine) supports the implementation of technical controls and
the choreography of their execution.

* The [Stewardship Action OMAS](../stewardship-action) supports the stewards as they manage the exceptions
detected to the governance program.
  
* The [Data Privacy OMAS](../data-privacy) supports the operational aspects of managing privacy as part of
the organization's activities.

* The [Subject Area OMAS](../subject-area) supports the definitions of the vocabularies associated with a
subject area.

* The [Data Manager OMAS](../data-manager) support the automated cataloging of assets and configuration
of technology that is managing them.

* The [Security Manager OMAS](../security-manager) support the configuration
of technology that is managing the security of assets.

* The [Security Officer OMAS](../security-officer) support the definitions of users and groups
and related definitions that make up the user directory.

* The [Asset Manager OMAS](../asset-manager) supports the automated exchange of governance definitions
between catalogs and asset managers to create a consistent rollout of governance requirements.
  
* The [Asset Owner OMAS](../asset-owner) supports the linking of governance definitions and classifications
to assets to define how they should be governed.

* The [Asset Consumer OMAS](../asset-consumer) supports the visibility of the governance definitions and
classification by consumers of the assets.

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

