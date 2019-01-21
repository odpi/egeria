<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Capabilities

Many software servers are connected to the open metadata ecosystem, each with wildly differing
capabilities.

A server capability documents a specific function that a software server is able to perform.

## Metadata managing server capabilities

Some server capabilities create and manage metadata - in fact they are the true master of this
metadata.  When this metadata is shared with an
[open metadata repository cohort](../../../../repository-services/docs/open-metadata-repository-cohort.md),
the members of the cohort need to take note that they should not update this metadata.

The server capabilities that are recognized as managing metadata are:

* [Master data manager](master-data-manager.md)
* [Engine](engine.md)
* [Tool](tool.md)





----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.