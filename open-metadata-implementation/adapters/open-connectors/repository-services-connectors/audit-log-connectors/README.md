<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Audit Log Connectors

The audit log connectors supports different destinations for audit log messages.

* **[audit-log-console-connector](audit-log-console-connector)** - supports the output of audit log records to stdout.

* **[event-display-audit-log-connector](audit-log-console-event-display-connector)** - an implementation of an Audit
Log Destination Connector that displays the contents of event added ans additionalInformation in EVENT audit log record.
Change the format or add additional information.

* **[audit-log-file-connector](audit-log-file-connector)** - supports a directory of JSON files that each contain an audit log record.
* 
* **[audit-log-jdbc-connector](audit-log-jdbc-connector)** - supports a JDBC Schema of tables that store the data from the audit log records.

* **[audit-log-slf4j-connector](audit-log-slf4j-connector)** - supports the logging of log record to the slf4j ecosystem.

* **[audit-log-event-topic-connector](audit-log-event-topic-connector)** - supports the publishing of log records to an event topic.

All of these connectors support the **supportedSeverities** configuration property which defines which log records are
sent to the destination based on their severity.


----
Return to [repository-services-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
