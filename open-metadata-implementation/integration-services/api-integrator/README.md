<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# API Integrator Open Metadata Integration Service (OMIS)

The API Integrator OMIS supports the exchange of data assets and their related schema and
connection information between an external API manager
and the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omis/api-integrator/overview)


## Module Implementation

The modules are as follows:

* [api-integrator-api](api-integrator-api) - defines the interface for an integration
connector that is supported by the API Manager Integrator OMIS.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [api-integrator-server](api-integrator-server) - implements the context manager for
the API Integrator OMIS.

* [api-integrator-spring](api-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [api-integrator-client](api-integrator-client) - implements a Java client for the REST API.


This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.