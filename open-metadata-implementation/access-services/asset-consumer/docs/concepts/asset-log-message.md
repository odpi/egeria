<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset log message

The asset log message is a type of log record that can be added to the local server's
[audit log](../../../../repository-services/docs/component-descriptions/audit-log.md).

It is used to record that a specific action has been taken on an asset.

The log record contains the following information:

* **userId** -              userId of user making request.
* **requestType** -           unique id for the asset.
* **connectorInstanceId** - (optional) id of connector in use (if any).
* **connectionName** -      (optional) name of the connection (extracted from the connector).
* **connectorType** -       (optional) type of connector in use (if any).
* **contextId** -           (optional) function name, or processId of the activity that the caller is performing.
* **message** -             log record content.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.