<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMRS Event Topic

The OMRS Topic is an event topic, typically hosted by Apache Kafka,
that an open metadata repository cohort uses to synchronize metadata
between metadata repositories.
It is accessed by the Open Metadata Repository Services (OMRS)
components through the OMRS Topic Connector.
The OMRS Topic Connector is a pluggable OCF Connector
that allows the use of different messaging infrastructures to
support the OMRS Topic without affecting the implementation of the OMRS.

## OMRS Topic Events

The OMRS Topic is used to send and receive OMRS Events.  There are three types of OMRS events that are sent/received on the OMRS Topic:

* Registry Events - these are sent/received by the OMRS Cohort Registry.
These are used to register and unregister members of an open metadata repository cohort.
* TypeDef Events - these events describe changes to the type definitions (TypeDefs)
used by each of the open metadata repositories.
They are sent between the members of the cohort to ensure there are no
conflicts in the types each are using.
* Instance Events - these events are used to create reference (cached)
copies of metadata in the open metadata repositories.  They are used to speed up metadata queries by maintaining copies of slowly changing metadata by populating metadata instances locally in multiple open metadata repositories within a cohort.

TypeDef events and Instance events are collectively called Repository events
because they affect the contents of the open metadata repositories.
Details of the structure of the different types of OMRS Events can be
found on the [events descriptions](event-descriptions).

## Enterprise OMRS Event Topic

The [enterprise repository services](subsystem-descriptions/enterprise-repository-services.md)
combine the OMRS events from all of the [open metadata repository cohorts](open-metadata-repository-cohort.md)
that the server is connected to and makes them available to each local
[Open Metadata Access Service (OMAS)](../../access-services).  This is called the
enterprise OMRS event topic.  By default, it is implemented as an
[in-memory open metadata topic](../../adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/inmemory-open-metadata-topic-connector).


----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.