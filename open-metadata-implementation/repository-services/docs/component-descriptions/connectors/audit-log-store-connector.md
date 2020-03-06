<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Audit Log Store Connector

The Audit Log Store Connector provides a common interface to
an audit log destination.
It is used by the OMRS's **[Audit Log](../audit-log.md)**.

The OMRS Audit Log supports multiple instances of the Audit Log Store
and will pass audit log records to each configured instance of the
Audit Log Store Connectors.

Implementations of this type of connector are
located in the
[adapters/open-connectors/repository-services-connectors/audit-log-connectors](../../../../adapters/open-connectors/repository-services-connectors/audit-log-connectors)
module.

----
Return to [repository services connectors](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
