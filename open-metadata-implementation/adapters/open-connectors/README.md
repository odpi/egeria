<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Connectors

Open Connectors are connectors that support the 
Open Connector Framework (OCF) - see the open-connector-framework module.

This is the principle (preferred) mechanism for supporting pluggable
components in the open metadata implementation.  The OCF has additional uses
in providing reusable/pluggable components for external technology - particularly
related to data access.

The open connectors are grouped as follows:

* **access-services-connectors** contains the topic connector implementations
for each of the access services that support inbound events through an InTopic.

* **repository-services-connectors** contains connector implementations for
each type of connector supported by the Open Metadata Repository Services (OMRS).
These connectors enable the OMRS to be adapted to many different platforms.

* **configuration-store-connectors** contains the connectors that manage
the open metadata configuration.

* **event-bus-connectors** supports different event/messaging infrastructures.
They can be plugged into the topic connectors from the access-service-connectors
and repository-service-connectors.

* **governance-daemon-connectors** contains connectors for the governance
daemon servers that monitor activity or synchronize metadata and configuration
asynchronously between different tools.

* **data-store-connectors** contain OCF connectors to data stores on different
data platforms.