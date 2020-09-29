<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Open Metadata Integration Services (OMISs)

The integration services run in an [Integration Daemon](../admin-services/docs/concepts/integration-daemon.md)
OMAG Server.  Each type of integration service focuses on metadata exchange with a particular
type of third party technology.

Each integration service defines the API for the integration connectors that it manages along with a context manager
implementation.

The integration services are:

* [Catalog Integrator](catalog-integrator) - provides a two-way synchronization for data catalogs.
* [Database Integrator](database-integrator) - provides metadata extraction from relational databases.
* [Files Integrator](files-integrator) - collects metadata about files stored in a filesystem or file manager.
* [Organization Integrator](organization-integrator) - imports details of an organization's structure - such as teams and departments.


----
Return to [open-metadata-implementation](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.