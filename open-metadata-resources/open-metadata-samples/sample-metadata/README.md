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

The utilities that load metadata into a repository are as follows:

* [Simple Catalogs](simple-catalogs) provides four archives that are each loaded into their own metadata server
  that are in turn connected together using an open metadata repository connector.

* [Coco Pharmaceuticals Metadata Archives](coco-metadata-archives) provides the 7 archives used in the Coco
  Pharmaceuticals labs and demos, including the organization's profiles and teams, its governance program
  definitions, its business systems catalog and lineage (intended for **cocoMDS5**), and its sustainability
  initiative definitions.

* [Big Glossaries](big-glossaries) creates 10 glossaries of 10,000 unique terms each in their own archive
  file, used for testing that a deployment environment has enough resources to manage a large repository.

* [Cloud Information Model](cloud-information-model) builds the Cloud Information Model open metadata
  archive from its `jsonld` model, describing a glossary, data dictionary and concept model for the
  properties, data fields and objects in the [Cloud Information Model](https://github.com/cloudinformationmodel/cloudinformationmodel).


----

* Return to the [open metadata samples](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.