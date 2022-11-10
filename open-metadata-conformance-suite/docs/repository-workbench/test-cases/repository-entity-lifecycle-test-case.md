<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity lifecycle test case

This test validates that entities can be managed through all stages of their life cycle.

## Operation

The entity lifecycle tests extract each of the type definitions for entity and create an instance of that entity and
drive it through each phase of its lifecycle validating that the entity values are correctly maintained.

## Assertions

* **repository-entity-lifecycle-01** - `typeName` new entity created.
* **repository-entity-lifecycle-02** - `typeName` new entity has createdBy user.
* **repository-entity-lifecycle-03** - `typeName` new entity has creation time.
* **repository-entity-lifecycle-04** - `typeName` new entity has correct provenance type.
* **repository-entity-lifecycle-05** - `typeName` new entity has correct initial status.
* **repository-entity-lifecycle-06** - `typeName` new entity has correct type.
* **repository-entity-lifecycle-07** - `typeName` new entity has local metadata collection.
* **repository-entity-lifecycle-08** - `typeName` new entity has version greater than zero.
* **repository-entity-lifecycle-09** - `typeName` new entity is known.
* **repository-entity-lifecycle-10** - `typeName` new entity summarized.
* **repository-entity-lifecycle-11** - `typeName` new entity retrieved.
* **repository-entity-lifecycle-12** - `typeName` new entity is unattached.
* **repository-entity-lifecycle-13** - `typeName` entity status updated.
* **repository-entity-lifecycle-14** - `typeName` entity new status is `status`
* **repository-entity-lifecycle-15** - `typeName` entity with new status version number is `versionNumber`
* **repository-entity-lifecycle-16** - `typeName` entity can not be set to DELETED status.
* **repository-entity-lifecycle-17** - `typeName` entity properties cleared to min.
* **repository-entity-lifecycle-18** - `typeName` entity with min properties version number is `versionNumber`
* **repository-entity-lifecycle-19** - `typeName` entity has properties restored.
* **repository-entity-lifecycle-20** - `typeName` entity after undo version number is `versionNumber`
* **repository-entity-lifecycle-21** - `typeName` entity deleted version number is `versionNumber`
* **repository-entity-lifecycle-22** - `typeName` entity no longer retrievable after delete.
* **repository-entity-lifecycle-23** - `typeName` entity restored version number is `versionNumber`
* **repository-entity-lifecycle-24** - `typeName` entity purged.

## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` undo support** : Enabled/Disabled - indicates whether the optional support for the undo command is enabled or not.
* **`typeName` soft delete support** Enabled/Disabled - indicates whether the optional support for soft delete is enabled or not.

## Sample Output

This is the sample output for the Asset type.

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-entity-lifecycle",
      "testCaseName" : "Repository entity lifecycle test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/repository-workbench/repository-entity-lifecycle-test-case.md",
      "assertionMessage" : "Entities can be managed through their lifecycle",
      "successfulAssertions" : [ "Asset new entity created.", "Asset new entity has createdBy user.", "Asset new entity has creation time.", "Asset new entity has correct provenance type.", "Asset new entity has correct initial status.", "Asset new entity has correct type.", "Asset new entity has local metadata collection.", "Asset new entity has version greater than zero.", "Asset new entity is known.", "Asset new entity summarized.", "Asset new entity retrieved.", "Asset new entity is unattached.", "Asset entity status updated.", "Asset entity new status is Active", "Asset entity with new status version number is 2", "Asset entity can not be set to DELETED status.", "Asset entity properties cleared to min.", "Asset entity with min properties version number is 3", "Asset entity has properties restored.", "Asset entity after undo version number is 4", "Asset entity deleted version number is 5", "Asset entity no longer retrievable after delete.", "Asset entity restored version number is 6", "Asset entity purged." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "Asset undo support" : "Enabled",
        "Asset soft delete support" : "Enabled"
      }
}
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.