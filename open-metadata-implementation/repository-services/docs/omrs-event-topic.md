<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMRS Event Topic

The OMRS Topic is a [topic](https://egeria-project.org/concepts/basic-concepts/#topic)
provided through an [event broker](https://egeria-project.org/concepts/basic-concepts/#event-broker) (typically Apache Kafka)
that an [open metadata repository cohort](open-metadata-repository-cohort.md) uses to synchronize metadata
between metadata repositories.

It is accessed by the Open Metadata Repository Services (OMRS)
components through the [OMRS Topic Connector](component-descriptions/connectors/omrs-topic-connector.md).
The OMRS Topic Connector is a pluggable OCF Connector
that allows the use of different messaging infrastructures to
support the OMRS Topic without affecting the implementation of the OMRS.

## OMRS Topic Events

The OMRS Topic is used to send and receive OMRS Events.
There are three types of OMRS events that are sent/received on the OMRS Topic:

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

## Cohort Topic(s)

A cohort can be configured to use:

* One OMRS Event Topic for all types of OMRS events
* Three OMRS Event Topics, one for each type of OMRS event

Using the single cohort topic is ok for small environment.  However, the use of the three topics gives
the best throughput, ensures rapid inclusion of new members in the cohort and enables a member to connect multiple instances
of itself to the cohort for high availability (HA).

Versions of the OMRS prior to release 2.11 only support the single cohort topic.
To allow a server running an older version of OMRS to join a cohort using the three topics, it is possible to
configure the other members to use both the single topic and the three topics.
This ensures all members see the same metadata, but the members configured to use both options will process
all events twice.  This configuration should only be used when absolutely needed and attention should be
paid to upgrading the back-level server so it can use the three topics.

Details of configuring the different topic options can be found in the
[Administration Guide](https://egeria-project.org/guides/admin/servers).


## Enterprise OMRS Event Topic

The [enterprise repository services](subsystem-descriptions/enterprise-repository-services.md)
combine the OMRS events from all the [open metadata repository cohorts](open-metadata-repository-cohort.md)
that the server is connected to and makes them available to each local
[Open Metadata Access Service (OMAS)](../../access-services).  This is called the
enterprise OMRS event topic.  By default, it is implemented as an
[in-memory open metadata topic](../../adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors/inmemory-open-metadata-topic-connector).


----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.