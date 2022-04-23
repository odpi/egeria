<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuration Document Store Connectors

The configuration document store connectors contain the connector implementations that manage
the [Configuration Documents](https://egeria-project.org/concepts/configuration-document/)
for [OMAG Servers](https://egeria-project.org/concepts/omag-server/).

The definition of the connector interface for these connectors is
defined in the [admin-services-api](../../../admin-services/admin-services-api) module.

These are the configuration document store connectors implemented by Egeria.

* **[configuration-file-store-connector](configuration-file-store-connector)** supports managing the
open metadata configuration document as a clear text JSON file.

* **[configuration-encrypted-file-store-connector](configuration-encrypted-file-store-connector)** supports managing
the open metadata configuration document as an encrypted JSON file.

A configuration store connector is needed by a
[OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform).
Details of how to configure a configuration store connector in the
OMAG Server Platform is found [here](https://egeria-project.org/guides/admin/configuring-the-omag-server-platform).
If no connector is configured, the OMAG Server Platform uses
the connector implemented in the **configuration-encrypted-file-store-connector**
module as a default.

Return to [open-connectors](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
