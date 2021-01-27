<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Archive Manager

An [open metadata archive](../../../../open-metadata-resources/open-metadata-archives) provides pre-built definitions
for types and metadata instances.

OMRSArchiveManager manages the loading and unloading of open metadata archives for the local OMRS repository.
It is invoked at server start up for a [cohort member](../cohort-member.md)
and whenever a new open metadata archive is loaded via a REST API.
 
During server start up, it first loads the archive containing all of the
open metadata types, and then the archives 

## Related information

A description of the structure of archives along with utilities for building archives
can be found in the [open-metadata-archives](../../../../open-metadata-resources/open-metadata-archives)
modules.

Details for configuring a metadata server to load archives can be found in the
[Administration Guide](../../../admin-services/docs/user/configuring-the-startup-archives.md).



----
* Return to [repository services component descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.