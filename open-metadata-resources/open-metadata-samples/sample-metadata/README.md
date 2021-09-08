<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

## Sample Metadata

Sample metadata provides the utilities to load a metadata server with a variety of sample
metadata which can be used for demos.  This metadata may then be exported into an
[Open Metadata Archive](../../open-metadata-archives) of type **RepositoryBackup**
so that it can be loaded into a repository in the open metadata labs.

As long as the
metadata collection identifier of the target repository matches the metadata collection
identifier of repository backup open metadata archive then the metadata will be editable in the target repository.
If the metadata collection identifier of the target repository is different from the
repository backup open metadata archive then the metadata from the archive will not load.

There are two utilities that load metadata into a repository:

* [Coco Pharmaceuticals Business Systems](coco-business-systems) provides a catalog of the business systems and the lineage between
them and the load of their data into the data lake.  This archive simulates the
type of metadata expected from an ETL tool suite.  It is intended for
**cocoMDS5** in the open metadata labs.

* [Coco Pharmaceuticals Governance Program](coco-governance-program) provides the
governance definitions that drive Coco Pharmaceuticals' governance program.
This is intended for **cocoMDS2** in the open metadata labs.

In addition to the utilities that load metadata into the repository is the utility that
extracts the metadata from the repository to build the open metadata archive.
This works on a list of open metadata types

----

* Return to the [open metadata samples](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.