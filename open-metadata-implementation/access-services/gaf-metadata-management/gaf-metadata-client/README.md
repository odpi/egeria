<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# GAF Metadata Management Client

GAF Metadata Management supports a REST API for requests and an event-based
interface for asynchronous integration.  This client module provides the Java client packages that make it
easier for governance servers and applications to call these interfaces.

* `GovernanceConfigurationClient` supports the configuration of governance engines, governance services and
  integration groups.
* `GovernanceContextClient` provides the context used by a governance action service running in an engine host
  OMAG server to interact with open metadata and manage its governance actions.
* `EgeriaOpenGovernanceClient` provides an interface to the services that build, monitor and trigger governance
  actions.
* `EgeriaOpenGovernanceEventClient` and `GovernanceListenerManager` manage the client-side listening for events
  published on the service's out topic.
* `OIFContextManager` sets up the integration context for an integration connector running in an integration
  daemon.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
