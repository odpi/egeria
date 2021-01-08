<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Organization Integrator Open Metadata Integration Service (OMIS)

The Organization Integrator integration service supports the loading of teams and
other organization structural information into the open metadata ecosystem.

The modules are as follows:

* [organization-integrator-api](organization-integrator-api) - defines the interface for an integration
connector that is supported by the Organization Integrator integration service. This includes the implementation
of the context that wraps the Community Profile OMAS's clients.

* [organization-integrator-server](organization-integrator-server) - implements the context manager for
the Organization Integrator integration service.

This integration service is paired with the [Community Profile](../../access-services/community-profile)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.