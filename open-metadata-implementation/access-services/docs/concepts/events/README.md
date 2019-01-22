<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Events

Most Open Metadata Access Services (OMASs) support an asynchronous
event APIs.  This may involve publishing events, receiving events
or both.

These events are sent on the access service's
[OutTopic](../client-server/out-topic.md) and received on
the access service's [InTopic](../client-server/in-topic.md).

The events from an access service typically inherit from the
same Java class and have a type field to indicate that type
of event.  The properties that follow are determined by the
type.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.