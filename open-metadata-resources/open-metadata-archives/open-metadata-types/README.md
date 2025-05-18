<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Types

The Open Metadata Types provides the type definitions for open metadata.
They are packaged into an open metadata archive.

The open metadata types are [documented here](https://egeria-project.org/types).

The open metadata archive for these types is built by the `OpenMetadataTypesArchive` Java class.
It uses the Open Metadata Repository Services (OMRS) archive building
classes `OMRSArchiveBuilder` and `OMRSArchiveHelper` found in the `repository-services-archive-utilities` module.

The types for each release are managed in separate classes. For example:
* `OpenMetadataTypesArchive` - new candidate types for current release
* `OpenMetadataTypesArchive5_3` - approved types added for 5.3 release
* `OpenMetadataTypesArchive5_2` - approved types added for 5.2 release
* `OpenMetadataTypesArchive5_1` - approved types added for 5.1 release
*     :
* `OpenMetadataTypesArchive1_3` - approved types added for 1.3 release
* `OpenMetadataTypesArchive1_2` - approved types added for releases before 1.3

The approved types can only be changed through official patches.

It is possible to add new types by creating a similar Java class to `OpenMetadataTypesArchive` that defines
and maintains the additional types that you would like to define.  This is described in [Defining new types](https://egeria-project.org/guides/developer/open-metadata-archives/defining-new-types/).

You can create a JSON formatted archive file containing the open metadata types by running the
[`OpenMetadataTypesArchiveWriterUtility`](../open-metadata-types-utility).  The output is located in the `content-packs` directory and is included in the next build.

----

* [Return to Open Metadata Archives](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.