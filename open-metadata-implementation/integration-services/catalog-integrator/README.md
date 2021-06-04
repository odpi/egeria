<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Catalog Integrator Open Metadata Integration Service (OMIS)

The Catalog Integrator integration service supports the exchange of asset information between a
third party asset management system and the open metadata ecosystem.

This involves:
* the transformation of metadata structures and names from the third party's APIs
to the open metadata APIs and types.
* the mapping of identifiers from the third.

## Implementation

The modules are as follows:

* [catalog-integrator-api](catalog-integrator-api) - defines the interface for an integration
connector that is supported by the Catalog Integrator integration service. This includes the implementation
of the context that wraps the Asset Manager OMAS's clients.

* [catalog-integrator-server](catalog-integrator-server) - implements the context manager for
the Asset Manager Integrator integration service.

This integration service is paired with the [Asset Manager](../../access-services/asset-manager)
Open Metadata Access Service (OMAS).



----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.