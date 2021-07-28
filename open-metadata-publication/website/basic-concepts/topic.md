<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Topic

A topic is a service provided by an [Event Broker](event-broker.md) that offers
a publish-subscribe capability for a specific type of event.

Multiple servers can read and write events to a topic. 
Each topic maintains information about the events that a server has not read so that
it can receive each event even if it restarts.
Typically the events are processed in a first-in-first-out (FIFO)
order, but that is not necessarily guaranteed since it depends on the type and configuration of the event broker.

## Further information

Details of the different types of topics used by Egeria

* [OMRSTopic](../../../open-metadata-implementation/repository-services/docs/omrs-event-topic.md) - for open metadata repository cohorts
* [InTopic](../../../open-metadata-implementation/access-services/docs/concepts/client-server/in-topic.md) - for sending events to an Open Metadata Access Service (OMAS)
* [OutTopic](../../../open-metadata-implementation/access-services/docs/concepts/client-server/out-topic.md) - for receiving events from an Open Metadata Access Service (OMAS)

In addition, it is possible to automatically catalog details of the event brokers that your organization uses:
* [Cataloguing topics and event types for an event broker](../../../open-metadata-implementation/integration-services/topic-integrator)


----
* Return to [Glossary](../open-metadata-glossary.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.