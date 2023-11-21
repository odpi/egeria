<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Integration Connectors

The integration connectors are responsible for exchanging metadata with third
party technologies through the [Open Metadata Integration Services (OMISs)](../../../integration-services).
They run in the [Integration Daemon](https://egeria-project.org/concepts/integration-daemon)
OMAG Server.

An integration connector implements the specific connector interface from the integration
service that matches the metadata needs of the third party technology.
For example, the [Files Integration Connectors](files-integration-connectors)
catalogs files located in a specific directory of a file system.
They implement the integration connector interface of the
[Files Integrator Open Metadata Integration Service (OMIS)](../../../integration-services/files-integrator).

Details of Egeria's integration connectors are found in the
[Connector Catalog](https://egeria-project.org/connectors/#integration-connectors)

----

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.