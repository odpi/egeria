<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Display Integrator Open Metadata Integration Service (OMIS)

The Display Integrator OMIS supports the exchange of displayed data such as reports and forms along with their related
schema and
connection information between an external system supporting the data display
and the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omis/display-integrator/overview)


## Module Implementation

The modules are as follows:

* [display-integrator-api](display-integrator-api) - defines the interface for an integration
connector that is supported by the Display Integrator OMIS.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [display-integrator-server](display-integrator-server) - implements the context manager for
the Analytics Integrator OMIS.

* [display-integrator-spring](display-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [display-integrator-client](display-integrator-client) - implements a Java client for the REST API.


This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).


----

* Return to the [integration services](..)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.