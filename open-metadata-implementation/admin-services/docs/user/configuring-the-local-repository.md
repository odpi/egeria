<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the local repository for a metadata server

A [Metadata Server](../concepts/metadata-server.md) supports a local metadata repository that has
native support for the
[Open Metadata Repository Services (OMRS) types and instances](../../../repository-services/docs/metadata-meta-model.md).
Egeria provides three implementations of such a repository:

* A graph repository based on JanusGraph.
* An in memory repository useful for demos and testing.
* A read only repository for hosting fixed content.

## Enable the graph repository

This command enables a JanusGraph based metadata repository that is embedded in the Metadata Server
and uses the local disk to store the metadata.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository/mode/local-graph-repository
```


## Enable the in-memory repository

The in-memory repository maintains an in-memory store of metadata. It is useful for demos and testing.
No metadata is kept if the open metadata services are deactivated,
or the server is shutdown.  It should nto be used in a production environment.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository/mode/in-memory-repository
```


## Enable the read-only repository

The read only repository connector provides a compliant implementation of a local
repository that can be configured into a Metadata Server.
It does not support the interfaces for create, update, delete.
However it does support the search interfaces and is able to cache metadata.

This means it can be loaded with metadata from an [Open Metadata Archive](../../../../open-metadata-resources/open-metadata-archives)
and connected to a cohort.
The content from the archive will be shared with other members of the cohort.


```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository/mode/read-only-repository
```


## Remove the local repository

This command removes all configuration for the local repository.
This includes the local metadata collection id.  If a new local repository is
added, it will have a new local metadata collection id and will
not be able to automatically re-register with its cohort(s).

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.