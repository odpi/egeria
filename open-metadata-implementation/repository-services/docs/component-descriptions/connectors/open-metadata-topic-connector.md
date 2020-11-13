<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Topic Connector

The open metadata topic connector provides
a topic interface to a generic string event.

It is the interface implemented by specific
[Event Bus](../../../../admin-services/docs/concepts/event-bus.md).

Topic connectors for specific types of events
(such as the [OMRS Topic Connector](omrs-topic-connector.md))
are configured with an instance of an open metadata
topic connector embedded inside it.

The open metadata topic connector means that
only one connector need be implemented
for each type of event bus - rather
than one for each type of topic that
Egeria supports.

Implementations of this type of connector are
located in the
[adapters/open-connectors/repository-services-connectors/audit-log-connectors](../../../../adapters/open-connectors/event-bus-connectors)
module.

----
Return to [repository services connectors](.).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.