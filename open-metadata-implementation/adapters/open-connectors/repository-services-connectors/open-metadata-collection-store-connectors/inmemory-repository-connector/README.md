<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


![Released](../../../../../../images/egeria-content-status-released.png#pagewidth)

# In Memory Repository Connector

The In-memory OMRS Repository Connector provides a simple repository
implementation that "stores" metadata in hash maps within the JVM. 
It is used for testing, or for environments where metadata maintained in other repositories
needs to be cached locally for performance/scalability reasons.

# Read-only Repository Connector

The read only repository connector provides a compliant implementation of a local repository
that can be configured into a [Metadata Access Store](https://egeria-project.org/concepts/metadata-access-store).
It does not support the interfaces for
create, update, delete.  However, it does support the search interfaces and is able to cache metadata.

This means it can be loaded with metadata from an
[Open Metadata Archive](https://egeria-project.org/concepts/open-metadata-archive) and connected
to a cohort.  The content from the archive will be shared with other members of the cohort.



----
Return to the [open-metadata-collection-store-connectors](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
