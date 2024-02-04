<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Files Integrator Open Metadata Integration Service (OMIS)

The Files Integrator OMIS supports the collection of information about files
stored in a filesystem for and then storing it in the open metadata ecosystem.

* [Documentation](https://egeria-project.org/services/omis/files-integrator/overview)

## Module Implementation

The modules are as follows:

* [files-integrator-api](files-integrator-api) - defines the interface for an integration
connector that is supported by the Files Integrator OMIS.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [files-integrator-server](files-integrator-server) - implements the context manager for
the Files Integrator OMIS.

* [files-integrator-spring](files-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [files-integrator-client](files-integrator-client) - implements a Java client for the REST API.


This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.