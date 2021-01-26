<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Audit Log

Egeria's audit log provides a configurable set of destinations for audit records and other
diagnostic logging for an [OMAG Server](../concepts/omag-server.md).  Some destinations also support a query interface to allow an administrator
to understand how the server is running.

If the server is a development or test server, then the default audit log configuration is probably
sufficient.  This is the just the console audit log destination.
Using this option overrides all previous audit log destinations.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/default
```

If this server is a production server then you will probably want to set up the audit log
destinations explicitly.  You can add multiple destinations and each one can be set up
to process specific severities of log records.  These severities are provided as a list of strings
in the request body of the command.  The audit log severities are as follows:

* **Information** - The server is providing information about its normal operation.
* **Event** - An event was received from another member of the open metadata repository cohort.
* **Decision** - A decision has been made related to the interaction of the local metadata repository and the rest of the cohort.
* **Action** - An Action is required by the administrator. At a minimum, the situation needs to be investigated and 
  if necessary, corrective action taken.
* **Error** - An error occurred, possibly caused by an incompatibility between the local metadata repository
  and one of the remote repositories. The local repository may restrict some of the metadata interchange
  functions as a result.
* **Exception** - An unexpected exception occurred.  This means that the server needs some administration
  attention to correct configuration or fix a logic error because it is not operating as a proper peer in the
  open metadata repository cohort.
* **Security** - Unauthorized access to a service or metadata instance has been attempted.
* **Startup** - A new component is starting up.
* **Shutdown** - An existing component is shutting down.
* **Asset** - An auditable action relating to an asset has been taken.
* **Types** - Activity is occurring that relates to the open metadata types in use by this server.
* **Cohort** - The server is exchanging registration information about an open metadata repository cohort that
  it is connecting to.
* **Trace** - This is additional information on the operation of the server that may be
  of assistance in debugging a problem.  It is not normally logged to any destination, but can be added when needed.,
* **PerfMon** - This log record contains performance monitoring timing information for 
  specific types of processing. It is not normally logged to any destination, but can be added when needed.
* **\<Unknown\>** - Uninitialized Severity


If an empty list is passed as the request body then all severities are supported by the destination.

The command below adds the **console audit log destination**.  This writes selected parts of
each audit log record to stdout.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/console
{ supported severities }
```

The next command adds the **slf4j audit log destination**.  This writes full log records to the
slf4j ecosystem.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/slf4j
{ supported severities }
```

When configuring slf4j as destination you also need to specify auditlog logger category via the application properties.
This is described in [Connecting the OMAG Audit Log Framework](omag-server-platform-logging.md#connecting-the-omag-audit-log-framework) section of the developer logging guide.


The next command adds an audit log destination that creates log records as JSON files in a shared directory.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/files
{ supported severities }
```

The next command adds an audit log destination that sends each log record as an event on the supplied event topic.
It assumes that the [event bus](configuring-event-bus.md) is set up first.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/event-topic
{ supported severities }
```

The next command sets up an audit log destination that is described though a
[Connection](../../../frameworks/open-connector-framework/docs/concepts/connection.md).
The connection is passed in the request body.  The supported severities can be supplied in the
connection's configuration properties.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/connection
{ connection }
```

The following command clears the list of audit log destinations from the configuration
enabling you to add a new set of audit log destinations.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations/none
```

It is also possible to set up the audit log destinations as a list of connections.
Using this option overrides all previous audit log destinations.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/audit-log-destinations
{ list of connections }
```

----
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)
* Return to [configuration document structure](../concepts/configuration-document.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.