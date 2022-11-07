<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Archive Store Connector

The open archive store connector provides a common interface
for managing stores of [Open Metadata Archives](../../open-metadata-archive.md).

Its interface is defined in `OpenMetadataArchiveStore`

```java
public interface OpenMetadataArchiveStore
{
    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     */
    OpenMetadataArchive getArchiveContents();


    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents  OpenMetadataArchive object
     */
    void setArchiveContents(OpenMetadataArchive archiveContents);
}
```

The open metadata archive structure is defined by `OpenMetadataArchive`.
There are 3 sections:
  * ArchiveProperties: provides details of the source and contents of the archive.
  * TypeStore: a list of new AttributeTypeDefs, new TypeDefs and patches to existing TypeDefs.
  * InstanceStore: a list of new metadata instances (Entities, Relationships and Classifications).

The definition of the connector interface for these connectors is
defined in the [repository-services-api](../../../repository-services-apis) module
in the
[org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore](https://github.com/odpi/egeria/tree/main/open-metadata-implementation/repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/connectors/stores/archivestore) Java package.

A implementations of this type of connector is located in the
[adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors](../../../../adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)
module.

## Related information

* [Open Metadata Archive Store Connectors in the Connector Catalog](https://egeria-project.org/connectors)

----
Return to [repository services connectors](.).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
