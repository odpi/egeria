<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Open Metadata Integration Services (OMISs)

The integration services run in an [Integration Daemon](https://egeria-project.org/concepts/integration-daemon)
OMAG Server.  Each type of integration service focuses on metadata exchange with a particular
type of third party technology.

The integration services available today are:

* [API Integrator](api-integrator) - provides cataloguing for APIs.
* [Analytics Integrator](analytics-integrator) - provides cataloguing for Analytics tools.
* [Catalog Integrator](catalog-integrator) - provides a two-way synchronization for data catalogs.
* [Database Integrator](database-integrator) - provides metadata extraction from relational databases.
* [Display Integrator](display-integrator) - provides metadata extraction from systems that provide user displays and forms to capture new data values.
* [Files Integrator](files-integrator) - collects metadata about files stored in a filesystem or file manager.
* [Lineage Integrator](lineage-integrator) - collects metadata about processes, their internal logic and the data assets they work with.
* [Organization Integrator](organization-integrator) - imports details of an organization's structure - such as teams and departments.
* [Search Integrator](search-integrator) - maintains search indexes.
* [Security Integrator](security-integrator) - distributes security properties to access control enforcement points.
* [Topic Integrator](topic-integrator) - provides cataloguing of topics and event schema for event brokers.

More information about the operation of the integration daemon and the integration services within,
along with details of how to set up these integration services are
located in the [Administration Guide](https://egeria-project.org/guides/admin/servers/configuring-an-integration-daemon/).

----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.