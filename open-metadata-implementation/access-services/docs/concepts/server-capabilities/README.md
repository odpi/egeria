<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Software Server Capabilities

Many software servers are connected to the open metadata ecosystem, each with wildly differing
capabilities.

A software server capability documents a specific function that a software server is able to perform.
The open metadata type for a software se

## Metadata managing server capabilities

Some server capabilities create and manage metadata - in fact they are the true master of this
metadata. for example, a database manager (aka database management system or DBMS) is the true
master of metadata about database schemas.

When metadata managed by such software server capabilities is shared with an
[open metadata repository cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md),
the members of the cohort need to take note that they should not update this metadata.

The server capabilities that are recognized as managing metadata are:

* [Asset manager](asset-manager.md)
* [Master data manager](master-data-manager.md)
* [Engine](engine.md)
* [Tool](tool.md)

The identifiers of these software server capabilities are used to 
[document the provenance](../../../../../open-metadata-publication/website/metadata-provenance)
of any metadata that comes from these technologies.
Metadata instances that are mastered by specific software server capabilities are referred to
as **external instances**.

----
* Return to [Access Services Concepts](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.