<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Specifications and Standards

ODPi Egeria is build up with layers of open components that build on many open standards relevant to
metadata management and governance.

Standards further influence the implementation of the Egeria libraries.

* [Metadata Metamodel](../../../open-metadata-implementation/repository-services/docs/metadata-meta-model.md) defines
how metadata is structured and organized through an open and extensible type system.

* [Open Metadata Types](../open-metadata-types) shows the pre-defined types included with Egeria.
These types have been built and are loaded into a [metadata server](../../../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md).

* [Open Metadata Repository Cohort Event Payloads](../../../open-metadata-implementation/repository-services/docs/event-descriptions) defines
the structure of the events that flow between members of an [open metadata repository cohort](../../../open-metadata-implementation/repository-services/docs/open-metadata-repository-cohort.md).

* [Open Metadata Archive Format](../../../open-metadata-resources/open-metadata-archives) - describes how to package up metadata into a
portable format for back up and sharing.

* [Open Metadata Frameworks](../../../open-metadata-implementation/frameworks) - define the Java interfaces for building
connectors and other plug-in components for Egeria.  Egeria 


## Core infrastructure

ODPi Egeria is written in Java.  It is accessible through Java Clients, REST APIs and the open event infrastructures
such as Apache Kafka.  The event payloads are JSON structures that are closely related to the JSON payloads
used in the REST APIs.

The deployment of of the code libraries are implemented using docker and helm charts.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
