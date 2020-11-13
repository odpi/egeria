<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->
  
# Open Metadata Topic Connectors

The open metadata topic connectors are connectors that support
the base Open Metadata Topic used for asynchronous event passing between members of
the open metadata ecosystem.

The open metadata topic connectors pass events as Strings containing JSON documents.
The open metadata modules are implemented with beans to enable these JSON documents
to be parsed into Java.

The open metadata repository services (OMRS) api
module, [repository-services-apis](../../../../repository-services/repository-services-apis),
provides the base classes for implementing an open metadata
topic connector.  This includes the thread management.

There are two implementations of these base classes:

* **[inmemory-open-metadata-topic-connector](inmemory-open-metadata-topic-connector)** that
supports in-memory event passing between the OMRS and OMAS modules within a single server.

* **[kafka-open-metadata-topic-connector](kafka-open-metadata-topic-connector)** that
supports event passing over [Apache Kafka](https://kafka.apache.org).



----
Return to [event-bus-connectors](..) module.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
