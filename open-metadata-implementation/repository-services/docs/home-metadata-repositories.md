<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Home Metadata Repositories

The metadata repository where a metadata entity or relationship is created
is called its **home repository**.

Metadata in its home repository can be updated and deleted.

The [Open Metadata Repository Services (OMRS)](..) is responsible
for sharing this metadata with other metadata repositories who are
members of the same **[cohort](open-metadata-repository-cohort.md)**.

The shared copies are called **reference copies** and are read-only.
Update requests to a reference copy are automatically
redirected to the home repository by the OMRS, without the caller being aware.

Every metadata repository in a cohort has a unique identifier called the
**[local metadata collection id](metadata-collection-id.md)**.
This identifier is set up in the server configuration and shared when this
server **[connects to a cohort](component-descriptions/cohort-registry.md)**.

When metadata is shared by OMRS, each element is tagged with the metadata
collection id of its home repository.
OMRS is able to route update requests to the right server by comparing
the metadata collection
id in the metadata instance with the cohort registration information passed
between members of the cohort when they connect.


----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

