<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Catalog Integrator Open Metadata Integration Service (OMIS)

The Catalog Integrator OMIS supports the exchange of asset information between a
third party asset management system and the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omis/catalog-integrator/overview)

## Module Implementation

The modules are as follows:

* [catalog-integrator-api](catalog-integrator-api) - defines the interface for an integration
connector that is supported by the Catalog Integrator OMIS. This includes the implementation
of the context that wraps the Asset Manager OMAS's clients.

* [catalog-integrator-server](catalog-integrator-server) - implements the context manager for
the Asset Manager Integrator OMIS.

* [catalog-integrator-spring](catalog-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [catalog-integrator-client](catalog-integrator-client) - implements a Java client for the REST API.

This integration service is paired with the [Asset Manager](../../access-services/asset-manager)
Open Metadata Access Service (OMAS).



----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.