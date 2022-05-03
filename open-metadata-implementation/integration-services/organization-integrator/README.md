<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Organization Integrator Open Metadata Integration Service (OMIS)

The Organization Integrator OMIS supports the loading of teams and
other organization structural information into the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omis/organization-integrator/overview)


## Module Implementation

The modules are as follows:

* [organization-integrator-api](organization-integrator-api) - defines the interface for an integration
connector that is supported by the Organization Integrator OMIS. This includes the implementation
of the context that wraps the Community Profile OMAS's clients.

* [organization-integrator-server](organization-integrator-server) - implements the context manager for
the Organization Integrator OMIS.

* [organization-integrator-spring](organization-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [organization-integrator-client](organization-integrator-client) - implements a Java client for the REST API.

This integration service is paired with the [Community Profile](../../access-services/community-profile)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.