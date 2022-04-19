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
 * `OpenMetadataTypesArchive2_11` - approved types added for [2.11 release](../../../release-notes/release-notes-2-11.md)
 * `OpenMetadataTypesArchive2_10` - approved types added for [2.10 release](../../../release-notes/release-notes-2-10.md)
 * `OpenMetadataTypesArchive2_9` - approved types added for [2.9 release](../../../release-notes/release-notes-2-9.md)
 * `OpenMetadataTypesArchive2_8` - approved types added for [2.8 release](../../../release-notes/release-notes-2-8.md)
 * `OpenMetadataTypesArchive2_7` - approved types added for [2.7 release](../../../release-notes/release-notes-2-7.md)
 * `OpenMetadataTypesArchive2_6` - approved types added for [2.6 release](../../../release-notes/release-notes-2-6.md)
 * `OpenMetadataTypesArchive2_5` - approved types added for [2.5 release](../../../release-notes/release-notes-2-5.md)
 * `OpenMetadataTypesArchive2_4` - approved types added for [2.4 release](../../../release-notes/release-notes-2-4.md)
 * `OpenMetadataTypesArchive2_0` - approved types added for [2.0 release](../../../release-notes/release-notes-2-0.md)
 * `OpenMetadataTypesArchive1_7` - approved types added for [1.7 release](../../../release-notes/release-notes-1-7.md)
 * `OpenMetadataTypesArchive1_6` - approved types added for [1.6 release](../../../release-notes/release-notes-1-6.md)
 * `OpenMetadataTypesArchive1_5` - approved types added for [1.5 release](../../../release-notes/release-notes-1-5.md)
 * `OpenMetadataTypesArchive1_4` - approved types added for [1.4 release](../../../release-notes/release-notes-1-4.md)
 * `OpenMetadataTypesArchive1_3` - approved types added for [1.3 release](../../../release-notes/release-notes-1-3.md)
 * `OpenMetadataTypesArchive1_2` - approved types added for releases before 1.3
 
The approved types can only be changed through official patches.

It is possible to add new types by creating a similar Java class to `OpenMetadataTypesArchive` that defines
and maintains the additional types that you would like to define.

If you would like your new types to extend the open metadata types, set the open metadata archive as an
archive that your new archive depends on ...

```
        List<OpenMetadataArchive>  dependentOpenMetadataArchives = new ArrayList<>();

        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);
```

This approach is used in the `design-model-archives` that create archives with glossary and model content
that follow the open metadata types.

You can create a JSON formatted archive file containing the open metadata types by running the 
[`OpenMetadataTypesArchiveWriterUtility`](../open-metadata-types-utility).

----

* [Return to Open Metadata Archives](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.