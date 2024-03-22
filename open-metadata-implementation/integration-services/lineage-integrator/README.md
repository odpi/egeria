<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)


# Lineage Integrator Open Metadata Integration Service (OMIS)

The Lineage Integrator OMIS supports the loading lineage information
from a third party technology.  Specifically this includes process structure, ports,
schemas and the data assets they are working with.

* [Documentation](https://egeria-project.org/services/omis/lineage-integrator/overview)

## Module Implementation

The modules are as follows:

* [lineage-integrator-api](lineage-integrator-api) - defines the interface for an integration
connector that is supported by the Lineage Integrator OMIS. This includes the implementation
of the context that wraps the Asset Manager OMAS's clients.

* [lineage-integrator-server](lineage-integrator-server) - implements the context manager for
the Lineage Integrator OMIS.

* [lineage-integrator-spring](lineage-integrator-spring) - implements a rest API for validating that a specific
integration connector is able to run under this service.

* [lineage-integrator-client](lineage-integrator-client) - implements a Java client for the REST API.

This integration service is paired with the [Asset Manager](../../access-services/asset-manager)
Open Metadata Access Service (OMAS).

----

* Return to the [Integration Services](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.