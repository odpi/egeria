<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Read-only Repository Connector

The read only repository connector provides a compliant implementation of a local repository
that can be configured into a [Metadata Server](../../../../../admin-services/docs/concepts/metadata-server.md).
It does not support the interfaces for
create, update, delete.  However it does support the search interfaces and is able to cache metadata.

This means it can be loaded with metadata from an
[Open Metadata Archive](../../../../../../open-metadata-resources/open-metadata-archives) and connected
to a cohort.  The content from the archive will be shared with other members of the cohort.


----
Return to the [open-metadata-collection-store-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.