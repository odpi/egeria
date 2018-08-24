<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

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
found on the [OMRS Events page](./event-descriptions/README.md).

## OMRS Federated Event Topic