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

* **[data-store-connectors](data-store-connectors)** contains OCF connectors to data stores on different
data platforms.

* **[discovery-service-connectors](discovery-service-connectors)** implementations of open discovery services
from the [Open Discovery Framework (ODF)](../../frameworks/open-discovery-framework).

* **[dynamic-archiver-connectors](dynamic-archiver-connectors)** implementations of archive services
that run in the [Repository Governance OMES](../../engine-services/repository-governance).

* **[event-bus-connectors](event-bus-connectors)** supports different event/messaging infrastructures.
They can be plugged into the topic connectors from the access-service-connectors
and repository-service-connectors.

* **[governance-action-connectors](governance-action-connectors)** contains governance action services for 
assessing and acting on governance issues detected in the metadata ecosystem.
These connectors support the [Governance Action Framework (GAF)](../../frameworks/governance-action-framework).

* **[governance-daemon-connectors](governance-daemon-connectors)** contains connectors for the governance
servers that monitor activity or synchronize metadata and configuration
asynchronously between different tools.

* **[integration-connectors](integration-connectors)** contains governance actions for assessing and acting on
governance issues detected in the metadata ecosystem.

* **[repository-services-connectors](repository-services-connectors)** contains connector implementations for
each type of connector supported by the Open Metadata Repository Services (OMRS).
These connectors enable the OMRS to be adapted to many different platforms.

* **[rest-client-connectors](rest-client-connectors)** provides a connector to call a REST API from
Java.

* **[secrets-store-connectors](secrets-store-connectors)** provides connectors to access secrets (passwords, certificates etc) at runtime.

* **[system-connectors](system-connectors)** provides connectors to access data and function from different systems.

In addition, there is the **[connector-configuration-factory](connector-configuration-factory)**
that creates **Connection** objects to configure these connectors.

----
* Return to [Adapters](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
