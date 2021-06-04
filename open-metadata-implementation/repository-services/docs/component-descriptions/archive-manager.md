<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Archive Manager

An [open metadata archive](../open-metadata-archive.md) provides pre-built definitions
for types and metadata instances.

**OMRSArchiveManager** manages the loading and unloading of open metadata archives for the local OMRS repository.
It is invoked at server start up for a [cohort member](../cohort-member.md)
and whenever a new open metadata archive is loaded via a REST API.
 
During server start up, it first calls the [Repository Content Manager](repository-content-manager.md)
to load the types into the local repository (if any) and to maintain the cache of know and active types in the server.

It then calls the [Local Repository Instance Event Processor](local-repository-instance-event-processor.md) to
load the instances. 

## Related information

A description of the utilities for building archives
can be found in the [open-metadata-archives](../../../../open-metadata-resources/open-metadata-archives)
modules.

Details for configuring a metadata server to load archives can be found in the
[Administration Guide](../../../admin-services/docs/user).

* [Configuring an open metadata archive in an OMAG Server](../../../admin-services/docs/user/configuring-the-startup-archives.md)
* [Adding an open metadata archive to a running OMAG Server](../../../admin-services/docs/user/adding-archive-to-running-server.md)

----
* Return to [repository services component descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.