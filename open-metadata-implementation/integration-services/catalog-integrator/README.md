<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Asset Manager Integrator Open Metadata Integration Service (OMIS)

The Asset Manager Integrator integration service supports the exchange of asset information between an external
asset management system and the open metadata ecosystem.

The modules are as follows:

* [asset-manager-integrator-api](asset-manager-integrator-api) - defines the interface for an integration
connector that is supported by the Asset Manager Integrator integration service. This includes the implementation
of the context that wraps the Asset Manager OMAS's clients.

* [asset-manager-integrator-server](asset-manager-integrator-server) - implements the context manager for
the Asset Manager Integrator integration service.

This integration service is paired with the [Asset Manager](../../access-services/asset-manager)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.