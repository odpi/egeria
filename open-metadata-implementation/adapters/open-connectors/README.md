<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Open Connectors

Open Connectors are connectors that support the 
Open Connector Framework (OCF) - see
the [open-connector-framework](../../frameworks/open-connector-framework/README.md) module.

This is the principle (preferred) mechanism for supporting pluggable
components in the open metadata implementation.  The OCF has additional uses
in providing reusable/pluggable components for external technology - particularly
related to data access.

The open connectors are grouped as follows:

* **[configuration-store-connectors](configuration-store-connectors)** contains the connectors that manage
the open metadata configuration.

* **[data-manager-connectors](data-manager-connectors)** work with data management platforms that
use metadata to describe and control access to data, such as Microsoft SQL Server, PostgreSQL and Unity Catalog.

* **[data-store-connectors](data-store-connectors)** contains OCF connectors to data stores on different
data platforms.

* **[dynamic-archiver-connectors](dynamic-archiver-connectors)** implementations of archive services
that run in the [Repository Governance OMES](../../engine-services/repository-governance).

* **[event-bus-connectors](event-bus-connectors)** supports different event/messaging infrastructures.
They can be plugged into the topic connectors from the access-service-connectors
and repository-service-connectors.

* **[governance-action-connectors](governance-action-connectors)** contains governance action services for 
assessing and acting on governance issues detected in the metadata ecosystem.
These connectors support the [OPEN Governance Framework (GAF)](../../frameworks/open-governance-framework).

* **[lovelace-insights](lovelace-insights)** provides the watchdog services that monitor the changing
open metadata ecosystem and add classifications that summarise the existing state of the metadata.

* **[metadata-security-connectors](metadata-security-connectors)** manage the 
authorization of requests to Egeria's services.

* **[integration-connectors](integration-connectors)** contains governance actions for assessing and acting on
governance issues detected in the metadata ecosystem.

* **[nanny-connectors](nanny-connectors)** support the observation, analysis and improvement of an existing
metadata catalog deployment, assembling digital products that represent collections of reference data and insights
based on the content of the open metadata repositories.

* **[report-generating-connectors](report-generating-connectors)** are governance action services that create
and publish reports on different types of elements.

* **[repository-services-connectors](repository-services-connectors)** contains connector implementations for
each type of connector supported by the Open Metadata Repository Services (OMRS).
These connectors enable the OMRS to be adapted to many different platforms.

* **[rest-client-connectors](rest-client-connectors)** provides a connector to call a REST API from
Java.

* **[secrets-store-connectors](secrets-store-connectors)** provides connectors to access secrets (passwords, certificates etc) at runtime.

* **[file-survey-connectors](file-survey-connectors)** implementations of survey action services
  from the [Open Survey Framework (OSF)](../../frameworks/open-survey-framework).

* **[system-connectors](system-connectors)** provides connectors to access data and function from different systems.

In addition, there is the **[connector-configuration-factory](connector-configuration-factory)**
that creates **Connection** objects to configure these connectors.

----
* Return to [Adapters](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
