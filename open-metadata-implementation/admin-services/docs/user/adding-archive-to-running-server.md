<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


## Adding an archive to a running OMAG Server

[Open Metadata Archives](../../../../open-metadata-resources/open-metadata-archives)
contain pre-canned metadata types and instances for [Cohort Members](../concepts/cohort-member.md).

Archives can be
[added to the configuration document](configuring-the-startup-archives.md) of a server
to ensure their content is loaded each time the server is started.  This is intended for
repositories that do not store the archive content but keep it in memory.
Although, if an archive is loaded multiple times, its content is only added to the local repository
if the repository does not have the content already.

Archives can also be loaded to a running server using the following commands.

Typically open metadata archives are stored as files.  
If the archive is stored as JSON in a file, it can be loaded into a running server
as follows.
The file should be specified either as a fully qualified path name
or as a path name relative to the start up directory of the OMAG Server Platform.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/instance/open-metadata-archives/file
{ path name of file }
```

Alternatively it is possible to set up the list of open metadata archives as a list of
[Connections](../../../frameworks/open-connector-framework/docs/concepts/connection.md).
These connections refer to
[open metadata archive connectors](../../../repository-services/docs/component-descriptions/connectors/open-metadata-archive-store-connector.md)
that can read and retrieve the open metadata archive content.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/instance/open-metadata-archives/connection
{ connection }
```

This option can be used when the open metadata archives are not stored in a file, or a different
file format from the default JSON is required.


----

* Return to [Operating an OMAG Server](operating-omag-server.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.