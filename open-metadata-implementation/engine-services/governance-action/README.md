<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Governance Action Open Metadata Engine Services (OMES)

Governance Action Open Metadata Engine Service (OMES) runs in an
[Engine Host](../../admin-services/docs/concepts/engine-host.md) OMAG Server.

It provides access to the open metadata ecosystem for
[Governance Action Services](../../frameworks/governance-action-framework/docs/governance-action-service.md).
These are pluggable connectors that manage governance of open metadata.
Their interfaces are defined by the [Governance Action Framework (GAF)](../../frameworks/governance-action-framework)
and supported by the Governance Action OMES.

The governance Action OMES also provides an API to allow a third party tool to validate
that a specific governance action service will load in the Engine Host server and
it returns the usage information encoded in the service's implementation.


## Using the Governance Action OMES

Governance action services are defined and linked to one or more
[Governance Action Engines](../../frameworks/governance-action-framework/docs/governance-action-engine.md)
using the [Governance Engine OMAS](../../access-services/governance-engine).
The definitions for the both the governance action engines and their linked services
are stored in a [Metadata Server](../../admin-services/docs/concepts/metadata-server.md).

When the Governance Action OMES is configured in the Engine Host, a list of governance action engines
is supplied.  This determines which governance action engines and hence governance action services that it supports.

The Governance Action OMES is responsible for initializing the governance action engines and providing the
context and runtime environment for [governance action services](../../frameworks/governance-action-framework/docs/governance-action-service.md)
when they are requested by third party technologies or through
the governance action processing of the [Governance Engine OMAS](../../access-services/governance-engine).


----
* Return to the [Engine Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.