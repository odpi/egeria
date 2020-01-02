<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Types

The Open Metadata Types provides the type definitions for open metadata.
They are packaged into an open metadata archive.

The open metadata types are [documented here](../../../open-metadata-publication/website/open-metadata-types).

The open metadata archive for these types is built by the `OpenMetadataTypesArchive` Java class.
It uses the Open Metadata Repository Services (OMRS) archive building
classes `OMRSArchiveBuilder` and `OMRSArchiveHelper` found in the `repository-services-archive-utilities` module.

The types for each release are managed in separate classes.
 * `OpenMetadataTypesArchive` - new candidate types for current release 
 * `OpenMetadataTypesArchive1_3` - approved types added for 1.3 release
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

You can create an archive file containing the open metadata types by running the `OpenMetadataTypesArachiveWriter`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.