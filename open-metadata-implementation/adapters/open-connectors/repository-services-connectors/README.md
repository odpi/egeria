<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Repository Services Connectors

The repository-services-connectors contains connector implementations for
each type of connector supported by the Open Metadata Repository Services (OMRS).
These connectors enable the OMRS to be adapted to many different platforms.

* **[audit-log-connectors](audit-log-connectors)** supports different destinations for 
audit log messages.

* **[cohort-registry-store-connectors](cohort-registry-store-connectors)** contains connectors the store of
cohort membership details used and maintained by the cohort registry.
There is a cohort registry (and hence a cohort registry store)
for each open metadata repository server.

* **[open-metadata-archive-connectors](open-metadata-archive-connectors)** contains connectors that can
read and write open metadata archives.

* **[open-metadata-collection-store-connectors](open-metadata-collection-store-connectors)** contains connectors that
support mappings to different vendor's metadata repositories.

The **[connector-configuration-factory](../connector-configuration-factory)** sets
up connections to the default implementations
of each type of the repository services connectors.