<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuration Store Connectors

The configuration store connectors contain the connector implementations that manage
the [Configuration Documents](../../../admin-services/docs/concepts/configuration-document.md)
for [OMAG Servers](../../../admin-services/docs/concepts/omag-server.md).

The definition of the connector interface for these connectors is
defined in the [admin-services-connector](../../../admin-services/admin-services-api) module.

These are the configuration store connectors implemented by Egeria.

* **[configuration-file-store-connector](configuration-file-store-connector)** supports managing the
open metadata configuration as a clear text JSON file.

* **[configuration-encrypted-file-store-connector](configuration-encrypted-file-store-connector)** supports managing
the open metadata configuration as an encrypted JSON file.

A configuration store connector is needed by a
[OMAG Server Platform](../../../admin-services/docs/concepts/omag-server-platform.md).
Details of how to configure a configuration store connector in the
OMAG Server Platform is found [here](../../../admin-services/docs/user/configuring-the-configuration-document-store.md).
If no connector is configured, the OMAG Server Platform uses
the connector implement in the **configuration-file-store-connectors**
module as a default.

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
