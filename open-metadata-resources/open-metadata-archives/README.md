<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata Archives
  
The open metadata archives provide pre-canned content (open metadata types and instances) to load into an open metadata
repository.  There are two main types of open metadata archive:

* **Content packs** - read only metadata types and instances that are reusable in many organizations.
* **Metadata exports** - metadata exported from a specific open metadata repository that can act as a backup.
It is read-write if loaded into an open metadata repository with the same metadata collection id as the originating
repository, or read-only if loaded into a repository with a different metadata collection id.

A metadata server's configuration document can list the
archives to load each time the server is started.  This is useful if the server does not retain
metadata through a server restart (like the in memory metadata repository).
Open metadata archives may also be loaded while the server is running using a REST API call.

```
POST {server-url-root}/open-metadata/admin-services/users/{userId}/servers/{serverName}/instance/open-metadata-archives/file
```
With the file name in the request body.

The archive is loaded once and its content is immediately available.
If the repository persists metadata over a server restart then this archive content
continues to be available after the server restarts.

It does not matter how many times an archive is loaded, only one copy of the content is
added to the repository.

## Inside an Open Metadata Archive

The open metadata archive has three parts to it.  This is shown in **Figure 1**.
The header defines the type of archive and its properties.  Then there is the type store.
This contains new types and updates to types (patches).  Finally there is the instance store.
This contains new instances (entities, relationships and classifications).

![Figure 1](docs/open-metadata-archives-structure.png)
> **Figure 1:** Inside an Open Metadata Archive

The archive loads in the following order:

* Attribute Type Definitions (AttributeTypeDefs) from the type store.
  * PrimitiveDefs
  * CollectionDefs
  * EnumDefs
* New Type Definitions (TypeDefs) from the type store.
  * EntityDefs
  * RelationshipDefs
  * ClassificationDefs
* Updates to types (TypeDefPatches)
* New Instances
  * Entities
  * Relationships
  * Classifications

## Supported utilities for open metadata archives

ODPi Egeria supports the following open metadata archives.  Associated with each archive
are utilities that help you build additional archives of your own content.

* [Open Metadata Types](open-metadata-types) - the ODPi Egeria Open Metadata Type Definitions.
This archive is always loaded by each OMAG metadata repository server at start-up.
This is to reduce the chance that new types developed by a third party have names that conflict with the open metadata types.
There is also a utility to create the archive file for these open metadata types.

* [Open Connector Archives](open-connector-archives) - provides utilities for building
open metadata archives containing information about one or more connectors that
follow the [Open Connector Framework (OCF)](../../open-metadata-implementation/frameworks/open-connector-framework).
In addition, there are utilities for building an open metadata archive containing the connector type
definitions for ODPi Egeria's data store connectors.

* [Design Model Archives](design-model-archives) - provides utilities to
manage common/standard model content from third parties.  It includes
an example archive for the [Cloud Information Model (CIM)](https://cloudinformationmodel.org).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
