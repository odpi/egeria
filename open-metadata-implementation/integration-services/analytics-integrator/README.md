<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Analytics Integrator Open Metadata Integration Service (OMIS)

The Analytics Integrator OMIS supports the exchange of glossary, data assets, schemas and analytics models between analytics tools
and the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omis/analytics-integrator/overview)


## Module Implementation

The modules are as follows:

* [analytics-integrator-api](analytics-integrator-api) - defines the interface for an integration
connector that is supported by the Analytics Manager Integrator OMIS.  This includes the implementation
of the context that wraps the Analytics Modeling OMAS's clients.

* [analytics-integrator-server](analytics-integrator-server) - implements the context manager for
the Analytics Integrator OMIS.

* [analytics-integrator-spring](analytics-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [analytics-integrator-client](analytics-integrator-client) - implements a Java client for the REST API.


This integration service is paired with the [Analytics Modeling](../../access-services/analytics-modeling)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.