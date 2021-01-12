<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)


# Lineage Integrator Open Metadata Integration Service (OMIS)

The Lineage Integrator integration service supports the loading lineage information
from a third party technology.  Specifically this include process structure, ports,
schemas and the data assets they are working with.

The modules are as follows:

* [lineage-integrator-api](lineage-integrator-api) - defines the interface for an integration
connector that is supported by the Lineage Integrator integration service. This includes the implementation
of the context that wraps the Asset Manager OMAS's clients.

* [lineage-integrator-server](lineage-integrator-server) - implements the context manager for
the Lineage Integrator integration service.

This integration service is paired with the [Asset Manager](../../access-services/asset-manager)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.