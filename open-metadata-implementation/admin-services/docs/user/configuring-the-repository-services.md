<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Open Metadata Repository Services (OMRS)

The [Open Metadata Repository Services](../../../repository-services) provide support for access and exchange
of open metadata along with support for audit logging.

A OMAG server always needs the repository services because they all need to at least configure an audit log.

In addition the **Open Metadata Servers** and **Repository Proxies** need to set up the local repository and
connect to a cohort if they are to share metadata.  You may also define a list of open metadata
archives to load into the metadata repository on server start up.

See [OMAG Server](../concepts/omag-server.md) for descriptions of these types of servers.

## Configuring the audit log

The repository services audit log provides a configurable set of destinations for audit records and other
diagnostic logging.  Some destinations also support a query interface to allow an administrator
to understand how the server is running.

If the server is a development or test server, then the default audit log configuration is probably
sufficient.  This is the just the console audit log destination.
Using this option overrides all previous audit log destinations.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/default
```

If this server is a production server then you will probably want to set up the audit log
destinations explicitly.  You can add multiple destinations and each one can be set up
to process specific severities of log records.  These severities are provided as a list of strings
in the request body of the command.  The audit log severities are as follows:

* **Information** - The server is providing information about its normal operation.
* **Event** - An OMRSEvent was received from another member of the open metadata repository cohort.
* **Decision** - A decision has been made related to the interaction of the local metadata repository and the rest of the cohort.
* **Action** An Action is required by the administrator. At a minimum, the situation needs to be investigated and 
  if necessary, corrective action taken.
* **Error** - An error occurred, possibly caused by an incompatibility between the local metadata repository
  and one of the remote repositories. The local repository may restrict some of the metadata interchange
  functions as a result.
* **Exception** - An unexpected exception occurred.  This means that the server needs some administration
  attention to correct configuration or fix a logic error because it is not operating as a proper peer in the
  open metadata repository cohort.
* **Security** - Unauthorized access to a service or metadata instance has been attempted.

If an empty list is passed as the request body then all severities are supported by the destination.

The command below adds the **console audit log destination**.  This writes selected parts of
each audit log record to stdout.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/console
{ supported severities }
```

The next command adds the **slf4j audit log destination**.  This writes full log records to the
slf4j ecosystem.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/slf4j
{ supported severities }
```

The next command adds an audit log destination that creates log records as JSON files in a shared directory.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/files
{ supported severities }
```

The next command adds an audit log destination that sends each log record as an event on the supplied event topic.
It assumes that the [event bus](configuring-event-bus.md) is set up first.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/event-topic
{ supported severities }
```

The next command sets up an audit log destination that is described though a
[Connection](../../../frameworks/open-connector-framework/docs/concepts/connection.md).
The connection is passed in the request body.  The supported severities can be supplied in the
connection's configuration properties.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/connection
{ connection }
```

The following command clears the list of audit log destinations from the configuration
enabling you to add a new set of audit log destinations.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations/none
```

It is also possible to set up the audit log destinations as a list of connections.
Using this option overrides all previous audit log destinations.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/audit-log-destinations
{ list of connections }
```

## Setting up the local repository

A local repository is optional.
The administration services can be used to enable one of the built-in
local repositories.

#### Enable the graph repository

This command enables a JanusGraph based metadata repository that is embedded in the OMAG Server
and uses the local disk to store the metadata.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/local-graph-repository
```

#### Enable the in-memory repository

The in-memory repository maintains an in-memory store of metadata. It is useful for demos and testing.
No metadata is kept if the open metadata services are deactivated,
or the server is shutdown.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/mode/in-memory-repository
```

#### Enable OMAG Server as a repository proxy

The OMAG Server can act as a proxy to a vendor's repository.
This is done by adding the connection
for the repository proxy as the local repository.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/proxy-details?connectorProvider={javaClassName}
```

#### Add the local repository's event mapper

Any open metadata repository that supports its own API needs an
event mapper to ensure the
Open Metadata Repository Services (OMRS) is notified when
metadata is added
to the repository without going through the open metadata APIs.

The event mapper is a connector that listens for proprietary events
from the repository and converts them into calls to the OMRS.
The OMRS then distributes this new metadata.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/event-mapper-details?connectorProvider={javaClassName}&eventSource={resourceName}
```

For example, to enable the IBM Information Governance Catalog event mapper,
POST the following (where `igc.hostname.somewhere.com` is the hostname of the
domain (services) tier of the environment, and `59092` is the port on which
its Kafka bus can be accessed):

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository/event-mapper-details?connectorProvider=org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.IGCOMRSRepositoryEventMapperProvider&eventSource=igc.hostname.somewhere.com:59092
```

#### Remove the local repository

This command removes all configuration for the local repository.
This includes the local metadata collection id.  If a new local repository is
added, it will have a new local metadata collection id and will
not be able to automatically re-register with its cohort(s).

```
DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/local-repository
```

### Cohort registration

Open metadata repository cohorts are a set of metadata servers
that are sharing metadata using the open metadata services.
They use a peer-to-peer protocol coordinated through an event bus topic
(typically this is an Apache Kafka topic).

#### Enable access to a cohort

The following command registers the server with cohort called `cocoCohort`.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cocoCohort
```

#### Disconnect from a cohort

This command unregisters a server from a cohort called `cocoCohort`.

```
DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/cohorts/cocoCohort
```

## Load one or more open metadata archives at server startup

An open metadata archive is a store of read-only metadata types and instances.
One or more open metadata archives can be configured to load at server start up.

Typically open metadata archives are stored as files.  To configure the load of a file
use the following command.  The file should be specified either as a fully qualified path name
or as a path name relative to the start up directory of the OMAG Server Platform.

```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/open-metadata-archives/file
{ path name of file }
```

Alternatively it is possible to set up the list of open metadata archives as a list of
[Connections](../../../frameworks/open-connector-framework/docs/concepts/connection.md).
These connections refer to connectors that can read and retrieve the open metadata archive content.
```
POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/open-metadata-archives
{ list of connections }
```
This option can be used when the open metadata archives are not stored in a file, or a different
file connector from the default one for the OMAG Server Platform is required.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.