<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![Released](../../../images/egeria-content-status-released.png#pagewidth)

## Integration Daemon Services

The integration daemon services provide the implementation
of the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon)
OMAG Server which is responsible for operating the dynamic integration groups.

An integration daemon runs a set of [integration connectors](../../frameworks/open-integration-framework),
each managed by an `IntegrationConnectorHandler` that controls its lifecycle and periodic refresh.  Most
connectors are refreshed by a shared pool of `IntegrationConnectorRefreshThread`s; connectors that use blocking
calls are instead given their own `IntegrationConnectorDedicatedThread` so that they cannot delay the refresh of
other connectors.  The `IntegrationGroupHandler` maintains the connectors that belong to an
[integration group](https://egeria-project.org/concepts/integration-group), listening for configuration changes
via a `GroupConfigurationRefreshThread` so that connectors can be added, removed or reconfigured while the
integration daemon is running.

* [Documentation](https://egeria-project.org/services/integration-daemon-services)

----
* Return to the [Governance Servers](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.