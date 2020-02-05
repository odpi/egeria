<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository relationship lifecycle test case

## Operation

This test validates that relationships can be managed through all stages of their life cycle.

## Operation

The relationship lifecycle tests extract each of the type definitions for relationships and create an instance of that
relationship (along with appropriate entities) and
drives it through each phase of its lifecycle validating that the relationship values are correctly maintained.

## Assertions

* **repository-relationship-lifecycle-01** - `typeName` new relationship created.
* **repository-relationship-lifecycle-02** - `typeName` new relationship has createdBy user.
* **repository-relationship-lifecycle-03** - `typeName` new relationship has creation time.
* **repository-relationship-lifecycle-04** - `typeName` new relationship has correct provenance type.
* **repository-relationship-lifecycle-05** - `typeName` new relationship has correct initial status.
* **repository-relationship-lifecycle-06** - `typeName` new relationship has correct type.
* **repository-relationship-lifecycle-07** - `typeName` new relationship has local metadata collection.
* **repository-relationship-lifecycle-08** - `typeName` new relationship has version greater than zero.
* **repository-relationship-lifecycle-09** - `typeName` new relationship is known.
* **repository-relationship-lifecycle-10** - `typeName` new relationship retrieved
* **repository-relationship-lifecycle-11** - `typeName` relationship deleted version number is `versionNumber`
* **repository-relationship-lifecycle-12** - `typeName` relationship no longer retrievable after delete.
* **repository-relationship-lifecycle-13** - `typeName` relationship restored version number is `versionNumber`
* **repository-relationship-lifecycle-14** - `typeName` relationship purged.


## Discovered Properties

* **`typeName` soft delete support** Enabled/Disabled - indicates whether the optional support for soft delete is enabled or not.


## Sample Output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-relationship-lifecycle",
      "testCaseName" : "Repository relationship lifecycle test case",
      "testCaseDescriptionURL" : "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/repository-relationship-lifecycle-test-case.md",
      "assertionMessage" : "Relationships can be managed through their lifecycle",
      "successfulAssertions" : [ "TermISATypeOFRelationship new relationship created.", "TermISATypeOFRelationship new relationship has createdBy user.", "TermISATypeOFRelationship new relationship has creation time.", "TermISATypeOFRelationship new relationship has correct provenance type.", "TermISATypeOFRelationship new relationship has correct initial status.", "TermISATypeOFRelationship new relationship has correct type.", "TermISATypeOFRelationship new relationship has local metadata collection.", "TermISATypeOFRelationship new relationship has version greater than zero.", "TermISATypeOFRelationship new relationship is known.", "TermISATypeOFRelationship new relationship retrieved.", "TermISATypeOFRelationship relationship deleted version number is 2", "TermISATypeOFRelationship relationship no longer retrievable after delete.", "TermISATypeOFRelationship relationship restored version number is 3", "TermISATypeOFRelationship relationship purged." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "TermISATypeOFRelationship soft delete support" : "Enabled"
      }
}
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.