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

## Configuring the audit log (all OMAG Servers)

See [Configuring the audit log](configuring-the-audit-log.md).


## Configuring the local repository (Metadata Server Only)

A local repository is optional.
The administration services can be used to enable one of the built-in
local repositories.

See [Configuring the Local Repository](configuring-the-local-repository.md).


## Configuring the repository proxy connectors (Repository Proxy only)

See [Configuring the repository proxy connectors](configuring-the-repository-proxy-connector.md).


## Remove the local repository

This command removes all configuration for the local repository.
This includes the local metadata collection id.  If a new local repository is
added, it will have a new local metadata collection id and will
not be able to automatically re-register with its cohort(s).

```
DELETE {serverURLroot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository
```

### Cohort registration

Open metadata repository cohorts are a set of metadata servers
that are sharing metadata using the open metadata services.
They use a peer-to-peer protocol coordinated through an event bus topic
(typically this is an Apache Kafka topic).

See [Configuring registration to a cohort](configuring-registration-to-a-cohort.md)
for information on connecting a server to a cohort.


## Load one or more open metadata archives at server startup

An open metadata archive is a store of read-only metadata types and instances.
One or more open metadata archives can be configured to load at server start up.

Typically open metadata archives are stored as files.  To configure the load of a file
use the following command.  The file should be specified either as a fully qualified path name
or as a path name relative to the start up directory of the OMAG Server Platform.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/open-metadata-archives/file
{ path name of file }
```

Alternatively it is possible to set up the list of open metadata archives as a list of
[Connections](../../../frameworks/open-connector-framework/docs/concepts/connection.md).
These connections refer to connectors that can read and retrieve the open metadata archive content.
```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/open-metadata-archives
{ list of connections }
```
This option can be used when the open metadata archives are not stored in a file, or a different
file connector from the default one for the OMAG Server Platform is required.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.