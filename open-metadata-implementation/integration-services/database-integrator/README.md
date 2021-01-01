<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![TechPreview](../../../open-metadata-publication/website/images/egeria-content-status-tech-preview.png#pagewidth)

# Database Integrator Open Metadata Integration Service (OMIS)

The Database Integrator integration service supports the exchange of data assets and their related schema and
connection information between an external relational database manager
and the open metadata ecosystem.

The modules are as follows:

* [database-integrator-api](database-integrator-api) - defines the interface for an integration
connector that is supported by the Database Manager Integrator integration service.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [database-integrator-server](database-integrator-server) - implements the context manager for
the Database Integrator integration service.

This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.