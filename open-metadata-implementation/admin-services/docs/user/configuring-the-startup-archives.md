<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Open Metadata Archives that are loaded on Server Startup

An open metadata archive is a store of read-only metadata types and instances.
One or more open metadata archives can be configured to load at the start up of the
following types of [OMAG Servers](../concepts/omag-server.md).

* [Metadata Access Point](../concepts/metadata-access-point.md)
* [Metadata Server](../concepts/metadata-server.md)
* [Repository Proxy](../concepts/repository-proxy.md)

These OMAG Servers are collectively called [Cohort Members](../concepts/cohort-member.md).
It should be noted that any instances found in the open metadata archive will not
be loaded into a Metadata Access Point server because it does not have a local repository.

Typically an open metadata archive is stored as JSON format in a file.  To configure the load of such a file
use the following command.  The file should be specified either as a fully qualified path name
or as a path name relative to the start up directory of the OMAG Server Platform.
Note the file name should not have any quotes around its name.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/open-metadata-archives/file
{ path name of file }
```

Alternatively it is possible to set up the list of open metadata archives as a list of
[Connections](../../../frameworks/open-connector-framework/docs/concepts/connection.md).
These connections refer to connectors that can read and retrieve the open metadata archive content from a particular type of store.
```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/open-metadata-archives
{ list of connections }
```
This option can be used when the open metadata archives are not stored in a file, or a different
file connector from the default one for the OMAG Server Platform is required.

Finally this is how to remove the archives from the configuration document.

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/open-metadata-archives
{ path name of file }
```

## Additional Information

* [Loading an open metadata archive in a running server](adding-archive-to-running-server.md)
* [Open Metadata Archive Store Connectors in the Connector Catalog](../../../../open-metadata-publication/website/connector-catalog/runtime-connectors.md)

----
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)
* Return to [Configuring a Metadata Access Point](../concepts/metadata-access-point.md#Configuring-a-Metadata-Access-Point)
* Return to [Configuring a Metadata Server](../concepts/metadata-server.md#Configuring-a-Metadata-Server)
* Return to [Configuring a Repository Proxy](../concepts/repository-proxy.md#Configuring-a-Repository-Proxy)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.