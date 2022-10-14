<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Topic Integrator Open Metadata Integration Service (OMIS)

The Topic Integrator OMIS supports the exchange of topic-based assets and the open metadata ecosystem.
Typically, these topics are managed by an event broker such as Apache Kafka.

* [Documentation](https://egeria-project.org/services/omis/topic-integrator/overview)

## Module Implementation

The modules are as follows:

* [topic-integrator-api](topic-integrator-api) - defines the interface for an integration
connector that is supported by the Topic Integrator OMIS.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [topic-integrator-server](topic-integrator-server) - implements the context manager for
the Topic Integrator OMIS.

* [topic-integrator-spring](topic-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [topic-integrator-client](topic-integrator-client) - implements a Java client for the REST API.


This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.