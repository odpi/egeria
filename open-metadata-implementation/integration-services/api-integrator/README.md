<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![TechPreview](../../../open-metadata-publication/website/images/egeria-content-status-tech-preview.png#pagewidth)

# API Integrator Open Metadata Integration Service (OMIS)

The API Integrator integration service supports the exchange of data assets and their related schema and
connection information between an external relational api manager
and the open metadata ecosystem.

The modules are as follows:

* [api-integrator-api](api-integrator-api) - defines the interface for an integration
connector that is supported by the API Manager Integrator integration service.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [api-integrator-server](api-integrator-server) - implements the context manager for
the API Integrator integration service.

This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.