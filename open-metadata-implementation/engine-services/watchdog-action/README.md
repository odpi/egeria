<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Watchdog Action Open Metadata Engine Service (OMES)

The Watchdog Action Open Metadata Engine services provide support for
[watchdog action engines](https://egeria-project.org/concepts/watchdog-action-engine/)
that are part of the [Open Watchdog Service (OWF)](https://egeria-project.org/frameworks/owf/overview/).
An watchdog action engine hosts the services that monitor for specification situations/events and issue notifications/actions to subscribers.

The Watchdog Action OMES is capable of hosting one or more
[watchdog action engines](https://egeria-project.org/concepts/watchdog-action-engine/)
and supports a REST API to request that a watchdog action engine runs a
[watchdog action service](https://egeria-project.org/concepts/watchdog-action-service/)
to begin monitoring and to access the results.
If/when the appropriate situation occurs, a notification/action is issued for each of the subscribers depending on their type.

The watchdog action engine services call the services
running in a Metadata Access Server to retrieve information about assets and to
store the results of the watchdog action services.

Detailed design documentation is found on the [egeria website](https://egeria-project.org/services/omes/watchdog-action/overview).

----
* Return to [engine services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.