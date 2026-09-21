<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# Core Content Pack

This module implements a utility to build the technology-specific [open metadata archive](https://egeria-project.org/concepts/open-metadata-archive/):

* `CoreContentPack.omarchive`
* `EgeriaContentPack.omarchive`
* `APIsContentPack.omarchive`
* `ApacheAtlasContentPack.omarchive`
* `ApacheKafkaContentPack.omarchive`
* `DB2LUWContentPack.omarchive`
* `DuckDBContentPack.omarchive`
* `FilesContentPack.omarchive`
* `MSSQLContentPack.omarchive`
* `OpenLineageContentPack.omarchive`
* `OpenMetadataDigitalProductsContentPack.omarchive`
* `OracleContentPack.omarchive`
* `OrganizationInsightContentPack.omarchive`
* `PostgresContentPack.omarchive`
* `UnityCatalogContentPack.omarchive`

These archives contain the metadata descriptions for each of the connectors supplied by Egeria, along with associated templates and reference data.

Full details of the contents of these archives can be found in the [Content Pack Catalog](https://egeria-project.org/content-packs/).

## Checking what ships

[`ContentPackIntegrityTest`](src/test/java/org/odpi/openmetadata/contentpacks/core/ContentPackIntegrityTest.java)
holds the committed archives to the properties a correct generation gives them. It runs as part of the
normal build and needs nothing but the files in this repository.

The archives here are generated and then **committed**, and the GUID maps that keep every element's identity
stable from one generation to the next are committed separately, at the top of the repository. That split is
why this test exists: an archive writer run without its GUID map to hand does not fail, it allocates fresh
identities for content that has already shipped and reports success. A repository that has already loaded
the old pack then holds two copies of everything in it.

The checks are read-only — running the writers would rewrite the archives and extend the maps as a side
effect of testing them, which is not a thing a test may do. Instead the shipped archives are read back and
checked that:

* they **deserialize** into `OpenMetadataArchive`, the platform's own bean, so a pack the platform could not
  load fails here rather than at start-up;
* every entity's identity is **recorded in a committed GUID map** — a GUID in a pack but in no map is one
  the next generation will allocate afresh, so that element's identity is already lost;
* **one GUID means one element** — packs layer deliberately and the same element appears in several, but the
  same identity standing for two different things would merge them in any repository that loaded both;
* every relationship end **resolves** to an entity some pack carries, so loading a pack leaves no dangling
  proxy behind.

`EXPECTED_DANGLING_ENDS` is empty, and worth keeping empty. It held one entry while a long-standing gap
was diagnosed: `CoreContentPack` placed `Egeria::AuditLogDestinationConnector::SLF4J` in the Open Connectors
collection folder, but no pack carried the connector type itself — and none ever had, in any released pack.

The cause turned out to be a GUID collision. That connector type's GUID was
`e8303911-ba1c-4640-974e-c4d57ee1b310`, which is also the type GUID of the `DigitalProductDependency`
relationship. A content pack loads the open metadata types as a dependent archive, and the archive builder
keeps **one** GUID namespace for types and instances together, so it refused the entity — and
`SimpleCatalogArchiveHelper.addConnectorType` caught the refusal, assumed it meant "already defined", and
returned the GUID anyway. The writer then put that GUID in a collection membership relationship, and the
pack shipped with a member that was not there.

Both halves are fixed at source: the definition has a GUID of its own, and the helper no longer swallows
the failure. `DefinitionGUIDCollisionTest` in
[open-metadata-api-checks](../../../open-metadata-test/open-metadata-api-checks) stops another collision
reaching a pack.

The packs have since been regenerated, so the connector type is in `CoreContentPack` and no pack has a
dangling end. Two things are worth knowing about that regeneration, because they apply to the next one too:

* `OMRSArchiveGUIDMap.saveGUIDs` **aborts the run** (`System.exit(-1)`, silently) when an id's GUID changes,
  which is the guard that stops a shipped element's identity being reassigned by accident. Giving the
  definition a new GUID therefore needs the map entry updated deliberately, as part of the same change.
* The collection membership relationship that had been dangling was **re-keyed rather than dropped**, so it
  kept the GUID it shipped with. A repository that had already loaded the old pack gains the missing
  connector type and the relationship it already held becomes valid, instead of acquiring a second one.

----

* Return to [Open Metadata Archives](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.