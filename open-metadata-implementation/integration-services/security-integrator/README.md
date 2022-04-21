<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Security Integrator Open Metadata Integration Service (OMIS)

The Security Integrator OMIS supports distribution of security
parameters to access control enforcement engines such as Apache Ranger.
Its API simplifies the internal models and structures of
the open metadata type model and related structure for the consumers.

* [Documentation](https://egeria-project.org/services/omis/security-integrator/overview)

## Module Implementation

The modules are as follows:

* [security-integrator-api](security-integrator-api) - defines the interface for an integration
connector that is supported by the Security Integrator OMIS. This includes the implementation
of the context that wraps the Security Manager OMAS's clients.

* [security-integrator-server](security-integrator-server) - implements the context manager for
the Security Integrator OMIS.

* [security-integrator-spring](security-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [security-integrator-client](security-integrator-client) - implements a Java client for the REST API.


This integration service will ultimately be paired with the [Security Manager](../../access-services/security-manager)
Open Metadata Access Service (OMAS).  However, it is currently calling the 
[Security Manager OMAS](../../access-services/security-manager) while the Security Manager OMAS is implemented.
This means it is only monitoring security tags for assets rather than data fields.

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.