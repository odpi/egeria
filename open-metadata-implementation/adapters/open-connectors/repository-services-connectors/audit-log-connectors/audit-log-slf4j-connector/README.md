<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

# The slf4j audit log connector

This connector outputs messages to the configured slf4j destinations for the server.
These messages can then be captured in application debug files.

Care needs to be taken when using this connector since the output of the audit log records
includes values from the metadata instances which may contain sensitive information.

----
Return to [audit-log-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.