<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Security Integrator Open Metadata Integration Service (OMIS)

The Security Integrator integration service supports distribution of security
parameters to access control enforcement engines such as Apache Ranger.
Its API simplifies the internal models and structures of
the open metadata type model and related structure for the consumers.

The modules are as follows:

* [security-integrator-api](security-integrator-api) - defines the interface for an integration
connector that is supported by the Security Integrator integration service. This includes the implementation
of the context that wraps the Asset Manager OMAS's clients.

* [security-integrator-server](security-integrator-server) - implements the context manager for
the Security Integrator integration service.

This integration service is paired with the [Security Officer](../../access-services/security-officer)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.