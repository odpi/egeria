<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the local repository for a metadata server

A [Metadata Server](../concepts/metadata-server.md) supports a local metadata repository that has
native support for the
[Open Metadata Repository Services (OMRS) types and instances](../../../repository-services/docs/metadata-meta-model.md).
Egeria provides tow implementations of such a repository:

* A Graph Repository based on JanusGraph
* An in memory repository useful for demos and testing

## Enable the graph repository

This command enables a JanusGraph based metadata repository that is embedded in the Metadata Server
and uses the local disk to store the metadata.

```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serever}/local-repository/mode/local-graph-repository
```

## Enable the in-memory repository

The in-memory repository maintains an in-memory store of metadata. It is useful for demos and testing.
No metadata is kept if the open metadata services are deactivated,
or the server is shutdown.

```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository/mode/in-memory-repository
```


## Remove the local repository

This command removes all configuration for the local repository.
This includes the local metadata collection id.  If a new local repository is
added, it will have a new local metadata collection id and will
not be able to automatically re-register with its cohort(s).

```
DELETE {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/local-repository
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.